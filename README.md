# Retail Store Sales Analysis

Full-stack Java FSD project for analyzing retail electronics sales across Pune, Mumbai, and Nagpur. The app answers the assignment questions through SQL, Excel, Python, and a Spring Boot + React dashboard.

## Features

- JWT login with seeded admin user
- REST APIs for sales records and analytics
- MySQL database with JPA/Hibernate ORM
- Dashboard summary cards for revenue, orders, top product, best city, and highest order
- City filter for sales records
- Bar chart: product vs total sales
- Pie chart: category sales distribution
- Line chart: sales by date
- Swagger/OpenAPI documentation
- SQL, Excel, and Python analysis files included

## Project Structure

```text
retailfsd/
  backend/                 Spring Boot REST API
  frontend/                React + Vite dashboard
  data/sales_data.csv      Dataset
  sql/retail_sales_queries.sql
  python/sales_analysis.py
  docs/excel-analysis.md
```

## Backend Setup

Prerequisites:

- Java 11
- Maven
- MySQL

Create or allow Spring Boot to create the database:

```sql
CREATE DATABASE retail_sales_db;
```

Update MySQL username/password in:

```text
backend/src/main/resources/application.properties
```

Run the backend:

```bash
cd backend
mvn spring-boot:run
```

The backend starts on:

```text
http://localhost:8080
```

Swagger is available at:

```text
http://localhost:8080/swagger-ui.html
```

Seeded login:

```text
username: admin
password: admin123
```

## Frontend Setup

Prerequisites:

- Node.js
- npm

Run the frontend:

```bash
cd frontend
npm install
npm run dev
```

Open:

```text
http://localhost:5173
```

## API Endpoints

```text
POST /api/auth/login
GET  /api/sales
GET  /api/sales?city=Pune
GET  /api/sales/{orderId}
POST /api/sales
PUT  /api/sales/{orderId}
DELETE /api/sales/{orderId}
GET  /api/analytics/summary
GET  /api/analytics/products/revenue
GET  /api/analytics/products/quantity
GET  /api/analytics/cities/revenue
GET  /api/analytics/categories/revenue
GET  /api/analytics/dates/revenue
```

## Assignment Mapping

SQL tasks are answered in:

```text
sql/retail_sales_queries.sql
```

Excel steps are documented in:

```text
docs/excel-analysis.md
```

Python Pandas analysis is in:

```text
python/sales_analysis.py
```

Dataset:

```text
data/sales_data.csv
```

## Notes

The `Sale` entity keeps product, city, and category as fields to match the CSV and assignment format directly. JPA still handles table mapping, validation, CRUD repositories, custom JPQL analytics queries, and transaction-safe database access through Spring Data.
