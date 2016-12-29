package com.andro.naveen.famousapp.parsing;

/**
 * Created by Lenovo on 5/2/2016.
 */
public class Grocery {

    String category,photo,items;

    public Grocery(String category, String photo, String items) {
        this.category = category;
        this.photo = photo;
        this.items = items;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }
}
