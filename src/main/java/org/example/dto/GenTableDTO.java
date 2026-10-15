package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class GenTableDTO{

    private String tableName;
    private List<GenColumnDTO> genColumns;
    private Boolean newTable;

    public GenTableDTO() {
        this.genColumns = new ArrayList<>();
        this.newTable = true;
    }

    public void addColumn(GenColumnDTO genColumnDTO) {
        this.genColumns.add(genColumnDTO);
    }
}