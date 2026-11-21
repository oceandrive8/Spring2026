package com.example.practice1.movie.web;

import com.example.practice1.movie.app.MovieService;
import com.example.practice1.movie.domain.Movie;
import com.example.practice1.movie.domain.MovieConflictException;
import com.example.practice1.movie.domain.MovieNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MovieRecController.class)
@Import({GlobalExceptionHandler.class, MovieApiMapperImpl.class}) // handler + generated mapper
class MovieControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    MovieService service;

    @Test
    void getById_returns200_withBody() throws Exception {
        when(service.get(1L)).thenReturn(new Movie(1L, "Ponyo", "Fantasy", 2008, "desc", null));

        mockMvc.perform(get("/api/movie/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Ponyo"));
    }

    @Test
    void getById_returns404_whenMissing() throws Exception {
        when(service.get(99L)).thenThrow(new MovieNotFoundException(99L));

        mockMvc.perform(get("/api/movie/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void post_returns409_whenTitleExists() throws Exception {
        when(service.create(any(Movie.class))).thenThrow(new MovieConflictException("already exists"));

        mockMvc.perform(post("/api/movie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"Ponyo","genre":"Fantasy","year":2008,"description":"d"}
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("already exists"));
    }

    @Test
    void post_returns400_whenYearIsInvalid() throws Exception {
        mockMvc.perform(post("/api/movie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"Old","genre":"Fantasy","year":1800,"description":"d"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.year").exists());
    }

    @Test
    void post_returns400_whenTitleIsBlank() throws Exception {
        mockMvc.perform(post("/api/movie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"","genre":"Fantasy","year":2008,"description":"d"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.title").exists());
    }
}
