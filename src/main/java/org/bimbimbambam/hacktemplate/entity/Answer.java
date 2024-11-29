package org.bimbimbambam.hacktemplate.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    @ManyToOne
    private Question questionId;

    @Column(nullable = false)
    @ManyToOne
    private User userId;

    @Column(nullable = false)
    private Integer answer;
}
