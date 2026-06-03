package org.example.repository;

import org.example.model.GenColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenColumnRepository extends JpaRepository<GenColumn, Long> {
}
