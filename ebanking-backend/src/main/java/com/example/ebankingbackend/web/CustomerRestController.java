package com.example.ebankingbackend.web;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.ebankingbackend.dtos.CustomerDTO;

import com.example.ebankingbackend.exceptions.CustomerNotFoundException;
import com.example.ebankingbackend.service.BankAccountService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
//@RequestMapping("/customer")
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")
public class CustomerRestController {
    private BankAccountService bankAccountService;
    

    @GetMapping("/customers")
    public List<CustomerDTO> customers(){
        return bankAccountService.listCustomer();
    }
    @GetMapping("/customers/search")
    public List<CustomerDTO> searchCustomers(@RequestParam (name = "keyword",defaultValue = "") String keyword){
        return bankAccountService.searchCustomer("%"+keyword + "%");
    }
    @GetMapping("/customers/{id}")
    public CustomerDTO getCustomer (@PathVariable(name = "id")Long customerId) throws CustomerNotFoundException{
        return bankAccountService.getCustomer(customerId);

    }
    @PostMapping("/customers")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
       return bankAccountService.saveCustomer(customerDTO);
    }
    @PutMapping("/customers/{customerId}")
    public CustomerDTO updatCustomer(@PathVariable Long customerId, @RequestBody CustomerDTO customerDTO){
        customerDTO.setId(customerId);
       return bankAccountService.updateCustomer(customerDTO);

    }
    @DeleteMapping("/customers/{id}")
    public void deleteCustomer (@PathVariable Long id){
        bankAccountService.deleteCustomer(id);

    }
}
