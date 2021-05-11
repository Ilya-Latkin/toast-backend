package com.ngteam.toastapp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "temporary_code")
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String token;
    @Column
    private LocalDateTime created;
    @Column
    private LocalDateTime expired;
    @Column
    private LocalDateTime confirmed;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public ConfirmationToken(String token, LocalDateTime created,
                             LocalDateTime expired, User user) {
        this.token = token;
        this.created = created;
        this.expired = expired;
        this.user = user;
    }
}
