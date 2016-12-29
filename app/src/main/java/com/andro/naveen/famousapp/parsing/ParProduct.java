package com.andro.naveen.famousapp.parsing;

/**
 * Created by Lenovo on 5/20/2016.
 */
public class ParProduct {

    String category,photo,items,ingredients;

    public ParProduct(String category, String photo, String items, String ingredients) {
        this.category = category;
        this.photo = photo;
        this.items = items;
        this.ingredients = ingredients;
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

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }
}
