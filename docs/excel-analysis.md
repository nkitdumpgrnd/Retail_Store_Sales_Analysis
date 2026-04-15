# Excel Analysis Steps

1. Open Excel and import `data/sales_data.csv`.
2. Add a new column named `Total Sales`.
3. In the first data row of `Total Sales`, enter `=F2*G2`, then fill down.
4. Sort the table by `Total Sales` from largest to smallest.
5. Use the city filter to compare Pune, Mumbai, and Nagpur.
6. Insert a Pivot Table.
7. Set `Rows` to `Product`.
8. Set `Columns` to `City`.
9. Set `Values` to `Sum of Total Sales`.
10. Identify the product and city with the highest value.

Suggested charts:

- Bar chart: `Product` vs `Total Sales`
- Pie chart: category-wise sales distribution
- Line chart: `Order Date` vs `Total Sales`
