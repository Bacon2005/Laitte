package com.laitte.Model;

public class MenuItem {
    private String id; // unique id, can be generated
    private String name;
    private String description;
    private double price;
    private String category; // e.g. "Breakfast"
    private String mealType; // e.g. "Breakfast", "Lunch", "Dinner"
    private String imagePath; // resource path like "/com/laitte/images/pancakes.jpg"

    public MenuItem() {}

    public MenuItem(String id, String name, String description, double price, String category, String mealType, String imagePath) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.mealType = mealType;
        this.imagePath = imagePath;
    }

    // getters & setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getMealType() { return mealType; }
    public void setMealType(String mealType) { this.mealType = mealType; }
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}
