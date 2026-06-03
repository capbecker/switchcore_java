package org.example.configuration;

import org.example.dto.SwitchcoreDto;
import org.springframework.stereotype.Component;

@Component
public class AppState {
    private SwitchcoreDto currentCore;

    public SwitchcoreDto getCurrentCore() {
        return currentCore;
    }

    public void setCurrentCore(SwitchcoreDto currentCore) {
        this.currentCore = currentCore;
    }
}
