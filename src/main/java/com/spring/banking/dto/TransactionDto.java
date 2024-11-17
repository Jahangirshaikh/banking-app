package com.spring.banking.dto;

import java.time.LocalDateTime;

public record TransactionDto(Long id, Long accountId, LocalDateTime timestamp,
		double amount, String transactionType) {

}
