package model;

import java.util.Date;

public class Product {
    private String name;
    private int rating;
    private Date createdAt;

    public Product(String name, int rating, Date createdAt) {
        this.name = name;
        this.rating = rating;
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public int getRating() {
        return rating;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
