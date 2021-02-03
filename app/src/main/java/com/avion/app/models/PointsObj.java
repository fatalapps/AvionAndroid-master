package com.avion.app.models;

public class PointsObj {
    private String name;
    private int parking;
    private int termId;
    private String polygone;
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParking() {
        return parking;
    }

    public void setParking(int parking) {
        this.parking = parking;
    }

    public int getTermId() {
        return termId;
    }

    public void setTermId(int id) {
        this.termId = id;
    }

    public String getPolygone() {
        return polygone;
    }

    public void setPolygone(String polygone) {
        this.polygone = polygone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}


