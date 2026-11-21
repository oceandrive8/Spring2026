-- Some rows have release_year = 0 (placeholder values), which the domain rejects.
-- Make sure the column accepts NULL (does nothing if it already does).
ALTER TABLE movies ALTER COLUMN release_year DROP NOT NULL;

-- Unknown years become NULL instead of an invalid number.
UPDATE movies SET release_year = NULL WHERE release_year < 1888;

-- Optional: restore a known year for a specific movie.
UPDATE movies SET release_year = 2004 WHERE title = 'Howl''s Moving Castle';