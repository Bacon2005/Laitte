package com.laitte.Model;

public class MenuItem {
    private String id;
    private String name;
    private String description;
    private double price;
    private String category;      // Breakfast, Lunch, Dinner, Drinks
    private String mealType;      // Vegan, Non-Vegan, Drinks
    private String imagePath;     // food image
    private String typeIconPath;  // meal type icon
    private String categoryIconPath; // category icon

    public MenuItem(String id, String name, String description, double price,
                    String category, String mealType,
                    String imagePath, String typeIconPath, String categoryIconPath) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.mealType = mealType;
        this.imagePath = imagePath;
        this.typeIconPath = typeIconPath;
        this.categoryIconPath = categoryIconPath;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }
    public String getMealType() { return mealType; }
    public String getImagePath() { return imagePath; }
    public String getTypeIconPath() { return typeIconPath; }
    public String getCategoryIconPath() { return categoryIconPath; }
}
