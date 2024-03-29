package com.ngteam.toastapp.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "event_types")
public class  EventType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String name;
    @OneToMany(mappedBy = "eventType")
    private List<Event> events;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public EventType() {}

    public EventType(String name, User user) {
        this.name = name;
        this.user = user;
    }
}
