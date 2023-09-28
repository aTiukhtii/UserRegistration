package com.example.clearsolutiontesttask.controller;

import com.example.clearsolutiontesttask.dto.UserRegistrationRequestDto;
import com.example.clearsolutiontesttask.dto.UserResponseDto;
import com.example.clearsolutiontesttask.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "User Controller", description = "API for managing users")
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register new user")
    @ApiResponse(responseCode = "201",
            description = "User registered successfully",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDto.class))})
    public UserResponseDto registerUser(
            @RequestBody @Valid UserRegistrationRequestDto requestDto) {
        return userService.register(requestDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user")
    @ApiResponse(responseCode = "200",
            description = "User updated successfully",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDto.class))})
    public UserResponseDto updateUser(@PathVariable Long id,
                                      @RequestBody @Valid UserRegistrationRequestDto request) {
        return userService.update(id, request);
    }

    @GetMapping("/search")
    @Operation(summary = "Search users by birth date")
    @ApiResponse(responseCode = "200",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDto.class))})
    public List<UserResponseDto> searchUsers(@RequestParam LocalDate from,
                                             @RequestParam LocalDate to) {
        return userService.searchByAge(from, to);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete user by id")
    @ApiResponse(responseCode = "204",
            description = "User deleted successfully")
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }
}
