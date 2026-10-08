package org.example.dto;

import org.example.enums.ColumnType;

public record GenColumnDTO(

    String columnName,
    String columnNameDatabase,
    Boolean isUnique,
    ColumnType columnType,
    Boolean isNullable,
    String sizeColumn
) {}