package com.laitte.Managers.Menu;

public class Meal {
    private String name;
    private int price;
    private String mealCategory;
    private String mealType;
    private String imagePath;
    private int id;

    public Meal(String name, int price, String mealCategory, String mealType, String imagePath, int id) {
        this.name = name;
        this.price = price;
        this.mealCategory = mealCategory;
        this.mealType = mealType;
        this.imagePath = imagePath;
        this.id = id;
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getType() {
        return mealType;
    }

    public String getCategory() {
        return mealCategory;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getId() {
        return id; // <-- get the meal ID
    }
}