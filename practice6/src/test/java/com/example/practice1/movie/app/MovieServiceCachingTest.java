package com.example.practice1.movie.app;

import com.example.practice1.movie.domain.Movie;
import com.example.practice1.movie.domain.MovieNotFoundException;
import com.example.practice1.movie.domain.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {MovieService.class, MovieServiceCachingTest.TestConfig.class})
class MovieServiceCachingTest {

    @Configuration
    @EnableCaching
    static class TestConfig {
        @Bean
        CacheManager cacheManager() {
            return new ConcurrentMapCacheManager("movies");
        }
    }

    @MockitoBean
    MovieRepository repository;

    @MockitoBean
    MovieQueries queries;

    @Autowired
    MovieService service;

    @Autowired
    CacheManager cacheManager;

    @BeforeEach
    void clearCache() {
        cacheManager.getCache("movies").clear();
    }

    private Movie movie(Long id, String title) {
        return new Movie(id, title, "Fantasy", 2008, "desc", null);
    }

    @Test
    void get_secondCallIsServedFromCache() {
        when(repository.findById(1L)).thenReturn(Optional.of(movie(1L, "Ponyo")));

        Movie first = service.get(1L);
        Movie second = service.get(1L);

        assertThat(second).isEqualTo(first);
        verify(repository, times(1)).findById(1L);   // в базу сходили один раз
    }

    @Test
    void get_doesNotCacheNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.get(99L)).isInstanceOf(MovieNotFoundException.class);
        assertThatThrownBy(() -> service.get(99L)).isInstanceOf(MovieNotFoundException.class);

        verify(repository, times(2)).findById(99L);  // ошибка не кешируется
    }

    @Test
    void update_refreshesCache() {
        when(repository.findById(3L)).thenReturn(Optional.of(movie(3L, "Old title")));
        when(repository.save(any(Movie.class))).thenAnswer(inv -> inv.getArgument(0));

        service.update(3L, movie(null, "New title"));   // внутренний get идёт мимо кеша, затем @CachePut
        Movie fromCache = service.get(3L);               // должен прийти из кеша

        assertThat(fromCache.title()).isEqualTo("New title");
        verify(repository, times(1)).findById(3L);       // только внутри update
    }

    @Test
    void delete_evictsCache() {
        when(repository.findById(2L)).thenReturn(Optional.of(movie(2L, "Ponyo")));

        service.get(2L);      // findById #1, кладёт в кеш
        service.delete(2L);   // findById #2 (внутренний get), deleteById, очистка кеша
        service.get(2L);      // кеша нет: findById #3

        verify(repository, times(3)).findById(2L);
        verify(repository).deleteById(2L);
    }

    @Test
    void create_doesNotTouchRepositoryRead() {
        Movie input = movie(null, "Spirited Away");
        when(repository.existsByTitle("Spirited Away")).thenReturn(false);
        when(repository.save(input)).thenReturn(movie(5L, "Spirited Away"));

        service.create(input);

        verify(repository, never()).findById(any());
    }
}
