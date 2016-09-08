package com.smart.smarthome;

/**
 * Created by Korn on 9/3/2016.
 */
public class ProductCompare {
    String name;
    Double price;
    int volume;
    int unit;
    String barcode;

    public ProductCompare(String name, Double price, int volume, int unit, String barcode) {
        this.name = name;
        this.price = price;
        this.volume = volume;
        this.unit = unit;
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

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setUnit(String barcode) {
        this.barcode = barcode;
    }
}
