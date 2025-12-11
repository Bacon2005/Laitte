package com.laitte.Managers;

public class Session {
    private static String username; // store the logged-in username
    private static boolean manager;
    private static String profileImagePath;
    private static String firstname;
    private static String lastname;
    private static String role;

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

    public static void setProfileImagePath(String path) {
        profileImagePath = path;
    }

    public static String getProfileImage() {
        return profileImagePath;
    }

    public static void setFirstname(String firstName) {
        firstname = firstName;
    }

    public static String getFirstname() {
        return firstname;
    }

    public static void setLastname(String lastName) {
        lastname = lastName;
    }

    public static String getLastname() {
        return lastname;
    }

    public static void setRole(String Role) {
        role = Role;
    }

    public static String getRole() {
        return role;
    }
}
