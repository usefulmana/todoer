CREATE TABLE device_tokens
(
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT       NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    token       VARCHAR(255) NOT NULL,
    device_type VARCHAR(20)  NOT NULL CHECK (device_type IN ('WEB', 'IOS', 'ANDROID')),
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Add unique constraint on token to prevent duplicates
CREATE UNIQUE INDEX idx_device_tokens_token ON device_tokens (token);

-- Add index on user_id for faster lookups
CREATE INDEX idx_device_tokens_user_id ON device_tokens (user_id);

-- Create updated_at trigger for device_tokens
CREATE TRIGGER update_device_tokens_updated_at
    BEFORE UPDATE
    ON device_tokens
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Add some comments for documentation
COMMENT
ON TABLE device_tokens IS 'Stores Firebase Cloud Messaging tokens for user devices';
COMMENT
ON COLUMN device_tokens.token IS 'Firebase Cloud Messaging token for the device';
COMMENT
ON COLUMN device_tokens.device_type IS 'Type of device (WEB, IOS, ANDROID)';