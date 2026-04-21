package com.bankinc.card_system.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bankinc.card_system.dto.CardResponseDTO;
import com.bankinc.card_system.dto.request.CardEnrollRequestDTO;
import com.bankinc.card_system.entity.Card;
import com.bankinc.card_system.repository.CardRepository;
import com.bankinc.card_system.service.impl.CardServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {

	@Mock
	private CardRepository cardRepository;

	@InjectMocks
	private CardServiceImpl cardService;

	private Card card;

	@BeforeEach
	void setUp() {
		card = new Card();
		card.setCardId("1234567890123456");
		card.setProductId("123456");
		card.setHolderName("Test User");
		card.setBalance(BigDecimal.ZERO);
		card.setActive(false);
		card.setBlocked(false);
		card.setExpiryDate(LocalDate.now().plusYears(3));
	}

	@Test
	void testActivateCard() {

		CardEnrollRequestDTO request = new CardEnrollRequestDTO();
		request.setCardId(card.getCardId());

		when(cardRepository.findById(card.getCardId())).thenReturn(Optional.of(card));

		when(cardRepository.save(any(Card.class))).thenAnswer(i -> i.getArgument(0));

		CardResponseDTO result = cardService.activateCard(request);

		assertTrue(result.isActive());

		verify(cardRepository, times(1)).save(any(Card.class));
	}

	@Test
	void testBlockCard() {

		when(cardRepository.findById(card.getCardId())).thenReturn(Optional.of(card));

		when(cardRepository.save(any(Card.class))).thenAnswer(i -> i.getArgument(0));

		cardService.blockCard(card.getCardId());

		verify(cardRepository, times(1)).save(any(Card.class));
	}
}
