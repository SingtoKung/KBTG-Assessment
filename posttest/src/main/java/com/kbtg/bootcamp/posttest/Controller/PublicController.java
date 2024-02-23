package com.kbtg.bootcamp.posttest.Controller;


import com.kbtg.bootcamp.posttest.lottery.LotteryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lotteries")
public class PublicController {

    private final LotteryService lotteryService;

    public PublicController(LotteryService lotteryService) {
        this.lotteryService = lotteryService;
    }

//    @GetMapping(value = "", produces = "application/json")
    @GetMapping("")
    public String getAllAvailableTicket() throws Exception {

        return this.lotteryService.getAllAvailableTicket();
    }
}
