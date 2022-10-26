package com.example.ebankingbackend.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ebankingbackend.entities.BankAccount;
import com.example.ebankingbackend.entities.CurrentAccount;
import com.example.ebankingbackend.entities.SavingAccount;
import com.example.ebankingbackend.repositories.BankAccountRepository;

@Service
@Transactional
public class BankService {
    @Autowired
    private BankAccountRepository bankAccountRepository;
    public void consulter(){
        BankAccount bankAccount=bankAccountRepository.
        findById("04991453-0ae4-4148-933b-2b7511728cdb").orElse(null);
        if (bankAccount !=null){    
            System.out.println("****************************************************");

            System.out.println(bankAccount.getId());
            System.out.println(bankAccount.getStatus());
            System.out.println(bankAccount.getCreatedAt());
            System.out.println(bankAccount.getCustomer().getName());
            System.out.println(bankAccount.getClass().getSimpleName());
            if (bankAccount instanceof  CurrentAccount){
                System.out.println ("Over Draft =>" + " "+((CurrentAccount)bankAccount).getOverDraft());
            } else if (bankAccount instanceof  SavingAccount){
                System.out.println ("Interet =>" + " "+((SavingAccount)bankAccount).getInterestRate());

            }
            bankAccount.getAccountOperations().forEach(op->{
                System.out.println("*******************************************");
                System.out.println(op.getAmount());
                System.out.println(op.getType());
            // System.out.println(op.getBankAccount());
                System.out.println(op.getOperationDate());

            }); 
        }
    }
}
