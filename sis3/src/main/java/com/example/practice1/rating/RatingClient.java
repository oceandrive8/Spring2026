package com.example.practice1.rating;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface RatingClient {

    @GetExchange("/")
    OmdbResponse find(@RequestParam("t") String title,
                      @RequestParam("apikey") String apiKey);
}
