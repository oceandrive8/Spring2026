package com.example.practice1.movie.app;

import com.example.practice1.movie.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    MovieRepository repository;

    @Mock
    MovieQueries queries;

    @InjectMocks
    MovieService service;

    private Movie movie(Long id, String title) {
        return new Movie(id, title, "Fantasy", 2004, "desc", null);
    }

    // ---------- create ----------

    @Test
    void create_savesMovie_whenTitleIsFree() {
        Movie input = movie(null, "Howl's Moving Castle");
        Movie saved = movie(1L, "Howl's Moving Castle");
        when(repository.existsByTitle("Howl's Moving Castle")).thenReturn(false);
        when(repository.save(input)).thenReturn(saved);

        Movie result = service.create(input);

        assertThat(result).isEqualTo(saved);
        verify(repository).save(input);
    }

    @Test
    void create_throwsConflict_whenTitleAlreadyExists() {
        Movie input = movie(null, "Spirited Away");
        when(repository.existsByTitle("Spirited Away")).thenReturn(true);

        assertThatThrownBy(() -> service.create(input))
                .isInstanceOf(MovieConflictException.class)
                .hasMessageContaining("Spirited Away");

        verify(repository, never()).save(any());
    }

    // ---------- get ----------

    @Test
    void get_returnsMovie_whenFound() {
        Movie found = movie(1L, "Ponyo");
        when(repository.findById(1L)).thenReturn(Optional.of(found));

        assertThat(service.get(1L)).isEqualTo(found);
    }

    @Test
    void get_throwsNotFound_whenMissing() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.get(99L))
                .isInstanceOf(MovieNotFoundException.class);
    }

    // ---------- update ----------

    @Test
    void update_keepsExistingDirector_andAppliesChanges() {
        Director director = mock(Director.class);
        Movie existing = new Movie(1L, "Old title", "Drama", 2000, "old", director);
        Movie changes = new Movie(null, "New title", "Fantasy", 2004, "new", null);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Movie.class))).thenAnswer(inv -> inv.getArgument(0));

        Movie result = service.update(1L, changes);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.title()).isEqualTo("New title");
        assertThat(result.genre()).isEqualTo("Fantasy");
        assertThat(result.releaseYear()).isEqualTo(2004);
        assertThat(result.description()).isEqualTo("new");
        assertThat(result.director()).isSameAs(director);
    }

    @Test
    void update_throwsNotFound_whenMissing() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(99L, movie(null, "Any")))
                .isInstanceOf(MovieNotFoundException.class);

        verify(repository, never()).save(any());
    }

    // ---------- delete ----------

    @Test
    void delete_deletesMovie_whenFound() {
        when(repository.findById(1L)).thenReturn(Optional.of(movie(1L, "Ponyo")));

        service.delete(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void delete_throwsNotFound_andDoesNotDelete_whenMissing() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(99L))
                .isInstanceOf(MovieNotFoundException.class);

        verify(repository, never()).deleteById(any());
    }

    // ---------- list (delegation) ----------

    @Test
    void list_delegatesToQueries() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<Movie> page = new PageImpl<>(List.of(movie(1L, "Ponyo")));
        when(queries.list(pageable)).thenReturn(page);

        assertThat(service.list(pageable)).isSameAs(page);
    }
}
