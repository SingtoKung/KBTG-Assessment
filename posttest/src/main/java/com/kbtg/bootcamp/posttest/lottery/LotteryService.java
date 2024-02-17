package com.kbtg.bootcamp.posttest.lottery;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            lotteryRepository.save(lottery);


        } else {
            lottery = new Lottery();
            lottery.setTicket(request.getTicket());
            lottery.setPrice(request.getPrice());
            lottery.setAmount(request.getAmount());
            lotteryRepository.save(lottery);
        }

        return lottery;
    }

    public List<Lottery> getAllAvailableTicket() throws Exception {

        return lotteryRepository.findAll();
    }
}
