package com.kbtg.bootcamp.posttest.Lottery;

import com.google.gson.Gson;
import com.kbtg.bootcamp.posttest.Controller.UserController;
import com.kbtg.bootcamp.posttest.Response.MyLotteryResponse;
import com.kbtg.bootcamp.posttest.lottery.Lottery;
import com.kbtg.bootcamp.posttest.lottery.LotteryResponse;
import com.kbtg.bootcamp.posttest.lottery.LotteryService;
import com.kbtg.bootcamp.posttest.user_ticket.UserTicket;
import com.kbtg.bootcamp.posttest.user_ticket.UserTicketResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    MockMvc mockMvc;
    @Mock
    LotteryService lotteryService;

    @BeforeEach
    void setUp() {

        UserController userController = new UserController(lotteryService);
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .alwaysDo(print())
                .build();
    }

    @Test
    @DisplayName("When perform on POST: /users/{userId}/lotteries/{ticketId} should return id of user_ticket table")
    public void buyLottery() throws Exception {

        String userId = "0987654321";
        String ticketId = "778899";
        Lottery lottery = new Lottery(ticketId, 80, 1);
        UserTicket userTicket = new UserTicket(1, userId, lottery, 1, 80);

        when(lotteryService.buyLottery(userId, ticketId)).thenReturn(new UserTicketResponse(userTicket.getId()));

        mockMvc.perform(post("/users/{userId}/lotteries/{ticketId}", userId, ticketId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id" , is(1)));
    }

    @Test
    @DisplayName("When perform on GET: /users/{userId}/lotteries should display by userId")
    public void getOwnLottery() throws Exception {

        String userId = "0987654321";
        Lottery lottery1 = new Lottery("112233", 80, 1);
        Lottery lottery2 = new Lottery("445566", 80, 1);
        UserTicket userTicket1 = new UserTicket(userId, lottery1, 1, 80);
        UserTicket userTicket2 = new UserTicket(userId, lottery2, 1, 80);

        int countLottery = 0;
        int totalCost = 0;
        ArrayList<String> ownLottery = new ArrayList<>();
        ownLottery.add(userTicket1.getLottery().getTicket());
        ownLottery.add(userTicket2.getLottery().getTicket());
        countLottery += userTicket1.getCount() + userTicket2.getCount();
        totalCost += userTicket1.getCost() + userTicket2.getCost();

        Gson gson = new Gson();
        String newJson = gson.toJson(new MyLotteryResponse(ownLottery, countLottery, totalCost));

        when(lotteryService.getOwnLottery(userId)).thenReturn(newJson);

        ArrayList<String> result = new ArrayList<>();
        result.add("112233");
        result.add("445566");

        mockMvc.perform(get("/users/{userId}/lotteries", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tickets" , is(result)))
                .andExpect(jsonPath("$.count", is(2)))
                .andExpect(jsonPath("$.cost", is(160)));
    }

    @Test
    @DisplayName("When perform on DELETE: /users/{userId}/lotteries/{ticketId} should return ticket number")
    public void deleteLottery() throws Exception {

        String userId = "0987654321";
        String ticketId = "778899";
        Lottery lottery = new Lottery(ticketId, 80, 1);
        UserTicket userTicket = new UserTicket(1, userId, lottery, 1, 80);

        when(lotteryService.deleteLottery(userId, ticketId)).thenReturn(new LotteryResponse(userTicket.getLottery().getTicket()));

        mockMvc.perform(delete("/users/{userId}/lotteries/{ticketId}", userId, ticketId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticket" , is("778899")));
    }
}
