package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.GenColumnDTO;
import org.example.dto.GenTableDTO;
import org.example.model.GenColumn;
import org.example.model.GenTable;
import org.example.repository.GenColumnRepository;
import org.example.repository.GenTableRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
public class GeneratorSqlService {

    private final GenTableRepository genTableRepository;
    private final GenColumnRepository genColumnRepository;
    private final JdbcTemplate jdbcTemplate;

    private GenTable converter(GenTableDTO dto, Boolean byAi) {
        return new GenTable(null, dto.tableName(), byAi, LocalDate.now(), "Springboot", null);
    }

    private List<GenColumn> converter(List<GenColumnDTO> listDto, GenTable genTable) {
        return listDto
                .stream()
                .map(
                        dto-> new GenColumn(
                                null, dto.columnName(), dto.isUnique(),
                                dto.columnType(), dto.isNullable(),
                                dto.sizeColumn(), genTable)
                ).toList();
    }

    private void generateInDatabase(GenTableDTO genTableDTO) {

        StringJoiner sql = new StringJoiner(" ");
        sql
                .add("CREATE TABLE")
                .add(genTableDTO.tableName())
                .add("( id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY ");
        genTableDTO.genColumns()
                .forEach(c -> {
                    sql.add(",");
                    if (c.columnNameDatabase() != null) {
                        sql.add(c.columnNameDatabase());
                    } else {
                        sql.add(c.columnName()
                                .replaceAll("([a-z])([A-Z])", "$1_$2")
                                .toLowerCase());
                    }
                    sql.add(c.columnType().getSqlType());
                    if (c.columnType().getDefaultSize() != null)
                        sql.add("(")
                                .add(Objects.requireNonNullElse(
                                        c.sizeColumn(), c.columnType().getDefaultSize()))
                                .add(")");
                    if (!c.isNullable())
                        sql.add("NOT NULL");
                    if (c.isUnique())
                        sql.add("UNIQUE");
                });
        sql.add(")");
        jdbcTemplate.execute(sql.toString());
    }

    public void generateTable(GenTableDTO genTableDTO, Boolean byAi) {
        GenTable genTable = genTableRepository.save(converter(genTableDTO, byAi));
        genColumnRepository.saveAll(converter(genTableDTO.genColumns(), genTable));
        generateInDatabase(genTableDTO);
    }
}
