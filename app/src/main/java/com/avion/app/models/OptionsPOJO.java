package com.avion.app.models;

public class OptionsPOJO {

    private Tarif tarrifs;
    private DopUslugi dopUslugi;
    private DopInfo dopInfo;
    private Points points;

    public Tarif getTarrifs() {
        return tarrifs;
    }

    public void setTarrifs(Tarif tarrifs) {
        this.tarrifs = tarrifs;
    }

    public DopUslugi getDopUslugi() {
        return dopUslugi;
    }

    public void setDopUslugi(DopUslugi dopUslugi) {
        this.dopUslugi = dopUslugi;
    }

    public DopInfo getDopInfo() {
        return dopInfo;
    }

    public void setDopInfo(DopInfo dopInfo) {
        this.dopInfo = dopInfo;
    }

    public Points getPoints() {
        return points;
    }

    public void setPoints(Points points) {
        this.points = points;
    }
}
