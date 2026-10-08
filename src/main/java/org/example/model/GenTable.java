package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="gen_Table")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String tableName;
    private Boolean byAi;
    private LocalDate dataCriacao;
    private String frameworkCriacao;


    @OneToMany(mappedBy = "genTable")
    private List<GenColumn> genColumns;
}
