CREATE TABLE user (
                      id UUID PRIMARY KEY,
                      name VARCHAR(255),
                      email VARCHAR(255) UNIQUE,
                      password VARCHAR(255),
                      created TIMESTAMP,
                      modified TIMESTAMP,
                      last_login TIMESTAMP,
                      token VARCHAR(255),
                      is_active BOOLEAN
);

CREATE TABLE phone (
                       id BIGINT PRIMARY KEY,
                       number VARCHAR(20),
                       city_code VARCHAR(10),
                       country_code VARCHAR(10),
                       user_id UUID,
                       FOREIGN KEY (user_id) REFERENCES user(id)
);