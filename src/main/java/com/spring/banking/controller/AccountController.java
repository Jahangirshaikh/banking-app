package com.spring.banking.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.banking.dto.AccountDto;
import com.spring.banking.dto.TransferFundDto;
import com.spring.banking.service.AccountService;

@RestController
@RequestMapping("/apis/accounts")
public class AccountController {
	
	private AccountService accountService;
	
	private AccountController(AccountService accountService) {
		this.accountService = accountService;
	}
	
	//http://localhost:8080/apis/accounts/addAccount
	@PostMapping("/addAccount")
	public ResponseEntity<AccountDto> createAccount(@RequestBody AccountDto accountDto){
		return new ResponseEntity<AccountDto>(accountService.createAccount(accountDto), HttpStatus.CREATED);
	}
	
	@GetMapping("/getAccount/{id}")
	//http://localhost:8080/apis/accounts/getAccount/2
	public ResponseEntity<AccountDto> getAccountById(@PathVariable("id") Long id){
		return new ResponseEntity<AccountDto>(accountService.getAccountById(id), HttpStatus.OK);
	}
	
	@PutMapping("/{id}/deposit")
	//http://localhost:8080/apis/accounts/2/deposit
	public ResponseEntity<AccountDto> depositAmount(@PathVariable Long id, @RequestBody Map<String, Double> request){
		AccountDto accountDto = accountService.depositAmount(id, request.get("amount"));
		return ResponseEntity.ok(accountDto);
	}
	
	@PutMapping("/{id}/withdraw")
	//http://localhost:8080/apis/accounts/2/withdraw
	public ResponseEntity<AccountDto> withdrawAmount(@PathVariable Long id, @RequestBody Map<String, Double> request){
		AccountDto accountDto = accountService.withdrawAmount(id, request.get("amount"));
		return ResponseEntity.ok(accountDto);
	}
	
	@GetMapping("/getAccounts")
	//http://localhost:8080/apis/accounts/getAccounts
	public ResponseEntity<List<AccountDto>> getAllAccounts(){
		return ResponseEntity.ok(accountService.getAllAccounts());
	}
	
	@DeleteMapping("/deleteAccount/{id}")
	//http://localhost:8080/apis/accounts/deleteAccount/1
	public ResponseEntity<String> deleteAccount(@PathVariable Long id){
		accountService.deleteAccount(id);
		return ResponseEntity.ok("Account with " +id+" deleted successfully");
	}
	
	@PutMapping("/transfer")
	//http://localhost:8080/apis/accounts/transfer
	public ResponseEntity<String> transferFunds(@RequestBody TransferFundDto transferFundDto){
		accountService.transferFunds(transferFundDto);
		return ResponseEntity.ok("Transaction Successful.");
	}
}
