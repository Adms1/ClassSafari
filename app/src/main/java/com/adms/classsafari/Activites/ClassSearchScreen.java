package com.adms.classsafari.Activites;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;

import com.adms.classsafari.AppConstant.ApiHandler;
import com.adms.classsafari.AppConstant.AppConfiguration;
import com.adms.classsafari.AppConstant.Utils;
import com.adms.classsafari.Model.SelectedDataModel;
import com.adms.classsafari.Model.Session.SessionDetailModel;
import com.adms.classsafari.Model.Session.sessionDataModel;
import com.adms.classsafari.R;
import com.adms.classsafari.databinding.ActivityClassSearchScreenBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class ClassSearchScreen extends AppCompatActivity implements View.OnClickListener{

    ActivityClassSearchScreenBinding classSearchScreenBinding;
    Context mContext;
    SessionDetailModel dataResponse;
    List<sessionDataModel> datafilterResponse;
    String flag, selectedSessionStr = "", selectedSessionCityStr, sessionCapacityStr,
            sessionTypeStr, wheretoComeStr, genderStr = "",
            searchByStr, searchfront, locationStr,
            classNameStr, firsttimesearch, boardStr, standardStr, streamStr, lessionNameStr;
    SelectedDataModel selectedDataModel = new SelectedDataModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        classSearchScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_class_search_screen);
        mContext = ClassSearchScreen.this;
        flag = getIntent().getStringExtra("flag");
        sessionTypeStr = getIntent().getStringExtra("sessionType");
        wheretoComeStr = getIntent().getStringExtra("withOR");
        searchByStr = getIntent().getStringExtra("SearchBy");
        searchfront = getIntent().getStringExtra("searchfront");
        locationStr = getIntent().getStringExtra("city");
        classNameStr = getIntent().getStringExtra("sessionName");
        firsttimesearch = getIntent().getStringExtra("firsttimesearch");
        boardStr = getIntent().getStringExtra("board");
        standardStr = getIntent().getStringExtra("standard");
        streamStr = getIntent().getStringExtra("stream");

        lessionNameStr = getIntent().getStringExtra("lessionName");
