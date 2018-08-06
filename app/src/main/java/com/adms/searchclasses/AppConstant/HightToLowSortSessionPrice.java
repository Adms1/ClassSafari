package com.adms.searchclasses.AppConstant;

import com.adms.searchclasses.Model.Session.sessionDataModel;

import java.util.Comparator;

public class HightToLowSortSessionPrice implements Comparator<sessionDataModel>{
    @Override
    public int compare(sessionDataModel sessionDataModel, sessionDataModel t1) {
        Double price = Double.valueOf(sessionDataModel.getSessionAmount());
        Double price1 = Double.valueOf(t1.getSessionAmount());
        if (price1.compareTo(price) < 0) {
            return -1;
        } else if (price1.compareTo(price) > 0) {
            return 1;
        } else {
            return 0;
        }
    }
}