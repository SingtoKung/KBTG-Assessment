package com.kbtg.bootcamp.posttest.user_ticket;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserTicketRepository extends JpaRepository<UserTicket, String> {

    List<UserTicket> findByuserID(String userID);
}

