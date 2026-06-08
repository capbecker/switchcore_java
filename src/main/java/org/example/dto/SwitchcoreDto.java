package org.example.dto;

import org.example.model.Switchcore;

public record SwitchcoreDto (        String language,
        String version,
        String framework,
        String frameworkVersion,
        String port
) {
        public static SwitchcoreDto fromEntity(Switchcore saved) {
                return new SwitchcoreDto(saved.getLanguage(),
                        saved.getVersion(),
                        saved.getFramework(),
                        saved.getFrameworkVersion(),
                        saved.getPort());
        }

}