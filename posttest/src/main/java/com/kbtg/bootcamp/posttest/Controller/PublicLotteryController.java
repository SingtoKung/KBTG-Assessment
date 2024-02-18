package com.kbtg.bootcamp.posttest.Controller;


import com.kbtg.bootcamp.posttest.lottery.Lottery;
import com.kbtg.bootcamp.posttest.lottery.LotteryRequest;
import com.kbtg.bootcamp.posttest.lottery.LotteryService;
import jakarta.validation.Valid;
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

    @PostMapping("")
    public Lottery createLottery (@Valid @RequestBody LotteryRequest request) throws Exception {

        return this.lotteryService.createLottery(request);
    }

}
