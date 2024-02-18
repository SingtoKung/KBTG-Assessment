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
    public LotteryResponse createLottery(LotteryRequest request) throws Exception, BadRequestException {

        Optional<Lottery> optionalLottery = lotteryRepository.findByTicket(request.getTicket());
        Lottery lottery = null;
        if (optionalLottery.isPresent()) {
            lottery = optionalLottery.get();
            if (lottery.getUserTicket() == null) {
                lottery.setAmount(lottery.getAmount() + request.getAmount());
                lottery.setPrice(request.getPrice());
            } else {
                throw new BadRequestException("This lottery already has owner");
            }

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

    public String getAllAvailableTicket() throws Exception {

        List<Lottery> optionalLottery = lotteryRepository.findByAllAvailableTicket();
        if (optionalLottery.isEmpty()) {
            throw new NotFoundException("Not found available lottery!");
        }

        ArrayList<String> availLottery = new ArrayList<>();
        for (Lottery lottery : optionalLottery) {
            if (lottery.getUserTicket()==null) {
                availLottery.add(lottery.getTicket());
            }
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
        if (optionalLottery.isEmpty()) {
            throw new BadRequestException("Invalid ticket id");
        }

        UserTicket userTicket = optionalUserTicket.get();
        Lottery lottery = optionalLottery.get();

        if (lottery.getUserTicket() != null) {
            throw new BadRequestException("This lottery already has owner");
        }

        try {
            lottery.setUserTicket(userTicket);
            lotteryRepository.save(lottery);
        } catch (Exception e) {
            throw new InternalServerException("Failed to add lottery");
        }

        return new UserTicketResponse(userTicket.getId());
    }

    @Transactional
    public String getOwnLottery(String userId) {

        Optional<UserTicket> optionalUserTicket = userTicketRepository.findByuserID(userId.trim());
        List<Lottery> optionalLottery = lotteryRepository.findByOwnerTicket();

        if (optionalUserTicket.isEmpty()) {
            throw new BadRequestException("Invalid user id");
        }
        if (optionalLottery.isEmpty()) {
            throw new NotFoundException("Not found lottery");
        }

        UserTicket userTicket = optionalUserTicket.get();
        ArrayList<String> ownLottery = new ArrayList<>();
        int count = 0;
        int cost = 0;
        for (Lottery lottery : optionalLottery) {
            if (lottery.getUserTicket() == userTicket) {
                ownLottery.add(lottery.getTicket());
                count += lottery.getAmount();
                cost += (lottery.getAmount() * lottery.getPrice());
            }
        }

        Gson gson = new Gson();
        String newJson = gson.toJson(new MyLotteyResponse(ownLottery, count, cost));

        try {
            return newJson;
        } catch (Exception e) {
            throw new InternalServerException("Failed to found your lottery");
        }
    }

    @Transactional
    public LotteryResponse deleteLottery(String userId, String ticketId) {

        Optional<UserTicket> optionalUserTicket = userTicketRepository.findByuserID(userId.trim());
        Optional<Lottery> optionalLottery = lotteryRepository.findByTicket(ticketId.trim());

        if (optionalUserTicket.isEmpty()) {
            throw new BadRequestException("Invalid user id");
        }
        if (optionalLottery.isEmpty()) {
            throw new BadRequestException("Invalid ticket id");
        }

        UserTicket userTicket = optionalUserTicket.get();
        Lottery lottery = optionalLottery.get();
        if (lottery.getUserTicket() == userTicket) {
            lottery.setUserTicket(null);
        } else {
            throw new BadRequestException("Failed to sell lottery");
        }

        try {
            lotteryRepository.save(lottery);

        } catch (Exception e) {
            throw new InternalServerException("Failed to sell lottery");
        }

        return new LotteryResponse(lottery.getTicket());
    }
}
