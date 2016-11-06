package com.smart.smarthome;

import java.util.Comparator;

/**
 * Created by panapolnphutiyotin on 11/6/16 AD.
 */
public class ItemCheapest implements Comparable<ItemCheapest> {


    private String name;
    private double price;
    private double different;

    public ItemCheapest(String name,double price, double different ) {
        this.name = name;
        this.price = price;
        this.different = different;
    }

    public double getDifferent() {
        return different;
    }

    public void setDifferent(double different) {
        this.different = different;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public int compareTo(ItemCheapest itemCheapest) {
        return ((Double)price).compareTo(itemCheapest.getPrice());
    }
}
