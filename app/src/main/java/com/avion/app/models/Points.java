package com.avion.app.models;

import java.util.List;

public class Points {
    private List<PointsObj> airports;
    private List<PointsObj> railways;

    public List<PointsObj> getAirports() {
        return airports;
    }

    public void setAirports(List<PointsObj> airports) {
        this.airports = airports;
    }

    public List<PointsObj> getRailways() {
        return railways;
    }

    public void setRailways(List<PointsObj> railways) {
        this.railways = railways;
    }
}
