-- Create Users table
CREATE TABLE users
(
    id            BIGSERIAL PRIMARY KEY,
    email         VARCHAR(255) NOT NULL UNIQUE,
    username      VARCHAR(50)  NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at    TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create Projects table
CREATE TABLE projects
(
    id           BIGSERIAL PRIMARY KEY,
    name         VARCHAR(100) NOT NULL,
    description  TEXT,
    owner_id     BIGINT       NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    is_deletable BOOLEAN DEFAULT true,
    created_at   TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create Tags table
CREATE TABLE tags
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(50) NOT NULL,
    color      VARCHAR(7)               DEFAULT '#808080',
    project_id BIGINT      NOT NULL REFERENCES projects (id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (name, project_id)
);

-- Create Tasks table
CREATE TABLE tasks
(
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(200) NOT NULL,
    description TEXT,
    status      VARCHAR(20)  NOT NULL    DEFAULT 'TODO' CHECK (status IN ('TODO', 'IN_PROGRESS', 'DONE')),
    priority    INTEGER      NOT NULL    DEFAULT 0,
    due_date    TIMESTAMP WITH TIME ZONE,
    project_id  BIGINT       NOT NULL REFERENCES projects (id) ON DELETE CASCADE,
    assigned_to BIGINT       REFERENCES users (id) ON DELETE SET NULL,
    created_by  BIGINT       NOT NULL REFERENCES users (id),
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create Task-Tag junction table
CREATE TABLE task_tags
(
    task_id BIGINT REFERENCES tasks (id) ON DELETE CASCADE,
    tag_id  BIGINT REFERENCES tags (id) ON DELETE CASCADE,
    PRIMARY KEY (task_id, tag_id)
);

-- Create indexes for better performance
CREATE INDEX idx_tasks_project_id ON tasks (project_id);
CREATE INDEX idx_tasks_assigned_to ON tasks (assigned_to);
CREATE INDEX idx_tasks_created_by ON tasks (created_by);
CREATE INDEX idx_tags_project_id ON tags (project_id);

-- Create updated_at trigger function
CREATE
OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at
= CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$
language 'plpgsql';

-- Create triggers for updated_at columns
CREATE TRIGGER update_users_updated_at
    BEFORE UPDATE
    ON users
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_projects_updated_at
    BEFORE UPDATE
    ON projects
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_tasks_updated_at
    BEFORE UPDATE
    ON tasks
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();