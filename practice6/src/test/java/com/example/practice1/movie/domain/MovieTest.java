package com.example.practice1.movie.domain;

import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MovieTest {

    private Movie movie(String title, Integer year) {
        return new Movie(null, title, "Fantasy", year, "desc", null);
    }

    @Test
    void createsMovie_whenDataIsValid() {
        Movie m = movie("Ponyo", 2008);

        assertThat(m.title()).isEqualTo("Ponyo");
        assertThat(m.releaseYear()).isEqualTo(2008);
    }

    @Test
    void rejectsNullTitle() {
        assertThatThrownBy(() -> movie(null, 2008))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Title must not be blank");
    }

    @Test
    void rejectsBlankTitle() {
        assertThatThrownBy(() -> movie("   ", 2008))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsYearBefore1888() {
        assertThatThrownBy(() -> movie("Old", 1887))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("1887");
    }

    @Test
    void acceptsYear1888() {
        assertThatCode(() -> movie("Roundhay Garden Scene", 1888)).doesNotThrowAnyException();
    }

    @Test
    void rejectsYearInTheFuture() {
        int nextYear = Year.now().getValue() + 1;

        assertThatThrownBy(() -> movie("Future", nextYear))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void acceptsCurrentYear() {
        assertThatCode(() -> movie("This year", Year.now().getValue())).doesNotThrowAnyException();
    }

    @Test
    void acceptsNullYear() {
        assertThatCode(() -> movie("Unknown year", null)).doesNotThrowAnyException();
    }

    @Test
    void withDescription_returnsCopyWithNewDescription() {
        Movie original = movie("Ponyo", 2008);

        Movie changed = original.withDescription("new");

        assertThat(changed.description()).isEqualTo("new");
        assertThat(changed.title()).isEqualTo(original.title());
        assertThat(original.description()).isEqualTo("desc");
    }
}
