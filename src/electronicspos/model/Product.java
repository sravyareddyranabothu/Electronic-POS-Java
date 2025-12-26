package electronicspos.model;

public class Product {

    private int productId;
    private String name;
    private String category;
    private double price;
    private int stock;
    private String imagePath;   // ⭐ NEW

    public Product() {}

    public Product(int productId, String name, String category,
                   double price, int stock, String imagePath) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.imagePath = imagePath;
    }

    public int getProductId() { return productId; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public String getImagePath() { return imagePath; }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return name + " - ₹" + price;
    }
}
