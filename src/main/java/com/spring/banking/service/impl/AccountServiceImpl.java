package com.spring.banking.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.spring.banking.dto.AccountDto;
import com.spring.banking.dto.TransactionDto;
import com.spring.banking.dto.TransferFundDto;
import com.spring.banking.entity.Account;
import com.spring.banking.entity.Transaction;
import com.spring.banking.exception.AccountException;
import com.spring.banking.mapper.AccountMapper;
import com.spring.banking.mapper.TransactionMapper;
import com.spring.banking.repository.AccountRepository;
import com.spring.banking.repository.TransactionRepository;
import com.spring.banking.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

	private AccountRepository accountRepository;

	private TransactionRepository transactionRepository;

	private final static String TRANSACTION_TYPE_DEPOSIT = "DEPOSIT";

	private final static String TRANSACTION_TYPE_WITHDRAW = "WITHDRAW";

	private final static String TRANSACTION_TYPE_TRANSFER = "TRANSFER";

	private AccountServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {
		this.accountRepository = accountRepository;
		this.transactionRepository = transactionRepository;
	}

	@Override
	public AccountDto createAccount(AccountDto accountDto) {
		Account account = AccountMapper.mapToAccount(accountDto);
		Account savedAccount = accountRepository.save(account);
		return AccountMapper.mapToAccountDto(savedAccount);
	}

	@Override
	public AccountDto getAccountById(Long id) {
		Account account = accountRepository.findById(id)
				.orElseThrow(() -> new AccountException("Account does not exists."));

		AccountDto accountDto = AccountMapper.mapToAccountDto(account);

		return accountDto;
	}

	@Override
	public AccountDto depositAmount(Long id, double amount) {
		Account account = accountRepository.findById(id)
				.orElseThrow(() -> new AccountException("Account does not exists."));
		double total = account.getBalance() + amount;
		account.setBalance(total);
		Account savedAccount = accountRepository.save(account);
		AccountDto accountDto = AccountMapper.mapToAccountDto(savedAccount);

		// save transaction_id
		Transaction transaction = new Transaction();
		transaction.setAccountId(id);
		transaction.setAmount(amount);
		transaction.setTimestamp(LocalDateTime.now());
		transaction.setTransactionType(TRANSACTION_TYPE_DEPOSIT);
		transactionRepository.save(transaction);
		return accountDto;
	}

	@Override
	public AccountDto withdrawAmount(Long id, double amount) {
		Account account = accountRepository.findById(id)
				.orElseThrow(() -> new AccountException("Account does not exists."));

		if (amount > account.getBalance()) {
			throw new RuntimeException("Insufficient funds.");
		}

		double total = account.getBalance() - amount;
		account.setBalance(total);
		Account savedAccount = accountRepository.save(account);

		// save the transaction_id
		Transaction transaction = new Transaction();
		transaction.setAccountId(id);
		transaction.setAmount(amount);
		transaction.setTimestamp(LocalDateTime.now());
		transaction.setTransactionType(TRANSACTION_TYPE_WITHDRAW);
		transactionRepository.save(transaction);

		return AccountMapper.mapToAccountDto(savedAccount);
	}

	@Override
	public List<AccountDto> getAllAccounts() {
		List<Account> accounts = accountRepository.findAll();
		List<AccountDto> accountDTOs = accounts.stream().map((account) -> AccountMapper.mapToAccountDto(account))
				.collect(Collectors.toList());
		return accountDTOs;
	}

	@Override
	public void deleteAccount(Long id) {
		Account account = accountRepository.findById(id)
				.orElseThrow(() -> new AccountException("Account does not exists."));
		accountRepository.delete(account);
	}

	@Override
	public void transferFunds(TransferFundDto transferFundDto) {

		// Get the from_account
		Account fromAccount = accountRepository.findById(transferFundDto.fromId())
				.orElseThrow(() -> new AccountException("Account does not exists."));

		// Get the to_account
		Account toAccount = accountRepository.findById(transferFundDto.toId())
				.orElseThrow(() -> new AccountException("Account does not exists."));

		// Check the balance of from_account
		if (transferFundDto.amount() > fromAccount.getBalance()) {
			throw new RuntimeException("Insufficient Funds.");
		}

		// Debit from the from_account
		fromAccount.setBalance(fromAccount.getBalance() - transferFundDto.amount());

		// Credit to the to_account
		toAccount.setBalance(toAccount.getBalance() + transferFundDto.amount());

		// Save the accounts
		accountRepository.save(fromAccount);
		accountRepository.save(toAccount);

		// Save transaction_id
		Transaction transaction = new Transaction();
		transaction.setAccountId(transferFundDto.fromId());
		transaction.setAmount(transferFundDto.amount());
		transaction.setTimestamp(LocalDateTime.now());
		transaction.setTransactionType(TRANSACTION_TYPE_TRANSFER);
		transactionRepository.save(transaction);

	}

	@Override
	public List<TransactionDto> getAccountTransaction(Long accountId) {

		List<Transaction> transactionList = transactionRepository.findByAccountIdOrderByTimestampDesc(accountId);
		if(transactionList.isEmpty()) {
			throw new RuntimeException("No transactions found for account ID "+accountId);
		}
		List<TransactionDto> transactionDtoList = transactionList
				.stream().map((transaction) -> TransactionMapper
						.mapToTransactionDto(transaction))
				.collect(Collectors.toList());
		return transactionDtoList;
	}
}
