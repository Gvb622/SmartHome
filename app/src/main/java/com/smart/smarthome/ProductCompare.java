package com.smart.smarthome;

/**
 * Created by Korn on 9/3/2016.
 */
public class ProductCompare {
    String name;
    Double price;
    int volume;
    String unit;
    int quantity;
    String barcode;

    public ProductCompare(String name, Double price, int volume, String unit, int quantity, String barcode) {
        this.name = name;
        this.price = price;
        this.volume = volume;
        this.unit = unit;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
