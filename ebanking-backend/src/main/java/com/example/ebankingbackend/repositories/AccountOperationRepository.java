package com.example.ebankingbackend.repositories;

import com.example.ebankingbackend.entities.AccountOperation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountOperationRepository extends JpaRepository<AccountOperation,Long> {
}
