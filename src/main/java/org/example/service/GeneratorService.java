package org.example.service;

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
public class GeneratorService {

    private final GenTableRepository genTableRepository;

    private final GenColumnRepository genColumnRepository;

    private final JdbcTemplate jdbcTemplate;


    public GeneratorService(GenTableRepository genTableRepository, GenColumnRepository genColumnRepository, JdbcTemplate jdbcTemplate) {
        this.genTableRepository = genTableRepository;
        this.genColumnRepository = genColumnRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    private GenTable converter(GenTableDTO dto) {
        return new GenTable(null, dto.getTableName(), null);
    }

    private List<GenColumn> converter(List<GenColumnDTO> listDto, GenTable genTable) {
        return listDto
            .stream()
            .map(
                dto-> new GenColumn(null, dto.getColumnName(), dto.getUnique(), dto.getColumnType(), dto.getNullable(), false, genTable)
            ).toList();
    }

    private void generateInDatabase(GenTableDTO genTableDTO) {
        StringBuilder sql = new StringBuilder();
        sql
                .append("CREATE TABLE ")
                .append(genTableDTO.getTableName())
                .append(" (id number generated always as identity primary key ");

        genTableDTO.getGenColumns()
                .forEach(c-> {
                    sql.append(", ")
                            .append(c.getColumnName()).append(" ")
                            .append(c.getColumnType());
                    if (!c.getNullable())
                        sql.append(" NOT NULL ");
                    if(c.getUnique())
                        sql.append(" UNIQUE ");
                });

        jdbcTemplate.execute(sql.toString());
    }

    public boolean generateTable(GenTableDTO genTableDTO) {
        try {
            GenTable genTable = genTableRepository.save(converter(genTableDTO));
            List<GenColumn> listGenColumn = genColumnRepository.saveAll(converter(genTableDTO.getGenColumns(), genTable));
            generateInDatabase(genTableDTO);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
