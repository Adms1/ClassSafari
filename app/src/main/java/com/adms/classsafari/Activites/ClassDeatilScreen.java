package com.adms.classsafari.Activites;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.adms.classsafari.Adapter.ClassDetailAdapter;
import com.adms.classsafari.Adapter.PopularClassListAdapter;
import com.adms.classsafari.AppConstant.ApiHandler;
import com.adms.classsafari.AppConstant.HighToLowSortRating;
import com.adms.classsafari.AppConstant.HightToLowSortSessionPrice;
import com.adms.classsafari.AppConstant.LowToHighSortRating;
import com.adms.classsafari.AppConstant.LowToHighSortSessionPrice;
import com.adms.classsafari.AppConstant.Utils;
import com.adms.classsafari.BottomNavigationViewHelper;
import com.adms.classsafari.Interface.onViewClick;
import com.adms.classsafari.Model.SelectedDataModel;
import com.adms.classsafari.Model.Session.SessionDetailModel;
import com.adms.classsafari.Model.Session.sessionDataModel;
import com.adms.classsafari.R;
import com.adms.classsafari.databinding.ActivityClassDeatilScreenBinding;
import com.adms.classsafari.databinding.DialogPopularBinding;
import com.adms.classsafari.databinding.DialogPriceBinding;
import com.adms.classsafari.databinding.DialogSortBinding;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class ClassDeatilScreen extends AppCompatActivity implements View.OnClickListener {

    public Dialog popularDialog, priceDialog, sortDialog;
    ActivityClassDeatilScreenBinding binding;
    DialogPopularBinding popularBinding;
    DialogPriceBinding priceBinding;
    DialogSortBinding sortBinding;

    Context mContext;
    ClassDetailAdapter classDetailAdapter;

    List<sessionDataModel> arrayListPopular;
    PopularClassListAdapter popularClassListAdapter;

    String subjectStr, boardStr = "", standardStr = "", streamStr = "",
            locationStr, classNameStr, searchByStr, searchTypeStr,
            wheretoComeStr, genderStr = "", maxpriceStr = "", minpriceStr = "",
            sessionId, commentStr, ratingValueStr, popularsessionID = "",
            populardurationStr = "", populargenderStr = "", popluarsessiondateStr = "", firsttimesearch;
    SessionDetailModel dataResponse, populardataresponse;
    List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
    boolean arrayfirst = true;


    String rangestatusStr = "", afterfilterresult, pricewiseStr = "", ratingStr = "", sessionType, searchfront;
    int count = 0, result = 0;
    int SessionHour = 0;
    Integer SessionMinit = 0;
    SelectedDataModel selectedDataModel;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_class_deatil_screen);
        mContext = ClassDeatilScreen.this;

        init();
        setListner();
    }

    public void init() {

//        Intent intent=getIntent();
//        selectedDataModel=intent.getParcelableExtra("selectedDataModel");
        searchByStr = getIntent().getStringExtra("SearchBy");

        locationStr = getIntent().getStringExtra("city");
        classNameStr = getIntent().getStringExtra("sessionName");
        wheretoComeStr = getIntent().getStringExtra("withOR");
        searchTypeStr = getIntent().getStringExtra("searchType");

        searchfront = getIntent().getStringExtra("searchfront");
        Utils.setPref(mContext, "location", locationStr.trim());
        Utils.setPref(mContext, "classname", classNameStr.trim());
        BottomNavigationViewHelper.removeShiftMode(binding.bottomNavigationView);


        if (wheretoComeStr.equalsIgnoreCase("withOR")) {
            subjectStr = getIntent().getStringExtra("lessionName");
            standardStr = getIntent().getStringExtra("standard");
            streamStr = getIntent().getStringExtra("stream");
            boardStr = getIntent().getStringExtra("board");
            sessionType = getIntent().getStringExtra("sessionType");
            genderStr = getIntent().getStringExtra("gender");
            firsttimesearch = getIntent().getStringExtra("firsttimesearch");
        } else {
            subjectStr = "";
            standardStr = "";
            streamStr = "";
            boardStr = "";
            sessionType = "";
            genderStr = "";
            firsttimesearch = "";
        }
        if (!searchByStr.equalsIgnoreCase("1")) {
            if (!boardStr.equalsIgnoreCase("")) { //||
                //  || ) {
                binding.boardTxt.setVisibility(View.VISIBLE);
                binding.boardTxt.setText("\u2022" + boardStr);
            } else {
                binding.boardTxt.setVisibility(View.GONE);
            }
            if (!standardStr.equalsIgnoreCase("")) {
                binding.standardTxt.setVisibility(View.VISIBLE);
                binding.standardTxt.setText("\u2022" + standardStr);
            } else {
                binding.standardTxt.setVisibility(View.GONE);
            }
            if (!streamStr.equalsIgnoreCase("")) {
                binding.streamTxt.setVisibility(View.VISIBLE);
                binding.streamTxt.setText("\u2022" + streamStr);
            } else {
                binding.streamTxt.setVisibility(View.GONE);
            }
        }
        binding.cityTxt.setText(locationStr);
        binding.subjectTxt.setText(classNameStr);

        callSessionListApi();

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
//                    case R.id.action_sort:
//                        SortDialog();
//                        break;
                    case R.id.action_filter:
                        SortDialog();
                        break;
                }


                return true;
            }
        });
        binding.backImg.setOnClickListener(this);
        binding.searchImg.setOnClickListener(this);
        binding.multiautocompe.setOnClickListener(this);
        binding.multiautocompe.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {
                Log.d("valueOf OnTextChange", "" + charSequence + i + i1);
                if (charSequence.length() == 0) {
                    binding.inforTxt.setVisibility(View.VISIBLE);
                    if (dataResponse.getData().size() >= 0) {
                        List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
                        for (sessionDataModel arrayObj : dataResponse.getData()) {
                            if (arrayObj.getAddressCity().trim().toLowerCase().contains(locationStr.trim().toLowerCase()) &&
                                    arrayObj.getSessionName().trim().toLowerCase().contains(classNameStr.trim().toLowerCase())) {
                                filterFinalArray.add(arrayObj);
                            }
                        }
                        fillData(filterFinalArray);
                    }
                    arrayfirst = true;

                } else {
                    if (dataResponse.getData().size() >= 0) {
                        String[] spilt = charSequence.toString().trim().split("\\,");
                        Log.d("spiltOnText", "" + spilt.length);
                        for (int k = 0; k < spilt.length; k++) {
                            List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
                            for (sessionDataModel arrayObj : dataResponse.getData()) {
                                if (arrayObj.getAddressCity().trim().toLowerCase().contains(locationStr.trim().toLowerCase()) &&
                                        arrayObj.getSessionName().trim().toLowerCase().contains(classNameStr.trim().toLowerCase())) {
                                    if (arrayObj.getRegionName().trim().toLowerCase().contains(spilt[k].trim().toLowerCase())) {
                                        filterFinalArray.add(arrayObj);
                                    }
                                }
                            }
                            fillData(filterFinalArray);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        binding.multiautocompe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                binding.inforTxt.setVisibility(View.GONE);
                if (arrayfirst) {
                    filterFinalArray = new ArrayList<sessionDataModel>();
                }
                String str = binding.multiautocompe.getText().toString();
                if (!str.equalsIgnoreCase("")) {
                    arrayfirst = false;
                    String[] spilt = adapterView.getItemAtPosition(i).toString().trim().split("\\,");

                    for (sessionDataModel arrayObj : dataResponse.getData()) {
                        if (arrayObj.getAddressCity().trim().equalsIgnoreCase(locationStr.trim()) &&
                                arrayObj.getSessionName().trim().toLowerCase().contains(classNameStr.trim().toLowerCase())) {
                            if (arrayObj.getRegionName().trim().toLowerCase().contains((spilt[0]).trim().toLowerCase())) {
                                filterFinalArray.add(arrayObj);

                            }
                        }
                    }
                    Log.d("FilterArray", "" + filterFinalArray.size());
                    fillData(filterFinalArray);
                } else {
                    List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
                    for (sessionDataModel arrayObj : dataResponse.getData()) {
                        if (arrayObj.getAddressCity().trim().toLowerCase().contains(locationStr.trim().toLowerCase()) &&
                                arrayObj.getSessionName().trim().toLowerCase().contains(classNameStr.trim().toLowerCase())) {
                            filterFinalArray.add(arrayObj);
                        }
                    }
                    Log.d("FilterArray", "" + filterFinalArray.size());
                    fillData(filterFinalArray);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                Intent inSearchUser = new Intent(mContext, SearchByUser.class);
                startActivity(inSearchUser);
                break;
            case R.id.search_img:
                Intent insession = new Intent(mContext, ClassSearchScreen.class);
                insession.putExtra("flag", searchTypeStr);
                insession.putExtra("withOR", wheretoComeStr);
                insession.putExtra("SearchBy", searchByStr);
                insession.putExtra("searchfront", "searchunder");
                insession.putExtra("city", locationStr);
                insession.putExtra("sessionName", classNameStr);
                insession.putExtra("sessionType", sessionType);
                insession.putExtra("lessionName", subjectStr);
                insession.putExtra("board", boardStr);
                insession.putExtra("standard", standardStr);
                insession.putExtra("stream", streamStr);
                insession.putExtra("gender", genderStr);
                insession.putExtra("firsttimesearch", firsttimesearch);
//                insession.putExtra("selectedDataModel",selectedDataModel);
                startActivity(insession);
                break;
            case R.id.multiautocompe:
                binding.multiautocompe.showDropDown();
                break;

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent inback = new Intent(mContext, SearchByUser.class);
        inback.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(inback);
    }

    public void PopularDialog() {
        popularBinding = DataBindingUtil.
                inflate(LayoutInflater.from(mContext), R.layout.dialog_popular, (ViewGroup) binding.getRoot(), false);
        popularDialog = new Dialog(mContext, R.style.PauseDialog);
        Window window = popularDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        popularDialog.getWindow().getAttributes().verticalMargin = 0.10F;
        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);

        popularDialog.getWindow().setBackgroundDrawableResource(R.drawable.poop_p3);

        popularDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popularDialog.setCancelable(true);
        popularDialog.setContentView(popularBinding.getRoot());

        callPopularSessionListApi();


        popularBinding.doneTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<SessionDetailModel> arrayList = new ArrayList<SessionDetailModel>();

                if (!popularsessionID.equalsIgnoreCase("")) {
                    List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
                    for (int i = 0; i < dataResponse.getData().size(); i++) {
                        if (popularsessionID.equalsIgnoreCase(dataResponse.getData().get(i).getSessionID())) {
                            String[] spiltPipes = dataResponse.getData().get(i).getSchedule().split("\\|");
                            String[] spiltComma;
                            String[] spiltDash;
                            Log.d("spilt", "" + spiltPipes.toString());
                            for (int j = 0; j < spiltPipes.length; j++) {
                                spiltComma = spiltPipes[j].split("\\,");
                                spiltDash = spiltComma[1].split("\\-");
                                calculateHours(spiltDash[0], spiltDash[1]);
                                populardurationStr = SessionHour + " hr " + SessionMinit + " min";

                            }
                            populargenderStr = dataResponse.getData().get(i).getGenderID();
                            popluarsessiondateStr = dataResponse.getData().get(i).getStartDate() + " To " + dataResponse.getData().get(i).getEndDate();
                        }
                    }
                    Log.d("duration,+Time", populardurationStr + "" + popluarsessiondateStr);

                    Intent inSession = new Intent(mContext, SessionName.class);
                    inSession.putExtra("sessionID", popularsessionID);
                    inSession.putExtra("SearchBy", searchByStr);
                    inSession.putExtra("city", locationStr);
                    inSession.putExtra("sessionName", classNameStr);
                    inSession.putExtra("board", boardStr);
                    inSession.putExtra("stream", streamStr);
                    inSession.putExtra("standard", standardStr);
                    inSession.putExtra("lessionName", subjectStr);
                    inSession.putExtra("sessiondate", popluarsessiondateStr);
                    inSession.putExtra("duration", populardurationStr);
                    inSession.putExtra("gender", populargenderStr);
                    inSession.putExtra("searchType", searchTypeStr);
                    inSession.putExtra("withOR", wheretoComeStr);
                    inSession.putExtra("searchfront", searchfront);
                    mContext.startActivity(inSession);
                    popularDialog.dismiss();
                } else {
                    popularDialog.dismiss();
                }
            }
        });

        popularDialog.show();
    }

    public void PriceDialog() {

        priceBinding = DataBindingUtil.
                inflate(LayoutInflater.from(mContext), R.layout.dialog_price, (ViewGroup) binding.getRoot(), false);

        priceDialog = new Dialog(mContext, R.style.PauseDialog);
        Window window = priceDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        priceDialog.getWindow().getAttributes().verticalMargin = 0.10F;
        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);

        priceDialog.getWindow().setBackgroundDrawableResource(R.drawable.price_p3);

        priceDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        priceDialog.setCancelable(true);
        priceDialog.setContentView(priceBinding.getRoot());

        priceBinding.resultTxt.setText(String.valueOf(result));
        List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
        List<Float> priceList = new ArrayList<>();

        if (dataResponse.getData().size() > 0) {
            for (sessionDataModel arrayObj : dataResponse.getData()) {
                if (arrayObj.getAddressCity().trim().equalsIgnoreCase(locationStr.trim()) &&
                        arrayObj.getSessionName().trim().toLowerCase().contains(classNameStr.trim().toLowerCase())) {
                    filterFinalArray.add(arrayObj);
                }
            }
            for (int i = 0; i < filterFinalArray.size(); i++) {
                priceList.add(Float.parseFloat(filterFinalArray.get(i).getSessionAmount()));
            }

            Object objmax = Collections.max(priceList);
            Object objmin = Collections.min(priceList);
//            if (!maxpriceStr.equalsIgnoreCase("") && !minpriceStr.equalsIgnoreCase("")) {
//                priceBinding.rangeSeekbar.setMaxStartValue(Float.parseFloat(maxpriceStr));
//                priceBinding.rangeSeekbar.setMinStartValue(Float.parseFloat(minpriceStr));
//            } else {
                priceBinding.rangeSeekbar.setMaxValue((Float) objmax);
                priceBinding.rangeSeekbar.setMinValue((Float) objmin);
//            }
        }
        priceBinding.doneTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String[] maxsplit = priceBinding.priceRange2Txt.getText().toString().split("\\s+");
