package com.adms.classsafari.AppConstant;


import com.adms.classsafari.Model.MainClassModel;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by admsandroid on 3/20/2018.
 */

public interface WebServices {
    @FormUrlEncoded
    @POST("/Get_SessionByEmployeeTypeID")
    public void get_SessionByEmployeeTypeID(@FieldMap Map<String, String> map, Callback<MainClassModel> callback);

    @FormUrlEncoded
    @POST("/Get_Board")
    public void get_Board(@FieldMap Map<String, String> map, Callback<MainClassModel> callback);

    @FormUrlEncoded
    @POST("/Get_Standard")
    public void get_Standard(@FieldMap Map<String, String> map, Callback<MainClassModel> callback);

    @FormUrlEncoded
    @POST("/Get_Stream")
    public void get_Stream(@FieldMap Map<String, String> map, Callback<MainClassModel> callback);
}
