package kz.pryahin.bitlabInternship.security.services;

import kz.pryahin.bitlabInternship.security.dtos.*;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;

public interface UserService {

	String refreshToken(RefreshTokenDto refreshToken);

	String loginUser(LoginUserDto user);

	ResponseEntity<?> createUser(CreateUserDto createUserDto);

	ResponseEntity<?> changeUserPassword(ChangePasswordDto passwords);

	ResponseEntity<?> setRolesToUser(UserRolesDto userRolesDto);

	ResponseEntity<?> updateUserData(UpdateUserDto updateUserDto);

	ResponseEntity<?> deleteUserRoles(UserRolesDto userRolesDto);

	UserRepresentation setupUserRepresentation(CreateUserDto createUserDto);

	UserRepresentation findUserByUsername(String username);

	RoleRepresentation findRoleByName(String name);

}
