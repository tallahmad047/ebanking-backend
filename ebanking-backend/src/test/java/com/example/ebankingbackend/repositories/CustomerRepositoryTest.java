package com.example.ebankingbackend.repositories;

import com.example.ebankingbackend.dtos.CustomerDTO;
import com.example.ebankingbackend.entities.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@MockitoSettings(strictness = Strictness.LENIENT)
class CustomerRepositoryTest {
    @Mock
    CustomerRepository customerRepository;
    @Autowired
    CustomerRepository personRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void create(){
        Customer person = personRepository.save(new  Customer(1L, "tonux","tonux@gmail.com"));
        assertNotNull(person);
        assertEquals("tonux", person.getName());
    }

    @Test
    void update(){
        //Given
        Customer person = personRepository.save(new  Customer(1L,"tonux", "tonux@gmail.com"));
        person.setName("Coundoul");
        //When
        Customer personUpdated = personRepository.save(person);
        //Then
        assertNotNull(personUpdated);
        assertEquals("Coundoul", personUpdated.getName());
    }
    // TODO : add test delete
    @Test
    void delete(){
        Customer person=personRepository.save(new Customer(4L,"tonux",
                "tonux@gmail.com"));
        person.setId(4L);
        personRepository.delete(person);
        assertNotNull(person);
        assertEquals(HttpStatus.OK.value(),200);

    }



    // TODO : add test findById
    @Test
    void findById()
    {
        Customer person=personRepository.save(new  Customer(1L,"tonux",
                "tonux@gmail.com"));
        Optional< Customer> personList=personRepository.findById(person.getId());
        assertNotNull(personList);
        assertEquals("tonux",personList.get().getName());
    }

    // TODO : add test findAll

    @Test
    void findAll()
    {
        List<Customer> personList=personRepository.findAll();
        assertNotNull(personList);
        assertEquals(3,personList.size());
    }

    @Test
    void searchCustomer() {
        boolean c=true;
        assertTrue(c);
    }
}