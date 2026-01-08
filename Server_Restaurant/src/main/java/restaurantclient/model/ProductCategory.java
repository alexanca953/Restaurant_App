package restaurantclient.model;

import java.io.Serializable;

public class ProductCategory implements Serializable {
    private int categoryId;
    private String name;

    public ProductCategory() {}

    public ProductCategory(int categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    @Override
    public String toString() {
        return "ProductCategory{" +
                "categoryId=" + categoryId +
                ", name='" + name + '\'' +
                '}';
    }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}