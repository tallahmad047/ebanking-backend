package com.example.ebankingbackend.web;

import com.example.ebankingbackend.dtos.*;
import com.example.ebankingbackend.exceptions.BankAccountNotFoundException;
import com.example.ebankingbackend.exceptions.BanlanceNotSufficentException;
import com.example.ebankingbackend.service.BankAccountService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class BankAccountRestAPITest {
    @Autowired
    private BankAccountRestAPI bankAccountRestAPI;
    @MockBean
    private BankAccountService bankAccountService;
    public void BankAccountRestAPI(BankAccountService bankAccountService) {
        if (bankAccountService == null) {
            throw new IllegalArgumentException("bankAccountService cannot be null");
        }
        this.bankAccountService = bankAccountService;
    }


    @Before("")
    public void setUp() {
        bankAccountService = mock(BankAccountService.class);
        bankAccountRestAPI = new BankAccountRestAPI(bankAccountService);
    }

    @Test
    public void testGetBankAccount_Success() throws BankAccountNotFoundException {
        String accountId = "12345";
        CurrentBankAccountDTO expectedBankAccountDTO = new CurrentBankAccountDTO();
        expectedBankAccountDTO.setAccountId(accountId);
        when(bankAccountService.getBankAccount(accountId)).thenReturn(expectedBankAccountDTO);

        BankAccountDTO actualBankAccountDTO = bankAccountRestAPI.getBankAccount(accountId);

        assertNotNull(actualBankAccountDTO);
        assertEquals(expectedBankAccountDTO, actualBankAccountDTO);
        verify(bankAccountService).getBankAccount(accountId);
    }


    @Test
    public void testGetBankAccount_BankAccountNotFoundException() throws BankAccountNotFoundException {
        String accountId = "12345";
        when(bankAccountService.getBankAccount(accountId)).thenThrow(new BankAccountNotFoundException("Bank account not found"));
        try {
            bankAccountRestAPI.getBankAccount(accountId);
            fail("Expected BankAccountNotFoundException was not thrown");
        } catch (BankAccountNotFoundException ex) {
            // BankAccountNotFoundException was thrown as expected, test passed
        }
    }
    @Test
    public void testListBankAccounts() {
        List<BankAccountDTO> expectedAccounts = Arrays.asList(
                new CurrentBankAccountDTO(),
                new SavingBankAccountDTO()
        );
        when(bankAccountService.bankAccountList()).thenReturn(expectedAccounts);

        List<BankAccountDTO> actualAccounts = bankAccountRestAPI.listaccounts();

        assertEquals(expectedAccounts, actualAccounts);
        verify(bankAccountService, times(2)).bankAccountList();
    }
    @Test
    public void testGetHistorique() {
        String accountId = "12345";
        List<AccountOperationDTO> expectedOperations = Arrays.asList(
                new AccountOperationDTO(),
                new AccountOperationDTO()
        );

        when(bankAccountService.accountHistory(accountId)).thenReturn(expectedOperations);
        List<AccountOperationDTO> result = bankAccountRestAPI.getHistorique(accountId);

        assertEquals(expectedOperations, result);
        verify(bankAccountService).accountHistory(accountId);
    }
    @Test
    public void testGetAccountHistorique() throws BankAccountNotFoundException {
        String accountId = "12345";
        int page = 0;
        int size = 5;
        AccountHistoryDTO expectedHistory = new AccountHistoryDTO();

        when(bankAccountService.getAccountHistory(accountId, page, size)).thenReturn(expectedHistory);
        AccountHistoryDTO result = bankAccountRestAPI.getAccountHistorique(accountId, page, size);

        assertEquals(expectedHistory, result);
        verify(bankAccountService).getAccountHistory(accountId, page, size);
    }

   @Test
   // (expected = BankAccountNotFoundException.class)
    public void testGetAccountHistorique_BankAccountNotFoundException() throws BankAccountNotFoundException {
        String accountId = "12345";
        int page = 0;
        int size = 5;

        when(bankAccountService.getAccountHistory(accountId, page, size)).thenThrow(new BankAccountNotFoundException("Bank account not found"));
        try {
            bankAccountRestAPI.getAccountHistorique(accountId, page, size);
        }catch (BankAccountNotFoundException exception){

        }


    }
    @Test
    public void testDebit_Success() throws BanlanceNotSufficentException, BankAccountNotFoundException {
        DebitDTO debitDTO = new DebitDTO();
        debitDTO.setAccountId("123456");
        debitDTO.setAmount(100);
        debitDTO.setDescription("Test Debit");

        bankAccountRestAPI.debit(debitDTO);

        verify(bankAccountService).debit("123456", 100, "Test Debit");
    }

    @Test
    public void testDebit_BanlanceNotSufficentException() throws BanlanceNotSufficentException, BankAccountNotFoundException {
       try {
           DebitDTO debitDTO = new DebitDTO();
           debitDTO.setAccountId("123456");
           debitDTO.setAmount(100);
           debitDTO.setDescription("Test Debit");

           doThrow(new BanlanceNotSufficentException("Balance not sufficient")).when(bankAccountService).debit(debitDTO.getAccountId(), debitDTO.getAmount(), debitDTO.getDescription());

           bankAccountRestAPI.debit(debitDTO);

       }catch (BanlanceNotSufficentException e){

       }

    }

    @Test
    public void testDebit_BankAccountNotFoundException() throws BanlanceNotSufficentException, BankAccountNotFoundException {
       try {
           DebitDTO debitDTO = new DebitDTO();
           debitDTO.setAccountId("123456");
           debitDTO.setAmount(100);
           debitDTO.setDescription("Test Debit");

           doThrow(new BankAccountNotFoundException("Bank account not found")).when(bankAccountService).debit(debitDTO.getAccountId(), debitDTO.getAmount(), debitDTO.getDescription());

           bankAccountRestAPI.debit(debitDTO);
       }catch (BankAccountNotFoundException exception){

       }

    }
    @Test
    public void testCredit_Success() throws BankAccountNotFoundException {
        String accountId = "12345";
        double amount = 100.0;
        String description = "Test credit";
        CreditDTO creditDTO = new CreditDTO();
        creditDTO.setAccountId(accountId);
        creditDTO.setAmount(amount);
        creditDTO.setDescription(description);

        bankAccountRestAPI.credit(creditDTO);

        verify(bankAccountService, times(1)).credit(accountId, amount, description);
    }

    @Test
    public void testCredit_BankAccountNotFoundException() throws BankAccountNotFoundException {
        try {
            String accountId = "12345";
            double amount = 100.0;
            String description = "Test credit";
            CreditDTO creditDTO = new CreditDTO();
            creditDTO.setAccountId(accountId);
            creditDTO.setAmount(amount);
            creditDTO.setDescription(description);

            doThrow(new BankAccountNotFoundException("Bank account not found")).when(bankAccountService).credit(accountId, amount, description);

            bankAccountRestAPI.credit(creditDTO);
        }catch (BankAccountNotFoundException exception){

        }

    }

    @Test
    public void testTransfer() throws BanlanceNotSufficentException, BankAccountNotFoundException {
        // Initialisation des objets nécessaires
        TransferRequestDTO transferRequestDTO = new TransferRequestDTO();
        transferRequestDTO.setAccountSource("src");
        transferRequestDTO.setAccountDestination("dest");
        transferRequestDTO.setAmount(100);

        BankAccountService bankAccountService = mock(BankAccountService.class);
        BankAccountRestAPI bankAccountRestAPI = new BankAccountRestAPI(bankAccountService);

        // Définir les comportements des objets mock
        doNothing().when(bankAccountService).transfert(
                eq(transferRequestDTO.getAccountSource()),
                eq(transferRequestDTO.getAccountDestination()),
                eq(transferRequestDTO.getAmount())
        );

        // Appel de la méthode à tester
        bankAccountRestAPI.transfer(transferRequestDTO);

        // Vérification
        verify(bankAccountService, times(1)).transfert(
                eq(transferRequestDTO.getAccountSource()),
                eq(transferRequestDTO.getAccountDestination()),
                eq(transferRequestDTO.getAmount())
        );
    }






}