package com.adms.classsafari.Fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adms.classsafari.Adapter.SessionDetailAdapter;
import com.adms.classsafari.R;
import com.adms.classsafari.databinding.FragmentReviewsBinding;
import com.adms.classsafari.databinding.FragmentSummaryBinding;

import java.util.ArrayList;


public class ReviewsFragment extends Fragment {

    private FragmentReviewsBinding fragmentReviewsBinding;
    private View rootView;
    private Context mContext;

    SessionDetailAdapter sessionDetailAdapter;
    ArrayList<String> arrayList;


    public ReviewsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentReviewsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_reviews, container, false);

        rootView = fragmentReviewsBinding.getRoot();
        mContext = getActivity().getApplicationContext();

        initViews();

        return rootView;
    }

    public void initViews() {
        arrayList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            arrayList.add(String.valueOf(i));
        }
        sessionDetailAdapter = new SessionDetailAdapter(mContext, arrayList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL, false);
        fragmentReviewsBinding.sessionListRecView.setLayoutManager(mLayoutManager);
        fragmentReviewsBinding.sessionListRecView.setItemAnimator(new DefaultItemAnimator());
        fragmentReviewsBinding.sessionListRecView.setAdapter(sessionDetailAdapter);
        setUserVisibleHint(true);
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && rootView != null) {

        }
        // execute your data loading logic.
    }



}
