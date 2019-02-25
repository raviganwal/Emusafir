package com.emusafir.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SeatLayout {
    @SerializedName("SeatId")
    @Expose
    private Long seatId;
    @SerializedName("SeatType")
    @Expose
    private String seatType;
    @SerializedName("SeatNo")
    @Expose
    private String seatNo;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("available")
    @Expose
    private String available;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("Icon")
    @Expose
    private String icon;

    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
