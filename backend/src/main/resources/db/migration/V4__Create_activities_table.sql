-- Create activity type enum
CREATE TYPE activity_type AS ENUM (
    'ACCOMMODATION', 'TRANSPORTATION', 'SIGHTSEEING', 'DINING', 
    'ENTERTAINMENT', 'SHOPPING', 'OUTDOOR', 'CULTURAL', 
    'BUSINESS', 'RELAXATION', 'ADVENTURE', 'EDUCATION', 'OTHER'
);

-- Create activities table
CREATE TABLE activities (
    id BIGSERIAL PRIMARY KEY,
    day_itinerary_id BIGINT NOT NULL,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    location VARCHAR(300) NOT NULL,
    place_id VARCHAR(100),
    start_time TIME,
    end_time TIME,
    type activity_type NOT NULL,
    estimated_cost DECIMAL(10,2) CHECK (estimated_cost >= 0),
    actual_cost DECIMAL(10,2) CHECK (actual_cost >= 0),
    notes TEXT,
    booking_reference VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_activity_day_itinerary FOREIGN KEY (day_itinerary_id) REFERENCES day_itineraries(id) ON DELETE CASCADE,
    CONSTRAINT chk_activity_times CHECK (end_time IS NULL OR start_time IS NULL OR end_time > start_time)
);

-- Create indexes
CREATE INDEX idx_activity_day_itinerary ON activities(day_itinerary_id);
CREATE INDEX idx_activity_type ON activities(type);
CREATE INDEX idx_activity_start_time ON activities(day_itinerary_id, start_time);
CREATE INDEX idx_activity_location ON activities(location);
CREATE INDEX idx_activity_place_id ON activities(place_id);

-- Add comments
COMMENT ON TABLE activities IS 'Individual activities within daily itineraries';
COMMENT ON COLUMN activities.day_itinerary_id IS 'Reference to the parent day itinerary';
COMMENT ON COLUMN activities.name IS 'Activity name or title';
COMMENT ON COLUMN activities.description IS 'Detailed description of the activity';
COMMENT ON COLUMN activities.location IS 'Activity location or address';
COMMENT ON COLUMN activities.place_id IS 'Google Places API place identifier';
COMMENT ON COLUMN activities.start_time IS 'Scheduled start time';
COMMENT ON COLUMN activities.end_time IS 'Scheduled end time';
COMMENT ON COLUMN activities.type IS 'Category/type of activity';
COMMENT ON COLUMN activities.estimated_cost IS 'Estimated cost for the activity';
COMMENT ON COLUMN activities.actual_cost IS 'Actual cost spent on the activity';
COMMENT ON COLUMN activities.booking_reference IS 'Booking confirmation number or reference';