package com.avion.app.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FaworiteAddressEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int address_name_icon = 0;
    private String address_name;
    private String address;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAddress_name_icon() {
        return address_name_icon;
    }

    public void setAddress_name_icon(int address_name_icon) {
        this.address_name_icon = address_name_icon;
    }

    public String getAddress_name() {
        return address_name;
    }

    public void setAddress_name(String address_name) {
        this.address_name = address_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
