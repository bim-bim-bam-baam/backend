package org.bimbimbambam.hacktemplate.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String avatar;
}