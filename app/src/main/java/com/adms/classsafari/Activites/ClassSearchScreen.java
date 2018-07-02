package com.adms.classsafari.Activites;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.adms.classsafari.AppConstant.ApiHandler;
import com.adms.classsafari.AppConstant.AppConfiguration;
import com.adms.classsafari.AppConstant.Utils;
import com.adms.classsafari.Model.Session.SessionDetailModel;
import com.adms.classsafari.Model.Session.sessionDataModel;
import com.adms.classsafari.Model.TeacherInfo.TeacherInfoModel;
import com.adms.classsafari.R;
import com.adms.classsafari.databinding.ActivityClassSearchScreenBinding;
import com.adms.classsafari.databinding.ChangePasswordDialogBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class ClassSearchScreen extends AppCompatActivity implements View.OnClickListener {

    ActivityClassSearchScreenBinding classSearchScreenBinding;
    Context mContext;
    SessionDetailModel dataResponse;
    List<sessionDataModel> datafilterResponse;
    String flag, selectedSessionStr = "", selectedSessionCityStr, sessionCapacityStr, selectedSessionRegionStr = "",
            sessionTypeStr, genderStr = "", RegionName,
            searchfront, locationStr,
            classNameStr, firsttimesearch, boardStr, standardStr, streamStr, lessionNameStr, SearchPlaystudy;

    //Use for Menu Dialog
    String passWordStr, confirmpassWordStr, currentpasswordStr, wheretocometypeStr;
    Dialog menuDialog, changeDialog;
    Button btnMyReport, btnMySession, btnChangePassword, btnaddChild, btnLogout, btnmyfamily;
    TextView userNameTxt;
    ChangePasswordDialogBinding changePasswordDialogBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        classSearchScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_class_search_screen);
        mContext = ClassSearchScreen.this;
        flag = getIntent().getStringExtra("flag");
        sessionTypeStr = getIntent().getStringExtra("sessionType");
        searchfront = getIntent().getStringExtra("searchfront");
        locationStr = getIntent().getStringExtra("city");
        classNameStr = getIntent().getStringExtra("sessionName");
        firsttimesearch = getIntent().getStringExtra("firsttimesearch");
        boardStr = getIntent().getStringExtra("board");
        standardStr = getIntent().getStringExtra("standard");
        streamStr = getIntent().getStringExtra("stream");
        RegionName = getIntent().getStringExtra("RegionName");
        SearchPlaystudy = getIntent().getStringExtra("SearchPlaystudy");
        lessionNameStr = getIntent().getStringExtra("lessionName");
        setTypeface();
        init();
        setListner();
    }
    public void setTypeface() {
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "font/TitilliumWeb-Regular.ttf");

        classSearchScreenBinding.activityName.setTypeface(custom_font);
        classSearchScreenBinding.searchTxt.setTypeface(custom_font);
        classSearchScreenBinding.searchAutoTxt.setTypeface(custom_font);
        classSearchScreenBinding.regionNameTxt.setTypeface(custom_font);
        classSearchScreenBinding.classAutoTxt.setTypeface(custom_font);
        classSearchScreenBinding.boardAutoTxt.setTypeface(custom_font);
        classSearchScreenBinding.standardAutoTxt.setTypeface(custom_font);
        classSearchScreenBinding.streamAutoTxt.setTypeface(custom_font);
        classSearchScreenBinding.sessionAutoTxt.setTypeface(custom_font);
        classSearchScreenBinding.searchBtn.setTypeface(custom_font);
    }

    public void init() {

        if (!Utils.getPref(mContext, "LoginType").equalsIgnoreCase("Family")) {
            classSearchScreenBinding.menu.setVisibility(View.GONE);
        } else {
            classSearchScreenBinding.menu.setVisibility(View.VISIBLE);
        }

        if (SearchPlaystudy.equalsIgnoreCase("2")) {
            classSearchScreenBinding.linearBg.setBackgroundResource(R.drawable.play_bg);
            classSearchScreenBinding.boardAutoTxt.setVisibility(View.GONE);
            classSearchScreenBinding.standardAutoTxt.setVisibility(View.GONE);
            classSearchScreenBinding.streamAutoTxt.setVisibility(View.GONE);
            classSearchScreenBinding.sessionAutoTxt.setHint("Activity");
            classSearchScreenBinding.activityName.setText("SEARCH AN ACTIVITY");
        } else {
            classSearchScreenBinding.linearBg.setBackgroundResource(R.drawable.study_bg);
            classSearchScreenBinding.boardAutoTxt.setVisibility(View.VISIBLE);
            classSearchScreenBinding.standardAutoTxt.setVisibility(View.VISIBLE);
            classSearchScreenBinding.streamAutoTxt.setVisibility(View.VISIBLE);
            classSearchScreenBinding.sessionAutoTxt.setHint("Subject");
            classSearchScreenBinding.activityName.setText("SEARCH A CLASS");
        }
        if (!firsttimesearch.equalsIgnoreCase("true")) {
            genderStr = getIntent().getStringExtra("gender");
            if (!classNameStr.equalsIgnoreCase("")) {
                classSearchScreenBinding.classAutoTxt.setText(classNameStr);
                selectedSessionStr = classNameStr;
            }
            if (!lessionNameStr.equalsIgnoreCase("")) {
                classSearchScreenBinding.sessionAutoTxt.setText(lessionNameStr);
            }
            if (!boardStr.equalsIgnoreCase("")) {
                classSearchScreenBinding.boardAutoTxt.setText(boardStr);
            }
            if (!standardStr.equalsIgnoreCase("")) {
                classSearchScreenBinding.standardAutoTxt.setText(standardStr);
            }
            if (!streamStr.equalsIgnoreCase("")) {
                classSearchScreenBinding.streamAutoTxt.setText(streamStr);
            }
            if (!RegionName.equalsIgnoreCase("")) {
                classSearchScreenBinding.regionNameTxt.setText(RegionName);
            }
        }
        callSessionListApi();


    }

    public void setListner() {
        classSearchScreenBinding.searchAutoTxt.setOnClickListener(this);
        classSearchScreenBinding.back.setOnClickListener(this);
        classSearchScreenBinding.streamAutoTxt.setOnClickListener(this);
        classSearchScreenBinding.standardAutoTxt.setOnClickListener(this);
        classSearchScreenBinding.boardAutoTxt.setOnClickListener(this);
        classSearchScreenBinding.classAutoTxt.setOnClickListener(this);
        classSearchScreenBinding.sessionAutoTxt.setOnClickListener(this);
        classSearchScreenBinding.searchAutoTxt.setOnClickListener(this);
        classSearchScreenBinding.searchBtn.setOnClickListener(this);
        classSearchScreenBinding.menu.setOnClickListener(this);

        classSearchScreenBinding.classAutoTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                classNameStr = String.valueOf(adapterView.getItemAtPosition(i));
                Log.d("SessionName", classNameStr);
                fillBoard();
                fillStandard();
                fillStream();
                fillLession();
            }
        });
        classSearchScreenBinding.searchAutoTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedSessionCityStr = String.valueOf(adapterView.getItemAtPosition(i));
                classSearchScreenBinding.classAutoTxt.setText("");
                classSearchScreenBinding.classAutoTxt.setText("");
                classSearchScreenBinding.boardAutoTxt.setText("");
                classSearchScreenBinding.standardAutoTxt.setText("");
                classSearchScreenBinding.streamAutoTxt.setText("");
                fillRegion();
                //fillSession();
            }
        });
        classSearchScreenBinding.regionNameTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedSessionRegionStr = String.valueOf(adapterView.getItemAtPosition(i));
                classSearchScreenBinding.sessionAutoTxt.setText("");
                classSearchScreenBinding.classAutoTxt.setText("");
                classSearchScreenBinding.boardAutoTxt.setText("");
                classSearchScreenBinding.standardAutoTxt.setText("");
                classSearchScreenBinding.streamAutoTxt.setText("");
               // fillSession();
            }
        });
        classSearchScreenBinding.regionNameTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    classSearchScreenBinding.sessionAutoTxt.setText("");
                    classSearchScreenBinding.classAutoTxt.setText("");
                    classSearchScreenBinding.boardAutoTxt.setText("");
                    classSearchScreenBinding.standardAutoTxt.setText("");
                    classSearchScreenBinding.streamAutoTxt.setText("");
                } else {
                     //fillSession();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        classSearchScreenBinding.searchAutoTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    classSearchScreenBinding.sessionAutoTxt.setText("");
                    classSearchScreenBinding.classAutoTxt.setText("");
                    classSearchScreenBinding.boardAutoTxt.setText("");
                    classSearchScreenBinding.standardAutoTxt.setText("");
                    classSearchScreenBinding.streamAutoTxt.setText("");
                } else {
                    fillRegion();
                    fillSession();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        classSearchScreenBinding.classAutoTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    fillLession();
                    fillBoard();
                    fillStandard();
                    fillStream();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        classSearchScreenBinding.streamAutoTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    validationSearch();
                }

                return false;
            }
        });
        classSearchScreenBinding.sessionAutoTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    validationSearch();
                }
                return false;
            }
        });
        classSearchScreenBinding.classAutoTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    validationSearch();
                }
                return false;
            }
        });
        classSearchScreenBinding.boardAutoTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    validationSearch();
                }
                return false;
            }
        });
        classSearchScreenBinding.standardAutoTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    validationSearch();
                }
                return false;
            }
        });
        classSearchScreenBinding.streamAutoTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    validationSearch();
                }
                return false;
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

    //Use for SessionList
    public void callSessionListApi() {
        if (Utils.checkNetwork(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_SessionList_Automplated(getSessionListDetail(), new retrofit.Callback<SessionDetailModel>() {
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
                            datafilterResponse = new ArrayList<sessionDataModel>();
                            if (flag.equalsIgnoreCase("1")) {
                                for (int i = 0; i < dataResponse.getData().size(); i++) {
                                    if (dataResponse.getData().get(i).getCoachTypeID().equalsIgnoreCase(flag)) {
                                        datafilterResponse.add(dataResponse.getData().get(i));
                                    }
                                }
                            } else if (flag.equalsIgnoreCase("2")) {
                                for (int i = 0; i < dataResponse.getData().size(); i++) {
                                    if (dataResponse.getData().get(i).getCoachTypeID().equalsIgnoreCase(flag)) {
                                        datafilterResponse.add(dataResponse.getData().get(i));
                                    }
                                }
                            } else {
                                for (int i = 0; i < dataResponse.getData().size(); i++) {
                                    datafilterResponse.add(dataResponse.getData().get(i));
                                }
                            }
                            classSearchScreenBinding.searchAutoTxt.setText(AppConfiguration.ClassLocation);
                            fillCity();
                            fillRegion();
//                            fillSession();
                            fillLession();
                            fillBoard();
                            fillStandard();
                            fillStream();
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
        } else

        {
            Utils.ping(mContext, getString(R.string.internet_connection_error));
        }

    }

    private Map<String, String> getSessionListDetail() {
        Map<String, String> map = new HashMap<>();
        return map;
    }

    public void fillSession() {
        selectedSessionCityStr = classSearchScreenBinding.searchAutoTxt.getText().toString();
        selectedSessionRegionStr = classSearchScreenBinding.regionNameTxt.getText().toString();
        ArrayList<String> SesisonNameArray = new ArrayList<String>();
        for (int j = 0; j < datafilterResponse.size(); j++) {
            if (!flag.equalsIgnoreCase("")) {
                if (!selectedSessionRegionStr.equalsIgnoreCase("")) {
                    if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr.trim()) &&
                            datafilterResponse.get(j).getCoachTypeID().equalsIgnoreCase(flag) &&
                            datafilterResponse.get(j).getRegionName().equalsIgnoreCase(selectedSessionRegionStr)) {
                        SesisonNameArray.add(datafilterResponse.get(j).getSessionName());
                    }
                } else {
                    if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr.trim()) &&
                            datafilterResponse.get(j).getCoachTypeID().equalsIgnoreCase(flag)) {
                        SesisonNameArray.add(datafilterResponse.get(j).getSessionName());
                    }
                }

            } else {
                if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr.trim())) {
                    SesisonNameArray.add(datafilterResponse.get(j).getSessionName());
                }
            }
        }
        HashSet<String> hashSet = new HashSet<String>();
        hashSet.addAll(SesisonNameArray);
        SesisonNameArray.clear();
        SesisonNameArray.addAll(hashSet);
        ArrayAdapter<String> adapterSessionName = new ArrayAdapter<String>(mContext, R.layout.autocomplete_layout, SesisonNameArray);
        classSearchScreenBinding.classAutoTxt.setThreshold(1);
        classSearchScreenBinding.classAutoTxt.setAdapter(adapterSessionName);

    }

    public void fillBoard() {
        selectedSessionStr = classSearchScreenBinding.classAutoTxt.getText().toString();
        ArrayList<String> BoardName = new ArrayList<String>();
        for (int j = 0; j < datafilterResponse.size(); j++) {
            if (selectedSessionStr.equalsIgnoreCase("")) {
                if (flag.equalsIgnoreCase("1")) {
                    if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr.trim()) &&
                            datafilterResponse.get(j).getCoachTypeID().equalsIgnoreCase("1")) {
                        BoardName.add(datafilterResponse.get(j).getBoard());
                    }
                } else if (flag.equalsIgnoreCase("2")) {
                    if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr.trim()) &&
                            datafilterResponse.get(j).getCoachTypeID().equalsIgnoreCase("2")) {
                        BoardName.add(datafilterResponse.get(j).getBoard());
                    }
                } else {
                    if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr.trim())) {
                        BoardName.add(datafilterResponse.get(j).getBoard());
                    }
                }

            } else {
                if (flag.equalsIgnoreCase("1")) {
                    if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr.trim()) &&
                            datafilterResponse.get(j).getSessionName().equalsIgnoreCase(selectedSessionStr.trim()) &&
                            datafilterResponse.get(j).getCoachTypeID().equalsIgnoreCase("1")) {
                        BoardName.add(datafilterResponse.get(j).getBoard());
                    }
                } else if (flag.equalsIgnoreCase("2")) {
                    if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr.trim()) &&
                            datafilterResponse.get(j).getSessionName().equalsIgnoreCase(selectedSessionStr.trim()) &&
                            datafilterResponse.get(j).getCoachTypeID().equalsIgnoreCase("2")) {
                        BoardName.add(datafilterResponse.get(j).getBoard());
                    }
                } else {
                    if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr.trim()) &&
                            datafilterResponse.get(j).getSessionName().equalsIgnoreCase(selectedSessionStr.trim())) {
                        BoardName.add(datafilterResponse.get(j).getBoard());
                    }
                }

            }

        }
        HashSet<String> hashSet = new HashSet<String>();
        hashSet.addAll(BoardName);
        BoardName.clear();
        BoardName.addAll(hashSet);
        ArrayAdapter<String> adapterTerm = new ArrayAdapter<String>(mContext, R.layout.autocomplete_layout, BoardName);
        classSearchScreenBinding.boardAutoTxt.setThreshold(1);
        classSearchScreenBinding.boardAutoTxt.setAdapter(adapterTerm);

    }

    public void fillStandard() {
        selectedSessionStr = classSearchScreenBinding.classAutoTxt.getText().toString();
        ArrayList<String> StandardName = new ArrayList<String>();
        for (int j = 0; j < datafilterResponse.size(); j++) {
            if (selectedSessionStr.equalsIgnoreCase("")) {
                if (flag.equalsIgnoreCase("1")) {
                    if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr.trim()) &&
                            datafilterResponse.get(j).getCoachTypeID().equalsIgnoreCase("1")) {
                        StandardName.add(datafilterResponse.get(j).getStandard());
                    }
                } else if (flag.equalsIgnoreCase("2")) {
                    if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr.trim()) &&
                            datafilterResponse.get(j).getCoachTypeID().equalsIgnoreCase("2")) {
                        StandardName.add(datafilterResponse.get(j).getStandard());
                    }
                } else {
                    if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr.trim())) {
                        StandardName.add(datafilterResponse.get(j).getStandard());
                    }
                }

            } else {
                if (flag.equalsIgnoreCase("1")) {
                    if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr.trim()) &&
                            datafilterResponse.get(j).getCoachTypeID().equalsIgnoreCase("1") &&
                            datafilterResponse.get(j).getSessionName().equalsIgnoreCase(selectedSessionStr.trim())) {
                        StandardName.add(datafilterResponse.get(j).getStandard());
                    }
                } else if (flag.equalsIgnoreCase("2")) {
                    if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr.trim()) &&
                            datafilterResponse.get(j).getCoachTypeID().equalsIgnoreCase("2") &&
                            datafilterResponse.get(j).getSessionName().equalsIgnoreCase(selectedSessionStr.trim())) {
                        StandardName.add(datafilterResponse.get(j).getStandard());
                    }
                } else {
                    if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr.trim()) &&
                            datafilterResponse.get(j).getSessionName().equalsIgnoreCase(selectedSessionStr.trim())) {
                        StandardName.add(datafilterResponse.get(j).getStandard());
                    }
                }
            }
        }
        HashSet<String> hashSet = new HashSet<String>();
        hashSet.addAll(StandardName);
        StandardName.clear();
        StandardName.addAll(hashSet);
        ArrayAdapter<String> adapterTerm = new ArrayAdapter<String>(mContext, R.layout.autocomplete_layout, StandardName);
        classSearchScreenBinding.standardAutoTxt.setThreshold(1);
        classSearchScreenBinding.standardAutoTxt.setAdapter(adapterTerm);
    }

    public void fillStream() {
        selectedSessionStr = classSearchScreenBinding.classAutoTxt.getText().toString();
        ArrayList<String> StreamName = new ArrayList<String>();
        for (int j = 0; j < datafilterResponse.size(); j++) {
            if (selectedSessionStr.equalsIgnoreCase("")) {
                if (flag.equalsIgnoreCase("1")) {
                    if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr.trim()) &&
                            datafilterResponse.get(j).getCoachTypeID().equalsIgnoreCase("1")) {
                        StreamName.add(datafilterResponse.get(j).getStream());
                    }
                } else if (flag.equalsIgnoreCase("2")) {
                    if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr.trim()) &&
                            datafilterResponse.get(j).getCoachTypeID().equalsIgnoreCase("2")) {
                        StreamName.add(datafilterResponse.get(j).getStream());
                    }
                } else {
                    if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr.trim())) {
                        StreamName.add(datafilterResponse.get(j).getStream());
                    }
                }
            } else {
                if (flag.equalsIgnoreCase("1")) {
                    if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr.trim()) &&
                            datafilterResponse.get(j).getCoachTypeID().equalsIgnoreCase("1")&&
                            datafilterResponse.get(j).getSessionName().equalsIgnoreCase(selectedSessionStr.trim())) {
                        StreamName.add(datafilterResponse.get(j).getStream());
                    }
                } else if(flag.equalsIgnoreCase("2")){
                    if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr.trim()) &&
                            datafilterResponse.get(j).getCoachTypeID().equalsIgnoreCase("2")&&
                            datafilterResponse.get(j).getSessionName().equalsIgnoreCase(selectedSessionStr.trim())) {
                        StreamName.add(datafilterResponse.get(j).getStream());
                    }
                }else {
                    if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr.trim()) &&
                            datafilterResponse.get(j).getSessionName().equalsIgnoreCase(selectedSessionStr.trim())) {
                        StreamName.add(datafilterResponse.get(j).getStream());
                    }
                }
            }
        }
        HashSet<String> hashSet = new HashSet<String>();
        hashSet.addAll(StreamName);
        StreamName.clear();
        StreamName.addAll(hashSet);
        ArrayAdapter<String> adapterTerm = new ArrayAdapter<String>(mContext, R.layout.autocomplete_layout, StreamName);
        classSearchScreenBinding.streamAutoTxt.setThreshold(1);
        classSearchScreenBinding.streamAutoTxt.setAdapter(adapterTerm);
    }

    public void fillLession() {
        selectedSessionStr = classSearchScreenBinding.classAutoTxt.getText().toString();
        ArrayList<String> LessionNameArray = new ArrayList<String>();

        for (int j = 0; j < datafilterResponse.size(); j++) {
            if (selectedSessionStr.trim().equalsIgnoreCase("")) {
                if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr.trim())) {
                    LessionNameArray.add(datafilterResponse.get(j).getLessionTypeName().trim());
                }

            } else {
                if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr.trim()) &&
                        datafilterResponse.get(j).getSessionName().trim().equalsIgnoreCase(selectedSessionStr.trim())) {
                    LessionNameArray.add(datafilterResponse.get(j).getLessionTypeName().trim());
                }
            }
        }
        HashSet<String> hashSet = new HashSet<String>();
        hashSet.addAll(LessionNameArray);
        LessionNameArray.clear();
        LessionNameArray.addAll(hashSet);
        ArrayAdapter<String> adapterSessionName = new ArrayAdapter<String>(mContext, R.layout.autocomplete_layout, LessionNameArray);
        classSearchScreenBinding.classAutoTxt.setThreshold(1);
        classSearchScreenBinding.classAutoTxt.setAdapter(adapterSessionName);
    }

    public void fillCity() {
        ArrayList<String> CityName = new ArrayList<String>();

        for (int j = 0; j < datafilterResponse.size(); j++) {
            CityName.add(datafilterResponse.get(j).getAddressCity());
        }
        HashSet<String> hashSet = new HashSet<String>();
        hashSet.addAll(CityName);
        CityName.clear();
        CityName.addAll(hashSet);
        ArrayAdapter<String> adapterTerm = new ArrayAdapter<String>(mContext, R.layout.autocomplete_layout, CityName);
        classSearchScreenBinding.searchAutoTxt.setThreshold(1);
        classSearchScreenBinding.searchAutoTxt.setAdapter(adapterTerm);
    }

    public void fillRegion() {
        ArrayList<String> RegionName = new ArrayList<String>();

        for (int j = 0; j < datafilterResponse.size(); j++) {
            RegionName.add(datafilterResponse.get(j).getRegionName());
        }
        HashSet<String> hashSet = new HashSet<String>();
        hashSet.addAll(RegionName);
        RegionName.clear();
        RegionName.addAll(hashSet);
        ArrayAdapter<String> adapterTerm = new ArrayAdapter<String>(mContext, R.layout.autocomplete_layout, RegionName);
        classSearchScreenBinding.regionNameTxt.setThreshold(1);
        classSearchScreenBinding.regionNameTxt.setAdapter(adapterTerm);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_btn:
                validationSearch();
                break;
            case R.id.back:
                Intent intent = new Intent(mContext, ClassDeatilScreen.class);
                intent.putExtra("SearchPlaystudy", SearchPlaystudy);
                intent.putExtra("city", locationStr);
                intent.putExtra("sessionName", classNameStr);
                startActivity(intent);
                break;
            case R.id.stream_auto_txt:
                classSearchScreenBinding.streamAutoTxt.showDropDown();
                break;
            case R.id.standard_auto_txt:
                classSearchScreenBinding.standardAutoTxt.showDropDown();
                break;
            case R.id.board_auto_txt:
                classSearchScreenBinding.boardAutoTxt.showDropDown();
                break;
            case R.id.class_auto_txt:
//                classSearchScreenBinding.classAutoTxt.showDropDown();
                break;
            case R.id.session_auto_txt:
//                classSearchScreenBinding.sessionAutoTxt.showDropDown();
                break;
            case R.id.search_auto_txt:
//                classSearchScreenBinding.searchAutoTxt.showDropDown();
                break;
            case R.id.menu:
                menuDialog();
                break;
        }
    }

    public void validationSearch() {
        if (!classSearchScreenBinding.searchAutoTxt.getText().toString().equalsIgnoreCase("")) { /**&&
         !classSearchScreenBinding.sessionAutoTxt.getText().toString().equalsIgnoreCase("")) {**/
//                    setValueModel();
            Intent inSearchUser = new Intent(mContext, ClassDeatilScreen.class);
            inSearchUser.putExtra("city", classSearchScreenBinding.searchAutoTxt.getText().toString());
            inSearchUser.putExtra("sessionName", classSearchScreenBinding.classAutoTxt.getText().toString());
            inSearchUser.putExtra("lessionName", classSearchScreenBinding.sessionAutoTxt.getText().toString());
            inSearchUser.putExtra("board", classSearchScreenBinding.boardAutoTxt.getText().toString());
            inSearchUser.putExtra("standard", classSearchScreenBinding.standardAutoTxt.getText().toString());
            inSearchUser.putExtra("stream", classSearchScreenBinding.streamAutoTxt.getText().toString());
            inSearchUser.putExtra("SearchPlaystudy", SearchPlaystudy);
            inSearchUser.putExtra("gender", genderStr);
            inSearchUser.putExtra("searchfront", searchfront);
            inSearchUser.putExtra("RegionName", classSearchScreenBinding.regionNameTxt.getText().toString());
            inSearchUser.putExtra("firsttimesearch", "false");
            inSearchUser.putExtra("teacherName",classSearchScreenBinding.teacherAutoTxt.getText().toString());
            startActivity(inSearchUser);
        } else {
            classSearchScreenBinding.sessionAutoTxt.setError("Please Enter Subject.");
        }
    }

    public void menuDialog() {
        menuDialog = new Dialog(mContext, R.style.Theme_Dialog);
        Window window = menuDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        menuDialog.getWindow().getAttributes().verticalMargin = 0.07F;
        wlp.gravity = Gravity.TOP;
        window.setAttributes(wlp);

        menuDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        menuDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        menuDialog.setCanceledOnTouchOutside(true);
//        menuDialog.setContentView(menuBinding.getRoot());
        menuDialog.setContentView(R.layout.layout_menu);

        btnMyReport = (Button) menuDialog.findViewById(R.id.btnMyReport);
        btnMySession = (Button) menuDialog.findViewById(R.id.btnMySession);
        btnChangePassword = (Button) menuDialog.findViewById(R.id.btnChangePassword);
        btnaddChild = (Button) menuDialog.findViewById(R.id.btnaddChild);
        btnLogout = (Button) menuDialog.findViewById(R.id.btnLogout);
        btnmyfamily = (Button) menuDialog.findViewById(R.id.btnmyfamily);

        userNameTxt = (TextView) menuDialog.findViewById(R.id.user_name_txt);
        userNameTxt.setText(Utils.getPref(mContext, "RegisterUserName"));

        btnMyReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imyaccount = new Intent(mContext, MyAccountActivity.class);
                imyaccount.putExtra("wheretocometype", "session");
                startActivity(imyaccount);
            }
        });
        btnMySession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent isession = new Intent(mContext, MySession.class);
                isession.putExtra("wheretocometype", "session");
                startActivity(isession);
                menuDialog.dismiss();
            }
        });
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuDialog.dismiss();
                changePasswordDialog();
            }
        });
        btnmyfamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, FamilyListActivity.class);
                intent.putExtra("froncontanct", "true");
                intent.putExtra("wheretocometype", "session");
                intent.putExtra("familyNameStr", Utils.getPref(mContext, "RegisterUserName"));
                intent.putExtra("familyID", Utils.getPref(mContext, "coachTypeID"));
                startActivity(intent);
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuDialog.dismiss();
                new android.support.v7.app.AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AlertDialogTheme))
                        .setCancelable(false)
