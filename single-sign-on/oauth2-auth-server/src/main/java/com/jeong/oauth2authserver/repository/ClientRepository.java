package com.jeong.oauth2authserver.repository;

import com.jeong.oauth2authserver.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {

    Optional<Client> findByClientId(String clientId);

}