package org.bimbimbambam.hacktemplate.entity;

import jakarta.persistence.*;

@Entity
@Table
public class Category extends AbstractEntity {
    @Column(nullable = false)
    private String name;

    @Column
    private String avatar;
}
