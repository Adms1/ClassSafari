package com.adms.classsafari.Activites;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.adms.classsafari.R;
import com.adms.classsafari.databinding.ActivityClassSearchScreenBinding;

public class ClassSearchScreen extends AppCompatActivity {

    ActivityClassSearchScreenBinding classSearchScreenBinding;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        classSearchScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_class_search_screen);
        mContext = ClassSearchScreen.this;

        setListner();
    }

    public void setListner() {
        classSearchScreenBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inSearchUser = new Intent(mContext, SearchByUser.class);
                startActivity(inSearchUser);
            }
        });

        classSearchScreenBinding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inSearchUser = new Intent(mContext, ClassDeatilScreen.class);
                startActivity(inSearchUser);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent inback = new Intent(mContext, SearchByUser.class);
        inback.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(inback);
    }
}
