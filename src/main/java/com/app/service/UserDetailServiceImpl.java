package com.app.service;

import com.app.model.User;
import com.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

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
}
