package com.adms.searchclasses.Activites;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.adms.searchclasses.Adapter.ClassDetailAdapter;
import com.adms.searchclasses.Adapter.PopularClassListAdapter;
import com.adms.searchclasses.Adapter.SearchAreaAdapter;
import com.adms.searchclasses.AppConstant.ApiHandler;
import com.adms.searchclasses.AppConstant.AtoZ;
import com.adms.searchclasses.AppConstant.HighToLowSortRating;
import com.adms.searchclasses.AppConstant.HightToLowSortSessionPrice;
import com.adms.searchclasses.AppConstant.LowToHighSortRating;
import com.adms.searchclasses.AppConstant.LowToHighSortSessionPrice;
import com.adms.searchclasses.AppConstant.Utils;
import com.adms.searchclasses.AppConstant.ZtoA;
import com.adms.searchclasses.BottomNavigationViewHelper;
import com.adms.searchclasses.Interface.bookClick;
import com.adms.searchclasses.Interface.onViewClick;
import com.adms.searchclasses.Model.Session.SessionDetailModel;
import com.adms.searchclasses.Model.Session.sessionDataModel;
import com.adms.searchclasses.Model.TeacherInfo.TeacherInfoModel;
import com.adms.searchclasses.R;
import com.adms.searchclasses.databinding.ActivityClassDeatilScreenBinding;
import com.adms.searchclasses.databinding.ChangePasswordDialogBinding;
import com.adms.searchclasses.databinding.DialogFilterBinding;
import com.adms.searchclasses.databinding.DialogPopularBinding;
import com.adms.searchclasses.databinding.DialogPriceBinding;
import com.adms.searchclasses.databinding.DialogSortBinding;
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
import java.util.Locale;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class ClassDeatilScreen extends AppCompatActivity implements View.OnClickListener {

    public Dialog popularDialog, priceDialog, sortDialog, filterDialog;
    ActivityClassDeatilScreenBinding binding;
    DialogPopularBinding popularBinding;
    DialogPriceBinding priceBinding;
    DialogSortBinding sortBinding;
    DialogFilterBinding filterBinding;


    Context mContext;
    ClassDetailAdapter classDetailAdapter;

    List<sessionDataModel> arrayListPopular;
    PopularClassListAdapter popularClassListAdapter;
    ArrayList<String> AreaName;
    SearchAreaAdapter searchAreaAdapter;
    ArrayList<String> dialogselectarea;

    String subjectStr, boardStr = "", standardStr = "", streamStr = "",sessionName = "",
            locationStr, classNameStr, genderStr = "", sessionId, commentStr, ratingValueStr, popularsessionID = "", TeacherName = "",
            populardurationStr = "", populargenderStr = "", popluarsessiondateStr = "", firsttimesearch, RegionName = "", SearchPlaystudy, dialogselectareaStr = "";
    SessionDetailModel dataResponse, populardataresponse, areadataResponse;
    List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
    boolean arrayfirst = true;


    String rangestatusStr = "", afterfilterresult, pricewiseStr = "", ratingStr = "", searchfront;
    int count = 0, result = 0;
    int SessionHour = 0;
    Integer SessionMinit = 0;
    ArrayList<Integer> totalHours;
    ArrayList<Integer> totalMinit;
    int avgHoursvalue, avgMinitvalue;

    //Use for Menu Dialog
    String passWordStr, confirmpassWordStr, currentpasswordStr, wheretocometypeStr;
    Dialog menuDialog, changeDialog;
    Button btnHome,btnMyReport, btnMySession, btnChangePassword, btnaddChild, btnLogout, btnmyfamily,btnMyenroll,btnMyprofile;
    TextView userNameTxt;
    ChangePasswordDialogBinding changePasswordDialogBinding;

    //area
    ArrayList<sessionDataModel> afterremoveduplicate;
    ArrayList<sessionDataModel> beforeremoveduplicate;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_class_deatil_screen);
        mContext = ClassDeatilScreen.this;
       // setTypeface();
        init();
        setListner();
    }

    public void setTypeface() {
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "font/TitilliumWeb-Regular.ttf");

        binding.subjectTxt.setTypeface(custom_font);
        binding.cityTxt.setTypeface(custom_font);
        binding.boardTxt.setTypeface(custom_font);
        binding.standardTxt.setTypeface(custom_font);
        binding.streamTxt.setTypeface(custom_font);
        binding.multiautocompe.setTypeface(custom_font);
        binding.inforTxt.setTypeface(custom_font);
        binding.noRecordTxt.setTypeface(custom_font);
    }

    public void init() {
        BottomNavigationViewHelper.removeShiftMode(binding.bottomNavigationView);
        validData();


        if (!Utils.getPref(mContext, "LoginType").equalsIgnoreCase("Family")) {
            binding.menu.setVisibility(View.GONE);
        } else {
            binding.menu.setVisibility(View.VISIBLE);
        }
        if (!RegionName.equalsIgnoreCase("")) {
            binding.header1Linear.setVisibility(View.VISIBLE);
            dialogselectareaStr=RegionName;
            binding.header1Linear.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, .4f));
            binding.headerLinear.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.7f));
            binding.recViewLinear.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0, 3.9f));
            binding.bottomNavigationView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        } else {
            binding.headerLinear.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.7f));
            binding.recViewLinear.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 4.3f));
            binding.bottomNavigationView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            binding.header1Linear.setVisibility(View.GONE);
        }

        callSessionListApi();

    }

    public void setListner() {

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
//                    case R.id.action_popular:
//                        PopularDialog();
//                        break;
                    case R.id.action_price:
                        PriceDialog();
                        break;
                    case R.id.action_sort:
                        SortDialog();
                        break;
                    case R.id.action_filter:
                        FilterDialog();
                        break;
                        default:
                }
                return true;
            }
        });
        binding.backImg.setOnClickListener(this);
        binding.searchImg.setOnClickListener(this);
        binding.addBtn.setOnClickListener(this);
        binding.menu.setOnClickListener(this);
