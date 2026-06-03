package org.example.dto;

import org.example.model.Switchcore;

public class SwitchcoreDto {
        private String language;
        private String version;
        private String framework;
        private String frameworkVersion;
        private String port;

        public SwitchcoreDto() {
        }

        public SwitchcoreDto(String language, String version, String framework, String frameworkVersion, String port) {
                this.language = language;
                this.version = version;
                this.framework = framework;
                this.frameworkVersion = frameworkVersion;
                this.port = port;
        }

        public SwitchcoreDto(Switchcore saved) {
                this.language = saved.getLanguage();
                this.version = saved.getVersion();
                this.framework = saved.getFramework();
                this.frameworkVersion = saved.getFrameworkVersion();
                this.port = saved.getPort();
        }

        public String getLanguage() {
                return language;
        }

        public void setLanguage(String language) {
                this.language = language;
        }

        public String getVersion() {
                return version;
        }

        public void setVersion(String version) {
                this.version = version;
        }

        public String getFramework() {
                return framework;
        }

        public void setFramework(String framework) {
                this.framework = framework;
        }

        public String getFrameworkVersion() {
                return frameworkVersion;
        }

        public void setFrameworkVersion(String frameworkVersion) {
                this.frameworkVersion = frameworkVersion;
        }

        public String getPort() {
                return port;
        }

        public void setPort(String port) {
                this.port = port;
        }
}
