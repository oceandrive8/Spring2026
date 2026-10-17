package com.example.practice1.repository;

import com.example.practice1.entity.DirectorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DirectorRepository extends JpaRepository<DirectorEntity, Long> {
    Optional<DirectorEntity> findByNameIgnoreCase(String name);
}