//        binding.multiautocompe.setOnClickListener(this);
//        binding.multiautocompe.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {
//                Log.d("valueOf OnTextChange", "" + charSequence + i + i1);
//                if (charSequence.length() == 0) {
////                    binding.inforTxt.setVisibility(View.VISIBLE);
//                    if (dataResponse.getSuccess().equalsIgnoreCase("True")) {
//                        if (dataResponse.getData().size() >= 0) {
//                            List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
//                            for (sessionDataModel arrayObj : dataResponse.getData()) {
//                                if (arrayObj.getAddressCity().trim().toLowerCase().contains(locationStr.trim().toLowerCase()) &&
//                                        arrayObj.getSessionName().trim().toLowerCase().contains(classNameStr.trim().toLowerCase())) {
//                                    filterFinalArray.add(arrayObj);
//                                }
//                            }
//                            fillData(filterFinalArray);
//                        }
//                    } else {
//                        Utils.ping(mContext, "Location not found.");
//                    }
//                    arrayfirst = true;
//
//                } else {
//                    if (dataResponse.getSuccess().equalsIgnoreCase("True")) {
//                        if (dataResponse.getData().size() >= 0) {
//                            String[] spilt = charSequence.toString().trim().split("\\,");
//                            Log.d("spiltOnText", "" + spilt.length);
//                            for (int k = 0; k < spilt.length; k++) {
//                                List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
//                                for (sessionDataModel arrayObj : dataResponse.getData()) {
//                                    if (arrayObj.getAddressCity().trim().toLowerCase().contains(locationStr.trim().toLowerCase()) &&
//                                            arrayObj.getSessionName().trim().toLowerCase().contains(classNameStr.trim().toLowerCase())) {
//                                        if (arrayObj.getRegionName().trim().toLowerCase().contains(spilt[k].trim().toLowerCase())) {
//                                            filterFinalArray.add(arrayObj);
//                                        }
//                                    }
//                                }
//                                fillData(filterFinalArray);
//                            }
//                        }
//                    } else {
//                        Utils.ping(mContext, "Location not found.");
//                    }
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//            }
//        });
        ///////////////////////
//
//        binding.multiautocompe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                binding.inforTxt.setVisibility(View.GONE);
//                if (arrayfirst) {
//                    filterFinalArray = new ArrayList<sessionDataModel>();
//                }
//                String str = binding.multiautocompe.getText().toString();
//                if (!str.equalsIgnoreCase("")) {
//                    arrayfirst = false;
//                    String[] spilt = adapterView.getItemAtPosition(i).toString().trim().split("\\,");
//
//                    for (sessionDataModel arrayObj : dataResponse.getData()) {
//                        if (arrayObj.getAddressCity().trim().equalsIgnoreCase(locationStr.trim()) &&
//                                arrayObj.getSessionName().trim().toLowerCase().contains(classNameStr.trim().toLowerCase())) {
//                            if (arrayObj.getRegionName().trim().toLowerCase().contains((spilt[0]).trim().toLowerCase())) {
//                                filterFinalArray.add(arrayObj);
//
//                            }
//                        }
//                    }
//                    Log.d("FilterArray", "" + filterFinalArray.size());
//                    fillData(filterFinalArray);
//                } else {
//                    List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
//                    for (sessionDataModel arrayObj : dataResponse.getData()) {
//                        if (arrayObj.getAddressCity().trim().toLowerCase().contains(locationStr.trim().toLowerCase()) &&
//                                arrayObj.getSessionName().trim().toLowerCase().contains(classNameStr.trim().toLowerCase())) {
//                            filterFinalArray.add(arrayObj);
//                        }
//                    }
//                    Log.d("FilterArray", "" + filterFinalArray.size());
//                    fillData(filterFinalArray);
//                }
//            }
//        });
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
                insession.putExtra("flag", SearchPlaystudy);
                insession.putExtra("searchfront", "searchunder");
                insession.putExtra("city", locationStr);
                insession.putExtra("sessionName", classNameStr);
                insession.putExtra("lessionName", subjectStr);
                insession.putExtra("board", boardStr);
                insession.putExtra("standard", standardStr);
                insession.putExtra("stream", streamStr);
                insession.putExtra("gender", genderStr);
                insession.putExtra("firsttimesearch", firsttimesearch);
                insession.putExtra("RegionName", RegionName);
                insession.putExtra("SearchPlaystudy", SearchPlaystudy);
                insession.putExtra("TeacherName", TeacherName);
                startActivity(insession);
                break;
            case R.id.multiautocompe:
