package com.khanhvan.book_rating.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEntity extends AuditingEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "username")
    String username;

    @Column(name = "password")
    String password;

    @Column(name = "user_image")
    String userImage;

    @Column(name = "full_name")
    String fullName;

    @Column(name = "user_DOB")
    LocalDate userDOB;

    @Column(name = "user_address")
    String userAddress;

    @Column(name = "user_gender")
    Boolean userGender;

    @Column(name = "user_phone")
    String userPhone;

    @Column(name = "user_email")
    String userEmail;

    @Column(name = "user_link")
    String userLink;

    @Column(name = "is_admin")
    Boolean isAdmin;
}
