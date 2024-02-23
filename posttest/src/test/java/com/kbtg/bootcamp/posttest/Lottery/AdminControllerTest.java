package com.kbtg.bootcamp.posttest.Lottery;

import com.kbtg.bootcamp.posttest.Controller.AdminController;
import com.kbtg.bootcamp.posttest.lottery.Lottery;
import com.kbtg.bootcamp.posttest.lottery.LotteryRequest;
import com.kbtg.bootcamp.posttest.lottery.LotteryResponse;
import com.kbtg.bootcamp.posttest.lottery.LotteryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {

    MockMvc mockMvc;
    @Mock
    LotteryService lotteryService;

    @BeforeEach
    void setUp() {

        AdminController adminController = new AdminController(lotteryService);
        mockMvc = MockMvcBuilders.standaloneSetup(adminController)
                .alwaysDo(print())
                .build();
    }

    @Test
    @DisplayName("When perform on POST: /admin/lotteries should return ticket number")
    public void createLotteryByAdmin() throws Exception {

        LotteryRequest request = new LotteryRequest("778899", 80, 3);
        Lottery lottery = new Lottery();
        lottery.setTicket("778899");

        when(lotteryService.createLottery(any())).thenReturn(new LotteryResponse(lottery.getTicket()));

        mockMvc.perform(post("/admin/lotteries")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"ticket\":\"778899\",\"price\":80,\"amount\":3}")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticket" , is("778899")));
    }
}
