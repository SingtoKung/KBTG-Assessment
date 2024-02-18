package com.kbtg.bootcamp.posttest.user_ticket;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTicketRepository extends JpaRepository<UserTicket, String> {

    Optional<UserTicket> findByuserID(String userID);
}

