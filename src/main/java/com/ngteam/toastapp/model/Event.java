package com.ngteam.toastapp.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    @ManyToOne(cascade = CascadeType.DETACH)
    private Category category;
    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;
    @ManyToMany
    private List<User> users;
}