package com.adms.classsafari.AppConstant;

import com.adms.classsafari.Model.Session.sessionDataModel;

import java.util.Comparator;

public class AtoZ implements Comparator<sessionDataModel> {
    @Override
    public int compare(sessionDataModel t, sessionDataModel t1) {
        String location = t.getRegionName();
        String location1=t1.getRegionName();
      return  location.compareToIgnoreCase(location1);
    }
}
