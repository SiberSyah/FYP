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

    // Field maklumat pelanggan & servis
    private String customerName;
    private String customerPhone;
    private String customerAddress;
    private String address;
    private String aircondType;
    private String hp;
    private String serviceType;

    private int totalPrice;

    public Order() {
        // Diperlukan oleh Firebase Firestore
    }

    public Order(
            String orderId,
            String orderType,
            String status,
            String productName,
            String imageUrl,
            String date,
            String time,
            String customerName,
            String customerPhone,
            String customerAddress,
            String address,
            String aircondType,
            String hp,
            String serviceType,
            int totalPrice) {

        this.orderId = orderId;
        this.orderType = orderType;
        this.status = status;
        this.productName = productName;
        this.imageUrl = imageUrl;
        this.date = date;
        this.time = time;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.customerAddress = customerAddress;
        this.address = address;
        this.aircondType = aircondType;
        this.hp = hp;
        this.serviceType = serviceType;
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

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public String getCustomerAddress() {
        return customerAddress != null ? customerAddress : address;
    }

    public String getAddress() {
        return address != null ? address : customerAddress;
    }

    public String getAircondType() {
        return aircondType;
    }

    public String getHp() {
        return hp;
    }

    public String getServiceType() {
        return serviceType;
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

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAircondType(String aircondType) {
        this.aircondType = aircondType;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}