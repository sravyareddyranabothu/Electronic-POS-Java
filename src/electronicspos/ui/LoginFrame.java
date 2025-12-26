package electronicspos.ui;

import electronicspos.dao.UserDAO;
import electronicspos.model.User;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginFrame extends JFrame {

    JTextField txtUser;
    JPasswordField txtPass;
    JCheckBox cbShow;
    JButton btnLogin, btnAdmin, btnCashier;
    String role = "ADMIN";

    Color NAVY = new Color(18, 38, 63);
    Color GRADIENT_START = new Color(18, 38, 63);
    Color GRADIENT_END = new Color(50, 80, 130);
    Color ACCENT = new Color(0, 120, 215); 
    Color INACTIVE = new Color(200, 200, 200);
    Color CARD_BG = new Color(230, 230, 230); 
    Color LOGIN_GREEN = new Color(70, 180, 90);
    Color LOGIN_GREEN_HOVER = new Color(50, 160, 80);

    public LoginFrame() {
        setTitle("Electronics POS");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        int w = Toolkit.getDefaultToolkit().getScreenSize().width;
        int h = Toolkit.getDefaultToolkit().getScreenSize().height;

        
        JPanel main = new JPanel(null);
        main.setBounds(0, 0, w, h);
        add(main);

        JPanel left = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, GRADIENT_START, 0, getHeight(), GRADIENT_END);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        left.setBounds(0, 0, w / 2, h);
        left.setLayout(null);
        main.add(left);

        JLabel title = new JLabel("<html>SR ELECTRONICS <br>POINT OF SALE</html>");
        title.setFont(new Font("Segoe UI", Font.BOLD, 40));
        title.setForeground(Color.WHITE);
        title.setBounds(100, 250, 600, 100);
        left.add(title);

        JLabel sub = new JLabel("Billing • Inventory • GST • Reports");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        sub.setForeground(new Color(200, 210, 220));
        sub.setBounds(100, 360, 500, 30);
        left.add(sub);

        
        JPanel right = new JPanel(null);
        right.setBounds(w / 2, 0, w / 2, h);
        right.setBackground(new Color(245, 245, 245));
        main.add(right);

        JPanel card = new JPanel(null);
        card.setBounds(160, 200, 500, 420);
        card.setBackground(CARD_BG); // light gray background
        card.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        right.add(card);

        JLabel loginTitle = new JLabel("USER LOGIN", SwingConstants.CENTER);
        loginTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        loginTitle.setBounds(0, 20, 500, 30);
        card.add(loginTitle);

        // ===== ROLE BUTTONS =====
        btnAdmin = new JButton("ADMIN");
        btnCashier = new JButton("CASHIER");

        btnAdmin.setBounds(60, 70, 170, 40);
        btnCashier.setBounds(270, 70, 170, 40);

        btnAdmin.setBackground(ACCENT);
        btnAdmin.setForeground(Color.WHITE);
        btnCashier.setBackground(INACTIVE);
        btnCashier.setForeground(Color.DARK_GRAY);

        btnAdmin.setFocusPainted(false);
        btnCashier.setFocusPainted(false);

        btnAdmin.addActionListener(e -> switchRole("ADMIN"));
        btnCashier.addActionListener(e -> switchRole("CASHIER"));

        card.add(btnAdmin);
        card.add(btnCashier);

        // ===== USERNAME =====
        JLabel lblUser = new JLabel("Username");
        lblUser.setBounds(60, 130, 100, 25);
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        card.add(lblUser);

        txtUser = new JTextField();
        txtUser.setBounds(60, 155, 380, 40);
        txtUser.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        card.add(txtUser);

        // ===== PASSWORD =====
        JLabel lblPass = new JLabel("Password");
        lblPass.setBounds(60, 205, 100, 25);
        lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        card.add(lblPass);

        txtPass = new JPasswordField();
        txtPass.setBounds(60, 230, 380, 40);
        txtPass.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        card.add(txtPass);

        cbShow = new JCheckBox("Show Password");
        cbShow.setBounds(60, 275, 150, 25);
        cbShow.setBackground(CARD_BG);
        cbShow.addActionListener(e -> txtPass.setEchoChar(cbShow.isSelected() ? (char) 0 : '•'));
        card.add(cbShow);

        // ===== LOGIN BUTTON (Green) =====
        btnLogin = new JButton("LOGIN");
        btnLogin.setBounds(60, 320, 380, 50);
        btnLogin.setBackground(LOGIN_GREEN);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.setFocusPainted(false);
        addHoverEffect(btnLogin, LOGIN_GREEN_HOVER, LOGIN_GREEN); // green hover
        btnLogin.addActionListener(e -> login());
        card.add(btnLogin);

        setVisible(true);
    }

    private void switchRole(String r) {
        role = r;
        if (r.equals("ADMIN")) {
            btnAdmin.setBackground(ACCENT);
            btnAdmin.setForeground(Color.WHITE);
            btnCashier.setBackground(INACTIVE);
            btnCashier.setForeground(Color.DARK_GRAY);
        } else {
            btnCashier.setBackground(ACCENT);
            btnCashier.setForeground(Color.WHITE);
            btnAdmin.setBackground(INACTIVE);
            btnAdmin.setForeground(Color.DARK_GRAY);
        }
    }

    private void login() {
        User u = new UserDAO().validateLogin(txtUser.getText(), new String(txtPass.getPassword()));
        if (u != null && u.getRole().equalsIgnoreCase(role)) {
            dispose();
            new DashboardFrame(u);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid " + role + " Login");
        }
    }

    private void addHoverEffect(JButton btn, Color hoverBg, Color normalBg) {
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(hoverBg);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(normalBg);
            }
        });
    }

    public static void main(String[] args) {
        new LoginFrame();
    }
}
