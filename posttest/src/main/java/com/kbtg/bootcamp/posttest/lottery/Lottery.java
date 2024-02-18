package com.kbtg.bootcamp.posttest.lottery;

import com.kbtg.bootcamp.posttest.user_ticket.UserTicket;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "lottery")
public class Lottery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

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

    @ManyToOne
    @JoinColumn(name = "user_ticket")
    private UserTicket userTicket;

    public Lottery() {

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

    public UserTicket getUserTicket() {
        return userTicket;
    }

    public void setUserTicket(UserTicket userTicket) {
        this.userTicket = userTicket;
    }
}
