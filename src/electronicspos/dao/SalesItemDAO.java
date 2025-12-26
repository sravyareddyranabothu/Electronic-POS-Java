package electronicspos.dao;

import electronicspos.db.DBConnection;
import electronicspos.model.SalesItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SalesItemDAO {

    private Connection conn = DBConnection.getConnection();

    public void addSalesItem(SalesItem item) {

        String sql = "INSERT INTO sales_item (invoice_id, product_id, quantity, price) " +
                     "VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, item.getInvoiceId());
            ps.setInt(2, item.getProductId());
            ps.setInt(3, item.getQuantity());
            ps.setDouble(4, item.getPrice());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}