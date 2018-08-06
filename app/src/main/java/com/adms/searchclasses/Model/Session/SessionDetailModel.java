package com.adms.searchclasses.Model.Session;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by admsandroid on 3/22/2018.
 */

public class SessionDetailModel {
    @SerializedName("Success")
    @Expose
    private String success;
    @SerializedName("RatingAvg")
    @Expose
    private String ratingAvg;
    @SerializedName("Data")
    @Expose
    private List<sessionDataModel> data = new ArrayList<sessionDataModel>();

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<sessionDataModel> getData() {
        for (sessionDataModel model:data){
            model.setWeekDays();

        }
        return data;
    }

    public void setData(List<sessionDataModel> data) {
        this.data = data;
    }

    public String getRatingAvg() {
        return ratingAvg;
    }

    public void setRatingAvg(String ratingAvg) {
        this.ratingAvg = ratingAvg;
    }


}
