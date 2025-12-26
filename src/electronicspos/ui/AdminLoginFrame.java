package electronicspos.ui;

import electronicspos.dao.UserDAO;
import electronicspos.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminLoginFrame extends JFrame implements ActionListener {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnBack;

    public AdminLoginFrame() {

        setTitle("Admin Login - Electronics POS");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Background
        getContentPane().setBackground(new Color(30, 30, 60));

        JLabel title = new JLabel("ADMIN LOGIN", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        title.setBounds(520, 120, 450, 40);
        add(title);

        JLabel lblUser = new JLabel("Username:");
        lblUser.setForeground(Color.WHITE);
        lblUser.setBounds(520, 220, 120, 30);
        add(lblUser);

        txtUsername = new JTextField();
        txtUsername.setBounds(650, 220, 220, 30);
        add(txtUsername);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setForeground(Color.WHITE);
        lblPass.setBounds(520, 270, 120, 30);
        add(lblPass);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(650, 270, 220, 30);
        add(txtPassword);

        btnLogin = new JButton("Login");
        btnLogin.setBounds(560, 350, 130, 40);
        btnLogin.setBackground(new Color(0, 153, 255));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.addActionListener(this);
        add(btnLogin);

        btnBack = new JButton("Back");
        btnBack.setBounds(720, 350, 130, 40);
        btnBack.setBackground(new Color(200, 50, 50));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.addActionListener(this);
        add(btnBack);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // ===== LOGIN =====
        if (e.getSource() == btnLogin) {

            String username = txtUsername.getText();
            String password = String.valueOf(txtPassword.getPassword());

            UserDAO dao = new UserDAO();
            User user = dao.validateLogin(username, password);

            if (user != null && "ADMIN".equalsIgnoreCase(user.getRole())) {
                dispose();
                new DashboardFrame(user);   // Admin Dashboard
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Invalid Admin Credentials",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }

        if (e.getSource() == btnBack) {
            dispose();
            new LoginFrame(); 
        }
    }
}