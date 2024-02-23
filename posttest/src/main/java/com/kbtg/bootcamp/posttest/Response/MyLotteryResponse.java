package com.kbtg.bootcamp.posttest.Response;

import java.util.List;

public class MyLotteryResponse {

    private List<String> tickets;
    private int count;
    private int cost;

    public MyLotteryResponse(List<String> tickets, int count, int cost) {
        this.tickets = tickets;
        this.count = count;
        this.cost = cost;
    }

    public List<String> getTickets() {
        return tickets;
    }

    public int getCount() {
        return count;
    }

    public int getCost() {
        return cost;
    }

    public void setTickets(List<String> tickets) {
        this.tickets = tickets;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
