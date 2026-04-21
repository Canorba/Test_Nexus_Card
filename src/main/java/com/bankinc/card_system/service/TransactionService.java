package com.bankinc.card_system.service;

import com.bankinc.card_system.entity.Transaction;

public interface TransactionService {
	
	Transaction save(Transaction request);
	
	Transaction findById(String idTransaction);
}
