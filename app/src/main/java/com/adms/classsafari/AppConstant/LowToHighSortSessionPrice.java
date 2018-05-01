package com.adms.classsafari.AppConstant;

import com.adms.classsafari.Model.Session.sessionDataModel;

import java.util.Comparator;

public class LowToHighSortSessionPrice implements Comparator<sessionDataModel>{
    @Override
    public int compare(sessionDataModel sessionDataModel, sessionDataModel t1) {
        Double price = Double.valueOf(sessionDataModel.getSessionAmount());
        Double price1 = Double.valueOf(t1.getSessionAmount());
        if (price.compareTo(price1) < 0) {
            return -1;
        } else if (price.compareTo(price1) > 0) {
            return 1;
        } else {
            return 0;
        }
    }
}
