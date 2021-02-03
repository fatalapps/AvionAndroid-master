package com.avion.app.models;

public class User {
    private String id;
    private int miles = 0;
    private String phone;
    private String name;
    private String email;
    private String promocode;
    private int avariablePromocodes = 5;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMiles() {
        return miles;
    }

    public void setMiles(int miles) {
        this.miles = miles;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPromocode() {
        return promocode;
    }

    public void setPromocode(String promocode) {
        this.promocode = promocode;
    }

    public int getAvariablePromocodes() {
        return avariablePromocodes;
    }

    public void setAvariablePromocodes(int avariablePromocodes) {
        this.avariablePromocodes = avariablePromocodes;
    }
}
