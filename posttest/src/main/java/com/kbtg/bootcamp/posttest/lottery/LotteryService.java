package com.kbtg.bootcamp.posttest.lottery;

import com.kbtg.bootcamp.posttest.Exception.BadRequestException;
import com.kbtg.bootcamp.posttest.Exception.InternalServerException;
import com.kbtg.bootcamp.posttest.Exception.NotFoundException;
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
    public LotteryResponse createLottery(LotteryRequest request) throws Exception {

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
            return new LotteryResponse(lottery.getTicket());
        } catch (Exception e) {
            throw new InternalServerException("Failed to add lottery");
        }


    }

    public List<Lottery> getAllAvailableTicket() throws Exception {

        List<Lottery> optionalLottery = lotteryRepository.findByAllAvailableTicket();
        if (optionalLottery.isEmpty()) {
            throw new NotFoundException("Not found available lottery!");
        }

        return optionalLottery;
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
        if (lottery.getUserTicket() == null) {
            lottery.setUserTicket(userTicket);
        } else {
            throw new BadRequestException("Failed to buy lottery");
        }

        try {

            lotteryRepository.save(lottery);
            return new UserTicketResponse(userTicket.getId());
        } catch (Exception e) {
            throw new InternalServerException("Failed to buy lottery");
        }
    }

    @Transactional
    public List<Lottery> getOwnLottery(String userId) {

        Optional<UserTicket> optionalUserTicket = userTicketRepository.findByuserID(userId.trim());
        List<Lottery> optionalLottery = lotteryRepository.findByOwnerTicket();

        if (optionalUserTicket.isEmpty()) {
            throw new BadRequestException("Invalid user id");
        }
        if (optionalLottery.isEmpty()) {
            throw new NotFoundException("Not found lottery");
        }

        UserTicket userTicket = optionalUserTicket.get();
        List<Lottery> ownLottery = new ArrayList<Lottery>();
        int count = 0;
        int cost = 0;
        for (Lottery lottery : optionalLottery) {
            if (lottery.getUserTicket() == userTicket) {
                ownLottery.add(lottery);
                count += lottery.getAmount();
                cost += (lottery.getAmount() * lottery.getPrice());
            }
        }

        return ownLottery;
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
            return new LotteryResponse(lottery.getTicket());
        } catch (Exception e) {
            throw new InternalServerException("Failed to sell lottery");
        }
    }
}
