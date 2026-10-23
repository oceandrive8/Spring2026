package com.example.practice1.movie.infra;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringDataMovies extends JpaRepository<MovieEntity, Long> {

    // derived query
    Page<MovieEntity> findByGenreIgnoreCase(String genre, Pageable pageable);


    // @Query with JOIN FETCH to avoid N+1 on the lazy director
    @Query(value = "SELECT m FROM MovieEntity m JOIN FETCH m.director d WHERE d.name = :name",
            countQuery = "SELECT COUNT(m) FROM MovieEntity m WHERE m.director.name = :name")
    Page<MovieEntity> findByDirectorName(@Param("name") String name, Pageable pageable);
    boolean existsByTitleIgnoreCase(String title);
}