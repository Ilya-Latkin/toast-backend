package com.ngteam.toastapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
    @JoinColumn(name = "types_id")
    @ManyToOne
    private EventType eventType;
    @JoinColumn(name = "categories_id")
    @ManyToOne
    private Category category;
    @JoinColumn(name = "user_id")
    @ManyToOne
    @JsonIgnore
    private User user;

    public Event() { }

    public Event(String name, String description, Date date, EventType eventType, Category category) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.eventType = eventType;
        this.category = category;
    }
}