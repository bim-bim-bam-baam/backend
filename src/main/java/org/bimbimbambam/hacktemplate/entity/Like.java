package org.bimbimbambam.hacktemplate.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    @ManyToOne
    private User fromId;

    @Column(nullable = false)
    @ManyToOne
    private User toId;
}
