package com.kbtg.bootcamp.posttest.Lottery;

import com.google.gson.Gson;
import com.kbtg.bootcamp.posttest.Controller.PublicController;
import com.kbtg.bootcamp.posttest.Response.TicketResponse;
import com.kbtg.bootcamp.posttest.lottery.Lottery;
import com.kbtg.bootcamp.posttest.lottery.LotteryService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PublicControllerTest {

    MockMvc mockMvc;
    @Mock
    LotteryService lotteryService;

    @BeforeEach
    void setUp() {

        PublicController publicController = new PublicController(lotteryService);
        mockMvc = MockMvcBuilders.standaloneSetup(publicController)
                .alwaysDo(print())
                .build();
    }

    @Test
    @DisplayName("When perform on GET: /lotteries should return all available lotteries")
    public void displayAvailableLotteries() throws Exception {

        Lottery lottery1 = new Lottery();
        lottery1.setTicket("112233");

        Lottery lottery2 = new Lottery();
        lottery2.setTicket("778899");

        Lottery lottery3 = new Lottery();
        lottery3.setTicket("001122");

        ArrayList<String> availLottery = new ArrayList<>();
        availLottery.add(lottery1.getTicket());
        availLottery.add(lottery2.getTicket());
        availLottery.add(lottery3.getTicket());

        Gson gson = new Gson();
        String newJson = gson.toJson(new TicketResponse(availLottery));

        when(lotteryService.getAllAvailableTicket()).thenReturn(newJson);

        ArrayList<String> result = new ArrayList<>();
        result.add("112233");
        result.add("778899");
        result.add("001122");

        mockMvc.perform(get("/lotteries"))
                .andExpect(jsonPath("$.tickets", is(result)))
                .andExpect(status().isOk());
    }
}