//                        .setTitle("Logout")
//                        .setIcon(mContext.getResources().getDrawable(R.drawable.safari))
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
//                        .setIcon(R.drawable.safari)
                        .show();
            }
        });
        menuDialog.show();
    }

    public void changePasswordDialog() {
        changePasswordDialogBinding = DataBindingUtil.
                inflate(LayoutInflater.from(mContext), R.layout.change_password_dialog, (ViewGroup) classSearchScreenBinding.getRoot(), false);

        changeDialog = new Dialog(mContext, R.style.Theme_Dialog);
        Window window = changeDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        changeDialog.getWindow().getAttributes().verticalMargin = 0.0f;
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);

        changeDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);

        changeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        changeDialog.setCancelable(false);
        changeDialog.setContentView(changePasswordDialogBinding.getRoot());

        changePasswordDialogBinding.changepwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentpasswordStr = changePasswordDialogBinding.edtcurrentpassword.getText().toString();
                confirmpassWordStr = changePasswordDialogBinding.edtconfirmpassword.getText().toString();
                passWordStr = changePasswordDialogBinding.edtnewpassword.getText().toString();
                if (currentpasswordStr.equalsIgnoreCase(Utils.getPref(mContext, "Password"))) {
                    if (!passWordStr.equalsIgnoreCase("") && passWordStr.length() >= 4 && passWordStr.length() <= 8) {
                        if (passWordStr.equalsIgnoreCase(confirmpassWordStr)) {
                            callChangePasswordApi();
                        } else {
                            changePasswordDialogBinding.edtconfirmpassword.setError("Confirm Password does not match.");
                        }
                    } else {
                        changePasswordDialogBinding.edtnewpassword.setError("Password must be 4-8 Characters.");
                        changePasswordDialogBinding.edtnewpassword.setText("");
                        changePasswordDialogBinding.edtconfirmpassword.setText("");
                    }
                } else {
                    changePasswordDialogBinding.edtcurrentpassword.setError("Password does not match to current password.");
                }


            }
        });
        changePasswordDialogBinding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDialog.dismiss();
            }
        });

        changeDialog.show();

    }

    //USe for Change Password
    public void callChangePasswordApi() {
        if (Utils.isNetworkConnected(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_Change_Password(getChangePasswordDetail(), new retrofit.Callback<TeacherInfoModel>() {
                @Override
                public void success(TeacherInfoModel forgotInfoModel, Response response) {
                    Utils.dismissDialog();
                    if (forgotInfoModel == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (forgotInfoModel.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (forgotInfoModel.getSuccess().equalsIgnoreCase("false")) {
                        Utils.ping(mContext, "Please Enter Valid Password.");
                        return;
                    }
                    if (forgotInfoModel.getSuccess().equalsIgnoreCase("True")) {
                        Utils.ping(mContext, getResources().getString(R.string.changPassword));
                        Utils.setPref(mContext, "Password", passWordStr);
                        changeDialog.dismiss();
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

    private Map<String, String> getChangePasswordDetail() {

        Map<String, String> map = new HashMap<>();
        map.put("EmailAddress", Utils.getPref(mContext, "RegisterEmail"));
        map.put("Password", passWordStr);
        return map;
    }

}
