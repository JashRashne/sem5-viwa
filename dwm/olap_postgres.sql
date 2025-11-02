CREATE TABLE dim_product (
    product_key SERIAL PRIMARY KEY,
    product_id VARCHAR(20),
    product_name VARCHAR(100),
    category VARCHAR(50),
    brand VARCHAR(50)
);


CREATE TABLE dim_customer (
    customer_key SERIAL PRIMARY KEY,
    customer_id VARCHAR(20),
    customer_name VARCHAR(100),
    region VARCHAR(50),
    country VARCHAR(50)
);


CREATE TABLE dim_date (
    date_key SERIAL PRIMARY KEY,
    full_date DATE,
    year INT,
    quarter INT,
    month INT,
    day INT
);


CREATE TABLE dim_store (
    store_key SERIAL PRIMARY KEY,
    store_id VARCHAR(20),
    store_name VARCHAR(100),
    city VARCHAR(50),
    state VARCHAR(50)
);


CREATE TABLE fact_sales (
    sales_key SERIAL PRIMARY KEY,
    product_key INT REFERENCES dim_product(product_key),
    customer_key INT REFERENCES dim_customer(customer_key),
    date_key INT REFERENCES dim_date(date_key),
    store_key INT REFERENCES dim_store(store_key),
    quantity_sold INT,
    sales_amount NUMERIC(10,2)
);



-- =====================================================
-- PART 1: INSERT SAMPLE DATA
-- =====================================================

-- Insert Date Dimension (2023-2024 data)
INSERT INTO dim_date (full_date, year, quarter, month, day)
SELECT 
    date::date,
    EXTRACT(YEAR FROM date)::INT,
    EXTRACT(QUARTER FROM date)::INT,
    EXTRACT(MONTH FROM date)::INT,
    EXTRACT(DAY FROM date)::INT
FROM generate_series('2023-01-01'::date, '2024-12-31'::date, '1 day'::interval) date;

-- Insert Product Dimension
INSERT INTO dim_product (product_id, product_name, category, brand) VALUES
('P001', 'iPhone 14 Pro', 'Electronics', 'Apple'),
('P002', 'Samsung Galaxy S23', 'Electronics', 'Samsung'),
('P003', 'MacBook Air M2', 'Electronics', 'Apple'),
('P004', 'Dell XPS 15', 'Electronics', 'Dell'),
('P005', 'Sony WH-1000XM5', 'Electronics', 'Sony'),
('P006', 'Nike Air Max', 'Footwear', 'Nike'),
('P007', 'Adidas Ultraboost', 'Footwear', 'Adidas'),
('P008', 'Levi''s 501 Jeans', 'Clothing', 'Levi''s'),
('P009', 'North Face Jacket', 'Clothing', 'North Face'),
('P010', 'Ray-Ban Aviators', 'Accessories', 'Ray-Ban'),
('P011', 'Fossil Watch', 'Accessories', 'Fossil'),
('P012', 'Kindle Paperwhite', 'Electronics', 'Amazon'),
('P013', 'iPad Pro', 'Electronics', 'Apple'),
('P014', 'Sony PlayStation 5', 'Electronics', 'Sony'),
('P015', 'Xbox Series X', 'Electronics', 'Microsoft');

-- Insert Customer Dimension
INSERT INTO dim_customer (customer_id, customer_name, region, country) VALUES
('C001', 'John Smith', 'North', 'USA'),
('C002', 'Emma Johnson', 'South', 'USA'),
('C003', 'Michael Brown', 'East', 'USA'),
('C004', 'Sarah Davis', 'West', 'USA'),
('C005', 'James Wilson', 'North', 'USA'),
('C006', 'Lisa Anderson', 'South', 'USA'),
('C007', 'Robert Taylor', 'East', 'USA'),
('C008', 'Mary Thomas', 'West', 'USA'),
('C009', 'David Martinez', 'North', 'Canada'),
('C010', 'Jennifer Garcia', 'South', 'Canada'),
('C011', 'William Lee', 'East', 'Canada'),
('C012', 'Linda Rodriguez', 'West', 'Canada'),
('C013', 'Richard Clark', 'North', 'USA'),
('C014', 'Patricia Lewis', 'South', 'USA'),
('C015', 'Charles Walker', 'East', 'USA');

