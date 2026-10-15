package org.example.dto;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record GenTableRequest(

    @NotBlank(message = "tableName é obrigatório")
    @Pattern(regexp = "^[A-Za-z][A-Za-z0-9]{0,62}$",
            message = "tableName deve ter só letras e números e começar com letra")
    String tableName,

    @NotEmpty(message = "informe ao menos uma coluna")
    @Valid
    List<GenColumnRequest> genColumns
){}