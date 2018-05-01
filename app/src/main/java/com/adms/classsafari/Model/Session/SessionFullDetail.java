package com.adms.classsafari.Model.Session;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by admsandroid on 3/23/2018.
 */

public class SessionFullDetail {
    @SerializedName("Session_ID")
    @Expose
    private String sessionID;
    @SerializedName("SessionDetail_ID")
    @Expose
    private String sessionDetailID;
    @SerializedName("SessionName")
    @Expose
    private String sessionName;
    @SerializedName("AddressLine1")
    @Expose
    private String addressLine1;
    @SerializedName("AddressLine2")
    @Expose
    private String addressLine2;
    @SerializedName("RegionName")
    @Expose
    private String regionName;
    @SerializedName("AddressCity")
    @Expose
    private String addressCity;
    @SerializedName("AddressState")
    @Expose
    private String addressState;
    @SerializedName("AddressZipCode")
    @Expose
    private String addressZipCode;
    @SerializedName("SessionDate")
    @Expose
    private String sessionDate;
    @SerializedName("SessionTime")
    @Expose
    private String sessionTime;
    @SerializedName("LowToHighSortSessionPrice")
    @Expose
    private String sessionPrice;
    @SerializedName("SessionCapacity")
    @Expose
    private String sessionCapacity;
    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public String getAddressState() {
        return addressState;
    }

    public void setAddressState(String addressState) {
        this.addressState = addressState;
    }

    public String getAddressZipCode() {
        return addressZipCode;
    }

    public void setAddressZipCode(String addressZipCode) {
        this.addressZipCode = addressZipCode;
    }

    public String getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(String sessionDate) {
        this.sessionDate = sessionDate;
    }

    public String getSessionTime() {
        return sessionTime;
    }

    public void setSessionTime(String sessionTime) {
        this.sessionTime = sessionTime;
    }

    public String getSessionDetailID() {
        return sessionDetailID;
    }

    public void setSessionDetailID(String sessionDetailID) {
        this.sessionDetailID = sessionDetailID;
    }

    public String getSessionPrice() {
        return sessionPrice;
    }

    public void setSessionPrice(String sessionPrice) {
        this.sessionPrice = sessionPrice;
    }

    public String getSessionCapacity() {
        return sessionCapacity;
    }

    public void setSessionCapacity(String sessionCapacity) {
        this.sessionCapacity = sessionCapacity;
    }
}
