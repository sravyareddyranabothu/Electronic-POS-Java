package electronicspos.ui;

import electronicspos.model.User;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    public DashboardFrame(User user) {

        setTitle("Electronics POS Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);   // ✅ FULLSCREEN
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Background color
        getContentPane().setBackground(Color.WHITE);

        // LOAD PANEL BASED ON ROLE
        if (user.getRole().equalsIgnoreCase("ADMIN")) {
            add(new AdminDashboardPanel(), BorderLayout.CENTER);
        } else {
            add(new CashierDashboardPanel(), BorderLayout.CENTER);
        }

        setVisible(true);
    }
}
