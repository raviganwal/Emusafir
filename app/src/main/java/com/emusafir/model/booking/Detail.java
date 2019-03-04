
package com.emusafir.model.booking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Detail {

    @SerializedName("Gender")
    @Expose
    private String gender;
    @SerializedName("SeatNo")
    @Expose
    private String seatNo;
    @SerializedName("SeatType")
    @Expose
    private String seatType;
    @SerializedName("Price")
    @Expose
    private String price;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
