package com.kbtg.bootcamp.posttest.Controller;


import com.kbtg.bootcamp.posttest.lottery.Lottery;
import com.kbtg.bootcamp.posttest.lottery.LotteryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lotteries")
public class PublicLotteryController {

    private final LotteryService lotteryService;

    public PublicLotteryController(LotteryService lotteryService) {
        this.lotteryService = lotteryService;
    }

    @GetMapping("")
    public List<Lottery> getAllAvailableTicket() throws Exception {

        return this.lotteryService.getAllAvailableTicket();
    }

}
