package ui;

import model.User;
import service.MedicineService;
import service.ReminderService;
import service.ReportService;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DashboardFrame extends JFrame {

    private User currentUser;
    private MedicineService medicineService;
    private ReminderService reminderService;
    private ReportService reportService;

    public DashboardFrame(User user) {
        this.currentUser = user;
        this.medicineService = new MedicineService();
        this.reminderService = new ReminderService();
        this.reportService = new ReportService();
        reminderService.startReminderChecker(user.getUserId());
        initializeUI();
    }

    private void initializeUI() {
        setTitle("MediTrack - Dashboard");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 600));

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                reminderService.stopReminderChecker();
            }
        });

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(236, 240, 241));
        mainPanel.add(createNavBar(), BorderLayout.NORTH);
        mainPanel.add(createDashboardContent(), BorderLayout.CENTER);
        mainPanel.add(createStatusBar(), BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createNavBar() {
        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setBackground(new Color(41, 128, 185));
        navBar.setPreferredSize(new Dimension(900, 60));
        navBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("MediTrack Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        navBar.add(titleLabel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        rightPanel.setOpaque(false);

        JLabel userLabel = new JLabel("Welcome, " + currentUser.getName());
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(Color.WHITE);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutButton.setBackground(new Color(231, 76, 60));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(e -> handleLogout());

        rightPanel.add(userLabel);
        rightPanel.add(logoutButton);
        navBar.add(rightPanel, BorderLayout.EAST);

        return navBar;
    }

    private JPanel createDashboardContent() {
        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
        contentPanel.setBackground(new Color(236, 240, 241));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setOpaque(false);

        JLabel welcomeLabel = new JLabel("Hello, " + currentUser.getName() + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(44, 62, 80));

        JLabel dateLabel = new JLabel(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")));
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateLabel.setForeground(new Color(127, 140, 141));

        welcomePanel.add(welcomeLabel, BorderLayout.NORTH);
        welcomePanel.add(dateLabel, BorderLayout.SOUTH);
        contentPanel.add(welcomePanel, BorderLayout.NORTH);

        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        cardsPanel.setOpaque(false);

        int medicineCount = medicineService.getMedicineCount(currentUser.getUserId());
        int reminderCount = medicineService.getPendingReminderCount(currentUser.getUserId());
        int recordCount = reportService.getRecordCount(currentUser.getUserId());
        int prescriptionCount = reportService.getPrescriptionCount(currentUser.getUserId());

        cardsPanel.add(createStatCard("Medicines", String.valueOf(medicineCount), new Color(52, 152, 219)));
        cardsPanel.add(createStatCard("Pending Reminders", String.valueOf(reminderCount), new Color(230, 126, 34)));
        cardsPanel.add(createStatCard("Health Records", String.valueOf(recordCount), new Color(46, 204, 113)));
        cardsPanel.add(createStatCard("Prescriptions", String.valueOf(prescriptionCount), new Color(155, 89, 182)));

        contentPanel.add(cardsPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        buttonPanel.add(createNavButton("Manage Medicines", new Color(52, 152, 219), e -> new MedicineFrame(currentUser)));
        buttonPanel.add(createNavButton("Health Records", new Color(46, 204, 113), e -> new HealthRecordFrame(currentUser)));
        buttonPanel.add(createNavButton("View Health Summary", new Color(155, 89, 182), e -> showHealthSummary()));

        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        return contentPanel;
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(color);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        titleLabel.setForeground(new Color(127, 140, 141));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalStrut(10));
        card.add(valueLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(titleLabel);

        return card;
    }

    private JButton createNavButton(String text, Color bgColor, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(250, 50));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(action);
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { button.setBackground(bgColor.darker()); }
            public void mouseExited(MouseEvent e) { button.setBackground(bgColor); }
        });
        return button;
    }

    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(new Color(44, 62, 80));
        statusBar.setPreferredSize(new Dimension(900, 30));
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        JLabel statusLabel = new JLabel("MediTrack v1.0 | Reminders are active");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusLabel.setForeground(new Color(189, 195, 199));
        statusBar.add(statusLabel, BorderLayout.WEST);

        return statusBar;
    }

    private void showHealthSummary() {
        String summary = reportService.generateHealthSummary(currentUser.getUserId());
        JTextArea textArea = new JTextArea(summary);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        textArea.setEditable(false);
        textArea.setBackground(new Color(44, 62, 80));
        textArea.setForeground(new Color(46, 204, 113));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(450, 350));
        JOptionPane.showMessageDialog(this, scrollPane, "Health Summary", JOptionPane.PLAIN_MESSAGE);
    }

    private void handleLogout() {
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            reminderService.stopReminderChecker();
            dispose();
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        }
    }
}
