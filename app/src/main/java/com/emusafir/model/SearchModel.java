package com.emusafir.model;

import java.io.Serializable;

public class SearchModel implements Serializable {
    private CityModel fromCity, toCity;
    private String tripType, onwardDate, returnDate, toCityId, toCityName, fromCityId, fromCityName;

    public CityModel getFromCity() {
        return fromCity;
    }

    public void setFromCity(CityModel fromCity) {
        this.fromCity = fromCity;
    }

    public CityModel getToCity() {
        return toCity;
    }

    public void setToCity(CityModel toCity) {
        this.toCity = toCity;
    }

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public String getOnwardDate() {
        return onwardDate;
    }

    public void setOnwardDate(String onwardDate) {
        this.onwardDate = onwardDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public SearchModel() {
    }

    public SearchModel(String tripType, String onwardDate, String returnDate, String toCityId, String toCityName, String fromCityId, String fromCityName) {
        this.tripType = tripType;
        this.onwardDate = onwardDate;
        this.returnDate = returnDate;
        this.toCityId = toCityId;
        this.toCityName = toCityName;
        this.fromCityId = fromCityId;
        this.fromCityName = fromCityName;
    }

    public String getToCityId() {
        return toCityId;
    }

    public void setToCityId(String toCityId) {
        this.toCityId = toCityId;
    }

    public String getToCityName() {
        return toCityName;
    }

    public void setToCityName(String toCityName) {
        this.toCityName = toCityName;
    }

    public String getFromCityId() {
        return fromCityId;
    }

    public void setFromCityId(String fromCityId) {
        this.fromCityId = fromCityId;
    }

    public String getFromCityName() {
        return fromCityName;
    }

    public void setFromCityName(String fromCityName) {
        this.fromCityName = fromCityName;
    }
}
