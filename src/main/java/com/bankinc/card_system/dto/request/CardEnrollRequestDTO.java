package com.bankinc.card_system.dto.request;

import jakarta.validation.constraints.NotNull;

public class CardEnrollRequestDTO {

	@NotNull
	private String cardId;

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
}
