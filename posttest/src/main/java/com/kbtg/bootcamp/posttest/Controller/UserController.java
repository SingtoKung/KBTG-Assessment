package com.kbtg.bootcamp.posttest.Controller;

import com.kbtg.bootcamp.posttest.lottery.LotteryRepository;
import com.kbtg.bootcamp.posttest.lottery.LotteryResponse;
import com.kbtg.bootcamp.posttest.lottery.LotteryService;
import com.kbtg.bootcamp.posttest.user_ticket.UserTicketResponse;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.web.bind.annotation.*;

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

}