//                binding.multiautocompe.showDropDown();
                break;
            case R.id.add_btn:
                FilterDialog();
                break;
            case R.id.menu:
                menuDialog();
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
//                List<SessionDetailModel> arrayList = new ArrayList<SessionDetailModel>();
//                totalHours = new ArrayList<>();
//                totalMinit = new ArrayList<>();
//                if (!popularsessionID.equalsIgnoreCase("")) {
//                    List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
//                    for (int i = 0; i < dataResponse.getData().size(); i++) {
//                        if (popularsessionID.equalsIgnoreCase(dataResponse.getData().get(i).getSessionID())) {
//                            String[] spiltPipes = dataResponse.getData().get(i).getSchedule().split("\\|");
//                            String[] spiltComma;
//                            String[] spiltDash;
//                            Log.d("spilt", "" + spiltPipes.toString());
//                            for (int j = 0; j < spiltPipes.length; j++) {
//                                spiltComma = spiltPipes[j].split("\\,");
//                                spiltDash = spiltComma[1].split("\\-");
//                                calculateHours(spiltDash[0], spiltDash[1]);
//                            }
//                            populargenderStr = dataResponse.getData().get(i).getGenderID();
//                            popluarsessiondateStr = dataResponse.getData().get(i).getStartDate() + " To " + dataResponse.getData().get(i).getEndDate();
//                        }
//                    }
//                    averageHours(totalHours);
//                    averageMinit(totalMinit);
//                    if (avgMinitvalue==0) {
//                        populardurationStr = String.valueOf(avgHoursvalue);
//                    }else{
//                        populardurationStr=avgHoursvalue + " hr " + avgMinitvalue + " min";
//                    }
//                    Log.d("duration,+Time", populardurationStr + "" + popluarsessiondateStr);
//
//                    Intent inSession = new Intent(mContext, SessionName.class);
//                    inSession.putExtra("sessionID", popularsessionID);
////                    inSession.putExtra("SearchBy", searchByStr);
//                    inSession.putExtra("city", locationStr);
//                    inSession.putExtra("sessionName", classNameStr);
//                    inSession.putExtra("board", boardStr);
//                    inSession.putExtra("stream", streamStr);
//                    inSession.putExtra("standard", standardStr);
//                    inSession.putExtra("lessionName", subjectStr);
//                    inSession.putExtra("sessiondate", popluarsessiondateStr);
//                    inSession.putExtra("duration", populardurationStr);
//                    inSession.putExtra("gender", populargenderStr);
////                    inSession.putExtra("searchType", searchTypeStr);
////                    inSession.putExtra("withOR", wheretoComeStr);
//                    inSession.putExtra("searchfront", searchfront);
//                    mContext.startActivity(inSession);
//                    popularDialog.dismiss();
//                } else {
                popularDialog.dismiss();
//                }
            }
        });

        popularDialog.show();
    }

    public void PriceDialog() {

        priceBinding = DataBindingUtil.
                inflate(LayoutInflater.from(mContext), R.layout.dialog_price, (ViewGroup) binding.getRoot(), false);

        priceDialog = new Dialog(mContext);//,R.style.PauseDialog);//, R.style.PauseDialog);//
        Window window = priceDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        priceDialog.getWindow().getAttributes().verticalMargin = 0.10F;
        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);

        priceDialog.getWindow().setBackgroundDrawableResource(R.drawable.box);

        priceDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        priceDialog.setCancelable(true);
        priceDialog.setContentView(priceBinding.getRoot());
        dialogselectareaStr = "";
        if (dialogselectareaStr.equalsIgnoreCase("")) {
            binding.headerLinear.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.7f));
            binding.recViewLinear.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 4.3f));
            binding.bottomNavigationView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            binding.header1Linear.setVisibility(View.GONE);
            binding.multiautocompe.setHint(getResources().getString(R.string.location_add));
            binding.multiautocompe.setText("");
            List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
            for (sessionDataModel arrayObj : dataResponse.getData()) {
                filterFinalArray.add(arrayObj);
            }
            fillData(filterFinalArray);
        }
       // priceBinding.resultTxt.setText(String.valueOf(result));
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
                priceBinding.priceRange1Txt.setText("₹" + String.valueOf(minValue) + "/-");
                priceBinding.priceRange2Txt.setText("₹" + String.valueOf(maxValue) + "/-");
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
                                //priceBinding.result1Txt.setText(afterfilterresult);
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
                priceBinding.priceRange1Txt.setText("₹" + String.valueOf(minValue) + "/-");
                priceBinding.priceRange2Txt.setText("₹" + String.valueOf(maxValue) + "/-");
                Log.d("final vlue", "min value" + minValue + "maxvalue" + maxValue);
            }
        });
        priceDialog.show();


    }

    public void SortDialog() {
        sortBinding = DataBindingUtil.
                inflate(LayoutInflater.from(mContext), R.layout.dialog_sort, (ViewGroup) binding.getRoot(), false);

        sortDialog = new Dialog(mContext);//,R.style.PauseDialog);//, R.style.PauseDialog);
        Window window = sortDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        sortDialog.getWindow().getAttributes().verticalMargin = 0.10F;
        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);

        sortDialog.getWindow().setBackgroundDrawableResource(R.drawable.filter1_1);

        sortDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sortDialog.setCancelable(true);
        sortDialog.setContentView(sortBinding.getRoot());

        sortBinding.resultTxt.setText(String.valueOf(result));

