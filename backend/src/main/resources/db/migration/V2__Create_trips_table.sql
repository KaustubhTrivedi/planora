-- Create trip status enum type
CREATE TYPE trip_status AS ENUM ('PLANNING', 'CONFIRMED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED');

-- Create trips table
CREATE TABLE trips (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    destination VARCHAR(200) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    description TEXT,
    status trip_status NOT NULL DEFAULT 'PLANNING',
    owner_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_trip_owner FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT chk_trip_dates CHECK (end_date >= start_date)
);

-- Create indexes
CREATE INDEX idx_trip_owner ON trips(owner_id);
CREATE INDEX idx_trip_dates ON trips(start_date, end_date);
CREATE INDEX idx_trip_status ON trips(status);
CREATE INDEX idx_trip_destination ON trips(destination);
CREATE INDEX idx_trip_created_at ON trips(created_at);

-- Add comments
COMMENT ON TABLE trips IS 'User trips and travel plans';
COMMENT ON COLUMN trips.title IS 'Trip title or name';
COMMENT ON COLUMN trips.destination IS 'Primary destination for the trip';
COMMENT ON COLUMN trips.start_date IS 'Trip start date';
COMMENT ON COLUMN trips.end_date IS 'Trip end date';
COMMENT ON COLUMN trips.description IS 'Optional trip description';
COMMENT ON COLUMN trips.status IS 'Current status of the trip';
COMMENT ON COLUMN trips.owner_id IS 'User who owns/created the trip';