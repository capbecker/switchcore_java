package org.example.dto;

public record GenColumnDTO(

    String columnName,
    Boolean isUnique,
    String columnType,
    Boolean isNullable
) {}