//                String[] minsplit = priceBinding.priceRange1Txt.getText().toString().split("\\s+");
//                maxpriceStr = maxsplit[1];
//                minpriceStr = minsplit[1];
//                if (dataResponse.getData().size() >= 0) {
//                    List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
//                    for (sessionDataModel arrayObj : dataResponse.getData()) {
//                        if (arrayObj.getAddressCity().trim().equalsIgnoreCase(locationStr.trim())
//                                && arrayObj.getSessionName().trim().toLowerCase().contains(classNameStr.trim().toLowerCase())) {
//                            if (Float.parseFloat(arrayObj.getSessionAmount()) >= Integer.parseInt(minpriceStr) &&
//                                    Float.parseFloat(arrayObj.getSessionAmount()) <= Integer.parseInt(maxpriceStr)) {
//                                filterFinalArray.add(arrayObj);
//                            }
//                        }
//                    }
//                    fillData(filterFinalArray);
//                }
                priceDialog.dismiss();


            }
        });
        priceBinding.rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                priceBinding.priceRange1Txt.setText("₹ " + String.valueOf(minValue));
                priceBinding.priceRange2Txt.setText("₹ " + String.valueOf(maxValue));
                Log.d("select vlue", "min value" + minValue + "maxvalue" + maxValue);
                if (dataResponse.getData().size() >= 0) {
                    List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
                    for (sessionDataModel arrayObj : dataResponse.getData()) {
                        if (arrayObj.getAddressCity().trim().equalsIgnoreCase(locationStr.trim())
                                && arrayObj.getSessionName().trim().toLowerCase().contains(classNameStr.trim().toLowerCase())) {
                            if (Float.parseFloat(arrayObj.getSessionAmount()) >= Integer.parseInt(String.valueOf(minValue)) &&
                                    Float.parseFloat(arrayObj.getSessionAmount()) <= Integer.parseInt(String.valueOf(maxValue))) {
                                filterFinalArray.add(arrayObj);
                                afterfilterresult = String.valueOf(filterFinalArray.size());
                                priceBinding.result1Txt.setText(afterfilterresult);
                            }
                        }
                    }
                    fillData(filterFinalArray);
                }
            }
        });
        priceBinding.rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                priceBinding.priceRange1Txt.setText("₹ " + String.valueOf(minValue));
                priceBinding.priceRange2Txt.setText("₹ " + String.valueOf(maxValue));
                Log.d("final vlue", "min value" + minValue + "maxvalue" + maxValue);
            }
        });
        priceDialog.show();


    }


    //    //Use for SessionList