//        if (!rangestatusStr.equalsIgnoreCase("")) {
//            if (rangestatusStr.equalsIgnoreCase("low")) {
//                sortBinding.lowHighRb.setChecked(true);
//            } else {
//                sortBinding.highLowRb.setChecked(true);
//            }
//        }
//        if (!ratingStr.equalsIgnoreCase("")) {
//            if (ratingStr.equalsIgnoreCase("low")) {
//                sortBinding.lowestFirstUserRb.setChecked(true);
//            } else {
//                sortBinding.highestFirstUserRb.setChecked(true);
//            }
//        }
        dialogselectareaStr = "";
        if (dialogselectareaStr.equalsIgnoreCase("")) {
            binding.headerLinear.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.7f));
            binding.recViewLinear.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 4.3f));
            binding.bottomNavigationView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            binding.header1Linear.setVisibility(View.GONE);
            binding.multiautocompe.setHint(getResources().getString(R.string.location_add));
            binding.multiautocompe.setText("");
            List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
            for (sessionDataModel arrayObj : dataResponse.getData()) {
                filterFinalArray.add(arrayObj);
            }
            fillData(filterFinalArray);
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
                        ratingStr = "";
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
                        ratingStr = "";
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
                        rangestatusStr = "";
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
                        rangestatusStr = "";
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
        sortBinding.asceDesendRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int radioButtonLocation = sortBinding.asceDesendRg.getCheckedRadioButtonId();
                switch (radioButtonLocation) {
                    case R.id.asce_rb:
                        if (dataResponse.getData().size() >= 0) {
                            List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
                            for (sessionDataModel arrayObj : dataResponse.getData()) {
                                if (arrayObj.getAddressCity().trim().equalsIgnoreCase(locationStr.trim()) &&
                                        arrayObj.getSessionName().trim().toLowerCase().contains(classNameStr.trim().toLowerCase())) {
                                    filterFinalArray.add(arrayObj);

                                }
                            }
                            Collections.sort(filterFinalArray, new AtoZ());
                            fillData(filterFinalArray);
                            sortDialog.dismiss();
                        }
                        break;
                    case R.id.decending_rb:
                        if (dataResponse.getData().size() >= 0) {
                            List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
                            for (sessionDataModel arrayObj : dataResponse.getData()) {
                                if (arrayObj.getAddressCity().trim().equalsIgnoreCase(locationStr.trim()) &&
                                        arrayObj.getSessionName().trim().toLowerCase().contains(classNameStr.trim().toLowerCase())) {
                                    filterFinalArray.add(arrayObj);

                                }
                            }
                            Collections.sort(filterFinalArray, new ZtoA());
                            fillData(filterFinalArray);
                            sortDialog.dismiss();
                        }
                        break;
                }
            }
        });
        sortDialog.show();
    }


    public void FilterDialog() {
        filterBinding = DataBindingUtil.
                inflate(LayoutInflater.from(mContext), R.layout.dialog_filter, (ViewGroup) binding.getRoot(), false);

        filterDialog = new Dialog(mContext);//,R.style.PauseDialog);//, R.style.PauseDialog);
        Window window = filterDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        filterDialog.getWindow().getAttributes().verticalMargin = 0.10F;
        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);

        filterDialog.getWindow().setBackgroundDrawableResource(R.drawable.poop_p3);

        filterDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        filterDialog.setCancelable(true);
        filterDialog.setContentView(filterBinding.getRoot());

        filterBinding.doneTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogselectareaStr = "";
                dialogselectarea = new ArrayList<>();
                for (int i = 0; i < searchAreaAdapter.getCount(); i++) {
                    sessionDataModel sessionDetailModel = (sessionDataModel) searchAreaAdapter.getItem(i);
                    String status = sessionDetailModel.getCheckStatus();
                    if (status.equalsIgnoreCase("1")) {
                        dialogselectarea.add(sessionDetailModel.getCheckValue());
                    }
                }
                Log.d("Area", dialogselectarea.toString());

