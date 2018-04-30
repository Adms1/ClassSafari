package com.adms.classsafari.Activites;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupWindow;

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
    private PopupWindow popupWindow;
    Button btnMyReport;

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
        if (!Utils.getPref(mContext, "RegisterUserName").equalsIgnoreCase("")) {
            if (Utils.getPref(mContext, "LoginType").equalsIgnoreCase("Coach")) {
                Intent iDash = new Intent(mContext, DashBoardActivity.class);
                iDash.putExtra("frontLogin", "beforeLogin");
                startActivity(iDash);
            }
            searchByUserBinding.logout.setVisibility(View.VISIBLE);
            searchByUserBinding.loginTxt.setText(Html.fromHtml("Logged as " + "<u> <b>" + Utils.getPref(mContext, "RegisterUserName") + "</u></b>"));

        } else {
            searchByUserBinding.loginTxt.setText(Html.fromHtml("<u><b>Login<u></b>"));
            searchByUserBinding.logout.setVisibility(View.GONE);
        }
    }

    public void setListner() {
        callSessionListApi();
        searchByUserBinding.letsStudyTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppConfiguration.ClassLocation = searchByUserBinding.locationEdt.getText().toString();
                Intent inClassDetail = new Intent(mContext, ClassSearchScreen.class);
                inClassDetail.putExtra("flag", "study");
                inClassDetail.putExtra("withOR", "withOR");
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
                inClassDetail.putExtra("flag", "play");
                inClassDetail.putExtra("withOR", "withOR");
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
                if (!Utils.getPref(mContext, "RegisterUserName").equalsIgnoreCase("")) {
                    PopupWindow popupwindow_obj = popupDisplay();
                    popupwindow_obj.showAsDropDown(searchByUserBinding.loginTxt, 0, 0);
                } else {
                    Intent inClassDetail = new Intent(mContext, LoginActivity.class);
                    inClassDetail.putExtra("frontLogin", "beforeLogin");
                    startActivity(inClassDetail);
                }
            }
        });
        searchByUserBinding.locationEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchByUserBinding.locationEdt.showDropDown();
            }
        });
        searchByUserBinding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AppTheme))
                        .setCancelable(false)
                        .setTitle("Logout")
                        .setIcon(mContext.getResources().getDrawable(R.drawable.safari))
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Utils.setPref(mContext, "coachID", "");
                                Utils.setPref(mContext, "coachTypeID", "");
                                Utils.setPref(mContext, "RegisterUserName", "");
                                Utils.setPref(mContext, "RegisterEmail", "");
                                Utils.setPref(mContext, "LoginType", "");
                                Utils.setPref(mContext, "Password", "");
                                Utils.setPref(mContext, "FamilyID", "");
                                Utils.setPref(mContext, "location", "");
                                Utils.setPref(mContext, "sessionName", "");
                                Intent intentLogin = new Intent(mContext, SearchByUser.class);
                                intentLogin.putExtra("frontLogin", "beforeLogin");
                                startActivity(intentLogin);
                                finish();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing

                            }
                        })
                        .setIcon(R.drawable.safari)
                        .show();
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
            inClassDetail.putExtra("city", selectedLocationStr);
            inClassDetail.putExtra("sessionName", selectedSessionNameStr);
            inClassDetail.putExtra("SearchBy", "1");
            inClassDetail.putExtra("withOR", "withOutOR");
            inClassDetail.putExtra("searchType", "study");
            startActivity(inClassDetail);
        } else {
            Utils.ping(mContext, getResources().getString(R.string.blank_value));
        }
    }

    public PopupWindow popupDisplay() {

        popupWindow = new PopupWindow(this);

        // inflate your layout or dynamically add view
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.layout_menu, null);

        btnMyReport = (Button) view.findViewById(R.id.btnMyReport);
        btnMyReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imyaccount = new Intent(mContext, MyAccountActivity.class);
                startActivity(imyaccount);
            }
        });

        popupWindow.setFocusable(true);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        popupWindow.setContentView(view);

        return popupWindow;
    }

}
