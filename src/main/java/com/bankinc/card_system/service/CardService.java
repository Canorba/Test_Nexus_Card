package com.bankinc.card_system.service;

import com.bankinc.card_system.dto.BalanceResponseDTO;
import com.bankinc.card_system.dto.CardResponseDTO;
import com.bankinc.card_system.dto.TransactionResponseDTO;
import com.bankinc.card_system.dto.request.AnulationRequestDTO;
import com.bankinc.card_system.dto.request.BalanceRequestDTO;
import com.bankinc.card_system.dto.request.CardEnrollRequestDTO;
import com.bankinc.card_system.dto.request.PurchaseRequestDTO;

public interface CardService {
	
	CardResponseDTO generateCard(String productId, String holderName);
	
	CardResponseDTO activateCard(CardEnrollRequestDTO request);
	
	void blockCard(String cardId);
	
	CardResponseDTO rechargeBalance(BalanceRequestDTO request);
	
	BalanceResponseDTO getBalance(String cardId);
	
	TransactionResponseDTO getTransaction(String transactionId);
	
	TransactionResponseDTO purchase(PurchaseRequestDTO request);
	
	TransactionResponseDTO reverseTransaction(AnulationRequestDTO request);
}
