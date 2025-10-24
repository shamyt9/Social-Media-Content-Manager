package com.app.socialmediacontentmanager.models;

public class Category {
    private int id;
    private String name;
    private int iconResId; // You can use resource IDs for platform icons

    public Category() {
        // Default constructor
    }

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }
}