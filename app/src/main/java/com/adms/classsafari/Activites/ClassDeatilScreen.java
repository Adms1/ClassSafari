package com.adms.classsafari.Activites;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.adms.classsafari.Adapter.ClassDetailAdapter;
import com.adms.classsafari.BottomNavigationViewHelper;
import com.adms.classsafari.R;
import com.adms.classsafari.databinding.ActivityClassDeatilScreenBinding;

import java.util.ArrayList;

public class ClassDeatilScreen extends AppCompatActivity {

    ActivityClassDeatilScreenBinding binding;
    Context mContext;
    TextView sessionName;
    ClassDetailAdapter classDetailAdapter;
    ArrayList<String> arrayList;
    private AlertDialog alertDialogAndroid = null;
    View bottomSheetView;

    BottomSheetDialog bottomSheetDialog;
    BottomSheetBehavior bottomSheetBehavior;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_class_deatil_screen);
        mContext = ClassDeatilScreen.this;

        init();
        setListner();
    }

    public void init() {
        BottomNavigationViewHelper.removeShiftMode(binding.bottomNavigationView);

        arrayList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            arrayList.add(String.valueOf(i));
        }
        classDetailAdapter = new ClassDetailAdapter(mContext, arrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        binding.classListRecView.setLayoutManager(mLayoutManager);
        binding.classListRecView.setItemAnimator(new DefaultItemAnimator());
        binding.classListRecView.setAdapter(classDetailAdapter);


    }

    public void setListner() {

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_popular:
                        SendMessage();
                        break;
                    case R.id.action_price:
                        break;
                    case R.id.action_sort:
                        Toast.makeText(mContext, "SORT", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.action_filter:
                        Toast.makeText(mContext, "FILTER", Toast.LENGTH_LONG).show();
                        break;
                }


                return true;
            }
        });
        binding.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inSearchUser = new Intent(mContext, SearchByUser.class);
                startActivity(inSearchUser);
            }
        });
        binding.searchImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent insession = new Intent(mContext, ClassSearchScreen.class);
                startActivity(insession);
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

    public void SendMessage() {
        bottomSheetView = getLayoutInflater().inflate(R.layout.dialog_popular, null);
        bottomSheetDialog = new BottomSheetDialog(ClassDeatilScreen.this);
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
        bottomSheetBehavior.setBottomSheetCallback(bottomSheetCallback);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                bottomSheetDialog.show();
//        bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
//            @Override
//            public void onShow(DialogInterface dialog) {
//
//            }
//        });
//
//        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//
//            }
//        });
//
//        binding.headerLinear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                bottomSheetDialog.show();
//            }
//        });


    }
    BottomSheetBehavior.BottomSheetCallback bottomSheetCallback =
            new BottomSheetBehavior.BottomSheetCallback(){
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    switch (newState){
                        case BottomSheetBehavior.STATE_COLLAPSED:
                            Toast.makeText(mContext, "Hello", Toast.LENGTH_LONG).show();
                            break;
                        case BottomSheetBehavior.STATE_DRAGGING:
                            Toast.makeText(mContext, "Hello", Toast.LENGTH_LONG).show();
                            break;
                        case BottomSheetBehavior.STATE_EXPANDED:
                            Toast.makeText(mContext, "Hello", Toast.LENGTH_LONG).show();
                            break;
                        case BottomSheetBehavior.STATE_HIDDEN:
                            Toast.makeText(mContext, "Hello", Toast.LENGTH_LONG).show();
                            bottomSheetDialog.dismiss();
                            break;
                        case BottomSheetBehavior.STATE_SETTLING:
                            Toast.makeText(mContext, "Hello", Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Toast.makeText(mContext, "hi", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                }
            };

}
