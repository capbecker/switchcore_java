package org.example.dto;

import java.util.List;

public class GenTableDTO {

    private String tableName;
    private List<GenColumnDTO> genColumns;

    public GenTableDTO() {
    }

    public GenTableDTO(String tableName, List<GenColumnDTO> genColumns) {
        this.tableName = tableName;
        this.genColumns = genColumns;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<GenColumnDTO> getGenColumns() {
        return genColumns;
    }

    public void setGenColumns(List<GenColumnDTO> genColumns) {
        this.genColumns = genColumns;
    }
}
