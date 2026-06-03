package org.example.model;

import jakarta.persistence.*;

@Entity
@Table(name="genColumn")
public class GenColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String columnName;
    private Boolean isUnique;
    private String columnType;
    private Boolean isNullable;
    private Boolean byAi;

    @ManyToOne
    @JoinColumn(name="id_gentable", nullable = false)
    private GenTable genTable;

    public GenColumn() {
    }

    public GenColumn(Long id, String columnName, Boolean isUnique, String columnType, Boolean isNullable, Boolean byAi, GenTable genTable) {
        this.id = id;
        this.columnName = columnName;
        this.isUnique = isUnique;
        this.columnType = columnType;
        this.isNullable = isNullable;
        this.byAi = byAi;
        this.genTable = genTable;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Boolean getUnique() {
        return isUnique;
    }

    public void setUnique(Boolean unique) {
        isUnique = unique;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public Boolean getNullable() {
        return isNullable;
    }

    public void setNullable(Boolean nullable) {
        isNullable = nullable;
    }

    public Boolean getByAi() {
        return byAi;
    }

    public void setByAi(Boolean byAi) {
        this.byAi = byAi;
    }

    public GenTable getGenTable() {
        return genTable;
    }

    public void setGenTable(GenTable genTable) {
        this.genTable = genTable;
    }
}
