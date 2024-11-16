package com.spring.banking.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.spring.banking.dto.AccountDto;
import com.spring.banking.dto.TransferFundDto;
import com.spring.banking.entity.Account;
import com.spring.banking.exception.AccountException;
import com.spring.banking.mapper.AccountMapper;
import com.spring.banking.repository.AccountRepository;
import com.spring.banking.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

	private AccountRepository accountRepository;

	private AccountServiceImpl(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
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
		
		//Get the to_account
		Account toAccount = accountRepository.findById(transferFundDto.toId())
		.orElseThrow(() -> new AccountException("Account does not exists."));
		
		//Check the balance of from_account
		if(transferFundDto.amount() > fromAccount.getBalance()) {
			throw new AccountException("Insufficient Funds.");
		}
		
		//Debit from the from_account
		fromAccount.setBalance(fromAccount.getBalance() - transferFundDto.amount());
		
		//Credit to the to_account
		toAccount.setBalance(toAccount.getBalance() + transferFundDto.amount());
		
		//Save the accounts
		accountRepository.save(fromAccount);
		accountRepository.save(toAccount);

	}
}
