package com.kbtg.bootcamp.posttest.lottery;

import com.google.gson.Gson;
import com.kbtg.bootcamp.posttest.Exception.InternalServerException;
import com.kbtg.bootcamp.posttest.Exception.BadRequestException;
import com.kbtg.bootcamp.posttest.Exception.NotFoundException;
import com.kbtg.bootcamp.posttest.Response.MyLotteryResponse;
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

        if (!userId.matches("[\\d]{10}")) {
            throw new BadRequestException("Invalid user id");
        }

        Optional<Lottery> optionalLottery = lotteryRepository.findByTicket(ticketId.trim());
        if (optionalLottery.isEmpty() || optionalLottery.get().getAmount() <= 0) {
            throw new BadRequestException("Invalid ticket id");
        }

        Lottery lottery = optionalLottery.get();
        lottery.setAmount(lottery.getAmount() - 1);

        List<UserTicket> userTicketSet = userTicketRepository.findByuserID(userId.trim());
        List<UserTicket> matchUserTicket = userTicketSet.stream()
                .filter(userTicket -> userTicket.getLottery().equals(lottery)).toList();

        try {
            lotteryRepository.save(lottery);
            if (matchUserTicket.isEmpty()) {
                //      Set userTicket by create userTicket set
                UserTicket newTransaction = new UserTicket(userId, lottery, 1, lottery.getPrice());
                userTicketRepository.save(newTransaction);

                return new UserTicketResponse(newTransaction.getId());
            } else {
                //      Update count and cost to match ticket because I will use this transaction for EX05
                matchUserTicket.getFirst().setCount(matchUserTicket.getFirst().getCount() + 1);
                matchUserTicket.getFirst().setCost(matchUserTicket.getFirst().getCost() + lottery.getPrice());

                return new UserTicketResponse(matchUserTicket.getFirst().getId());
            }
        } catch (Exception e) {
            throw new InternalServerException("Failed to buy lottery");
        }
    }

    @Transactional
    public String getOwnLottery(String userId) {

        if (!userId.matches("[\\d]{10}")) {
            throw new BadRequestException("Invalid user id");
        }

        List<UserTicket> optionalUserTicket = userTicketRepository.findByuserID(userId.trim());
        if (optionalUserTicket.isEmpty()) {
            throw new BadRequestException("Not found your lottery, Buy it!");
        }

//        Calculate part
        int countLottery = 0;
        int totalCost = 0;
        ArrayList<String> ownLottery = new ArrayList<>();
        for (UserTicket userTicket : optionalUserTicket) {
            if (ownLottery.isEmpty() || !ownLottery.contains(userTicket.getLottery().getTicket())) {
                ownLottery.add(userTicket.getLottery().getTicket());
            }
            countLottery += userTicket.getCount();
            totalCost += (userTicket.getCost());
        }

        Gson gson = new Gson();
        String newJson = gson.toJson(new MyLotteryResponse(ownLottery, countLottery, totalCost));

        try {
            return newJson;
        } catch (Exception e) {
            throw new InternalServerException("Failed to find your lottery");
        }
    }

    @Transactional
    public LotteryResponse deleteLottery(String userId, String ticketId) {

        if (!userId.matches("[\\d]{10}")) {
            throw new BadRequestException("Invalid user id");
        }

        Optional<Lottery> optionalLottery = lotteryRepository.findByTicket(ticketId);
        if (optionalLottery.isEmpty()) {
            throw new BadRequestException("Invalid ticket id");
        }

        List<UserTicket> optionalUserTicket = userTicketRepository.findByuserID(userId.trim());
        if (optionalUserTicket.isEmpty()) {
            throw new BadRequestException("Not found your lottery, Buy it!");
        }

        UserTicket matchUserTicket = optionalUserTicket.stream()
                .filter(userTicket -> userTicket.getLottery().getTicket().equals(ticketId.trim()))
                .toList().getFirst();
        if (matchUserTicket == null) {
            throw new BadRequestException("You don't have this ticket id, Buy it!");
        }

        Lottery lottery = optionalLottery.get();
        //        Calculate lottery amount
        lottery.setAmount(lottery.getAmount() + matchUserTicket.getCount());
        //        Remove transaction matchUserTicket
        optionalUserTicket.removeIf(userTicket -> userTicket.getLottery().equals(lottery));

        try {
            lotteryRepository.save(lottery);
        } catch (Exception e) {
            throw new InternalServerException("Failed to sell lottery");
        }

        return new LotteryResponse(lottery.getTicket());
    }
}
