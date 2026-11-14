package com.example.practice1.movie.infra;

import com.example.practice1.movie.domain.Director;
import com.example.practice1.movie.domain.Movie;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MovieEntityMapper {

    Movie toDomain(MovieEntity entity);

    Director toDomain(DirectorEntity entity);
}
