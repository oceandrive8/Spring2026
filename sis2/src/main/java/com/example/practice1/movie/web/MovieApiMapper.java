package com.example.practice1.movie.web;

import com.example.practice1.movie.domain.Director;
import com.example.practice1.movie.domain.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MovieApiMapper {

    @Mapping(source = "releaseYear", target = "year")
    MovieResponse toResponse(Movie movie);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "year", target = "releaseYear")
    @Mapping(target = "director", expression = "java(toDirector(request))")
    Movie toDomain(MovieRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "year", target = "releaseYear")
    @Mapping(target = "director", ignore = true)
    Movie toDomain(MovieUpdateRequest request);

    default Director toDirector(MovieRequest r) {
        return r.directorName() == null || r.directorName().isBlank()
                ? null
                : new Director(null, r.directorName(), r.directorCountry());
    }
}
