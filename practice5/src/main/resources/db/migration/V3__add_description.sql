ALTER TABLE movies ADD COLUMN description TEXT;

UPDATE movies SET description = 'Seed movie: ' || title;