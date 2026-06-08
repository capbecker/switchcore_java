package org.example.service;

import org.example.dto.SwitchcoreDto;
import org.example.model.Switchcore;
import org.example.repository.SwitchcoreRepository;
import org.springframework.boot.SpringBootVersion;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Service
public class SwitchcoreService {

    private SwitchcoreRepository repository;

    public SwitchcoreService(SwitchcoreRepository repository) {
        this.repository = repository;
    }

    public SwitchcoreDto currentCore(String port) {
        Optional<Switchcore> current = repository.findByService("switchCore_java");
        if (current.isEmpty()) {
            Switchcore saved = repository.save(new Switchcore(null,
                    "java",
                    System.getProperty("java.version"),
                    "spring-boot",
                    SpringBootVersion.getVersion(),
                    "switchCore_java",
                    1D,
                    port,
                    "java -jar",
                    "java/switchcore_java",
                    "mvn clean package -DskipTests",
                    "target/switchCore_java-1.0-SNAPSHOT.jar"));
            return SwitchcoreDto.fromEntity(saved);
        }
        return SwitchcoreDto.fromEntity(current.get());
    }

    public void executeOtherCore(String service) throws IOException {
        Optional<Switchcore> core = repository.findByService(service);
        if (core.isPresent()) {


            String[] currentDir = System.getProperty("user.dir").split("\\\\");
            String dir = String.join("/", Arrays.copyOf(currentDir, currentDir.length-2))+"/";

            String directory = dir + core.get().getDirectory();

            ProcessBuilder builder = new ProcessBuilder();
            builder.directory(new File(directory));
            builder.command(core.get().getCommand().split(" "));

            builder.redirectOutput(ProcessBuilder.Redirect.DISCARD);
            builder.redirectError(ProcessBuilder.Redirect.DISCARD);

            Process process = builder.start();
        }
    }
}
