package com.bankinc.card_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bankinc.card_system.entity.Card;

public interface CardRepository extends JpaRepository<Card, String>{

}
