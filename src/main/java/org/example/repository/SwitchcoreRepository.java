package org.example.repository;

import org.example.model.Switchcore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SwitchcoreRepository extends JpaRepository<Switchcore, Long> {
    Optional<Switchcore> findByService(String java);
}
