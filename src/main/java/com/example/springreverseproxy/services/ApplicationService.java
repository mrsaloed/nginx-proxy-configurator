package com.example.springreverseproxy.services;

import com.example.springreverseproxy.models.Application;
import com.example.springreverseproxy.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApplicationService {
    private final ApplicationRepository applicationRepository;

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public Application save(Application application) {
        Optional<Application> app = applicationRepository.getApplicationByHostAndPort(application.getHost(), application.getPort());
        return app.orElseGet(() -> applicationRepository.save(application));
    }

    public Iterable<Application> findAll() {
        return applicationRepository.findAll();
    }

}
