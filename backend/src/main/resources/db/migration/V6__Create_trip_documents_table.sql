-- Create document category enum
CREATE TYPE document_category AS ENUM (
    'BOOKING', 'TICKET', 'PASSPORT', 'VISA', 'INSURANCE', 
    'ITINERARY', 'MAP', 'RECEIPT', 'PHOTO', 'CHECKLIST', 
    'EMERGENCY', 'MEDICAL', 'OTHER'
);

-- Create trip_documents table
CREATE TABLE trip_documents (
    id BIGSERIAL PRIMARY KEY,
    trip_id BIGINT NOT NULL,
    uploaded_by_id BIGINT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    original_file_name VARCHAR(255) NOT NULL,
    file_type VARCHAR(50) NOT NULL,
    file_size BIGINT NOT NULL CHECK (file_size > 0),
    s3_key VARCHAR(500) NOT NULL UNIQUE,
    s3_bucket VARCHAR(100),
    category document_category NOT NULL,
    description TEXT,
    content_type VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_trip_document_trip FOREIGN KEY (trip_id) REFERENCES trips(id) ON DELETE CASCADE,
    CONSTRAINT fk_trip_document_uploaded_by FOREIGN KEY (uploaded_by_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create indexes
CREATE INDEX idx_trip_document_trip ON trip_documents(trip_id);
CREATE INDEX idx_trip_document_category ON trip_documents(category);
CREATE INDEX idx_trip_document_uploaded_by ON trip_documents(uploaded_by_id);
CREATE UNIQUE INDEX idx_trip_document_s3_key ON trip_documents(s3_key);
CREATE INDEX idx_trip_document_file_type ON trip_documents(file_type);
CREATE INDEX idx_trip_document_content_type ON trip_documents(content_type);
CREATE INDEX idx_trip_document_created_at ON trip_documents(created_at);

-- Add comments
COMMENT ON TABLE trip_documents IS 'Documents and files associated with trips';
COMMENT ON COLUMN trip_documents.trip_id IS 'Reference to the parent trip';
COMMENT ON COLUMN trip_documents.uploaded_by_id IS 'User who uploaded the document';
COMMENT ON COLUMN trip_documents.file_name IS 'Stored file name (may be different from original)';
COMMENT ON COLUMN trip_documents.original_file_name IS 'Original file name as uploaded by user';
COMMENT ON COLUMN trip_documents.file_type IS 'File extension or type';
COMMENT ON COLUMN trip_documents.file_size IS 'File size in bytes';
COMMENT ON COLUMN trip_documents.s3_key IS 'Unique S3 object key for file storage';
COMMENT ON COLUMN trip_documents.s3_bucket IS 'S3 bucket name where file is stored';
COMMENT ON COLUMN trip_documents.category IS 'Document category for organization';
COMMENT ON COLUMN trip_documents.description IS 'Optional description of the document';
COMMENT ON COLUMN trip_documents.content_type IS 'MIME type of the file';