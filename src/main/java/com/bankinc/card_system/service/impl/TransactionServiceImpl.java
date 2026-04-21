package com.bankinc.card_system.service.impl;

import org.springframework.stereotype.Service;

import com.bankinc.card_system.entity.Transaction;
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
		// TODO Auto-generated method stub
		return transactionRepository.save(request);
	}

	@Override
	public Transaction findById(String idTransaction) {
		// TODO Auto-generated method stub
		return transactionRepository.getById(idTransaction);
	}

}
