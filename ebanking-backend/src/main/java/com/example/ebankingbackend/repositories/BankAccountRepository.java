package com.example.ebankingbackend.repositories;

import com.example.ebankingbackend.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin("*")
public interface BankAccountRepository extends JpaRepository<BankAccount,String> {
    void findByCustomer(Long customer);
}
