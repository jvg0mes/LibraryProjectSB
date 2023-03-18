package com.project.library.Models;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name="sessions")
@Getter
public class Session {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @ManyToOne
    private Account account;

    public Session(Account account) {
        this.account = account;
    }
}
