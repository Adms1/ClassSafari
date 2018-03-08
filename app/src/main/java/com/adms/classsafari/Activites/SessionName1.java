package com.adms.classsafari.Activites;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.util.Log;
import android.view.View;

import com.adms.classsafari.Adapter.PTMPageAdapter;
import com.adms.classsafari.Adapter.SessionDetailAdapter;
import com.adms.classsafari.R;
import com.adms.classsafari.databinding.ActivitySessionName1Binding;

import java.util.ArrayList;

public class SessionName1 extends AppCompatActivity {

    ActivitySessionName1Binding activitySessionName1Binding;
    Context mContext;
    PTMPageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activitySessionName1Binding = DataBindingUtil.setContentView(this, R.layout.activity_session_name1);
        mContext = SessionName1.this;

        init();
        setListner();
    }

    public void init() {
        setSupportActionBar(activitySessionName1Binding.toolbar);
        activitySessionName1Binding.tabLayoutPtm.addTab(activitySessionName1Binding.tabLayoutPtm.newTab().setText("Summary"), true);
        activitySessionName1Binding.tabLayoutPtm.addTab(activitySessionName1Binding.tabLayoutPtm.newTab().setText("Reviews"));
//        activitySessionName1Binding.tabLayoutPtm.addTab(activitySessionName1Binding.tabLayoutPtm.newTab().setText("Photos"));
        activitySessionName1Binding.tabLayoutPtm.setTabMode(TabLayout.MODE_FIXED);
        activitySessionName1Binding.tabLayoutPtm.setTabGravity(TabLayout.GRAVITY_FILL);

        adapter = new PTMPageAdapter(mContext, getSupportFragmentManager(), activitySessionName1Binding.tabLayoutPtm.getTabCount());
//Adding adapter to pager
        activitySessionName1Binding.pager.setAdapter(adapter);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            activitySessionName1Binding.view.setVisibility(View.GONE);
        } else {
            activitySessionName1Binding.view.setVisibility(View.VISIBLE);
        }
    }

    public void setListner() {
        activitySessionName1Binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inback = new Intent(mContext, ClassDeatilScreen.class);
                startActivity(inback);
            }
        });

        activitySessionName1Binding.bookSessionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inback = new Intent(mContext, LoginScreen.class);
                inback.putExtra("flag", "1");
                startActivity(inback);
                Log.d("flag", "" + inback.putExtra("flag", "1"));
            }
        });
        activitySessionName1Binding.pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(
                activitySessionName1Binding.tabLayoutPtm));
        activitySessionName1Binding.tabLayoutPtm.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                activitySessionName1Binding.pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent inback = new Intent(mContext, ClassDeatilScreen.class);
        inback.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(inback);
    }
}
