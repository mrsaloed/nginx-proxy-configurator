package com.example.springreverseproxy.repository;

import com.example.springreverseproxy.models.Application;
import com.example.springreverseproxy.models.ApplicationPK;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ApplicationRepository extends CrudRepository<Application, ApplicationPK> {
    Optional<Application> getApplicationByHostAndPort(String host, int port);
}
