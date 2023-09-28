package com.example.clearsolutiontesttask.mapper;

import com.example.clearsolutiontesttask.config.MapperConfig;
import com.example.clearsolutiontesttask.dto.UserRegistrationRequestDto;
import com.example.clearsolutiontesttask.dto.UserResponseDto;
import com.example.clearsolutiontesttask.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    User toModel(UserRegistrationRequestDto requestDto);
}
