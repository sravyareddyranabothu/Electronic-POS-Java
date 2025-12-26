package electronicspos.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HomeFrame extends JFrame {

	private JButton loginBtn;

	public HomeFrame() {

		setTitle("ElectronicsPOS");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		JPanel navBar = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				GradientPaint gp = new GradientPaint(0, 0, new Color(34, 45, 65), getWidth(), 0,
						new Color(60, 75, 100));
				g2.setPaint(gp);
				g2.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		navBar.setLayout(new FlowLayout(FlowLayout.LEFT, 30, 15));
		JLabel logo = new JLabel("SR Electronics");
		logo.setForeground(Color.WHITE);
		logo.setFont(new Font("Segoe UI", Font.BOLD, 28));
		navBar.add(logo);
		add(navBar, BorderLayout.NORTH);

		JPanel heroPanel = new JPanel(new GridBagLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				GradientPaint gp = new GradientPaint(0, 0, new Color(245, 250, 255), getWidth(), getHeight(),
						new Color(180, 210, 245));
				g2.setPaint(gp);
				g2.fillRect(0, 0, getWidth(), getHeight());
			}
		};

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.CENTER;
		heroPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		JPanel centerPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(new Color(255, 255, 255, 230));
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
			}
		};
		centerPanel.setOpaque(false);
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		centerPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

		JLabel heading = new JLabel("Welcome to SR Electronics POS");
		heading.setFont(new Font("Segoe UI", Font.BOLD, 40));
		heading.setForeground(new Color(20, 30, 60));
		heading.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel subText = new JLabel(
				"<html>Manage billing, inventory, customers,<br>and reports with one powerful platform.</html>");
		subText.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		subText.setForeground(Color.DARK_GRAY);
		subText.setAlignmentX(Component.CENTER_ALIGNMENT);

		loginBtn = new JButton("Login POS");
		styleMainButton(loginBtn);
		addHoverEffect(loginBtn, new Color(50, 160, 80), new Color(70, 180, 90));
		loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		loginBtn.setMaximumSize(new Dimension(160, 45));
		loginBtn.setPreferredSize(new Dimension(160, 45));

		centerPanel.add(heading);
		centerPanel.add(Box.createVerticalStrut(20));
		centerPanel.add(subText);
		centerPanel.add(Box.createVerticalStrut(50));
		centerPanel.add(loginBtn);

		heroPanel.add(centerPanel, gbc);

		loginBtn.addActionListener(e -> {
			new LoginFrame().setVisible(true);
			dispose();
		});

		add(heroPanel, BorderLayout.CENTER);
		setVisible(true);
	}

	private void styleMainButton(JButton btn) {
		btn.setBackground(new Color(70, 180, 90));
		btn.setForeground(Color.WHITE);
		btn.setFont(new Font("Segoe UI", Font.BOLD, 20));
		btn.setFocusPainted(false);
		btn.setBorder(BorderFactory.createEmptyBorder(15, 50, 15, 50));
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btn.setOpaque(true);
		btn.setBorder(BorderFactory.createLineBorder(new Color(50, 160, 80), 2));
		btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
		btn.setRolloverEnabled(true);
	}

	private void addHoverEffect(JButton btn, Color hoverBg, Color normalBg) {
		btn.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent e) {
				btn.setBackground(hoverBg);
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent e) {
				btn.setBackground(normalBg);
			}
		});
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(HomeFrame::new);
	}
}
