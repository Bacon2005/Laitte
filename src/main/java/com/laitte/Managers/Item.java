package com.laitte.Managers;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Item {
    private int mealId;
    private String mealName;
    private int stockAvailable;
    private String stockDate;
    private String category;
    private String mealType;
    private double price;

    public Item(int mealId, String mealName, int stockAvailable, String stockDate,
                String category, String mealType, double price) {
        this.mealId = mealId;
        this.mealName = mealName;
        this.stockAvailable = stockAvailable;
        this.stockDate = stockDate;
        this.category = category;
        this.mealType = mealType;
        this.price = price;
    }

    public int getMealId() { return mealId; }
    public String getMealName() { return mealName; }
    public int getStockAvailable() { return stockAvailable; }
    public String getStockDate() { return stockDate; }
    public String getCategory() { return category; }
    public String getMealType() { return mealType; }
    public double getPrice() { return price; }
}