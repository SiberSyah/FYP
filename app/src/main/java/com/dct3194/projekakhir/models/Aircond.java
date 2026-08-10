package com.dct3194.projekakhir.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Aircond implements Serializable {

    private String name;
    private String brand;
    private String imageUrl;

    // Legacy image resource
    // Digunakan oleh code lama yang masih panggil getImage()
    private int image;

    private int price1HP;
    private int price15HP;
    private int price2HP;

    private int installationPrice;

    // Firestore arrays
    private List<String> models;
    private List<String> types;
    private List<String> packages;

    // =========================
    // Constructor - Firestore
    // =========================

    public Aircond(
            String name,
            String brand,
            String imageUrl,
            int price1HP,
            int price15HP,
            int price2HP,
            int installationPrice) {

        this.name = name;
        this.brand = brand;
        this.imageUrl = imageUrl;

        this.image = 0;

        this.price1HP = price1HP;
        this.price15HP = price15HP;
        this.price2HP = price2HP;
        this.installationPrice = installationPrice;

        this.models = new ArrayList<>();
        this.types = new ArrayList<>();
        this.packages = new ArrayList<>();
    }

    // =========================
    // Constructor - Legacy
    // =========================
    // Untuk code lama yang masih guna:
    //
    // new Aircond(
    //     name,
    //     brand,
    //     R.mipmap.ic_launcher,
    //     price1HP,
    //     price15HP,
    //     price2HP,
    //     installationPrice
    // )

    public Aircond(
            String name,
            String brand,
            int image,
            int price1HP,
            int price15HP,
            int price2HP,
            int installationPrice) {

        this.name = name;
        this.brand = brand;
        this.image = image;

        this.imageUrl = "";

        this.price1HP = price1HP;
        this.price15HP = price15HP;
        this.price2HP = price2HP;
        this.installationPrice = installationPrice;

        this.models = new ArrayList<>();
        this.types = new ArrayList<>();
        this.packages = new ArrayList<>();
    }

    // =========================
    // Basic Getters
    // =========================

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    // Untuk code lama
    public int getImage() {
        return image;
    }

    public int getPrice1HP() {
        return price1HP;
    }

    public int getPrice15HP() {
        return price15HP;
    }

    public int getPrice2HP() {
        return price2HP;
    }

    public int getInstallationPrice() {
        return installationPrice;
    }

    // =========================
    // Firestore Array Getters
    // =========================

    public List<String> getModels() {
        return models;
    }

    public List<String> getTypes() {
        return types;
    }

    public List<String> getPackages() {
        return packages;
    }

    // =========================
    // Setters
    // =========================

    public void setName(String name) {
        this.name = name;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setPrice1HP(int price1HP) {
        this.price1HP = price1HP;
    }

    public void setPrice15HP(int price15HP) {
        this.price15HP = price15HP;
    }

    public void setPrice2HP(int price2HP) {
        this.price2HP = price2HP;
    }

    public void setInstallationPrice(int installationPrice) {
        this.installationPrice = installationPrice;
    }

    // =========================
    // Firestore Array Setters
    // =========================

    public void setModels(List<String> models) {
        this.models = models;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public void setPackages(List<String> packages) {
        this.packages = packages;
    }
}