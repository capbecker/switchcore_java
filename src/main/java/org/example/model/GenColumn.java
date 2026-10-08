package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.enums.ColumnType;

@Entity
@Table(name="gen_Column")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String columnName;
    private Boolean isUnique;
    @Enumerated(EnumType.STRING)
    private ColumnType columnType;
    private Boolean isNullable;
    private String sizeColumn;

    @ManyToOne
    @JoinColumn(name="id_gentable", nullable = false)
    private GenTable genTable;
}
