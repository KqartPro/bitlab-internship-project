package kz.pryahin.bitlabInternship.security.services.impl;

import jakarta.ws.rs.core.Response;
import kz.pryahin.bitlabInternship.security.dtos.*;
import kz.pryahin.bitlabInternship.security.services.UserService;
import kz.pryahin.bitlabInternship.security.utils.UserUtils;
import kz.pryahin.bitlabInternship.security.utils.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final Keycloak keycloak;
    private final RestTemplate restTemplate;
    private final Validator validator;

    @Value("${keycloak.token.url}")
    private String tokenUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client}")
    private String client;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Value("${keycloak.grant-type}")
    private String grantType;


    @Override
    public GetTokensDto refreshToken(RefreshTokenDto refreshToken) {

        String request = tokenUrl;
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "refresh_token");
        formData.add("client_id", client);
        formData.add("client_secret", clientSecret);
        formData.add("refresh_token", refreshToken.getRefreshToken());

        return getAccessAndRefreshTokens(request, formData);
    }


    @Override
    public GetTokensDto loginUser(LoginUserDto user) {

        String request = tokenUrl;
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", grantType);
        formData.add("client_id", client);
        formData.add("client_secret", clientSecret);
        formData.add("username", user.getUsername());
        formData.add("password", user.getPassword());

        return getAccessAndRefreshTokens(request, formData);
    }


    private GetTokensDto getAccessAndRefreshTokens(String request, MultiValueMap<String, String> formData) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        ResponseEntity<Map> response = restTemplate.postForEntity(
                request,
                new HttpEntity<>(formData, headers),
                Map.class);

        Map<String, Object> responseBody = response.getBody();

        if (!response.getStatusCode().is2xxSuccessful() || responseBody == null) {
            log.error("Error on login");
            throw new RuntimeException("Failed authentication");
        }

        String accessToken = (String) responseBody.get("access_token");
        String refreshToken = (String) responseBody.get("refresh_token");

        return new GetTokensDto(accessToken, refreshToken);
    }


