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

import java.util.List;

@Service
@RequiredArgsConstructor
public class GeneratorService {

    private final GenTableRepository genTableRepository;
    private final GenColumnRepository genColumnRepository;
    private final JdbcTemplate jdbcTemplate;

    private GenTable converter(GenTableDTO dto) {
        return new GenTable(null, dto.tableName(), null);
    }

    private List<GenColumn> converter(List<GenColumnDTO> listDto, GenTable genTable) {
        return listDto
            .stream()
            .map(
                dto-> new GenColumn(null, dto.columnName(), dto.isUnique(), dto.columnType(), dto.isNullable(), false, genTable)
            ).toList();
    }

    private void generateInDatabase(GenTableDTO genTableDTO) {
        StringBuilder sql = new StringBuilder();
        sql
                .append("CREATE TABLE ")
                .append(genTableDTO.tableName())
                .append(" (id number generated always as identity primary key ");

        genTableDTO.genColumns()
                .forEach(c-> {
                    sql.append(", ")
                            .append(c.columnName()).append(" ")
                            .append(c.columnType());
                    if (!c.isNullable())
                        sql.append(" NOT NULL ");
                    if(c.isUnique())
                        sql.append(" UNIQUE ");
                });

        jdbcTemplate.execute(sql.toString());
    }

    public boolean generateTable(GenTableDTO genTableDTO) {
        try {
            GenTable genTable = genTableRepository.save(converter(genTableDTO));
            List<GenColumn> listGenColumn = genColumnRepository.saveAll(converter(genTableDTO.genColumns(), genTable));
            generateInDatabase(genTableDTO);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
