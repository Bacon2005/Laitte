package com.laitte.Managers;

public class MealSales {
    private String mealName;
    private Integer sales;

    public MealSales(String mealName, Integer sales) {
        this.mealName = mealName;
        this.sales = sales;
    }

    public String getMealName() {
        return mealName;
    }

    public Integer getSales() {
        return sales;
    }
}
