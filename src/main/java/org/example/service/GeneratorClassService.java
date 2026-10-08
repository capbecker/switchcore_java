package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.GenTableDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
public class GeneratorClassService {

    public void generateController(GenTableDTO genTableDTO, Boolean byAi) throws IOException {

        String caminho = "src/main/java/org/example/automatic/controller/";
        String nomeTabela = genTableDTO.tableName();
        nomeTabela = nomeTabela.substring(0,1).toUpperCase()+nomeTabela.substring(1).toLowerCase();
        Path arquivo = Paths.get(caminho+nomeTabela+"Controller.java");
        Files.writeString(arquivo, """
            package org.example.automatic.controller;                
            import org.springframework.web.bind.annotation.GetMapping;
            import org.springframework.web.bind.annotation.RequestMapping;
            import org.springframework.web.bind.annotation.RestController;                
            @RestController
            @RequestMapping("/%s")
            public class %sController {
                @GetMapping("/ola") public String ola() { return "gerado!"; }
            }
        """.formatted(nomeTabela.toLowerCase(), nomeTabela));
    }

    public void generateEntity(GenTableDTO genTableDTO, Boolean byAi) throws IOException {
        String caminho = "src/main/java/org/example/automatic/model/";
        String nomeTabela = genTableDTO.tableName();
        nomeTabela = nomeTabela.substring(0,1).toUpperCase()+nomeTabela.substring(1).toLowerCase();
        Path arquivo = Paths.get(caminho+nomeTabela+".java");
        StringJoiner textoArquivo = new StringJoiner(System.lineSeparator());
        textoArquivo.add("package org.example.automatic.model;")
                .add("import jakarta.persistence.*;")
                .add("import lombok.AllArgsConstructor;")
                .add("import lombok.Getter;")
                .add("import lombok.NoArgsConstructor;")
                .add("import lombok.Setter;")
                .add("import java.time.LocalDate;")
                .add("@Entity")
                .add("public class %s {".formatted(nomeTabela))
                .add("@Id")
                .add("@GeneratedValue(strategy = GenerationType.IDENTITY)")
                .add("private Long id;");
        genTableDTO.genColumns().forEach(c-> {
            StringJoiner columnAnotation = new StringJoiner(", ", "@Column(", ")");
            if (c.columnNameDatabase()!=null)
                columnAnotation.add("name = %s".formatted(c.columnNameDatabase()));
            if (c.isUnique()!=null && c.isUnique())
                columnAnotation.add("unique = true");
            if (c.isNullable()!=null && !c.isNullable() )
                columnAnotation.add("nullable  = false");
            if (columnAnotation.length()>10)
                textoArquivo.add(columnAnotation.toString());
            textoArquivo.add("private %s %s;".formatted(c.columnType().getJavaType(),c.columnName()));
        });
        textoArquivo.add("}");
        Files.writeString(arquivo, textoArquivo.toString());
    }
}
