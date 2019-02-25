
package com.emusafir.model.buslayoutmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PointModel implements Serializable {

    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("StoppageType")
    @Expose
    private String stoppageType;
    @SerializedName("StopPointId")
    @Expose
    private String stopPointId;
    @SerializedName("RouteId")
    @Expose
    private String routeId;
    @SerializedName("ArrivalTime")
    @Expose
    private long arrivalTime;
    @SerializedName("DepartureTime")
    @Expose
    private long departureTime;
    @SerializedName("ManageOrder")
    @Expose
    private String manageOrder;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("UpdatedDate")
    @Expose
    private String updatedDate;
    @SerializedName("Name")
    @Expose
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoppageType() {
        return stoppageType;
    }

    public void setStoppageType(String stoppageType) {
        this.stoppageType = stoppageType;
    }

    public String getStopPointId() {
        return stopPointId;
    }

    public void setStopPointId(String stopPointId) {
        this.stopPointId = stopPointId;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(long arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public long getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(long departureTime) {
        this.departureTime = departureTime;
    }

    public String getManageOrder() {
        return manageOrder;
    }

    public void setManageOrder(String manageOrder) {
        this.manageOrder = manageOrder;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
