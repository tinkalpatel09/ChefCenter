package com.example.chefcenter.model;

import androidx.annotation.NonNull;

public class FoodData {
    private String itemCategory;
    private String itemDescription;
    private String itemDuration;
    private String itemImage;
    private String itemName;
    private String key;


    public FoodData() {
    }

    public FoodData(String itemCategory, String itemDescription, String itemDuration, String itemImage, String itemName) {
        this.itemCategory = itemCategory;
        this.itemDescription = itemDescription;
        this.itemDuration = itemDuration;
        this.itemImage = itemImage;
        this.itemName = itemName;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemDuration() {
        return itemDuration;
    }

    public void setItemDuration(String itemDuration) {
        this.itemDuration = itemDuration;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @NonNull
    @Override
    public String toString() {
        return itemName;
    }
}
