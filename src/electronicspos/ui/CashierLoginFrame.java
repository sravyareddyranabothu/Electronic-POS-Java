package electronicspos.ui;

import electronicspos.dao.UserDAO;
import electronicspos.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CashierLoginFrame extends JFrame implements ActionListener {

    JTextField txtUsername;
    JPasswordField txtPassword;
    JButton btnLogin, btnBack;

    public CashierLoginFrame() {

        setTitle("Cashier Login - Electronics POS");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Background color
        getContentPane().setBackground(new Color(20, 60, 40));

        JLabel title = new JLabel("CASHIER LOGIN", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        title.setBounds(540, 120, 420, 40);
        add(title);

        JLabel u = new JLabel("Username:");
        u.setForeground(Color.WHITE);
        u.setBounds(500, 220, 120, 30);
        add(u);

        txtUsername = new JTextField();
        txtUsername.setBounds(620, 220, 220, 30);
        add(txtUsername);

        JLabel p = new JLabel("Password:");
        p.setForeground(Color.WHITE);
        p.setBounds(500, 270, 120, 30);
        add(p);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(620, 270, 220, 30);
        txtPassword.addActionListener(this); // Press Enter to login
        add(txtPassword);

        btnLogin = new JButton("Login");
        btnLogin.setBounds(540, 350, 120, 40);
        btnLogin.addActionListener(this);
        add(btnLogin);

        btnBack = new JButton("Back");
        btnBack.setBounds(700, 350, 120, 40);
        btnBack.addActionListener(this);
        add(btnBack);

        // Set focus to username field
        txtUsername.requestFocusInWindow();

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnLogin || e.getSource() == txtPassword) {

            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword());

            // Input validation
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please enter both username and password",
                        "Input Required",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            UserDAO dao = new UserDAO();
            User user = dao.validateLogin(username, password);

            if (user != null && user.getRole().equalsIgnoreCase("CASHIER")) {
                dispose();
                new DashboardFrame(user); // Open cashier dashboard
            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid Cashier Credentials",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
                txtPassword.setText("");
                txtPassword.requestFocusInWindow();
            }
        }

        if (e.getSource() == btnBack) {
            dispose();
            new LoginFrame(); // Back to role selection
        }
    }

    public static void main(String[] args) {
        new CashierLoginFrame();
    }
}