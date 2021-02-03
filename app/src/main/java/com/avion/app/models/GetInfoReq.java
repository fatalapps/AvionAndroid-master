package com.avion.app.models;

public class GetInfoReq {
    private String action;
    private int id;

    public GetInfoReq(int region) {
        action = "getOptions";
        this.id = region;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getRegion() {
        return id;
    }

    public void setRegion(int region) {
        this.id = region;
    }
}
