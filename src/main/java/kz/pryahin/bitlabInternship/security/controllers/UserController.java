package kz.pryahin.bitlabInternship.security.controllers;

import io.swagger.v3.oas.annotations.Operation;
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


    @Operation(summary = "Создает пользователя в базе данных")
    @PostMapping("/create")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserDto user) {

        return userServiceImpl.createUser(user);
    }


    @Operation(summary = "Авторизация пользователя")
    @PostMapping("/login")
    public GetTokensDto loginUser(@Valid @RequestBody LoginUserDto user) {

        return userServiceImpl.loginUser(user);
    }


    @Operation(summary = "Обновляет токен пользователя")
    @PostMapping("/refresh-token")
    public GetTokensDto refreshToken(@Valid @RequestBody RefreshTokenDto refreshToken) {

        return userServiceImpl.refreshToken(refreshToken);
    }


    @Operation(summary = "Изменяет пароль пользователя")
    @PutMapping("/change-password")
    public ResponseEntity<?> changeUserPassword(@Valid @RequestBody ChangePasswordDto passwords) {

        return userServiceImpl.changeUserPassword(passwords);
    }


    @Operation(summary = "Задает роли пользователю")
    @PutMapping("/set-roles")
    public ResponseEntity<?> addRolesToUser(@Valid @RequestBody UserRolesDto userRolesDto) {

        return userServiceImpl.setRolesToUser(userRolesDto);
    }


    @Operation(summary = "Обновляет данные о пользователе")
    @PatchMapping("/update")
    public ResponseEntity<?> updateUserData(@Valid @RequestBody UpdateUserDto updateUserDto) {

        return userServiceImpl.updateUserData(updateUserDto);
    }


    @Operation(summary = "Удаляет роли и пользователя")
    @DeleteMapping("delete-roles")
    public ResponseEntity<?> deleteUserRoles(@Valid @RequestBody UserRolesDto userRolesDto) {

        return userServiceImpl.deleteUserRoles(userRolesDto);
    }
}
