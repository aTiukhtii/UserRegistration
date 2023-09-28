package com.example.clearsolutiontesttask.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserRegistrationRequestDto {
    @Email
    @Size(min = 8, max = 50)
    private String email;
    @NotBlank
    @Size(min = 2, max = 35)
    private String firstName;
    @NotBlank
    @Size(min = 2, max = 35)
    private String lastName;
    @Size(min = 5, max = 100)
    private String address;
    @Past
    @NotBlank
    private LocalDate birthDate;
    @Pattern(regexp = "^\\+?\\d{9,15}$", message = "Invalid phone number format")
    private String phoneNumber;
}
