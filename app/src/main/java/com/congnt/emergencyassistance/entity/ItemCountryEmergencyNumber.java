package com.congnt.emergencyassistance.entity;

/**
 * Created by congnt24 on 19/10/2016.
 */

public class ItemCountryEmergencyNumber {
    public String countryName;
    public String ambulance;
    public String countryCode;
    public String dispatch;
    public String fire;
    public String notes;
    public String police;

    public ItemCountryEmergencyNumber(String countryCode, String countryName, String police, String ambulance, String fire, String notes) {
        this.countryName = countryName;
        this.ambulance = ambulance;
        this.countryCode = countryCode;
        this.fire = fire;
        this.notes = notes;
        this.police = police;
    }

    @Override
    public String toString() {
        return "ItemCountryEmergencyNumber{" +
                "countryName='" + countryName + '\'' +
                ", ambulance='" + ambulance + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", dispatch='" + dispatch + '\'' +
                ", fire='" + fire + '\'' +
                ", notes='" + notes + '\'' +
                ", police='" + police + '\'' +
                '}';
    }
}
