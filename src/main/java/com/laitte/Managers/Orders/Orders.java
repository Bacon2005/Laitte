package com.laitte.Managers.Orders;

public class Orders {
    private String customerName;
    private int orderNumber;
    private boolean isPending;

    public Orders(String customerName, int orderNumber, boolean isPending) {
        this.customerName = customerName;
        this.orderNumber = orderNumber;
        this.isPending = isPending;
    }

    // Getters
    public String getCustomer() {
        return customerName;
    }

    public int getNum() {
        return orderNumber;
    }

    public boolean isPending() {
        return isPending;
    }

}