-- Create the service_request table with a spatial column

CREATE EXTENSION IF NOT EXISTS postgis; 

CREATE TABLE service_request (
    id          UUID PRIMARY KEY,
    category    VARCHAR(50)  NOT NULL,
    description TEXT,
    status      VARCHAR(30)  NOT NULL,
    location    geometry(Point, 4326) NOT NULL,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT now()
);

-- Spatial index: needed later for ward lookup and duplicate detection
CREATE INDEX idx_service_request_location
    ON service_request USING GIST (location);