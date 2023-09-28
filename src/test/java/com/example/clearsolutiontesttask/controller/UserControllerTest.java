package com.example.clearsolutiontesttask.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.clearsolutiontesttask.dto.UserRegistrationRequestDto;
import com.example.clearsolutiontesttask.dto.UserResponseDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .build();
    }

    @Test
    @Sql(scripts = "classpath:database/create_two_users.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/delete_two_users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void registerUser_OK() throws Exception {
        String request = objectMapper.writeValueAsString(registerTestUser());
        MvcResult mvcResult = mockMvc.perform(
                        post("/users")
                                .content(request)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        UserResponseDto actual = objectMapper.readValue(mvcResult.getResponse()
                .getContentAsString(), UserResponseDto.class);
        assertNotNull(actual);
        UserResponseDto expected = createUserResponseDto(registerTestUser());
        assertEquals(expected, actual);
    }

    @Test
    @Sql(scripts = "classpath:database/create_two_users.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/delete_two_users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void registerUser_DuplicateEmail() throws Exception {
        String request = objectMapper.writeValueAsString(registerTestUser()
                .setEmail("user1@example.com"));
        mockMvc.perform(
                post("/users")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(scripts = "classpath:database/create_two_users.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/delete_two_users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void registerUser_DuplicatePhoneNumber() throws Exception {
        String request = objectMapper.writeValueAsString(registerTestUser()
                .setPhoneNumber("+1234567890"));
        mockMvc.perform(
                        post("/users")
                                .content(request)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(scripts = "classpath:database/create_two_users.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/delete_two_users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void registerUser_AgeUnder18() throws Exception {
        String request = objectMapper.writeValueAsString(registerTestUser()
                .setBirthDate(LocalDate.now()));
        mockMvc.perform(
                        post("/users")
                                .content(request)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(scripts = "classpath:database/create_two_users.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/delete_two_users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateUser_Ok() throws Exception {
        String request = objectMapper.writeValueAsString(registerTestUser());
        long userId = 1;
        MvcResult mvcResult = mockMvc.perform(
                        put("/users/{id}", userId)
                                .content(request)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        UserResponseDto actual = objectMapper.readValue(mvcResult.getResponse()
                .getContentAsString(), UserResponseDto.class);
        assertNotNull(actual);
        UserResponseDto expected = createUserResponseDto(registerTestUser()).setId(userId);
        assertEquals(expected, actual);
    }

    @Test
    @Sql(scripts = "classpath:database/create_two_users.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/delete_two_users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateUser_WithWrongID_99() throws Exception {
        String request = objectMapper.writeValueAsString(registerTestUser());
        long userId = 99;
        mockMvc.perform(
                put("/users/{id}", userId)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(scripts = "classpath:database/create_two_users.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/delete_two_users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void searchUsers_Ok() throws Exception {
        LocalDate from = LocalDate.of(1990, 1, 1);
        LocalDate to = LocalDate.of(2000, 12, 31);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/users/search")
                        .param("from", from.toString())
                        .param("to", to.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        List<UserResponseDto> users = objectMapper
                .readValue(responseContent, new TypeReference<>() {});

        assertEquals(1, users.size());
    }

    @Test
    @Sql(scripts = "classpath:database/create_two_users.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/delete_two_users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void searchUsers_InvalidDate() throws Exception {
        LocalDate to = LocalDate.of(1990, 1, 1);
        LocalDate from = LocalDate.of(2000, 12, 31);
        mockMvc.perform(get("/users/search")
                        .param("from", from.toString())
                        .param("to", to.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(scripts = "classpath:database/create_two_users.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/delete_two_users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteUser_Ok() throws Exception {
        long userId = 1;
        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isNoContent());
    }

    @Test
    @Sql(scripts = "classpath:database/create_two_users.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/delete_two_users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteUser_WithWrongId_999() throws Exception {
        long userId = 999;
        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isNotFound());
    }

    private static UserRegistrationRequestDto registerTestUser() {
        return new UserRegistrationRequestDto()
                .setEmail("test@example.com")
                .setFirstName("John")
                .setLastName("Doe")
                .setAddress("123 Main St")
                .setBirthDate(LocalDate.of(1990, 1, 1))
                .setPhoneNumber("+1234567899");
    }

    private static UserResponseDto createUserResponseDto(UserRegistrationRequestDto dto) {
        return new UserResponseDto()
                .setId(3L)
                .setEmail(dto.getEmail())
                .setFirstName(dto.getFirstName())
                .setLastName(dto.getLastName())
                .setBirthDate(dto.getBirthDate())
                .setPhoneNumber(dto.getPhoneNumber())
                .setAddress(dto.getAddress());
    }
}
