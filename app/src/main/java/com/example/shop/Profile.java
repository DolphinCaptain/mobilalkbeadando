package com.example.shop;

public class Profile {
    private String id;
    private String username;
    private String email;
    private String phone;

    public Profile(String name, String email, String phone) {
        this.username = name;
        this.email = email;
        this.phone = phone;
    }

    public Profile() {
    }

    public String getName() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String _getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}


