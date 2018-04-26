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

public class ClassSearchScreen extends AppCompatActivity {

    ActivityClassSearchScreenBinding classSearchScreenBinding;
    Context mContext;
    SessionDetailModel dataResponse;
    List<sessionDataModel> datafilterResponse;
    String flag, selectedSessionStr = "", selectedSessionCityStr, sessionCapacityStr, sessionTypeStr, wheretoComeStr, genderStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        classSearchScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_class_search_screen);
        mContext = ClassSearchScreen.this;
        flag = getIntent().getStringExtra("flag");
        wheretoComeStr = getIntent().getStringExtra("withOR");
        init();
        setListner();
    }

    public void init() {
        if (wheretoComeStr.equalsIgnoreCase("withOR")) {
            if (flag.equalsIgnoreCase("play")) {
                classSearchScreenBinding.boardAutoTxt.setVisibility(View.GONE);
                classSearchScreenBinding.standardAutoTxt.setVisibility(View.GONE);
                classSearchScreenBinding.streamAutoTxt.setVisibility(View.GONE);
//                classSearchScreenBinding.gendarAutoTxt.setVisibility(View.GONE);
//                classSearchScreenBinding.gendar1AutoTxt.setVisibility(View.VISIBLE);
//                classSearchScreenBinding.gender1Group.setVisibility(View.VISIBLE);
                classSearchScreenBinding.genderGroup.setVisibility(View.VISIBLE);
                sessionTypeStr = "2";

            } else {
//                classSearchScreenBinding.gendar1AutoTxt.setVisibility(View.GONE);
                classSearchScreenBinding.boardAutoTxt.setVisibility(View.VISIBLE);
                classSearchScreenBinding.standardAutoTxt.setVisibility(View.VISIBLE);
                classSearchScreenBinding.streamAutoTxt.setVisibility(View.VISIBLE);
//                classSearchScreenBinding.gendarAutoTxt.setVisibility(View.VISIBLE);
                classSearchScreenBinding.genderGroup.setVisibility(View.VISIBLE);
//                classSearchScreenBinding.gender1Group.setVisibility(View.GONE);
                sessionTypeStr = "1";
            }
        }
        callSessionListApi();


    }

    public void setListner() {
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
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        classSearchScreenBinding.sessionAutoTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedSessionStr = String.valueOf(adapterView.getItemAtPosition(i));
                Log.d("SessionName", selectedSessionStr);

                fillLession();
                fillBoard();
                fillStandard();
                fillStream();
                fillGender();

            }
        });
        classSearchScreenBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inSearchUser = new Intent(mContext, SearchByUser.class);
                startActivity(inSearchUser);
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
        classSearchScreenBinding.searchAutoTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classSearchScreenBinding.searchAutoTxt.showDropDown();
            }
        });
        classSearchScreenBinding.sessionAutoTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classSearchScreenBinding.sessionAutoTxt.showDropDown();

            }
        });
        classSearchScreenBinding.classAutoTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classSearchScreenBinding.classAutoTxt.showDropDown();
            }
        });
        classSearchScreenBinding.boardAutoTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classSearchScreenBinding.boardAutoTxt.showDropDown();
            }
        });
        classSearchScreenBinding.standardAutoTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classSearchScreenBinding.standardAutoTxt.showDropDown();
            }
        });
        classSearchScreenBinding.streamAutoTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classSearchScreenBinding.streamAutoTxt.showDropDown();
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
        classSearchScreenBinding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!classSearchScreenBinding.searchAutoTxt.getText().toString().equalsIgnoreCase("") &&
                        !classSearchScreenBinding.sessionAutoTxt.getText().toString().equalsIgnoreCase("")) {
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
                    inSearchUser.putExtra("withOR",wheretoComeStr);
                    startActivity(inSearchUser);
                } else {
                    classSearchScreenBinding.sessionAutoTxt.setError("Please Select Session and location.");
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
                            datafilterResponse = new ArrayList<sessionDataModel>();
                            if (wheretoComeStr.equalsIgnoreCase("withOR")) {
                                if (flag.equalsIgnoreCase("play")) {
                                    for (int i = 0; i < dataResponse.getData().size(); i++) {
                                        if (dataResponse.getData().get(i).getSessionType().equalsIgnoreCase(sessionTypeStr)) {
                                            datafilterResponse.add(dataResponse.getData().get(i));
                                        }
                                    }
                                } else {
                                    for (int i = 0; i < dataResponse.getData().size(); i++) {
                                        if (dataResponse.getData().get(i).getSessionType().equalsIgnoreCase(sessionTypeStr)) {
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
                            fillGender();
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
            if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr)) {
                SesisonNameArray.add(datafilterResponse.get(j).getSessionName());
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
                if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr)) {
                    BoardName.add(datafilterResponse.get(j).getBoard());
                }
            } else {
                if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr) &&
                        datafilterResponse.get(j).getSessionName().equalsIgnoreCase(selectedSessionStr)) {
                    BoardName.add(datafilterResponse.get(j).getBoard());
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
                if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr)) {
                    StandardName.add(datafilterResponse.get(j).getStandard());
                }
            } else {
                if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr) &&
                        datafilterResponse.get(j).getSessionName().equalsIgnoreCase(selectedSessionStr)) {
                    StandardName.add(datafilterResponse.get(j).getStandard());
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
                if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr)) {
                    StreamName.add(datafilterResponse.get(j).getStream());
                }
            } else {
                if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr) &&
                        datafilterResponse.get(j).getSessionName().equalsIgnoreCase(selectedSessionStr)) {
                    StreamName.add(datafilterResponse.get(j).getStream());
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
        ArrayList<String> LessionNameArray = new ArrayList<String>();

        for (int j = 0; j < datafilterResponse.size(); j++) {
            if (selectedSessionStr.trim().equalsIgnoreCase("")) {
                if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr)) {
                    LessionNameArray.add(datafilterResponse.get(j).getLessionTypeName().trim());
                }
            } else {
                if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr) &&
                        datafilterResponse.get(j).getSessionName().trim().equalsIgnoreCase(selectedSessionStr)) {
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

    public void fillGender() {
        for (int j = 0; j < datafilterResponse.size(); j++) {
            if (selectedSessionStr.equalsIgnoreCase("")) {

            } else {
                if (datafilterResponse.get(j).getAddressCity().trim().equalsIgnoreCase(selectedSessionCityStr) &&
                        datafilterResponse.get(j).getSessionName().equalsIgnoreCase(selectedSessionStr)) {
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
