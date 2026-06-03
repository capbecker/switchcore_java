package org.example.repository;

import org.example.model.GenTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenTableRepository extends JpaRepository<GenTable, Long> {
}
