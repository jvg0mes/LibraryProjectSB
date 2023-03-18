package com.project.library.Models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name="accounts")
public class Account{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne @NotNull
    private Customer customer;
    @NotNull @NotEmpty
    private String username;
    @NotNull @NotEmpty
    private String password;

    public Account(Customer customer, String username, String password) {
        this.customer = customer;
        this.username = username;
        this.password = password;
    }

}
