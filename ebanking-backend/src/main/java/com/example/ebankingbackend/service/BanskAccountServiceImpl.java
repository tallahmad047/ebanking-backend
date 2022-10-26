package com.example.ebankingbackend.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.management.RuntimeErrorException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.ebankingbackend.entities.BankAccount;
import com.example.ebankingbackend.entities.CurrentAccount;
import com.example.ebankingbackend.entities.Customer;
import com.example.ebankingbackend.entities.SavingAccount;
import com.example.ebankingbackend.repositories.AccountOperationRepository;
import com.example.ebankingbackend.repositories.BankAccountRepository;
import com.example.ebankingbackend.repositories.CustomerRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class BanskAccountServiceImpl implements BankAccountService {

    private CustomerRepository customerRepository;

    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    

    public BanskAccountServiceImpl(CustomerRepository customerRepository,
            BankAccountRepository bankAccountRepository,
            AccountOperationRepository accountOperationRepository) {
        this.customerRepository = customerRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.accountOperationRepository = accountOperationRepository;
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        log.info("saving a new customer");
        Customer saveCustomer= customerRepository.save(customer);
        return saveCustomer;
    }

    @Override
    public BankAccount saveBankAccount(double initialBalance, String type, Long customerId) {
        Customer customer=customerRepository.findById(customerId).orElse(null);
        if (customer ==null)
          throw new RuntimeException("Customer not found");
        BankAccount bankAccount;
        if (type.equals("current")){
            bankAccount=new CurrentAccount();
        }else{
            bankAccount=new SavingAccount();
        } 
        bankAccount.setId(UUID.randomUUID().toString());
        bankAccount.setCreatedAt(new Date());
        bankAccount.setBalance(initialBalance);

        return null;
    }

    @Override
    public List<Customer> listCustomer() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BankAccount getBankAccount(String accountId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void debit(String accoundId, double amount, String description) {
        // TODO Auto-generated method stub

    }

    @Override
    public void credit(String accoundId, double amount, String description) {
        // TODO Auto-generated method stub

    }

    @Override
    public void transfert(String accoundIdSource, String accoundIdDestination, double amount) {
        // TODO Auto-generated method stub

    }

}
