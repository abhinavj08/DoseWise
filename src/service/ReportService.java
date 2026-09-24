package service;

import dao.HealthRecordDAO;
import model.HealthRecord;
import model.Prescription;
import java.util.List;

public class ReportService {

    private HealthRecordDAO healthRecordDAO;

    public ReportService() {
        this.healthRecordDAO = new HealthRecordDAO();
    }

    public boolean addHealthRecord(HealthRecord record) {
        if (record.getRecordDate() == null) {
            return false;
        }
        return healthRecordDAO.addHealthRecord(record);
    }

    public List<HealthRecord> getHealthRecords(int userId) {
        return healthRecordDAO.getRecordsByUser(userId);
    }

    public boolean updateHealthRecord(HealthRecord record) {
        return healthRecordDAO.updateHealthRecord(record);
    }

    public boolean deleteHealthRecord(int recordId) {
        return healthRecordDAO.deleteHealthRecord(recordId);
    }

    public int getRecordCount(int userId) {
        return healthRecordDAO.countRecordsByUser(userId);
    }

    public boolean addPrescription(Prescription prescription) {
        if (prescription.getDoctorName() == null || prescription.getDoctorName().trim().isEmpty()) {
            return false;
        }
        return healthRecordDAO.addPrescription(prescription);
    }

    public List<Prescription> getPrescriptions(int userId) {
        return healthRecordDAO.getPrescriptionsByUser(userId);
    }

    public boolean deletePrescription(int prescriptionId) {
        return healthRecordDAO.deletePrescription(prescriptionId);
    }

    public int getPrescriptionCount(int userId) {
        return healthRecordDAO.countPrescriptionsByUser(userId);
    }

    public String generateHealthSummary(int userId) {
        List<HealthRecord> records = healthRecordDAO.getRecordsByUser(userId);
        if (records.isEmpty()) {
            return "No health records found.";
        }

        StringBuilder summary = new StringBuilder();
        summary.append("========== HEALTH SUMMARY ==========").append("\n");
        summary.append("Total Records: ").append(records.size()).append("\n\n");

        double totalWeight = 0;
        double totalSugar = 0;
        int weightCount = 0;
        int sugarCount = 0;

        for (HealthRecord record : records) {
            if (record.getWeight() > 0) {
                totalWeight += record.getWeight();
                weightCount++;
            }
            if (record.getBloodSugar() > 0) {
                totalSugar += record.getBloodSugar();
                sugarCount++;
            }
        }

        if (weightCount > 0) {
            summary.append("Average Weight: ").append(String.format("%.2f", totalWeight / weightCount)).append(" kg\n");
        }
        if (sugarCount > 0) {
            summary.append("Average Blood Sugar: ").append(String.format("%.2f", totalSugar / sugarCount)).append(" mg/dL\n");
        }

        HealthRecord latest = records.get(0);
        summary.append("\n--- Latest Record (").append(latest.getRecordDate()).append(") ---\n");
        summary.append("Weight: ").append(latest.getWeight()).append(" kg\n");
        summary.append("Blood Pressure: ").append(latest.getBloodPressure()).append("\n");
        summary.append("Blood Sugar: ").append(latest.getBloodSugar()).append(" mg/dL\n");
        if (latest.getNotes() != null && !latest.getNotes().isEmpty()) {
            summary.append("Notes: ").append(latest.getNotes()).append("\n");
        }
        summary.append("====================================").append("\n");

        return summary.toString();
    }
}
