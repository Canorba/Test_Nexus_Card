package com.bankinc.card_system.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bankinc.card_system.dto.request.PurchaseRequestDTO;
import com.bankinc.card_system.entity.Card;
import com.bankinc.card_system.entity.Transaction;
import com.bankinc.card_system.repository.CardRepository;
import com.bankinc.card_system.repository.TransactionRepository;
import com.bankinc.card_system.service.impl.CardServiceImpl;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private TransactionService transactionService; // 🔥 ESTE ES EL IMPORTANTE

    @InjectMocks
    private CardServiceImpl cardService;

    @Test
    void testPurchaseSuccess() {

        Card card = new Card();
        card.setCardId("1234567890123456");
        card.setActive(true);
        card.setBlocked(false);
        card.setBalance(new BigDecimal("1000"));
        card.setExpiryDate(LocalDate.now().plusYears(2));

        PurchaseRequestDTO request = new PurchaseRequestDTO();
        request.setCardId(card.getCardId());
        request.setPrice(new BigDecimal("200"));

        when(cardRepository.findById(card.getCardId()))
                .thenReturn(Optional.of(card));

        when(cardRepository.save(any(Card.class)))
                .thenAnswer(i -> i.getArgument(0));

        when(transactionService.save(any(Transaction.class)))
                .thenAnswer(i -> {
                    Transaction t = i.getArgument(0);
                    if (t.getTransactionId() == null) {
                        t.setTransactionId("TX123");
                    }
                    return t;
                });

        var result = cardService.purchase(request);

        assertEquals("APPROVED", result.getStatus());
        assertEquals(new BigDecimal("800"), card.getBalance());
    }
}