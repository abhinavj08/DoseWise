package model;

import java.sql.Date;

public class Prescription {
    private int prescriptionId;
    private int userId;
    private String doctorName;
    private Date prescriptionDate;
    private String filePath;

    public Prescription() {}

    public Prescription(int userId, String doctorName, Date prescriptionDate, String filePath) {
        this.userId = userId;
        this.doctorName = doctorName;
        this.prescriptionDate = prescriptionDate;
        this.filePath = filePath;
    }

    public Prescription(int prescriptionId, int userId, String doctorName, Date prescriptionDate, String filePath) {
        this.prescriptionId = prescriptionId;
        this.userId = userId;
        this.doctorName = doctorName;
        this.prescriptionDate = prescriptionDate;
        this.filePath = filePath;
    }

    public int getPrescriptionId() { return prescriptionId; }
    public void setPrescriptionId(int prescriptionId) { this.prescriptionId = prescriptionId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public Date getPrescriptionDate() { return prescriptionDate; }
    public void setPrescriptionDate(Date prescriptionDate) { this.prescriptionDate = prescriptionDate; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    @Override
    public String toString() {
        return "Dr. " + doctorName + " (" + prescriptionDate + ")";
    }
}
