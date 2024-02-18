package com.kbtg.bootcamp.posttest.Response;

import java.util.List;

public class TicketResponse {

    private List<String> tickets;

    public TicketResponse(List<String> tickets) {
        this.tickets = tickets;
    }

    public List<String> getTickets() {
        return tickets;
    }

    public void setTickets(List<String> tickets) {
        this.tickets = tickets;
    }
}
