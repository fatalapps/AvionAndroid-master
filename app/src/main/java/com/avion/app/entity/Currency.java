package com.avion.app.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Currency {
    @PrimaryKey
    private int id;
    private int value;

    @Ignore
    public Currency(int id, float value) {
        this.id = id;
        this.value = id != 1 ? (int) (value * 1.02) : (int) value;
    }

    public Currency() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = id != 1 ? (int) (value * 1.02) : (int) (value * 1);
    }
}
