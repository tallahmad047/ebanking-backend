package com.example.ebankingbackend.web;

import com.example.ebankingbackend.dtos.CustomerDTO;
import com.example.ebankingbackend.exceptions.CustomerNotFoundException;
import com.example.ebankingbackend.service.BankAccountService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;


import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.http.RequestEntity.put;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;




class CustomerRestControllerTest {
    @MockBean
    private BankAccountService bankAccountService;
    @Autowired
    private CustomerRestController bankAccountRestAPI;
    @Autowired
    private MockMvc mockMvc;
    public void BankAccountRestAPI(BankAccountService bankAccountService) {
        if (bankAccountService == null) {
            throw new IllegalArgumentException("bankAccountService cannot be null");
        }
        this.bankAccountService = bankAccountService;
    }
    @Test
    public void testCustomers() {
        List<CustomerDTO> expectedCustomers = Arrays.asList(new CustomerDTO(), new CustomerDTO(), new CustomerDTO());
        when(bankAccountService.listCustomer()).thenReturn(expectedCustomers);

        List<CustomerDTO> actualCustomers = bankAccountRestAPI.customers();

        assertEquals(expectedCustomers, actualCustomers);
        verify(bankAccountService, times(1)).listCustomer();
    }
    @Test
    public void testSearchCustomers() {
        String keyword = "John";
        List<CustomerDTO> expectedCustomers = Arrays.asList(
                new CustomerDTO(1l, "John Doe", "john.doe@example.com"),
                new CustomerDTO(2l, "John Smith", "john.smith@example.com")
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
        customer.setName("John");
        //customer.setLastName("Doe");
        when(bankAccountService.getCustomer(customerId)).thenReturn(customer);

        CustomerDTO result = bankAccountRestAPI.getCustomer(customerId);

        assertNotNull(result);
        assertEquals(customerId, result.getId());
        assertEquals("John", result.getName());
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

   /* @Test
    public void testSaveCustomer() throws Exception {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setName("John");
        customerDTO.setEmail("john.doe@example.com");

        when(bankAccountService.saveCustomer(customerDTO)).thenReturn(customerDTO);

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(customerDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(customerDTO.getName())))
                .andExpect(jsonPath("$.email", is(customerDTO.getEmail())));


        verify(bankAccountService, times(1)).saveCustomer(customerDTO);
    }*/
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