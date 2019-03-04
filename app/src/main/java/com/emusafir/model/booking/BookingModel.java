
package com.emusafir.model.booking;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookingModel {

    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("PNRNo")
    @Expose
    private String pNRNo;
    @SerializedName("TicketNo")
    @Expose
    private String ticketNo;
    @SerializedName("JourneyDate")
    @Expose
    private String journeyDate;
    @SerializedName("JourneyType")
    @Expose
    private String journeyType;
    @SerializedName("UserId")
    @Expose
    private String userId;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("Mobile")
    @Expose
    private String mobile;
    @SerializedName("BusId")
    @Expose
    private String busId;
    @SerializedName("RouteId")
    @Expose
    private String routeId;
    @SerializedName("BoardingPoints")
    @Expose
    private String boardingPoints;
    @SerializedName("DroppingPoints")
    @Expose
    private String droppingPoints;
    @SerializedName("InsurancePrice")
    @Expose
    private String insurancePrice;
    @SerializedName("TotalTax")
    @Expose
    private String totalTax;
    @SerializedName("TotalPrice")
    @Expose
    private String totalPrice;
    @SerializedName("FinalTotalPrice")
    @Expose
    private String finalTotalPrice;
    @SerializedName("TransactionId")
    @Expose
    private String transactionId;
    @SerializedName("PaymentMode")
    @Expose
    private String paymentMode;
    @SerializedName("PaymentStatus")
    @Expose
    private String paymentStatus;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("UpdatedDate")
    @Expose
    private String updatedDate;
    @SerializedName("BusName")
    @Expose
    private String busName;
    @SerializedName("RegistrationNo")
    @Expose
    private String registrationNo;
    @SerializedName("BoardingName")
    @Expose
    private String boardingName;
    @SerializedName("DropingName")
    @Expose
    private String dropingName;
    @SerializedName("SourceName")
    @Expose
    private String sourceName;
    @SerializedName("DestinationName")
    @Expose
    private String destinationName;
    @SerializedName("DepartureTime")
    @Expose
    private String departureTime;
    @SerializedName("details")
    @Expose
    private List<Detail> details = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPNRNo() {
        return pNRNo;
    }

    public void setPNRNo(String pNRNo) {
        this.pNRNo = pNRNo;
    }

    public String getTicketNo() {
        return ticketNo;
    }

    public void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
    }

    public String getJourneyDate() {
        return journeyDate;
    }

    public void setJourneyDate(String journeyDate) {
        this.journeyDate = journeyDate;
    }

    public String getJourneyType() {
        return journeyType;
    }

    public void setJourneyType(String journeyType) {
        this.journeyType = journeyType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getBoardingPoints() {
        return boardingPoints;
    }

    public void setBoardingPoints(String boardingPoints) {
        this.boardingPoints = boardingPoints;
    }

    public String getDroppingPoints() {
        return droppingPoints;
    }

    public void setDroppingPoints(String droppingPoints) {
        this.droppingPoints = droppingPoints;
    }

    public String getInsurancePrice() {
        return insurancePrice;
    }

    public void setInsurancePrice(String insurancePrice) {
        this.insurancePrice = insurancePrice;
    }

    public String getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(String totalTax) {
        this.totalTax = totalTax;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getFinalTotalPrice() {
        return finalTotalPrice;
    }

    public void setFinalTotalPrice(String finalTotalPrice) {
        this.finalTotalPrice = finalTotalPrice;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
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

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public String getBoardingName() {
        return boardingName;
    }

    public void setBoardingName(String boardingName) {
        this.boardingName = boardingName;
    }

    public String getDropingName() {
        return dropingName;
    }

    public void setDropingName(String dropingName) {
        this.dropingName = dropingName;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }

}
