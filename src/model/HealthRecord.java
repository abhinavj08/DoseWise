package model;

import java.sql.Date;

public class HealthRecord {
    private int recordId;
    private int userId;
    private Date recordDate;
    private double weight;
    private String bloodPressure;
    private double bloodSugar;
    private String notes;

    public HealthRecord() {}

    public HealthRecord(int userId, Date recordDate, double weight, String bloodPressure, double bloodSugar, String notes) {
        this.userId = userId;
        this.recordDate = recordDate;
        this.weight = weight;
        this.bloodPressure = bloodPressure;
        this.bloodSugar = bloodSugar;
        this.notes = notes;
    }

    public HealthRecord(int recordId, int userId, Date recordDate, double weight, String bloodPressure, double bloodSugar, String notes) {
        this.recordId = recordId;
        this.userId = userId;
        this.recordDate = recordDate;
        this.weight = weight;
        this.bloodPressure = bloodPressure;
        this.bloodSugar = bloodSugar;
        this.notes = notes;
    }

    public int getRecordId() { return recordId; }
    public void setRecordId(int recordId) { this.recordId = recordId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public Date getRecordDate() { return recordDate; }
    public void setRecordDate(Date recordDate) { this.recordDate = recordDate; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public String getBloodPressure() { return bloodPressure; }
    public void setBloodPressure(String bloodPressure) { this.bloodPressure = bloodPressure; }

    public double getBloodSugar() { return bloodSugar; }
    public void setBloodSugar(double bloodSugar) { this.bloodSugar = bloodSugar; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public String toString() {
        return "HealthRecord{date=" + recordDate + ", weight=" + weight + ", BP=" + bloodPressure + "}";
    }
}
