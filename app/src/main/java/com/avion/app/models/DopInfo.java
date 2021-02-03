package com.avion.app.models;

import java.util.Objects;

public class DopInfo {
    private int airportTimeWithFlight;
    private int airportTimeNoFlight;
    private int railwayTime;
    private int addressTime;
    private int vstrechaAirport;
    private int vstrechaRailway;

    public int getAirportTimeWithFlight() {
        return airportTimeWithFlight;
    }

    public void setAirportTimeWithFlight(int airportTimeWithFlight) {
        this.airportTimeWithFlight = airportTimeWithFlight;
    }

    public int getAirportTimeNoFlight() {
        return airportTimeNoFlight;
    }

    public void setAirportTimeNoFlight(int airportTimeNoFlight) {
        this.airportTimeNoFlight = airportTimeNoFlight;
    }

    public int getRailwayTime() {
        return railwayTime;
    }

    public void setRailwayTime(int railwayTime) {
        this.railwayTime = railwayTime;
    }

    public int getAddressTime() {
        return addressTime;
    }

    public void setAddressTime(int addressTime) {
        this.addressTime = addressTime;
    }

    public int getVstrechaAirport() {
        return vstrechaAirport;
    }

    public void setVstrechaAirport(int vstrechaAirport) {
        this.vstrechaAirport = vstrechaAirport;
    }

    public int getVstrechaRailway() {
        return vstrechaRailway;
    }

    public void setVstrechaRailway(int vstrechaRailway) {
        this.vstrechaRailway = vstrechaRailway;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DopInfo dopInfo = (DopInfo) o;
        return airportTimeWithFlight == dopInfo.airportTimeWithFlight &&
                airportTimeNoFlight == dopInfo.airportTimeNoFlight &&
                railwayTime == dopInfo.railwayTime &&
                addressTime == dopInfo.addressTime &&
                vstrechaAirport == dopInfo.vstrechaAirport &&
                vstrechaRailway == dopInfo.vstrechaRailway;
    }

    @Override
    public int hashCode() {
        return Objects.hash(airportTimeWithFlight, airportTimeNoFlight, railwayTime, addressTime, vstrechaAirport, vstrechaRailway);
    }
}
