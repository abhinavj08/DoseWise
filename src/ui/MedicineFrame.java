package ui;

import model.Medicine;
import model.Reminder;
import model.User;
import service.MedicineService;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class MedicineFrame extends JFrame {

    private User currentUser;
    private MedicineService medicineService;
    private JTable medicineTable;
    private DefaultTableModel tableModel;
    private JTextField nameField, dosageField, frequencyField;
    private JTextField startDateField, endDateField;
    private JTextField reminderTimeField;

    public MedicineFrame(User user) {
        this.currentUser = user;
        this.medicineService = new MedicineService();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("MediTrack - Manage Medicines");
        setSize(950, 650);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(850, 550));

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(236, 240, 241));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        mainPanel.add(createHeader(), BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createFormPanel(), createTablePanel());
        splitPane.setDividerLocation(320);
        splitPane.setResizeWeight(0.35);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        add(mainPanel);
        loadMedicineData();
        setVisible(true);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(52, 152, 219));
        header.setPreferredSize(new Dimension(950, 50));
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("Medicine Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        header.add(titleLabel, BorderLayout.WEST);

        JButton backButton = new JButton("Back to Dashboard");
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(41, 128, 185));
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> dispose());
        header.add(backButton, BorderLayout.EAST);

        return header;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel formTitle = new JLabel("Add / Edit Medicine");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        formTitle.setForeground(new Color(44, 62, 80));
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(formTitle);
        formPanel.add(Box.createVerticalStrut(15));

        formPanel.add(createLabel("Medicine Name:"));
        nameField = createTextField();
        formPanel.add(nameField);
        formPanel.add(Box.createVerticalStrut(8));

        formPanel.add(createLabel("Dosage (e.g., 500mg):"));
        dosageField = createTextField();
        formPanel.add(dosageField);
        formPanel.add(Box.createVerticalStrut(8));

        formPanel.add(createLabel("Frequency (e.g., Twice Daily):"));
        frequencyField = createTextField();
        formPanel.add(frequencyField);
        formPanel.add(Box.createVerticalStrut(8));

        formPanel.add(createLabel("Start Date (YYYY-MM-DD):"));
        startDateField = createTextField();
        formPanel.add(startDateField);
        formPanel.add(Box.createVerticalStrut(8));

        formPanel.add(createLabel("End Date (YYYY-MM-DD):"));
        endDateField = createTextField();
        formPanel.add(endDateField);
        formPanel.add(Box.createVerticalStrut(8));

        formPanel.add(createLabel("Reminder Time (HH:MM):"));
        reminderTimeField = createTextField();
        formPanel.add(reminderTimeField);
        formPanel.add(Box.createVerticalStrut(15));

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        buttonPanel.setOpaque(false);
        buttonPanel.setMaximumSize(new Dimension(300, 90));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton addButton = createButton("Add", new Color(46, 204, 113));
        addButton.addActionListener(e -> addMedicine());
        JButton updateButton = createButton("Update", new Color(52, 152, 219));
        updateButton.addActionListener(e -> updateMedicine());
        JButton deleteButton = createButton("Delete", new Color(231, 76, 60));
        deleteButton.addActionListener(e -> deleteMedicine());
        JButton clearButton = createButton("Clear", new Color(149, 165, 166));
        clearButton.addActionListener(e -> clearFields());

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        formPanel.add(buttonPanel);

        return formPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout(0, 10));
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel tableTitle = new JLabel("Your Medicines");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tableTitle.setForeground(new Color(44, 62, 80));
        tablePanel.add(tableTitle, BorderLayout.NORTH);

        String[] columns = {"ID", "Medicine Name", "Dosage", "Frequency", "Start Date", "End Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };

        medicineTable = new JTable(tableModel);
        medicineTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        medicineTable.setRowHeight(30);
        medicineTable.setSelectionBackground(new Color(52, 152, 219));
        medicineTable.setSelectionForeground(Color.WHITE);
        medicineTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        medicineTable.getTableHeader().setBackground(new Color(52, 152, 219));
        medicineTable.getTableHeader().setForeground(Color.WHITE);

        medicineTable.getColumnModel().getColumn(0).setMaxWidth(0);
        medicineTable.getColumnModel().getColumn(0).setMinWidth(0);
        medicineTable.getColumnModel().getColumn(0).setPreferredWidth(0);

        medicineTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = medicineTable.getSelectedRow();
                if (row >= 0) {
                    nameField.setText(tableModel.getValueAt(row, 1).toString());
                    dosageField.setText(tableModel.getValueAt(row, 2).toString());
                    frequencyField.setText(tableModel.getValueAt(row, 3).toString());
                    startDateField.setText(tableModel.getValueAt(row, 4).toString());
                    Object endDate = tableModel.getValueAt(row, 5);
                    endDateField.setText(endDate != null ? endDate.toString() : "");
                    int medicineId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                    List<Reminder> reminders = medicineService.getRemindersByMedicine(medicineId);
                    if (!reminders.isEmpty()) {
                        reminderTimeField.setText(reminders.get(0).getReminderTime().toString().substring(0, 5));
                    } else {
                        reminderTimeField.setText("");
                    }
                }
            }
        });

        tablePanel.add(new JScrollPane(medicineTable), BorderLayout.CENTER);
        return tablePanel;
    }

    private void loadMedicineData() {
        tableModel.setRowCount(0);
        List<Medicine> medicines = medicineService.getMedicinesByUser(currentUser.getUserId());
        for (Medicine m : medicines) {
            tableModel.addRow(new Object[]{
                m.getMedicineId(), m.getMedicineName(), m.getDosage(),
                m.getFrequency(), m.getStartDate(), m.getEndDate()
            });
        }
    }

    private void addMedicine() {
        try {
            String name = nameField.getText().trim();
            String dosage = dosageField.getText().trim();
            String frequency = frequencyField.getText().trim();
            String startStr = startDateField.getText().trim();
            String endStr = endDateField.getText().trim();
            String timeStr = reminderTimeField.getText().trim();

            if (name.isEmpty() || dosage.isEmpty() || frequency.isEmpty() || startStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Date startDate = Date.valueOf(startStr);
            Date endDate = endStr.isEmpty() ? null : Date.valueOf(endStr);
            Medicine medicine = new Medicine(currentUser.getUserId(), name, dosage, frequency, startDate, endDate);

            if (medicineService.addMedicine(medicine)) {
                if (!timeStr.isEmpty()) {
                    List<Medicine> medicines = medicineService.getMedicinesByUser(currentUser.getUserId());
                    if (!medicines.isEmpty()) {
                        int newMedicineId = medicines.get(0).getMedicineId();
                        Time reminderTime = Time.valueOf(timeStr + ":00");
                        Reminder reminder = new Reminder(newMedicineId, reminderTime, "PENDING");
                        medicineService.addReminder(reminder);
                    }
                }
                JOptionPane.showMessageDialog(this, "Medicine added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadMedicineData();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add medicine.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Invalid date or time format.\nDate: YYYY-MM-DD, Time: HH:MM", "Format Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateMedicine() {
        int row = medicineTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a medicine to update.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int medicineId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
            Date startDate = Date.valueOf(startDateField.getText().trim());
            String endStr = endDateField.getText().trim();
            Date endDate = endStr.isEmpty() ? null : Date.valueOf(endStr);
            Medicine medicine = new Medicine(medicineId, currentUser.getUserId(), nameField.getText().trim(), dosageField.getText().trim(), frequencyField.getText().trim(), startDate, endDate);
            if (medicineService.updateMedicine(medicine)) {
                JOptionPane.showMessageDialog(this, "Medicine updated!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadMedicineData();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD", "Format Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteMedicine() {
        int row = medicineTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a medicine to delete.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this medicine and its reminders?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            int medicineId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
            if (medicineService.deleteMedicine(medicineId)) {
                JOptionPane.showMessageDialog(this, "Medicine deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadMedicineData();
                clearFields();
            }
        }
    }

    private void clearFields() {
        nameField.setText("");
        dosageField.setText("");
        frequencyField.setText("");
        startDateField.setText("");
        endDateField.setText("");
        reminderTimeField.setText("");
        medicineTable.clearSelection();
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setForeground(new Color(44, 62, 80));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setMaximumSize(new Dimension(300, 30));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1, true),
            BorderFactory.createEmptyBorder(3, 8, 3, 8)
        ));
        return field;
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { button.setBackground(bgColor.darker()); }
            public void mouseExited(MouseEvent e) { button.setBackground(bgColor); }
        });
        return button;
    }
}
