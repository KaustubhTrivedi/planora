-- Create collaborator role enum
CREATE TYPE collaborator_role AS ENUM ('VIEWER', 'EDITOR', 'ADMIN');

-- Create invitation status enum
CREATE TYPE invitation_status AS ENUM ('PENDING', 'ACCEPTED', 'DECLINED', 'EXPIRED');

-- Create trip_collaborators table
CREATE TABLE trip_collaborators (
    id BIGSERIAL PRIMARY KEY,
    trip_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    role collaborator_role NOT NULL DEFAULT 'VIEWER',
    status invitation_status NOT NULL DEFAULT 'PENDING',
    invited_by_id BIGINT,
    invited_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    joined_at TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_trip_collaborator_trip FOREIGN KEY (trip_id) REFERENCES trips(id) ON DELETE CASCADE,
    CONSTRAINT fk_trip_collaborator_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_trip_collaborator_invited_by FOREIGN KEY (invited_by_id) REFERENCES users(id) ON DELETE SET NULL,
    CONSTRAINT uq_trip_collaborator_trip_user UNIQUE (trip_id, user_id)
);

-- Create indexes
CREATE UNIQUE INDEX idx_trip_collaborator_trip_user ON trip_collaborators(trip_id, user_id);
CREATE INDEX idx_trip_collaborator_trip ON trip_collaborators(trip_id);
CREATE INDEX idx_trip_collaborator_user ON trip_collaborators(user_id);
CREATE INDEX idx_trip_collaborator_role ON trip_collaborators(role);
CREATE INDEX idx_trip_collaborator_status ON trip_collaborators(status);
CREATE INDEX idx_trip_collaborator_invited_by ON trip_collaborators(invited_by_id);

-- Add comments
COMMENT ON TABLE trip_collaborators IS 'Trip collaboration and sharing permissions';
COMMENT ON COLUMN trip_collaborators.trip_id IS 'Reference to the shared trip';
COMMENT ON COLUMN trip_collaborators.user_id IS 'User being granted access to the trip';
COMMENT ON COLUMN trip_collaborators.role IS 'Permission level for the collaborator';
COMMENT ON COLUMN trip_collaborators.status IS 'Current status of the collaboration invitation';
COMMENT ON COLUMN trip_collaborators.invited_by_id IS 'User who sent the collaboration invitation';
COMMENT ON COLUMN trip_collaborators.invited_at IS 'When the invitation was sent';
COMMENT ON COLUMN trip_collaborators.joined_at IS 'When the user accepted the invitation';