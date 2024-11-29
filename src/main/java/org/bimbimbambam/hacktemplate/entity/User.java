package org.bimbimbambam.hacktemplate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name="users")
public class User extends AbstractEntity {
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column
    private String avatar;

    @Column(nullable = false)
    private String roles = "USER";

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers;

    @OneToMany(mappedBy = "fromUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chat> likesSent;

    @OneToMany(mappedBy = "toUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chat> likesReceived;
}
