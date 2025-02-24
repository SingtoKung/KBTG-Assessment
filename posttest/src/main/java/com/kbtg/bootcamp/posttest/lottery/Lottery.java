package com.kbtg.bootcamp.posttest.lottery;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "lottery")
public class Lottery {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Id
    @Column(length = 6)
    @Size(min = 6, max = 6)
    @NotNull
    @Pattern(regexp = "[\\d]{6}")
    private String ticket;

    @NotNull
    @Min(value = 1)
    private int price;

    @NotNull
    @Min(value = 0)
    private int amount;

    public Lottery() {}

    public Lottery(String ticket, int price, int amount) {
        this.ticket = ticket;
        this.price = price;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public String getTicket() {
        return ticket;
    }

    public int getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

}
