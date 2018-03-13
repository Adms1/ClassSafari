package com.adms.classsafari.Activites;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.View;

import com.adms.classsafari.Adapter.SelectStudentListAdapter;
import com.adms.classsafari.R;
import com.adms.classsafari.databinding.ActivityAddStudentScreenBinding;

import java.util.ArrayList;

public class AddStudentScreen extends AppCompatActivity {
    ActivityAddStudentScreenBinding addStudentScreenBinding;
    Context mContext;
    SelectStudentListAdapter selectStudentListAdapter;
    ArrayList<String> arrayList;
    String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addStudentScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_student_screen);
        mContext = AddStudentScreen.this;
        flag = getIntent().getStringExtra("flag");
        init();
        setListner();
    }

    public void init() {
        arrayList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            arrayList.add(String.valueOf(i));
        }
        selectStudentListAdapter = new SelectStudentListAdapter(mContext, arrayList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
        addStudentScreenBinding.selectStudentRcView.setLayoutManager(mLayoutManager);
        addStudentScreenBinding.selectStudentRcView.setItemAnimator(new DefaultItemAnimator());
        addStudentScreenBinding.selectStudentRcView.setAdapter(selectStudentListAdapter);
    }

    public void setListner() {
        addStudentScreenBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inback = new Intent(mContext, RegistrationScreen.class);
                inback.putExtra("flag", flag);
                startActivity(inback);
            }
        });
        addStudentScreenBinding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent inaddsession = new Intent(mContext, AddSessionScreen.class);
//                startActivity(inaddsession);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent inback = new Intent(mContext, RegistrationScreen.class);
        inback.putExtra("flag", flag);
        startActivity(inback);
    }
}
