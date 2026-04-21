package com.bankinc.card_system.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankinc.card_system.dto.ApiResponse;
import com.bankinc.card_system.dto.BalanceResponseDTO;
import com.bankinc.card_system.dto.CardResponseDTO;
import com.bankinc.card_system.dto.TransactionResponseDTO;
import com.bankinc.card_system.dto.request.AnulationRequestDTO;
import com.bankinc.card_system.dto.request.BalanceRequestDTO;
import com.bankinc.card_system.dto.request.CardEnrollRequestDTO;
import com.bankinc.card_system.dto.request.PurchaseRequestDTO;
import com.bankinc.card_system.service.CardService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/card")
public class CardController {

	private final CardService cardService;

	public CardController(CardService cardService) {
		this.cardService = cardService;
	}

	// =========================
	// GENERATE CARD
	// =========================
	@GetMapping("/{productId}/number")
	public ResponseEntity<ApiResponse<CardResponseDTO>> generateCard(@PathVariable String productId) {

		String defaultHolderName = "UNKNOWN";

		CardResponseDTO response = cardService.generateCard(productId, defaultHolderName);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponse<>(true, "Card generated successfully", response));
	}

	// =========================
	// ACTIVATE CARD
	// =========================
	@PostMapping("/enroll")
	public ResponseEntity<ApiResponse<CardResponseDTO>> activateCard(@Valid @RequestBody CardEnrollRequestDTO request) {

		CardResponseDTO response = cardService.activateCard(request);

		return ResponseEntity.ok(new ApiResponse<>(true, "Card activated successfully", response));
	}

	// =========================
	// BLOCK CARD
	// =========================
	@DeleteMapping("/{cardId}")
	public ResponseEntity<ApiResponse<Void>> blockCard(@PathVariable String cardId) {

		cardService.blockCard(cardId);

		return ResponseEntity.ok(new ApiResponse<>(true, "Card blocked successfully", null));
	}

	// =========================
	// RECHARGE BALANCE
	// =========================
	@PostMapping("/balance")
	public ResponseEntity<ApiResponse<CardResponseDTO>> rechargeBalance(@Valid @RequestBody BalanceRequestDTO request) {

		CardResponseDTO response = cardService.rechargeBalance(request);

		return ResponseEntity.ok(new ApiResponse<>(true, "Balance recharged successfully", response));
	}

	// =========================
	// GET BALANCE
	// =========================
	@GetMapping("/balance/{cardId}")
	public ResponseEntity<ApiResponse<BalanceResponseDTO>> getBalance(@PathVariable String cardId) {

		BalanceResponseDTO response = cardService.getBalance(cardId);

		return ResponseEntity.ok(new ApiResponse<>(true, "Balance retrieved successfully", response));
	}

	// =========================
	// GET TRANSACTION
	// =========================
	@GetMapping("/transaction/{transactionId}")
	public ResponseEntity<ApiResponse<TransactionResponseDTO>> getTransaction(@PathVariable String transactionId) {

		TransactionResponseDTO response = cardService.getTransaction(transactionId);

		return ResponseEntity.ok(new ApiResponse<>(true, "Transaction retrieved successfully", response));
	}

	// =========================
	// PURCHASE
	// =========================
	@PostMapping("/transaction/purchase")
	public ResponseEntity<ApiResponse<TransactionResponseDTO>> purchase(
			@Valid @RequestBody PurchaseRequestDTO request) {

		TransactionResponseDTO response = cardService.purchase(request);

		return ResponseEntity.ok(new ApiResponse<>(true, "Purchase successful", response));
	}

	// =========================
	// REVERSE TRANSACTION
	// =========================
	@PostMapping("/transaction/anulation")
	public ResponseEntity<ApiResponse<TransactionResponseDTO>> reverseTransaction(
			@Valid @RequestBody AnulationRequestDTO request) {

		TransactionResponseDTO response = cardService.reverseTransaction(request);

		return ResponseEntity.ok(new ApiResponse<>(true, "Transaction reversed successfully", response));
	}
}