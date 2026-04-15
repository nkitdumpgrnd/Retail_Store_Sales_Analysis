CREATE DATABASE IF NOT EXISTS retail_sales_db;
USE retail_sales_db;

CREATE TABLE IF NOT EXISTS sales (
    order_id BIGINT PRIMARY KEY,
    order_date DATE NOT NULL,
    city VARCHAR(100) NOT NULL,
    product VARCHAR(100) NOT NULL,
    category VARCHAR(100) NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(12, 2) NOT NULL
);

INSERT INTO sales (order_id, order_date, city, product, category, quantity, price) VALUES
(1001, '2025-01-10', 'Pune', 'Laptop', 'Electronics', 2, 50000),
(1002, '2025-01-11', 'Mumbai', 'Mobile', 'Electronics', 3, 20000),
(1003, '2025-01-11', 'Pune', 'Headphones', 'Accessories', 5, 2000),
(1004, '2025-01-12', 'Nagpur', 'Keyboard', 'Accessories', 4, 1500),
(1005, '2025-01-13', 'Mumbai', 'Laptop', 'Electronics', 1, 50000)
ON DUPLICATE KEY UPDATE city = VALUES(city);

-- 1. Display all sales records.
SELECT * FROM sales;

-- 2. Calculate total sales revenue.
SELECT SUM(quantity * price) AS total_revenue FROM sales;

-- 3. Identify the top-selling product by quantity.
SELECT product, SUM(quantity) AS total_quantity
FROM sales
GROUP BY product
ORDER BY total_quantity DESC
LIMIT 1;

-- 4. Calculate total revenue generated in each city.
SELECT city, SUM(quantity * price) AS city_revenue
FROM sales
GROUP BY city
ORDER BY city_revenue DESC;

-- 5. Calculate category-wise revenue.
SELECT category, SUM(quantity * price) AS category_revenue
FROM sales
GROUP BY category
ORDER BY category_revenue DESC;

-- 6. Identify the highest-value order.
SELECT order_id, product, city, quantity, price, quantity * price AS order_value
FROM sales
ORDER BY order_value DESC
LIMIT 1;

-- 7. Calculate the average product price.
SELECT AVG(price) AS average_product_price FROM sales;

-- Extra: sales trend across time.
SELECT order_date, SUM(quantity * price) AS daily_revenue
FROM sales
GROUP BY order_date
ORDER BY order_date;
