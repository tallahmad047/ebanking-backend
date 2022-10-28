package com.example.ebankingbackend;

import com.example.ebankingbackend.entities.*;
import com.example.ebankingbackend.enums.AccountStatus;
import com.example.ebankingbackend.enums.OperationType;
import com.example.ebankingbackend.exceptions.BankAccountNotFoundException;
import com.example.ebankingbackend.exceptions.BanlanceNotSufficentException;
import com.example.ebankingbackend.exceptions.CustomerNotFoundException;
import com.example.ebankingbackend.repositories.AccountOperationRepository;
import com.example.ebankingbackend.repositories.BankAccountRepository;
import com.example.ebankingbackend.repositories.CustomerRepository;
import com.example.ebankingbackend.service.BankAccountService;
import com.example.ebankingbackend.service.BankService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;
import java.util.*;;



@SpringBootApplication
public class EbankingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankingBackendApplication.class, args);
    }
    //@Transactional
    @Bean
    CommandLineRunner commandLineRunner(CustomerRepository customerRepository, BankAccountRepository bankAccountRepository,
                            AccountOperationRepository accountOperationRepository,
                            BankService bankService,BankAccountService bankAccountService ){
        return args -> {
            Stream.of("Ahmad","Awa","Samba").forEach(name->{
                Customer customer=new Customer();
                customer.setName(name);
                customer.setEmail(name + "@gmail.com");
                bankAccountService.saveCustomer(customer);
                //customerRepository.save(customer);
            });
            bankAccountService.listCustomer().forEach(customer->{
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random() *9000, 9000, customer.getId());
                    bankAccountService.savesavingCurrentBankAccount(Math.random() *120000, 5.5, customer.getId());
                    List<BankAccount> bankAccounts=bankAccountService.bankAccountList();
                    for(BankAccount bankAccount:bankAccounts){
                       
                            for (int i = 0; i < 10; i++) {
                                bankAccountService.credit(bankAccount.getId(), 10000 + Math.random()*1200000, "credit");
                                bankAccountService.debit(bankAccount.getId(),1000+Math.random()*9000 , "Debit");
                            }
                     
                    }
                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                } catch (BankAccountNotFoundException e) {
                    
                    e.printStackTrace();
                } catch (BanlanceNotSufficentException e) {
                    
                    e.printStackTrace();
                }

            });
           
           // bankService.consulter();
        };}
   // @Bean
    CommandLineRunner start(CustomerRepository customerRepository, BankAccountRepository bankAccountRepository,
                            AccountOperationRepository accountOperationRepository){
        return args -> {
            Stream.of("Ahmad","Awa","Samba").forEach(name->{
                Customer customer=new Customer();
                customer.setName(name);
                customer.setEmail(name + "@gmail.com");
                customerRepository.save(customer);
            });
            customerRepository.findAll().forEach(
                    cust -> {
                        CurrentAccount currentAccount=new CurrentAccount();
                        currentAccount.setId(UUID.randomUUID().toString());
                        currentAccount.setBalance(Math.random()*90000);
                        currentAccount.setCreatedAt(new Date());
                        currentAccount.setStatus(AccountStatus.CREATED);
                        currentAccount.setCustomer(cust);
                        currentAccount.setOverDraft(9000);
                        bankAccountRepository.save(currentAccount);

                        SavingAccount savingAccount=new SavingAccount();
                        savingAccount.setId(UUID.randomUUID().toString());
                        savingAccount.setBalance(Math.random()*90000);
                        savingAccount.setCreatedAt(new Date());
                        savingAccount.setStatus(AccountStatus.CREATED);
                        savingAccount.setCustomer(cust);
                        savingAccount.setInterestRate(5.6);
                        bankAccountRepository.save(savingAccount);
                    }
            );
            bankAccountRepository.findAll().forEach(acc->
            {
                for (int i = 0; i <10; i++) {
                    AccountOperation accountOperation=new AccountOperation();
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setAmount(Math.random()*12000);
                    accountOperation.setType(Math.random()>0.4 ? OperationType.DEBIT: OperationType.CREDIT);
                    accountOperation.setBankAccount(acc);
                    accountOperationRepository.save(accountOperation);

                }
                

            });
            //2h55min
        };
    }

}