//        selectedDataModel=getIntent().getParcelableExtra("selectedDataModel");
        init();
        setListner();
    }

    public void init() {

        if (wheretoComeStr.equalsIgnoreCase("withOR")) {
            if (flag.equalsIgnoreCase("play")) {
                classSearchScreenBinding.linearBg.setBackgroundResource(R.drawable.play_bg);
                classSearchScreenBinding.boardAutoTxt.setVisibility(View.GONE);
                classSearchScreenBinding.standardAutoTxt.setVisibility(View.GONE);
                classSearchScreenBinding.streamAutoTxt.setVisibility(View.GONE);
                classSearchScreenBinding.genderGroup.setVisibility(View.VISIBLE);
            } else {
                classSearchScreenBinding.linearBg.setBackgroundResource(R.drawable.study_bg);
                classSearchScreenBinding.boardAutoTxt.setVisibility(View.VISIBLE);
                classSearchScreenBinding.standardAutoTxt.setVisibility(View.VISIBLE);
                classSearchScreenBinding.streamAutoTxt.setVisibility(View.VISIBLE);
                classSearchScreenBinding.genderGroup.setVisibility(View.VISIBLE);
            }
            if (!firsttimesearch.equalsIgnoreCase("true")) {
                genderStr=getIntent().getStringExtra("gender");
                if(!classNameStr.equalsIgnoreCase("")){
                    classSearchScreenBinding.sessionAutoTxt.setText(classNameStr);
                    selectedSessionStr=classNameStr;
                }
                if(!lessionNameStr.equalsIgnoreCase("")){
                    classSearchScreenBinding.classAutoTxt.setText(lessionNameStr);
                }
                if(!boardStr.equalsIgnoreCase("")){
                    classSearchScreenBinding.boardAutoTxt.setText(boardStr);
                }
                if(!standardStr.equalsIgnoreCase("")){
                    classSearchScreenBinding.standardAutoTxt.setText(standardStr);
                }
                if(!streamStr.equalsIgnoreCase("")){
                    classSearchScreenBinding.streamAutoTxt.setText(streamStr);
                }

                if(!genderStr.equalsIgnoreCase("")){
                    if(genderStr.equalsIgnoreCase("1")){
                        classSearchScreenBinding.maleChk.setChecked(true);
                    }else{
                        classSearchScreenBinding.femaleChk.setChecked(true);
                    }
                }
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

        classSearchScreenBinding.sessionAutoTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                classNameStr = String.valueOf(adapterView.getItemAtPosition(i));
                Log.d("SessionName", classNameStr);
                fillLession();
                fillBoard();
                fillStandard();
                fillStream();
            }
        });
        classSearchScreenBinding.searchAutoTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedSessionCityStr = String.valueOf(adapterView.getItemAtPosition(i));
                classSearchScreenBinding.sessionAutoTxt.setText("");
                classSearchScreenBinding.classAutoTxt.setText("");
                classSearchScreenBinding.boardAutoTxt.setText("");
                classSearchScreenBinding.standardAutoTxt.setText("");
                classSearchScreenBinding.streamAutoTxt.setText("");
                classSearchScreenBinding.countStudentTxt.setText("");
                fillSession();
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
                    classSearchScreenBinding.countStudentTxt.setText("");
                }else{
                    fillSession();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        classSearchScreenBinding.sessionAutoTxt.addTextChangedListener(new TextWatcher() {
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
        classSearchScreenBinding.genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int radiobuttonID = classSearchScreenBinding.genderGroup.getCheckedRadioButtonId();
                switch (radiobuttonID) {
                    case R.id.male_chk:
                        genderStr = "1";
                        break;
                    case R.id.female_chk:
                        genderStr = "2";
                        break;
                    default:
                }
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

    public void setValueModel() {
        selectedDataModel.setSessionName(classSearchScreenBinding.sessionAutoTxt.getText().toString());
        selectedDataModel.setCity(classSearchScreenBinding.searchAutoTxt.getText().toString());
        selectedDataModel.setLessionName(classSearchScreenBinding.classAutoTxt.getText().toString());
        selectedDataModel.setBoard(classSearchScreenBinding.boardAutoTxt.getText().toString());
        selectedDataModel.setStandard(classSearchScreenBinding.standardAutoTxt.getText().toString());
        selectedDataModel.setStream(classSearchScreenBinding.streamAutoTxt.getText().toString());
        selectedDataModel.setGender(genderStr);
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
                            if (wheretoComeStr.equalsIgnoreCase("withOR")) {
                                if (flag.equalsIgnoreCase("play")) {
                                    for (int i = 0; i < dataResponse.getData().size(); i++) {
                                        if (dataResponse.getData().get(i).getCoachTypeID().equalsIgnoreCase(sessionTypeStr)) {
                                            datafilterResponse.add(dataResponse.getData().get(i));
                                        }
                                    }
                                } else {
                                    for (int i = 0; i < dataResponse.getData().size(); i++) {
                                        if (dataResponse.getData().get(i).getCoachTypeID().equalsIgnoreCase(sessionTypeStr)) {
                                            datafilterResponse.add(dataResponse.getData().get(i));
                                        }
                                    }
                                }
                            } else {
                                for (int i = 0; i < dataResponse.getData().size(); i++) {
                                    datafilterResponse.add(dataResponse.getData().get(i));
                                }
                            }

                            classSearchScreenBinding.searchAutoTxt.setText(AppConfiguration.ClassLocation);
                            fillCity();
                            fillSession();
                            fillLession();
                            fillBoard();
                            fillStandard();
                            fillStream();
//                            fillGender();
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
        ArrayList<String> SesisonNameArray = new ArrayList<String>();
        for (int j = 0; j < datafilterResponse.size(); j++) {
            if (wheretoComeStr.equalsIgnoreCase("withOR")) {
                if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr.trim()) &&
                        datafilterResponse.get(j).getCoachTypeID().equalsIgnoreCase(sessionTypeStr)) {
                    SesisonNameArray.add(datafilterResponse.get(j).getSessionName());
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
        classSearchScreenBinding.sessionAutoTxt.setThreshold(1);
        classSearchScreenBinding.sessionAutoTxt.setAdapter(adapterSessionName);

    }

    public void fillBoard() {
        ArrayList<String> BoardName = new ArrayList<String>();
        for (int j = 0; j < datafilterResponse.size(); j++) {
            if (selectedSessionStr.equalsIgnoreCase("")) {
                if (wheretoComeStr.equalsIgnoreCase("withOR")) {
                    if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr.trim()) &&
                            datafilterResponse.get(j).getCoachTypeID().equalsIgnoreCase(sessionTypeStr)) {
                        BoardName.add(datafilterResponse.get(j).getBoard());
                    }
                } else {
                    if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr.trim())) {
                        BoardName.add(datafilterResponse.get(j).getBoard());
                    }
                }
            } else {
                if (wheretoComeStr.equalsIgnoreCase("withOR")) {
                    if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr.trim()) &&
                            datafilterResponse.get(j).getSessionName().equalsIgnoreCase(selectedSessionStr.trim()) &&
                            datafilterResponse.get(j).getCoachTypeID().equalsIgnoreCase(sessionTypeStr)) {
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
        ArrayList<String> StandardName = new ArrayList<String>();
        for (int j = 0; j < datafilterResponse.size(); j++) {
            if (selectedSessionStr.equalsIgnoreCase("")) {
                if (wheretoComeStr.equalsIgnoreCase("withOR")) {
                    if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr.trim()) &&
                            datafilterResponse.get(j).getCoachTypeID().equalsIgnoreCase(sessionTypeStr)) {
                        StandardName.add(datafilterResponse.get(j).getStandard());
                    }
                } else {
                    if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr.trim())) {
                        StandardName.add(datafilterResponse.get(j).getStandard());
                    }
                }
            } else {
                if (wheretoComeStr.equalsIgnoreCase("withOR")) {
                    if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr.trim()) &&
                            datafilterResponse.get(j).getSessionName().equalsIgnoreCase(selectedSessionStr.trim()) &&
                            datafilterResponse.get(j).getCoachTypeID().equalsIgnoreCase(sessionTypeStr)) {
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

        ArrayList<String> StreamName = new ArrayList<String>();
        for (int j = 0; j < datafilterResponse.size(); j++) {
            if (selectedSessionStr.equalsIgnoreCase("")) {
                if (wheretoComeStr.equalsIgnoreCase("withOR")) {
                    if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr) &&
                            datafilterResponse.get(j).getCoachTypeID().equalsIgnoreCase(sessionTypeStr)) {
                        StreamName.add(datafilterResponse.get(j).getStream());
                    }
                } else {
                    if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr)) {
                        StreamName.add(datafilterResponse.get(j).getStream());
                    }
                }
            } else {
                if (wheretoComeStr.equalsIgnoreCase("withOR")) {
                    if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr.trim()) &&
                            datafilterResponse.get(j).getSessionName().equalsIgnoreCase(selectedSessionStr.trim()) &&
                            datafilterResponse.get(j).getCoachTypeID().equalsIgnoreCase(sessionTypeStr)) {
                        StreamName.add(datafilterResponse.get(j).getStream());
                    }
                } else {
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
        selectedSessionStr=classSearchScreenBinding.sessionAutoTxt.getText().toString();
        ArrayList<String> LessionNameArray = new ArrayList<String>();

        for (int j = 0; j < datafilterResponse.size(); j++) {
            if (selectedSessionStr.trim().equalsIgnoreCase("")) {
                if (wheretoComeStr.equalsIgnoreCase("withOR")) {
                    if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr.trim()) &&
                            datafilterResponse.get(j).getCoachTypeID().equalsIgnoreCase(sessionTypeStr)) {
                        LessionNameArray.add(datafilterResponse.get(j).getLessionTypeName().trim());
                    }
                } else {
                    if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr.trim())) {
                        LessionNameArray.add(datafilterResponse.get(j).getLessionTypeName().trim());
                    }
                }
            } else {
                if (wheretoComeStr.equalsIgnoreCase("withOR")) {
                    if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr.trim()) &&
                            datafilterResponse.get(j).getSessionName().trim().equalsIgnoreCase(selectedSessionStr.trim()) &&
                            datafilterResponse.get(j).getCoachTypeID().equalsIgnoreCase(sessionTypeStr)) {
                        LessionNameArray.add(datafilterResponse.get(j).getLessionTypeName().trim());
                    }
                } else {
                    if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr.trim()) &&
                            datafilterResponse.get(j).getSessionName().trim().equalsIgnoreCase(selectedSessionStr.trim())) {
                        LessionNameArray.add(datafilterResponse.get(j).getLessionTypeName().trim());
                    }
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

    public void fillGender() {
        for (int j = 0; j < datafilterResponse.size(); j++) {
            if (selectedSessionStr.equalsIgnoreCase("")) {

            } else {
                if (wheretoComeStr.equalsIgnoreCase("withOR")) {
                    if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr.trim()) &&
                            datafilterResponse.get(j).getSessionName().equalsIgnoreCase(selectedSessionStr.trim()) &&
                            datafilterResponse.get(j).getCoachTypeID().equalsIgnoreCase(sessionTypeStr)) {
                        if (dataResponse.getData().get(j).getGenderID().equalsIgnoreCase("1")) {
                            classSearchScreenBinding.maleChk.setChecked(true);
                        } else {
                            classSearchScreenBinding.femaleChk.setChecked(true);
                        }
                    }
                } else {
                    if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr.trim()) &&
                            datafilterResponse.get(j).getSessionName().equalsIgnoreCase(selectedSessionStr.trim())) {
                        if (dataResponse.getData().get(j).getGenderID().equalsIgnoreCase("1")) {
                            classSearchScreenBinding.maleChk.setChecked(true);
                        } else {
                            classSearchScreenBinding.femaleChk.setChecked(true);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_btn:
                if (!classSearchScreenBinding.searchAutoTxt.getText().toString().equalsIgnoreCase("") &&
                        !classSearchScreenBinding.sessionAutoTxt.getText().toString().equalsIgnoreCase("")) {
//                    setValueModel();
                    Intent inSearchUser = new Intent(mContext, ClassDeatilScreen.class);
                    inSearchUser.putExtra("SearchBy", "2");
                    inSearchUser.putExtra("city", classSearchScreenBinding.searchAutoTxt.getText().toString());
                    inSearchUser.putExtra("sessionName", classSearchScreenBinding.sessionAutoTxt.getText().toString());
                    inSearchUser.putExtra("lessionName", classSearchScreenBinding.classAutoTxt.getText().toString());
                    inSearchUser.putExtra("board", classSearchScreenBinding.boardAutoTxt.getText().toString());
                    inSearchUser.putExtra("standard", classSearchScreenBinding.standardAutoTxt.getText().toString());
                    inSearchUser.putExtra("stream", classSearchScreenBinding.streamAutoTxt.getText().toString());
                    inSearchUser.putExtra("searchType", flag);
                    inSearchUser.putExtra("gender", genderStr);
                    inSearchUser.putExtra("withOR", wheretoComeStr);
                    inSearchUser.putExtra("sessionType", sessionTypeStr);
                    inSearchUser.putExtra("SearchBy", searchByStr);
                    inSearchUser.putExtra("searchfront", searchfront);
//                    inSearchUser.putExtra("selectedDataModel", selectedDataModel);
                    inSearchUser.putExtra("firsttimesearch", "false");
                    startActivity(inSearchUser);
                } else {
                    classSearchScreenBinding.sessionAutoTxt.setError("Please Select Session and location.");
                }
            break;
            case R.id.back:
                Intent inSearchUser = new Intent(mContext, SearchByUser.class);
                startActivity(inSearchUser);
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
                classSearchScreenBinding.classAutoTxt.showDropDown();
                break;
            case R.id.session_auto_txt:
                classSearchScreenBinding.sessionAutoTxt.showDropDown();
                break;
            case R.id.search_auto_txt:
                classSearchScreenBinding.searchAutoTxt.showDropDown();
                break;
        }
    }


}
