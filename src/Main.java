import model.User;
import dao.UserDAO;
import service.SessionManager;
import ui.DashboardFrame;
import ui.LoginFrame;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            // Check if user is already logged in (saved session)
            int savedUserId = SessionManager.getSavedUserId();
            if (savedUserId > 0) {
                UserDAO userDAO = new UserDAO();
                User user = userDAO.getUserById(savedUserId);
                if (user != null) {
                    // Open Dashboard directly without logging out
                    new DashboardFrame(user);
                    return;
                }
            }
            // If no active session, show Login screen
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
