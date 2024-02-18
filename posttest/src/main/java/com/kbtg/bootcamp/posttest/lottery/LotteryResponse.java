package com.kbtg.bootcamp.posttest.lottery;

import com.kbtg.bootcamp.posttest.user_ticket.UserTicket;

public record LotteryResponse (String ticket, UserTicket userTicket) {
}


