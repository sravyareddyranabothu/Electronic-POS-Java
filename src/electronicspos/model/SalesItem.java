package electronicspos.model;

public class SalesItem {

    private int salesItemId;
    private int invoiceId;
    private int productId;
    private int quantity;
    private double price;

    public SalesItem() {}

    public SalesItem(int salesItemId, int invoiceId,
                     int productId, int quantity, double price) {
        this.salesItemId = salesItemId;
        this.invoiceId = invoiceId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public int getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
}