-- Insert Store Dimension
INSERT INTO dim_store (store_id, store_name, city, state) VALUES
('S001', 'Mega Store Manhattan', 'New York', 'NY'),
('S002', 'Mega Store Los Angeles', 'Los Angeles', 'CA'),
('S003', 'Mega Store Chicago', 'Chicago', 'IL'),
('S004', 'Mega Store Houston', 'Houston', 'TX'),
('S005', 'Mega Store Phoenix', 'Phoenix', 'AZ'),
('S006', 'Mega Store Toronto', 'Toronto', 'ON'),
('S007', 'Mega Store Miami', 'Miami', 'FL'),
('S008', 'Mega Store Seattle', 'Seattle', 'WA'),
('S009', 'Mega Store Boston', 'Boston', 'MA'),
('S010', 'Mega Store Denver', 'Denver', 'CO');

-- Insert Fact Sales (Sample transactions)
INSERT INTO fact_sales (product_key, customer_key, date_key, store_key, quantity_sold, sales_amount)
SELECT 
    (RANDOM() * 14 + 1)::INT as product_key,
    (RANDOM() * 14 + 1)::INT as customer_key,
    (RANDOM() * 729 + 1)::INT as date_key,
    (RANDOM() * 9 + 1)::INT as store_key,
    (RANDOM() * 5 + 1)::INT as quantity_sold,
    (RANDOM() * 2000 + 100)::NUMERIC(10,2) as sales_amount
FROM generate_series(1, 1000);

-- Additional targeted sales data for better analysis
INSERT INTO fact_sales (product_key, customer_key, date_key, store_key, quantity_sold, sales_amount) VALUES
(1, 1, 365, 1, 2, 2199.98),
(2, 2, 366, 2, 1, 999.99),
(3, 3, 367, 3, 1, 1199.99),
(6, 4, 368, 4, 3, 389.97),
(7, 5, 369, 5, 2, 359.98),
(13, 6, 370, 6, 1, 1099.99),
(14, 7, 371, 7, 1, 499.99),
(1, 8, 400, 1, 1, 1099.99),
(3, 9, 401, 2, 2, 2399.98),
(5, 10, 402, 3, 1, 399.99);

-- =====================================================
-- PART 2: OLAP OPERATIONS
-- =====================================================

-- 1. ROLL-UP: Aggregate from day to month to quarter to year
-- Sales by Year
SELECT 
    d.year,
    COUNT(*) as transaction_count,
    SUM(f.quantity_sold) as total_quantity,
    SUM(f.sales_amount) as total_sales,
    AVG(f.sales_amount) as avg_sale_amount
FROM fact_sales f
JOIN dim_date d ON f.date_key = d.date_key
GROUP BY d.year
ORDER BY d.year;

-- Sales by Quarter
SELECT 
    d.year,
    d.quarter,
    SUM(f.sales_amount) as total_sales,
    COUNT(*) as transaction_count
FROM fact_sales f
JOIN dim_date d ON f.date_key = d.date_key
GROUP BY d.year, d.quarter
ORDER BY d.year, d.quarter;

-- Sales by Month
SELECT 
    d.year,
    d.month,
    SUM(f.sales_amount) as total_sales,
    COUNT(*) as transaction_count
FROM fact_sales f
JOIN dim_date d ON f.date_key = d.date_key
GROUP BY d.year, d.month
ORDER BY d.year, d.month;

