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
    private String ItemClassifier;
    private String Key;
    private String ItemTopsPrice;
    private String ItemLotusPrice;
    private String KeyAll;

    public Shoplistitem() {
    }

    public String getKeyAll() {
        return KeyAll;
    }

    public void setKeyAll(String keyAll) {
        KeyAll = keyAll;
    }

    public String getKey() {
        return Key;
    }

    public String getItemTopsPrice() {
        return ItemTopsPrice;
    }

    public void setItemTopsPrice(String itemTopsPrice) {
        ItemTopsPrice = itemTopsPrice;
    }

    public String getItemLotusPrice() {
        return ItemLotusPrice;
    }

    public void setItemLotusPrice(String itemLotusPrice) {
        ItemLotusPrice = itemLotusPrice;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getItemClassifier() {
        return ItemClassifier;
    }

    public void setItemClassifier(String itemClassifier) {
        ItemClassifier = itemClassifier;
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
