package electronicspos.model;

import java.util.Date;

public class Invoice {

    private int invoiceId;
    private int customerId;
    private Date invoiceDate;
    private double totalAmount;
    private double gst;
    private double grandTotal;

    public Invoice() {}

    public Invoice(int invoiceId, int customerId, Date invoiceDate,
                   double totalAmount, double gst, double grandTotal) {
        this.invoiceId = invoiceId;
        this.customerId = customerId;
        this.invoiceDate = invoiceDate;
        this.totalAmount = totalAmount;
        this.gst = gst;
        this.grandTotal = grandTotal;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public double getGst() {
        return gst;
    }

    public double getGrandTotal() {
        return grandTotal;
    }
}