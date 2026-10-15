package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.enums.ColumnType;
import org.example.model.GenColumn;

@AllArgsConstructor
@Getter
@Setter
public class GenColumnDTO {
    private String columnName;
    private String columnNameDatabase;
    private Boolean isUnique;
    private ColumnType columnType;
    private Boolean isNullable;
    private String sizeColumn;
    private Boolean newField;

    public GenColumnDTO() {
        this.newField=true;
    }

    public GenColumnDTO(GenColumn gc) {
        this.columnName = gc.getColumnName();
//        this.columnNameDatabase = gc.getColumnNameDatabase();
        this.isUnique = gc.getIsUnique();
        this.columnType = gc.getColumnType();
        this.isNullable = gc.getIsNullable();
        this.sizeColumn = gc.getSizeColumn();
        this.newField = true;
    }
}