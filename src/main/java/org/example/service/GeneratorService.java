package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.GenColumnDTO;

import org.example.dto.GenTableDTO;
import org.example.dto.GenTableRequest;
import org.example.exceptions.SchemaConflictException;
import org.example.messaging.producer.TableEventProducer;
import org.example.model.GenColumn;
import org.example.model.GenTable;
import org.example.repository.GenTableRepository;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class GeneratorService {

    private final TableEventProducer tableEventProducer;

    private final GeneratorSqlService genSqlService;
    private final GenTableRepository genTableRepository;
    private final GeneratorClassService genClassService;
    //o bean criado no mapperConfig injeta uma função sobre o modelmapper padrao
    private final ModelMapper mapper;

    /**
     * Compila e limpa o maven
     * @throws IOException
     * @throws InterruptedException
     */
    private void compile() throws IOException, InterruptedException {
        boolean windows = System.getProperty("os.name").toLowerCase().contains("win");

        List<String> comando = windows
                ? List.of("cmd", "/c", "mvn clean -q compile")
                : List.of("mvn", "clean", "-q", "compile");

        Process process = new ProcessBuilder(comando)
                .inheritIO()
                .start();

        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new RuntimeException("Falha ao compilar. Exit code: " + exitCode);
        }
    }

    /**
     * Prepara o objeto GenTableDTO, com base na requisição + o existente no banco de dados,
     *  para a criação/edição de classes e tabelas
     * @param request
     * @return
     */
    private GenTableDTO buildTableDTO(GenTableRequest request) {
        GenTableDTO dto = mapper.map(request, GenTableDTO.class);

        genTableRepository.findByTableNameIgnoreCase(request.tableName())
                .ifPresent(existing -> mergeWithExisting(dto, existing));

        return dto;
    }

    /**
     * Une as colunas vindas da requisição com as existentes no banco de dados
     * @param dto
     * @param existing
     */
    private void mergeWithExisting(GenTableDTO dto, GenTable existing) {
        dto.setNewTable(false);

        for (GenColumn current : existing.getGenColumns()) {
            Optional<GenColumnDTO> requested = dto.getGenColumns().stream()
                    .filter(c -> c.getColumnName().equalsIgnoreCase(current.getColumnName()))
                    .findFirst();

            if (requested.isEmpty()) {
                // coluna que já existe no banco e não veio no request
                GenColumnDTO old = new GenColumnDTO(current);
                old.setNewField(false);
                dto.addColumn(old);
            } else if (Objects.equals(requested.get().getColumnType(), current.getColumnType())) {
                // coluna que já existe e veio no request
                requested.get().setNewField(false);
            } else {
                // erro se o request enviou uma coluna de tipo diferente da já cadastrada no banco
                throw new SchemaConflictException(
                        "Mudança de tipo na coluna %s: %s -> %s".formatted(
                                current.getColumnName(),
                                current.getColumnType(),
                                requested.get().getColumnType()));
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public String generate(GenTableRequest genTableReq, Boolean byAi) throws Exception {
        GenTableDTO genTableDTO = buildTableDTO(genTableReq);
        Map<String, String> originalFiles = genClassService
                .getOriginals(genTableReq.tableName(), byAi);
        try {
            genClassService.generateClasses(genTableDTO, byAi);
            genSqlService.generateTable(genTableDTO, byAi);
            //tableEventProducer.tableCreated(genTableReq.tableName());
            compile();
            return "ok";
        } catch (Exception e) {
            restoreFiles(originalFiles, e);
            return e.getMessage();
        }
    }

    private void restoreFiles(Map<String, String> originalFiles, Exception original) {
        try {
            genClassService.rollback(originalFiles);
            compile();
        } catch (Exception rollbackError) {
            original.addSuppressed(rollbackError);
        }
    }
}
