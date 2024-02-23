package com.kbtg.bootcamp.posttest.user_ticket;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserTicketRepository extends JpaRepository<UserTicket, String> {

    List<UserTicket> findByuserID(String userID);
}

