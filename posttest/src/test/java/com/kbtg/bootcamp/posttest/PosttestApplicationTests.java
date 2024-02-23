package com.kbtg.bootcamp.posttest;

import com.google.gson.Gson;
import com.kbtg.bootcamp.posttest.Controller.AdminController;
import com.kbtg.bootcamp.posttest.Controller.PublicController;
import com.kbtg.bootcamp.posttest.Controller.UserController;
import com.kbtg.bootcamp.posttest.Response.MyLotteryResponse;
import com.kbtg.bootcamp.posttest.Response.TicketResponse;
import com.kbtg.bootcamp.posttest.lottery.Lottery;
import com.kbtg.bootcamp.posttest.lottery.LotteryRequest;
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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = PosttestApplication.class)
class PosttestApplicationTests {

	MockMvc mockMvcAdmin;
	MockMvc mockMvcPublic;
	MockMvc mockMvcUser;

	@Mock
	LotteryService lotteryService;

	// Prepare data
	String userId1 = "1111111111";
	String userId2 = "2222222222";
	Lottery lottery1 = new Lottery("112233", 80, 1);
	Lottery lottery2 = new Lottery("445566", 80, 2);
	Lottery lottery3 = new Lottery("778899", 80, 3);
	Lottery lottery4 = new Lottery("123456", 80, 2);
	UserTicket userTicket1 = new UserTicket(1, userId1, lottery4, 1, 80);
	UserTicket userTicket2 = new UserTicket(2, userId2, lottery3, 1, 80);

	@BeforeEach
	void setUp() {

		AdminController adminController = new AdminController(lotteryService);
		mockMvcAdmin = MockMvcBuilders.standaloneSetup(adminController)
				.alwaysDo(print())
				.build();

		PublicController publicController = new PublicController(lotteryService);
		mockMvcPublic = MockMvcBuilders.standaloneSetup(publicController)
				.alwaysDo(print())
				.build();

		UserController userController = new UserController(lotteryService);
		mockMvcUser = MockMvcBuilders.standaloneSetup(userController)
				.alwaysDo(print())
				.build();
	}

	@Test
	@DisplayName("When perform on POST: /admin/lotteries should return ticket number")
	public void createLotteryByAdmin() throws Exception {

		when(lotteryService.createLottery(any())).thenReturn(new LotteryResponse(lottery1.getTicket()));

		mockMvcAdmin.perform(post("/admin/lotteries")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.accept(MediaType.APPLICATION_JSON_VALUE)
						.content("{\"ticket\":\"112233\",\"price\":80,\"amount\":1}")
				)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.ticket" , is("112233")));
	}

	@Test
	@DisplayName("When perform on GET: /lotteries should return all available lotteries")
	public void displayAvailableLotteries() throws Exception {

		ArrayList<String> availLottery = new ArrayList<>();
		availLottery.add(lottery1.getTicket());
		availLottery.add(lottery2.getTicket());
		availLottery.add(lottery3.getTicket());

		Gson gson = new Gson();
		String newJson = gson.toJson(new TicketResponse(availLottery));

		when(lotteryService.getAllAvailableTicket()).thenReturn(newJson);

		ArrayList<String> result = new ArrayList<>();
		result.add("112233");
		result.add("445566");
		result.add("778899");

		mockMvcPublic.perform(get("/lotteries"))
				.andExpect(jsonPath("$.tickets", is(result)))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("When perform on POST: /users/{userId}/lotteries/{ticketId} should return id of user_ticket table")
	public void buyLottery() throws Exception {

		String ticketId = "123456";

		when(lotteryService.buyLottery(userId1, ticketId)).thenReturn(new UserTicketResponse(userTicket1.getId()));

		mockMvcUser.perform(post("/users/{userId}/lotteries/{ticketId}", userId1, ticketId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id" , is(1)));
	}

	@Test
	@DisplayName("When perform on GET: /users/{userId}/lotteries should display by userId")
	public void getOwnLottery() throws Exception {

		int countLottery = 0;
		int totalCost = 0;
		ArrayList<String> ownLottery = new ArrayList<>();
		ownLottery.add(userTicket1.getLottery().getTicket());
		ownLottery.add(userTicket2.getLottery().getTicket());
		countLottery += userTicket1.getCount() + userTicket2.getCount();
		totalCost += userTicket1.getCost() + userTicket2.getCost();

		Gson gson = new Gson();
		String newJson = gson.toJson(new MyLotteryResponse(ownLottery, countLottery, totalCost));

		when(lotteryService.getOwnLottery(userId1)).thenReturn(newJson);

		ArrayList<String> result = new ArrayList<>();
		result.add("123456");
		result.add("778899");

		mockMvcUser.perform(get("/users/{userId}/lotteries", userId1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.tickets" , is(result)))
				.andExpect(jsonPath("$.count", is(2)))
				.andExpect(jsonPath("$.cost", is(160)));
	}

	@Test
	@DisplayName("When perform on DELETE: /users/{userId}/lotteries/{ticketId} should return ticket number")
	public void deleteLottery() throws Exception {

		String ticketId = "123456";

		when(lotteryService.deleteLottery(userId1, ticketId)).thenReturn(new LotteryResponse(userTicket1.getLottery().getTicket()));

		mockMvcUser.perform(delete("/users/{userId}/lotteries/{ticketId}", userId1, ticketId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.ticket" , is("123456")));
	}
}