//                for (String s : dialogselectarea) {
//                    if (!s.equals("")) {
//                        dialogselectareaStr = dialogselectareaStr + " , " + s;
//                    }
//
//                }
                dialogselectareaStr = dialogselectarea.toString();
                if (!dialogselectareaStr.equalsIgnoreCase("")) {
                    dialogselectareaStr = dialogselectareaStr.substring(1, dialogselectareaStr.length() - 1);
                }
                if (!dialogselectareaStr.equalsIgnoreCase("")) {
                    Log.d("dialogselectareaStr ", dialogselectareaStr);
                    binding.multiautocompe.setText(dialogselectareaStr);
                    binding.multiautocompe.setEnabled(false);
                    binding.header1Linear.setVisibility(View.VISIBLE);
                    binding.header1Linear.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, .4f));
                    binding.headerLinear.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.7f));
                    binding.recViewLinear.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0, 3.9f));
                    binding.bottomNavigationView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
                    for (int i = 0; i < dialogselectarea.size(); i++) {
                        for (int j = 0; j < dataResponse.getData().size(); j++) {
                            if (dataResponse.getData().get(j).getRegionName().trim().toLowerCase().contains(dialogselectarea.get(i).trim().toLowerCase())) {
                                filterFinalArray.add(dataResponse.getData().get(j));
                            }
                        }
                    }

                    fillData(filterFinalArray);
                } else {
                    binding.headerLinear.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.7f));
                    binding.recViewLinear.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 4.3f));
                    binding.bottomNavigationView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    binding.header1Linear.setVisibility(View.GONE);
                    binding.multiautocompe.setHint(getResources().getString(R.string.location_add));
                    binding.multiautocompe.setText("");
                    List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
                    for (sessionDataModel arrayObj : dataResponse.getData()) {
                        filterFinalArray.add(arrayObj);
                    }
                    fillData(filterFinalArray);
                }
                filterDialog.dismiss();
            }
        });


        if (dialogselectareaStr.equalsIgnoreCase("")) {
            for (int j = 0; j < dataResponse.getData().size(); j++) {
                dataResponse.getData().get(j).setCheckStatus("0");
            }
        }
// else {
//            String[] split = dialogselectareaStr.split("\\,");
//            for (int i = 0; i < split.length; i++) {
//                for (int j = 0; j < dataResponse.getData().size(); j++) {
//                    if (dataResponse.getData().get(j).getRegionName().equalsIgnoreCase(split[i])) {
//                        dataResponse.getData().get(j).setCheckStatus("1");
//                    } else {
//                        dataResponse.getData().get(j).setCheckStatus("0");
//                    }
//                }
//            }
//        }

        if (searchAreaAdapter == null) {
            searchAreaAdapter = new SearchAreaAdapter(mContext, dataResponse, dialogselectareaStr);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
//        filterBinding.displayAreaList.setLayoutManager(mLayoutManager);
//        filterBinding.displayAreaList.setItemAnimator(new DefaultItemAnimator());
        }
        filterBinding.displayAreaList.setAdapter(searchAreaAdapter);
        filterDialog.show();
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
                        Utils.ping(mContext, "Class not found");
                        dataResponse = sessionInfo;

                        return;
                    }
                    if (sessionInfo.getSuccess().equalsIgnoreCase("True")) {
                        Utils.dismissDialog();
                        if (sessionInfo.getData().size() >= 0) {
                            dataResponse = sessionInfo;
                            areadataResponse = sessionInfo;
                            fillArea();
                            List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
                            for (sessionDataModel arrayObj : dataResponse.getData()) {
                                filterFinalArray.add(arrayObj);
                            }
                            fillData(filterFinalArray);
                        }
                        Log.d("FilterArray", "" + filterFinalArray.size());

                        result = dataResponse.getData().size();
//                        if (result < 10) {
//                            binding.countTxt.setText("0" + String.valueOf(result));
//                        } else {
//                            binding.countTxt.setText(String.valueOf(result));
//                        }
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
        map.put("SessionName",classNameStr);
        map.put("AddressCity", locationStr);
        map.put("LessonTypeName",subjectStr );
        map.put("BoardName", boardStr);
        map.put("StandardName", standardStr);
        map.put("StreamName", streamStr);
        map.put("Gender_ID", genderStr);
        map.put("CoachType_ID", SearchPlaystudy);
        map.put("RegionName", RegionName);
        map.put("TeacherName", TeacherName);

        return map;
    }


    public void fillData(List<sessionDataModel> array) {
        classDetailAdapter = new ClassDetailAdapter(mContext, array, locationStr,
                classNameStr, boardStr, streamStr, standardStr, SearchPlaystudy, /*searchTypeStr, wheretoComeStr, searchByStr,*/
                searchfront, firsttimesearch, RegionName, TeacherName, new bookClick() {
            @Override
            public void bookClick() {
                ArrayList<String> selectedId = new ArrayList<String>();
                String sessionName = "";
                String[] splitvalue = new String[0];
                selectedId = classDetailAdapter.getSessionBookDetail();
                Log.d("selectedId", "" + selectedId);
                for (int i = 0; i < selectedId.size(); i++) {
                    splitvalue = selectedId.get(i).split("\\|");
//                    sessionName = splitvalue[0];
                    sessionId = splitvalue[0];
                }
                if (Utils.getPref(mContext, "LoginType").equalsIgnoreCase("Family")) {
                    Intent intentLogin = new Intent(mContext, FamilyListActivity.class);
                    intentLogin.putExtra("froncontanct", "false");
                    intentLogin.putExtra("back", "classDeatil");
                    intentLogin.putExtra("sessionID", sessionId);
                    intentLogin.putExtra("city", locationStr);
                    intentLogin.putExtra("sessionName", classNameStr);
                    intentLogin.putExtra("frontLogin", "afterLogin");
                    intentLogin.putExtra("board", boardStr);
                    intentLogin.putExtra("stream", streamStr);
                    intentLogin.putExtra("standard", standardStr);
                    intentLogin.putExtra("lessionName", subjectStr);
                    intentLogin.putExtra("gender", genderStr);
                    intentLogin.putExtra("searchfront", searchfront);
                    intentLogin.putExtra("firsttimesearch", firsttimesearch);
                    intentLogin.putExtra("RegionName", RegionName);
                    intentLogin.putExtra("SearchPlaystudy", SearchPlaystudy);
                    intentLogin.putExtra("TeacherName", TeacherName);
                    startActivity(intentLogin);
                    finish();
                } else {
                    new android.support.v7.app.AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AlertDialogTheme))
                            .setCancelable(false)
//                            .setTitle("Login")
//                            .setIcon(mContext.getResources().getDrawable(R.drawable.safari))
                            .setMessage(getResources().getString(R.string.loginmsg))
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intentLogin = new Intent(mContext, LoginActivity.class);
                                    intentLogin.putExtra("frontLogin", "afterLogin");
                                    intentLogin.putExtra("sessionID", sessionId);
                                    intentLogin.putExtra("board", boardStr);
                                    intentLogin.putExtra("stream", streamStr);
                                    intentLogin.putExtra("standard", standardStr);
                                    intentLogin.putExtra("city", locationStr);
                                    intentLogin.putExtra("sessionName", classNameStr);
                                    intentLogin.putExtra("lessionName", subjectStr);
                                    intentLogin.putExtra("gender", genderStr);
                                    intentLogin.putExtra("ratingLogin", "booksession");
                                    intentLogin.putExtra("searchfront", searchfront);
                                    intentLogin.putExtra("firsttimesearch", firsttimesearch);
                                    intentLogin.putExtra("RegionName", RegionName);
                                    intentLogin.putExtra("back", "classDeatil");
                                    intentLogin.putExtra("SearchPlaystudy", SearchPlaystudy);
                                    intentLogin.putExtra("TeacherName", TeacherName);
                                    startActivity(intentLogin);
                                    finish();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing

                                }
                            })
