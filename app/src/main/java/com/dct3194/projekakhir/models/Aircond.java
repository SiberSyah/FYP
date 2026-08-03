package com.dct3194.projekakhir.models;

import java.io.Serializable;

public class Aircond implements Serializable {

    private String name;
    private String brand;
    private int image;

    private int price1HP;
    private int price15HP;
    private int price2HP;

    private int installationPrice;

    public Aircond(String name,
                   String brand,
                   int image,
                   int price1HP,
                   int price15HP,
                   int price2HP,
                   int installationPrice) {

        this.name = name;
        this.brand = brand;
        this.image = image;
        this.price1HP = price1HP;
        this.price15HP = price15HP;
        this.price2HP = price2HP;
        this.installationPrice = installationPrice;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

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

    public void setName(String name) {
        this.name = name;
    }

    public void setBrand(String brand) {
        this.brand = brand;
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
}