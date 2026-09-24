package dao;

import model.Medicine;
import model.Reminder;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicineDAO {

    public boolean addMedicine(Medicine medicine) {
        String sql = "INSERT INTO medicines (user_id, medicine_name, dosage, frequency, start_date, end_date) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, medicine.getUserId());
            ps.setString(2, medicine.getMedicineName());
            ps.setString(3, medicine.getDosage());
            ps.setString(4, medicine.getFrequency());
            ps.setDate(5, medicine.getStartDate());
            ps.setDate(6, medicine.getEndDate());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Medicine> getMedicinesByUser(int userId) {
        List<Medicine> medicines = new ArrayList<>();
        String sql = "SELECT * FROM medicines WHERE user_id = ? ORDER BY start_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                medicines.add(new Medicine(
                    rs.getInt("medicine_id"), rs.getInt("user_id"),
                    rs.getString("medicine_name"), rs.getString("dosage"),
                    rs.getString("frequency"), rs.getDate("start_date"), rs.getDate("end_date")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medicines;
    }

    public boolean updateMedicine(Medicine medicine) {
        String sql = "UPDATE medicines SET medicine_name = ?, dosage = ?, frequency = ?, start_date = ?, end_date = ? WHERE medicine_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, medicine.getMedicineName());
            ps.setString(2, medicine.getDosage());
            ps.setString(3, medicine.getFrequency());
            ps.setDate(4, medicine.getStartDate());
            ps.setDate(5, medicine.getEndDate());
            ps.setInt(6, medicine.getMedicineId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteMedicine(int medicineId) {
        String sql = "DELETE FROM medicines WHERE medicine_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, medicineId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Medicine getMedicineById(int medicineId) {
        String sql = "SELECT * FROM medicines WHERE medicine_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, medicineId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Medicine(
                    rs.getInt("medicine_id"), rs.getInt("user_id"),
                    rs.getString("medicine_name"), rs.getString("dosage"),
                    rs.getString("frequency"), rs.getDate("start_date"), rs.getDate("end_date")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addReminder(Reminder reminder) {
        String sql = "INSERT INTO reminders (medicine_id, reminder_time, status) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reminder.getMedicineId());
            ps.setTime(2, reminder.getReminderTime());
            ps.setString(3, reminder.getStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Reminder> getRemindersByMedicine(int medicineId) {
        List<Reminder> reminders = new ArrayList<>();
        String sql = "SELECT * FROM reminders WHERE medicine_id = ? ORDER BY reminder_time";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, medicineId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                reminders.add(new Reminder(
                    rs.getInt("reminder_id"), rs.getInt("medicine_id"),
                    rs.getTime("reminder_time"), rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reminders;
    }

    public List<Reminder> getPendingRemindersByUser(int userId) {
        List<Reminder> reminders = new ArrayList<>();
        String sql = "SELECT r.* FROM reminders r JOIN medicines m ON r.medicine_id = m.medicine_id WHERE m.user_id = ? AND r.status = 'PENDING' ORDER BY r.reminder_time";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                reminders.add(new Reminder(
                    rs.getInt("reminder_id"), rs.getInt("medicine_id"),
                    rs.getTime("reminder_time"), rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reminders;
    }

    public boolean updateReminderStatus(int reminderId, String status) {
        String sql = "UPDATE reminders SET status = ? WHERE reminder_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, reminderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteReminder(int reminderId) {
        String sql = "DELETE FROM reminders WHERE reminder_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reminderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int countMedicinesByUser(int userId) {
        String sql = "SELECT COUNT(*) FROM medicines WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int countPendingReminders(int userId) {
        String sql = "SELECT COUNT(*) FROM reminders r JOIN medicines m ON r.medicine_id = m.medicine_id WHERE m.user_id = ? AND r.status = 'PENDING'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
