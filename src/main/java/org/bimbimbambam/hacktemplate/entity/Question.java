package org.bimbimbambam.hacktemplate.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table
public class Question extends AbstractEntity {
    @Column(nullable = false)
    private String content;

    @Column
    private String image;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers;
}
