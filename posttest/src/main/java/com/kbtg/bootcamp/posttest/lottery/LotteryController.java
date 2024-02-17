package com.kbtg.bootcamp.posttest.lottery;


import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lotteries")
public class LotteryController {

    private final LotteryService lotteryService;

    public LotteryController (LotteryService lotteryService) {
        this.lotteryService = lotteryService;
    }

    @PostMapping("")
    public Lottery createLottery (@Valid @RequestBody LotteryRequest request) throws Exception {

        return this.lotteryService.createLottery(request);
    }

    @GetMapping("")
    public List<Lottery> getAllAvailableTicket() throws Exception {

        return this.lotteryService.getAllAvailableTicket();
    }

}
