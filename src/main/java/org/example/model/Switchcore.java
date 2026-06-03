package org.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

@Table
@Entity
public class Switchcore {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String language;
        private String version;
        private String framework;
        private String frameworkVersion;
        private String service;
        private Double protocolVersion;
        private String port;
        private String command;
        private String directory;
        private String compiler;
        private String compiledFile;

        public Switchcore() {
        }

        public Switchcore(Long id, String language, String version, String framework, String frameworkVersion, String service, Double protocolVersion, String port, String command, String directory, String compiler, String compiledFile) {
                this.id = id;
                this.language = language;
                this.version = version;
                this.framework = framework;
                this.frameworkVersion = frameworkVersion;
                this.service = service;
                this.protocolVersion = protocolVersion;
                this.port = port;
                this.command = command;
                this.directory = directory;
                this.compiler = compiler;
                this.compiledFile = compiledFile;
        }

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
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

        public String getService() {
                return service;
        }

        public void setService(String service) {
                this.service = service;
        }

        public Double getProtocolVersion() {
                return protocolVersion;
        }

        public void setProtocolVersion(Double protocolVersion) {
                this.protocolVersion = protocolVersion;
        }

        public String getPort() {
                return port;
        }

        public void setPort(String port) {
                this.port = port;
        }

        public String getCommand() {
                return command;
        }

        public void setCommand(String command) {
                this.command = command;
        }

        public String getDirectory() {
                return directory;
        }

        public void setDirectory(String directory) {
                this.directory = directory;
        }

        public String getCompiler() {
                return compiler;
        }

        public void setCompiler(String compiler) {
                this.compiler = compiler;
        }

        public String getCompiledFile() {
                return compiledFile;
        }

        public void setCompiledFile(String compiledFile) {
                this.compiledFile = compiledFile;
        }
}
