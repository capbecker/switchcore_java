package org.example.controller;

import org.example.configuration.AppState;
import org.example.service.SwitchcoreService;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootVersion;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("core")
public class SwitchCoreController implements ApplicationContextAware  {

    private ApplicationContext context;

    private final AppState appState;

    private final SwitchcoreService switchcoreService;

    public SwitchCoreController(AppState appState, SwitchcoreService switchcoreService) {
        this.appState = appState;
        this.switchcoreService = switchcoreService;
    }

    @GetMapping("/info")
    public ResponseEntity<Object> get() {
        return ResponseEntity.ok(appState.getCurrentCore());
        //return ResponseEntity.ok("appState.getCurrentCore()");
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
    }

    @PostMapping("/shutdown")
    public String shutdown() {
        // Encerra o Spring ApplicationContext
        SpringApplication.exit(context, () -> 0);

        // Opcional: força saída da JVM
        System.exit(0);

        return "Aplicação encerrada!";
    }

    @PostMapping("/startOther")
    public ResponseEntity<String> startOther(@RequestParam String service) {
        try {
            switchcoreService.executeOtherCore(service);
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }

}
