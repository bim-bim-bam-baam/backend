package org.bimbimbambam.hacktemplate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table
public class QuestionInQueue extends AbstractEntity {
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String answerLeft;

    @Column(nullable = false)
    private String answerRight;

    @Column
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
