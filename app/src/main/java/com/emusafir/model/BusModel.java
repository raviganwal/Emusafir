package com.emusafir.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BusModel implements Serializable {

    @SerializedName("RouteId")
    @Expose
    private String routeId;

    @SerializedName("BusId")
    @Expose
    private String busId;
    @SerializedName("BusName")
    @Expose
    private String busName;
    @SerializedName("busTypeName")
    @Expose
    private String busTypeName;
    @SerializedName("SourceCityName")
    @Expose
    private String sourceCityName;
    @SerializedName("DestinationCityName")
    @Expose
    private String destinationCityName;
    @SerializedName("busFare")
    @Expose
    private String busFare;
    @SerializedName("SourceDepartureTime")
    @Expose
    private long sourceDepartureTime;
    @SerializedName("DestinationArrivalTime")
    @Expose
    private long destinationArrivalTime;
    @SerializedName("busAmenities")
    @Expose
    private List<BusAmenity> busAmenities = null;
    @SerializedName("remainingSeat")
    @Expose
    private int remainingSeat;
    @SerializedName("busrating")
    @Expose
    private String busrating;

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getBusTypeName() {
        return busTypeName;
    }

    public void setBusTypeName(String busTypeName) {
        this.busTypeName = busTypeName;
    }

    public String getSourceCityName() {
        return sourceCityName;
    }

    public void setSourceCityName(String sourceCityName) {
        this.sourceCityName = sourceCityName;
    }

    public String getDestinationCityName() {
        return destinationCityName;
    }

    public void setDestinationCityName(String destinationCityName) {
        this.destinationCityName = destinationCityName;
    }

    public String getBusFare() {
        return busFare;
    }

    public void setBusFare(String busFare) {
        this.busFare = busFare;
    }

    public long getSourceDepartureTime() {
        return sourceDepartureTime;
    }

    public void setSourceDepartureTime(long sourceDepartureTime) {
        this.sourceDepartureTime = sourceDepartureTime;
    }

    public long getDestinationArrivalTime() {
        return destinationArrivalTime;
    }

    public void setDestinationArrivalTime(long destinationArrivalTime) {
        this.destinationArrivalTime = destinationArrivalTime;
    }

    public List<BusAmenity> getBusAmenities() {
        return busAmenities;
    }

    public void setBusAmenities(List<BusAmenity> busAmenities) {
        this.busAmenities = busAmenities;
    }

    public int getRemainingSeat() {
        return remainingSeat;
    }

    public void setRemainingSeat(int remainingSeat) {
        this.remainingSeat = remainingSeat;
    }

    public String getBusrating() {
        return busrating;
    }

    public void setBusrating(String busrating) {
        this.busrating = busrating;
    }


}
