package com.adms.classsafari.Model.TeacherInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admsandroid on 3/22/2018.
 */

public class TeacherInfoModel {
    @SerializedName("Success")
    @Expose
    private String success;
    @SerializedName("ErrorMessage")
    @Expose
    private String errorMessage;
    @SerializedName("CoachID")
    @Expose
    private String coachID;
    @SerializedName("ContactID")
    @Expose
    private String contactID;
    @SerializedName("ClassID")
    @Expose
    private String classID;
    @SerializedName("OrderID")
    @Expose
    private String orderID;
    @SerializedName("EmailID")
    @Expose
    private String emailID;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("LoginType")
    @Expose
    private String loginType;

    @SerializedName("Data")
    @Expose
    private List<FamilyDetailModel> data = new ArrayList<FamilyDetailModel>();

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getCoachID() {
        return coachID;
    }

    public void setCoachID(String coachID) {
        this.coachID = coachID;
    }

    public String getContactID() {
        return contactID;
    }

    public void setContactID(String contactID) {
        this.contactID = contactID;
    }

    public List<FamilyDetailModel> getData() {
        return data;
    }

    public void setData(List<FamilyDetailModel> data) {
        this.data = data;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }
}
