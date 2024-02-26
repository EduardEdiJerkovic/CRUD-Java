CREATE TABLE IF NOT EXISTS service_provider (
    service_id BIGINT,
    provider_id BIGINT,
    PRIMARY KEY (service_id, provider_id),
    FOREIGN KEY (service_id) REFERENCES services (id),
    FOREIGN KEY (provider_id) REFERENCES providers (id)
);
