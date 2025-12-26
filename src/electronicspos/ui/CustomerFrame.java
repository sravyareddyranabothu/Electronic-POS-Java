package electronicspos.ui;

import electronicspos.dao.CustomerDAO;
import electronicspos.model.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class CustomerFrame extends JFrame {

    private JTable customerTable;
    private DefaultTableModel tableModel;
    private JButton btnDelete, btnRefresh;
    private CustomerDAO customerDAO;

    // ===== COLORS (MATCH SALES REPORT) =====
    private final Color VIOLET = new Color(99, 102, 241);
    private final Color VIOLET_DARK = new Color(79, 70, 229);
    private final Color DARK_BAR = new Color(31, 41, 55);
    private final Color BG_WHITE = Color.WHITE;

    public CustomerFrame() {

        setTitle("Customer Management");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        customerDAO = new CustomerDAO();

        // ===== TOP BAR =====
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(DARK_BAR);
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel title = new JLabel("Customer Management");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));

        topBar.add(title, BorderLayout.WEST);
        add(topBar, BorderLayout.NORTH);

        // ===== TABLE =====
        tableModel = new DefaultTableModel(
                new String[]{"ID", "Name", "Email", "Phone", "Address"}, 0
        ) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        customerTable = new JTable(tableModel);
        customerTable.setRowHeight(28);
        customerTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        customerTable.setSelectionBackground(VIOLET);
        customerTable.setSelectionForeground(Color.WHITE);

        JTableHeader header = customerTable.getTableHeader();
        header.setBackground(VIOLET);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(100, 35));

        JScrollPane scrollPane = new JScrollPane(customerTable);
        add(scrollPane, BorderLayout.CENTER);

        // ===== BUTTON PANEL =====
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(BG_WHITE);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        btnRefresh = violetButton("Refresh");
        btnDelete = deleteButton("Delete Customer");

        btnPanel.add(btnRefresh);
        btnPanel.add(Box.createHorizontalStrut(10));
        btnPanel.add(btnDelete);

        add(btnPanel, BorderLayout.SOUTH);

        // ===== LOAD DATA =====
        loadCustomers();

        // ===== EVENTS =====
        btnRefresh.addActionListener(e -> loadCustomers());
        btnDelete.addActionListener(e -> deleteCustomer());
    }

    // ===== LOAD CUSTOMERS =====
    private void loadCustomers() {
        tableModel.setRowCount(0);
        for (Customer c : customerDAO.getAllCustomers()) {
            tableModel.addRow(new Object[]{
                    c.getCustomerId(),
                    c.getName(),
                    c.getEmail(),
                    c.getPhone(),
                    c.getAddress()
            });
        }
    }

    // ===== DELETE CUSTOMER =====
    private void deleteCustomer() {
        int row = customerTable.getSelectedRow();
        if (row >= 0) {
            int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Delete selected customer?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                customerDAO.deleteCustomer(id);
                loadCustomers();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a customer first");
        }
    }

    // ===== BUTTON STYLES =====
    private JButton violetButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(VIOLET);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        return b;
    }

    private JButton deleteButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(new Color(239, 68, 68)); // red
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        return b;
    }
}
