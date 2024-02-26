-- Insert 5 providers
INSERT INTO providers (name, is_deleted) VALUES
    ('Provider 1', false),
    ('Provider 2', false),
    ('Provider 3', false),
    ('Provider 4', false),
    ('Provider 5', false);

-- Insert 5 services
INSERT INTO services (name, is_deleted) VALUES
    ('Service 1', false),
    ('Service 2', false),
    ('Service 3', false),
    ('Service 4', false),
    ('Service 5', false);

-- Establish connections in join table
INSERT INTO provider_service (provider_id, service_id) VALUES
    -- Provider 1 connected to 0 services
    (2, 1), -- Provider 2 connected to Service 1
    (3, 1), -- Provider 3 connected to Service 1
    (3, 2), -- Provider 3 connected to Service 2
    (4, 1), -- Provider 4 connected to Service 1
    (4, 2), -- Provider 4 connected to Service 2
    (4, 3), -- Provider 4 connected to Service 3
    (5, 1); -- Provider 5 connected to Service 1
    (5, 2); -- Provider 5 connected to Service 2
    (5, 3); -- Provider 5 connected to Service 3
    (5, 4); -- Provider 5 connected to Service 4
