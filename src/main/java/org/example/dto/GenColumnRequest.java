package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.example.enums.ColumnType;

public record GenColumnRequest(

    @NotBlank(message = "columnName é obrigatório")
    @Pattern(regexp = "^[a-z][A-Za-z0-9]*$",
            message = "columnName deve estar em camelCase, só letras e números")
    String columnName,

    @Pattern(regexp = "^[a-z][a-z0-9_]*$",
            message = "columnNameDatabase deve estar em snake_case")
    String columnNameDatabase,

    Boolean isUnique,

    @NotNull(message = "columnType é obrigatório")
    ColumnType columnType,

    Boolean isNullable,

    @Pattern(regexp = "^\\d+(,\\d+)?$",
            message = "sizeColumn deve ser um número ou 'precisão,escala'")
    String sizeColumn
) {
    public GenColumnRequest {
        if (isUnique == null) isUnique = false;
        if (isNullable == null) isNullable = true;
    }
}