package com.kbtg.bootcamp.posttest.lottery;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class LotteryRequest {

    @NotNull
    @Pattern(regexp = "[\\d]{6}")
    private String ticket;

    @NotNull
    @Min(value = 1)
    private int price;

    @NotNull
    @Min(value = 1)
    private int amount;

    public String getTicket() {
        return ticket;
    }

    public int getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
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

    public LotteryRequest () {}

    public LotteryRequest (String ticket, Integer price, Integer amount) {
        this.ticket = ticket;
        this.price = price;
        this.amount = amount;
    }
}
