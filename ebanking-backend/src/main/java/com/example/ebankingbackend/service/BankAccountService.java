package com.example.ebankingbackend.service;

import java.util.List;

import com.example.ebankingbackend.entities.BankAccount;
import com.example.ebankingbackend.entities.CurrentAccount;
import com.example.ebankingbackend.entities.Customer;
import com.example.ebankingbackend.entities.SavingAccount;
//import com.example.ebankingbackend.exceptions.CustomerNotFoundException;
import com.example.ebankingbackend.exceptions.BankAccountNotFoundException;
import com.example.ebankingbackend.exceptions.BanlanceNotSufficentException;
import com.example.ebankingbackend.exceptions.CustomerNotFoundException;

public interface BankAccountService {
    public Customer saveCustomer(Customer customer);

    CurrentAccount saveCurrentBankAccount(double initialBalance,double overDraft,Long customerId)throws CustomerNotFoundException ;
    SavingAccount savesavingCurrentBankAccount(double initialBalance,double interestRate,Long customerId) throws CustomerNotFoundException;
    List <Customer> listCustomer();
    BankAccount getBankAccount(String accountId)throws BankAccountNotFoundException;
    List<BankAccount> bankAccountList();
    void debit (String accoundId,double amount ,String description) throws BankAccountNotFoundException, BanlanceNotSufficentException;
    void credit (String accoundId,double amount ,String description) throws BankAccountNotFoundException;
    void transfert (String accoundIdSource,String accoundIdDestination ,double amount) throws BankAccountNotFoundException, BanlanceNotSufficentException;//34:39

}
