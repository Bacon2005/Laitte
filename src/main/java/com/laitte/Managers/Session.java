package com.laitte.Managers;

public class Session {
    private static String username; // store the logged-in username
    private static boolean manager;

    // Set the username after login
    public static void setUsername(String user) {
        username = user;
    }

    // Get the username in any scene
    public static String getUsername() {
        return username;
    }

    public static void setManager(boolean isManager) {
        manager = isManager;
    }

    public static boolean getManager() {
        return manager;
    }
}
