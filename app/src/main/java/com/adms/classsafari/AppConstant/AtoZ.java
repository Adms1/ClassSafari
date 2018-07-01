package com.adms.classsafari.AppConstant;

import com.adms.classsafari.Model.Session.sessionDataModel;

import java.util.Comparator;

public class AtoZ implements Comparator<sessionDataModel> {
    @Override
    public int compare(sessionDataModel sessionDataModel, sessionDataModel t1) {
        String location = sessionDataModel.getRegionName();
        String location1=sessionDataModel.getRegionName();
      return  location.compareToIgnoreCase(location1);
    }
}
