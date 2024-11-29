package org.bimbimbambam.hacktemplate.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table
public class UserCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    @ManyToOne
    private Question userId;

    @Column(nullable = false)
    @ManyToOne
    private User categoryId;

    @Column(nullable = false)
    private Integer cnt = 0;
}
