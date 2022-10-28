package com.example.ebankingbackend.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.management.RuntimeErrorException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ebankingbackend.entities.AccountOperation;
import com.example.ebankingbackend.entities.BankAccount;
import com.example.ebankingbackend.entities.CurrentAccount;
import com.example.ebankingbackend.entities.Customer;
import com.example.ebankingbackend.entities.SavingAccount;
import com.example.ebankingbackend.enums.OperationType;
import com.example.ebankingbackend.exceptions.BankAccountNotFoundException;
import com.example.ebankingbackend.exceptions.BanlanceNotSufficentException;
import com.example.ebankingbackend.exceptions.CustomerNotFoundException;
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
    public CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId)  throws CustomerNotFoundException {
         Customer customer=customerRepository.findById(customerId).orElse(null);
        if (customer ==null)
          throw new RuntimeException("Customer not found");
        CurrentAccount  currentAccount=new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setOverDraft(overDraft);
        currentAccount.setCustomer(customer);
        CurrentAccount savedBankAccount= bankAccountRepository.save(currentAccount);

        return savedBankAccount;
    }


    @Override
    public SavingAccount savesavingCurrentBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer=customerRepository.findById(customerId).orElse(null);
        if (customer ==null)
          throw new RuntimeException("Customer not found");
        SavingAccount savingAccount  =new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCustomer(customer);
       // savingAccount.setStatus(null);
        SavingAccount savedBankAccount= bankAccountRepository.save(savingAccount);

        return savedBankAccount;
    }


    @Override
    public List<Customer> listCustomer() {
        
        return customerRepository.findAll();
    }


    @Override
    public BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException {
       BankAccount bankAccount=bankAccountRepository.findById(accountId).
       orElseThrow(()->new BankAccountNotFoundException("BankAccount not found "));
      
        
    return bankAccount;
    }


    @Override
    public void debit(String accoundId, double amount, String description) throws BankAccountNotFoundException, BanlanceNotSufficentException {
        BankAccount bankAccount=getBankAccount(accoundId);
        if(bankAccount.getBalance()<amount)
                throw new BanlanceNotSufficentException("Balance not sufficent");

        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()-amount);
        accountOperation.setBankAccount(bankAccount);
        bankAccountRepository.save(bankAccount);
    }


    @Override
    public void credit(String accoundId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount=getBankAccount(accoundId);
        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepository.save(bankAccount);
        
    }


    @Override
    public void transfert(String accoundIdSource, String accoundIdDestination, double amount) throws BankAccountNotFoundException, BanlanceNotSufficentException {
       debit(accoundIdSource, amount, "transfert"+accoundIdDestination);
       credit(accoundIdDestination, amount, "transfert from"+accoundIdSource);
        
    }
    @Override
    public List<BankAccount> bankAccountList(){
        return bankAccountRepository.findAll();
    }

  

}
