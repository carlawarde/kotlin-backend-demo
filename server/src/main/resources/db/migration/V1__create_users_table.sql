CREATE EXTENSION IF NOT EXISTS citext;

CREATE TABLE users (
   id UUID PRIMARY KEY,
   username CITEXT NOT NULL CONSTRAINT uidx_users_username UNIQUE,
   email CITEXT NOT NULL CONSTRAINT uidx_users_email UNIQUE,
   password_hash VARCHAR(128) NOT NULL,
   created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
   updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_users_created_at ON users(created_at);
