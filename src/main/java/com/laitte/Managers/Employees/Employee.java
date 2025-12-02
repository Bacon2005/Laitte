package com.laitte.Managers.Employees;

public class Employee {
    private String name;
    private int id;
    private String role;
    private boolean manager;
    private String imagePath;

    public Employee(String name, int id, String role, boolean manager, String imagePath) {
        this.name = name;
        this.id = id;
        this.role = role;
        this.manager = manager;
        this.imagePath = imagePath;
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public boolean isManager() {
        return manager;
    }

    public String getImagePath() {
        return imagePath;
    }
}
