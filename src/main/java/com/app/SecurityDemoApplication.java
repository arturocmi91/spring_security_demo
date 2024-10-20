package com.app;

import com.app.Enums.PermissionEnum;
import com.app.Enums.RoleEnum;
import com.app.model.Permission;
import com.app.model.Role;
import com.app.model.User;
import com.app.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Set;

@SpringBootApplication
public class SecurityDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityDemoApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository userRepository){
		return  args -> {
			//Crear Permisos
			Permission createPermission = Permission.builder()
					.permissionEnum(PermissionEnum.CREATE)
					.build();
			Permission readPermission = Permission.builder()
					.permissionEnum(PermissionEnum.READ)
					.build();
			Permission updatePermission = Permission.builder()
					.permissionEnum(PermissionEnum.UPDATE)
					.build();
			Permission deletePermission = Permission.builder()
					.permissionEnum(PermissionEnum.DELETE)
					.build();
			Permission refactorPermission = Permission.builder()
					.permissionEnum(PermissionEnum.REFACTOR)
					.build();
			//Crear Roles

			Role roleAdmin = Role.builder()
					.roleEnum(RoleEnum.ADMIN)
					.permissionList(Set.of(createPermission, readPermission, updatePermission, deletePermission))
					.build();

			Role roleUser = Role.builder()
					.roleEnum(RoleEnum.USER)
					.permissionList(Set.of(createPermission, readPermission))
					.build();

			Role roleInvited = Role.builder()
					.roleEnum(RoleEnum.INVITED)
					.permissionList(Set.of( readPermission))
					.build();

			Role roleDeveloper= Role.builder()
					.roleEnum(RoleEnum.DEVELOPER)
					.permissionList(Set.of(createPermission, readPermission, updatePermission, deletePermission, refactorPermission))
					.build();

			//Crear Users

			User userArturo= User.builder()
					.userName("arturo")
					.password("$2a$10$QWPvTAJ4P6XrlWkpEJ0EQuM6zEdMlUVJ8JB3jggA0/sP5p1ODnxZC")
					.isEnable(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.roles(Set.of(roleDeveloper))
					.build();

			User userMagna= User.builder()
					.userName("magna")
					.password("$2a$10$QWPvTAJ4P6XrlWkpEJ0EQuM6zEdMlUVJ8JB3jggA0/sP5p1ODnxZC")
					.isEnable(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.roles(Set.of(roleAdmin))
					.build();
			User userGreta= User.builder()
					.userName("greta")
					.password("$2a$10$QWPvTAJ4P6XrlWkpEJ0EQuM6zEdMlUVJ8JB3jggA0/sP5p1ODnxZC")
					.isEnable(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.roles(Set.of(roleUser))
					.build();

			User userMarco= User.builder()
					.userName("marco")
					.password("$2a$10$QWPvTAJ4P6XrlWkpEJ0EQuM6zEdMlUVJ8JB3jggA0/sP5p1ODnxZC")
					.isEnable(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.roles(Set.of(roleInvited))
					.build();

			userRepository.saveAll(List.of(userArturo, userMagna, userGreta, userMarco));

		};
	}
}
