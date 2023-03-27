package com.example.ebankingbackend.service;

import com.example.ebankingbackend.dtos.BankAccountDTO;
import com.example.ebankingbackend.dtos.CurrentBankAccountDTO;
import com.example.ebankingbackend.dtos.CustomerDTO;
import com.example.ebankingbackend.entities.BankAccount;
import com.example.ebankingbackend.entities.Customer;
import com.example.ebankingbackend.enums.AccountStatus;
import com.example.ebankingbackend.exceptions.BankAccountNotFoundException;
import com.example.ebankingbackend.exceptions.CustomerNotFoundException;
import com.example.ebankingbackend.mappers.BankAccountMapperImpl;
import com.example.ebankingbackend.repositories.CustomerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@MockitoSettings(strictness = Strictness.LENIENT)

class BanskAccountServiceImplTest {
    @Autowired
    BanskAccountServiceImpl customerService;
    @Mock
    private BankAccountService bankAccountService;
    @Mock
    CustomerRepository customerRepository;
    @Mock
    private BankAccountMapperImpl dtoMapper;


    @Test
    @DisplayName("should return a list of all customers as CustomerDTO objects")
    void listCustomerReturnsAllCustomersAsCustomerDTOs() {
        Customer customer1 = new Customer(1L, "customer1", "customer1@gmail.com");
        Customer customer2 = new Customer(2L, "customer2", "customer2@gmail.com");
       // Customer customer3 = new Customer(3L, "customer3", "customer3@gmail.com");

        List<Customer> customers = Arrays.asList(customer1, customer2 );
        when(customerRepository.findAll()).thenReturn(customers);

        when(dtoMapper.fromCustomer(any())).thenReturn(new CustomerDTO());
        List<CustomerDTO> customerDTOS = customerService.listCustomer();
        assertThat(customerDTOS).as("La liste des CustomerDTO ne doit pas être nulle").isNotNull();
        assertThat(customerDTOS.size()).as("La liste des CustomerDTO doit contenir 2 éléments").isEqualTo(2);
        //assertThat(customerDTOS).isNotNull();
        //assertThat(customerDTOS.size()).isEqualTo(2);
    }

    @Test
    public void getBankAccountWhenAccountIdNotFoundThrowsException() throws BankAccountNotFoundException {
        when(bankAccountService.getBankAccount(anyString()))
                .thenThrow(new BankAccountNotFoundException("BankAccount not found "));
        assertThrows(
                BankAccountNotFoundException.class,
                () -> {
                    bankAccountService.getBankAccount("1");
                });
    }

    @Test
    public void getBankAccountWhenAccountIdIsValid() throws BankAccountNotFoundException {
        String accountId = "123";
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(accountId);
        bankAccount.setBalance(1000);
        bankAccount.setCreatedAt(new Date());
        bankAccount.setStatus(AccountStatus.CREATED);
        BankAccountDTO bankAccountDTO = new BankAccountDTO();
        bankAccount.setId(accountId);
        bankAccount.setBalance(1000);
        when(bankAccountService.getBankAccount(accountId)).thenReturn(bankAccountDTO);
        BankAccountDTO result = bankAccountService.getBankAccount(accountId);
        // assertThat(result.getId()).isEqualTo(accountId);
        // assertThat(result.getBalance()).isEqualTo(1000);
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
        customerDTO.setId(6L); // The ID of the customer you want to update
        customerDTO.setName("JohnSmith"); // The updated name for the customer
        customerDTO.setEmail("JohnSmith@gmail.com");

        // Call the updateCustomer method with the test CustomerDTO object
        CustomerDTO updatedCustomer = customerService.updateCustomer(customerDTO);

        // Verify that the updatedCustomer object returned by the method has the expected data
        assertEquals(6L, updatedCustomer.getId());
        assertEquals("JohnSmith", updatedCustomer.getName());
        assertEquals("JohnSmith@gmail.com", updatedCustomer.getEmail());

    }




}