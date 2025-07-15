package com.example.shop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;


    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    private Cart cart;


    @Column(name = "user_name")
    private String name;

    @Column(name = "user_login", unique = true, nullable = false)
    private String login;

    @Column(name = "user_password")
    private String password;

    @Column(name = "user_cash_balance")
    private Integer userCashBalance = 0;


    public User() {
    }

    public User(Cart cart, String name, String login, String password, Integer userCashBalance) {
        this.cart = cart;
        this.name = name;
        this.login = login;
        this.password = password;
        this.userCashBalance = userCashBalance;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getCashBalance() {
        return userCashBalance;
    }

    public void setCashBalance(Integer userCashBalance) {
        this.userCashBalance = userCashBalance;
    }
}

