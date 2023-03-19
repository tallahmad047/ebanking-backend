package com.example.ebankingbackend.service;

import com.example.ebankingbackend.dtos.CurrentBankAccountDTO;
import com.example.ebankingbackend.dtos.CustomerDTO;
import com.example.ebankingbackend.entities.Customer;
import com.example.ebankingbackend.enums.AccountStatus;
import com.example.ebankingbackend.exceptions.CustomerNotFoundException;
import com.example.ebankingbackend.mappers.BankAccountMapperImpl;
import com.example.ebankingbackend.repositories.CustomerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class BanskAccountServiceImplTest {
    @Autowired
    BanskAccountServiceImpl customerService;
    @Mock
    private BankAccountService bankAccountService;
    @Mock
    CustomerRepository customerRepository;
    @Mock
    private BankAccountMapperImpl dtoMapper;

    /* @Test
    @DisplayName("Test saving a new customer with empty value")
    void saveCustomerWithEmptyValue() {
        // given
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setName("");
        customerDTO.setEmail("");

        // when
        assertThrows(DataIntegrityViolationException.class, () -> customerService.saveCustomer(customerDTO));
    }

    @Test
    @DisplayName("Test saving a new customer with null value")
    void saveCustomerWithNullValue() {
        // given
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setName(null);
        customerDTO.setEmail(null);

        // when
        assertThrows(DataIntegrityViolationException.class, () -> customerService.saveCustomer(customerDTO));
    }*/


    @Test
    @DisplayName("Test saving a new customer")
    void saveCustomer() {
        // given
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setName("John Doe");
        customerDTO.setEmail("john.doe@example.com");

        // when

        CustomerDTO savedCustomer = customerService.saveCustomer(customerDTO);

        // then
        assertThat(savedCustomer.getId()).isNotNull();
        assertThat(savedCustomer.getName()).isEqualTo(customerDTO.getName());
        assertThat(savedCustomer.getEmail()).isEqualTo(customerDTO.getEmail());
    }

    @Test
    @DisplayName("Test updating an existing customer")
    void updateCustomer() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(5L); // The ID of the customer you want to update
        customerDTO.setName("John Smith"); // The updated name for the customer

        // Call the updateCustomer method with the test CustomerDTO object
        CustomerDTO updatedCustomer = customerService.updateCustomer(customerDTO);

        // Verify that the updatedCustomer object returned by the method has the expected data
        assertEquals(5L, updatedCustomer.getId());
        assertEquals("John Smith", updatedCustomer.getName());

    }

    @Test
    @DisplayName("Test deleting a customer")
    void deleteCustomer() {
        // Create a test customer
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");

        // Save the test customer to the repository
        customerRepository.save(customer);

        // Call the deleteCustomer method
        customerService.deleteCustomer(customer.getId());

        // Verify that the customer was deleted
        Optional<Customer> deletedCustomer = customerRepository.findById(customer.getId());
        assertFalse(deletedCustomer.isPresent());
    }

    @Test
    public void testSearchCustomer() {
        String keyword = "Ahmad";
        List<CustomerDTO> expectedCustomers = Arrays.asList(
                new CustomerDTO(1l, "Ahmad", "Ahmad@gmail.com")
        );
        List<Customer> expectedEntities = expectedCustomers.stream()
                .map(dto -> new Customer(dto.getId(), dto.getName(), dto.getEmail()))
                .collect(Collectors.toList());

        when(customerRepository.searchCustomer("%" + keyword + "%")).thenReturn(expectedEntities);


        List<CustomerDTO> actualCustomers = customerService.searchCustomer(keyword);

        assertEquals(expectedCustomers, actualCustomers);
    }

    @Test
    public void testSaveCurrentBankAccount() throws CustomerNotFoundException {
        // Mock the customer repository to return a customer
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Ahmad");
        customer.setEmail("ahmad@example.com");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        // Call the method to save the current bank account
        double initialBalance = 1000.0;
        double overDraft = 500.0;
        Long customerId = 1L;

        CurrentBankAccountDTO savedAccount = bankAccountService.saveCurrentBankAccount(initialBalance, overDraft, customerId);

        // Verify that the bank account is saved correctly
        assertNotNull(savedAccount.getId());
        assertEquals(initialBalance, savedAccount.getBalance(), 0.0);
        assertEquals(overDraft, savedAccount.getOverDraft(), 0.0);
        assertEquals(customerId, savedAccount.getCustomerDTO());
    }

    @Test
    @DisplayName("Test saving a new current bank account with zero initial balance and overdraft")
    void saveCurrentBankAccountWithZeroInitialBalanceAndOverdraft() {
    }

    @Test
    @DisplayName(
            "Test saving a new current bank account with initial balance and without overdraft")
    void saveCurrentBankAccountWithInitialBalanceWithoutOverdraft() {
    }

    @Test
    @DisplayName("Test saving a new current bank account with initial balance and overdraft")
    void saveCurrentBankAccountWithInitialBalanceAndOverdraft() {
    }

    @Test
    @DisplayName(
            "Test saving a new current bank account with negative initial balance and overdraft")
    void saveCurrentBankAccountWithNegativeInitialBalanceAndOverdraft() {
       // fail("Not implemented yet");
    }

    @Test
    @DisplayName("Test saving a new current bank account with invalid customer ID")
    void saveCurrentBankAccountWithInvalidCustomerId() {
    }

    @Test
    @DisplayName("Test saving a new customer with valid input")
    void saveCustomerWithValidInput() {
        // TODO
    }

    @Test
    @DisplayName("Test saving a new customer with invalid input")
    void saveCustomerWithInvalidInput() {
        // TODO
    }

    @Test
    void saveCurrentBankAccount() {
    }

    @Test
    void savesavingCurrentBankAccount() {
    }

    @Test
    void listCustomer() {
    }

    @Test
    void getBankAccount() {
    }

    @Test
    void debit() {
    }

    @Test
    void credit() {
    }

    @Test
    void transfert() {
    }

    @Test
    void bankAccountList() {
    }

    @Test
    void getCustomer() {
    }

    @Test
    void accountHistory() {
    }

    @Test
    void getAccountHistory() {
    }

    @Test
    void searchCustomer() {
    }
}