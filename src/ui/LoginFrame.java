package ui;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {

    private UserDAO userDAO;
    private JTextField loginEmailField;
    private JPasswordField loginPasswordField;
    private JTextField regNameField;
    private JTextField regEmailField;
    private JPasswordField regPasswordField;
    private JPasswordField regConfirmPasswordField;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public LoginFrame() {
        userDAO = new UserDAO();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("MediTrack - Login");
        setSize(500, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 248, 255));

        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false);
        cardPanel.add(createLoginPanel(), "LOGIN");
        cardPanel.add(createRegisterPanel(), "REGISTER");
        mainPanel.add(cardPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(41, 128, 185));
        panel.setPreferredSize(new Dimension(500, 100));
        panel.setLayout(new GridBagLayout());

        JLabel titleLabel = new JLabel("MediTrack");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Medicine Reminder & Health Record Organizer");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(200, 230, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(titleLabel, gbc);
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 0, 0, 0);
        panel.add(subtitleLabel, gbc);

        return panel;
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 30, 8, 30);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel loginTitle = new JLabel("Sign In", SwingConstants.CENTER);
        loginTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        loginTitle.setForeground(new Color(44, 62, 80));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(loginTitle, gbc);

        gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(createLabel("Email:"), gbc);
        gbc.gridy = 2;
        loginEmailField = createTextField();
        panel.add(loginEmailField, gbc);

        gbc.gridy = 3;
        panel.add(createLabel("Password:"), gbc);
        gbc.gridy = 4;
        loginPasswordField = createPasswordField();
        panel.add(loginPasswordField, gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(20, 30, 8, 30);
        JButton loginButton = createButton("Login", new Color(41, 128, 185));
        loginButton.addActionListener(e -> handleLogin());
        panel.add(loginButton, gbc);

        gbc.gridy = 6;
        gbc.insets = new Insets(8, 30, 8, 30);
        JButton switchToRegister = createLinkButton("Don't have an account? Sign Up");
        switchToRegister.addActionListener(e -> cardLayout.show(cardPanel, "REGISTER"));
        panel.add(switchToRegister, gbc);

        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 30, 6, 30);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel regTitle = new JLabel("Create Account", SwingConstants.CENTER);
        regTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        regTitle.setForeground(new Color(44, 62, 80));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(regTitle, gbc);

        gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(createLabel("Full Name:"), gbc);
        gbc.gridy = 2;
        regNameField = createTextField();
        panel.add(regNameField, gbc);

        gbc.gridy = 3;
        panel.add(createLabel("Email:"), gbc);
        gbc.gridy = 4;
        regEmailField = createTextField();
        panel.add(regEmailField, gbc);

        gbc.gridy = 5;
        panel.add(createLabel("Password:"), gbc);
        gbc.gridy = 6;
        regPasswordField = createPasswordField();
        panel.add(regPasswordField, gbc);

        gbc.gridy = 7;
        panel.add(createLabel("Confirm Password:"), gbc);
        gbc.gridy = 8;
        regConfirmPasswordField = createPasswordField();
        panel.add(regConfirmPasswordField, gbc);

        gbc.gridy = 9;
        gbc.insets = new Insets(15, 30, 6, 30);
        JButton registerButton = createButton("Sign Up", new Color(39, 174, 96));
        registerButton.addActionListener(e -> handleRegister());
        panel.add(registerButton, gbc);

        gbc.gridy = 10;
        gbc.insets = new Insets(6, 30, 6, 30);
        JButton switchToLogin = createLinkButton("Already have an account? Sign In");
        switchToLogin.addActionListener(e -> cardLayout.show(cardPanel, "LOGIN"));
        panel.add(switchToLogin, gbc);

        return panel;
    }

    private void handleLogin() {
        String email = loginEmailField.getText().trim();
        String password = new String(loginPasswordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = userDAO.loginUser(email, password);
        if (user != null) {
            service.SessionManager.saveSession(user.getUserId());
            JOptionPane.showMessageDialog(this, "Welcome back, " + user.getName() + "!", "Login Successful", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new DashboardFrame(user);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRegister() {
        String name = regNameField.getText().trim();
        String email = regEmailField.getText().trim();
        String password = new String(regPasswordField.getPassword());
        String confirmPassword = new String(regConfirmPasswordField.getPassword());

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (userDAO.isEmailExists(email)) {
            JOptionPane.showMessageDialog(this, "This email is already registered.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = new User(name, email, password);
        if (userDAO.registerUser(user)) {
            JOptionPane.showMessageDialog(this, "Registration successful! Please login.", "Success", JOptionPane.INFORMATION_MESSAGE);
            cardLayout.show(cardPanel, "LOGIN");
            regNameField.setText("");
            regEmailField.setText("");
            regPasswordField.setText("");
            regConfirmPasswordField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(new Color(44, 62, 80));
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(300, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField(20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(300, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(300, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { button.setBackground(bgColor.darker()); }
            public void mouseExited(MouseEvent e) { button.setBackground(bgColor); }
        });
        return button;
    }

    private JButton createLinkButton(String text) {
        JButton button = new JButton("<html><u>" + text + "</u></html>");
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setForeground(new Color(41, 128, 185));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
}
