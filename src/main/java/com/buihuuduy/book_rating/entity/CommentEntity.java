package com.buihuuduy.book_rating.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "feedback")
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentEntity extends AuditingEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "book_id")
    Integer bookId;

    @Column(name = "rating")
    Integer rating;

    @Column(name = "comment")
    String comment;

    @Column(name = "user_id")
    Integer userId;
}
