package electronicspos.ui;

import electronicspos.dao.DashboardDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CashierDashboardPanel extends JPanel {

    Color sidebarBg  = new Color(32, 36, 48);
    Color primary    = new Color(108, 92, 231);
    Color hoverColor = new Color(45, 50, 65);

    JButton btnDashboard, btnBilling, btnCustomers, btnLogout;
    JLabel lblSales, lblBills, lblProducts, lblBalance;

    DashboardDAO dashboardDAO = new DashboardDAO();

    public CashierDashboardPanel() {

        setLayout(new BorderLayout());
        setBackground(new Color(245, 246, 250));

        add(createSidebar(), BorderLayout.WEST);
        add(createTopBar(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);

        connectActions();
    }

    private JPanel createSidebar() {

        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setBackground(sidebarBg);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        JLabel logo = new JLabel("CASHIER");
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logo.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 0));
        sidebar.add(logo);

        btnDashboard = menuButton("Dashboard", true);
        btnBilling   = menuButton("Billing", false);
        btnCustomers = menuButton("Customers Management", false);
        btnLogout    = menuButton("Logout", false);

        sidebar.add(btnDashboard);
        sidebar.add(btnBilling);
        sidebar.add(btnCustomers);
        sidebar.add(btnLogout);

        return sidebar;
    }

    private JButton menuButton(String text, boolean active) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(230, 42));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 0));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBackground(active ? primary : sidebarBg);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { if (!active) btn.setBackground(hoverColor); }
            public void mouseExited(MouseEvent e) { if (!active) btn.setBackground(sidebarBg); }
        });

        return btn;
    }

    private JPanel createTopBar() {
        JPanel top = new JPanel(new BorderLayout());
        top.setPreferredSize(new Dimension(0, 55));
        top.setBackground(Color.WHITE);
        top.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));

        JLabel title = new JLabel("Cashier Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 0));

        JTextField search = new JTextField(" Search...");
        search.setPreferredSize(new Dimension(200, 30));
        search.setForeground(Color.GRAY);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        right.setBackground(Color.WHITE);
        right.add(search);

        top.add(title, BorderLayout.WEST);
        top.add(right, BorderLayout.EAST);
        return top;
    }

    private JPanel createMainContent() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(new Color(245, 246, 250));
        main.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        main.add(createStatsCards(), BorderLayout.NORTH);
        return main;
    }

    private JPanel createStatsCards() {

        double salesToday  = dashboardDAO.getTodaySales();
        int billsToday     = dashboardDAO.getTodayBills();
        int totalProducts  = dashboardDAO.getTotalProducts();
        double totalAmount = dashboardDAO.getTotalEarnings();

        lblSales    = new JLabel("₹ " + String.format("%.2f", salesToday));
        lblBills    = new JLabel(String.valueOf(billsToday));
        lblProducts = new JLabel(String.valueOf(totalProducts));
        lblBalance  = new JLabel("₹ " + String.format("%.2f", totalAmount));

        JPanel cards = new JPanel(new GridLayout(1, 4, 15, 15));
        cards.setBackground(new Color(245, 246, 250));

        cards.add(statCard("Sales Today", lblSales));
        cards.add(statCard("Bills Today", lblBills));
        cards.add(statCard("Products", lblProducts));
        cards.add(statCard("Total Balance", lblBalance));

        return cards;
    }

    private JPanel statCard(String title, JLabel valueLabel) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        t.setBorder(BorderFactory.createEmptyBorder(10, 12, 0, 0));

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valueLabel.setForeground(primary);
        valueLabel.setBorder(BorderFactory.createEmptyBorder(5, 12, 10, 0));

        card.add(t, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    // ---------------- REFRESH STATS ----------------
    public void refreshStats() {
        lblProducts.setText(String.valueOf(dashboardDAO.getTotalProducts()));
        lblBills.setText(String.valueOf(dashboardDAO.getTodayBills()));
        lblSales.setText("₹ " + String.format("%.2f", dashboardDAO.getTodaySales()));
        lblBalance.setText("₹ " + String.format("%.2f", dashboardDAO.getTotalEarnings()));
    }

    private void connectActions() {

        btnBilling.addActionListener(e -> new BillingFrame().setVisible(true));
        btnCustomers.addActionListener(e -> new CustomerFrame().setVisible(true));
        btnLogout.addActionListener(e -> {
            SwingUtilities.getWindowAncestor(this).dispose();
            new LoginFrame();
        });
    }
}
