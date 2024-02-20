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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Size(min = 10, max = 10)
    @Pattern(regexp = "[\\d]{10}")
    @Column(name = "userId")
    private String userID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_ticket")
    private Lottery lottery;

    @PositiveOrZero
    @Column(name = "countTicket")
    private Integer count = 0;

    @PositiveOrZero
    @Column(name = "totalCost")
    private Integer cost = 0;


//    @OneToMany(mappedBy = "userTicket")
//    private Set<Lottery> lotteries;

    public UserTicket() {}

    public UserTicket(String userID, Lottery lottery, Integer count, Integer cost) {
        this.userID = userID;
        this.lottery = lottery;
        this.count = count;
        this.cost = cost;
    }

    public Integer getId() {
        return id;
    }

    public String getUserID() {
        return userID;
    }

    public Lottery getLottery() {
        return lottery;
    }

    public Integer getCount() {
        return count;
    }

    public Integer getCost() {
        return cost;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setLottery(Lottery lottery) {
        this.lottery = lottery;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }
}
