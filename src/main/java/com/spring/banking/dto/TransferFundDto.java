package com.spring.banking.dto;

public record TransferFundDto(Long fromId, Long toId, double amount) {

}
