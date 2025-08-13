-- Create day_itineraries table
CREATE TABLE day_itineraries (
    id BIGSERIAL PRIMARY KEY,
    trip_id BIGINT NOT NULL,
    date DATE NOT NULL,
    day_number INTEGER NOT NULL,
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_day_itinerary_trip FOREIGN KEY (trip_id) REFERENCES trips(id) ON DELETE CASCADE,
    CONSTRAINT uq_day_itinerary_trip_date UNIQUE (trip_id, date),
    CONSTRAINT chk_day_number_positive CHECK (day_number > 0)
);

-- Create indexes
CREATE UNIQUE INDEX idx_day_itinerary_trip_date ON day_itineraries(trip_id, date);
CREATE INDEX idx_day_itinerary_day_number ON day_itineraries(trip_id, day_number);
CREATE INDEX idx_day_itinerary_date ON day_itineraries(date);

-- Add comments
COMMENT ON TABLE day_itineraries IS 'Daily itineraries for trips';
COMMENT ON COLUMN day_itineraries.trip_id IS 'Reference to the parent trip';
COMMENT ON COLUMN day_itineraries.date IS 'Date for this day of the itinerary';
COMMENT ON COLUMN day_itineraries.day_number IS 'Sequential day number within the trip (1, 2, 3, etc.)';
COMMENT ON COLUMN day_itineraries.notes IS 'Optional notes for the day';