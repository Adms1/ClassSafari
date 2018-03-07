package com.adms.classsafari.Activites;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.adms.classsafari.R;
import com.adms.classsafari.databinding.ActivityRegistrationScreenBinding;

public class RegistrationScreen extends AppCompatActivity {

    ActivityRegistrationScreenBinding familyRegister1Binding;
    Context mContext;
    String selectedValue = "Parents", flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        familyRegister1Binding = DataBindingUtil.setContentView(this, R.layout.activity_registration_screen);
        mContext = RegistrationScreen.this;
        flag = getIntent().getStringExtra("flag");

        init();
        setListner();
    }

    public void init() {
        if (flag.equalsIgnoreCase("1")){
            familyRegister1Binding.teacherChk.setVisibility(View.GONE);
        }else{
            familyRegister1Binding.teacherChk.setVisibility(View.VISIBLE);
        }
    }

    public void setListner() {
        familyRegister1Binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inback = new Intent(mContext, LoginScreen.class);
                inback.putExtra("flag", flag);
                startActivity(inback);
            }
        });
        familyRegister1Binding.statusGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                int radioButtonId = familyRegister1Binding.statusGroup.getCheckedRadioButtonId();
                switch (radioButtonId) {
                    case R.id.parents_chk:
                        selectedValue = familyRegister1Binding.parentsChk.getTag().toString();
                        break;
                    case R.id.teacher_chk:
                        selectedValue = familyRegister1Binding.teacherChk.getTag().toString();
                        break;
                }
            }
        });
        familyRegister1Binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedValue.equalsIgnoreCase("Parents")) {
                    Intent inaddstudent = new Intent(mContext, AddStudentScreen.class);
                    inaddstudent.putExtra("flag", flag);
                    startActivity(inaddstudent);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent inback = new Intent(mContext, LoginScreen.class);
        inback.putExtra("flag", flag);
        startActivity(inback);
    }
}
