package com.example.springreverseproxy.controllers;

import com.example.springreverseproxy.exceptions.NginxConfigurationException;
import com.example.springreverseproxy.models.Application;

import com.example.springreverseproxy.services.ApplicationService;
import com.example.springreverseproxy.services.NginxService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/proxy-config")
public class ProxyController {

    private final NginxService nginxService;
    private final ApplicationService applicationService;

    @Autowired
    public ProxyController(NginxService nginxService, ApplicationService applicationService) {
        this.nginxService = nginxService;
        this.applicationService = applicationService;
    }

    @GetMapping
    public String doGet(Application application, Model model) {
        Iterable<Application> apps = applicationService.findAll();
        model.addAttribute("apps", apps);
        return "proxy-config";
    }

    @PostMapping
    public String doPost(@Valid Application application, Errors inputErrors, Model model) {
        Iterable<Application> apps = applicationService.findAll();
        model.addAttribute("apps", apps);
        if (inputErrors.hasErrors()) {
            return "proxy-config";
        }
        try {
            nginxService.setHostTo(application);
            log.info("Host changed to " + application);
            applicationService.save(application);
            log.info("Application saved to DB. " + application);
            return "redirect:" + nginxService.getRedirectHost();
        } catch (NginxConfigurationException e) {
            model.addAttribute("error", true);
            model.addAttribute("errorMsg", e.getMessage());
            log.error("Error host changing.", e);
            return "proxy-config";
        }
    }
}
