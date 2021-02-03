package com.avion.app.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class QuaryAddressEntity {
    @PrimaryKey
    private int id;
    private String name;

    public QuaryAddressEntity() {
        id = hashCode();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = hashCode();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
