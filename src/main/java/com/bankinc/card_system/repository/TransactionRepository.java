package com.bankinc.card_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bankinc.card_system.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, String>{

}
