package com.example.practice1.rating;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OmdbResponse(@JsonProperty("Title") String title,
                           @JsonProperty("imdbRating") String imdbRating) { }
