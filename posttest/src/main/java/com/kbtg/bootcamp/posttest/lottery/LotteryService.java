package com.kbtg.bootcamp.posttest.lottery;

import com.kbtg.bootcamp.posttest.Exception.InternalServerException;
import com.kbtg.bootcamp.posttest.Exception.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LotteryService {

    private final LotteryRepository lotteryRepository;
    public LotteryService (LotteryRepository lotteryRepository) {

        this.lotteryRepository = lotteryRepository;
    }

    @Transactional
    public Lottery createLottery(LotteryRequest request) throws Exception {

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

        return lottery;
    }

    public List<Lottery> getAllAvailableTicket() throws Exception {

        List<Lottery> optionalLottery = lotteryRepository.findByAllAvailableTicket();
        if (optionalLottery.isEmpty()) {
            throw new NotFoundException("Not found available lottery!");
        }



        return optionalLottery;
    }
}
