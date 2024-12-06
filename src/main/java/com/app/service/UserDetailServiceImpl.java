package com.app.service;

import com.app.controller.dto.AuthCreateUserRequest;
import com.app.controller.dto.AuthLoginRequest;
import com.app.controller.dto.AuthResponse;
import com.app.model.Role;
import com.app.model.User;
import com.app.repositories.RoleRepository;
import com.app.repositories.UserRepository;
import com.app.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user= userRepository.findUserByUserName(username)
                .orElseThrow(() ->  new UsernameNotFoundException(" El usuario " + username + " no existe "));

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        user.getRoles()
                .forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));

        user.getRoles().stream()
                .flatMap(role -> role.getPermissionList().stream())
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getPermissionEnum().name())));

        return  new org.springframework.security.core.userdetails.User(user.getUserName(),
                user.getPassword(),
                user.isEnable(),
                user.isAccountNoExpired(),
                user.isCredentialNoExpired(),
                user.isAccountNoLocked(),
                authorityList);

    }

    public AuthResponse loginUser(AuthLoginRequest authLoginRequest){

        String username = authLoginRequest.username();
        String password = authLoginRequest.password();

        Authentication authentication = this.authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.createToken(authentication);

        AuthResponse authResponse = new AuthResponse(username, "User logged successfully",accessToken,true);
        return  authResponse;

    }

    public Authentication authenticate(String username, String password){
        UserDetails userDetails = this.loadUserByUsername(username);

        if(userDetails == null){
            throw new BadCredentialsException("Invalid username or password.");

        }

        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("Invalid password.");
        }

        return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());

    }

    public AuthResponse createUser(AuthCreateUserRequest authCreateUserRequest) throws IllegalAccessException {
        String username = authCreateUserRequest.username();
        String password = authCreateUserRequest.password();
        List<String> roleRequest = authCreateUserRequest.roleRequest().roleListName();

        Set<Role>  roleSet= roleRepository.findRoleByRoleEnumIn(roleRequest).stream().collect(Collectors.toSet());

        if(roleSet.isEmpty()){
            throw new IllegalAccessException("The roles specified does not exist");
        }

        User user = User.builder()
                .userName(username)
                .password(passwordEncoder.encode(password))
                .roles(roleSet)
                .isEnable(true)
                .accountNoLocked(true)
                .accountNoExpired(true)
                .credentialNoExpired(true)
                .build();
        User userCreated = userRepository.save(user);

        ArrayList<SimpleGrantedAuthority> authorityList= new ArrayList<>();

        userCreated.getRoles().forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));

        userCreated.getRoles()
                .stream()
                .flatMap(role -> role.getPermissionList().stream())
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getPermissionEnum().name())));

        //SecurityContext context= SecurityContextHolder.getContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userCreated.getUserName(),userCreated.getPassword(),authorityList);

        String accessToken = jwtUtils.createToken(authentication);

        AuthResponse authResponse = new AuthResponse(userCreated.getUserName(),"User created successfully",accessToken,true);


        return authResponse;
    }


}


