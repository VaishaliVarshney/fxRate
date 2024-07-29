CREATE TABLE fx_rate (
    id SERIAL PRIMARY KEY,
    source_currency VARCHAR(3) NOT NULL,
    target_currency VARCHAR(3) NOT NULL,
    rate DOUBLE PRECISION NOT NULL,
    date VARCHAR NOT NULL
);
