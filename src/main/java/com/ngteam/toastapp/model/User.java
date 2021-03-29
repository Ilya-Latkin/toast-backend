package com.ngteam.toastapp.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@Builder
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
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<EventType> userEventType;
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Category> userCategory;
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Event> userEvent;
    @Column
    @Enumerated(value = EnumType.STRING)
    private State state;
    @Column
    private String confirmCode;
}