//                            .setIcon(R.drawable.safari)
                            .show();
                }
            }
        }, new onViewClick() {
            @Override
            public void getViewClick() {
                ArrayList<String> selectedId = new ArrayList<String>();
                String[] splitvalue = new String[0];
                selectedId = classDetailAdapter.getSessionDetail();
                Log.d("selectedId", "" + selectedId);
                for (int i = 0; i < selectedId.size(); i++) {
                    splitvalue = selectedId.get(i).split("\\|");
                    classNameStr = splitvalue[0];
                    sessionId = splitvalue[1];
                    TeacherName=splitvalue[2];
                }
                if (Utils.getPref(mContext, "LoginType").equalsIgnoreCase("Family")) {
                    addRating();
                } else {
                    new android.support.v7.app.AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AlertDialogTheme))
                            .setCancelable(false)
//                            .setIcon(mContext.getResources().getDrawable(R.drawable.safari))
                            .setMessage(getResources().getString(R.string.loginmsg))
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intentLogin = new Intent(mContext, LoginActivity.class);
                                    intentLogin.putExtra("frontLogin", "afterLogin");
                                    intentLogin.putExtra("sessionID", sessionId);
                                    intentLogin.putExtra("board", boardStr);
                                    intentLogin.putExtra("stream", streamStr);
                                    intentLogin.putExtra("standard", standardStr);
                                    intentLogin.putExtra("city", locationStr);
                                    intentLogin.putExtra("sessionName", classNameStr);
                                    intentLogin.putExtra("lessionName", subjectStr);
                                    intentLogin.putExtra("gender", genderStr);
                                    intentLogin.putExtra("ratingLogin", "ratingLoginclass");
                                    intentLogin.putExtra("searchfront", searchfront);
                                    intentLogin.putExtra("firsttimesearch", firsttimesearch);
                                    intentLogin.putExtra("RegionName", RegionName);
                                    intentLogin.putExtra("SearchPlaystudy", SearchPlaystudy);
                                    intentLogin.putExtra("TeacherName", TeacherName);
                                    startActivity(intentLogin);
                                    finish();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing

                                }
                            })
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
        AreaName = new ArrayList<String>();
        for (int j = 0; j < dataResponse.getData().size(); j++) {
//            if (dataResponse.getData().get(j).getAddressCity().trim().toLowerCase().contains(locationStr.trim().toLowerCase())
//                    && dataResponse.getData().get(j).getSessionName().trim().toLowerCase().contains(classNameStr.trim().toLowerCase())) {
            AreaName.add(dataResponse.getData().get(j).getRegionName());
//            }
        }

        HashSet<String> hashSet = new HashSet<String>();
        hashSet.addAll(AreaName);
        AreaName.clear();
        AreaName.addAll(hashSet);
        ArrayAdapter<String> adapterTerm = new ArrayAdapter<String>(mContext, R.layout.autocomplete_layout, AreaName);
