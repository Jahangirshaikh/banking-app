package com.spring.banking.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "transaction")
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "account_id")
	private Long accountId;

	private double amount;

	private LocalDateTime timestamp;

	@Column(name = "transaction_type")
	private String transactionType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public Transaction(Long id, Long accountId, double amount, LocalDateTime timestamp, String transactionType) {
		super();
		this.id = id;
		this.accountId = accountId;
		this.amount = amount;
		this.timestamp = timestamp;
		this.transactionType = transactionType;
	}

	public Transaction() {
		super();
	}

	@Override
	public String toString() {
		return "Transaction [id=" + id + ", accountId=" + accountId + ", amount=" + amount + ", timestamp=" + timestamp
				+ ", transactionType=" + transactionType + "]";
	}
	
	

}
