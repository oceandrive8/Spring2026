INSERT INTO directors (name, country) VALUES
                                          ('Christopher Nolan', 'UK'),
                                          ('Denis Villeneuve', 'Canada'),
                                          ('Greta Gerwig', 'USA');

INSERT INTO movies (title, genre, release_year, director_id) VALUES
                                                                 ('Inception',     'Sci-Fi', 2010, (SELECT id FROM directors WHERE name = 'Christopher Nolan')),
                                                                 ('Interstellar',  'Sci-Fi', 2014, (SELECT id FROM directors WHERE name = 'Christopher Nolan')),
                                                                 ('The Dark Knight','Action', 2008, (SELECT id FROM directors WHERE name = 'Christopher Nolan')),
                                                                 ('Dune',          'Sci-Fi', 2021, (SELECT id FROM directors WHERE name = 'Denis Villeneuve')),
                                                                 ('Arrival',       'Sci-Fi', 2016, (SELECT id FROM directors WHERE name = 'Denis Villeneuve')),
                                                                 ('Barbie',        'Comedy', 2023, (SELECT id FROM directors WHERE name = 'Greta Gerwig')),
                                                                 ('Lady Bird',     'Drama',  2017, (SELECT id FROM directors WHERE name = 'Greta Gerwig'));