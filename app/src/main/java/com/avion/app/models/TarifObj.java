package com.avion.app.models;

import java.io.Serializable;

public class TarifObj implements Serializable {
    private String name;
    private String passengers;
    private String baggage;
    private Float pricePerMinute;
    private String carname = "Kia Rio и другие";
    private String price;
    private String crm_name;
    private Integer icon;
    private Integer picture;
    private Integer bonnus_miles = 0;
    private Integer min = 0;

    public String getCrm_name() {
        return crm_name;
    }

    public void setCrm_name(String crm_name) {
        this.crm_name = crm_name;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassengers() {
        return passengers;
    }

    public void setPassengers(String passengers) {
        this.passengers = passengers;
    }

    public String getBaggage() {
        return baggage;
    }

    public void setBaggage(String baggage) {
        this.baggage = baggage;
    }

    public Float getPricePerMinute() {
        return Float.valueOf(pricePerMinute);
    }

    public void setPricePerMinute(Float pricePerMinute) {
        this.pricePerMinute = pricePerMinute;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public Integer getPicture() {
        return picture;
    }

    public void setPicture(Integer picture) {
        this.picture = picture;
    }

    public String getCarname() {
        return carname;
    }

    public void setCarname(String carname) {
        this.carname = carname;
    }

    public Integer getBonnus_miles() {
        return bonnus_miles;
    }

    public void setBonnus_miles(Integer bonnus_miles) {
        this.bonnus_miles = bonnus_miles;
    }
}