-- 2. DRILL-DOWN: From high-level to detailed view
-- From Country to Region to Customer
SELECT 
    c.country,
    c.region,
    c.customer_name,
    SUM(f.sales_amount) as total_sales,
    COUNT(*) as purchase_count
FROM fact_sales f
JOIN dim_customer c ON f.customer_key = c.customer_key
GROUP BY c.country, c.region, c.customer_name
ORDER BY c.country, c.region, total_sales DESC;

-- From Category to Brand to Product
SELECT 
    p.category,
    p.brand,
    p.product_name,
    SUM(f.quantity_sold) as units_sold,
    SUM(f.sales_amount) as total_sales
FROM fact_sales f
JOIN dim_product p ON f.product_key = p.product_key
GROUP BY p.category, p.brand, p.product_name
ORDER BY p.category, p.brand, total_sales DESC;

-- 3. SLICE: Fix one dimension
-- Sales for Electronics category only
SELECT 
    d.year,
    d.quarter,
    s.store_name,
    SUM(f.sales_amount) as total_sales
FROM fact_sales f
JOIN dim_product p ON f.product_key = p.product_key
JOIN dim_date d ON f.date_key = d.date_key
JOIN dim_store s ON f.store_key = s.store_key
WHERE p.category = 'Electronics'
GROUP BY d.year, d.quarter, s.store_name
ORDER BY d.year, d.quarter, total_sales DESC;

-- 4. DICE: Filter multiple dimensions
-- Sales for Electronics in USA during 2024
SELECT 
    p.product_name,
    c.customer_name,
    s.store_name,
    d.full_date,
    f.sales_amount
FROM fact_sales f
JOIN dim_product p ON f.product_key = p.product_key
JOIN dim_customer c ON f.customer_key = c.customer_key
JOIN dim_date d ON f.date_key = d.date_key
JOIN dim_store s ON f.store_key = s.store_key
WHERE p.category = 'Electronics'
    AND c.country = 'USA'
    AND d.year = 2024
ORDER BY f.sales_amount DESC;

-- 5. PIVOT: Rotate data for different view
-- Sales by Category and Quarter (Pivot-like view)
SELECT 
    p.category,
    SUM(CASE WHEN d.quarter = 1 THEN f.sales_amount ELSE 0 END) as Q1_sales,
    SUM(CASE WHEN d.quarter = 2 THEN f.sales_amount ELSE 0 END) as Q2_sales,
    SUM(CASE WHEN d.quarter = 3 THEN f.sales_amount ELSE 0 END) as Q3_sales,
    SUM(CASE WHEN d.quarter = 4 THEN f.sales_amount ELSE 0 END) as Q4_sales,
    SUM(f.sales_amount) as total_sales
FROM fact_sales f
JOIN dim_product p ON f.product_key = p.product_key
JOIN dim_date d ON f.date_key = d.date_key
WHERE d.year = 2024
GROUP BY p.category
ORDER BY total_sales DESC;



-- 8. CUBE: All possible aggregations
SELECT 
    d.year,
    p.category,
    c.region,
    SUM(f.sales_amount) as total_sales
FROM fact_sales f
JOIN dim_date d ON f.date_key = d.date_key
JOIN dim_product p ON f.product_key = p.product_key
JOIN dim_customer c ON f.customer_key = c.customer_key
GROUP BY CUBE(d.year, p.category, c.region)
ORDER BY d.year NULLS FIRST, p.category NULLS FIRST, c.region NULLS FIRST;

-- 9. ROLLUP: Hierarchical aggregations
SELECT 
    d.year,
    d.quarter,
    d.month,
    SUM(f.sales_amount) as total_sales,
    COUNT(*) as transaction_count
FROM fact_sales f
JOIN dim_date d ON f.date_key = d.date_key
GROUP BY ROLLUP(d.year, d.quarter, d.month)
ORDER BY d.year NULLS FIRST, d.quarter NULLS FIRST, d.month NULLS FIRST;
