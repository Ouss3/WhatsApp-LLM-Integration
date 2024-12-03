package com.ouss.clientservice.repository;

import com.ouss.clientservice.entites.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Integer> {
}
