package com.example.clearsolutiontesttask.service;

import com.example.clearsolutiontesttask.dto.UserRegistrationRequestDto;
import com.example.clearsolutiontesttask.dto.UserResponseDto;
import com.example.clearsolutiontesttask.exception.EntityNotFoundException;
import com.example.clearsolutiontesttask.exception.RegistrationException;
import com.example.clearsolutiontesttask.mapper.UserMapper;
import com.example.clearsolutiontesttask.model.User;
import com.example.clearsolutiontesttask.repository.UserRepository;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Value("${user.age}")
    private int minAge;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        checkAge(requestDto);
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("this email is already in use");
        }
        if (userRepository.findByPhoneNumber(requestDto.getPhoneNumber()).isPresent()) {
            throw new RegistrationException("this phone number is already in use");
        }
        return userMapper.toDto(userRepository.save(userMapper.toModel(requestDto)));
    }

    @Override
    public UserResponseDto update(Long id, UserRegistrationRequestDto requestDto) {
        checkAge(requestDto);
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("couldn't update user with id: + " + id);
        }
        User userFromDb = userMapper.toModel(requestDto);
        userFromDb.setId(id);
        return userMapper.toDto(userRepository.save(userFromDb));
    }

    @Override
    public List<UserResponseDto> searchByAge(LocalDate from, LocalDate to) {
        if (!from.isBefore(to)) {
            throw new IllegalArgumentException("The 'from' date must be before the 'to' date");
        }
        return userRepository.findAllByBirthDateBetween(from, to)
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Couldn't delete user with id: " + id);
        }
        userRepository.deleteById(id);
    }

    private void checkAge(UserRegistrationRequestDto requestDto) {
        LocalDate currentDate = LocalDate.now();
        LocalDate birthDate = requestDto.getBirthDate();
        if (currentDate.isBefore(birthDate)) {
            throw new IllegalArgumentException("Please enter the correct date of birth");
        }
        int age = Period.between(birthDate, currentDate).getYears();
        if (age < minAge) {
            throw new IllegalArgumentException("You must be at least " + minAge + " years old");
        }
    }
}
