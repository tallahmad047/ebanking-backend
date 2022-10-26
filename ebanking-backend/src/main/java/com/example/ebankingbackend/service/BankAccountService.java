package com.example.ebankingbackend.service;

import java.util.List;

import com.example.ebankingbackend.entities.BankAccount;
import com.example.ebankingbackend.entities.Customer;
import com.example.ebankingbackend.exceptions.CustomerNotFoundException;

public interface BankAccountService {
    public Customer saveCustomer(Customer customer);

    BankAccount saveCurrentBankAccount(double initialBalance,double overDraft,Long customerId) 
    throws CustomerNotFoundException;
    List <Customer> listCustomer();
    BankAccount getBankAccount(String accountId);
    void debit (String accoundId,double amount ,String description);
    void credit (String accoundId,double amount ,String description);
    void transfert (String accoundIdSource,String accoundIdDestination ,double amount);//34:39

}
