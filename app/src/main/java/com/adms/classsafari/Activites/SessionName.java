package com.adms.classsafari.Activites;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adms.classsafari.Adapter.ClassDetailAdapter;
import com.adms.classsafari.Adapter.SessionDetailAdapter;
import com.adms.classsafari.R;
import com.adms.classsafari.databinding.ActivitySessionNameBinding;
import com.adms.classsafari.databinding.ActivitySessionNameNewBinding;

import java.util.ArrayList;

public class SessionName extends AppCompatActivity {
    ActivitySessionNameNewBinding sessionNameBinding;
    Context mContext;
    SessionDetailAdapter sessionDetailAdapter;
    ArrayList<String> arrayList;
    Dialog confimDialog;
    TextView cancel_txt, confirm_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionNameBinding = DataBindingUtil.setContentView(this, R.layout.activity_session_name_new);
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
                ConformSessionDialog();

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

    public void ConformSessionDialog() {
        confimDialog = new Dialog(mContext, R.style.Theme_Dialog);
        Window window = confimDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        confimDialog.getWindow().getAttributes().verticalMargin = 0.10f;
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);

        confimDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);

        confimDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        confimDialog.setCancelable(false);
        confimDialog.setContentView(R.layout.confirm_session_dialog);


        confirm_txt = (TextView) confimDialog.findViewById(R.id.confirm_txt);
        cancel_txt = (TextView) confimDialog.findViewById(R.id.cancel_txt);

        cancel_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confimDialog.dismiss();
            }
        });
        confirm_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inLogin=new Intent(SessionName.this,LoginScreen.class);
                inLogin.putExtra("flag", "1");
                startActivity(inLogin);
            }
        });


        confimDialog.show();


    }
}
