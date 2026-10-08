package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.GenTableDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class GeneratorService {

    //private final TableEventProducer tableEventProducer;

    private final GeneratorSqlService genSqlService;
    private final GeneratorClassService genClassService;

    public void compile() throws IOException, InterruptedException {
        Process process = new ProcessBuilder("cmd", "/c", "mvn -q compile")
                .inheritIO()
                .start();

        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new RuntimeException("Falha ao compilar. Exit code: " + exitCode);
        }
    }

    public String generate(GenTableDTO genTableDTO, Boolean byAi) {
        try {
            genClassService.generateController(genTableDTO, byAi);
            genSqlService.generateTable(genTableDTO, byAi);
            genClassService.generateEntity(genTableDTO, byAi);
            //tableEventProducer.tableCreated(genTableDTO.tableName());
            compile();
            return "ok";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
