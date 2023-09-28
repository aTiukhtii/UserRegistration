package com.example.clearsolutiontesttask.service;

import com.example.clearsolutiontesttask.dto.UserRegistrationRequestDto;
import com.example.clearsolutiontesttask.dto.UserResponseDto;
import java.time.LocalDate;
import java.util.List;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto);

    UserResponseDto update(Long id, UserRegistrationRequestDto requestDto);

    List<UserResponseDto> searchByAge(LocalDate from, LocalDate to);

    void delete(Long id);
}
