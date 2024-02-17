package com.kbtg.bootcamp.posttest.lottery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LotteryRepository extends JpaRepository<Lottery, String> {

    Optional<Lottery> findByTicket(String ticket);

    @Modifying
    @Query("SELECT ticket FROM Lottery l WHERE l.amount > 0")
    List<Lottery> findByAllAvailableTicket();

}
