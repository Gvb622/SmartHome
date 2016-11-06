package com.smart.smarthome;

/**
 * Created by panapolnphutiyotin on 11/6/16 AD.
 */
public class ItemCheaper implements Comparable<ItemCheaper> {


    private String name;
    private double price;

    public ItemCheaper(String name, double price ) {
        this.name = name;
        this.price = price;
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
    public int compareTo(ItemCheaper itemCheapest) {
        return ((Double)price).compareTo(itemCheapest.getPrice());
    }
}
