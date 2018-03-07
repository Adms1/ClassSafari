package com.adms.classsafari.Activites;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.adms.classsafari.R;
import com.adms.classsafari.databinding.ActivityLoginScreenBinding;

public class LoginScreen extends AppCompatActivity {

    ActivityLoginScreenBinding loginScreenBinding;
    Context mContext;
    String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_login_screen);
        mContext = LoginScreen.this;
        flag = getIntent().getStringExtra("flag");
//        Log.d("flag",flag);
        setListner();
    }

    public void setListner() {
        loginScreenBinding.registerTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inregister = new Intent(mContext, RegistrationScreen.class);
                inregister.putExtra("flag", flag);
                startActivity(inregister);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (flag.equalsIgnoreCase("0")) {
            Intent inback = new Intent(mContext, SearchByUser.class);
            inback.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(inback);
        } else {
            Intent inback = new Intent(mContext, SessionName.class);
            inback.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(inback);
        }
    }
}
