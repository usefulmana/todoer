-- Insert sample users
INSERT INTO users (email, username, password_hash)
VALUES ('john.doe@example.com', 'johndoe',
        '$2a$10$kPw4vN6pVe7PmWR.gQ3GR.YU4L5zWtemR7QgB9OE/hF7hXkwJVm2q'), -- password: password123
       ('jane.smith@example.com', 'janesmith', '$2a$10$kPw4vN6pVe7PmWR.gQ3GR.YU4L5zWtemR7QgB9OE/hF7hXkwJVm2q'),
       ('bob.wilson@example.com', 'bobwilson', '$2a$10$kPw4vN6pVe7PmWR.gQ3GR.YU4L5zWtemR7QgB9OE/hF7hXkwJVm2q');

-- Insert sample projects
INSERT INTO projects (name, description, owner_id)
VALUES ('Personal Website', 'Redesign of personal portfolio website', 1),
       ('Home Renovation', 'Kitchen and bathroom renovation project', 2),
       ('Vacation Planning', 'Summer 2024 family vacation planning', 1),
       ('Work Dashboard', 'Team productivity dashboard development', 3);

-- Insert sample tags
INSERT INTO tags (name, color, project_id)
VALUES ('High Priority', '#FF0000', 1),
       ('Design', '#800080', 1),
       ('Backend', '#0000FF', 1),
       ('Shopping', '#FFA500', 2),
       ('Contractor', '#008000', 2),
       ('Budget', '#FFD700', 2),
       ('Flights', '#87CEEB', 3),
       ('Hotels', '#4B0082', 3),
       ('Research', '#A0522D', 3),
       ('Frontend', '#FF1493', 4),
       ('API', '#20B2AA', 4),
       ('Testing', '#8B4513', 4);

-- Insert sample tasks
INSERT INTO tasks (title, description, status, priority, due_date, project_id, assigned_to, created_by)
VALUES ('Design Homepage Mockup', 'Create initial mockup for homepage layout', 'IN_PROGRESS', 2,
        CURRENT_TIMESTAMP + INTERVAL '5 days', 1, 1, 1),
       ('Implement User Authentication', 'Set up JWT authentication system', 'TODO', 3,
        CURRENT_TIMESTAMP + INTERVAL '7 days', 1, 1, 1),
       ('Get Contractor Quotes', 'Contact at least 3 contractors for kitchen renovation', 'DONE', 1,
        CURRENT_TIMESTAMP - INTERVAL '2 days', 2, 2, 2),
       ('Create Budget Spreadsheet', 'Detailed cost breakdown for materials and labor', 'TODO', 2,
        CURRENT_TIMESTAMP + INTERVAL '3 days', 2, 2, 2),
       ('Research Flight Options', 'Compare prices for different airlines and dates', 'IN_PROGRESS', 1,
        CURRENT_TIMESTAMP + INTERVAL '10 days', 3, 1, 1),
       ('Book Hotel Accommodations', 'Find and reserve family-friendly hotel', 'TODO', 2,
        CURRENT_TIMESTAMP + INTERVAL '14 days', 3, 1, 1),
       ('Design API Architecture', 'Create REST API endpoints documentation', 'IN_PROGRESS', 3,
        CURRENT_TIMESTAMP + INTERVAL '4 days', 4, 3, 3),
       ('Implement Data Visualization', 'Add charts for team performance metrics', 'TODO', 2,
        CURRENT_TIMESTAMP + INTERVAL '8 days', 4, 3, 3);

-- Insert sample task-tag relationships
INSERT INTO task_tags (task_id, tag_id)
VALUES (1, 1),  -- Homepage Mockup - High Priority
       (1, 2),  -- Homepage Mockup - Design
       (2, 3),  -- User Authentication - Backend
       (3, 4),  -- Contractor Quotes - Shopping
       (3, 5),  -- Contractor Quotes - Contractor
       (4, 6),  -- Budget Spreadsheet - Budget
       (5, 7),  -- Flight Options - Flights
       (5, 9),  -- Flight Options - Research
       (6, 8),  -- Hotel Accommodations - Hotels
       (7, 10), -- API Architecture - Frontend
       (7, 11), -- API Architecture - API
       (8, 10), -- Data Visualization - Frontend
       (8, 12); -- Data Visualization - Testing