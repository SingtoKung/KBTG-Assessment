package com.kbtg.bootcamp.posttest.Controller;


import com.kbtg.bootcamp.posttest.lottery.Lottery;
import com.kbtg.bootcamp.posttest.lottery.LotteryRequest;
import com.kbtg.bootcamp.posttest.lottery.LotteryResponse;
import com.kbtg.bootcamp.posttest.lottery.LotteryService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/lotteries")
public class PublicLotteryController {

    private final LotteryService lotteryService;

    public PublicLotteryController(LotteryService lotteryService) {
        this.lotteryService = lotteryService;
    }

    @GetMapping("")
    public String getAllAvailableTicket() throws Exception {

        return this.lotteryService.getAllAvailableTicket();
    }

    @PostMapping("")
    public LotteryResponse createLottery (@Valid @RequestBody LotteryRequest request) throws Exception {

        return this.lotteryService.createLottery(request);
    }

}
