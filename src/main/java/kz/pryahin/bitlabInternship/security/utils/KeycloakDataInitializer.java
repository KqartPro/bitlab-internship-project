package kz.pryahin.bitlabInternship.security.utils;

import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.core.Response;
import kz.pryahin.bitlabInternship.security.dtos.CreateUserDto;
import kz.pryahin.bitlabInternship.security.dtos.UserRolesDto;
import kz.pryahin.bitlabInternship.security.services.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class KeycloakDataInitializer {
	private final Keycloak keycloak;
	private final UserServiceImpl userServiceImpl;

	@Value("${keycloak.realm}")
	private String realm;

	@Value("${keycloak.url}")
	private String url;


	@PostConstruct
	public void addInitialUsers() {

		CreateUserDto teacher = new CreateUserDto();
		teacher.setUsername("teacher");
		teacher.setEmail("teacher@gmail.com");
		teacher.setFirstName("Bitlab");
		teacher.setLastName("Teacher");
		teacher.setPassword("!TEacherPass1");

		CreateUserDto user = new CreateUserDto();
		user.setUsername("user");
		user.setEmail("user@gmail.com");
		user.setFirstName("Filling");
		user.setLastName("User");
		user.setPassword("!USerPass1");

		CreateUserDto admin = new CreateUserDto();
		admin.setUsername("admin");
		admin.setEmail("admin@gmail.com");
		admin.setFirstName("Filling");
		admin.setLastName("Admin");
		admin.setPassword("!ADminPass1");

		createInitialUser(teacher);
		createInitialUser(user);
		createInitialUser(admin);

		UserRolesDto userRolesDto1 = new UserRolesDto();
		userRolesDto1.setUsername(teacher.getUsername());
		userRolesDto1.setRoles(List.of("TEACHER"));

		UserRolesDto userRolesDto2 = new UserRolesDto();
		userRolesDto2.setUsername(user.getUsername());
		userRolesDto2.setRoles(List.of("USER"));

		UserRolesDto userRolesDto3 = new UserRolesDto();
		userRolesDto3.setUsername(admin.getUsername());
		userRolesDto3.setRoles(List.of("ADMIN"));

		userServiceImpl.setRolesToUser(userRolesDto1);
		userServiceImpl.setRolesToUser(userRolesDto2);
		userServiceImpl.setRolesToUser(userRolesDto3);
	}


	private void createInitialUser(CreateUserDto user) {

		UserRepresentation foundUser = userServiceImpl.findUserByUsername(user.getUsername());

		if (foundUser != null) {
			log.info("User with username {} already exists", user.getUsername());
			return;
		}

		UserRepresentation userRepresentation = userServiceImpl.setupUserRepresentation(user);

		Response response = keycloak
				.realm(realm)
				.users()
				.create(userRepresentation);

		if (response.getStatus() != HttpStatus.CREATED.value()) {
			log.error("Error on creating initial user");
		}

	}
}

