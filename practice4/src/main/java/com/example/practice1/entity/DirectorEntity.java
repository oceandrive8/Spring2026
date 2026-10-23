package com.example.practice1.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "directors")
public class DirectorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String country;

    @OneToMany(mappedBy = "director", fetch = FetchType.LAZY)
    private List<MovieEntity> movies = new ArrayList<>();

    protected DirectorEntity() {}

    public DirectorEntity(String name, String country) {
        this.name = name;
        this.country = country;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public List<MovieEntity> getMovies() { return movies; }
}