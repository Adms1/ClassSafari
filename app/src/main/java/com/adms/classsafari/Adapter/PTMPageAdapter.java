package com.adms.classsafari.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.adms.classsafari.Activites.SessionName1;
import com.adms.classsafari.Fragment.PhotosFragment;
import com.adms.classsafari.Fragment.ReviewsFragment;
import com.adms.classsafari.Fragment.SummaryFragment;


/**
 * Created by admsandroid on 10/25/2017.
 */

public class PTMPageAdapter extends FragmentStatePagerAdapter {
    //integer to count number of tabs
    int tabCount;
    private Context context;

    //Constructor to the class
    public PTMPageAdapter(Context context, FragmentManager fm, int tabCount) {
        super(fm);
//Initializing tab count
        this.tabCount = tabCount;
        this.context=context;
    }



    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
//Returning the current tabs
        switch (position) {
            case 0:
                SummaryFragment tab1 = new SummaryFragment();
                return tab1;
            case 1:
                ReviewsFragment tab2 = new ReviewsFragment();
                return tab2;
//            case 2:
//                PhotosFragment tab3 = new PhotosFragment();
//                return tab3;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}



