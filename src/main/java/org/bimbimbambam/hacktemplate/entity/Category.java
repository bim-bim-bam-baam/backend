package org.bimbimbambam.hacktemplate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table
public class Category extends AbstractEntity {
    @Column(nullable = false)
    private String name;

    @Column
    private String avatar;

    @Column
    private Long questionCount = 0L;
}
