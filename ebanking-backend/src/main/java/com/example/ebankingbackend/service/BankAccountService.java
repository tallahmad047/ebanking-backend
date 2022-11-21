package com.example.ebankingbackend.service;

import java.util.List;

import com.example.ebankingbackend.dtos.*;
import com.example.ebankingbackend.entities.BankAccount;
import com.example.ebankingbackend.entities.CurrentAccount;

import com.example.ebankingbackend.entities.SavingAccount;
//import com.example.ebankingbackend.exceptions.CustomerNotFoundException;
import com.example.ebankingbackend.exceptions.BankAccountNotFoundException;
import com.example.ebankingbackend.exceptions.BanlanceNotSufficentException;
import com.example.ebankingbackend.exceptions.CustomerNotFoundException;



public interface BankAccountService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    CustomerDTO updateCustomer(CustomerDTO customerDTO);
    void deleteCustomer(Long customerId);
    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId)throws CustomerNotFoundException ;
    SavingBankAccountDTO savesavingCurrentBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
    List <CustomerDTO> listCustomer();
    BankAccountDTO getBankAccount(String accountId)throws BankAccountNotFoundException;
    List<BankAccountDTO> bankAccountList();
    void debit (String accoundId,double amount ,String description) throws BankAccountNotFoundException, BanlanceNotSufficentException;
    void credit (String accoundId,double amount ,String description) throws BankAccountNotFoundException;
    void transfert (String accoundIdSource,String accoundIdDestination ,double amount) throws BankAccountNotFoundException, BanlanceNotSufficentException;
    CustomerDTO getCustomer(Long customerId)throws CustomerNotFoundException;

    List<AccountOperationDTO> accountHistory(String accoundId);

    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;

    List<CustomerDTO> searchCustomer(String keyword);
}
