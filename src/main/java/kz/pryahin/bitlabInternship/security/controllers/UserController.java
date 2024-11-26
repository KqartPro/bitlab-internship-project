package kz.pryahin.bitlabInternship.security.controllers;

import jakarta.validation.Valid;
import kz.pryahin.bitlabInternship.security.dtos.*;
import kz.pryahin.bitlabInternship.security.services.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
	private final UserServiceImpl userServiceImpl;


	@PostMapping("/create")
	public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserDto user) {

		return userServiceImpl.createUser(user);
	}


	@PostMapping("/login")
	public String loginUser(@Valid @RequestBody LoginUserDto user) {

		return userServiceImpl.loginUser(user);
	}


	@PostMapping("/refresh-token")
	public String refreshToken(@Valid @RequestBody RefreshTokenDto refreshToken) {

		return userServiceImpl.refreshToken(refreshToken);
	}


	@PutMapping("/change-password")
	public ResponseEntity<?> changeUserPassword(@Valid @RequestBody ChangePasswordDto passwords) {

		return userServiceImpl.changeUserPassword(passwords);
	}


	@PutMapping("/set-roles")
	public ResponseEntity<?> addRolesToUser(@Valid @RequestBody UserRolesDto userRolesDto) {

		return userServiceImpl.setRolesToUser(userRolesDto);
	}


	@PatchMapping("/update")
	public ResponseEntity<?> updateUserData(@Valid @RequestBody UpdateUserDto updateUserDto) {

		return userServiceImpl.updateUserData(updateUserDto);
	}


	@DeleteMapping("delete-roles")
	public ResponseEntity<?> deleteUserRoles(@Valid @RequestBody UserRolesDto userRolesDto) {

		return userServiceImpl.deleteUserRoles(userRolesDto);
	}
}
