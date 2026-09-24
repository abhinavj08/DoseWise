package service;

import dao.MedicineDAO;
import model.Medicine;
import model.Reminder;

import javax.swing.*;
import java.awt.*;
import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ReminderService {

    private MedicineDAO medicineDAO;
    private Timer timer;

    public ReminderService() {
        this.medicineDAO = new MedicineDAO();
    }

    public void startReminderChecker(int userId) {
        timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkReminders(userId);
            }
        }, 0, 60000);
    }

    public void stopReminderChecker() {
        if (timer != null) {
            timer.cancel();
        }
    }

    private void checkReminders(int userId) {
        List<Reminder> pendingReminders = medicineDAO.getPendingRemindersByUser(userId);
        LocalTime now = LocalTime.now();

        for (Reminder reminder : pendingReminders) {
            LocalTime reminderTime = reminder.getReminderTime().toLocalTime();
            if (reminderTime.getHour() == now.getHour() && reminderTime.getMinute() == now.getMinute()) {
                Medicine medicine = medicineDAO.getMedicineById(reminder.getMedicineId());
                if (medicine != null) {
                    showReminderNotification(medicine, reminder);
                }
            }
        }
    }

    private void showReminderNotification(Medicine medicine, Reminder reminder) {
        SwingUtilities.invokeLater(() -> {
            Toolkit.getDefaultToolkit().beep();

            int choice = JOptionPane.showConfirmDialog(
                null,
                "Time to take your medicine!\n\n" +
                "Medicine: " + medicine.getMedicineName() + "\n" +
                "Dosage: " + medicine.getDosage() + "\n" +
                "Time: " + reminder.getReminderTime() + "\n\n" +
                "Did you take this medicine?",
                "MediTrack - Medicine Reminder",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE
            );

            if (choice == JOptionPane.YES_OPTION) {
                medicineDAO.updateReminderStatus(reminder.getReminderId(), "TAKEN");
            } else {
                medicineDAO.updateReminderStatus(reminder.getReminderId(), "MISSED");
            }
        });
    }

    public List<Reminder> getPendingReminders(int userId) {
        return medicineDAO.getPendingRemindersByUser(userId);
    }

    public boolean markAsTaken(int reminderId) {
        return medicineDAO.updateReminderStatus(reminderId, "TAKEN");
    }

    public boolean markAsMissed(int reminderId) {
        return medicineDAO.updateReminderStatus(reminderId, "MISSED");
    }
}
