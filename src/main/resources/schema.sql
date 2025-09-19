-- 用户表
CREATE TABLE IF NOT EXISTS "user" (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    salt VARCHAR(255)
);

-- 座位表
CREATE TABLE IF NOT EXISTS seat (
    id SERIAL PRIMARY KEY,
    seat_number VARCHAR(255) NOT NULL,
    capacity INT NOT NULL,
    status INT DEFAULT 0,
    qr_code TEXT
);

-- 菜品表
CREATE TABLE IF NOT EXISTS food (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    category VARCHAR(255),
    image_url VARCHAR(255)
);

-- 订单表
CREATE TABLE IF NOT EXISTS "order" (
    id SERIAL PRIMARY KEY,
    seat_id INT,
    total_price DECIMAL(10, 2),
    status INT,
    create_time TIMESTAMP,
    FOREIGN KEY (seat_id) REFERENCES seat(id)
);

-- 订单项表
CREATE TABLE IF NOT EXISTS order_item (
    id SERIAL PRIMARY KEY,
    order_id INT,
    food_id INT,
    quantity INT,
    price DECIMAL(10, 2),
    FOREIGN KEY (order_id) REFERENCES "order"(id),
    FOREIGN KEY (food_id) REFERENCES food(id)
);
