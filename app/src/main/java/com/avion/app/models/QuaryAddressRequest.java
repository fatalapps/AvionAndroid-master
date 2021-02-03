package com.avion.app.models;

import com.avion.app.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class QuaryAddressRequest {
    private String query;
    private int count;
    private List<Locations> locations;

    public QuaryAddressRequest(String query) {
        this.query = query;
        this.count = 10;
        this.locations = new ArrayList<>();
        this.locations.add(new Locations(MainActivity.chooseRegionLiveData.getValue().getName()));
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Locations> getLocations() {
        return locations;
    }

    public void setLocations(List<Locations> locations) {
        this.locations = locations;
    }

    private class Locations {
        String region;

        public Locations(String region) {
            this.region = region;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }
    }

}
