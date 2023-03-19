package com.example.ebankingbackend.service;


import java.util.Date;
import java.util.List;
import java.util.UUID;

import java.util.stream.Collectors;

import com.example.ebankingbackend.dtos.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import com.example.ebankingbackend.mappers.BankAccountMapperImpl;
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
    private BankAccountMapperImpl dtoMapper;

   
    

    public BanskAccountServiceImpl(CustomerRepository customerRepository,
            BankAccountRepository bankAccountRepository,
            AccountOperationRepository accountOperationRepository,BankAccountMapperImpl dtoMapper) {
        this.customerRepository = customerRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.accountOperationRepository = accountOperationRepository;
        this.dtoMapper=dtoMapper;
    }


    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("saving a new customer");
        Customer customer=dtoMapper.fromCustomerDTO(customerDTO);
        Customer saveCustomer= customerRepository.save(customer);
        return dtoMapper.fromCustomer(saveCustomer);
    }


    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId)  throws CustomerNotFoundException {
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

        return dtoMapper.fromcCurrentBankAccount(savedBankAccount);
    }


    @Override
    public SavingBankAccountDTO savesavingCurrentBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
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

        return dtoMapper.fromSavingBankAccount(savedBankAccount);
    }


    @Override
    public List<CustomerDTO> listCustomer() {
        List<Customer> customers= customerRepository.findAll();
       List<CustomerDTO> customerDTOs = customers.stream().
       map(customer ->dtoMapper.fromCustomer(customer))
        .collect(Collectors.toList());
      /*
      List<CustomerDTO> customerDTOs=new ArrayList<>();
       for (Customer customer:customers){
        CustomerDTO customerDTO=dtoMapper.fromCustomer(customer);
        customerDTOs.add(customerDTO);
           } */ 
        
        return customerDTOs;
    }



    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
       BankAccount bankAccount=bankAccountRepository.findById(accountId).
       orElseThrow(()->new BankAccountNotFoundException("BankAccount not found "));
       if(bankAccount instanceof SavingAccount){
           SavingAccount savingAccount=(SavingAccount) bankAccount;
           return dtoMapper.fromSavingBankAccount(savingAccount);
       }else {
           CurrentAccount currentAccount=(CurrentAccount) bankAccount;
           return dtoMapper.fromcCurrentBankAccount(currentAccount);
       }

    }


    @Override
    public void debit(String accoundId, double amount, String description) throws BankAccountNotFoundException, BanlanceNotSufficentException {
        BankAccount bankAccount=bankAccountRepository.findById(accoundId).
                orElseThrow(()->new BankAccountNotFoundException("BankAccount not found "));
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
        BankAccount bankAccount=bankAccountRepository.findById(accoundId).
                orElseThrow(()->new BankAccountNotFoundException("BankAccount not found "));
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
    public void transfert(String accoundIdSource, String accoundIdDestination, double amount) throws BankAccountNotFoundException,
            BanlanceNotSufficentException {
       debit(accoundIdSource, amount, "transfert"+accoundIdDestination);
       credit(accoundIdDestination, amount, "transfert from"+accoundIdSource);
        
    }
    @Override
    public List<BankAccountDTO> bankAccountList(){
       List<BankAccount> bankAccounts=bankAccountRepository.findAll();
        List<BankAccountDTO> bankAccountDTOS = bankAccounts.stream().map(bankAccount -> {
            if (bankAccount instanceof SavingAccount) {
                SavingAccount savingAccount = (SavingAccount) bankAccount;
                return dtoMapper.fromSavingBankAccount(savingAccount);
            } else {
                CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                return dtoMapper.fromcCurrentBankAccount(currentAccount);
            }
        }).collect(Collectors.toList());
        return bankAccountDTOS;
    }
    @Override
    public CustomerDTO getCustomer(Long customerId)throws CustomerNotFoundException{
       Customer customer=  customerRepository.findById(customerId)
        .orElseThrow(()->new CustomerNotFoundException("Cusmoter not found"));
        return dtoMapper.fromCustomer(customer);
    }
    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("saving a new customer");
        Customer customer=dtoMapper.fromCustomerDTO(customerDTO);
        Customer saveCustomer= customerRepository.save(customer);
        return dtoMapper.fromCustomer(saveCustomer);
    }
   @Override
    public void deleteCustomer(Long customerId){

        customerRepository.deleteById(customerId);
    }
    @Override
    public List<AccountOperationDTO> accountHistory(String accoundId){
        List <AccountOperation> byBankAccountId=accountOperationRepository.findByBankAccountId(accoundId);
       return byBankAccountId.stream().map(op->dtoMapper.fromAccountOperation(op)).
                collect(Collectors.toList());

    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount=bankAccountRepository.findById(accountId).orElse(null);
        if (bankAccount==null)throw  new BankAccountNotFoundException("Account not found");
        Page<AccountOperation> accountOperations=accountOperationRepository.
                findByBankAccountIdOrderByOperationDateDesc(accountId, PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO=new AccountHistoryDTO();
        List<AccountOperationDTO> accountOperationDTOS=accountOperations.getContent().stream().map(op->
                dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
        accountHistoryDTO.setAccountOperationDTOS(accountOperationDTOS);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
        return accountHistoryDTO;
    }

    @Override
    public List<CustomerDTO> searchCustomer(String keyword) {
        List<Customer> customers=customerRepository.searchCustomer(keyword);
        List<CustomerDTO> customerDTO = customers.stream().map(customer -> dtoMapper.fromCustomer(customer)).collect(Collectors.toList());
        return customerDTO;
    }


}
