package com.example.wishlist.Model;

public class Product {
    String price;
    String image;
    String name;
    String category;
    String details;

    public String getDetails() {
        return details;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    String user;
    public Product() {
    }

    public Product(String price, String image, String name,String category,String details) {
        this.price = price;
        this.image = image;
        this.name = name;
        this.category=category;

        this.details=details;

    }
    public void setCategory(String category) {
        this.category = category;
    }

    public void setDetails(String detail) {
        this.details = detail;
    }
    public String getCategory() {
        return category;
    }


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}