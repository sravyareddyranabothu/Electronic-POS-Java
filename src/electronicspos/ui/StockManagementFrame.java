package electronicspos.ui;

import electronicspos.db.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class StockManagementFrame extends JFrame {

    JTable table;
    DefaultTableModel model;
    Connection conn = DBConnection.getConnection();

    public StockManagementFrame() {

        setTitle("Daily Stock & Sales Report");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10,10));

        // ===== HEADER =====
        JLabel title = new JLabel("Daily Stock & Sales Report");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

        add(title, BorderLayout.NORTH);

        // ===== TABLE MODEL =====
        model = new DefaultTableModel(
            new String[]{
                "Date",
                "Cashier",
                "Product Name",
                "Opening Stock",
                "Sold Qty",
                "Remaining Stock"
            }, 0
        );

        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(108, 92, 231));
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        add(sp, BorderLayout.CENTER);

        loadDailyStockSales();
        setVisible(true); // Make frame visible
    }

    // ===== LOAD DATA =====
    private void loadDailyStockSales() {

        model.setRowCount(0);

        String sql =
            "SELECT p.product_name, " +
            "p.quantity AS opening_stock, " +
            "NVL(SUM(si.quantity), 0) AS sold_qty, " +
            "p.quantity - NVL(SUM(si.quantity), 0) AS remaining_stock, " +
            "u.name AS cashier, " +
            "TRUNC(SYSDATE) AS sale_date " +
            "FROM products p " +
            "LEFT JOIN sales_item si ON p.product_id = si.product_id " +
            "LEFT JOIN sales_invoice s ON si.invoice_id = s.invoice_id AND TRUNC(s.invoice_date) = TRUNC(SYSDATE) " +
            "LEFT JOIN users u ON s.user_id = u.user_id " +
            "GROUP BY p.product_name, p.quantity, u.name " +
            "ORDER BY p.product_name";

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getDate("sale_date"),
                    rs.getString("cashier") != null ? rs.getString("cashier") : "-",
                    rs.getString("product_name"),
                    rs.getInt("opening_stock"),
                    rs.getInt("sold_qty"),
                    rs.getInt("remaining_stock")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
