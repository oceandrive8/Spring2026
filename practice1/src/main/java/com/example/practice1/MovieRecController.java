package com.example.practice1;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/movie")
public class MovieRecController {

    private final List<Movie> movies=new ArrayList<>(
            List.of(
                    new Movie(
                            "Howl's Moving Castle",
                            "Fantasy",
                            2004,
                            "A beautiful movie about love,identity,war,friendship and finding your place in the world. And that if it was meant to happen it would happens anyways. "
                    )
            )
    );

    @GetMapping
    public List<Movie> getRec(){
        return movies;
    }


    @PostMapping
    public Movie createMovie(@RequestBody Movie movie){
        movies.add(movie);
        return movie;
    }
    @PatchMapping
    public String updateMovie(@RequestParam String title, @RequestParam String description ){
        for(int i=0; i< movies.size();i++){
            Movie movie=movies.get(i);
            if (movie.title().equals(title)){
                movies.set(i, new Movie(movie.title(), movie.genre(),movie.year(), description));
                return movie.title()+" was updated ";
            }

        }
        return "no movie was found";
    }
    @DeleteMapping
    public String deleteMovie(@RequestParam int year ){
        for (Movie movie : movies){
            if (movie.year() ==year){
                movies.remove(movie);
                return movie.title()+" was deleted cause it was filmed in "+year;
            }
        }
        return "No movie was filmed in "+year;
    }
}