//        binding.multiautocompe.setThreshold(1);
//        binding.multiautocompe.setAdapter(adapterTerm);
//        binding.multiautocompe.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    }

    public void addRating() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.rating_dialog_layout, null);
        final RatingBar ratingBar = alertLayout.findViewById(R.id.rating_bar);
        final TextView sessionNametxt = alertLayout.findViewById(R.id.session_name_txt);
        final TextView session_rating_view_txt = alertLayout.findViewById(R.id.session_rating_view_txt);
        final TextView cancel_txt = alertLayout.findViewById(R.id.cancel_txt);
        final TextView confirm_txt = alertLayout.findViewById(R.id.confirm_txt);
        final EditText comment_edt = alertLayout.findViewById(R.id.comment_edt);
        final TextView teacher_name_txt = alertLayout.findViewById(R.id.teacher_name_txt);
        sessionNametxt.setText(classNameStr);
        teacher_name_txt.setText(TeacherName);
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
                Button b1 = mAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
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
                                Utils.ping(mContext, "Please select rate");
                            }
                        } else {
                            Utils.ping(mContext, getResources().getString(R.string.not_loging));
                        }
                    }
                });
                b.setTextColor(getResources().getColor(R.color.blue));
                b1.setTextColor(getResources().getColor(R.color.gray1));
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

                                    List<SessionDetailModel> arrayList = new ArrayList<SessionDetailModel>();
                                    totalHours = new ArrayList<>();
                                    totalMinit = new ArrayList<>();
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
                                                }
                                                populargenderStr = dataResponse.getData().get(i).getGenderID();
                                                popluarsessiondateStr = dataResponse.getData().get(i).getStartDate() + " To " + dataResponse.getData().get(i).getEndDate();
                                            }
                                        }
                                        averageHours(totalHours);
                                        averageMinit(totalMinit);
                                        if (avgMinitvalue == 0) {
                                            populardurationStr = String.valueOf(avgHoursvalue);
                                        } else {
                                            populardurationStr = avgHoursvalue + " hr " + avgMinitvalue + " min";
                                        }
                                        Log.d("duration,+Time", populardurationStr + "" + popluarsessiondateStr);

                                        Intent inSession = new Intent(mContext, SessionName.class);
                                        inSession.putExtra("sessionID", popularsessionID);
//                    inSession.putExtra("SearchBy", searchByStr);
                                        inSession.putExtra("city", locationStr);
                                        inSession.putExtra("sessionName", classNameStr);
                                        inSession.putExtra("board", boardStr);
                                        inSession.putExtra("stream", streamStr);
                                        inSession.putExtra("standard", standardStr);
                                        inSession.putExtra("lessionName", subjectStr);
                                        inSession.putExtra("sessiondate", popluarsessiondateStr);
                                        inSession.putExtra("duration", populardurationStr);
                                        inSession.putExtra("gender", populargenderStr);
//                    inSession.putExtra("searchType", searchTypeStr);
//                    inSession.putExtra("withOR", wheretoComeStr);
                                        inSession.putExtra("searchfront", searchfront);
                                        mContext.startActivity(inSession);
                                        popularDialog.dismiss();
                                    }

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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa", Locale.US);
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
            if (SessionHour > 0) {
                totalHours.add(SessionHour);
            }
//            if(SessionMinit>0) {
            totalMinit.add(SessionMinit);
