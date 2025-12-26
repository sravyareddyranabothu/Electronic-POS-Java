package electronicspos.ui;

import electronicspos.dao.ProductDAO;
import electronicspos.model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ProductManagementFrame extends JFrame {

    AdminDashboardPanel adminPanel;

    JTextField txtName, txtPrice, txtStock;
    JComboBox<String> comboCategory;
    JButton btnAdd, btnUpdate, btnDelete, btnClear, btnReload, btnImage;
    JTable table;
    DefaultTableModel model;

    int selectedProductId = -1;
    String selectedImagePath = null;   // ⭐ IMAGE PATH

    ProductDAO productDAO = new ProductDAO();

    Color PRIMARY = new Color(108, 92, 231);
    Color PRIMARY_DARK = new Color(88, 74, 200);
    Color DANGER = new Color(239, 68, 68);
    Color NEUTRAL = new Color(156, 163, 175);
    Color WHITE = Color.WHITE;

    public ProductManagementFrame(AdminDashboardPanel dashboard) {
        this.adminPanel = dashboard;

        setTitle("Product Management - Electronics POS");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("Product Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        add(title, BorderLayout.NORTH);

        // ================= FORM =================
        JPanel formPanel = new JPanel(new GridLayout(2, 6, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Product Details"));

        txtName = new JTextField();
        comboCategory = new JComboBox<>(new String[]{
                "Accessories", "Computer", "Electronics", "Networking", "Peripherals"
        });
        txtPrice = new JTextField();
        txtStock = new JTextField();

        btnImage = styledButton("Choose Image", NEUTRAL);
        btnAdd   = styledButton("Add", PRIMARY);

        formPanel.add(new JLabel("Product Name"));
        formPanel.add(new JLabel("Category"));
        formPanel.add(new JLabel("Price"));
        formPanel.add(new JLabel("Stock"));
        formPanel.add(new JLabel("Image"));
        formPanel.add(new JLabel(""));

        formPanel.add(txtName);
        formPanel.add(comboCategory);
        formPanel.add(txtPrice);
        formPanel.add(txtStock);
        formPanel.add(btnImage);
        formPanel.add(btnAdd);

        add(formPanel, BorderLayout.NORTH);

        // ================= TABLE =================
        model = new DefaultTableModel(
                new String[]{"Product ID", "Product Name", "Category", "Price", "Stock"}, 0
        ) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setBackground(PRIMARY);
        table.getTableHeader().setForeground(WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;

            int row = table.getSelectedRow();
            if (row < 0) return;

            selectedProductId = (int) model.getValueAt(row, 0);
            txtName.setText(model.getValueAt(row, 1).toString());
            comboCategory.setSelectedItem(model.getValueAt(row, 2).toString());
            txtPrice.setText(model.getValueAt(row, 3).toString());
            txtStock.setText(model.getValueAt(row, 4).toString());
        });

        add(new JScrollPane(table), BorderLayout.CENTER);

        // ================= BUTTONS =================
        JPanel btnPanel = new JPanel();
        btnUpdate = styledButton("Update", PRIMARY_DARK);
        btnDelete = styledButton("Delete", DANGER);
        btnReload = styledButton("Reload", NEUTRAL);
        btnClear  = styledButton("Clear", DANGER);

        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnReload);
        btnPanel.add(btnClear);

        add(btnPanel, BorderLayout.SOUTH);

        loadProducts();

        // ================= ACTIONS =================
        btnImage.addActionListener(e -> chooseImage());
        btnAdd.addActionListener(e -> addProduct());
        btnReload.addActionListener(e -> reloadProducts());
        btnClear.addActionListener(e -> clearFields());
        btnDelete.addActionListener(e -> deleteProduct());
        btnUpdate.addActionListener(e -> updateProduct());
    }

    // ================= IMAGE PICKER =================
    private void chooseImage() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(
                new javax.swing.filechooser.FileNameExtensionFilter(
                        "Image Files", "jpg", "png", "jpeg"
                )
        );

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File src = chooser.getSelectedFile();
                File dir = new File("images/products/");
                dir.mkdirs();

                File dest = new File(dir, System.currentTimeMillis() + "_" + src.getName());
                Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);

                selectedImagePath = dest.getPath();
                JOptionPane.showMessageDialog(this, "Image Selected");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Image error");
            }
        }
    }

    // ================= CRUD =================
    private void addProduct() {
        try {
            Product p = new Product(
                    0,
                    txtName.getText().trim(),
                    comboCategory.getSelectedItem().toString(),
                    Double.parseDouble(txtPrice.getText()),
                    Integer.parseInt(txtStock.getText()),
                    selectedImagePath
            );

            if (productDAO.addProduct(p)) {
                JOptionPane.showMessageDialog(this, "Product Added Successfully");
                loadProducts();
                clearFields();
                if (adminPanel != null) adminPanel.refreshStats();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input");
        }
    }

    private void updateProduct() {
        if (selectedProductId == -1) return;

        try {
            Product p = new Product(
                    selectedProductId,
                    txtName.getText().trim(),
                    comboCategory.getSelectedItem().toString(),
                    Double.parseDouble(txtPrice.getText()),
                    Integer.parseInt(txtStock.getText()),
                    selectedImagePath
            );

            productDAO.updateProduct(p);
            loadProducts();
            clearFields();
            if (adminPanel != null) adminPanel.refreshStats();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input");
        }
    }

    private void deleteProduct() {
        if (selectedProductId == -1) return;

        if (JOptionPane.showConfirmDialog(
                this, "Delete product?", "Confirm",
                JOptionPane.YES_NO_OPTION
        ) == JOptionPane.YES_OPTION) {

            productDAO.deleteProduct(selectedProductId);
            loadProducts();
            clearFields();
            if (adminPanel != null) adminPanel.refreshStats();
        }
    }

    private void loadProducts() {
        model.setRowCount(0);
        for (Product p : productDAO.getAllProducts()) {
            model.addRow(new Object[]{
                    p.getProductId(),
                    p.getName(),
                    p.getCategory(),
                    p.getPrice(),
                    p.getStock()
            });
        }
    }

    private void reloadProducts() {
        table.clearSelection();
        loadProducts();
        clearFields();
    }

    private void clearFields() {
        txtName.setText("");
        txtPrice.setText("");
        txtStock.setText("");
        comboCategory.setSelectedIndex(0);
        selectedImagePath = null;
        selectedProductId = -1;
    }

    private JButton styledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        return btn;
    }
}
