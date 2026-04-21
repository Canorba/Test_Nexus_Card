package com.bankinc.card_system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankinc.card_system.dto.BalanceResponseDTO;
import com.bankinc.card_system.dto.CardResponseDTO;
import com.bankinc.card_system.dto.TransactionResponseDTO;
import com.bankinc.card_system.dto.request.AnulationRequestDTO;
import com.bankinc.card_system.dto.request.BalanceRequestDTO;
import com.bankinc.card_system.dto.request.CardEnrollRequestDTO;
import com.bankinc.card_system.dto.request.PurchaseRequestDTO;
import com.bankinc.card_system.service.CardService;

@RestController
@RequestMapping("api/v1/card")
public class CardController {

	private final CardService cardService;

	public CardController(CardService cardService) {
		this.cardService = cardService;
	}

	@GetMapping("/{productId}/number")
	public ResponseEntity<CardResponseDTO> generateCard(@PathVariable String productId) {

		// En prueba técnica se suele poner placeholder si no llega el nombre
		String defaultHolderName = "UNKNOWN";

		CardResponseDTO response = cardService.generateCard(productId, defaultHolderName);

		return ResponseEntity.ok(response);
	}

	@PostMapping("/enroll")
	public ResponseEntity<CardResponseDTO> activateCard(@RequestBody CardEnrollRequestDTO request) {

		CardResponseDTO response = cardService.activateCard(request);

		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{cardId}")
	public ResponseEntity<Void> blockCard(@PathVariable String cardId) {

		cardService.blockCard(cardId);

		return ResponseEntity.noContent().build();
	}

	@PostMapping("/balance")
	public ResponseEntity<CardResponseDTO> rechargeBalance(@RequestBody BalanceRequestDTO request) {

		CardResponseDTO response = cardService.rechargeBalance(request);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/balance/{cardId}")
	public ResponseEntity<BalanceResponseDTO> getBalance(@PathVariable String cardId) {

		BalanceResponseDTO response = cardService.getBalance(cardId);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/transaction/{transactionId}")
	public ResponseEntity<TransactionResponseDTO> getTransaction(@PathVariable String transactionId) {

		TransactionResponseDTO response = cardService.getTransaction(transactionId);

		return ResponseEntity.ok(response);
	}

	@PostMapping("/transaction/purchase")
	public ResponseEntity<TransactionResponseDTO> purchase(@RequestBody PurchaseRequestDTO request) {

		TransactionResponseDTO response = cardService.purchase(request);

		return ResponseEntity.ok(response);
	}

	@PostMapping("/transaction/anulation")
	public ResponseEntity<TransactionResponseDTO> reverseTransaction(@RequestBody AnulationRequestDTO request) {

		TransactionResponseDTO response = cardService.reverseTransaction(request);

		return ResponseEntity.ok(response);
	}
}