//    @Override
//    public String refreshToken(RefreshTokenDto refreshToken) {
//
//        String request = tokenUrl;
//        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
//        formData.add("grant_type", "refresh_token");
//        formData.add("client_id", client);
//        formData.add("client_secret", clientSecret);
//        formData.add("refresh_token", refreshToken.getRefreshToken());
//
//        return getAccessAndRefreshTokens(request, formData);
//    }
//
//
//    @Override
//    public String loginUser(LoginUserDto user) {
//
//        String request = tokenUrl;
//        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
//        formData.add("grant_type", grantType);
//        formData.add("client_id", client);
//        formData.add("client_secret", clientSecret);
//        formData.add("username", user.getUsername());
//        formData.add("password", user.getPassword());
//
//        return getAccessAndRefreshTokens(request, formData);
//    }
//
//
//    private String getAccessAndRefreshTokens(String request, MultiValueMap<String, String> formData) {
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/x-www-form-urlencoded");
//        ResponseEntity<Map> response = restTemplate.postForEntity(
//                request,
//                new HttpEntity<>(formData, headers),
//                Map.class);
//
//        Map<String, Object> responseBody = response.getBody();
//
//        if (!response.getStatusCode().is2xxSuccessful() || responseBody == null) {
//            log.error("Error on login");
//            throw new RuntimeException("Failed authentication");
//        }
//        return "\"access_token:\" " + responseBody.get("access_token") + "\n\"refresh_token: " + responseBody.get(
//                "refresh_token");
//    }


    @Override
    public ResponseEntity<?> createUser(CreateUserDto createUserDto) {

        UserRepresentation foundUser = findUserByUsername(createUserDto.getUsername());

        if (foundUser != null) {
            log.info("User is not null");
            if (createUserDto.getUsername().equalsIgnoreCase(foundUser.getUsername())) {
                log.info("User with username {} already exists", createUserDto.getUsername());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
            }
        }


        if (createUserDto.getUsername().length() < 2 || createUserDto.getUsername().length() > 20) {
            log.error("Username is invalid");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username must contains 2-20 symbols");
        }

        if (!validator.isEmailValid(createUserDto.getEmail())) {
            log.error("Email is invalid");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is invalid");
        }

        if (!validator.isPasswordValid(createUserDto.getPassword())) {
            log.error("Password is invalid");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validator.writePasswordRulesDescription());
        }

        if (!validator.isNameValid(createUserDto.getFirstName()) || !validator.isNameValid(createUserDto.getLastName())) {
            log.error("First or last name is invalid");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validator.writeNameRulesDescription());
        }

        UserRepresentation user = setupUserRepresentation(createUserDto);

        Response response = keycloak
                .realm(realm)
                .users()
                .create(user);
        if (response.getStatus() != HttpStatus.CREATED.value()) {
            log.error("Error on creating user");
            throw new RuntimeException("Failed to create user");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("User successfully created");
    }


    @Override
    public ResponseEntity<?> changeUserPassword(ChangePasswordDto passwords) {

        String username = UserUtils.getCurrentUsername();

        LoginUserDto loginUserDto = new LoginUserDto();
        loginUserDto.setUsername(username);
        loginUserDto.setPassword(passwords.getCurrentPassword());

        GetTokensDto result = loginUser(loginUserDto);

        if (result == null) {
            log.error("Incorrect current password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect current password");
        }

        if (!validator.isPasswordValid(passwords.getNewPassword())) {
            log.error("New password is invalid");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validator.writePasswordRulesDescription());
        }

        UserRepresentation user = findUserByUsername(username);

        CredentialRepresentation credentials = new CredentialRepresentation();
        credentials.setType(CredentialRepresentation.PASSWORD);
        credentials.setValue(passwords.getNewPassword());
        credentials.setTemporary(false);

        keycloak
                .realm(realm)
                .users()
                .get(user.getId())
                .resetPassword(credentials);

        log.info("Password changed");
        return ResponseEntity.ok("Password has been changed");
    }


    @Override
    public ResponseEntity<?> setRolesToUser(UserRolesDto userRolesDto) {

        UserRepresentation foundUser = findUserByUsername(userRolesDto.getUsername());

        if (foundUser == null) {
            log.warn("User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        for (String role : userRolesDto.getRoles()) {
            if (findRoleByName(role) == null) {
                RoleRepresentation roleRepresentation = new RoleRepresentation();
                roleRepresentation.setName(role);
                keycloak.realm(realm)
                        .roles()
                        .create(roleRepresentation);

                keycloak.realm(realm)
                        .users()
                        .get(foundUser.getId())
                        .roles()
                        .realmLevel()
                        .add(List.of(keycloak.realm(realm).roles().get(role).toRepresentation()));
            } else {
                keycloak.realm(realm)
                        .users()
                        .get(foundUser.getId())
                        .roles()
                        .realmLevel()
                        .add(List.of(keycloak.realm(realm).roles().get(role).toRepresentation()));
            }
        }
        return ResponseEntity.ok("Success. Roles set");
    }


    @Override
    public ResponseEntity<?> updateUserData(UpdateUserDto updateUserDto) {

        String username = UserUtils.getCurrentUsername();
        UserRepresentation user = findUserByUsername(username);

        if (user == null) {
            log.error("Authentication Error - User to update data not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Authentication Error");
        }

        if (updateUserDto.getEmail() != null) {
            if (!validator.isEmailValid(updateUserDto.getEmail())) {
                log.error("Email '{}' is invalid", updateUserDto.getEmail());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is invalid");
            }
            user.setEmail(updateUserDto.getEmail());
        }

        if (updateUserDto.getFirstName() != null) {
            if (!validator.isNameValid(updateUserDto.getFirstName())) {
                log.error("First name '{}' is invalid", updateUserDto.getFirstName());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validator.writeNameRulesDescription());
            }
            user.setFirstName(updateUserDto.getFirstName());
        }

        if (updateUserDto.getLastName() != null) {
            if (!validator.isNameValid(updateUserDto.getLastName())) {
                log.error("Last name '{}' is invalid", updateUserDto.getLastName());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validator.writeNameRulesDescription());
            }
            user.setLastName(updateUserDto.getLastName());
        }

        keycloak.realm(realm)
                .users()
                .get(user.getId())
                .update(user);
        return ResponseEntity.ok("Success. User data updated");
    }


    @Override
    public ResponseEntity<?> deleteUserRoles(UserRolesDto userRolesDto) {

        UserRepresentation foundUser = findUserByUsername(userRolesDto.getUsername());

        if (foundUser == null) {
            log.error("User to delete roles not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        for (String role : userRolesDto.getRoles()) {
            if (findRoleByName(role) != null) {
                keycloak.realm(realm)
                        .users()
                        .get(foundUser.getId())
                        .roles()
                        .realmLevel()
                        .remove(List.of(keycloak.realm(realm).roles().get(role).toRepresentation()));
            }
        }
        log.info("Roles deleted");
        return ResponseEntity.ok("Success. Roles deleted");
    }


    // Вспомогательные методы
    @Override
    public UserRepresentation setupUserRepresentation(CreateUserDto createUserDto) {

        UserRepresentation user = new UserRepresentation();
        user.setEmail(createUserDto.getEmail());
        user.setEmailVerified(true);
        user.setEnabled(true);
        user.setUsername(createUserDto.getUsername());
        user.setFirstName(createUserDto.getFirstName());
        user.setLastName(createUserDto.getLastName());

        CredentialRepresentation credentials = new CredentialRepresentation();
        credentials.setType(CredentialRepresentation.PASSWORD);
        credentials.setValue(createUserDto.getPassword());
        credentials.setTemporary(false);

        user.setCredentials(List.of(credentials));
        return user;
    }


    @Override
    public UserRepresentation findUserByUsername(String username) {

        List<UserRepresentation> foundUsers = keycloak.realm(realm)
                .users()
                .search(username);

        Optional<UserRepresentation> user = foundUsers.stream()
                .filter(u -> username.equalsIgnoreCase(u.getUsername()))
                .findFirst();

        if (user.isEmpty()) {
            log.info("User '{}' not found", username);
            return null;
        }
        return user.get();
    }


    @Override
    public RoleRepresentation findRoleByName(String name) {

        List<RoleRepresentation> foundRoles = keycloak.realm(realm)
                .roles()
                .list();

        Optional<RoleRepresentation> role = foundRoles.stream()
                .filter(r -> name.equals(r.getName()))
                .findFirst();

        if (role.isEmpty()) {
            log.info("Role '{}' not found", name);
            return null;
        }

        return role.get();
    }
}
