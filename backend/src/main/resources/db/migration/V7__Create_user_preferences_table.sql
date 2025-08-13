-- Create preference type enum
CREATE TYPE preference_type AS ENUM ('STRING', 'INTEGER', 'DECIMAL', 'BOOLEAN', 'JSON');

-- Create user_preferences table
CREATE TABLE user_preferences (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    preference_key VARCHAR(100) NOT NULL,
    preference_value TEXT,
    type preference_type NOT NULL DEFAULT 'STRING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_preference_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT uq_user_preference_user_key UNIQUE (user_id, preference_key)
);

-- Create indexes
CREATE UNIQUE INDEX idx_user_preference_user_key ON user_preferences(user_id, preference_key);
CREATE INDEX idx_user_preference_user ON user_preferences(user_id);
CREATE INDEX idx_user_preference_key ON user_preferences(preference_key);
CREATE INDEX idx_user_preference_type ON user_preferences(type);

-- Add comments
COMMENT ON TABLE user_preferences IS 'User preferences and settings';
COMMENT ON COLUMN user_preferences.user_id IS 'Reference to the user';
COMMENT ON COLUMN user_preferences.preference_key IS 'Unique key identifying the preference';
COMMENT ON COLUMN user_preferences.preference_value IS 'Value of the preference (stored as text)';
COMMENT ON COLUMN user_preferences.type IS 'Data type of the preference value';

-- Insert common preference keys with descriptions
INSERT INTO user_preferences (user_id, preference_key, preference_value, type) VALUES 
-- These are example preferences that would be created for users
-- (1, 'travel_style', 'adventure', 'STRING'),
-- (1, 'budget_range', 'medium', 'STRING'),
-- (1, 'preferred_currency', 'USD', 'STRING'),
-- (1, 'notifications_enabled', 'true', 'BOOLEAN'),
-- (1, 'default_trip_duration', '7', 'INTEGER')
(0, '_system_preference_types', '{"travel_style": "STRING", "budget_range": "STRING", "preferred_currency": "STRING", "notifications_enabled": "BOOLEAN", "default_trip_duration": "INTEGER"}', 'JSON')
ON CONFLICT (user_id, preference_key) DO NOTHING;