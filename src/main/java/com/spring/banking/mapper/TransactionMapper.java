package com.spring.banking.mapper;

import com.spring.banking.dto.TransactionDto;
import com.spring.banking.entity.Transaction;

public class TransactionMapper {

	public static Transaction mapToTransaction(TransactionDto transactionDto) {
		return new Transaction(transactionDto.id(), transactionDto.accountId(), transactionDto.amount(),
				transactionDto.timestamp(), transactionDto.transactionType());
	}

	public static TransactionDto mapToTransactionDto(Transaction transaction) {
		return new TransactionDto(transaction.getId(), transaction.getAccountId(), transaction.getTimestamp(),
				transaction.getAmount(), transaction.getTransactionType());
	}
}
