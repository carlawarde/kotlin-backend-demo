CREATE TABLE users (
   id UUID PRIMARY KEY,
   username VARCHAR(30) NOT NULL UNIQUE,
   email VARCHAR(255) NOT NULL UNIQUE,
   password_hash VARCHAR(128) NOT NULL,
   created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
   updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_users_created_at ON users(created_at);
CREATE UNIQUE INDEX users_email_unique ON users (LOWER(email));
