package model;

import java.sql.Time;

public class Reminder {
    private int reminderId;
    private int medicineId;
    private Time reminderTime;
    private String status;

    public Reminder() {}

    public Reminder(int medicineId, Time reminderTime, String status) {
        this.medicineId = medicineId;
        this.reminderTime = reminderTime;
        this.status = status;
    }

    public Reminder(int reminderId, int medicineId, Time reminderTime, String status) {
        this.reminderId = reminderId;
        this.medicineId = medicineId;
        this.reminderTime = reminderTime;
        this.status = status;
    }

    public int getReminderId() { return reminderId; }
    public void setReminderId(int reminderId) { this.reminderId = reminderId; }

    public int getMedicineId() { return medicineId; }
    public void setMedicineId(int medicineId) { this.medicineId = medicineId; }

    public Time getReminderTime() { return reminderTime; }
    public void setReminderTime(Time reminderTime) { this.reminderTime = reminderTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Reminder{time=" + reminderTime + ", status='" + status + "'}";
    }
}
