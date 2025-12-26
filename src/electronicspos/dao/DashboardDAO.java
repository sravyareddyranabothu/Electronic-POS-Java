package electronicspos.dao;

import java.sql.*;
import electronicspos.db.DBConnection;

public class DashboardDAO {

    // ================= TOTAL SALES AMOUNT (ALL TIME) =================
    public double getTotalEarnings() {

        String sql = "SELECT NVL(SUM(total_amount), 0) FROM sales_invoice";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ================= TOTAL BILLS COUNT =================
    public int getTotalSales() {

        String sql = "SELECT COUNT(*) FROM sales_invoice";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ================= TOTAL PRODUCTS =================
    public int getTotalProducts() {

        String sql = "SELECT COUNT(*) FROM products";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
        
    }

    // ================= SALES TODAY =================
    public double getTodaySales() {

        String sql = """
                SELECT NVL(SUM(total_amount), 0)
                FROM sales_invoice
                WHERE TRUNC(invoice_date) = TRUNC(SYSDATE)
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ================= BILLS TODAY =================
    public int getTodayBills() {

        String sql = """
                SELECT COUNT(*)
                FROM sales_invoice
                WHERE TRUNC(invoice_date) = TRUNC(SYSDATE)
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
