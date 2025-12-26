package electronicspos.ui;

import electronicspos.dao.CustomerDAO;
import electronicspos.dao.ProductDAO;
import electronicspos.model.Customer;
import electronicspos.model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BillingFrame extends JFrame {

    JTable table;
    DefaultTableModel model;

    JLabel lblSubtotal, lblGST, lblTotal;
    JTextField txtName, txtMobile, txtEmail, txtAddress;

    double subtotal = 0;

    ProductDAO productDAO = new ProductDAO();

    // ===== COLORS (OLD BILLING FRAME THEME) =====
    Color GREEN  = new Color(22, 163, 74);
    Color RED    = new Color(220, 38, 38);
    Color PURPLE = new Color(150, 50, 200);
    Color BLUE   = new Color(37, 99, 235);

    public BillingFrame() {

        setTitle("SR ELECTRONICS POS");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));

        JScrollPane sp = new JScrollPane(
                productPanel(),
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        sp.setBorder(null);
        sp.getVerticalScrollBar().setUnitIncrement(18);

        leftPanel.add(sp, BorderLayout.CENTER);
        leftPanel.add(customerPanel(), BorderLayout.SOUTH);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(520, 0));
        rightPanel.add(billTablePanel(), BorderLayout.CENTER);
        rightPanel.add(billSummaryPanel(), BorderLayout.SOUTH);

        add(leftPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);

        setVisible(true);
    }

    // ================= PRODUCTS FROM DB =================
    JPanel productPanel() {

        JPanel panel = new JPanel(new GridLayout(0, 3, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        List<Product> products = productDAO.getAllProducts();

        for (Product p : products) {
            panel.add(productCard(p));
        }
        return panel;
    }

    JPanel productCard(Product p) {

        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        card.setBackground(Color.WHITE);

        JLabel imgLabel = new JLabel("No Image", SwingConstants.CENTER);

        try {
            String path = p.getImagePath(); // from DB

            if (path != null && !path.isEmpty()) {

                ImageIcon icon;

                // ✅ If image is inside project (resources)
                if (path.startsWith("images/")) {
                    icon = new ImageIcon(getClass().getClassLoader().getResource(path));
                }
                // ✅ If image is external (uploaded by admin)
                else {
                    icon = new ImageIcon(path);
                }

                Image img = icon.getImage().getScaledInstance(100, 70, Image.SCALE_SMOOTH);
                imgLabel.setIcon(new ImageIcon(img));
                imgLabel.setText("");
            }

        } catch (Exception e) {
            System.out.println("Image load failed for: " + p.getName());
        }


        JLabel lblName = new JLabel(p.getName(), SwingConstants.CENTER);
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JLabel lblPrice = new JLabel("₹ " + p.getPrice(), SwingConstants.CENTER);
        lblPrice.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        JButton btnAdd = new JButton("ADD");
        btnAdd.setBackground(GREEN);
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.setFocusPainted(false);

        btnAdd.addActionListener(e ->
                addOrUpdateProduct(p.getName(), p.getPrice())
        );

        JPanel bottom = new JPanel(new GridLayout(3, 1));
        bottom.setOpaque(false);
        bottom.add(lblName);
        bottom.add(lblPrice);
        bottom.add(btnAdd);

        card.add(imgLabel, BorderLayout.CENTER);
        card.add(bottom, BorderLayout.SOUTH);

        return card;
    }

    // ================= BILL TABLE =================
    JPanel billTablePanel() {

        model = new DefaultTableModel(
                new Object[]{"Product", "Qty", "Price", "Total"}, 0
        ) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(24);

        return new JPanel(new BorderLayout()) {{
            add(new JScrollPane(table), BorderLayout.CENTER);
        }};
    }

    
    JPanel billSummaryPanel() {

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(0, 250));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        lblSubtotal = summaryLabel("Subtotal");
        lblGST = summaryLabel("GST (18%)");
        lblTotal = summaryLabel("Total");
        lblTotal.setForeground(GREEN);

        JButton btnRemove   = colorButton("REMOVE ITEM", RED);
        JButton btnClear    = colorButton("CLEAR BILL", PURPLE);
        JButton btnGenerate = colorButton("GENERATE BILL", BLUE);

        btnRemove.addActionListener(e -> removeItem());
        btnClear.addActionListener(e -> clearBill());
        btnGenerate.addActionListener(e -> generateBill());

        btnRemove.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnClear.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnGenerate.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnRemove.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnClear.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnGenerate.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        panel.add(lblSubtotal);
        panel.add(Box.createVerticalStrut(8));
        panel.add(lblGST);
        panel.add(Box.createVerticalStrut(8));
        panel.add(lblTotal);
        panel.add(Box.createVerticalStrut(15));
        panel.add(btnRemove);
        panel.add(Box.createVerticalStrut(8));
        panel.add(btnClear);
        panel.add(Box.createVerticalStrut(15));
        panel.add(btnGenerate);

        return panel;
    }

    JLabel summaryLabel(String text) {
        JLabel lbl = new JLabel(text + "   ₹ 0.00");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return lbl;
    }

    JButton colorButton(String text, Color c) {
        JButton b = new JButton(text);
        b.setBackground(c);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        return b;
    }

   
    JPanel customerPanel() {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Customer Details"));

        txtName = new JTextField();
        txtMobile = new JTextField();
        txtEmail = new JTextField();
        txtAddress = new JTextField();

        panel.add(customerRow("Name", txtName, "Mobile", txtMobile));
        panel.add(Box.createVerticalStrut(6));
        panel.add(customerRow("Email", txtEmail, "Address", txtAddress));

        panel.add(Box.createVerticalStrut(10));

        
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        JButton btnSave = new JButton("SAVE");
        btnSave.setBackground(GREEN);
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.setPreferredSize(new Dimension(80, 30));

        JButton btnClear = new JButton("CLEAR");
        btnClear.setBackground(RED);
        btnClear.setForeground(Color.WHITE);
        btnClear.setFocusPainted(false);
        btnClear.setPreferredSize(new Dimension(80, 30));

        btnSave.addActionListener(e -> saveCustomer());
        btnClear.addActionListener(e -> {
            txtName.setText("");
            txtMobile.setText("");
            txtEmail.setText("");
            txtAddress.setText("");
        });

        btnRow.add(btnSave);
        btnRow.add(btnClear);

        panel.add(btnRow);

        return panel;
    }


    JPanel customerRow(String l1, JTextField f1, String l2, JTextField f2) {
        JPanel r = new JPanel(new GridLayout(1, 2, 10, 0));
        r.add(customerField(l1, f1));
        r.add(customerField(l2, f2));
        return r;
    }

    JPanel customerField(String label, JTextField field) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JLabel(label), BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    // ================= LOGIC =================
    void addOrUpdateProduct(String name, double price) {

        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).equals(name)) {
                int qty = (int) model.getValueAt(i, 1) + 1;
                model.setValueAt(qty, i, 1);
                model.setValueAt(qty * price, i, 3);
                subtotal += price;
                updateTotals();
                return;
            }
        }
        model.addRow(new Object[]{name, 1, price, price});
        subtotal += price;
        updateTotals();
    }

    void updateTotals() {
        double gst = subtotal * 0.18;
        lblSubtotal.setText("Subtotal   ₹ " + String.format("%.2f", subtotal));
        lblGST.setText("GST (18%)   ₹ " + String.format("%.2f", gst));
        lblTotal.setText("Total   ₹ " + String.format("%.2f", subtotal + gst));
    }

    void removeItem() {
        int r = table.getSelectedRow();
        if (r >= 0) {
            subtotal -= (double) model.getValueAt(r, 3);
            model.removeRow(r);
            updateTotals();
        }
    }

    void clearBill() {
        model.setRowCount(0);
        subtotal = 0;
        updateTotals();
    }

    void saveCustomer() {

        String name = txtName.getText().trim();
        String mobile = txtMobile.getText().trim();
        String email = txtEmail.getText().trim();
        String address = txtAddress.getText().trim();

        if (name.isEmpty() || mobile.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Mobile are required");
            return;
        }

        CustomerDAO dao = new CustomerDAO();
        Customer existing = dao.getCustomerByPhone(mobile);

        if (existing != null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Customer already exists",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        Customer c = new Customer(0, name, email, mobile, address);

        if (dao.addCustomer(c)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Customer saved successfully",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }


    void generateBill() {

        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No items in bill");
            return;
        }

        String mobile = txtMobile.getText().trim();

        if (mobile.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter customer mobile");
            return;
        }

        CustomerDAO customerDAO = new CustomerDAO();
        Customer c = customerDAO.getCustomerByPhone(mobile);

        if (c == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Customer not saved. Please save customer first."
            );
            return;
        }

        double gst = subtotal * 0.18;
        double total = subtotal + gst;

        new InvoicepreviewFrame(
                c.getName(),
                c.getPhone(),
                c.getEmail(),
                c.getAddress(),
                model,
                subtotal,
                gst,
                total
        );

        clearBill();
    }


       

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BillingFrame::new);
    }
}
