package model;

public class Product {
    private int productId;
    private int categoryId;
    private String productName;
    private String description;
    private Double price;
    private String ingredients;

    public Product() {}

    public Product(int productId, int categoryId, String productName, String description, Double price, String ingredients) {
        this.productId = productId;
        this.categoryId = categoryId;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.ingredients = ingredients;
    }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getIngredients() { return ingredients; }
    public void setIngredients(String ingredients) { this.ingredients = ingredients; }

    @Override
    public String toString() {
        return productName + " - $" + price;
    }
}