package org.example.repository;

import org.example.model.GenColumn;
import org.example.model.GenTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenColumnRepository extends JpaRepository<GenColumn, Long> {
    Long deleteByGenTable(GenTable gt);
}
