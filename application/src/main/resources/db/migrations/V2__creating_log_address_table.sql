DROP TABLE IF EXISTS tb_log_address CASCADE;
    CREATE TABLE IF NOT EXISTS tb_log_address (
        id SERIAL PRIMARY KEY,
        data TEXT,
        ip VARCHAR(255) NOT NULL,
        nm_created VARCHAR(255) NOT NULL,
        dt_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
        nm_edited VARCHAR(255),
        dt_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        version BIGINT DEFAULT 0
    );
