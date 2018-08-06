package com.adms.searchclasses.AppConstant;

import com.adms.searchclasses.Model.Session.sessionDataModel;

import java.util.Comparator;

public class ZtoA  implements Comparator<sessionDataModel> {
    @Override
    public int compare(sessionDataModel t, sessionDataModel t1) {
        String location = t.getRegionName();
        String location1=t1.getRegionName();
        return  location1.compareToIgnoreCase(location);
    }
}
