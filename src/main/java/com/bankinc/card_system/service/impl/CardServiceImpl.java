package com.bankinc.card_system.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bankinc.card_system.config.ErrorMessages;
import com.bankinc.card_system.config.TransactionStatus;
import com.bankinc.card_system.dto.BalanceResponseDTO;
import com.bankinc.card_system.dto.CardResponseDTO;
import com.bankinc.card_system.dto.TransactionResponseDTO;
import com.bankinc.card_system.dto.request.AnulationRequestDTO;
import com.bankinc.card_system.dto.request.BalanceRequestDTO;
import com.bankinc.card_system.dto.request.CardEnrollRequestDTO;
import com.bankinc.card_system.dto.request.PurchaseRequestDTO;
import com.bankinc.card_system.entity.Card;
import com.bankinc.card_system.entity.Transaction;
import com.bankinc.card_system.exception.BusinessException;
import com.bankinc.card_system.repository.CardRepository;
import com.bankinc.card_system.service.CardService;
import com.bankinc.card_system.service.TransactionService;

import jakarta.transaction.Transactional;

@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final TransactionService transactionService;

    public CardServiceImpl(CardRepository cardRepository, TransactionService transactionService) {
        this.cardRepository = cardRepository;
        this.transactionService = transactionService;
    }

    // =========================
    // CARD CREATION
    // =========================

    @Override
    public CardResponseDTO generateCard(String productId, String holderName) {

        Card card = new Card();
        card.setCardId(generateCardNumber(productId));
        card.setProductId(productId);
        card.setHolderName(holderName);
        card.setExpiryDate(LocalDate.now().plusYears(3));
        card.setBalance(BigDecimal.ZERO);
        card.setActive(false);
        card.setBlocked(false);

        return toDTO(cardRepository.save(card));
    }

    private String generateCardNumber(String productId) {
        return productId + generateRandomDigits(10);
    }

    private String generateRandomDigits(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }

    // =========================
    // CARD ACTIONS
    // =========================

    @Override
    public CardResponseDTO activateCard(CardEnrollRequestDTO request) {

        Card card = getCard(request.getCardId());

        if (card.isBlocked()) {
            throw new BusinessException(ErrorMessages.CARD_BLOCKED);
        }

        card.setActive(true);
        return toDTO(cardRepository.save(card));
    }

    @Override
    public void blockCard(String cardId) {

        Card card = getCard(cardId);

        card.setBlocked(true);
        card.setActive(false);

        cardRepository.save(card);
    }

    @Override
    public CardResponseDTO rechargeBalance(BalanceRequestDTO request) {

        Card card = getCard(request.getCardId());
        validateCard(card);

        if (request.getBalance().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(ErrorMessages.INVALID_AMOUNT);
        }

        card.setBalance(card.getBalance().add(request.getBalance()));

        return toDTO(cardRepository.save(card));
    }

    @Override
    public BalanceResponseDTO getBalance(String cardId) {

        Card card = getCard(cardId);

        BalanceResponseDTO dto = new BalanceResponseDTO();
        dto.setCardId(card.getCardId());
        dto.setBalance(card.getBalance());

        return dto;
    }

    // =========================
    // PURCHASE
    // =========================

    @Override
    public TransactionResponseDTO purchase(PurchaseRequestDTO request) {

        Card card = getCard(request.getCardId());
        validateCard(card);

        if (card.getExpiryDate().isBefore(LocalDate.now())) {
            throw new BusinessException(ErrorMessages.CARD_EXPIRED);
        }

        if (card.getBalance().compareTo(request.getPrice()) < 0) {
            throw new BusinessException(ErrorMessages.INSUFFICIENT_BALANCE);
        }

        card.setBalance(card.getBalance().subtract(request.getPrice()));
        cardRepository.save(card);

        Transaction tx = new Transaction();
        tx.setTransactionId(generateTransactionId());
        tx.setCardId(card.getCardId());
        tx.setPrice(request.getPrice());
        tx.setDate(LocalDateTime.now());
        tx.setStatus(TransactionStatus.APPROVED.name());

        return toTransactionDTO(transactionService.save(tx));
    }

    // =========================
    // REVERSE TRANSACTION
    // =========================

    @Override
    @Transactional
    public TransactionResponseDTO reverseTransaction(AnulationRequestDTO request) {

        Transaction tx = transactionService.findById(request.getTransactionId());

        if (!tx.getCardId().equals(request.getCardId())) {
            throw new BusinessException(ErrorMessages.TX_NOT_BELONG);
        }

        if (TransactionStatus.REVERSED.name().equals(tx.getStatus())) {
            throw new BusinessException(ErrorMessages.TX_ALREADY_REVERSED);
        }

        if (tx.getDate().isBefore(LocalDateTime.now().minusHours(24))) {
            throw new BusinessException(ErrorMessages.TX_TOO_OLD);
        }

        Card card = getCard(request.getCardId());

        card.setBalance(card.getBalance().add(tx.getPrice()));
        cardRepository.save(card);

        tx.setStatus(TransactionStatus.REVERSED.name());
        transactionService.save(tx);

        return toTransactionDTO(tx);
    }

    @Override
    public TransactionResponseDTO getTransaction(String transactionId) {

        Transaction tx = transactionService.findById(transactionId);

        return toTransactionDTO(tx);
    }

    // =========================
    // HELPERS
    // =========================

    private Card getCard(String cardId) {
        return cardRepository.findById(cardId)
                .orElseThrow(() -> new BusinessException(ErrorMessages.CARD_NOT_FOUND));
    }

    private void validateCard(Card card) {
        if (!card.isActive()) {
            throw new BusinessException(ErrorMessages.CARD_INACTIVE);
        }

        if (card.isBlocked()) {
            throw new BusinessException(ErrorMessages.CARD_BLOCKED);
        }
    }

    private String generateTransactionId() {
        return UUID.randomUUID().toString();
    }

    // =========================
    // MAPPERS
    // =========================

    private CardResponseDTO toDTO(Card card) {

        CardResponseDTO dto = new CardResponseDTO();
        dto.setCardId(card.getCardId());
        dto.setProductId(card.getProductId());
        dto.setHolderName(card.getHolderName());
        dto.setExpiryDate(card.getExpiryDate());
        dto.setBalance(card.getBalance());
        dto.setActive(card.isActive());
        dto.setBlocked(card.isBlocked());
        dto.setCreatedAt(card.getCreatedAt());

        return dto;
    }

    private TransactionResponseDTO toTransactionDTO(Transaction transaction) {

        TransactionResponseDTO dto = new TransactionResponseDTO();
        dto.setTransactionId(transaction.getTransactionId());
        dto.setCardId(transaction.getCardId());
        dto.setPrice(transaction.getPrice());
        dto.setDate(transaction.getDate());
        dto.setStatus(transaction.getStatus());

        return dto;
    }
}