CREATE TABLE IF NOT EXISTS agent (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    instance_uid VARBINARY(255) NOT NULL,
    description TEXT,
    capabilities BIGINT,
    created_at DATETIME NOT NULL,
    disconnected_at DATETIME
);

CREATE TABLE IF NOT EXISTS agent_config (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    agent_id BIGINT,
    target TEXT,
    body VARBINARY(255),
    content_type TEXT
);

CREATE TABLE IF NOT EXISTS agent_health (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    is_root TINYINT(1),
    parent TEXT,
    target_name TEXT,
    agent_id BIGINT,
    health TINYINT(1),
    start_time BIGINT,
    last_error TEXT,
    status TEXT,
    status_time BIGINT,
    create_at DATETIME
);

CREATE TABLE IF NOT EXISTS agent_package_status (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name TEXT,
    agent_has_version TEXT,
    agent_has_hash TEXT,
    server_offered_version TEXT,
    server_offered_hash TEXT,
    status INT,
    error_message TEXT
);

CREATE TABLE IF NOT EXISTS agent_package_statuses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    agent_id BIGINT,
    total_hash TEXT,
    error_message TEXT
);

CREATE TABLE IF NOT EXISTS agent_remote_config_status (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    hash TEXT,
    status INT,
    error_message TEXT
);