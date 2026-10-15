package org.example.repository;

import org.example.model.GenTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenTableRepository extends JpaRepository<GenTable, Long> {
    Optional<GenTable> findByTableNameIgnoreCase(String tableName);
}
