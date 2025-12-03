package com.laitte.Managers;

public class Session {
    private static String username; // store the logged-in username

    // Set the username after login
    public static void setUsername(String user) {
        username = user;
    }

    // Get the username in any scene
    public static String getUsername() {
        return username;
    }
}
