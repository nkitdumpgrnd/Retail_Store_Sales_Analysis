import pandas as pd
import matplotlib.pyplot as plt


DATASET_PATH = "../data/sales_data.csv"


def main():
    df = pd.read_csv(DATASET_PATH, parse_dates=["order_date"])

    print("First rows")
    print(df.head())

    print("\nDataset structure")
    print(df.info())

    print("\nSummary statistics")
    print(df.describe(include="all"))

    df["total_sales"] = df["quantity"] * df["price"]

    total_revenue = df["total_sales"].sum()
    most_sold_product = df.groupby("product")["quantity"].sum().idxmax()
    city_sales = df.groupby("city")["total_sales"].sum().sort_values(ascending=False)
    category_sales = df.groupby("category")["total_sales"].sum().sort_values(ascending=False)
    date_sales = df.groupby("order_date")["total_sales"].sum()

    print(f"\nTotal revenue: {total_revenue}")
    print(f"Most sold product: {most_sold_product}")
    print("\nSales by city")
    print(city_sales)
    print("\nSummary statistics with total_sales")
    print(df.describe())

    product_sales = df.groupby("product")["total_sales"].sum().sort_values(ascending=False)
    product_sales.plot(kind="bar", title="Product vs Total Sales", ylabel="Total Sales")
    plt.tight_layout()
    plt.show()

    category_sales.plot(kind="pie", title="Category Sales Distribution", autopct="%1.1f%%")
    plt.ylabel("")
    plt.tight_layout()
    plt.show()

    date_sales.plot(kind="line", marker="o", title="Sales by Date", ylabel="Total Sales")
    plt.tight_layout()
    plt.show()


if __name__ == "__main__":
    main()
