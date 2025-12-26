package electronicspos.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import electronicspos.dao.DashboardDAO;

public class AdminDashboardPanel extends JPanel {

    Color sidebarBg = new Color(32, 36, 48);
    Color primary   = new Color(108, 92, 231);
    Color hoverColor = new Color(45, 50, 65);

    JButton btnDashboard, btnProducts, btnSales, btnReports, btnLogout;
    JLabel lblEarnings, lblSales, lblProducts, lblBalance;
    DashboardDAO dashboardDAO = new DashboardDAO();

    public AdminDashboardPanel() {

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

        JLabel logo = new JLabel("ADMIN");
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logo.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 0));
        sidebar.add(logo);

        btnDashboard = menuButton("Dashboard", true);
        btnProducts  = menuButton("Products", false);
        btnSales     = menuButton("Stock Management", false);
        btnReports   = menuButton("Sales Reports", false);
        btnLogout    = menuButton("Logout", false);

        sidebar.add(btnDashboard);
        sidebar.add(btnProducts);
        sidebar.add(btnSales);
        sidebar.add(btnReports);
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

        JLabel title = new JLabel("Admin Dashboard");
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
        double todayEarnings = dashboardDAO.getTodaySales();
        int todaySales       = dashboardDAO.getTodayBills();
        int products         = dashboardDAO.getTotalProducts();

        lblEarnings = new JLabel("₹" + String.format("%.2f", todayEarnings));
        lblSales    = new JLabel(String.valueOf(todaySales));
        lblProducts = new JLabel(String.valueOf(products));
        lblBalance  = new JLabel("₹" + String.format("%.2f", todayEarnings));

        JPanel cards = new JPanel(new GridLayout(1, 4, 15, 15));
        cards.setBackground(new Color(245, 246, 250));

        cards.add(statCard("Sales Today", lblEarnings));
        cards.add(statCard("Bills Today", lblSales));
        cards.add(statCard("Products", lblProducts));
        cards.add(statCard("Balance Today", lblBalance));

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

    // ---------------- REFRESH STAT CARDS ----------------
    public void refreshStats() {
        lblProducts.setText(String.valueOf(dashboardDAO.getTotalProducts()));
        lblSales.setText(String.valueOf(dashboardDAO.getTodayBills()));
        lblEarnings.setText("₹" + String.format("%.2f", dashboardDAO.getTodaySales()));
        lblBalance.setText("₹" + String.format("%.2f", dashboardDAO.getTodaySales()));
    }

    private void connectActions() {
        btnProducts.addActionListener(e -> new ProductManagementFrame(this).setVisible(true));
        btnSales.addActionListener(e -> new StockManagementFrame().setVisible(true));
        btnReports.addActionListener(e -> new ReportFrame().setVisible(true));
        btnLogout.addActionListener(e -> {
            SwingUtilities.getWindowAncestor(this).dispose();
            new LoginFrame();
        });
    }
}
