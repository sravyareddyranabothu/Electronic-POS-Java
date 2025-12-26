package electronicspos.dao;

import electronicspos.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InvoiceItemDAO {

    private Connection conn = DBConnection.getConnection();

    // Save individual invoice item
    public boolean addItem(int invoiceId, int productId, int qty, double price) {
        try {
            String sql = "INSERT INTO invoice_items (item_id, invoice_id, product_id, quantity, price) " +
                         "VALUES (invoice_item_seq.NEXTVAL, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, invoiceId);
            ps.setInt(2, productId);
            ps.setInt(3, qty);
            ps.setDouble(4, price);

            int rows = ps.executeUpdate();
            ps.close();

            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
