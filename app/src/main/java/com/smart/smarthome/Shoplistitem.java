package com.smart.smarthome;

/**
 * Created by panapolnphutiyotin on 9/11/16 AD.
 */
public class Shoplistitem {
    private String ItemImage;
    private String ItemName;
    private String ItemPrice;
    private String ItemVolumn;
    private String Type;

    public Shoplistitem() {
    }

    public String getItemImage() {
        return ItemImage;
    }

    public void setItemImage(String itemImage) {
        ItemImage = itemImage;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getItemPrice() {
        return ItemPrice;
    }

    public void setItemPrice(String itemPrice) {
        ItemPrice = itemPrice;
    }

    public String getItemVolumn() {
        return ItemVolumn;
    }

    public void setItemVolumn(String itemVolumn) {
        ItemVolumn = itemVolumn;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
