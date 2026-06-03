package org.example.configuration;


import org.example.service.SwitchcoreService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

@Component
public class Initializer implements CommandLineRunner {

    private final SwitchcoreService switchcoreService;

    @Value("${server.port}")
    private String port;

    private final AppState appState;

    public Initializer(AppState appState, SwitchcoreService switchcoreService) {
        this.appState = appState;
        this.switchcoreService = switchcoreService;
    }

    @Override
    public void run(String... args) throws Exception {
        this.appState.setCurrentCore(switchcoreService.currentCore(port));
    }
}
