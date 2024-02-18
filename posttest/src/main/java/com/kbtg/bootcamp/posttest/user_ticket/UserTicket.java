package com.kbtg.bootcamp.posttest.user_ticket;

import com.kbtg.bootcamp.posttest.lottery.Lottery;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;
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


    @OneToMany(mappedBy = "userTicket")
    private Set<Lottery> lotteries;

    public Integer getId() {
        return id;
    }

    public String getUserID() {
        return userID;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }


}