//            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    public void averageHours(List<Integer> list) {
        if (list != null) {
            int sum = 0;
            int n = list.size();
            for (int i = 0; i < n; i++)
                sum += list.get(i);
            Log.d("sum", "" + sum);
            avgHoursvalue = (sum) / n;
            Log.d("value", "" + avgHoursvalue);
        }
    }

    public void averageMinit(List<Integer> list) {
        if (list != null) {
            int sum = 0;
            int n = list.size();
            for (int i = 0; i < n; i++)
                sum += list.get(i);
            avgMinitvalue = (sum) / n;
            Log.d("value", "" + avgMinitvalue);
        }
    }

    public void validData() {
        if (getIntent().getStringExtra("searchfront") != null) {
            searchfront = getIntent().getStringExtra("searchfront");
        } else {
            searchfront = "";
        }
        if (getIntent().getStringExtra("sessionName") != null) {
            classNameStr = getIntent().getStringExtra("sessionName");
            Utils.setPref(mContext, "classname", classNameStr.trim());
        } else {
            classNameStr = "";
        }
        if (classNameStr.equalsIgnoreCase("")) {
            binding.subjectTxt.setText("All Classes");
        } else {
            binding.subjectTxt.setText(classNameStr);
        }
        if (getIntent().getStringExtra("city") != null) {
            locationStr = getIntent().getStringExtra("city");
            Utils.setPref(mContext, "location", locationStr.trim());
        } else {
            locationStr = "";
        }
        if (!locationStr.equalsIgnoreCase("")) {
            binding.cityTxt.setText(locationStr);
        }

        if (getIntent().getStringExtra("lessionName") != null) {
            subjectStr = getIntent().getStringExtra("lessionName");
        } else {
            subjectStr = "";
        }
        if (getIntent().getStringExtra("standard") != null) {
            standardStr = getIntent().getStringExtra("standard");
        } else {
            standardStr = "";
        }
        if (getIntent().getStringExtra("stream") != null) {
            streamStr = getIntent().getStringExtra("stream");
        } else {
            streamStr = "";
        }
        if (getIntent().getStringExtra("board") != null) {
            boardStr = getIntent().getStringExtra("board");
        } else {
            boardStr = "";
        }
        if (getIntent().getStringExtra("RegionName") != null) {
            RegionName = getIntent().getStringExtra("RegionName");
            binding.multiautocompe.setText(RegionName);
        } else {
            RegionName = "";
        }
        if (getIntent().getStringExtra("SearchPlaystudy") != null) {
            SearchPlaystudy = getIntent().getStringExtra("SearchPlaystudy");
        } else {
            SearchPlaystudy = "";
        }
        if (getIntent().getStringExtra("firsttimesearch") != null) {
            firsttimesearch = getIntent().getStringExtra("firsttimesearch");
        } else {
            firsttimesearch = "";
        }
        if (getIntent().getStringExtra("teacherName") != null) {
            TeacherName = getIntent().getStringExtra("teacherName");
        } else {
            TeacherName = "";
        }
        if (!boardStr.equalsIgnoreCase("")) {
            binding.boardTxt.setVisibility(View.VISIBLE);
            binding.boardTxt.setText("\u2022" + boardStr);

        } else {
            binding.boardTxt.setText("");
        }
        if (!standardStr.equalsIgnoreCase("")) {
            binding.standardTxt.setVisibility(View.VISIBLE);
            binding.standardTxt.setText("\u2022" + standardStr);
        } else {
            binding.standardTxt.setText("");
        }
        if (!streamStr.equalsIgnoreCase("")) {
            binding.streamTxt.setVisibility(View.VISIBLE);
            binding.streamTxt.setText("\u2022" + streamStr);
        } else {
            binding.streamTxt.setText("");

        }
    }

    public void menuDialog() {
        menuDialog = new Dialog(mContext);//, R.style.Theme_Dialog);
        Window window = menuDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.x = 10;
        menuDialog.getWindow().getAttributes().verticalMargin = 0.07F;
        wlp.gravity = Gravity.TOP|Gravity.RIGHT;
        window.setAttributes(wlp);

        menuDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        menuDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        menuDialog.setCanceledOnTouchOutside(true);
//        menuDialog.setContentView(menuBinding.getRoot());
        menuDialog.setContentView(R.layout.layout_menu);

        btnHome=(Button)menuDialog.findViewById(R.id.btnHome);
        btnMyReport = (Button) menuDialog.findViewById(R.id.btnMyReport);
        btnMySession = (Button) menuDialog.findViewById(R.id.btnMySession);
        btnChangePassword = (Button) menuDialog.findViewById(R.id.btnChangePassword);
//        btnaddChild = (Button) menuDialog.findViewById(R.id.btnaddChild);
        btnLogout = (Button) menuDialog.findViewById(R.id.btnLogout);
        btnmyfamily = (Button) menuDialog.findViewById(R.id.btnmyfamily);
        btnMyenroll=(Button)menuDialog.findViewById(R.id.btnMyenroll);
        btnMyprofile=(Button)menuDialog.findViewById(R.id.btnMyprofile);

        userNameTxt = (TextView) menuDialog.findViewById(R.id.user_name_txt);
        userNameTxt.setText(Utils.getPref(mContext, "RegisterUserName"));

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(mContext,SearchByUser.class);
                startActivity(i);
            }
        });
        btnMyprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imyaccount = new Intent(mContext, AddStudentScreen.class);
                imyaccount.putExtra("wheretocometype", "session");
                imyaccount.putExtra("myprofile","true");
                imyaccount.putExtra("type", "myprofile");
                startActivity(imyaccount);
            }
        });
        btnMyReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imyaccount = new Intent(mContext, MyAccountActivity.class);
                imyaccount.putExtra("wheretocometype", "session");
                startActivity(imyaccount);
            }
        });
        btnMyenroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent isession = new Intent(mContext, MySession.class);
                isession.putExtra("wheretocometype", "session");
                startActivity(isession);
                menuDialog.dismiss();
            }
        });
        btnMySession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, UpcomingActivity.class);
                intent.putExtra("wheretocometype", "session");
                startActivity(intent);
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
                        .show();
            }
        });
        menuDialog.show();
    }

    public void changePasswordDialog() {
        changePasswordDialogBinding = DataBindingUtil.
                inflate(LayoutInflater.from(mContext), R.layout.change_password_dialog, (ViewGroup) binding.getRoot(), false);

        changeDialog = new Dialog(mContext, R.style.Theme_Dialog);
        Window window = changeDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        changeDialog.getWindow().getAttributes().verticalMargin = 0.0f;
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);

        //changeDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);
        changeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
                        Utils.ping(mContext, "Please enter valid password");
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