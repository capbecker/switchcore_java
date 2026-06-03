package org.example.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="GenTable")
public class GenTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tableName;

    @OneToMany(mappedBy = "genTable")
    private List<GenColumn> genColumns;

    public GenTable() {
    }

    public GenTable(Long id, String tableName, List<GenColumn> genColumns) {
        this.id = id;
        this.tableName = tableName;
        this.genColumns = genColumns;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<GenColumn> getGenColumns() {
        return genColumns;
    }

    public void setGenColumns(List<GenColumn> genColumns) {
        this.genColumns = genColumns;
    }
}
