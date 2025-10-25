-- Create the PostGIS extension
CREATE EXTENSION IF NOT EXISTS postgis;

CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE INDEX IF NOT EXISTS idx_species_speciesname_trgm
    ON species USING GIN (species_name gin_trgm_ops);