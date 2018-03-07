package com.adms.classsafari.Activites;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.adms.classsafari.R;
import com.adms.classsafari.databinding.ActivitySearchByUserBinding;

public class SearchByUser extends AppCompatActivity {
    ActivitySearchByUserBinding searchByUserBinding;
    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchByUserBinding = DataBindingUtil.setContentView(this, R.layout.activity_search_by_user);
        mContext = SearchByUser.this;
        init();
        setListner();
    }

    public void init() {

    }

    public void setListner() {
        searchByUserBinding.letsStudyTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inClassDetail = new Intent(mContext, ClassSearchScreen.class);
                startActivity(inClassDetail);
            }
        });

        searchByUserBinding.letsPlayTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inClassDetail = new Intent(mContext, ClassSearchScreen.class);
                startActivity(inClassDetail);
            }
        });
        searchByUserBinding.locationEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inClassDetail = new Intent(mContext, ClassSearchScreen.class);
                startActivity(inClassDetail);
            }
        });
        searchByUserBinding.searchClassEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inClassDetail = new Intent(mContext, ClassDeatilScreen.class);
                startActivity(inClassDetail);
            }
        });
        searchByUserBinding.loginTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inClassDetail = new Intent(mContext, LoginScreen.class);
                inClassDetail.putExtra("flag","0");
                startActivity(inClassDetail);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);
    }
}
