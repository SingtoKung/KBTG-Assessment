package com.kbtg.bootcamp.posttest.lottery;

import com.kbtg.bootcamp.posttest.user_ticket.UserTicket;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Set;

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

//    @ManyToOne
//    @JoinColumn(name = "user_ticket")
//    private UserTicket userTicket;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH })
    @JoinTable(name = "user_ticket_mapping", joinColumns = @JoinColumn(name = "lottery_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<UserTicket> userTickets;

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

    public Set<UserTicket> getUserTickets() {
        return userTickets;
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

    public void setUserTickets(Set<UserTicket> userTickets) {
        this.userTickets = userTickets;
    }
}
