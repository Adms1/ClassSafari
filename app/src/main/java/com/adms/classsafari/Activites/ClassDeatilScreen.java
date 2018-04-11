package com.adms.classsafari.Activites;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.adms.classsafari.Adapter.ClassDetailAdapter;
import com.adms.classsafari.Adapter.PopularClassListAdapter;
import com.adms.classsafari.AppConstant.ApiHandler;
import com.adms.classsafari.AppConstant.Utils;
import com.adms.classsafari.BottomNavigationViewHelper;
import com.adms.classsafari.Model.ClassDetailModel;
import com.adms.classsafari.Model.MainClassModel;
import com.adms.classsafari.R;
import com.adms.classsafari.databinding.ActivityClassDeatilScreenBinding;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class ClassDeatilScreen extends AppCompatActivity {

    ActivityClassDeatilScreenBinding binding;
    Context mContext;
    ClassDetailAdapter classDetailAdapter;
    ArrayList<String> arrayList;
    ArrayList<String> arrayListPopular;
    public Dialog popularDialog, priceDialog, sortDialog;
    PopularClassListAdapter popularClassListAdapter;
    RecyclerView popularListrcView;
    TextView done;
    CrystalRangeSeekbar rangeSeekbar;
    TextView price_range1_txt, price_range2_txt;
    List<ClassDetailModel> sessionfullDetailList;
    String subjectStr, boardStr, standardStr, streamStr;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_class_deatil_screen);
        mContext = ClassDeatilScreen.this;

        init();
        setListner();
    }

    public void init() {
        subjectStr = getIntent().getStringExtra("subject");
        standardStr = getIntent().getStringExtra("standard");
        streamStr = getIntent().getStringExtra("stream");
        boardStr = getIntent().getStringExtra("board");
        BottomNavigationViewHelper.removeShiftMode(binding.bottomNavigationView);
        callClassDetailApi();
//        binding.subjectTxt.setText(subjectStr);
//        binding.boardTxt.setText("\u2022 "+boardStr);
//        binding.standardTxt.setText("\u2022 "+standardStr);
//        binding.streamTxt.setText("\u2022 "+streamStr);

    }

    public void setListner() {

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_popular:
                        PopularDialog();
                        break;
                    case R.id.action_price:
                        PriceDialog();
                        break;
                    case R.id.action_sort:
                        SortDialog();
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

    public void PopularDialog() {
        popularDialog = new Dialog(mContext, R.style.Theme_Dialog);
        Window window = popularDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        popularDialog.getWindow().getAttributes().verticalMargin = 0.10F;
        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);

        popularDialog.getWindow().setBackgroundDrawableResource(R.drawable.poop_p3);

        popularDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popularDialog.setCancelable(true);
        popularDialog.setContentView(R.layout.dialog_popular);

        popularListrcView = (RecyclerView) popularDialog.findViewById(R.id.popular_list_rcView);
        done = (TextView) popularDialog.findViewById(R.id.done_txt);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popularDialog.dismiss();
            }
        });
        arrayListPopular = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            arrayListPopular.add(String.valueOf(i));
        }

        popularClassListAdapter = new PopularClassListAdapter(mContext, arrayListPopular);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        popularListrcView.setLayoutManager(mLayoutManager);
        popularListrcView.setItemAnimator(new DefaultItemAnimator());
        popularListrcView.setAdapter(popularClassListAdapter);
        popularDialog.show();
    }

    public void PriceDialog() {
        priceDialog = new Dialog(mContext, R.style.Theme_Dialog);
        Window window = priceDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        priceDialog.getWindow().getAttributes().verticalMargin = 0.10F;
        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);

        priceDialog.getWindow().setBackgroundDrawableResource(R.drawable.price_p3);

        priceDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        priceDialog.setCancelable(true);
        priceDialog.setContentView(R.layout.dialog_price);

        done = (TextView) priceDialog.findViewById(R.id.done_txt);
        rangeSeekbar = (CrystalRangeSeekbar) priceDialog.findViewById(R.id.rangeSeekbar);
        price_range1_txt = (TextView) priceDialog.findViewById(R.id.price_range1_txt);
        price_range2_txt = (TextView) priceDialog.findViewById(R.id.price_range2_txt);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priceDialog.dismiss();
            }
        });
        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                price_range1_txt.setText("₹ " + String.valueOf(minValue));
                price_range2_txt.setText("₹ " + String.valueOf(maxValue));
                Log.d("select vlue", "min value" + minValue + "maxvalue" + maxValue);
            }
        });
        rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                price_range1_txt.setText("₹ " + String.valueOf(minValue));
                price_range2_txt.setText("₹ " + String.valueOf(maxValue));
                Log.d("final vlue", "min value" + minValue + "maxvalue" + maxValue);
            }
        });
        priceDialog.show();


    }

    public void SortDialog() {
        sortDialog = new Dialog(mContext, R.style.Theme_Dialog);
        Window window = sortDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        sortDialog.getWindow().getAttributes().verticalMargin = 0.10F;
        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);

        sortDialog.getWindow().setBackgroundDrawableResource(R.drawable.sort_p3);

        sortDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sortDialog.setCancelable(true);
        sortDialog.setContentView(R.layout.dialog_sort);

        done = (TextView) sortDialog.findViewById(R.id.done_txt);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortDialog.dismiss();
            }
        });

        sortDialog.show();
    }


    //Use for get CLass Detail
    public void callClassDetailApi() {
        if (Utils.checkNetwork(mContext)) {
            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_SessionByEmployeeTypeID(getsessionDetail(), new retrofit.Callback<MainClassModel>() {
                @Override
                public void success(MainClassModel sessionInfo, Response response) {
                    Utils.dismissDialog();
                    if (sessionInfo == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (sessionInfo.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (sessionInfo.getSuccess().equalsIgnoreCase("false")) {
                        Utils.dismissDialog();
                        if (sessionInfo.getData() != null) {
                            Utils.ping(mContext, getString(R.string.false_msg));
                        }
                        return;
                    }
                    if (sessionInfo.getSuccess().equalsIgnoreCase("True")) {
                        Utils.dismissDialog();
                        if (sessionInfo.getData() != null) {
                            sessionfullDetailList = sessionInfo.getData();
                            if (sessionfullDetailList.size() > 0) {
                                binding.recViewLinear.setVisibility(View.VISIBLE);
                                binding.noRecordTxt.setVisibility(View.GONE);
                                classDetailAdapter = new ClassDetailAdapter(mContext, sessionfullDetailList);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                                binding.classListRecView.setLayoutManager(mLayoutManager);
                                binding.classListRecView.setItemAnimator(new DefaultItemAnimator());
                                binding.classListRecView.setAdapter(classDetailAdapter);
                            } else {
                                binding.recViewLinear.setVisibility(View.GONE);
                                binding.noRecordTxt.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Utils.dismissDialog();
                    error.printStackTrace();
                    error.getMessage();
                    Utils.ping(mContext, getString(R.string.something_wrong));
                }
            });
        } else {
            Utils.ping(mContext, getString(R.string.internet_connection_error));
        }
    }

    private Map<String, String> getsessionDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("EmployeeTypeID", "1");
        return map;
    }


}
