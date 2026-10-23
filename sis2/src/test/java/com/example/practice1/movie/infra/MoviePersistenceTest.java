package com.example.practice1.movie.infra;

import com.example.practice1.movie.domain.Movie;
import com.example.practice1.movie.domain.MovieRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers

@Import({JpaMovieRepository.class, MovieEntityMapperImpl.class})
class MoviePersistenceTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16-alpine");

    @Autowired
    MovieRepository repository;

    private Movie movie(String title) {
        return new Movie(null, title, "Fantasy", 2004, "desc", null);
    }

    @Test
    void save_assignsId_andFindByIdReturnsIt() {
        Movie saved = repository.save(movie("Persist me"));

        assertThat(saved.id()).isNotNull();
        assertThat(repository.findById(saved.id()))
                .hasValueSatisfying(m -> assertThat(m.title()).isEqualTo("Persist me"));
    }

    @Test
    void existsByTitle_isTrueOnlyForSavedTitle() {
        repository.save(movie("Unique title"));

        assertThat(repository.existsByTitle("Unique title")).isTrue();
        assertThat(repository.existsByTitle("Never saved")).isFalse();
    }

    @Test
    void deleteById_removesMovie() {
        Movie saved = repository.save(movie("Delete me"));

        repository.deleteById(saved.id());

        assertThat(repository.findById(saved.id())).isEmpty();
    }

    @Test
    void save_withNullReleaseYear_isAllowed() { // proves V4 made the column nullable
        Movie saved = repository.save(new Movie(null, "No year", "Drama", null, "d", null));

        assertThat(repository.findById(saved.id()).orElseThrow().releaseYear()).isNull();
    }
}
