package com.adms.classsafari.Fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adms.classsafari.R;
import com.adms.classsafari.databinding.FragmentSummaryBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SummaryFragment extends Fragment {

    private FragmentSummaryBinding fragmentSummaryBinding;
    private View rootView;
    private Context mContext;



    public SummaryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentSummaryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_summary, container, false);

        rootView = fragmentSummaryBinding.getRoot();
        mContext = getActivity().getApplicationContext();

        initViews();

        return rootView;
    }

    public void initViews() {
        setUserVisibleHint(true);
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && rootView != null) {

        }
        // execute your data loading logic.
    }



}
