CREATE TABLE notification_failures
(
    id             BIGSERIAL PRIMARY KEY,
    topic          VARCHAR(255)             NOT NULL,
    task_id        BIGINT                   NOT NULL,
    payload        TEXT                     NOT NULL,
    failure_reason TEXT,
    retry_count    INTEGER                  NOT NULL DEFAULT 0,
    status         VARCHAR(20)              NOT NULL CHECK (status IN ('FAILED', 'SENT')),
    created_at     TIMESTAMP WITH TIME ZONE NOT NULL,
    last_attempt   TIMESTAMP WITH TIME ZONE,

    CONSTRAINT fk_notification_failures_task
        FOREIGN KEY (task_id)
            REFERENCES tasks (id)
            ON DELETE CASCADE
);

-- Add indexes for common queries
CREATE INDEX idx_notification_failures_status_retry ON notification_failures (status, retry_count);
CREATE INDEX idx_notification_failures_task_id ON notification_failures (task_id);
CREATE INDEX idx_notification_failures_created_at ON notification_failures (created_at);

-- Add comments
COMMENT
ON TABLE notification_failures IS 'Stores failed Firebase notification attempts for retry';
COMMENT
ON COLUMN notification_failures.payload IS 'JSON payload of the notification message';
COMMENT
ON COLUMN notification_failures.retry_count IS 'Number of retry attempts made';
COMMENT
ON COLUMN notification_failures.status IS 'Current status of the notification (FAILED or SENT)';