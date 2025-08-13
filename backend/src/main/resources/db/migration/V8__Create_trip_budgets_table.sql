-- Create trip_budgets table
CREATE TABLE trip_budgets (
    id BIGSERIAL PRIMARY KEY,
    trip_id BIGINT NOT NULL UNIQUE,
    total_budget DECIMAL(12,2) CHECK (total_budget >= 0),
    accommodation_budget DECIMAL(12,2) CHECK (accommodation_budget >= 0),
    transportation_budget DECIMAL(12,2) CHECK (transportation_budget >= 0),
    food_budget DECIMAL(12,2) CHECK (food_budget >= 0),
    activities_budget DECIMAL(12,2) CHECK (activities_budget >= 0),
    shopping_budget DECIMAL(12,2) CHECK (shopping_budget >= 0),
    miscellaneous_budget DECIMAL(12,2) CHECK (miscellaneous_budget >= 0),
    actual_spent DECIMAL(12,2) DEFAULT 0 CHECK (actual_spent >= 0),
    currency VARCHAR(3) DEFAULT 'USD',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_trip_budget_trip FOREIGN KEY (trip_id) REFERENCES trips(id) ON DELETE CASCADE
);

-- Create indexes
CREATE UNIQUE INDEX idx_trip_budget_trip ON trip_budgets(trip_id);
CREATE INDEX idx_trip_budget_currency ON trip_budgets(currency);
CREATE INDEX idx_trip_budget_total ON trip_budgets(total_budget);
CREATE INDEX idx_trip_budget_actual_spent ON trip_budgets(actual_spent);

-- Add comments
COMMENT ON TABLE trip_budgets IS 'Budget planning and tracking for trips';
COMMENT ON COLUMN trip_budgets.trip_id IS 'Reference to the parent trip (one-to-one relationship)';
COMMENT ON COLUMN trip_budgets.total_budget IS 'Total planned budget for the trip';
COMMENT ON COLUMN trip_budgets.accommodation_budget IS 'Budget allocated for accommodation';
COMMENT ON COLUMN trip_budgets.transportation_budget IS 'Budget allocated for transportation';
COMMENT ON COLUMN trip_budgets.food_budget IS 'Budget allocated for food and dining';
COMMENT ON COLUMN trip_budgets.activities_budget IS 'Budget allocated for activities and entertainment';
COMMENT ON COLUMN trip_budgets.shopping_budget IS 'Budget allocated for shopping';
COMMENT ON COLUMN trip_budgets.miscellaneous_budget IS 'Budget allocated for miscellaneous expenses';
COMMENT ON COLUMN trip_budgets.actual_spent IS 'Total amount actually spent on the trip';
COMMENT ON COLUMN trip_budgets.currency IS 'Currency code (ISO 4217) for all budget amounts';