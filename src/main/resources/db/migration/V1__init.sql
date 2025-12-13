-- Create the PostGIS extension
CREATE EXTENSION IF NOT EXISTS postgis;

CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE INDEX IF NOT EXISTS idx_species_speciesname_trgm
    ON species USING GIN (species_name gin_trgm_ops);

CREATE TABLE users (
                       username VARCHAR(50) NOT NULL,
                       password VARCHAR(100) NOT NULL,
                       enabled SMALLINT NOT NULL DEFAULT 1,
                       PRIMARY KEY (username)
);

CREATE TABLE authorities (
                             username VARCHAR(50) NOT NULL,
                             authority VARCHAR(50) NOT NULL,
                             FOREIGN KEY (username) REFERENCES users(username)
);

CREATE UNIQUE INDEX ix_auth_username
    on authorities (username,authority);