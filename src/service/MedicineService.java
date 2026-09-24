package service;

import dao.MedicineDAO;
import model.Medicine;
import model.Reminder;
import java.util.List;

public class MedicineService {

    private MedicineDAO medicineDAO;

    public MedicineService() {
        this.medicineDAO = new MedicineDAO();
    }

    public boolean addMedicine(Medicine medicine) {
        if (medicine.getMedicineName() == null || medicine.getMedicineName().trim().isEmpty()) {
            return false;
        }
        if (medicine.getDosage() == null || medicine.getDosage().trim().isEmpty()) {
            return false;
        }
        if (medicine.getStartDate() == null) {
            return false;
        }
        return medicineDAO.addMedicine(medicine);
    }

    public List<Medicine> getMedicinesByUser(int userId) {
        return medicineDAO.getMedicinesByUser(userId);
    }

    public boolean updateMedicine(Medicine medicine) {
        return medicineDAO.updateMedicine(medicine);
    }

    public boolean deleteMedicine(int medicineId) {
        return medicineDAO.deleteMedicine(medicineId);
    }

    public Medicine getMedicineById(int medicineId) {
        return medicineDAO.getMedicineById(medicineId);
    }

    public boolean addReminder(Reminder reminder) {
        return medicineDAO.addReminder(reminder);
    }

    public List<Reminder> getRemindersByMedicine(int medicineId) {
        return medicineDAO.getRemindersByMedicine(medicineId);
    }

    public boolean deleteReminder(int reminderId) {
        return medicineDAO.deleteReminder(reminderId);
    }

    public int getMedicineCount(int userId) {
        return medicineDAO.countMedicinesByUser(userId);
    }

    public int getPendingReminderCount(int userId) {
        return medicineDAO.countPendingReminders(userId);
    }
}
