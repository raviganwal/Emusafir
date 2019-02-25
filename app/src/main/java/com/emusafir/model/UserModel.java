package com.emusafir.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserModel {

    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("Password")
    @Expose
    private String password;
    @SerializedName("Mobile")
    @Expose
    private String mobile;
    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("Image")
    @Expose
    private String image;
    @SerializedName("otp")
    @Expose
    private String otp;
    @SerializedName("otp_verification")
    @Expose
    private String otpVerification;
    @SerializedName("device_token")
    @Expose
    private String deviceToken;
    @SerializedName("email_verification")
    @Expose
    private String emailVerification;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("Create_date")
    @Expose
    private String createDate;
    @SerializedName("Update_date")
    @Expose
    private String updateDate;

    public UserModel(String id, String name, String email, String mobile) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getOtpVerification() {
        return otpVerification;
    }

    public void setOtpVerification(String otpVerification) {
        this.otpVerification = otpVerification;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getEmailVerification() {
        return emailVerification;
    }

    public void setEmailVerification(String emailVerification) {
        this.emailVerification = emailVerification;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
}
