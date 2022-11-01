package com.example.ebankingbackend.dtos;

import com.example.ebankingbackend.entities.AccountOperation;
import com.example.ebankingbackend.entities.Customer;
import com.example.ebankingbackend.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE",length = 4 )
@Data @AllArgsConstructor @NoArgsConstructor
public class SavingBankAccountDTO {
    @Id
    private  String id;
    private double balance;
    private Date createdAt;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    @ManyToOne
    private Customer customer;
    @OneToMany(mappedBy="bankAccount",fetch = FetchType.LAZY)
    //lazy veut dire chargement les attributs de ce compte EAger veut dire de charger les oprations à chaque fois
    // quont charge un compte il est dengereux car il charge bcp de donner en memoire
    private List<AccountOperation> accountOperations;
}
