package com.example.ebankingbackend.web;

import com.example.ebankingbackend.dtos.*;
import com.example.ebankingbackend.exceptions.BankAccountNotFoundException;
import com.example.ebankingbackend.exceptions.BanlanceNotSufficentException;
import com.example.ebankingbackend.service.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class BankAccountRestAPI {
    private BankAccountService  bankAccountService;

    public BankAccountRestAPI(BankAccountService bankAccountService) {

        this.bankAccountService = bankAccountService;
    }
    @GetMapping("/accounts/{accountId}")
    public BankAccountDTO getBankAccount (@PathVariable  String accountId) throws BankAccountNotFoundException {
        return  bankAccountService.getBankAccount(accountId);

    }
    @GetMapping("/accounts")
    public List<BankAccountDTO> listaccounts(){
        return  bankAccountService.bankAccountList();
    }
    @GetMapping("/accounts/{accountId}/operations")
    public List<AccountOperationDTO> getHistorique(@PathVariable  String accountId){
        return  bankAccountService.accountHistory(accountId);
    }
    @GetMapping("/accounts/{accountId}/pageOperations")
    public AccountHistoryDTO getAccountHistorique(
            @PathVariable  String accountId,
            @RequestParam(name = "page",defaultValue = "0") int page,
            @RequestParam(name = "size",defaultValue = "5") int size) throws BankAccountNotFoundException {
        return  bankAccountService.getAccountHistory(accountId,page,size);
    }
    @PostMapping("/accounts/debit")
    public DebitDTO debit(@RequestBody DebitDTO debitDTO) throws BanlanceNotSufficentException, BankAccountNotFoundException {
        this.bankAccountService.debit(debitDTO.getAccountId(),debitDTO.getAmount(),debitDTO.getDescription());
        return  debitDTO;

    }
    @PostMapping("/accounts/credit")
    public CreditDTO credit(@RequestBody CreditDTO  creditDTO) throws  BankAccountNotFoundException {
        this.bankAccountService.credit(creditDTO.getAccountId(),creditDTO.getAmount(),creditDTO.getDescription());
        return  creditDTO;
    }
    @PostMapping("/accounts/transfer")
    public void transfer(@RequestBody TransferRequestDTO  transferRequestDTO) throws BanlanceNotSufficentException, BankAccountNotFoundException {
        this.bankAccountService.transfert(transferRequestDTO.getAccountSource(),transferRequestDTO.getAccountDestination(),
                transferRequestDTO.getAmount());

        //return  creditDTO;
    }


}