//    public void callSessionListApi() {
//        if (Utils.checkNetwork(mContext)) {
//
//            Utils.showDialog(mContext);
//            ApiHandler.getApiService().get_SessionList(getSessionListDetail(), new retrofit.Callback<SessionDetailModel>() {
//                @Override
//                public void success(SessionDetailModel sessionInfo, Response response) {
//                    Utils.dismissDialog();
//                    if (sessionInfo == null) {
//                        Utils.ping(mContext, getString(R.string.something_wrong));
//                        return;
//                    }
//                    if (sessionInfo.getSuccess() == null) {
//                        Utils.ping(mContext, getString(R.string.something_wrong));
//                        return;
//                    }
//                    if (sessionInfo.getSuccess().equalsIgnoreCase("false")) {
//                        Utils.ping(mContext, getString(R.string.false_msg));
//                        return;
//                    }
//                    if (sessionInfo.getSuccess().equalsIgnoreCase("True")) {
//                        Utils.dismissDialog();
//                        if (sessionInfo.getData().size() > 0) {
//                            dataResponse = sessionInfo;
//                            fillArea();
//                            if (searchByStr.equalsIgnoreCase("1")) {
//                                if (searchfront.equalsIgnoreCase("searchfront")) {
//                                    List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
//                                    for (sessionDataModel arrayObj : dataResponse.getData()) {
//                                        if (arrayObj.getAddressCity().trim().equalsIgnoreCase(locationStr.trim())
//                                                && arrayObj.getSessionName().trim().toLowerCase().contains(classNameStr.trim().toLowerCase())) {
//                                            filterFinalArray.add(arrayObj);
//                                            Collections.sort(filterFinalArray, new HighToLowSortRating());
//                                            result = filterFinalArray.size();
//                                        }
//                                        fillData(filterFinalArray);
//                                    }
//                                } else {
//                                    if (!subjectStr.equalsIgnoreCase("") && !boardStr.equalsIgnoreCase("") &&
//                                            !standardStr.equalsIgnoreCase("") && !streamStr.equalsIgnoreCase("")) {
//                                        List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
//                                        for (sessionDataModel arrayObj : dataResponse.getData()) {
//                                            if (arrayObj.getAddressCity().trim().equalsIgnoreCase(locationStr.trim()) &&
//                                                    arrayObj.getSessionName().trim().equalsIgnoreCase(classNameStr.trim()) &&
//                                                    arrayObj.getLessionTypeName().trim().equalsIgnoreCase(subjectStr.trim()) &&
//                                                    arrayObj.getBoard().trim().equalsIgnoreCase(boardStr.trim()) &&
//                                                    arrayObj.getStandard().trim().equalsIgnoreCase(standardStr.trim()) &&
//                                                    arrayObj.getStream().trim().equalsIgnoreCase(streamStr.trim()) &&
//                                                    arrayObj.getGenderID().trim().equalsIgnoreCase(genderStr.trim())) {
//                                                filterFinalArray.add(arrayObj);
//                                                Collections.sort(filterFinalArray, new HighToLowSortRating());
//                                                result = filterFinalArray.size();
//                                            }
//                                            fillData(filterFinalArray);
//                                        }
//                                    } else {
//                                        if (!subjectStr.equalsIgnoreCase("")) {
//                                            List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
//                                            for (sessionDataModel arrayObj : dataResponse.getData()) {
//                                                if (arrayObj.getAddressCity().trim().equalsIgnoreCase(locationStr.trim()) &&
//                                                        arrayObj.getSessionName().trim().equalsIgnoreCase(classNameStr.trim()) &&
//                                                        arrayObj.getLessionTypeName().trim().equalsIgnoreCase(subjectStr.trim())) {
//                                                    filterFinalArray.add(arrayObj);
//                                                    Collections.sort(filterFinalArray, new HighToLowSortRating());
//                                                    result = filterFinalArray.size();
//                                                }
//                                                fillData(filterFinalArray);
//                                            }
//                                        } else if (!boardStr.equalsIgnoreCase("")) {
//                                            List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
//                                            for (sessionDataModel arrayObj : dataResponse.getData()) {
//                                                if (arrayObj.getAddressCity().trim().equalsIgnoreCase(locationStr.trim()) &&
//                                                        arrayObj.getSessionName().trim().equalsIgnoreCase(classNameStr.trim()) &&
//                                                        arrayObj.getBoard().trim().equalsIgnoreCase(boardStr.trim())) {
//                                                    filterFinalArray.add(arrayObj);
//                                                    Collections.sort(filterFinalArray, new HighToLowSortRating());
//                                                    result = filterFinalArray.size();
//                                                }
//                                                fillData(filterFinalArray);
//                                            }
//                                        } else if (!standardStr.equalsIgnoreCase("")) {
//                                            List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
//                                            for (sessionDataModel arrayObj : dataResponse.getData()) {
//                                                if (arrayObj.getAddressCity().trim().equalsIgnoreCase(locationStr.trim()) &&
//                                                        arrayObj.getSessionName().trim().equalsIgnoreCase(classNameStr.trim()) &&
//                                                        arrayObj.getStandard().trim().equalsIgnoreCase(standardStr.trim())) {
//                                                    filterFinalArray.add(arrayObj);
//                                                    Collections.sort(filterFinalArray, new HighToLowSortRating());
//                                                    result = filterFinalArray.size();
//                                                }
//                                                fillData(filterFinalArray);
//                                            }
//                                        } else if (!streamStr.equalsIgnoreCase("")) {
//                                            List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
//                                            for (sessionDataModel arrayObj : dataResponse.getData()) {
//                                                if (arrayObj.getAddressCity().trim().equalsIgnoreCase(locationStr.trim()) &&
//                                                        arrayObj.getSessionName().trim().equalsIgnoreCase(classNameStr.trim()) &&
//                                                        arrayObj.getStream().trim().equalsIgnoreCase(streamStr.trim())) {
//                                                    filterFinalArray.add(arrayObj);
//                                                    Collections.sort(filterFinalArray, new HighToLowSortRating());
//                                                    result = filterFinalArray.size();
//                                                }
//                                                fillData(filterFinalArray);
//                                            }
//                                        } else if (!genderStr.equalsIgnoreCase("")) {
//                                            List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
//                                            for (sessionDataModel arrayObj : dataResponse.getData()) {
//                                                if (arrayObj.getAddressCity().trim().equalsIgnoreCase(locationStr.trim()) &&
//                                                        arrayObj.getSessionName().trim().equalsIgnoreCase(classNameStr.trim()) &&
//                                                        arrayObj.getGenderID().trim().equalsIgnoreCase(genderStr.trim())) {
//                                                    filterFinalArray.add(arrayObj);
//                                                    Collections.sort(filterFinalArray, new HighToLowSortRating());
//                                                    result = filterFinalArray.size();
//                                                }
//                                                fillData(filterFinalArray);
//                                            }
//                                        } else {
//                                            List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
//                                            for (sessionDataModel arrayObj : dataResponse.getData()) {
//                                                if (arrayObj.getAddressCity().trim().equalsIgnoreCase(locationStr.trim()) &&
//                                                        arrayObj.getSessionName().trim().toLowerCase().equalsIgnoreCase(classNameStr.trim().toLowerCase())) {
//                                                    filterFinalArray.add(arrayObj);
//                                                    Collections.sort(filterFinalArray, new HighToLowSortRating());
//                                                    result = filterFinalArray.size();
//                                                }
//                                                fillData(filterFinalArray);
//                                            }
//                                        }
//                                    }
//                                }
//                            } else {
//                                if (searchTypeStr.equalsIgnoreCase("play")) {
//                                    if (!subjectStr.equalsIgnoreCase("") && !genderStr.equalsIgnoreCase("")) {
//                                        List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
//                                        for (sessionDataModel arrayObj : dataResponse.getData()) {
//                                            if (arrayObj.getAddressCity().trim().equalsIgnoreCase(locationStr.trim()) &&
//                                                    arrayObj.getSessionName().trim().equalsIgnoreCase(classNameStr.trim()) &&
//                                                    arrayObj.getLessionTypeName().trim().equalsIgnoreCase(subjectStr.trim()) &&
//                                                    arrayObj.getGenderID().trim().equalsIgnoreCase(genderStr.trim())) {
//                                                filterFinalArray.add(arrayObj);
//                                                Collections.sort(filterFinalArray, new HighToLowSortRating());
//                                                result = filterFinalArray.size();
//                                            }
//                                            fillData(filterFinalArray);
//                                        }
//                                    } else {
//                                        if (!subjectStr.equalsIgnoreCase("")) {
//                                            List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
//                                            for (sessionDataModel arrayObj : dataResponse.getData()) {
//                                                if (arrayObj.getAddressCity().trim().equalsIgnoreCase(locationStr.trim()) &&
//                                                        arrayObj.getSessionName().trim().equalsIgnoreCase(classNameStr.trim()) &&
//                                                        arrayObj.getLessionTypeName().trim().equalsIgnoreCase(subjectStr.trim())) {
//                                                    filterFinalArray.add(arrayObj);
//                                                    Collections.sort(filterFinalArray, new HighToLowSortRating());
//                                                    result = filterFinalArray.size();
//                                                }
//                                                fillData(filterFinalArray);
//                                            }
//                                        } else if (!genderStr.equalsIgnoreCase("")) {
//                                            List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
//                                            for (sessionDataModel arrayObj : dataResponse.getData()) {
//                                                if (arrayObj.getAddressCity().trim().equalsIgnoreCase(locationStr.trim()) &&
//                                                        arrayObj.getSessionName().trim().equalsIgnoreCase(classNameStr.trim()) &&
//                                                        arrayObj.getGenderID().trim().equalsIgnoreCase(genderStr.trim())) {
//                                                    filterFinalArray.add(arrayObj);
//                                                    Collections.sort(filterFinalArray, new HighToLowSortRating());
//                                                    result = filterFinalArray.size();
//                                                }
//                                                fillData(filterFinalArray);
//                                            }
//                                        } else {
//                                            List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
//                                            for (sessionDataModel arrayObj : dataResponse.getData()) {
//                                                if (arrayObj.getAddressCity().trim().equalsIgnoreCase(locationStr.trim()) &&
//                                                        arrayObj.getSessionName().trim().equalsIgnoreCase(classNameStr.trim())) {
//                                                    filterFinalArray.add(arrayObj);
//                                                    Collections.sort(filterFinalArray, new HighToLowSortRating());
//                                                    result = filterFinalArray.size();
//                                                }
//                                                fillData(filterFinalArray);
//                                            }
//                                        }
//                                    }
//
//                                } else {
//                                    if (!subjectStr.equalsIgnoreCase("") && !boardStr.equalsIgnoreCase("") &&
//                                            !standardStr.equalsIgnoreCase("") && !streamStr.equalsIgnoreCase("")) {
//                                        List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
//                                        for (sessionDataModel arrayObj : dataResponse.getData()) {
//                                            if (arrayObj.getAddressCity().trim().equalsIgnoreCase(locationStr.trim()) &&
//                                                    arrayObj.getSessionName().trim().equalsIgnoreCase(classNameStr.trim()) &&
//                                                    arrayObj.getLessionTypeName().trim().equalsIgnoreCase(subjectStr.trim()) &&
//                                                    arrayObj.getBoard().trim().equalsIgnoreCase(boardStr.trim()) &&
//                                                    arrayObj.getStandard().trim().equalsIgnoreCase(standardStr.trim()) &&
//                                                    arrayObj.getStream().trim().equalsIgnoreCase(streamStr.trim()) &&
//                                                    arrayObj.getGenderID().trim().equalsIgnoreCase(genderStr.trim())) {
//                                                filterFinalArray.add(arrayObj);
//                                                Collections.sort(filterFinalArray, new HighToLowSortRating());
//                                                result = filterFinalArray.size();
//                                            }
//                                            fillData(filterFinalArray);
//                                        }
//                                    } else {
//                                        if (!subjectStr.equalsIgnoreCase("")) {
//                                            List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
//                                            for (sessionDataModel arrayObj : dataResponse.getData()) {
//                                                if (arrayObj.getAddressCity().trim().equalsIgnoreCase(locationStr.trim()) &&
//                                                        arrayObj.getSessionName().trim().equalsIgnoreCase(classNameStr.trim()) &&
//                                                        arrayObj.getLessionTypeName().trim().equalsIgnoreCase(subjectStr.trim())) {
//                                                    filterFinalArray.add(arrayObj);
//                                                    Collections.sort(filterFinalArray, new HighToLowSortRating());
//                                                    result = filterFinalArray.size();
//                                                }
//                                                fillData(filterFinalArray);
//                                            }
//                                        } else if (!boardStr.equalsIgnoreCase("")) {
//                                            List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
//                                            for (sessionDataModel arrayObj : dataResponse.getData()) {
//                                                if (arrayObj.getAddressCity().trim().equalsIgnoreCase(locationStr.trim()) &&
//                                                        arrayObj.getSessionName().trim().equalsIgnoreCase(classNameStr.trim()) &&
//                                                        arrayObj.getBoard().trim().equalsIgnoreCase(boardStr.trim())) {
//                                                    filterFinalArray.add(arrayObj);
//                                                    Collections.sort(filterFinalArray, new HighToLowSortRating());
//                                                    result = filterFinalArray.size();
//                                                }
//                                                fillData(filterFinalArray);
//                                            }
//                                        } else if (!standardStr.equalsIgnoreCase("")) {
//                                            List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
//                                            for (sessionDataModel arrayObj : dataResponse.getData()) {
//                                                if (arrayObj.getAddressCity().trim().equalsIgnoreCase(locationStr.trim()) &&
//                                                        arrayObj.getSessionName().trim().equalsIgnoreCase(classNameStr.trim()) &&
//                                                        arrayObj.getStandard().trim().equalsIgnoreCase(standardStr.trim())) {
//                                                    filterFinalArray.add(arrayObj);
//                                                    Collections.sort(filterFinalArray, new HighToLowSortRating());
//                                                    result = filterFinalArray.size();
//                                                }
//                                                fillData(filterFinalArray);
//                                            }
//                                        } else if (!streamStr.equalsIgnoreCase("")) {
//                                            List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
//                                            for (sessionDataModel arrayObj : dataResponse.getData()) {
//                                                if (arrayObj.getAddressCity().trim().equalsIgnoreCase(locationStr.trim()) &&
//                                                        arrayObj.getSessionName().trim().equalsIgnoreCase(classNameStr.trim()) &&
//                                                        arrayObj.getStream().trim().equalsIgnoreCase(streamStr.trim())) {
//                                                    filterFinalArray.add(arrayObj);
//                                                    Collections.sort(filterFinalArray, new HighToLowSortRating());
//                                                    result = filterFinalArray.size();
//                                                }
//                                                fillData(filterFinalArray);
//                                            }
//                                        } else if (!genderStr.equalsIgnoreCase("")) {
//                                            List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
//                                            for (sessionDataModel arrayObj : dataResponse.getData()) {
//                                                if (arrayObj.getAddressCity().trim().equalsIgnoreCase(locationStr.trim()) &&
//                                                        arrayObj.getSessionName().trim().equalsIgnoreCase(classNameStr.trim()) &&
//                                                        arrayObj.getGenderID().trim().equalsIgnoreCase(genderStr.trim())) {
//                                                    filterFinalArray.add(arrayObj);
//                                                    Collections.sort(filterFinalArray, new HighToLowSortRating());
//                                                    result = filterFinalArray.size();
//                                                }
//                                                fillData(filterFinalArray);
//                                            }
//                                        } else {
//                                            List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
//                                            for (sessionDataModel arrayObj : dataResponse.getData()) {
//                                                if (arrayObj.getAddressCity().trim().equalsIgnoreCase(locationStr.trim()) &&
//                                                        arrayObj.getSessionName().trim().toLowerCase().equalsIgnoreCase(classNameStr.trim().toLowerCase())) {
//                                                    filterFinalArray.add(arrayObj);
//                                                    Collections.sort(filterFinalArray, new HighToLowSortRating());
//                                                    result = filterFinalArray.size();
//                                                }
//                                                fillData(filterFinalArray);
//                                            }
//                                        }
//                                    }
//                                }
////                                    } else if (!subjectStr.equalsIgnoreCase("") || !boardStr.equalsIgnoreCase("") ||
////                                            !standardStr.equalsIgnoreCase("") || !streamStr.equalsIgnoreCase("")) {
////                                        List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
////                                        for (sessionDataModel arrayObj : dataResponse.getData()) {
////                                            if (arrayObj.getAddressCity().trim().equalsIgnoreCase(locationStr.trim()) &&
////                                                    arrayObj.getSessionName().trim().toLowerCase().contains(classNameStr.trim().toLowerCase())) {
////                                                if (arrayObj.getLessionTypeName().trim().equalsIgnoreCase(subjectStr.trim()) ||
////                                                        arrayObj.getBoard().equalsIgnoreCase(boardStr.trim()) ||
////                                                        arrayObj.getStandard().equalsIgnoreCase(standardStr.trim()) ||
////                                                        arrayObj.getStream().equalsIgnoreCase(streamStr.trim()) ||
////                                                        arrayObj.getGenderID().equalsIgnoreCase(genderStr)) {
////                                                    filterFinalArray.add(arrayObj);
////                                                    result = String.valueOf(filterFinalArray.size());
////                                                }
////                                            }
////                                            fillData(filterFinalArray);
////                                        }
////                                    } else {
////                                        List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
////                                        for (sessionDataModel arrayObj : dataResponse.getData()) {
////                                            if (arrayObj.getAddressCity().trim().equalsIgnoreCase(locationStr.trim()) &&
////                                                    arrayObj.getSessionName().trim().toLowerCase().contains(classNameStr.trim().toLowerCase())) {
////                                                filterFinalArray.add(arrayObj);
////                                                result = String.valueOf(filterFinalArray.size());
////                                            }
////                                            fillData(filterFinalArray);
////                                        }
////                                    }
//                            }
//                            if (result < 10) {
//                                binding.countTxt.setText("0" + String.valueOf(result));
//                            } else {
//                                binding.countTxt.setText(String.valueOf(result));
//                            }
//                        }
//
//                    }
//
//                }
//
//                @Override
//                public void failure(RetrofitError error) {
//                    Utils.dismissDialog();
//                    error.printStackTrace();
//                    error.getMessage();
//                    Utils.ping(mContext, getString(R.string.something_wrong));
//                }
//            });
//        } else {
//            Utils.ping(mContext, getString(R.string.internet_connection_error));
//        }
//    }
//
//    private Map<String, String> getSessionListDetail() {
//        Map<String, String> map = new HashMap<>();
//        return map;
//    }
///================================

    public void SortDialog() {
        sortBinding = DataBindingUtil.
                inflate(LayoutInflater.from(mContext), R.layout.dialog_sort, (ViewGroup) binding.getRoot(), false);

        sortDialog = new Dialog(mContext, R.style.PauseDialog);
        Window window = sortDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        sortDialog.getWindow().getAttributes().verticalMargin = 0.10F;
        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);

        sortDialog.getWindow().setBackgroundDrawableResource(R.drawable.filter1);

        sortDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sortDialog.setCancelable(true);
        sortDialog.setContentView(sortBinding.getRoot());

        sortBinding.resultTxt.setText(String.valueOf(result));

        if (!rangestatusStr.equalsIgnoreCase("")) {
            if (rangestatusStr.equalsIgnoreCase("low")) {
                sortBinding.lowHighRb.setChecked(true);
            } else {
                sortBinding.highLowRb.setChecked(true);
            }
        }
        if (!ratingStr.equalsIgnoreCase("")) {
            if (ratingStr.equalsIgnoreCase("low")) {
                sortBinding.lowestFirstUserRb.setChecked(true);
            } else {
                sortBinding.highestFirstUserRb.setChecked(true);
            }
        }

        sortBinding.doneTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (pricewiseStr.equalsIgnoreCase("price")) {
//                    if (rangestatusStr.equalsIgnoreCase("low")) {
//                        if (dataResponse.getData().size() >= 0) {
//                            List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
//                            for (sessionDataModel arrayObj : dataResponse.getData()) {
//                                if (arrayObj.getAddressCity().trim().equalsIgnoreCase(locationStr.trim()) &&
//                                        arrayObj.getSessionName().trim().toLowerCase().contains(classNameStr.trim().toLowerCase())) {
//                                    filterFinalArray.add(arrayObj);
//                                    Collections.sort(filterFinalArray, new LowToHighSortSessionPrice());
//                                }
//                            }
//                            fillData(filterFinalArray);
//                            sortDialog.dismiss();
//                            pricewiseStr = "";
//                        }
//                    } else {
//                        if (dataResponse.getData().size() >= 0) {
//                            List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
//                            for (sessionDataModel arrayObj : dataResponse.getData()) {
//                                if (arrayObj.getAddressCity().trim().equalsIgnoreCase(locationStr.trim()) &&
//                                        arrayObj.getSessionName().trim().toLowerCase().contains(classNameStr.trim().toLowerCase())) {
//                                    filterFinalArray.add(arrayObj);
//                                    Collections.sort(filterFinalArray, new HightToLowSortSessionPrice());
//                                }
//                            }
//                            fillData(filterFinalArray);
//                            sortDialog.dismiss();
//                            pricewiseStr = "";
//                        }
//                    }
////                        } else {
////                            Utils.ping(mContext, "Please Select One Option");
////                        }
//                } else {
////                        if (!ratingStr.equalsIgnoreCase("")) {
//                    if (ratingStr.equalsIgnoreCase("low")) {
//                        if (dataResponse.getData().size() >= 0) {
//                            List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
//                            for (sessionDataModel arrayObj : dataResponse.getData()) {
//                                if (arrayObj.getAddressCity().trim().equalsIgnoreCase(locationStr.trim()) &&
//                                        arrayObj.getSessionName().trim().toLowerCase().contains(classNameStr.trim().toLowerCase())) {
//                                    filterFinalArray.add(arrayObj);
//                                    Collections.sort(filterFinalArray, new LowToHighSortRating());
//                                }
//                            }
//                            fillData(filterFinalArray);
//                            sortDialog.dismiss();
//                            pricewiseStr = "";
//                        }
//                    } else {
//                        List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
//                        if (dataResponse.getData().size() >= 0) {
//                            for (sessionDataModel arrayObj : dataResponse.getData()) {
//                                if (arrayObj.getAddressCity().trim().equalsIgnoreCase(locationStr.trim()) &&
//                                        arrayObj.getSessionName().trim().toLowerCase().contains(classNameStr.trim().toLowerCase())) {
//                                    filterFinalArray.add(arrayObj);
//                                    Collections.sort(filterFinalArray, new HighToLowSortRating());
//                                }
//                            }
//                            fillData(filterFinalArray);
//                            sortDialog.dismiss();
//                            pricewiseStr = "";
//                        }
//                    }
////                        } else {
////                            Utils.ping(mContext, "Please Select One Option");
////                        }
//                }

//                } else {
//                    Utils.ping(mContext, "Please Select One Option");
//                }

                sortDialog.dismiss();
            }
        });
        sortBinding.rangeStatusRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int radioButtonID = sortBinding.rangeStatusRg.getCheckedRadioButtonId();
                switch (radioButtonID) {
                    case R.id.low_high_rb:
                        ratingStr="";
                        rangestatusStr = "low";
                        pricewiseStr = "price";
                        if (dataResponse.getData().size() >= 0) {
                            List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
                            for (sessionDataModel arrayObj : dataResponse.getData()) {
                                if (arrayObj.getAddressCity().trim().equalsIgnoreCase(locationStr.trim()) &&
                                        arrayObj.getSessionName().trim().toLowerCase().contains(classNameStr.trim().toLowerCase())) {
                                    filterFinalArray.add(arrayObj);
                                    Collections.sort(filterFinalArray, new LowToHighSortSessionPrice());
                                }
                            }
                            fillData(filterFinalArray);
                            sortDialog.dismiss();
                            pricewiseStr = "";
                        }
                        break;
                    case R.id.high_low_rb:
                        ratingStr="";
                        rangestatusStr = "high";
                        pricewiseStr = "price";
                        if (dataResponse.getData().size() >= 0) {
                            List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
                            for (sessionDataModel arrayObj : dataResponse.getData()) {
                                if (arrayObj.getAddressCity().trim().equalsIgnoreCase(locationStr.trim()) &&
                                        arrayObj.getSessionName().trim().toLowerCase().contains(classNameStr.trim().toLowerCase())) {
                                    filterFinalArray.add(arrayObj);
                                    Collections.sort(filterFinalArray, new HightToLowSortSessionPrice());
                                }
                            }
                            fillData(filterFinalArray);
                            sortDialog.dismiss();
                            pricewiseStr = "";
                        }
                        break;
                    default:
                        break;
                }
            }
        });

        sortBinding.userRatingStatusRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int radioButtonRating = sortBinding.userRatingStatusRg.getCheckedRadioButtonId();
                switch (radioButtonRating) {
                    case R.id.lowest_first_user_rb:
                        rangestatusStr="";
                        ratingStr = "low";
                        pricewiseStr = "rating";
                        if (dataResponse.getData().size() >= 0) {
                            List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
                            for (sessionDataModel arrayObj : dataResponse.getData()) {
                                if (arrayObj.getAddressCity().trim().equalsIgnoreCase(locationStr.trim()) &&
                                        arrayObj.getSessionName().trim().toLowerCase().contains(classNameStr.trim().toLowerCase())) {
                                    filterFinalArray.add(arrayObj);
                                    Collections.sort(filterFinalArray, new LowToHighSortRating());
                                }
                            }
                            fillData(filterFinalArray);
                            sortDialog.dismiss();
                            pricewiseStr = "";
                        }
                        break;
                    case R.id.highest_first_user_rb:
                        rangestatusStr="";
                        ratingStr = "high";
                        pricewiseStr = "rating";
                        List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
                        if (dataResponse.getData().size() >= 0) {
                            for (sessionDataModel arrayObj : dataResponse.getData()) {
                                if (arrayObj.getAddressCity().trim().equalsIgnoreCase(locationStr.trim()) &&
                                        arrayObj.getSessionName().trim().toLowerCase().contains(classNameStr.trim().toLowerCase())) {
                                    filterFinalArray.add(arrayObj);
                                    Collections.sort(filterFinalArray, new HighToLowSortRating());
                                }
                            }
                            fillData(filterFinalArray);
                            sortDialog.dismiss();
                            pricewiseStr = "";
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        sortDialog.show();
    }

    //    //Use for SessionList
    public void callSessionListApi() {
        if (Utils.checkNetwork(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_SessionList_Search_Criteria(getSessionListDetail(), new retrofit.Callback<SessionDetailModel>() {
                @Override
                public void success(SessionDetailModel sessionInfo, Response response) {
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
                        Utils.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (sessionInfo.getSuccess().equalsIgnoreCase("True")) {
                        Utils.dismissDialog();
                        if (sessionInfo.getData().size() >= 0) {
                            dataResponse = sessionInfo;
                            fillArea();
                            List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
                            for (sessionDataModel arrayObj : dataResponse.getData()) {
                                filterFinalArray.add(arrayObj);
                            }
                            fillData(filterFinalArray);
                        }
                        Log.d("FilterArray", "" + filterFinalArray.size());

                        result = dataResponse.getData().size();
                        if (result < 10) {
                            binding.countTxt.setText("0" + String.valueOf(result));
                        } else {
                            binding.countTxt.setText(String.valueOf(result));
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
        }

    }

    private Map<String, String> getSessionListDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("SessionName", classNameStr);
        map.put("AddressCity", locationStr);
        map.put("LessonTypeName", subjectStr);
        map.put("BoardName", boardStr);
        map.put("StandardName", standardStr);
        map.put("StreamName", streamStr);
        map.put("Gender_ID", genderStr);
        map.put("CoachType_ID", sessionType);


        return map;
    }


    public void fillData(List<sessionDataModel> array) {
        classDetailAdapter = new ClassDetailAdapter(mContext, array, searchByStr, locationStr,
                classNameStr, boardStr, streamStr, standardStr, searchTypeStr, wheretoComeStr, searchfront, sessionType, firsttimesearch, new onViewClick() {
            @Override
            public void getViewClick() {
                if (Utils.getPref(mContext, "LoginType").equalsIgnoreCase("Family")) {
                    addRating();
                } else {
                    new android.support.v7.app.AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AppTheme))
                            .setCancelable(false)
                            .setTitle("Login")
                            .setIcon(mContext.getResources().getDrawable(R.drawable.safari))
                            .setMessage("You are not login,So Please Login.")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intentLogin = new Intent(mContext, LoginActivity.class);
                                    intentLogin.putExtra("frontLogin", "afterLogin");
                                    intentLogin.putExtra("sessionID", sessionId);
                                    intentLogin.putExtra("SearchBy", searchByStr);
                                    intentLogin.putExtra("board", boardStr);
                                    intentLogin.putExtra("stream", streamStr);
                                    intentLogin.putExtra("standard", standardStr);
                                    intentLogin.putExtra("city", locationStr);
                                    intentLogin.putExtra("sessionName", classNameStr);
                                    intentLogin.putExtra("searchType", searchTypeStr);
                                    intentLogin.putExtra("lessionName", subjectStr);
                                    intentLogin.putExtra("gender", genderStr);
                                    intentLogin.putExtra("withOR", wheretoComeStr);
                                    intentLogin.putExtra("ratingLogin", "ratingLoginclass");
                                    intentLogin.putExtra("searchfront", searchfront);
                                    intentLogin.putExtra("sessionType", sessionType);
                                    intentLogin.putExtra("firsttimesearch", firsttimesearch);
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
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        binding.classListRecView.setLayoutManager(mLayoutManager);
        binding.classListRecView.setItemAnimator(new DefaultItemAnimator());
        binding.classListRecView.setAdapter(classDetailAdapter);

    }

    public void fillArea() {
        ArrayList<String> AreaName = new ArrayList<String>();

        for (int j = 0; j < dataResponse.getData().size(); j++) {
            if (locationStr.trim().toLowerCase().contains(dataResponse.getData().get(j).getAddressCity().trim().toLowerCase())
                    && classNameStr.trim().toLowerCase().contains(dataResponse.getData().get(j).getSessionName().trim().toLowerCase())) {
                AreaName.add(dataResponse.getData().get(j).getRegionName());
            }
        }
        HashSet<String> hashSet = new HashSet<String>();
        hashSet.addAll(AreaName);
        AreaName.clear();
        AreaName.addAll(hashSet);
        ArrayAdapter<String> adapterTerm = new ArrayAdapter<String>(mContext, R.layout.autocomplete_layout, AreaName);
        binding.multiautocompe.setThreshold(1);
        binding.multiautocompe.setAdapter(adapterTerm);
        binding.multiautocompe.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    }

    public void addRating() {
        ArrayList<String> selectedId = new ArrayList<String>();
        String sessionName = "";
        selectedId = classDetailAdapter.getSessionDetail();
        Log.d("selectedId", "" + selectedId);
        for (int i = 0; i < selectedId.size(); i++) {
            String[] splitvalue = selectedId.get(i).split("\\|");
            sessionName = splitvalue[0];
            sessionId = splitvalue[1];
        }

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.rating_dialog_layout, null);
        final RatingBar ratingBar = alertLayout.findViewById(R.id.rating_bar);
        final TextView sessionNametxt = alertLayout.findViewById(R.id.session_name_txt);
        final TextView session_rating_view_txt = alertLayout.findViewById(R.id.session_rating_view_txt);
        final TextView cancel_txt = alertLayout.findViewById(R.id.cancel_txt);
        final TextView confirm_txt = alertLayout.findViewById(R.id.confirm_txt);
        final EditText comment_edt = alertLayout.findViewById(R.id.comment_edt);
        sessionNametxt.setText(sessionName);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (b) {
                    int rating = (int) ratingBar.getRating();
                    if (rating == 1) {
                        session_rating_view_txt.setText("Very poor");
                        session_rating_view_txt.setTextColor(getResources().getColor(R.color.remarks));
                    } else if (rating == 2) {
                        session_rating_view_txt.setText("Poor");
                        session_rating_view_txt.setTextColor(getResources().getColor(R.color.remarks));
                    } else if (rating == 3) {
                        session_rating_view_txt.setText("Average");
                        session_rating_view_txt.setTextColor(getResources().getColor(R.color.rating_bar));
                    } else if (rating == 4) {
                        session_rating_view_txt.setText("Good");
                        session_rating_view_txt.setTextColor(getResources().getColor(R.color.present));
                    } else if (rating == 5) {
                        session_rating_view_txt.setText("Excellent");
                        session_rating_view_txt.setTextColor(getResources().getColor(R.color.present));
                    }
                }
            }
        });

        AlertDialog.Builder sayWindows = new AlertDialog.Builder(
                mContext);

        sayWindows.setPositiveButton("Rate", null);
        sayWindows.setNegativeButton("Not Now", null);
        sayWindows.setView(alertLayout);

        final AlertDialog mAlertDialog = sayWindows.create();
        mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button b = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Do something
                        String rating = String.valueOf(ratingBar.getRating());
//                Toast.makeText(getApplicationContext(), rating, Toast.LENGTH_LONG).show();
                        commentStr = comment_edt.getText().toString();
                        if (commentStr.equalsIgnoreCase("")) {
                            commentStr = session_rating_view_txt.getText().toString();
                        }
                        ratingValueStr = String.valueOf(ratingBar.getRating());
                        if (!Utils.getPref(mContext, "coachID").equalsIgnoreCase("")) {
                            if (!ratingValueStr.equalsIgnoreCase("0.0")) {
                                callAddrating();
                                mAlertDialog.dismiss();
                            } else {
                                Utils.ping(mContext, "Please Select Rate.");
                            }
                        } else {
                            Utils.ping(mContext, getResources().getString(R.string.not_loging));
                        }
                    }
                });
            }
        });
        mAlertDialog.show();

    }

    //Use for AddRating
    public void callAddrating() {
        if (Utils.isNetworkConnected(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().Add_Session_Rating(getratingDetail(), new retrofit.Callback<SessionDetailModel>() {
                @Override
                public void success(SessionDetailModel addratingmodel, Response response) {
                    Utils.dismissDialog();
                    if (addratingmodel == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (addratingmodel.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (addratingmodel.getSuccess().equalsIgnoreCase("false")) {
                        Utils.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (addratingmodel.getSuccess().equalsIgnoreCase("True")) {
                        Utils.dismissDialog();
                        callSessionListApi();
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

    private Map<String, String> getratingDetail() {

        Map<String, String> map = new HashMap<>();
        map.put("SessionID", sessionId);
        map.put("ContactID", Utils.getPref(mContext, "coachID"));
        map.put("Comment", commentStr);
        map.put("RatingValue", ratingValueStr);

        return map;
    }

    //Use for PopularSessionList
    public void callPopularSessionListApi() {
        if (Utils.checkNetwork(mContext)) {

//            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_Popular_Session_List(getPopularSessionListDetail(), new retrofit.Callback<SessionDetailModel>() {
                @Override
                public void success(SessionDetailModel sessionInfo, Response response) {
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
                        Utils.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (sessionInfo.getSuccess().equalsIgnoreCase("True")) {
                        Utils.dismissDialog();
                        if (sessionInfo.getData().size() > 0) {
                            populardataresponse = sessionInfo;
                            arrayListPopular = new ArrayList<sessionDataModel>();
                            for (int i = 0; i < populardataresponse.getData().size(); i++) {
                                arrayListPopular.add(populardataresponse.getData().get(i));
                            }
                            count = arrayListPopular.size();
                            if (count < 10) {
                                popularBinding.resultTxt.setText("0" + String.valueOf(count));
                            } else {
                                popularBinding.resultTxt.setText(String.valueOf(count));
                            }
//                            result_txt.setText(arrayListPopular.size());
                            popularClassListAdapter = new PopularClassListAdapter(mContext, arrayListPopular, new onViewClick() {
                                @Override
                                public void getViewClick() {
                                    ArrayList<String> selectedId = new ArrayList<String>();
                                    String sessionName = "";
                                    selectedId = popularClassListAdapter.getSessionDetail();
                                    Log.d("selectedId", "" + selectedId);
                                    for (int i = 0; i < selectedId.size(); i++) {
                                        popularsessionID = selectedId.get(i);
                                    }
                                    Log.d("popularsessionID", "" + popularsessionID);
                                }
                            });
                            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext, 2);
                            popularBinding.popularListRcView.setLayoutManager(mLayoutManager);
                            popularBinding.popularListRcView.setItemAnimator(new DefaultItemAnimator());
                            popularBinding.popularListRcView.setAdapter(popularClassListAdapter);
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

    private Map<String, String> getPopularSessionListDetail() {
        Map<String, String> map = new HashMap<>();
        return map;
    }

    public void calculateHours(String time1, String time2) {
        Date date1, date2;
        int days, hours = 0, min = 0;
        String hourstr, minstr;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        try {
            date1 = simpleDateFormat.parse(time1);
            date2 = simpleDateFormat.parse(time2);

            long difference = date2.getTime() - date1.getTime();
            days = (int) (difference / (1000 * 60 * 60 * 24));
            hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
            min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
            hours = (hours < 0 ? -hours : hours);
            SessionHour = hours;
            SessionMinit = min;
            Log.i("======= Hours", " :: " + hours + ":" + min);

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }


}