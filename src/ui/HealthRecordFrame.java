package ui;

import model.HealthRecord;
import model.Prescription;
import model.User;
import service.ReportService;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.util.List;

public class HealthRecordFrame extends JFrame {

    private User currentUser;
    private ReportService reportService;
    private JTable healthTable;
    private DefaultTableModel healthTableModel;
    private JTable prescriptionTable;
    private DefaultTableModel prescriptionTableModel;
    private JTextField dateField, weightField, bpField, sugarField;
    private JTextArea notesArea;
    private JTextField doctorField, prescDateField, filePathField;

    public HealthRecordFrame(User user) {
        this.currentUser = user;
        this.reportService = new ReportService();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("MediTrack - Health Records & Prescriptions");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 600));

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(236, 240, 241));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        mainPanel.add(createHeader(), BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.addTab("Health Records", createHealthRecordPanel());
        tabbedPane.addTab("Prescriptions", createPrescriptionPanel());

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        add(mainPanel);
        loadHealthRecords();
        loadPrescriptions();
        setVisible(true);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(46, 204, 113));
        header.setPreferredSize(new Dimension(1000, 50));
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("Health Records & Prescriptions");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        header.add(titleLabel, BorderLayout.WEST);

        JButton backButton = new JButton("Back to Dashboard");
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(39, 174, 96));
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> dispose());
        header.add(backButton, BorderLayout.EAST);

        return header;
    }

    private JPanel createHealthRecordPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(46, 204, 113), 1, true),
                "Add Health Record", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14), new Color(44, 62, 80)
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createLabel("Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        dateField = createSmallTextField();
        formPanel.add(dateField, gbc);
        gbc.gridx = 2;
        formPanel.add(createLabel("Weight (kg):"), gbc);
        gbc.gridx = 3;
        weightField = createSmallTextField();
        formPanel.add(weightField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(createLabel("Blood Pressure:"), gbc);
        gbc.gridx = 1;
        bpField = createSmallTextField();
        formPanel.add(bpField, gbc);
        gbc.gridx = 2;
        formPanel.add(createLabel("Blood Sugar (mg/dL):"), gbc);
        gbc.gridx = 3;
        sugarField = createSmallTextField();
        formPanel.add(sugarField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(createLabel("Notes:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        notesArea = new JTextArea(2, 20);
        notesArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        notesArea.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        notesArea.setLineWrap(true);
        formPanel.add(new JScrollPane(notesArea), gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 4;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnPanel.setOpaque(false);
        JButton addBtn = createButton("Add Record", new Color(46, 204, 113));
        addBtn.addActionListener(e -> addHealthRecord());
        JButton updateBtn = createButton("Update", new Color(52, 152, 219));
        updateBtn.addActionListener(e -> updateHealthRecord());
        JButton deleteBtn = createButton("Delete", new Color(231, 76, 60));
        deleteBtn.addActionListener(e -> deleteHealthRecord());
        JButton clearBtn = createButton("Clear", new Color(149, 165, 166));
        clearBtn.addActionListener(e -> clearHealthFields());
        btnPanel.add(addBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(clearBtn);
        formPanel.add(btnPanel, gbc);

        panel.add(formPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Date", "Weight (kg)", "Blood Pressure", "Blood Sugar", "Notes"};
        healthTableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        healthTable = new JTable(healthTableModel);
        styleTable(healthTable, new Color(46, 204, 113));
        healthTable.getColumnModel().getColumn(0).setMaxWidth(0);
        healthTable.getColumnModel().getColumn(0).setMinWidth(0);
        healthTable.getColumnModel().getColumn(0).setPreferredWidth(0);
        healthTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = healthTable.getSelectedRow();
                if (row >= 0) {
                    dateField.setText(healthTableModel.getValueAt(row, 1).toString());
                    Object w = healthTableModel.getValueAt(row, 2);
                    weightField.setText(w != null ? w.toString() : "");
                    Object bp = healthTableModel.getValueAt(row, 3);
                    bpField.setText(bp != null ? bp.toString() : "");
                    Object s = healthTableModel.getValueAt(row, 4);
                    sugarField.setText(s != null ? s.toString() : "");
                    Object n = healthTableModel.getValueAt(row, 5);
                    notesArea.setText(n != null ? n.toString() : "");
                }
            }
        });
        panel.add(new JScrollPane(healthTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createPrescriptionPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(155, 89, 182), 1, true),
                "Add Prescription", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14), new Color(44, 62, 80)
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createLabel("Doctor Name:"), gbc);
        gbc.gridx = 1;
        doctorField = createSmallTextField();
        formPanel.add(doctorField, gbc);
        gbc.gridx = 2;
        formPanel.add(createLabel("Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 3;
        prescDateField = createSmallTextField();
        formPanel.add(prescDateField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(createLabel("File Path:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        filePathField = createSmallTextField();
        formPanel.add(filePathField, gbc);
        gbc.gridx = 3; gbc.gridwidth = 1;
        JButton browseBtn = createButton("Browse", new Color(52, 152, 219));
        browseBtn.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
                filePathField.setText(fc.getSelectedFile().getAbsolutePath());
        });
        formPanel.add(browseBtn, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 4;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnPanel.setOpaque(false);
        JButton addBtn = createButton("Add Prescription", new Color(155, 89, 182));
        addBtn.addActionListener(e -> addPrescription());
        JButton deleteBtn = createButton("Delete", new Color(231, 76, 60));
        deleteBtn.addActionListener(e -> deletePrescription());
        JButton clearBtn = createButton("Clear", new Color(149, 165, 166));
        clearBtn.addActionListener(e -> clearPrescriptionFields());
        btnPanel.add(addBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(clearBtn);
        formPanel.add(btnPanel, gbc);

        panel.add(formPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Doctor Name", "Prescription Date", "File Path"};
        prescriptionTableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        prescriptionTable = new JTable(prescriptionTableModel);
        styleTable(prescriptionTable, new Color(155, 89, 182));
        prescriptionTable.getColumnModel().getColumn(0).setMaxWidth(0);
        prescriptionTable.getColumnModel().getColumn(0).setMinWidth(0);
        prescriptionTable.getColumnModel().getColumn(0).setPreferredWidth(0);
        prescriptionTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = prescriptionTable.getSelectedRow();
                if (row >= 0) {
                    doctorField.setText(prescriptionTableModel.getValueAt(row, 1).toString());
                    prescDateField.setText(prescriptionTableModel.getValueAt(row, 2).toString());
                    Object fp = prescriptionTableModel.getValueAt(row, 3);
                    filePathField.setText(fp != null ? fp.toString() : "");
                }
            }
        });
        panel.add(new JScrollPane(prescriptionTable), BorderLayout.CENTER);
        return panel;
    }

    private void loadHealthRecords() {
        healthTableModel.setRowCount(0);
        List<HealthRecord> records = reportService.getHealthRecords(currentUser.getUserId());
        for (HealthRecord r : records) {
            healthTableModel.addRow(new Object[]{
                r.getRecordId(), r.getRecordDate(), r.getWeight(),
                r.getBloodPressure(), r.getBloodSugar(), r.getNotes()
            });
        }
    }

    private void addHealthRecord() {
        try {
            String dateStr = dateField.getText().trim();
            if (dateStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Date is required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Date date = Date.valueOf(dateStr);
            double weight = weightField.getText().trim().isEmpty() ? 0 : Double.parseDouble(weightField.getText().trim());
            String bp = bpField.getText().trim();
            double sugar = sugarField.getText().trim().isEmpty() ? 0 : Double.parseDouble(sugarField.getText().trim());
            String notes = notesArea.getText().trim();
            HealthRecord record = new HealthRecord(currentUser.getUserId(), date, weight, bp, sugar, notes);
            if (reportService.addHealthRecord(record)) {
                JOptionPane.showMessageDialog(this, "Health record added!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadHealthRecords();
                clearHealthFields();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateHealthRecord() {
        int row = healthTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a record.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int id = Integer.parseInt(healthTableModel.getValueAt(row, 0).toString());
            Date date = Date.valueOf(dateField.getText().trim());
            double weight = weightField.getText().trim().isEmpty() ? 0 : Double.parseDouble(weightField.getText().trim());
            String bp = bpField.getText().trim();
            double sugar = sugarField.getText().trim().isEmpty() ? 0 : Double.parseDouble(sugarField.getText().trim());
            String notes = notesArea.getText().trim();
            HealthRecord record = new HealthRecord(id, currentUser.getUserId(), date, weight, bp, sugar, notes);
            if (reportService.updateHealthRecord(record)) {
                JOptionPane.showMessageDialog(this, "Record updated!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadHealthRecords();
                clearHealthFields();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteHealthRecord() {
        int row = healthTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a record.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Delete?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(healthTableModel.getValueAt(row, 0).toString());
            if (reportService.deleteHealthRecord(id)) {
                JOptionPane.showMessageDialog(this, "Deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadHealthRecords();
                clearHealthFields();
            }
        }
    }

    private void clearHealthFields() {
        dateField.setText(""); weightField.setText(""); bpField.setText("");
        sugarField.setText(""); notesArea.setText(""); healthTable.clearSelection();
    }

    private void loadPrescriptions() {
        prescriptionTableModel.setRowCount(0);
        List<Prescription> list = reportService.getPrescriptions(currentUser.getUserId());
        for (Prescription p : list) {
            prescriptionTableModel.addRow(new Object[]{
                p.getPrescriptionId(), p.getDoctorName(), p.getPrescriptionDate(), p.getFilePath()
            });
        }
    }

    private void addPrescription() {
        try {
            String doctor = doctorField.getText().trim();
            String dateStr = prescDateField.getText().trim();
            if (doctor.isEmpty() || dateStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Doctor and date required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Date date = Date.valueOf(dateStr);
            Prescription p = new Prescription(currentUser.getUserId(), doctor, date, filePathField.getText().trim());
            if (reportService.addPrescription(p)) {
                JOptionPane.showMessageDialog(this, "Prescription added!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadPrescriptions();
                clearPrescriptionFields();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid date format.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletePrescription() {
        int row = prescriptionTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a prescription.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Delete?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(prescriptionTableModel.getValueAt(row, 0).toString());
            if (reportService.deletePrescription(id)) {
                JOptionPane.showMessageDialog(this, "Deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadPrescriptions();
                clearPrescriptionFields();
            }
        }
    }

    private void clearPrescriptionFields() {
        doctorField.setText(""); prescDateField.setText("");
        filePathField.setText(""); prescriptionTable.clearSelection();
    }

    private void styleTable(JTable table, Color headerColor) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(28);
        table.setSelectionBackground(headerColor);
        table.setSelectionForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(headerColor);
        table.getTableHeader().setForeground(Color.WHITE);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setForeground(new Color(44, 62, 80));
        return label;
    }

    private JTextField createSmallTextField() {
        JTextField field = new JTextField(15);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
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
