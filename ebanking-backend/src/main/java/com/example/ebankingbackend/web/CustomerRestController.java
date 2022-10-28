package com.example.ebankingbackend.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ebankingbackend.entities.BankAccount;
import com.example.ebankingbackend.entities.Customer;
import com.example.ebankingbackend.service.BankAccountService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
//@RequestMapping("/customer")
@AllArgsConstructor
@Slf4j
public class CustomerRestController {
    private BankAccountService bankAccountService;
    

    @GetMapping("/customers")
    public List<Customer> customers(){
        return bankAccountService.listCustomer();
    }
}
