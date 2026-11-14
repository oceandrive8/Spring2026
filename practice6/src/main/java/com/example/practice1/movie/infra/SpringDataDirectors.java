package com.example.practice1.movie.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SpringDataDirectors extends JpaRepository<DirectorEntity, Long> {
    Optional<DirectorEntity> findByNameIgnoreCase(String name);
}