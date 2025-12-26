package electronicspos.dao;

import electronicspos.db.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {

    private Connection conn = DBConnection.getConnection();

    // WEEKLY PRODUCT SALES
    public List<Object[]> getWeeklyProductSales(Date startDate, Date endDate) {
        List<Object[]> list = new ArrayList<>();
        String sql = """
                SELECT u.name AS cashier,
                       p.product_name,
                       SUM(si.quantity) AS quantity_sold,
                       SUM(si.total) AS total_amount
                FROM sales_item si
                JOIN sales_invoice i ON si.invoice_id = i.invoice_id
                JOIN products p ON si.product_id = p.product_id
                JOIN users u ON i.user_id = u.user_id
                WHERE TRUNC(i.invoice_date) BETWEEN ? AND ?
                GROUP BY u.name, p.product_name
                ORDER BY u.name, p.product_name
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, startDate);
            ps.setDate(2, endDate);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{
                        rs.getString("cashier"),
                        rs.getString("product_name"),
                        rs.getInt("quantity_sold"),
                        rs.getDouble("total_amount")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // MONTHLY PRODUCT SALES
    public List<Object[]> getMonthlyProductSales(int month, int year) {
        List<Object[]> list = new ArrayList<>();
        String sql = """
                SELECT u.name AS cashier,
                       p.product_name,
                       SUM(si.quantity) AS quantity_sold,
                       SUM(si.total) AS total_amount
                FROM sales_item si
                JOIN sales_invoice i ON si.invoice_id = i.invoice_id
                JOIN products p ON si.product_id = p.product_id
                JOIN users u ON i.user_id = u.user_id
                WHERE EXTRACT(MONTH FROM i.invoice_date) = ? 
                  AND EXTRACT(YEAR FROM i.invoice_date) = ?
                GROUP BY u.name, p.product_name
                ORDER BY u.name, p.product_name
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, month);
            ps.setInt(2, year);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{
                        rs.getString("cashier"),
                        rs.getString("product_name"),
                        rs.getInt("quantity_sold"),
                        rs.getDouble("total_amount")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // YEARLY PRODUCT SALES
    public List<Object[]> getYearlyProductSales(int year) {
        List<Object[]> list = new ArrayList<>();
        String sql = """
                SELECT u.name AS cashier,
                       p.product_name,
                       SUM(si.quantity) AS quantity_sold,
                       SUM(si.total) AS total_amount
                FROM sales_item si
                JOIN sales_invoice i ON si.invoice_id = i.invoice_id
                JOIN products p ON si.product_id = p.product_id
                JOIN users u ON i.user_id = u.user_id
                WHERE EXTRACT(YEAR FROM i.invoice_date) = ?
                GROUP BY u.name, p.product_name
                ORDER BY u.name, p.product_name
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, year);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{
                        rs.getString("cashier"),
                        rs.getString("product_name"),
                        rs.getInt("quantity_sold"),
                        rs.getDouble("total_amount")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
