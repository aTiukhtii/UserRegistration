package com.example.clearsolutiontesttask.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Data
@Entity
@SQLDelete(sql = "UPDATE users SET is_deleted = TRUE WHERE id=?")
@Where(clause = "is_deleted=false")
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Past
    @Column(nullable = false)
    private LocalDate birthDate;
    @Column(unique = true, length = 15)
    private String phoneNumber;
    private String address;
    @Column(nullable = false)
    private boolean isDeleted = false;
}
