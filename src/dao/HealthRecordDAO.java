package dao;

import model.HealthRecord;
import model.Prescription;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HealthRecordDAO {

    public boolean addHealthRecord(HealthRecord record) {
        String sql = "INSERT INTO health_records (user_id, record_date, weight, blood_pressure, blood_sugar, notes) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, record.getUserId());
            ps.setDate(2, record.getRecordDate());
            ps.setDouble(3, record.getWeight());
            ps.setString(4, record.getBloodPressure());
            ps.setDouble(5, record.getBloodSugar());
            ps.setString(6, record.getNotes());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<HealthRecord> getRecordsByUser(int userId) {
        List<HealthRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM health_records WHERE user_id = ? ORDER BY record_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                records.add(new HealthRecord(
                    rs.getInt("record_id"), rs.getInt("user_id"),
                    rs.getDate("record_date"), rs.getDouble("weight"),
                    rs.getString("blood_pressure"), rs.getDouble("blood_sugar"),
                    rs.getString("notes")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    public boolean updateHealthRecord(HealthRecord record) {
        String sql = "UPDATE health_records SET record_date = ?, weight = ?, blood_pressure = ?, blood_sugar = ?, notes = ? WHERE record_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, record.getRecordDate());
            ps.setDouble(2, record.getWeight());
            ps.setString(3, record.getBloodPressure());
            ps.setDouble(4, record.getBloodSugar());
            ps.setString(5, record.getNotes());
            ps.setInt(6, record.getRecordId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteHealthRecord(int recordId) {
        String sql = "DELETE FROM health_records WHERE record_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, recordId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int countRecordsByUser(int userId) {
        String sql = "SELECT COUNT(*) FROM health_records WHERE user_id = ?";
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

    public boolean addPrescription(Prescription prescription) {
        String sql = "INSERT INTO prescriptions (user_id, doctor_name, prescription_date, file_path) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, prescription.getUserId());
            ps.setString(2, prescription.getDoctorName());
            ps.setDate(3, prescription.getPrescriptionDate());
            ps.setString(4, prescription.getFilePath());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Prescription> getPrescriptionsByUser(int userId) {
        List<Prescription> prescriptions = new ArrayList<>();
        String sql = "SELECT * FROM prescriptions WHERE user_id = ? ORDER BY prescription_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                prescriptions.add(new Prescription(
                    rs.getInt("prescription_id"), rs.getInt("user_id"),
                    rs.getString("doctor_name"), rs.getDate("prescription_date"),
                    rs.getString("file_path")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prescriptions;
    }

    public boolean deletePrescription(int prescriptionId) {
        String sql = "DELETE FROM prescriptions WHERE prescription_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, prescriptionId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int countPrescriptionsByUser(int userId) {
        String sql = "SELECT COUNT(*) FROM prescriptions WHERE user_id = ?";
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
