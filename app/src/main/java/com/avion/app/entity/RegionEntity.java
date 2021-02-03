package com.avion.app.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class RegionEntity {
    @Ignore
    public int order;
    @PrimaryKey
    private int id;
    private String name;


    @Ignore
    public RegionEntity(int id, String name) {
        this.id = id;
        this.name = name;

    }

    public RegionEntity() {
    }


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

}
