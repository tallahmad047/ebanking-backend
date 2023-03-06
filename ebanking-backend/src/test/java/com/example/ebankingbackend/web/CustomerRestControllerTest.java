package com.example.ebankingbackend.web;

import com.example.ebankingbackend.dtos.CustomerDTO;
import com.example.ebankingbackend.entities.Customer;
import com.example.ebankingbackend.exceptions.CustomerNotFoundException;
import com.example.ebankingbackend.repositories.CustomerRepository;
import com.example.ebankingbackend.service.BankAccountService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.http.RequestEntity.put;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CustomerRestControllerTest {
    //@MockBean
    @Mock
    //@Autowired
    private BankAccountService bankAccountService;
    @Autowired
    private CustomerRestController bankAccountRestAPI;
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
   /* @Test
    public void testCustomers() {
        List<CustomerDTO> expectedCustomers = bankAccountService.listCustomer();


        when(bankAccountService.listCustomer()).thenReturn(expectedCustomers);

        List<CustomerDTO> actualCustomers = bankAccountRestAPI.customers();

        assertEquals(expectedCustomers, actualCustomers);
        verify(bankAccountService, times(1)).listCustomer();
    }*/
   @Test
   public void testCustomers() throws Exception {
       // Créer une liste de clients factices pour simuler la réponse du service
       List<CustomerDTO> customers = new ArrayList<>();
       customers.add(new CustomerDTO(1l, "Ahmad","Ahmad@gmail.com"));
       customers.add(new CustomerDTO(2l,"Awa", "Awa@gmail.com"));
       customers.add(new CustomerDTO(2l,"Samba", "Samba@gmail.com"));


       // Configurer le comportement du service pour renvoyer la liste de clients factices
       Mockito.when(bankAccountService.listCustomer()).thenReturn(customers);

       // Envoyer une requête GET à l'endpoint /customers
       mockMvc.perform(MockMvcRequestBuilders.get("/customers")
                       .contentType(MediaType.APPLICATION_JSON))
               // Vérifier que la réponse a un code de statut 200 (OK)
               .andExpect(MockMvcResultMatchers.status().isOk())
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
       }catch (CustomerNotFoundException exception){

       }

    }
    private byte[] toJson(Object object) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsBytes(object);
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



    @Test
    public void testUpdateCustomer() {
        // Mock input data
        Long customerId = 1L;
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setName("Ahmad");
        customerDTO.setEmail("tall@gmail.com");

        // Mock repository response
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setName("Jane");
        customer.setEmail("jane.doe@example.com");


        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        // Call service method
        CustomerDTO updatedCustomer = bankAccountService.updateCustomer(customerDTO);

        // Verify repository method called with correct argument
        verify(customerRepository).findById(customerId);

        // Verify updated customer data returned by service
        assertEquals(customerId, updatedCustomer.getId());
        assertEquals(customerDTO.getName(), updatedCustomer.getName());
        assertEquals(customerDTO.getEmail(), updatedCustomer.getEmail());
    }






    /* @Test
    public void testSaveCustomer() {
        // Créer un customerDTO pour simuler l'entrée de la requête
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(1l);
        customerDTO.setName("Nambe");
        customerDTO.setEmail("Nambe@example.com");

        // Créer un customerDTO pour simuler la sortie de la méthode de service
        CustomerDTO savedCustomerDTO = new CustomerDTO();
        savedCustomerDTO.setId(1L);
        savedCustomerDTO.setName("Nambe");
        savedCustomerDTO.setEmail("Nambe@example.com");

        // Définir le comportement attendu de la méthode de service
        when(bankAccountService.saveCustomer(customerDTO)).thenReturn(savedCustomerDTO);

        // Appeler la méthode de contrôleur
        CustomerDTO result = bankAccountRestAPI.saveCustomer(customerDTO);

        // Vérifier que la méthode de service a été appelée avec les bons arguments
        verify(bankAccountService).saveCustomer(customerDTO);

        // Vérifier que la méthode de contrôleur renvoie le bon résultat
        assertEquals(savedCustomerDTO, result);
    }
  /* @Test
    public void testSaveCustomer() throws Exception {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setName("John");
        customerDTO.setEmail("john.doe@example.com");

        when(bankAccountService.saveCustomer(customerDTO)).thenReturn(customerDTO);

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(customerDTO))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(customerDTO.getName())))
                .andExpect(jsonPath("$.email", is(customerDTO.getEmail())));


        verify(bankAccountService, times(1)).saveCustomer(customerDTO);
    }
   /* @Test
    public void testUpdateCustomer() throws Exception {
        Long customerId = 1L;
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customerId);
        customerDTO.setName("John");


        when(bankAccountService.updateCustomer(customerDTO)).thenReturn(customerDTO);

        mockMvc.perform(put("/customers/{customerId}", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(customerDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(customerId.intValue())))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")));

        verify(bankAccountService, times(1)).updateCustomer(customerDTO);
    }*/
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}