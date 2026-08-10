package com.dct3194.projekakhir.models;

import java.io.Serializable;

public class Order implements Serializable {

    private String orderId;
    private String orderType;
    private String status;
    private String productName;
    private String imageUrl;
    private String date;
    private String time;

    private int totalPrice;

    public Order() {
        // Required empty constructor for Firebase
    }

    public Order(
            String orderId,
            String orderType,
            String status,
            String productName,
            String imageUrl,
            String date,
            String time,
            int totalPrice) {

        this.orderId = orderId;
        this.orderType = orderType;
        this.status = status;
        this.productName = productName;
        this.imageUrl = imageUrl;
        this.date = date;
        this.time = time;
        this.totalPrice = totalPrice;
    }

    // =========================
    // Getters
    // =========================

    public String getOrderId() {
        return orderId;
    }

    public String getOrderType() {
        return orderType;
    }

    public String getStatus() {
        return status;
    }

    public String getProductName() {
        return productName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    // =========================
    // Setters
    // =========================

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}