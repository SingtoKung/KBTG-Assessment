package com.kbtg.bootcamp.posttest.Controller;

import com.kbtg.bootcamp.posttest.lottery.Lottery;
import com.kbtg.bootcamp.posttest.lottery.LotteryResponse;
import com.kbtg.bootcamp.posttest.lottery.LotteryService;
import com.kbtg.bootcamp.posttest.user_ticket.UserTicketResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final LotteryService lotteryService;

    public UserController(LotteryService lotteryService) {
        this.lotteryService = lotteryService;
    }

    @PostMapping("/{userId}/lotteries/{ticketId}")
    public UserTicketResponse buyLottery (
            @PathVariable(name = "userId") String userID,
            @PathVariable(name = "ticketId") String ticketId
    ) {

        return lotteryService.buyLottery(userID, ticketId);
    }

    @GetMapping("/{userId}/lotteries")
    public List<Lottery> getOwnLottery (
            @PathVariable(name = "userId") String userID
    ) {

        return lotteryService.getOwnLottery(userID);
    }

    @DeleteMapping("/{userId}/lotteries/{ticketId}")
    public LotteryResponse deleteLottery (
            @PathVariable(name = "userId") String userID,
            @PathVariable(name = "ticketId") String ticketId
    ) {

        return lotteryService.deleteLottery(userID, ticketId);
    }

}
