package com.bankinc.card_system.service.impl;

import org.springframework.stereotype.Service;

import com.bankinc.card_system.config.ErrorMessages;
import com.bankinc.card_system.entity.Transaction;
import com.bankinc.card_system.exception.BusinessException;
import com.bankinc.card_system.repository.TransactionRepository;
import com.bankinc.card_system.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

	private final TransactionRepository transactionRepository;

	public TransactionServiceImpl(TransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}

	@Override
	public Transaction save(Transaction request) {
		return transactionRepository.save(request);
	}

	@Override
	public Transaction findById(String idTransaction) {
		return transactionRepository.findById(idTransaction)
				.orElseThrow(() -> new BusinessException(ErrorMessages.TX_NOT_FOUND));
	}
}