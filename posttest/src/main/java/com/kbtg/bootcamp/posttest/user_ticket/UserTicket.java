package com.kbtg.bootcamp.posttest.user_ticket;

import com.kbtg.bootcamp.posttest.lottery.Lottery;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.util.Set;

@Entity
@Table(name = "user_ticket")
public class UserTicket {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Id
    @NotNull
    @Size(min = 10, max = 10)
    @Pattern(regexp = "[\\d]{10}")
    private String userID;

    @PositiveOrZero
    @Column(name = "countTicket")
    private Integer count = 0;

    @PositiveOrZero
    @Column(name = "totalCost")
    private Integer cost = 0;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH })
    @JoinTable(name = "user_ticket_mapping", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "lottery_id"))
    private Set<Lottery> lotteries;


//    @OneToMany(mappedBy = "userTicket")
//    private Set<Lottery> lotteries;

    public UserTicket() {}

    public UserTicket(String userID, Integer count, Integer cost) {

        this.userID = userID;
        this.count = count;
        this.cost = cost;
    }

    public Integer getId() {
        return id;
    }

    public String getUserID() {
        return userID;
    }

    public Integer getCount() { return count; }

    public Integer getCost() {
        return cost;
    }

    public Set<Lottery> getLotteries() {
        return lotteries;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setCount(Integer count) { this.count = count; }

    public void setCost(Integer cost) { this.cost = cost; }

    public void setLotteries(Set<Lottery> lotteries) {
        this.lotteries = lotteries;
    }
}
