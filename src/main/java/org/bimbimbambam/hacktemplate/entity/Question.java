package org.bimbimbambam.hacktemplate.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column
    private String image;
}
