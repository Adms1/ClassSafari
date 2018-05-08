package com.adms.classsafari.AppConstant;

import com.adms.classsafari.Model.Session.sessionDataModel;

import java.util.Comparator;

public class HighToLowSortRating implements Comparator<sessionDataModel> {
    @Override
    public int compare(sessionDataModel sessionDataModel, sessionDataModel t1) {
        Integer rating = Math.round(Float.parseFloat(sessionDataModel.getRating()));
        Integer rating1 = Math.round(Float.parseFloat(t1.getRating()));
        if (rating1.compareTo(rating) < 0) {
            return -1;
        } else if (rating1.compareTo(rating) > 0) {
            return 1;
        } else {
            return 0;
        }
    }
}
