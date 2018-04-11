package com.adms.classsafari.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MainClassModel {
    @SerializedName("Success")
    @Expose
    private String success;
    @SerializedName("Data")
    @Expose
    private List<ClassDetailModel> data = new ArrayList<ClassDetailModel>();

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<ClassDetailModel> getData() {
        return data;
    }

    public void setData(List<ClassDetailModel> data) {
        this.data = data;
    }

}
