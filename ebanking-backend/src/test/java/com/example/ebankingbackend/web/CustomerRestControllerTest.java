package com.example.ebankingbackend.web;

import com.example.ebankingbackend.dtos.CustomerDTO;
import com.example.ebankingbackend.entities.Customer;
import com.example.ebankingbackend.exceptions.CustomerNotFoundException;
import com.example.ebankingbackend.repositories.BankAccountRepository;
import com.example.ebankingbackend.repositories.CustomerRepository;
import com.example.ebankingbackend.service.BankAccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CustomerRestControllerTest {
    // @MockBean
    @Mock
    // @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private CustomerRestController bankAccountRestAPI;
    @Mock
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CustomerRepository customerRepository;

    public void BankAccountRestAPI(BankAccountService bankAccountService) {
        if (bankAccountService == null) {
            throw new IllegalArgumentException("bankAccountService cannot be null");
        }
        this.bankAccountService = bankAccountService;
    }

    public void BankAccountService(BankAccountService bankAccountService) {
        if (bankAccountService == null) {
            throw new IllegalArgumentException("bankAccountService cannot be null");
        }
        this.bankAccountService = bankAccountService;
    }

    private byte[] toJson(Object object) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsBytes(object);
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test if customer id is null and throws IllegalArgumentException
     */
    @Test
    public void deleteCustomerNullId() {
        try {
            bankAccountService.deleteCustomer(null);
        } catch (Exception e) {
            assertEquals("Customer id cannot be null", e.getMessage());
        }
    }

    /**
     * Test if customer id is negative and throws IllegalArgumentException
     */
    @Test
    public void deleteCustomerNegativeId() {
        try {
            bankAccountService.deleteCustomer(-1L);
        } catch (CustomerNotFoundException e) {
            assertEquals("Customer with id -1 not found", e.getMessage());
        }
    }

    /**
     * Test if customer is not found and throws CustomerNotFoundException
     */
    @Test
    public void deleteCustomerNotFound() {
        Mockito.when(bankAccountService.getCustomer(ArgumentMatchers.anyLong()))
                .thenThrow(CustomerNotFoundException.class);
        try {
            bankAccountService.deleteCustomer(1L);
        } catch (CustomerNotFoundException e) {
            assertEquals("Customer not found", e.getMessage());
        }
    }

    /**
     * Test if customer id is valid and customer is deleted from the database
     */
  /*  @Test
    public void deleteCustomerFromDatabase() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John");

        customer.setEmail("john@doe.com");

        customerRepository.save(customer);

        bankAccountRestAPI.deleteCustomer(1L);

        assertEquals(0, customerRepository.count());
    }*/

    /**
     * Test if customer is deleted successfully
     */
    @Test
    public void deleteCustomerSuccess() {
        CustomerDTO customerDTO = new CustomerDTO(1L, "test", "test@test.com");
        Mockito.when(bankAccountService.getCustomer(1L)).thenReturn(customerDTO);
        bankAccountService.deleteCustomer(1L);
        verify(bankAccountService, times(1)).deleteCustomer(1L);
    }

    /**
     * Test updating a customer's email
     */
    @Test
    public void updateCustomerEmail() {
        CustomerDTO customerDTO = new CustomerDTO(1L, "John", "john@gmail.com");
        //Customer customer = new Customer(1L, "John", "john@gmail.com");
        when(bankAccountService.updateCustomer(customerDTO)).thenReturn(customerDTO);
        CustomerDTO result = bankAccountService.updateCustomer(customerDTO);
        assertEquals("john@gmail.com", result.getEmail());
    }

    @Test
    public void updateCustomerName() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(1L);
        customerDTO.setName("John");
        customerDTO.setEmail("john@gmail.com");
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John");
        customer.setEmail("john@gmail.com");
        when(bankAccountService.updateCustomer(customerDTO)).thenReturn(customerDTO);
        CustomerDTO result = bankAccountService.updateCustomer(customerDTO);
        assertEquals(result, customerDTO);
    }

    @Test
    public void testCustomers() throws Exception {
        // Créer une liste de clients factices pour simuler la réponse du service
        List<CustomerDTO> customers = new ArrayList<>();
        customers.add(new CustomerDTO(1l, "Ahmad", "Ahmad@gmail.com"));
        customers.add(new CustomerDTO(2l, "Awa", "Awa@gmail.com"));
        customers.add(new CustomerDTO(2l, "Samba", "Samba@gmail.com"));


        // Configurer le comportement du service pour renvoyer la liste de clients factices
        Mockito.when(bankAccountService.listCustomer()).thenReturn(customers);

        // Envoyer une requête GET à l'endpoint /customers
        mockMvc.perform(get("/customers")
                        .contentType(MediaType.APPLICATION_JSON))
                // Vérifier que la réponse a un code de statut 200 (OK)
                .andExpect(status().isOk())
                // Vérifier que la réponse contient bien la liste de clients factices
                .andExpect(MockMvcResultMatchers.content().json(" [{\"id\":1,\"name\":\"Ahmad\",\"email\":\"Ahmad@gmail.com\"},{\"id\":2,\"name\":\"Awa\",\"email\"\n" +
                        " :\"Awa@gmail.com\"},{\"id\":3,\"name\":\"Samba\",\"email\":\"Samba@gmail.com\"}]"));
    }

    @Test
    public void testSearchCustomers() {
        String keyword = "Ahmad";
        List<CustomerDTO> expectedCustomers = Arrays.asList(
                new CustomerDTO(1l, "Ahmad", "Ahmad@gmail.com")
                //new CustomerDTO(2l, "Awa", "Awa@gmail.com")
        );
        when(bankAccountService.searchCustomer("%" + keyword + "%")).thenReturn(expectedCustomers);

        List<CustomerDTO> actualCustomers = bankAccountRestAPI.searchCustomers(keyword);

        assertEquals(expectedCustomers, actualCustomers);
    }

    @Test
    public void testGetCustomer_Success() throws CustomerNotFoundException {
        Long customerId = 1L;
        CustomerDTO customer = new CustomerDTO();
        customer.setId(customerId);
        customer.setName("Ahmad");
        //customer.setLastName("Doe");
        when(bankAccountService.getCustomer(customerId)).thenReturn(customer);

        CustomerDTO result = bankAccountRestAPI.getCustomer(customerId);

        assertNotNull(result);

        assertEquals(customerId, result.getId());
        assertEquals("Ahmad", result.getName());
        //assertEquals("Doe", result.getLastName());
    }

    @Test
    public void testGetCustomer_CustomerNotFoundException() throws CustomerNotFoundException {
        try {
            Long customerId = 1L;
            when(bankAccountService.getCustomer(customerId)).thenThrow(new CustomerNotFoundException("Customer not found"));

            bankAccountRestAPI.getCustomer(customerId);
        } catch (CustomerNotFoundException exception) {

        }

    }

    @Test
    public void testSaveCustomer() {
        // Création d'un objet CustomerDTO pour tester la méthode
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setName("Namber");
        customerDTO.setEmail("Namber@gmail.com");

        // Création d'un mock pour le service BankAccountService
        BankAccountService bankAccountService = Mockito.mock(BankAccountService.class);

        // Définition /du comportement attendu pour la méthode saveCustomer du mock
        Mockito.when(bankAccountService.saveCustomer(customerDTO)).thenReturn(customerDTO);

        // Appel de la méthode saveCustomer de la classe CustomerController avec l'objet customerDTO créé précédemment
        CustomerDTO result = new CustomerRestController(bankAccountService).saveCustomer(customerDTO);

        // Vérification que le résultat retourné est égal à l'objet customerDTO créé précédemment
        assertEquals(customerDTO, result);

        // Vérification que la méthode saveCustomer du mock a été appelée une fois avec l'objet customerDTO créé précédemment
        Mockito.verify(bankAccountService, Mockito.times(1)).saveCustomer(customerDTO);
    }
}