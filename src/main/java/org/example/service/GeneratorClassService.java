package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.GenColumnDTO;
import org.example.dto.GenTableDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeneratorClassService {

    private static String CAMINHO_BASE = "src/main/java/org/example/";

    private String getByAi(Boolean byAi) {
        return (byAi? "byAi": "automatic");
    }

    private String getCaminhoBase(Boolean byAi) {
        return CAMINHO_BASE+getByAi(byAi);
    }

    private String getCaminhoController(Boolean byAi) {
        return getCaminhoBase(byAi)+"/controller/";
    }

    private String getCaminhoModel(Boolean byAi) {
        return getCaminhoBase(byAi)+"/model/";
    }

    private String getCaminhoRepository(Boolean byAi) {
        return getCaminhoBase(byAi)+"/repository/";
    }

    private String getCaminhoService(Boolean byAi) {
        return getCaminhoBase(byAi)+"/service/";
    }

    private String getFileText(String caminho) throws IOException {
        Path arquivo = Paths.get(caminho);
        return Files.exists(arquivo) ? Files.readString(arquivo): null;
    }

    private String toClassName(String nomeTabela) {
        return nomeTabela.substring(0, 1).toUpperCase() + nomeTabela.substring(1).toLowerCase();
    }

    public Map<String, String> getOriginals(String nomeTabela, Boolean byAi) throws IOException {
        String nome = toClassName(nomeTabela);

        List<String> caminhos = List.of(
            getCaminhoController(byAi) + nome + "Controller.java",
            getCaminhoService(byAi) + nome + "Service.java",
            getCaminhoRepository(byAi) + nome + "Repository.java",
            getCaminhoModel(byAi) + nome + ".java");

        Map<String, String> arquivosOriginais = new HashMap<>();

        for (String caminho : caminhos) {
            arquivosOriginais.put(caminho, getFileText(caminho));
        }
        return arquivosOriginais;
    }

    public void rollback(Map<String,String> originals) throws IOException {
        IOException falha = null;

        for(Map.Entry<String,String> entry: originals.entrySet()) {
            Path arquivo = Paths.get(entry.getKey());
            try {
                if (entry.getValue() == null) {
                    //Apaga o arquivo gerado caso ele nao existia antes
                    Files.deleteIfExists(arquivo);
                } else {
                    //Escreve o arquivo antigo sobre o novo
                    Files.writeString(arquivo,entry.getValue());
                }
            } catch (IOException e) {
                log.error("Falha ao restaurar {}", arquivo, e);
                if (falha == null) {
                    falha = new IOException("Falha ao restaurar arquivos");
                }
                falha.addSuppressed(e);
            }
        }
        if (falha != null) {
            throw falha;
        }
    }

    private void generateController(String nomeTabela, Boolean byAi) throws IOException {

        nomeTabela = nomeTabela.substring(0,1).toUpperCase()+nomeTabela.substring(1).toLowerCase();
        Path arquivo = Paths.get(getCaminhoController(byAi)+nomeTabela+"Controller.java");
        String packageByAi = getByAi(byAi);
        StringJoiner textoArquivo = new StringJoiner(System.lineSeparator());
        textoArquivo
            .add("package org.example.%s.controller;".formatted(packageByAi))
            .add("")
            .add("import org.example.%s.model.%s;".formatted(packageByAi, nomeTabela))
            .add("import org.example.%s.service.%sService; ".formatted(packageByAi, nomeTabela))
            .add("import org.springframework.http.HttpStatus; ")
            .add("import org.springframework.http.ResponseEntity; ")
            .add("import org.springframework.web.bind.annotation.*;")
            .add("import java.util.List;")
            .add("")
            .add("@RestController")
            .add("@RequestMapping(\"/%s\")"
                    .formatted(nomeTabela.toLowerCase()))
            .add("public class %sController {"
                    .formatted(nomeTabela))
            .add("  private final %sService service;"
                    .formatted(nomeTabela))
            .add("")
            .add("  public %sController(%sService service) {"
                    .formatted(nomeTabela,nomeTabela))
            .add("      this.service = service;")
            .add("  }")
            .add("")
            .add("  @PostMapping")
            .add("  public ResponseEntity<%s> criar(@RequestBody %s entidade) {"
                    .formatted(nomeTabela,nomeTabela))
            .add("      return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(entidade));")
            .add("  }")
            .add("")
            .add("  @GetMapping")
            .add("  public ResponseEntity<List<%s>> listar() {"
                    .formatted(nomeTabela))
            .add("      return ResponseEntity.ok(service.listarTodos());")
            .add("  }")
            .add("")
            .add("  @GetMapping(\"/{id}\")")
            .add("  public ResponseEntity<%s> buscarPorId(@PathVariable Long id) {"
                    .formatted(nomeTabela))
            .add("      return ResponseEntity.ok(service.buscarPorId(id));")
            .add("  }")
            .add("")
            .add("  @PutMapping(\"/{id}\")")
            .add("  public ResponseEntity<%s> atualizar( @PathVariable Long id, @RequestBody %s entidade) {"
                    .formatted(nomeTabela,nomeTabela))
            .add("      %s atualizado = service.atualizar(id, entidade);"
                    .formatted(nomeTabela))
            .add("      return ResponseEntity.ok(atualizado);")
            .add("  }")
            .add("")
            .add("  @DeleteMapping(\"/{id}\")")
            .add("  public ResponseEntity<Void> excluir(@PathVariable Long id) {")
            .add("      service.excluir(id);")
            .add("      return ResponseEntity.noContent().build();")
            .add("  }")
            .add("")
            .add("}");
        System.out.println(textoArquivo);
        Files.writeString(arquivo,textoArquivo.toString());
    }

    private void generateService(String nomeTabela, Boolean byAi) throws IOException {

        nomeTabela = nomeTabela.substring(0,1).toUpperCase()+nomeTabela.substring(1).toLowerCase();
        Path arquivo = Paths.get(getCaminhoService(byAi)+nomeTabela+"Service.java");
        String packageByAi = getByAi(byAi);
        StringJoiner textoArquivo = new StringJoiner(System.lineSeparator());
        textoArquivo
            .add("package org.example.%s.service;".formatted(packageByAi))
            .add("")
            .add("import org.example.%s.model.%s;".formatted(packageByAi, nomeTabela))
            .add("import org.example.%s.repository.%sRepository;".formatted(packageByAi, nomeTabela))
            .add("import org.springframework.stereotype.Service;")
            .add("import org.springframework.transaction.annotation.Transactional;")
            .add("import org.springframework.http.HttpStatus;")
            .add("import org.springframework.web.server.ResponseStatusException;")
            .add("import java.util.List;")
            .add("")
            .add("@Service")
            .add("public class %sService {".formatted(nomeTabela))
            .add("    private final %sRepository repository;".formatted(nomeTabela))
            .add("    public %sService(%sRepository repository) {".formatted(nomeTabela,nomeTabela))
            .add("        this.repository = repository;")
            .add("    }")
            .add("")
            .add("    @Transactional")
            .add("    public %s salvar(%s entidade) {".formatted(nomeTabela,nomeTabela))
            .add("        return repository.save(entidade);")
            .add("    }")
            .add("")
            .add("    @Transactional(readOnly = true)")
            .add("    public List<%s> listarTodos() {".formatted(nomeTabela))
            .add("        return repository.findAll();")
            .add("    }")
            .add("")
            .add("    @Transactional(readOnly = true)")
            .add("    public %s buscarPorId(Long id) {".formatted(nomeTabela))
            .add("        return repository.findById(id)")
            .add("            .orElseThrow(() ->")
            .add("                new ResponseStatusException( HttpStatus.NOT_FOUND,")
            .add("                    \"Não encontrado: \" + id ));")
            .add("    }")
            .add("")
            .add("    @Transactional")
            .add("    public %s atualizar(Long id, %s entidade) {".formatted(nomeTabela,nomeTabela))
            .add("        %s existente = buscarPorId(id);".formatted(nomeTabela))
            .add("")
            .add("        return repository.save(existente);")
            .add("    }")
            .add("    ")
            .add("    @Transactional")
            .add("    public void excluir(Long id) {")
            .add("        %s entidade = buscarPorId(id);".formatted(nomeTabela))
            .add("        repository.delete(entidade);")
            .add("    }")
            .add("}");
        System.out.println(textoArquivo);
        Files.writeString(arquivo,textoArquivo.toString());
    }

    private void generateEntity(GenTableDTO genTableDTO, Boolean byAi) throws IOException {
        String nomeTabela = toClassName(genTableDTO.getTableName());
        String packageByAi = getByAi(byAi);
        Path arquivo = Paths.get(getCaminhoModel(byAi)+nomeTabela+".java");
        StringJoiner textoArquivo = new StringJoiner(System.lineSeparator());
        textoArquivo.add("package org.example.%s.model;".formatted(packageByAi))
                .add("import jakarta.persistence.*;")
                .add("import lombok.AllArgsConstructor;")
                .add("import lombok.Getter;")
                .add("import lombok.NoArgsConstructor;")
                .add("import lombok.Setter;")
                .add("import java.time.LocalDate;")
                .add("import java.math.BigDecimal;")
                .add("@Getter")
                .add("@Setter")
                .add("@NoArgsConstructor")
                .add("@AllArgsConstructor")
                .add("@Entity")
                .add("public class %s {".formatted(nomeTabela))
                .add("@Id")
                .add("@GeneratedValue(strategy = GenerationType.IDENTITY)")
                .add("private Long id;");
        for (GenColumnDTO c : genTableDTO.getGenColumns()) {
            List<String> atributos = new ArrayList<>();
            if (c.getColumnNameDatabase() != null)
                atributos.add("name = \"%s\"".formatted(c.getColumnNameDatabase()));
            if (Boolean.TRUE.equals(c.getIsUnique()))
                atributos.add("unique = true");
            if (Boolean.FALSE.equals(c.getIsNullable()))
                atributos.add("nullable = false");
            if (!atributos.isEmpty())
                textoArquivo.add("@Column(" + String.join(", ", atributos) + ")");

            textoArquivo.add("private %s %s;".formatted(c.getColumnType().getJavaType(),c.getColumnName()));
        }
        textoArquivo.add("}");
        Files.writeString(arquivo, textoArquivo.toString());
    }

    private void generateRepository(String nomeTabela, Boolean byAi) throws IOException {
        nomeTabela = nomeTabela.substring(0,1).toUpperCase()+nomeTabela.substring(1).toLowerCase();
        Path arquivo = Paths.get(getCaminhoRepository(byAi)+nomeTabela+"Repository.java");
        String packageByAi = getByAi(byAi);
        StringJoiner textoArquivo = new StringJoiner(System.lineSeparator());
        textoArquivo.add("package org.example.%s.repository;".formatted(packageByAi))
            .add("")
            .add("import org.example.%s.model.%s;".formatted(packageByAi, nomeTabela))
            .add("import org.springframework.data.jpa.repository.JpaRepository;")
            .add("import org.springframework.stereotype.Repository;")
            .add("@Repository")
            .add("public interface %sRepository extends JpaRepository<%s, Long> {"
                    .formatted(nomeTabela, nomeTabela)
            ).add("}");
        System.out.println(textoArquivo);
        Files.writeString(arquivo, textoArquivo.toString());
    }


    public void generateClasses(GenTableDTO genTableDTO, Boolean byAi) throws IOException {
        String nomeTabela = toClassName(genTableDTO.getTableName());

        generateEntity(genTableDTO, byAi);
        generateRepository(nomeTabela,byAi);
        generateService(nomeTabela,byAi);
        generateController(nomeTabela, byAi);
    }
}
