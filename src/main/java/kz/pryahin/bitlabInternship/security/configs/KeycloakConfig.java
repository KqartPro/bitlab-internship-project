package kz.pryahin.bitlabInternship.security.configs;

import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
@Slf4j
public class KeycloakConfig {

	@Value("${keycloak.url}")
	private String url;

	@Value("${keycloak.realm}")
	private String realmName;

	@Value("${keycloak.client}")
	private String client;

	@Value("${keycloak.client-secret}")
	private String clientSecret;

	@Value("${keycloak.username}")
	private String username;

	@Value("${keycloak.password}")
	private String password;

	@Value("${keycloak.grant-type}")
	private String grantType;


	@Bean
	public Keycloak keycloak() {

		log.info("Инициализация Keycloak Bean");

		// Подключение к master-realm
		Keycloak adminKeycloak = KeycloakBuilder.builder()
				.serverUrl(url)
				.realm("master")
				.clientId("admin-cli")
				.username("admin")
				.password("admin")
				.build();


		createRealmIfNotExists(adminKeycloak);
		createClient(adminKeycloak);
		createUserWithRoles(adminKeycloak);

		return KeycloakBuilder.builder()
				.serverUrl(url)
				.realm(realmName)
				.clientId(client)
				.clientSecret(clientSecret)
				.username(username)
				.password(password)
				.grantType(grantType)
				.build();
	}


	private void createRealmIfNotExists(Keycloak adminKeycloak) {

		try {
			adminKeycloak.realm(realmName).toRepresentation();
			log.info("Realm '{}' уже существует", realmName);
		} catch (NotFoundException e) {
			log.info("Realm '{}' не найден, создаем новый", realmName);

			RealmRepresentation realmRepresentation = new RealmRepresentation();
			realmRepresentation.setRealm(realmName);
			realmRepresentation.setEnabled(true);
			realmRepresentation.setDuplicateEmailsAllowed(true);
			realmRepresentation.setLoginWithEmailAllowed(false);

			adminKeycloak.realms().create(realmRepresentation);

			adminKeycloak.realm(realmName).toRepresentation();
			log.info("Realm '{}' успешно создан", realmName);
		}
	}


	private void createClient(Keycloak adminKeycloak) {

		var clientsResource = adminKeycloak.realm(realmName).clients();

		// Проверяем, существует ли клиент
		var existingClients = clientsResource.findByClientId(client);
		if (!existingClients.isEmpty()) {
			log.info("Клиент '{}' уже существует", client);
			return;
		}

		ClientRepresentation clientRepresentation = new ClientRepresentation();
		clientRepresentation.setClientId(client);
		clientRepresentation.setSecret(clientSecret);
		clientRepresentation.setDirectAccessGrantsEnabled(true);
		clientRepresentation.setPublicClient(false);
		clientRepresentation.setServiceAccountsEnabled(true);
		clientRepresentation.setProtocol("openid-connect");

		clientsResource.create(clientRepresentation);
		log.info("Клиент '{}' успешно создан", client);
	}


	private void createUserWithRoles(Keycloak adminKeycloak) {

		var users = adminKeycloak.realm(realmName).users();

		// Проверяем, существует ли пользователь
		var existingUsers = users.search(username);
		if (!existingUsers.isEmpty()) {
			log.info("Пользователь '{}' уже существует", username);
			return;
		}

		UserRepresentation userRepresentation = new UserRepresentation();
		userRepresentation.setUsername(username);
		userRepresentation.setEnabled(true);
		userRepresentation.setEmailVerified(true);
		userRepresentation.setEmail(username + "@example.com");
		userRepresentation.setFirstName("Bitlab");
		userRepresentation.setLastName("Admin");

		CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
		credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
		credentialRepresentation.setTemporary(false);
		credentialRepresentation.setValue(password);

		userRepresentation.setCredentials(List.of(credentialRepresentation));

		var response = users.create(userRepresentation);
		if (response.getStatus() != 201) {
			log.error("Не удалось создать пользователя '{}', код ответа: {}", username, response.getStatus());
			return;
		}

		log.info("Пользователь '{}' успешно создан", username);

		// Назначаем роли пользователю
		assignRolesToUser(username, adminKeycloak);
	}


	private void assignRolesToUser(String username, Keycloak adminKeycloak) {

		var users = adminKeycloak.realm(realmName).users();
		var userId = users.search(username).get(0).getId();

		// Подключаемся к клиенту realm-management
		var clients = adminKeycloak.realm(realmName).clients();
		var realmManagementClient = clients.findByClientId("realm-management").get(0);
		var realmManagementClientId = realmManagementClient.getId();

		var rolesToAssign = List.of("manage-users", "realm-admin", "view-realm");
		var clientRoles = adminKeycloak.realm(realmName)
				.clients()
				.get(realmManagementClientId)
				.roles();

		// Получаем роли
		var roleRepresentations = rolesToAssign.stream()
				.map(clientRoles::get)
				.map(RoleResource::toRepresentation)
				.toList();

		// Назначаем роли пользователю
		users.get(userId).roles().clientLevel(realmManagementClientId).add(roleRepresentations);
		log.info("Роли {} успешно назначены пользователю '{}'", rolesToAssign, username);
	}


	@Bean
	public RestTemplate restTemplate() {

		return new RestTemplate();
	}
}
