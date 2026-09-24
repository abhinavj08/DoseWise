package service;

import java.io.*;

public class SessionManager {

    private static final String SESSION_FILE = ".session";

    // Save user ID to session file on login
    public static void saveSession(int userId) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SESSION_FILE))) {
            writer.println(userId);
        } catch (IOException e) {
            System.err.println("Could not save session: " + e.getMessage());
        }
    }

    // Retrieve saved user ID if session file exists
    public static int getSavedUserId() {
        File file = new File(SESSION_FILE);
        if (!file.exists()) {
            return -1;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line != null && !line.trim().isEmpty()) {
                return Integer.parseInt(line.trim());
            }
        } catch (Exception e) {
            clearSession();
        }
        return -1;
    }

    // Clear session when user explicitly logs out
    public static void clearSession() {
        File file = new File(SESSION_FILE);
        if (file.exists()) {
            file.delete();
        }
    }
}
