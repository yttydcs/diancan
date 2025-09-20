-- 店铺表
CREATE TABLE IF NOT EXISTS store (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address TEXT
);

-- 角色表
CREATE TABLE IF NOT EXISTS role (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- 权限表
CREATE TABLE IF NOT EXISTS permission (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(255)
);

-- 用户表 (添加角色ID)
CREATE TABLE IF NOT EXISTS "user" (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    salt VARCHAR(255),
    role_id INT,
    FOREIGN KEY (role_id) REFERENCES role(id)
);

-- 用户-店铺关联表 (一个用户可以管理多个店铺)
CREATE TABLE IF NOT EXISTS user_store (
    user_id INT,
    store_id INT,
    PRIMARY KEY (user_id, store_id),
    FOREIGN KEY (user_id) REFERENCES "user"(id),
    FOREIGN KEY (store_id) REFERENCES store(id)
);

-- 角色-权限关联表
CREATE TABLE IF NOT EXISTS role_permission (
    role_id INT,
    permission_id INT,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES role(id),
    FOREIGN KEY (permission_id) REFERENCES permission(id)
);

-- 座位表 (添加店铺ID)
CREATE TABLE IF NOT EXISTS seat (
    id SERIAL PRIMARY KEY,
    seat_number VARCHAR(255) NOT NULL,
    capacity INT NOT NULL,
    status INT DEFAULT 0,
    qr_code TEXT,
    store_id INT,
    FOREIGN KEY (store_id) REFERENCES store(id)
);

-- 菜品表 (添加店铺ID)
CREATE TABLE IF NOT EXISTS food (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    category VARCHAR(255),
    image_url VARCHAR(255),
    store_id INT,
    FOREIGN KEY (store_id) REFERENCES store(id)
);

-- 订单表
CREATE TABLE IF NOT EXISTS "order" (
    id SERIAL PRIMARY KEY,
    seat_id INT,
    store_id INT,
    total_price DECIMAL(10, 2),
    status INT,
    create_time TIMESTAMP,
    FOREIGN KEY (seat_id) REFERENCES seat(id),
    FOREIGN KEY (store_id) REFERENCES store(id)
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
