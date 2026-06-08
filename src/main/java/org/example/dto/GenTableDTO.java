package org.example.dto;

import java.util.List;

public record GenTableDTO(

    String tableName,
    List<GenColumnDTO> genColumns
){}