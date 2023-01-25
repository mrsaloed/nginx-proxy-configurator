package com.example.springreverseproxy.controllers;

import com.example.springreverseproxy.models.Application;

import com.example.springreverseproxy.services.ApplicationService;
import com.example.springreverseproxy.services.NginxService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String doGet(Model model) {
        Iterable<Application> apps = applicationService.findAll();
        model.addAttribute("apps", apps);
        return "proxy-config";
    }

    @PostMapping
    public String doPost(Application application, Model model) {
        Iterable<Application> apps = applicationService.findAll();
        model.addAttribute("apps", apps);
        if (applicationService.isValid(application)) {
            Application app = applicationService.save(application);
            log.info("Host changed to " + app.toString());
            //nginxService.setHostTo(app);
        } else {
            model.addAttribute("block", true);
            model.addAttribute("blockedIp", application.getHost());
            log.info("New host blocked " + application);
        }

        return "proxy-config";
    }

}
