CREATE TABLE directors (
                           id          BIGSERIAL PRIMARY KEY,
                           name        VARCHAR(255) NOT NULL,
                           country     VARCHAR(100)
);

CREATE TABLE movies (
                        id            BIGSERIAL PRIMARY KEY,
                        title         VARCHAR(255) NOT NULL,
                        genre         VARCHAR(100),
                        release_year  INTEGER,
                        director_id   BIGINT REFERENCES directors(id)
);

CREATE INDEX idx_movies_director_id ON movies(director_id);