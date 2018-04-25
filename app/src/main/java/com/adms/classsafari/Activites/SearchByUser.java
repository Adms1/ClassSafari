package com.adms.classsafari.Activites;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.adms.classsafari.AppConstant.ApiHandler;
import com.adms.classsafari.AppConstant.AppConfiguration;
import com.adms.classsafari.AppConstant.Utils;
import com.adms.classsafari.Model.Session.SessionDetailModel;
import com.adms.classsafari.R;
import com.adms.classsafari.databinding.ActivitySearchByUserBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class SearchByUser extends AppCompatActivity {
    ActivitySearchByUserBinding searchByUserBinding;
    Context mContext;
    SessionDetailModel dataResponse;
    String selectedSessionNameStr = "", selectedLocationStr = "", sessionName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchByUserBinding = DataBindingUtil.setContentView(this, R.layout.activity_search_by_user);
        mContext = SearchByUser.this;
        init();
        setListner();
    }

    public void init() {
        searchByUserBinding.locationEdt.setText("Ahmedabad");
//        selectedLocationStr = searchByUserBinding.searchClassEdt.getText().toString();
    }

    public void setListner() {
        callSessionListApi();
        searchByUserBinding.letsStudyTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppConfiguration.ClassLocation = searchByUserBinding.locationEdt.getText().toString();
                Intent inClassDetail = new Intent(mContext, ClassSearchScreen.class);
                inClassDetail.putExtra("flag","study");
                inClassDetail.putExtra("withOR","withOR");
                startActivity(inClassDetail);
            }
        });
        searchByUserBinding.searchClassEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchByUserBinding.searchClassEdt.showDropDown();
            }
        });
        searchByUserBinding.searchTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });
        searchByUserBinding.letsPlayTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppConfiguration.ClassLocation = searchByUserBinding.locationEdt.getText().toString();
                Intent inClassDetail = new Intent(mContext, ClassSearchScreen.class);
                inClassDetail.putExtra("flag","play");
                inClassDetail.putExtra("withOR","withOR");
                startActivity(inClassDetail);
            }
        });
        searchByUserBinding.locationEdt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedLocationStr = String.valueOf(adapterView.getItemAtPosition(i));
                searchByUserBinding.searchClassEdt.setText("");
                fillSessionList();

            }
        });
        searchByUserBinding.searchClassEdt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                sessionName = String.valueOf(adapterView.getItemAtPosition(i));
                Log.d("session", sessionName);
                fillCity();

            }
        });
        searchByUserBinding.loginTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inClassDetail = new Intent(mContext, LoginActivity.class);
//                inClassDetail.putExtra("flag", "0");
                startActivity(inClassDetail);
            }
        });
        searchByUserBinding.locationEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchByUserBinding.locationEdt.showDropDown();
            }
        });

    }

    //Use for SessionList
    public void callSessionListApi() {
        if (Utils.checkNetwork(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_SessionList(getSessionListDetail(), new retrofit.Callback<SessionDetailModel>() {
                @Override
                public void success(SessionDetailModel cityInfo, Response response) {
                    Utils.dismissDialog();
                    if (cityInfo == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (cityInfo.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (cityInfo.getSuccess().equalsIgnoreCase("false")) {
                        Utils.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (cityInfo.getSuccess().equalsIgnoreCase("True")) {
                        Utils.dismissDialog();
                        if (cityInfo.getData().size() > 0) {
                            dataResponse = cityInfo;
                            fillCity();
                            fillSessionList();
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

    private Map<String, String> getSessionListDetail() {
        Map<String, String> map = new HashMap<>();
        return map;
    }

    public void fillCity() {
        ArrayList<String> CityName = new ArrayList<String>();

        for (int j = 0; j < dataResponse.getData().size(); j++) {
            CityName.add(dataResponse.getData().get(j).getAddressCity());
        }
        HashSet<String> hashSet = new HashSet<String>();
        hashSet.addAll(CityName);
        CityName.clear();
        CityName.addAll(hashSet);
        ArrayAdapter<String> adapterTerm = new ArrayAdapter<String>(mContext, R.layout.autocomplete_layout, CityName);
        searchByUserBinding.locationEdt.setThreshold(1);
        searchByUserBinding.locationEdt.setAdapter(adapterTerm);
        searchByUserBinding.locationEdt.setSelection(searchByUserBinding.locationEdt.getText().length());
    }

    public void fillSessionList() {
        ArrayList<String> SesisonNameArray = new ArrayList<String>();

        for (int j = 0; j < dataResponse.getData().size(); j++) {
            if (searchByUserBinding.locationEdt.getText().toString().trim().equalsIgnoreCase(dataResponse.getData().get(j).getAddressCity().trim())) {
                SesisonNameArray.add(dataResponse.getData().get(j).getSessionName());
            }
        }
        ArrayAdapter<String> adapterSessionName = new ArrayAdapter<String>(mContext, R.layout.autocomplete_layout, SesisonNameArray);
        searchByUserBinding.searchClassEdt.setThreshold(1);
        searchByUserBinding.searchClassEdt.setAdapter(adapterSessionName);
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

    public void validation() {
        selectedSessionNameStr = searchByUserBinding.searchClassEdt.getText().toString();
        selectedLocationStr = searchByUserBinding.locationEdt.getText().toString();
        AppConfiguration.ClassLocation = selectedLocationStr;
        if (!selectedSessionNameStr.equalsIgnoreCase("") && !selectedLocationStr.equalsIgnoreCase("")) {
            Utils.setPref(mContext, "location", selectedLocationStr);
            Utils.setPref(mContext, "sessionName", selectedSessionNameStr);
            Intent inClassDetail = new Intent(mContext, ClassDeatilScreen.class);
            inClassDetail.putExtra("city",selectedLocationStr);
            inClassDetail.putExtra("sessionName",selectedSessionNameStr);
            inClassDetail.putExtra("SearchBy","1");
            inClassDetail.putExtra("withOR","withOutOR");
            inClassDetail.putExtra("searchType","study");
            startActivity(inClassDetail);
        } else {
            Utils.ping(mContext, getResources().getString(R.string.blank_value));
        }
    }
}
