package electronicspos.dao;

import electronicspos.db.DBConnection;
import electronicspos.model.Invoice;

import java.sql.*;

public class InvoiceDAO {

    private Connection conn = DBConnection.getConnection();

    // Save invoice and return generated ID
    public int addInvoice(Invoice invoice) {

        String sql = "INSERT INTO invoice (customer_id, invoice_date, total_amount, gst, grand_total) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, invoice.getCustomerId());
            ps.setDate(2, new java.sql.Date(invoice.getInvoiceDate().getTime()));
            ps.setDouble(3, invoice.getTotalAmount());
            ps.setDouble(4, invoice.getGst());
            ps.setDouble(5, invoice.getGrandTotal());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // invoice_id
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}