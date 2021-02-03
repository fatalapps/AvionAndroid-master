package com.avion.app.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class CountryEntity {
    @PrimaryKey
    @NonNull
    public String name;
    public String code;

    @Ignore
    public CountryEntity(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public CountryEntity() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}