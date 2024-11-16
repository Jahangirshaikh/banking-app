package com.spring.banking.service;

import java.util.List;

import com.spring.banking.dto.AccountDto;
import com.spring.banking.dto.TransferFundDto;

public interface AccountService {

	AccountDto createAccount(AccountDto accountDto);
	
	AccountDto getAccountById(Long id);
	
	AccountDto depositAmount(Long id, double amount);
	
	AccountDto withdrawAmount(Long id, double amount);
	
	List<AccountDto> getAllAccounts();
	
	void deleteAccount(Long id);
	
	void transferFunds(TransferFundDto transferFundDto);
}
