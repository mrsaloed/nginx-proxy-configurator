package com.example.springreverseproxy.repository;

import com.example.springreverseproxy.Application;
import org.springframework.data.repository.CrudRepository;

public interface ApplicationRepository extends CrudRepository<Application, Long> {
}
