package com.adms.classsafari.Activites;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.adms.classsafari.Adapter.ClassDetailAdapter;
import com.adms.classsafari.Adapter.SessionDetailAdapter;
import com.adms.classsafari.R;
import com.adms.classsafari.databinding.ActivitySessionNameBinding;

import java.util.ArrayList;

public class SessionName extends AppCompatActivity {
    ActivitySessionNameBinding sessionNameBinding;
    Context mContext;
    SessionDetailAdapter sessionDetailAdapter;
    ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionNameBinding = DataBindingUtil.setContentView(this, R.layout.activity_session_name);
        mContext = SessionName.this;

        init();
        setListner();
    }

    public void init() {
        arrayList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            arrayList.add(String.valueOf(i));
        }
        sessionDetailAdapter = new SessionDetailAdapter(mContext, arrayList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
        sessionNameBinding.sessionListRecView.setLayoutManager(mLayoutManager);
        sessionNameBinding.sessionListRecView.setItemAnimator(new DefaultItemAnimator());
        sessionNameBinding.sessionListRecView.setAdapter(sessionDetailAdapter);
    }

    public void setListner() {
        sessionNameBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inback = new Intent(mContext, ClassDeatilScreen.class);
                startActivity(inback);
            }
        });

        sessionNameBinding.bookSessionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inback = new Intent(mContext, LoginScreen.class);
                inback.putExtra("flag", "1");
                startActivity(inback);
                Log.d("flag",""+inback.putExtra("flag", "1"));
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
