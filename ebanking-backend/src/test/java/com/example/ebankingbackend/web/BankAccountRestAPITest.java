package com.example.ebankingbackend.web;

import com.example.ebankingbackend.dtos.*;
import com.example.ebankingbackend.exceptions.BankAccountNotFoundException;
import com.example.ebankingbackend.exceptions.BanlanceNotSufficentException;
import com.example.ebankingbackend.service.BankAccountService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.CrossOrigin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@CrossOrigin("*")
class BankAccountRestAPITest {

    @Mock
    private BankAccountService bankAccountService;

    /**
     * Test if the method throws an exception when the account id is invalid
     */
    @Test
    public void getBankAccountThrowsExceptionForInvalidId() throws BankAccountNotFoundException {
        BankAccountRestAPI bankAccountRestAPI = new BankAccountRestAPI(bankAccountService);
        when(bankAccountService.getBankAccount("1"))
                .thenThrow(new BankAccountNotFoundException("Invalid account id"));
        assertThrows(
                BankAccountNotFoundException.class,
                () -> {
                    bankAccountRestAPI.getBankAccount("1");
                });
    }

    /**
     * Test if the method returns the correct bank account when the account has a positive balance
     */
    @Test
    public void getBankAccountReturnsCorrectAccountWithPositiveBalance() throws BankAccountNotFoundException {
        BankAccountRestAPI bankAccountRestAPI = new BankAccountRestAPI(bankAccountService);
        String accountId = "12345";
        CurrentBankAccountDTO bankAccountDTO = new CurrentBankAccountDTO();
        bankAccountDTO.setBalance(100);
        when(bankAccountService.getBankAccount(accountId)).thenReturn(bankAccountDTO);

        BankAccountDTO result = bankAccountRestAPI.getBankAccount(accountId);

        assertEquals(bankAccountDTO, result);
    }

    /**
     * Test if the method returns the correct bank account
     */
    @Test
    public void getBankAccountReturnsCorrectAccount() throws BankAccountNotFoundException {
        BankAccountRestAPI bankAccountRestAPI = new BankAccountRestAPI(bankAccountService);
        String accountId = "12345";
        CurrentBankAccountDTO  bankAccountDTO = new CurrentBankAccountDTO ();
        bankAccountDTO.setId(accountId);
        when(bankAccountService.getBankAccount(accountId)).thenReturn(bankAccountDTO);

        CurrentBankAccountDTO  result = (CurrentBankAccountDTO) bankAccountRestAPI.getBankAccount(accountId);

        assertEquals(accountId, result.getId());
    }

    /**
     * Test if the method returns the correct bank account when the account has a negative balance
     */
    @Test
    public void getBankAccountReturnsCorrectAccountWithNegativeBalance() throws BankAccountNotFoundException {
        BankAccountRestAPI bankAccountRestAPI = new BankAccountRestAPI(bankAccountService);
        String accountId = "12345";
        CurrentBankAccountDTO  bankAccountDTO = new CurrentBankAccountDTO ();
        bankAccountDTO.setBalance(-100);
        when(bankAccountService.getBankAccount(accountId)).thenReturn(bankAccountDTO);

        CurrentBankAccountDTO  result = (CurrentBankAccountDTO) bankAccountRestAPI.getBankAccount(accountId);

        assertEquals(-100, result.getBalance());
    }

    /**
     * Test if the method returns the correct bank account when the account has a balance of zero
     */
    @Test
    public void getBankAccountReturnsCorrectAccountWithZeroBalance() throws BankAccountNotFoundException {
        BankAccountRestAPI bankAccountRestAPI = new BankAccountRestAPI(bankAccountService);
        String accountId = "12345";
        CurrentBankAccountDTO  bankAccountDTO = new CurrentBankAccountDTO ();
        bankAccountDTO.setBalance(0);
        when(bankAccountService.getBankAccount(accountId)).thenReturn(bankAccountDTO);

        CurrentBankAccountDTO  result = (CurrentBankAccountDTO) bankAccountRestAPI.getBankAccount(accountId);

        assertEquals(0, result.getBalance());
    }

    /**
     * Test if the method throws an exception when the account balance is not sufficient for debit
     */
    @Test
    public void debitThrowsExceptionWhenBalanceIsNotSufficient() throws BankAccountNotFoundException, BanlanceNotSufficentException {
        BankAccountRestAPI bankAccountRestAPI = new BankAccountRestAPI(bankAccountService);
        DebitDTO debitDTO = new DebitDTO();
        debitDTO.setAccountId("12345");
        debitDTO.setAmount(100);
        debitDTO.setDescription("Test debit");
        doThrow(new BanlanceNotSufficentException("Insufficient balance"))
                .when(bankAccountService).debit(debitDTO.getAccountId(), debitDTO.getAmount(), debitDTO.getDescription());

        assertThrows(
                BanlanceNotSufficentException.class,
                () -> {
                    bankAccountRestAPI.debit(debitDTO);
                });
    }


    /**
     * Test if the method returns the correct debit DTO
     */
    @Test
    public void debitReturnsCorrectDebitDTO() throws BankAccountNotFoundException, BanlanceNotSufficentException {
        BankAccountRestAPI bankAccountRestAPI = new BankAccountRestAPI(bankAccountService);
        DebitDTO debitDTO = new DebitDTO();
        debitDTO.setAccountId("12345");
        debitDTO.setAmount(100);
        debitDTO.setDescription("Test debit");

        DebitDTO result = bankAccountRestAPI.debit(debitDTO);

        assertEquals(debitDTO, result);
    }

    /**
     * Test if the method returns the correct credit DTO
     */
    @Test
    public void creditReturnsCorrectCreditDTO() throws BankAccountNotFoundException {
        BankAccountRestAPI bankAccountRestAPI = new BankAccountRestAPI(bankAccountService);
        CreditDTO creditDTO = new CreditDTO();
        creditDTO.setAccountId("12345");
        creditDTO.setAmount(100);
        creditDTO.setDescription("Test credit");

        CreditDTO result = bankAccountRestAPI.credit(creditDTO);

        assertEquals(creditDTO, result);
    }


}