package com.example.clearsolutiontesttask.repository;

import com.example.clearsolutiontesttask.model.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByPhoneNumber(String phoneNumber);

    List<User> findAllByBirthDateBetween(LocalDate from, LocalDate to);
}
