package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.GenColumnDTO;
import org.example.dto.GenColumnRequest;
import org.example.dto.GenTableDTO;
import org.example.dto.GenTableRequest;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GeneratorSqlService {

    private final GenTableRepository genTableRepository;
    private final GenColumnRepository genColumnRepository;
    private final JdbcTemplate jdbcTemplate;

    private GenTable converter(GenTableDTO dto, Boolean byAi) {
        return new GenTable(null, dto.getTableName(), byAi, LocalDate.now(), "Springboot", null);
    }

    private List<GenColumn> converter(List<GenColumnDTO> listDto, GenTable genTable) {
        return listDto
                .stream()
                .filter(GenColumnDTO::getNewField)
                .map(
                    dto-> new GenColumn(
                        null, dto.getColumnName(), dto.getIsUnique(),
                        dto.getColumnType(), dto.getIsNullable(),
                        dto.getSizeColumn(), genTable)
                ).toList();
    }

    private void generateInDatabase(GenTableDTO genTableDTO) {

        StringJoiner sql = new StringJoiner(" ");
        sql
                .add("CREATE TABLE")
                .add(genTableDTO.getTableName())
                .add("( id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY ");
        genTableDTO.getGenColumns()
                .forEach(c -> {
                    sql.add(",");
                    if (c.getColumnNameDatabase() != null) {
                        sql.add(c.getColumnNameDatabase());
                    } else {
                        sql.add(c.getColumnName()
                                .replaceAll("([a-z])([A-Z])", "$1_$2")
                                .toLowerCase());
                    }
                    sql.add(c.getColumnType().getSqlType());
                    if (c.getColumnType().getDefaultSize() != null)
                        sql.add("(")
                                .add(Objects.requireNonNullElse(
                                        c.getSizeColumn(), c.getColumnType().getDefaultSize()))
                                .add(")");
                    if (!c.getIsNullable())
                        sql.add("NOT NULL");
                    if (c.getIsUnique())
                        sql.add("UNIQUE");
                });
        sql.add(")");
        System.out.println(sql);
        jdbcTemplate.execute(sql.toString());
    }

    public void generateTable(GenTableDTO genTableDTO, Boolean byAi) {
        GenTable genTable;
        if (genTableDTO.getNewTable()) {
            genTable = genTableRepository.save(converter(genTableDTO, byAi));
            genColumnRepository.saveAll(converter(genTableDTO.getGenColumns(), genTable));
            generateInDatabase(genTableDTO);
        } else {
            genTable = genTableRepository
                    .findByTableNameIgnoreCase(genTableDTO.getTableName())
                    .orElseThrow();
            genColumnRepository.saveAll(converter(genTableDTO.getGenColumns(), genTable));
            alterInDatabase(genTableDTO);
        }
        System.out.println(genTable);
    }

    private void alterInDatabase(GenTableDTO genTableDTO) {
        String columns =
            genTableDTO.getGenColumns().stream().filter(GenColumnDTO::getNewField)
                .map(c -> {
                    StringJoiner sj = new StringJoiner(" ");
                    sj.add("ADD COLUMN");
                    if (c.getColumnNameDatabase() != null) {
                        sj.add(c.getColumnNameDatabase());
                    } else {
                        sj.add(c.getColumnName()
                                .replaceAll("([a-z])([A-Z])", "$1_$2")
                                .toLowerCase());
                    }
                    sj.add(c.getColumnType().getSqlType());
                    if (c.getColumnType().getDefaultSize() != null)
                        sj.add("(")
                                .add(Objects.requireNonNullElse(
                                        c.getSizeColumn(), c.getColumnType().getDefaultSize()))
                                .add(")");
                    if (!c.getIsNullable())
                        sj.add("NOT NULL");
                    if (c.getIsUnique())
                        sj.add("UNIQUE");
                    return sj.toString();
                }).collect(Collectors.joining(","));
        String sql = "ALTER TABLE "+genTableDTO.getTableName()+ " " + columns;
        System.out.println(sql);
        jdbcTemplate.execute(sql);
    }
}
