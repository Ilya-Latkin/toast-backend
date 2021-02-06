package com.ngteam.toastapp.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String email;
    @Column
    private String name;
    @Column
    private String password;
    @OneToMany(mappedBy = "user")
    private List<Event> userEvent;
    @OneToMany(mappedBy = "user")
    private List<EventType> userEventType;
    @OneToMany(mappedBy = "user")
    private List<Category> userCategory;


    public User() {
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
