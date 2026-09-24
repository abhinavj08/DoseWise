package model;

import java.sql.Date;

public class Medicine {
    private int medicineId;
    private int userId;
    private String medicineName;
    private String dosage;
    private String frequency;
    private Date startDate;
    private Date endDate;

    public Medicine() {}

    public Medicine(int userId, String medicineName, String dosage, String frequency, Date startDate, Date endDate) {
        this.userId = userId;
        this.medicineName = medicineName;
        this.dosage = dosage;
        this.frequency = frequency;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Medicine(int medicineId, int userId, String medicineName, String dosage, String frequency, Date startDate, Date endDate) {
        this.medicineId = medicineId;
        this.userId = userId;
        this.medicineName = medicineName;
        this.dosage = dosage;
        this.frequency = frequency;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getMedicineId() { return medicineId; }
    public void setMedicineId(int medicineId) { this.medicineId = medicineId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getMedicineName() { return medicineName; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }

    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    @Override
    public String toString() {
        return medicineName + " (" + dosage + ")";
    }
}
