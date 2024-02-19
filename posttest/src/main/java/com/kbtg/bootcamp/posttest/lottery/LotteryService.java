package com.kbtg.bootcamp.posttest.lottery;

import com.google.gson.Gson;
import com.kbtg.bootcamp.posttest.Exception.InternalServerException;
import com.kbtg.bootcamp.posttest.Exception.BadRequestException;
import com.kbtg.bootcamp.posttest.Exception.NotFoundException;
import com.kbtg.bootcamp.posttest.Response.MyLotteyResponse;
import com.kbtg.bootcamp.posttest.Response.TicketResponse;
import com.kbtg.bootcamp.posttest.user_ticket.UserTicket;
import com.kbtg.bootcamp.posttest.user_ticket.UserTicketRepository;
import com.kbtg.bootcamp.posttest.user_ticket.UserTicketResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class LotteryService {

    private final LotteryRepository lotteryRepository;
    private final UserTicketRepository userTicketRepository;
    public LotteryService (LotteryRepository lotteryRepository, UserTicketRepository userTicketRepository) {

        this.lotteryRepository = lotteryRepository;
        this.userTicketRepository = userTicketRepository;
    }

    @Transactional
    public LotteryResponse createLottery(LotteryRequest request) {

        Optional<Lottery> optionalLottery = lotteryRepository.findByTicket(request.getTicket());
        Lottery lottery = null;
        if (optionalLottery.isPresent()) {
            lottery = optionalLottery.get();
            lottery.setAmount(lottery.getAmount() + request.getAmount());
        } else {
            lottery = new Lottery();
            lottery.setTicket(request.getTicket());
            lottery.setPrice(request.getPrice());
            lottery.setAmount(request.getAmount());
        }

        try {
            lotteryRepository.save(lottery);
        } catch (Exception e) {
            throw new InternalServerException("Failed to add lottery");
        }

        return new LotteryResponse(lottery.getTicket());
    }

    public String getAllAvailableTicket() {

        List<Lottery> optionalLottery = lotteryRepository.findByAllAvailableTicket();
        if (optionalLottery.isEmpty()) {
            throw new NotFoundException("Not found available lottery!");
        }

        ArrayList<String> availLottery = new ArrayList<>();
        for (Lottery lottery : optionalLottery) {
            availLottery.add(lottery.getTicket());
        }

        Gson gson = new Gson();
        String newJson = gson.toJson(new TicketResponse(availLottery));

        try {
            return newJson;
        } catch (Exception e) {
            throw new InternalServerException("Failed to found available lottery");
        }
    }

    @Transactional
    public UserTicketResponse buyLottery(String userId, String ticketId) {

        Optional<UserTicket> optionalUserTicket = userTicketRepository.findByuserID(userId.trim());
        Optional<Lottery> optionalLottery = lotteryRepository.findByTicket(ticketId.trim());

        if (optionalUserTicket.isEmpty()) {
            throw new BadRequestException("Invalid user id");
        }
        if (optionalLottery.isEmpty() || optionalLottery.get().getAmount() <= 0) {
            throw new BadRequestException("Invalid ticket id");
        }

        UserTicket userTicket = optionalUserTicket.get();
        Lottery lottery = optionalLottery.get();

//        Set userTicket by create userTicket set
        Set<UserTicket> userTickets = lottery.getUserTickets();
        userTickets.add(userTicket);
        lottery.setUserTickets(userTickets);
        lottery.setAmount(lottery.getAmount() - 1); //Remove amount = 1
        userTicket.setCount(userTicket.getCount() + 1);
        userTicket.setCost(userTicket.getCost() + lottery.getPrice());

        try {
            lotteryRepository.save(lottery);
            userTicketRepository.save(userTicket);
        } catch (Exception e) {
            throw new InternalServerException("Failed to buy lottery");
        }

        return new UserTicketResponse(userTicket.getId());
    }

    @Transactional
    public String getOwnLottery(String userId) {

        Optional<UserTicket> optionalUserTicket = userTicketRepository.findByuserID(userId.trim());
        if (optionalUserTicket.isEmpty()) {
            throw new BadRequestException("Invalid user id");
        }

        UserTicket userTicket = optionalUserTicket.get();
        Set<Lottery> lotteries = userTicket.getLotteries();
        if (lotteries.isEmpty()) {
            throw new NotFoundException("Not found lottery");
        }

//        Calculate part
        ArrayList<String> ownLottery = new ArrayList<>();
        for (Lottery lottery : lotteries) {
            ownLottery.add(lottery.getTicket());
        }

        Gson gson = new Gson();
        String newJson = gson.toJson(new MyLotteyResponse(ownLottery, userTicket.getCount(), userTicket.getCost()));

        try {
            return newJson;
        } catch (Exception e) {
            throw new InternalServerException("Failed to find your lottery");
        }
    }

    @Transactional
    public LotteryResponse deleteLottery(String userId, String ticketId) {

        Optional<UserTicket> optionalUserTicket = userTicketRepository.findByuserID(userId.trim());

        if (optionalUserTicket.isEmpty()) {
            throw new BadRequestException("Invalid user id");
        }

        UserTicket userTicket = optionalUserTicket.get();

        Set<Lottery> lotteries = userTicket.getLotteries();
        if (lotteries.isEmpty()) {
            throw new BadRequestException("Not found your lottery");
        }

        List<Lottery> lotteryByTicketId = lotteries.stream().filter(lottery1 -> lottery1.getTicket().equals(ticketId.trim())).toList();
        if (lotteryByTicketId.isEmpty()) {
            throw new BadRequestException("Invalid ticket id");
        }

        lotteries.removeIf(lottery -> lottery.getTicket().equals(ticketId.trim()));
        userTicket.setLotteries(lotteries);


        Lottery lottery = lotteryByTicketId.getFirst();
        lottery.setAmount(lottery.getAmount() + lotteryByTicketId.size());
        userTicket.setCount(userTicket.getCount() - lotteryByTicketId.size());
        userTicket.setCost(userTicket.getCost() - (lottery.getPrice() * lotteryByTicketId.size()));


        try {
            lotteryRepository.save(lottery);
            userTicketRepository.save(userTicket);
        } catch (Exception e) {
            throw new InternalServerException("Failed to sell lottery");
        }

        return new LotteryResponse(lottery.getTicket());
    }
}
