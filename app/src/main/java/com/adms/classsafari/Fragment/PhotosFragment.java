package com.adms.classsafari.Fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adms.classsafari.R;
import com.adms.classsafari.databinding.FragmentPhotosBinding;
import com.adms.classsafari.databinding.FragmentSummaryBinding;


public class PhotosFragment extends Fragment {

    private FragmentPhotosBinding fragmentPhotosBinding;
    private View rootView;
    private Context mContext;


    public PhotosFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentPhotosBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_photos, container, false);

        rootView = fragmentPhotosBinding.getRoot();
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
