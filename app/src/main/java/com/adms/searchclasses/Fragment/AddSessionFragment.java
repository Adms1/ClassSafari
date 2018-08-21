package com.adms.searchclasses.Fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.adms.searchclasses.Activites.DashBoardActivity;
import com.adms.searchclasses.Adapter.AddressListAdapter;
import com.adms.searchclasses.Adapter.AlertListAdapter;
import com.adms.searchclasses.AppConstant.ApiHandler;
import com.adms.searchclasses.AppConstant.Utils;
import com.adms.searchclasses.Interface.onViewClick;
import com.adms.searchclasses.Model.Session.SessionDetailModel;
import com.adms.searchclasses.R;
import com.adms.searchclasses.databinding.AddSessionDialogBinding;
import com.adms.searchclasses.databinding.DisplayTimeGridBinding;
import com.adms.searchclasses.databinding.FragmentAddSessionBinding;
import com.adms.searchclasses.databinding.ViewSessionDialogBinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.internal.Util;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class AddSessionFragment extends Fragment implements com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {
    public static AddSessionFragment fragment;
    static boolean checkTime_sun = false, checkTime_mon = false,
            checkTime_tue = false, checkTime_wed = false, checkTime_thu = false, checkTime_fri = false, checkTime_sat = false;
    static String Tag;
    static ArrayList<String> days;
    static AddSessionDialogBinding addSessionDialogBinding;
    static SessionDetailModel finalsessionfullDetailModel;
    private static String dateFinal;
    private static String minuteFinal, hourFinal, FinalTimeStr;
    private static boolean isFromDate = false;
    private static Context mContext;
    public Dialog popularDialog;
    //Use for Alert Dialog
    ArrayList<String> timegapArray;
    AlertListAdapter alertListAdapter;
    int Year, Month, Day;
    Calendar calendar;
    int mYear, mMonth, mDay;
    String flag, SeslectedsessionID, CoachTypeStr, studentAvailable;
    //Use for selectedSessionTimeValue
    String coachIdStr, lessionTypeNameStr = "", sessionNameStr = "", boardStr = "", standardStr = "", streamStr = "", startDateStr = "", endDateStr = "",
            address1Str = "", address2Str = "", regionStr = "", cityStr = "", stateStr = "", zipcodeStr = "", descriptionStr = "", sessionamtStr = "0",
            sessioncapacityStr = "", alerttimeStr = "", scheduleStr = "", sessiontypeStr = "1", sessionTypeValueStr = "", sessionTypeStr = "", doneStartDate = "", doneEndDate = "", selectprice = "Free";
    String sunstartTimeStr, sunendTimeStr, finalsunTimeStr, monstartTimeStr, monendTimeStr, finalmonTimeStr,
            tuestartTimeStr, tueendTimeStr, finaltueTimeStr, wedstartTimeStr, wedendTimeStr, finalwedTimeStr,
            thustartTimeStr, thuendTimeStr, finalthuTimeStr, fristartTimeStr, friendTimeStr, finalfriTimeStr,
            satstartTimeStr, satendTimeStr, finalsatTimeStr;
    String EditStartDateStr, EditEndDateStr, EditScheduleStr = "";
    ArrayList<String> scheduleArray;
    ArrayList<String> newEnteryArray;
    SessionDetailModel dataResponse, addressResponse;
    String line;
    String[] RowData = new String[0];
    ArrayList<String> lineArray;
    DisplayTimeGridBinding displayTimeGridBinding;
    Dialog addressDialog;
    ArrayList<String> SelectedAddressList;
    AddressListAdapter addressListAdapter;
    int SessionHour = 0;
    Integer SessionMinit = 0;
    String SessionDuration;
    ArrayList<Integer> totalHours;
    ArrayList<Integer> totalMinit;
    boolean checkfalse = false;
    private FragmentAddSessionBinding addSessionBinding;
    private View rootView;
    private com.wdullaer.materialdatetimepicker.date.DatePickerDialog datePickerDialog;

    public AddSessionFragment() {
    }

    //Use for ClassTimeDialog
    private static List<String> getDates(String dateString1, String dateString2) {
        days = new ArrayList<String>();
        DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");

        Date date1 = null;
        Date date2 = null;

        try {
            date1 = df1.parse(dateString1);
            date2 = df1.parse(dateString2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);


        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

//        if(cal2.after(cal1)) {
        while (!cal1.after(cal2)) {
            days.add(new SimpleDateFormat("EE").format(cal1.getTime()));
            cal1.add(Calendar.DATE, 1);

        }
        Log.d("days", "" + days);


        addSessionDialogBinding.sunStartLinear.setEnabled(false);
        addSessionDialogBinding.sunEndLinear.setEnabled(false);
        addSessionDialogBinding.sunStartLinear.setAlpha(0.2f);
        addSessionDialogBinding.sunEndLinear.setAlpha(0.2f);
        addSessionDialogBinding.sunStartAddSessionBtn.setEnabled(false);
        addSessionDialogBinding.sunEndAddSessionBtn.setEnabled(false);

        addSessionDialogBinding.monStartLinear.setEnabled(false);
        addSessionDialogBinding.monEndLinear.setEnabled(false);
        addSessionDialogBinding.monStartLinear.setAlpha(0.2f);
        addSessionDialogBinding.monEndLinear.setAlpha(0.2f);
        addSessionDialogBinding.monStartAddSessionBtn.setEnabled(false);
        addSessionDialogBinding.monEndAddSessionBtn.setEnabled(false);

        addSessionDialogBinding.tueStartLinear.setEnabled(false);
        addSessionDialogBinding.tueEndLinear.setEnabled(false);
        addSessionDialogBinding.tueStartLinear.setAlpha(0.2f);
        addSessionDialogBinding.tueEndLinear.setAlpha(0.2f);
        addSessionDialogBinding.tueStartAddSessionBtn.setEnabled(false);
        addSessionDialogBinding.tueEndAddSessionBtn.setEnabled(false);

        addSessionDialogBinding.wedStartLinear.setEnabled(false);
        addSessionDialogBinding.wedEndLinear.setEnabled(false);
        addSessionDialogBinding.wedStartLinear.setAlpha(0.2f);
        addSessionDialogBinding.wedEndLinear.setAlpha(0.2f);
        addSessionDialogBinding.wedStartAddSessionBtn.setEnabled(false);
        addSessionDialogBinding.wedEndAddSessionBtn.setEnabled(false);

        addSessionDialogBinding.thuStartLinear.setEnabled(false);
        addSessionDialogBinding.thuEndLinear.setEnabled(false);
        addSessionDialogBinding.thuStartLinear.setAlpha(0.2f);
        addSessionDialogBinding.thuEndLinear.setAlpha(0.2f);
        addSessionDialogBinding.thuStartAddSessionBtn.setEnabled(false);
        addSessionDialogBinding.thuEndAddSessionBtn.setEnabled(false);

        addSessionDialogBinding.friStartLinear.setEnabled(false);
        addSessionDialogBinding.friEndLinear.setEnabled(false);
        addSessionDialogBinding.friStartLinear.setAlpha(0.2f);
        addSessionDialogBinding.friEndLinear.setAlpha(0.2f);
        addSessionDialogBinding.friStartAddSessionBtn.setEnabled(false);
        addSessionDialogBinding.friEndAddSessionBtn.setEnabled(false);

        addSessionDialogBinding.satStartLinear.setEnabled(false);
        addSessionDialogBinding.satEndLinear.setEnabled(false);
        addSessionDialogBinding.satStartLinear.setAlpha(0.2f);
        addSessionDialogBinding.satEndLinear.setAlpha(0.2f);
        addSessionDialogBinding.satStartAddSessionBtn.setEnabled(false);
        addSessionDialogBinding.satEndAddSessionBtn.setEnabled(false);

        for (int i = 0; i < days.size(); i++) {
            switch (days.get(i)) {
                case "Sun":
                    addSessionDialogBinding.sunStartLinear.setEnabled(true);
                    addSessionDialogBinding.sunEndLinear.setEnabled(true);
                    addSessionDialogBinding.sunStartLinear.setAlpha(1);
                    addSessionDialogBinding.sunEndLinear.setAlpha(1);
                    addSessionDialogBinding.sunStartAddSessionBtn.setEnabled(true);
                    addSessionDialogBinding.sunEndAddSessionBtn.setEnabled(true);
                    break;
                case "Mon":
                    addSessionDialogBinding.monStartLinear.setEnabled(true);
                    addSessionDialogBinding.monEndLinear.setEnabled(true);
                    addSessionDialogBinding.monStartLinear.setAlpha(1);
                    addSessionDialogBinding.monEndLinear.setAlpha(1);
                    addSessionDialogBinding.monStartAddSessionBtn.setEnabled(true);
                    addSessionDialogBinding.monEndAddSessionBtn.setEnabled(true);
                    break;
                case "Tue":
                    addSessionDialogBinding.tueStartLinear.setEnabled(true);
                    addSessionDialogBinding.tueEndLinear.setEnabled(true);
                    addSessionDialogBinding.tueStartLinear.setAlpha(1);
                    addSessionDialogBinding.tueEndLinear.setAlpha(1);
                    addSessionDialogBinding.tueStartAddSessionBtn.setEnabled(true);
                    addSessionDialogBinding.tueEndAddSessionBtn.setEnabled(true);
                    break;
                case "Wed":
                    addSessionDialogBinding.wedStartLinear.setEnabled(true);
                    addSessionDialogBinding.wedEndLinear.setEnabled(true);
                    addSessionDialogBinding.wedStartLinear.setAlpha(1);
                    addSessionDialogBinding.wedEndLinear.setAlpha(1);
                    addSessionDialogBinding.wedStartAddSessionBtn.setEnabled(true);
                    addSessionDialogBinding.wedEndAddSessionBtn.setEnabled(true);
                    break;
                case "Thu":
                    addSessionDialogBinding.thuStartLinear.setEnabled(true);
                    addSessionDialogBinding.thuEndLinear.setEnabled(true);
                    addSessionDialogBinding.thuStartLinear.setAlpha(1);
                    addSessionDialogBinding.thuEndLinear.setAlpha(1);
                    addSessionDialogBinding.thuStartAddSessionBtn.setEnabled(true);
                    addSessionDialogBinding.thuEndAddSessionBtn.setEnabled(true);
                    break;
                case "Fri":
                    addSessionDialogBinding.friStartLinear.setEnabled(true);
                    addSessionDialogBinding.friEndLinear.setEnabled(true);
                    addSessionDialogBinding.friStartLinear.setAlpha(1);
                    addSessionDialogBinding.friEndLinear.setAlpha(1);
                    addSessionDialogBinding.friStartAddSessionBtn.setEnabled(true);
                    addSessionDialogBinding.friEndAddSessionBtn.setEnabled(true);
                    break;
                case "Sat":
                    addSessionDialogBinding.satStartLinear.setEnabled(true);
                    addSessionDialogBinding.satEndLinear.setEnabled(true);
                    addSessionDialogBinding.satStartLinear.setAlpha(1);
                    addSessionDialogBinding.satEndLinear.setAlpha(1);
                    addSessionDialogBinding.satStartAddSessionBtn.setEnabled(true);
                    addSessionDialogBinding.satEndAddSessionBtn.setEnabled(true);
                    break;
                default:
            }
        }
        return days;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        addSessionBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_session, container, false);

        rootView = addSessionBinding.getRoot();
//        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mContext = getActivity();
        flag = getArguments().getString("flag");


        SeslectedsessionID = getArguments().getString("sessionID");
        if (flag.equalsIgnoreCase("edit")) {
            ((DashBoardActivity) getActivity()).setActionBar(1, "edit");
            studentAvailable = getArguments().getString("studentAvailable");
            Log.d("studentAvailable", studentAvailable);
        } else if (flag.equalsIgnoreCase("view")) {
            ((DashBoardActivity) getActivity()).setActionBar(1, "view");
            studentAvailable = getArguments().getString("studentAvailable");
            Log.d("studentAvailable", studentAvailable);
        } else {
            ((DashBoardActivity) getActivity()).setActionBar(1, "add");
        }


        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        coachIdStr = Utils.getPref(mContext, "coachID");
        CoachTypeStr = Utils.getPref(mContext, "coachTypeID");

        if (!CoachTypeStr.equalsIgnoreCase("1")) {
            addSessionBinding.sessionBoardLinear.setVisibility(View.GONE);
            addSessionBinding.sessionStandardLinear.setVisibility(View.GONE);
            addSessionBinding.sessionStreamLinear.setVisibility(View.GONE);
        } else {
            addSessionBinding.sessionBoardLinear.setVisibility(View.VISIBLE);
            addSessionBinding.sessionStandardLinear.setVisibility(View.VISIBLE);
            addSessionBinding.sessionStreamLinear.setVisibility(View.VISIBLE);
        }


        if (flag.equalsIgnoreCase("edit")) {
            addSessionBinding.sessionTimeTxt.setText("Edit Class Time");
            addSessionBinding.sessionSelectAddressTxt.setText("Edit Class Address");
            if (!studentAvailable.equalsIgnoreCase("0")) {
                addSessionBinding.sessionTimeLinear.setVisibility(View.GONE);
            } else {
                addSessionBinding.sessionTimeLinear.setVisibility(View.VISIBLE);
            }
            addSessionBinding.submitBtn.setText("Update");
            callEditSessionApi();
        } else if (flag.equalsIgnoreCase("view")) {
            disableControl();
            callEditSessionApi();
        } else {
            addSessionBinding.submitBtn.setText("Submit");
        }
        callGetSessionDetailApi();
        callBoardApi();
        callstandardApi();
        callStreamApi();
        callLessionApi();
        callRegionApi();
        fillCity();
        callAddressApi();
        initViews();
        setListners();

    }

    //Use for initilize view
    public void initViews() {
        timegapArray = new ArrayList<String>();
        timegapArray.add("none");
        timegapArray.add("At time of event");
        for (int i = 1; i < 7; i++) {
            timegapArray.add(String.valueOf(i * 5) + " minutes before");
        }
        timegapArray.add("1 hour before");
        timegapArray.add("1 day before");
        Log.d("timegapArray", "" + timegapArray);

        if (Utils.getPref(mContext, "ClassName") != null) {
            if (!Utils.getPref(mContext, "ClassName").equalsIgnoreCase("")) {
                addSessionBinding.sessionNameEdt.setText(Utils.getPref(mContext, "ClassName"));
                addSessionBinding.sessionNameEdt.setEnabled(false);
            } else {
                addSessionBinding.sessionNameEdt.setEnabled(true);
                Utils.setPref(mContext, "ClassName", addSessionBinding.sessionNameEdt.getText().toString());
            }
        } else {
            addSessionBinding.sessionNameEdt.setEnabled(true);
            Utils.setPref(mContext, "ClassName", addSessionBinding.sessionNameEdt.getText().toString());
        }
    }

    //Use for ClickEvent
    public void setListners() {
        addSessionBinding.fessStatusRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int radioButtonId = addSessionBinding.fessStatusRg.getCheckedRadioButtonId();
                switch (radioButtonId) {
                    case R.id.free_rb:
                        addSessionBinding.sessionPriceEdt.setVisibility(View.GONE);
                        selectprice = addSessionBinding.freeRb.getText().toString();
                        break;
                    case R.id.paid_rb:
                        addSessionBinding.sessionPriceEdt.setVisibility(View.VISIBLE);
                        selectprice = addSessionBinding.paidRb.getText().toString();
                        break;
                }
            }
        });
        addSessionBinding.session1TypeRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                int radioButtonId = addSessionBinding.session1TypeRg.getCheckedRadioButtonId();
                switch (radioButtonId) {
                    case R.id.Academic_rb:
                        sessionTypeStr = addSessionBinding.AcademicRb.getTag().toString();
                        addSessionBinding.sessionBoardLinear.setVisibility(View.VISIBLE);
                        addSessionBinding.sessionStandardLinear.setVisibility(View.VISIBLE);
                        addSessionBinding.sessionStreamLinear.setVisibility(View.VISIBLE);
                        addSessionBinding.subjectEdt.setHint("Subject");
                        break;
                    case R.id.play_rb:
                        sessionTypeStr = addSessionBinding.playRb.getTag().toString();
                        addSessionBinding.sessionBoardLinear.setVisibility(View.GONE);
                        addSessionBinding.sessionStandardLinear.setVisibility(View.GONE);
                        addSessionBinding.sessionStreamLinear.setVisibility(View.GONE);
                        addSessionBinding.subjectEdt.setHint("Activity");
                        break;

                }
            }
        });
        addSessionBinding.sessionTypeRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int radioButtonId = addSessionBinding.sessionTypeRg.getCheckedRadioButtonId();
                switch (radioButtonId) {
                    case R.id.recurring_rb:
                        sessiontypeStr = "1";
                        Log.d("sessionTypeValue", sessionTypeValueStr);
                        break;
                    case R.id.single_rb:
                        sessiontypeStr = "2";
                        Log.d("sessionTypeValue", sessionTypeValueStr);
                        break;
                    default:
                }
            }
        });

        addSessionBinding.scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                addSessionBinding.descriptionEdt.getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });
        addSessionBinding.descriptionEdt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                addSessionBinding.descriptionEdt.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        addSessionBinding.alertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TestDialog();
            }
        });
        addSessionBinding.addSessionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionTimeDialog();
            }
        });
        addSessionBinding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                fillEditSessionFiled();
                editSessionValidation();
            }
        });
        addSessionBinding.sportsEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    editSessionValidation();
                }
                return false;
            }
        });
        addSessionBinding.selectSessionAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSessionBinding.sessionAddressLinear.setVisibility(View.GONE);
                addSessionBinding.sessionAddressLinear1.setVisibility(View.GONE);
                addSessionBinding.sessionAreaLinear.setVisibility(View.GONE);
                addSessionBinding.stateLinear.setVisibility(View.GONE);
                AddressDialog();
            }
        });
        addSessionBinding.newSessionAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSessionBinding.addressEdt.setText("");
                addSessionBinding.addressEdt1.setText("");
                addSessionBinding.areaEdt.setText("");
                addSessionBinding.cityEdt.setText("");
                addSessionBinding.stateEdt.setText("");
                addSessionBinding.zipcodeEdt.setText("");
                addSessionBinding.sessionAddressLinear1.setVisibility(View.VISIBLE);
                addSessionBinding.sessionAreaLinear.setVisibility(View.VISIBLE);
                addSessionBinding.stateLinear.setVisibility(View.VISIBLE);
                addSessionBinding.sessionAddressLinear.setVisibility(View.VISIBLE);
                addSessionBinding.sessionAddressLinear1.setVisibility(View.VISIBLE);
                addSessionBinding.sessionAreaLinear.setVisibility(View.VISIBLE);
                addSessionBinding.stateLinear.setVisibility(View.VISIBLE);
            }
        });
    }

    //Use for AlertClassTime
    public void TestDialog() {
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(getActivity());
        View sheetView = getActivity().getLayoutInflater().inflate(R.layout.alert_time_dialog, null);
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.show();

        RecyclerView rcView = (RecyclerView) sheetView.findViewById(R.id.alert_list_rcView);
        TextView canceltxt = (TextView) sheetView.findViewById(R.id.cancel_txt);
        canceltxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.dismiss();
            }
        });
        alertListAdapter = new AlertListAdapter(mContext, timegapArray, new onViewClick() {
            @Override
            public void getViewClick() {
                getSelectedTimeAlert();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        mBottomSheetDialog.dismiss();
                    }
                }, 600);

            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rcView.setLayoutManager(mLayoutManager);
        rcView.setItemAnimator(new DefaultItemAnimator());
        rcView.setAdapter(alertListAdapter);
    }

    //Use for SelectAlertClassTime
    public void getSelectedTimeAlert() {
        String rowValueStr = "";
        for (int k = 0; k < alertListAdapter.getTime().size(); k++) {
            rowValueStr = alertListAdapter.getTime().get(k);
            Log.d("rowValueStr", rowValueStr);
        }
        addSessionBinding.alertBtn.setText(rowValueStr);
    }

    //Use for ClassTimeDialog
    public void SessionTimeDialog() {
        addSessionDialogBinding = DataBindingUtil.
                inflate(LayoutInflater.from(mContext), R.layout.add_session_dialog, null, false);
        popularDialog = new Dialog(mContext, R.style.Theme_Dialog);
        Window window = popularDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        popularDialog.getWindow().getAttributes().verticalMargin = 0.0f;
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);

        // changeDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);
        popularDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popularDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popularDialog.setCancelable(false);
        popularDialog.setContentView(addSessionDialogBinding.getRoot());

        popularDialog.show();

        calendar = Calendar.getInstance();
        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);

        if (flag.equalsIgnoreCase("view")) {
            addSessionDialogBinding.doneBtn.setVisibility(View.GONE);
        } else {
            addSessionDialogBinding.doneBtn.setVisibility(View.VISIBLE);
        }
        if (addSessionBinding.recurringRb.isChecked()) {
            if (sessionTypeStr.equalsIgnoreCase("Academic")) {
                sessionTypeValueStr = "1";
            } else {
                sessionTypeValueStr = "3";
            }
        } else {
        }
        if (addSessionBinding.singleRb.isChecked()) {
            if (sessionTypeStr.equalsIgnoreCase("Academic")) {

                sessionTypeValueStr = "2";
            } else {
                sessionTypeValueStr = "4";
            }

        } else {
            // sessionTypeValueStr = "";
        }
        Log.d("sessionTypeValue", sessionTypeValueStr);
        if (!sessionTypeValueStr.equalsIgnoreCase("")) {
            switch (sessionTypeValueStr) {
                case "1":
                    addSessionDialogBinding.recurringRb.setChecked(true);
                    break;
                case "2":
                    addSessionDialogBinding.singleRb.setChecked(true);
                    break;
                case "3":
                    addSessionDialogBinding.recurringRb.setChecked(true);
                    break;
                case "4":
                    addSessionDialogBinding.singleRb.setChecked(true);
                    break;
            }
        }
        Log.d("flag", flag);
        if (flag.equalsIgnoreCase("edit")) {
            addSessionDialogBinding.confirmTitleTxt.setText("EDIT CLASS TIME");
            addSessionDialogBinding.startDateTxt.setText(EditStartDateStr);
            addSessionDialogBinding.endDateTxt.setText(EditEndDateStr);
            String[] spiltPipes = EditScheduleStr.split("\\|");
            String[] spiltComma;
            String[] spiltDash;
            Log.d("spilt", "" + spiltPipes.toString());
            for (int i = 0; i < spiltPipes.length; i++) {
                spiltComma = spiltPipes[i].split("\\,");
                spiltDash = spiltComma[1].split("\\-");
                switch (spiltComma[0]) {
                    case "sun":
                        addSessionDialogBinding.sunStartTimeTxt.setText(spiltDash[0]);
                        addSessionDialogBinding.sunEndTimeTxt.setText(spiltDash[1]);
                        addSessionDialogBinding.sunStartAddSessionBtn.setText("x");
                        addSessionDialogBinding.sunStartAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                        addSessionDialogBinding.sunStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                        addSessionDialogBinding.sunEndAddSessionBtn.setText("x");
                        addSessionDialogBinding.sunEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                        addSessionDialogBinding.sunEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                        break;
                    case "mon":
                        addSessionDialogBinding.monStartTimeTxt.setText(spiltDash[0]);
                        addSessionDialogBinding.monEndTimeTxt.setText(spiltDash[1]);
                        addSessionDialogBinding.monStartAddSessionBtn.setText("x");
                        addSessionDialogBinding.monStartAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                        addSessionDialogBinding.monStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                        addSessionDialogBinding.monEndAddSessionBtn.setText("x");
                        addSessionDialogBinding.monEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                        addSessionDialogBinding.monEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                        break;
                    case "tue":
                        addSessionDialogBinding.tueStartTimeTxt.setText(spiltDash[0]);
                        addSessionDialogBinding.tueEndTimeTxt.setText(spiltDash[1]);
                        addSessionDialogBinding.tueStartAddSessionBtn.setText("x");
                        addSessionDialogBinding.tueStartAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                        addSessionDialogBinding.tueStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                        addSessionDialogBinding.tueEndAddSessionBtn.setText("x");
                        addSessionDialogBinding.tueEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                        addSessionDialogBinding.tueEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                        break;
                    case "wed":
                        addSessionDialogBinding.wedStartTimeTxt.setText(spiltDash[0]);
                        addSessionDialogBinding.wedEndTimeTxt.setText(spiltDash[1]);
                        addSessionDialogBinding.wedStartAddSessionBtn.setText("x");
                        addSessionDialogBinding.wedStartAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                        addSessionDialogBinding.wedStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                        addSessionDialogBinding.wedEndAddSessionBtn.setText("x");
                        addSessionDialogBinding.wedEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                        addSessionDialogBinding.wedEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                        break;
                    case "thu":
                        addSessionDialogBinding.thuStartTimeTxt.setText(spiltDash[0]);
                        addSessionDialogBinding.thuEndTimeTxt.setText(spiltDash[1]);
                        addSessionDialogBinding.thuStartAddSessionBtn.setText("x");
                        addSessionDialogBinding.thuStartAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                        addSessionDialogBinding.thuStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                        addSessionDialogBinding.thuEndAddSessionBtn.setText("x");
                        addSessionDialogBinding.thuEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                        addSessionDialogBinding.thuEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                        break;
                    case "fri":
                        addSessionDialogBinding.friStartTimeTxt.setText(spiltDash[0]);
                        addSessionDialogBinding.friEndTimeTxt.setText(spiltDash[1]);
                        addSessionDialogBinding.friStartAddSessionBtn.setText("x");
                        addSessionDialogBinding.friStartAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                        addSessionDialogBinding.friStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                        addSessionDialogBinding.friEndAddSessionBtn.setText("x");
                        addSessionDialogBinding.friEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                        addSessionDialogBinding.friEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                        break;
                    case "sat":
                        addSessionDialogBinding.satStartTimeTxt.setText(spiltDash[0]);
                        addSessionDialogBinding.satEndTimeTxt.setText(spiltDash[1]);
                        addSessionDialogBinding.satStartAddSessionBtn.setText("x");
                        addSessionDialogBinding.satStartAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                        addSessionDialogBinding.satStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                        addSessionDialogBinding.satEndAddSessionBtn.setText("x");
                        addSessionDialogBinding.satEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                        addSessionDialogBinding.satEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                        break;
                    default:

                }
            }
            final List<String> days = getDates(addSessionDialogBinding.startDateTxt.getText().toString(), addSessionDialogBinding.endDateTxt.getText().toString());
            System.out.println(days);
        } else if (flag.equalsIgnoreCase("view")) {
            addSessionDialogBinding.confirmTitleTxt.setText("VIEW CLASS TIME");
            if (!scheduleStr.equalsIgnoreCase("")) {
                diableDialogControl();
                addSessionDialogBinding.startDateTxt.setText(EditStartDateStr);
                addSessionDialogBinding.endDateTxt.setText(EditEndDateStr);
                String[] spiltPipes = scheduleStr.split("\\|");
                String[] spiltComma;
                String[] spiltDash;
                Log.d("spilt", "" + spiltPipes.toString());
                for (int i = 0; i < spiltPipes.length; i++) {
                    spiltComma = spiltPipes[i].split("\\,");
                    spiltDash = spiltComma[1].split("\\-");
                    switch (spiltComma[0]) {
                        case "sun":
                            addSessionDialogBinding.sunStartTimeTxt.setText(spiltDash[0]);
                            addSessionDialogBinding.sunEndTimeTxt.setText(spiltDash[1]);
                            addSessionDialogBinding.sunStartAddSessionBtn.setText("x");
                            addSessionDialogBinding.sunStartAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                            addSessionDialogBinding.sunStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            addSessionDialogBinding.sunEndAddSessionBtn.setText("x");
                            addSessionDialogBinding.sunEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                            addSessionDialogBinding.sunEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            break;
                        case "mon":
                            addSessionDialogBinding.monStartTimeTxt.setText(spiltDash[0]);
                            addSessionDialogBinding.monEndTimeTxt.setText(spiltDash[1]);
                            addSessionDialogBinding.monStartAddSessionBtn.setText("x");
                            addSessionDialogBinding.monStartAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                            addSessionDialogBinding.monStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            addSessionDialogBinding.monEndAddSessionBtn.setText("x");
                            addSessionDialogBinding.monEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                            addSessionDialogBinding.monEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            break;
                        case "tue":
                            addSessionDialogBinding.tueStartTimeTxt.setText(spiltDash[0]);
                            addSessionDialogBinding.tueEndTimeTxt.setText(spiltDash[1]);
                            addSessionDialogBinding.tueStartAddSessionBtn.setText("x");
                            addSessionDialogBinding.tueStartAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                            addSessionDialogBinding.tueStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            addSessionDialogBinding.tueEndAddSessionBtn.setText("x");
                            addSessionDialogBinding.tueEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                            addSessionDialogBinding.tueEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            break;
                        case "wed":
                            addSessionDialogBinding.wedStartTimeTxt.setText(spiltDash[0]);
                            addSessionDialogBinding.wedEndTimeTxt.setText(spiltDash[1]);
                            addSessionDialogBinding.wedStartAddSessionBtn.setText("x");
                            addSessionDialogBinding.wedStartAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                            addSessionDialogBinding.wedStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            addSessionDialogBinding.wedEndAddSessionBtn.setText("x");
                            addSessionDialogBinding.wedEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                            addSessionDialogBinding.wedEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            break;
                        case "thu":
                            addSessionDialogBinding.thuStartTimeTxt.setText(spiltDash[0]);
                            addSessionDialogBinding.thuEndTimeTxt.setText(spiltDash[1]);
                            addSessionDialogBinding.thuStartAddSessionBtn.setText("x");
                            addSessionDialogBinding.thuStartAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                            addSessionDialogBinding.thuStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            addSessionDialogBinding.thuEndAddSessionBtn.setText("x");
                            addSessionDialogBinding.thuEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                            addSessionDialogBinding.thuEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            break;
                        case "fri":
                            addSessionDialogBinding.friStartTimeTxt.setText(spiltDash[0]);
                            addSessionDialogBinding.friEndTimeTxt.setText(spiltDash[1]);
                            addSessionDialogBinding.friStartAddSessionBtn.setText("x");
                            addSessionDialogBinding.friStartAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                            addSessionDialogBinding.friStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            addSessionDialogBinding.friEndAddSessionBtn.setText("x");
                            addSessionDialogBinding.friEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                            addSessionDialogBinding.friEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            break;
                        case "sat":
                            addSessionDialogBinding.satStartTimeTxt.setText(spiltDash[0]);
                            addSessionDialogBinding.satEndTimeTxt.setText(spiltDash[1]);
                            addSessionDialogBinding.satStartAddSessionBtn.setText("x");
                            addSessionDialogBinding.satStartAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                            addSessionDialogBinding.satStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            addSessionDialogBinding.satEndAddSessionBtn.setText("x");
                            addSessionDialogBinding.satEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                            addSessionDialogBinding.satEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            break;
                        default:
                    }

                }

            } else {
                addSessionDialogBinding.startDateTxt.setText(Utils.getTodaysDate());
                addSessionDialogBinding.endDateTxt.setText(Utils.getTodaysDate());

                final List<String> days = getDates(addSessionDialogBinding.startDateTxt.getText().toString(), addSessionDialogBinding.endDateTxt.getText().toString());
                System.out.println(days);
            }
        } else if (flag.equalsIgnoreCase("reedit")) {
            if (!scheduleStr.equalsIgnoreCase("")) {
                addSessionDialogBinding.startDateTxt.setText(EditStartDateStr);
                addSessionDialogBinding.endDateTxt.setText(EditEndDateStr);

                String[] spiltPipes = scheduleStr.split("\\|");
                String[] spiltComma;
                String[] spiltDash;
                Log.d("spilt", "" + spiltPipes.toString());
                for (int i = 0; i < spiltPipes.length; i++) {
                    spiltComma = spiltPipes[i].split("\\,");
                    spiltDash = spiltComma[1].split("\\-");
                    switch (spiltComma[0]) {
                        case "sun":
                            addSessionDialogBinding.sunStartTimeTxt.setText(spiltDash[0]);
                            addSessionDialogBinding.sunEndTimeTxt.setText(spiltDash[1]);
                            addSessionDialogBinding.sunStartAddSessionBtn.setText("x");
                            addSessionDialogBinding.sunStartAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                            addSessionDialogBinding.sunStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            addSessionDialogBinding.sunEndAddSessionBtn.setText("x");
                            addSessionDialogBinding.sunEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                            addSessionDialogBinding.sunEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            addSessionDialogBinding.sunStartLinear.setAlpha(1);
                            addSessionDialogBinding.sunEndLinear.setAlpha(1);
                            break;
                        case "mon":
                            addSessionDialogBinding.monStartTimeTxt.setText(spiltDash[0]);
                            addSessionDialogBinding.monEndTimeTxt.setText(spiltDash[1]);
                            addSessionDialogBinding.monStartAddSessionBtn.setText("x");
                            addSessionDialogBinding.monStartAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                            addSessionDialogBinding.monStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            addSessionDialogBinding.monEndAddSessionBtn.setText("x");
                            addSessionDialogBinding.monEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                            addSessionDialogBinding.monEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            addSessionDialogBinding.monStartLinear.setAlpha(1);
                            addSessionDialogBinding.monEndLinear.setAlpha(1);
                            break;
                        case "tue":
                            addSessionDialogBinding.tueStartTimeTxt.setText(spiltDash[0]);
                            addSessionDialogBinding.tueEndTimeTxt.setText(spiltDash[1]);
                            addSessionDialogBinding.tueStartAddSessionBtn.setText("x");
                            addSessionDialogBinding.tueStartAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                            addSessionDialogBinding.tueStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            addSessionDialogBinding.tueEndAddSessionBtn.setText("x");
                            addSessionDialogBinding.tueEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                            addSessionDialogBinding.tueEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            addSessionDialogBinding.tueStartLinear.setAlpha(1);
                            addSessionDialogBinding.tueEndLinear.setAlpha(1);
                            break;
                        case "wed":
                            addSessionDialogBinding.wedStartTimeTxt.setText(spiltDash[0]);
                            addSessionDialogBinding.wedEndTimeTxt.setText(spiltDash[1]);
                            addSessionDialogBinding.wedStartAddSessionBtn.setText("x");
                            addSessionDialogBinding.wedStartAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                            addSessionDialogBinding.wedStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            addSessionDialogBinding.wedEndAddSessionBtn.setText("x");
                            addSessionDialogBinding.wedEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                            addSessionDialogBinding.wedEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            addSessionDialogBinding.wedStartLinear.setAlpha(1);
                            addSessionDialogBinding.wedEndLinear.setAlpha(1);
                            break;
                        case "thu":
                            addSessionDialogBinding.thuStartTimeTxt.setText(spiltDash[0]);
                            addSessionDialogBinding.thuEndTimeTxt.setText(spiltDash[1]);
                            addSessionDialogBinding.thuStartAddSessionBtn.setText("x");
                            addSessionDialogBinding.thuStartAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                            addSessionDialogBinding.thuStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            addSessionDialogBinding.thuEndAddSessionBtn.setText("x");
                            addSessionDialogBinding.thuEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                            addSessionDialogBinding.thuEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            addSessionDialogBinding.thuStartLinear.setAlpha(1);
                            addSessionDialogBinding.thuEndLinear.setAlpha(1);
                            break;
                        case "fri":
                            addSessionDialogBinding.friStartTimeTxt.setText(spiltDash[0]);
                            addSessionDialogBinding.friEndTimeTxt.setText(spiltDash[1]);
                            addSessionDialogBinding.friStartAddSessionBtn.setText("x");
                            addSessionDialogBinding.friStartAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                            addSessionDialogBinding.friStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            addSessionDialogBinding.friEndAddSessionBtn.setText("x");
                            addSessionDialogBinding.friEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                            addSessionDialogBinding.friEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            addSessionDialogBinding.friStartLinear.setAlpha(1);
                            addSessionDialogBinding.friEndLinear.setAlpha(1);
                            break;
                        case "sat":
                            addSessionDialogBinding.satStartTimeTxt.setText(spiltDash[0]);
                            addSessionDialogBinding.satEndTimeTxt.setText(spiltDash[1]);
                            addSessionDialogBinding.satStartAddSessionBtn.setText("x");
                            addSessionDialogBinding.satStartAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                            addSessionDialogBinding.satStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            addSessionDialogBinding.satEndAddSessionBtn.setText("x");
                            addSessionDialogBinding.satEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                            addSessionDialogBinding.satEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            addSessionDialogBinding.satStartLinear.setAlpha(1);
                            addSessionDialogBinding.satEndLinear.setAlpha(1);
                            break;
                        default:
                    }

                }
                final List<String> days = getDates(addSessionDialogBinding.startDateTxt.getText().toString(), addSessionDialogBinding.endDateTxt.getText().toString());
                System.out.println(days);
            } else {
                addSessionDialogBinding.startDateTxt.setText(Utils.getTodaysDate());
                addSessionDialogBinding.endDateTxt.setText(Utils.getTodaysDate());

                final List<String> days = getDates(addSessionDialogBinding.startDateTxt.getText().toString(), addSessionDialogBinding.endDateTxt.getText().toString());
                System.out.println(days);
            }
        } else {
            addSessionDialogBinding.startDateTxt.setText(Utils.getTodaysDate());
            addSessionDialogBinding.endDateTxt.setText(Utils.getTodaysDate());

            final List<String> days = getDates(addSessionDialogBinding.startDateTxt.getText().toString(), addSessionDialogBinding.endDateTxt.getText().toString());
            System.out.println(days);
        }


        addSessionDialogBinding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popularDialog.dismiss();
            }
        });
        if (!flag.equalsIgnoreCase("view")) {
            addSessionDialogBinding.doneBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getArguments().getString("flag").equalsIgnoreCase("Add")) {
                        flag = "reedit";
                    }
                    doneStartDate = addSessionDialogBinding.startDateTxt.getText().toString();
                    doneEndDate = addSessionDialogBinding.endDateTxt.getText().toString();

                    EditStartDateStr = doneStartDate;
                    EditEndDateStr = doneEndDate;
                    scheduleArray = new ArrayList<>();
                    newEnteryArray = new ArrayList<>();
                    scheduleStr = "";
                    if (!checkTime_sun && !checkTime_mon && !checkTime_thu && !checkTime_wed && !checkTime_tue && !checkTime_fri && !checkTime_sat) {
                        if (!addSessionDialogBinding.sunStartTimeTxt.getText().toString().equalsIgnoreCase("Add") && !addSessionDialogBinding.sunEndTimeTxt.getText().toString().equalsIgnoreCase("Add")) {
                            sunstartTimeStr = addSessionDialogBinding.sunStartTimeTxt.getText().toString();
                            sunendTimeStr = addSessionDialogBinding.sunEndTimeTxt.getText().toString();
                            finalsunTimeStr = "sun" + "," + sunstartTimeStr + "-" + sunendTimeStr;
                            Log.d("SundayTime", finalsunTimeStr);

                            scheduleArray.add(finalsunTimeStr);

                        } else {
                        }
                        if (!addSessionDialogBinding.monStartTimeTxt.getText().toString().equalsIgnoreCase("Add") && !addSessionDialogBinding.monEndTimeTxt.getText().toString().equalsIgnoreCase("")) {
                            monstartTimeStr = addSessionDialogBinding.monStartTimeTxt.getText().toString();
                            monendTimeStr = addSessionDialogBinding.monEndTimeTxt.getText().toString();
                            finalmonTimeStr = "mon" + "," + monstartTimeStr + "-" + monendTimeStr;
                            scheduleArray.add(finalmonTimeStr);

                        } else {

                        }
                        if (!addSessionDialogBinding.tueStartTimeTxt.getText().toString().equalsIgnoreCase("Add") && !addSessionDialogBinding.tueEndTimeTxt.getText().toString().equalsIgnoreCase("Add")) {
                            tuestartTimeStr = addSessionDialogBinding.tueStartTimeTxt.getText().toString();
                            tueendTimeStr = addSessionDialogBinding.tueEndTimeTxt.getText().toString();
                            finaltueTimeStr = "tue" + "," + tuestartTimeStr + "-" + tueendTimeStr;
                            scheduleArray.add(finaltueTimeStr);
                        } else {

                        }
                        if (!addSessionDialogBinding.wedStartTimeTxt.getText().toString().equalsIgnoreCase("Add") && !addSessionDialogBinding.wedEndTimeTxt.getText().toString().equalsIgnoreCase("Add")) {
                            wedstartTimeStr = addSessionDialogBinding.wedStartTimeTxt.getText().toString();
                            wedendTimeStr = addSessionDialogBinding.wedEndTimeTxt.getText().toString();
                            finalwedTimeStr = "wed" + "," + wedstartTimeStr + "-" + wedendTimeStr;
                            scheduleArray.add(finalwedTimeStr);
                        } else {

                        }
                        if (!addSessionDialogBinding.thuStartTimeTxt.getText().toString().equalsIgnoreCase("Add") && !addSessionDialogBinding.thuEndTimeTxt.getText().toString().equalsIgnoreCase("Add")) {
                            thustartTimeStr = addSessionDialogBinding.thuStartTimeTxt.getText().toString();
                            thuendTimeStr = addSessionDialogBinding.thuEndTimeTxt.getText().toString();
                            finalthuTimeStr = "thu" + "," + thustartTimeStr + "-" + thuendTimeStr;

                            scheduleArray.add(finalthuTimeStr);
                        } else {

                        }
                        if (!addSessionDialogBinding.friStartTimeTxt.getText().toString().equalsIgnoreCase("Add") && !addSessionDialogBinding.friEndTimeTxt.getText().toString().equalsIgnoreCase("Add")) {
                            fristartTimeStr = addSessionDialogBinding.friStartTimeTxt.getText().toString();
                            friendTimeStr = addSessionDialogBinding.friEndTimeTxt.getText().toString();
                            finalfriTimeStr = "fri" + "," + fristartTimeStr + "-" + friendTimeStr;

                            scheduleArray.add(finalfriTimeStr);

                        } else {

                        }
                        if (!addSessionDialogBinding.satStartTimeTxt.getText().toString().equalsIgnoreCase("Add") && !addSessionDialogBinding.satEndTimeTxt.getText().toString().equalsIgnoreCase("Add")) {
                            satstartTimeStr = addSessionDialogBinding.satStartTimeTxt.getText().toString();
                            satendTimeStr = addSessionDialogBinding.satEndTimeTxt.getText().toString();
                            finalsatTimeStr = "sat" + "," + satstartTimeStr + "-" + satendTimeStr;

                            scheduleArray.add(finalsatTimeStr);
                        } else {
                        }
                        Log.d("scheduleArray", "" + scheduleArray.size());

                        for (int i = 0; i < scheduleArray.size(); i++) {
                            newEnteryArray.add(scheduleArray.get(i));
                        }
                        Log.d("newEnteryArray", "" + newEnteryArray);

                        for (String s : newEnteryArray) {
                            if (!s.equals("")) {
                                scheduleStr = scheduleStr + "|" + s;

                            }

                        }
                        Log.d("scheduleStr", scheduleStr);
                        if (!scheduleStr.equalsIgnoreCase("")) {
                            scheduleStr = scheduleStr.substring(1, scheduleStr.length());
                            Log.d("responseString ", scheduleStr);
                        }
                        if (sessionTypeValueStr.equalsIgnoreCase("")) {
                            Utils.ping(mContext, "Please select classtype");
                        }
                        if (addSessionDialogBinding.recurringRb.isChecked()) {
                            if (sessionTypeStr.equalsIgnoreCase("Academic")) {
                                sessionTypeValueStr = "1";
                            } else {
                                sessionTypeValueStr = "3";
                            }
                        } else {
                        }
                        if (addSessionDialogBinding.singleRb.isChecked()) {
                            if (sessionTypeStr.equalsIgnoreCase("Academic")) {

                                sessionTypeValueStr = "2";
                            } else {
                                sessionTypeValueStr = "4";
                            }

                        } else {
                            // sessionTypeValueStr = "";
                        }
                        Log.d("sessionTypeValue", sessionTypeValueStr);
                        if (!sessionTypeValueStr.equalsIgnoreCase("")) {
                            switch (sessionTypeValueStr) {
                                case "1":
                                    addSessionBinding.recurringRb.setChecked(true);
                                    break;
                                case "2":
                                    addSessionBinding.singleRb.setChecked(true);
                                    break;
                                case "3":
                                    addSessionBinding.recurringRb.setChecked(true);
                                    break;
                                case "4":
                                    addSessionBinding.singleRb.setChecked(true);
                                    break;
                            }
                        }


                        if (sessiontypeStr.equalsIgnoreCase("1")) {
                            if (!scheduleStr.equalsIgnoreCase("")) {
                                popularDialog.dismiss();
                            } else {
                                Utils.ping(mContext, "Please select time");
                            }
                        } else {
                            if (startDateStr.equalsIgnoreCase(endDateStr)) {
                                if (!scheduleStr.equalsIgnoreCase("")) {
                                    popularDialog.dismiss();
                                } else {
                                    Utils.ping(mContext, "Please select time");
                                }
                            } else {
                                Utils.ping(mContext, "Please select class start date and end date should be same");
                            }

                        }
                    } else {
                        Utils.ping(mContext, "Please select proper time");

                    }
                    fillTimeGrid();

                }
            });

        }
        addSessionDialogBinding.startDateTxt.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                isFromDate = true;
                datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(AddSessionFragment.this, Year, Month, Day);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.setOkText("Done");
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.setAccentColor(Color.parseColor("#f2552c"));
                datePickerDialog.show(getActivity().getFragmentManager(), "DatePickerDialog");
            }
        });
        addSessionDialogBinding.endDateTxt.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                isFromDate = false;
                datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(AddSessionFragment.this, Year, Month, Day);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.setOkText("Done");
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.setAccentColor(Color.parseColor("#f2552c"));
                datePickerDialog.show(getActivity().getFragmentManager(), "DatePickerDialog");

            }
        });
        addSessionDialogBinding.sunStartLinear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });

        addSessionDialogBinding.sunEndLinear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });
        addSessionDialogBinding.monStartLinear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });

        addSessionDialogBinding.monEndLinear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });
        addSessionDialogBinding.tueStartLinear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });

        addSessionDialogBinding.tueEndLinear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });
        addSessionDialogBinding.wedStartLinear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });

        addSessionDialogBinding.wedEndLinear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });
        addSessionDialogBinding.thuStartLinear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });

        addSessionDialogBinding.thuEndLinear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });
        addSessionDialogBinding.friStartLinear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });

        addSessionDialogBinding.friEndLinear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });
        addSessionDialogBinding.satStartLinear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });

        addSessionDialogBinding.satEndLinear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });

        addSessionDialogBinding.sunStartAddSessionBtn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
                addSessionDialogBinding.sunStartAddSessionBtn.setText("+");
                addSessionDialogBinding.sunStartAddSessionBtn.setTextColor(getResources().getColor(R.color.blue));
                addSessionDialogBinding.sunStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_btn));
                if (!addSessionDialogBinding.sunStartTimeTxt.getText().toString().equalsIgnoreCase("Add")) {
                    addSessionDialogBinding.sunStartTimeTxt.setText("Add");
                }

            }
        });
        addSessionDialogBinding.sunEndAddSessionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
                addSessionDialogBinding.sunEndAddSessionBtn.setText("+");
                addSessionDialogBinding.sunEndAddSessionBtn.setTextColor(getResources().getColor(R.color.blue));
                addSessionDialogBinding.sunEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_btn));
                if (!addSessionDialogBinding.sunEndTimeTxt.getText().toString().equalsIgnoreCase("Add")) {
                    addSessionDialogBinding.sunEndTimeTxt.setText("Add");
                }
            }
        });
        addSessionDialogBinding.monStartAddSessionBtn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
                addSessionDialogBinding.monStartAddSessionBtn.setText("+");
                addSessionDialogBinding.monStartAddSessionBtn.setTextColor(getResources().getColor(R.color.blue));
                addSessionDialogBinding.monStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_btn));
                if (!addSessionDialogBinding.monStartTimeTxt.getText().toString().equalsIgnoreCase("Add")) {
                    addSessionDialogBinding.monStartTimeTxt.setText("Add");
                }
            }
        });
        addSessionDialogBinding.monEndAddSessionBtn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
                addSessionDialogBinding.monEndAddSessionBtn.setText("+");
                addSessionDialogBinding.monEndAddSessionBtn.setTextColor(getResources().getColor(R.color.blue));
                addSessionDialogBinding.monEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_btn));
                if (!addSessionDialogBinding.monEndTimeTxt.getText().toString().equalsIgnoreCase("Add")) {
                    addSessionDialogBinding.monEndTimeTxt.setText("Add");
                }
            }
        });
        addSessionDialogBinding.tueStartAddSessionBtn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
                addSessionDialogBinding.tueStartAddSessionBtn.setText("+");
                addSessionDialogBinding.tueStartAddSessionBtn.setTextColor(getResources().getColor(R.color.blue));
                addSessionDialogBinding.tueStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_btn));
                if (!addSessionDialogBinding.tueStartTimeTxt.getText().toString().equalsIgnoreCase("Add")) {
                    addSessionDialogBinding.tueStartTimeTxt.setText("Add");
                }
            }
        });
        addSessionDialogBinding.tueEndAddSessionBtn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
                addSessionDialogBinding.tueEndAddSessionBtn.setText("+");
                addSessionDialogBinding.tueEndAddSessionBtn.setTextColor(getResources().getColor(R.color.blue));
                addSessionDialogBinding.tueEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_btn));
                if (!addSessionDialogBinding.tueEndTimeTxt.getText().toString().equalsIgnoreCase("Add")) {
                    addSessionDialogBinding.tueEndTimeTxt.setText("Add");
                }
            }
        });
        addSessionDialogBinding.wedStartAddSessionBtn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
                addSessionDialogBinding.wedStartAddSessionBtn.setText("+");
                addSessionDialogBinding.wedStartAddSessionBtn.setTextColor(getResources().getColor(R.color.blue));
                addSessionDialogBinding.wedStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_btn));
                if (!addSessionDialogBinding.wedStartTimeTxt.getText().toString().equalsIgnoreCase("Add")) {
                    addSessionDialogBinding.wedStartTimeTxt.setText("Add");
                }
            }
        });
        addSessionDialogBinding.wedEndAddSessionBtn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
                addSessionDialogBinding.wedEndAddSessionBtn.setText("+");
                addSessionDialogBinding.wedEndAddSessionBtn.setTextColor(getResources().getColor(R.color.blue));
                addSessionDialogBinding.wedEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_btn));
                if (!addSessionDialogBinding.wedEndTimeTxt.getText().toString().equalsIgnoreCase("Add")) {
                    addSessionDialogBinding.wedEndTimeTxt.setText("Add");
                }
            }
        });
        addSessionDialogBinding.thuStartAddSessionBtn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
                addSessionDialogBinding.thuStartAddSessionBtn.setText("+");
                addSessionDialogBinding.thuStartAddSessionBtn.setTextColor(getResources().getColor(R.color.blue));
                addSessionDialogBinding.thuStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_btn));
                if (!addSessionDialogBinding.thuStartTimeTxt.getText().toString().equalsIgnoreCase("Add")) {
                    addSessionDialogBinding.thuStartTimeTxt.setText("Add");
                }
            }
        });
        addSessionDialogBinding.thuEndAddSessionBtn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
                addSessionDialogBinding.thuEndAddSessionBtn.setText("+");
                addSessionDialogBinding.thuEndAddSessionBtn.setTextColor(getResources().getColor(R.color.blue));
                addSessionDialogBinding.thuEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_btn));
                if (!addSessionDialogBinding.thuEndTimeTxt.getText().toString().equalsIgnoreCase("Add")) {
                    addSessionDialogBinding.thuEndTimeTxt.setText("Add");
                }
            }
        });
        addSessionDialogBinding.friStartAddSessionBtn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
                addSessionDialogBinding.friStartAddSessionBtn.setText("+");
                addSessionDialogBinding.friStartAddSessionBtn.setTextColor(getResources().getColor(R.color.blue));
                addSessionDialogBinding.friStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_btn));
                if (!addSessionDialogBinding.friStartTimeTxt.getText().toString().equalsIgnoreCase("Add")) {
                    addSessionDialogBinding.friStartTimeTxt.setText("Add");
                }
            }
        });
        addSessionDialogBinding.friEndAddSessionBtn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
                addSessionDialogBinding.friEndAddSessionBtn.setText("+");
                addSessionDialogBinding.friEndAddSessionBtn.setTextColor(getResources().getColor(R.color.blue));
                addSessionDialogBinding.friEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_btn));
                if (!addSessionDialogBinding.friEndTimeTxt.getText().toString().equalsIgnoreCase("Add")) {
                    addSessionDialogBinding.friEndTimeTxt.setText("Add");
                }
            }
        });
        addSessionDialogBinding.satStartAddSessionBtn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
                addSessionDialogBinding.satStartAddSessionBtn.setText("+");
                addSessionDialogBinding.satStartAddSessionBtn.setTextColor(getResources().getColor(R.color.blue));
                addSessionDialogBinding.satStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_btn));
                if (!addSessionDialogBinding.satStartTimeTxt.getText().toString().equalsIgnoreCase("Add")) {
                    addSessionDialogBinding.satStartTimeTxt.setText("Add");
                }
            }
        });
        addSessionDialogBinding.satEndAddSessionBtn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
                addSessionDialogBinding.satEndAddSessionBtn.setText("+");
                addSessionDialogBinding.satEndAddSessionBtn.setTextColor(getResources().getColor(R.color.blue));
                addSessionDialogBinding.satEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_btn));
                if (!addSessionDialogBinding.satEndTimeTxt.getText().toString().equalsIgnoreCase("Add")) {
                    addSessionDialogBinding.satEndTimeTxt.setText("Add");
                }
            }
        });

        startDateStr = addSessionDialogBinding.startDateTxt.getText().

                toString();

        endDateStr = addSessionDialogBinding.endDateTxt.getText().

                toString();
    }

    //Use for Select ClassStartDate and ClassEndDate
    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view,
                          int year, int monthOfYear, int dayOfMonth) {
        mDay = dayOfMonth;
        mMonth = monthOfYear + 1;
        mYear = year;
        String d, m, y;
        d = Integer.toString(mDay);
        m = Integer.toString(mMonth);
        y = Integer.toString(mYear);

        if (mDay < 10) {
            d = "0" + d;
        }
        if (mMonth < 10) {
            m = "0" + m;
        }
        dateFinal = d + "/" + m + "/" + y;
        if (sessiontypeStr.equalsIgnoreCase("1")) {
            if (isFromDate) {
                addSessionDialogBinding.startDateTxt.setText(dateFinal);
            } else {
                addSessionDialogBinding.endDateTxt.setText(dateFinal);
            }
        } else {
            addSessionDialogBinding.startDateTxt.setText(dateFinal);
            addSessionDialogBinding.endDateTxt.setText(dateFinal);
            addSessionDialogBinding.endDateTxt.setEnabled(false);
        }
        startDateStr = addSessionDialogBinding.startDateTxt.getText().toString();
        endDateStr = addSessionDialogBinding.endDateTxt.getText().toString();

        if (flag.equalsIgnoreCase("edit")) {
            if (EditStartDateStr.equalsIgnoreCase(startDateStr)) {
                if (EditEndDateStr.equalsIgnoreCase(endDateStr)) {

                }
            } else {
                addSessionDialogBinding.sunStartTimeTxt.setText("Add");
                addSessionDialogBinding.sunStartAddSessionBtn.setText("+");
                addSessionDialogBinding.sunStartAddSessionBtn.setTextColor(getResources().getColor(R.color.blue));
                addSessionDialogBinding.sunStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                addSessionDialogBinding.sunEndTimeTxt.setText("Add");
                addSessionDialogBinding.sunEndAddSessionBtn.setText("+");
                addSessionDialogBinding.sunEndAddSessionBtn.setTextColor(getResources().getColor(R.color.blue));
                addSessionDialogBinding.sunEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                addSessionDialogBinding.monStartTimeTxt.setText("Add");
                addSessionDialogBinding.monStartAddSessionBtn.setText("+");
                addSessionDialogBinding.monStartAddSessionBtn.setTextColor(getResources().getColor(R.color.blue));
                addSessionDialogBinding.monStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                addSessionDialogBinding.monEndTimeTxt.setText("Add");
                addSessionDialogBinding.monEndAddSessionBtn.setText("+");
                addSessionDialogBinding.monEndAddSessionBtn.setTextColor(getResources().getColor(R.color.blue));
                addSessionDialogBinding.monEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                addSessionDialogBinding.tueStartTimeTxt.setText("Add");
                addSessionDialogBinding.tueStartAddSessionBtn.setText("+");
                addSessionDialogBinding.tueStartAddSessionBtn.setTextColor(getResources().getColor(R.color.blue));
                addSessionDialogBinding.tueStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                addSessionDialogBinding.tueEndTimeTxt.setText("Add");
                addSessionDialogBinding.tueEndAddSessionBtn.setText("+");
                addSessionDialogBinding.tueEndAddSessionBtn.setTextColor(getResources().getColor(R.color.blue));
                addSessionDialogBinding.tueEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                addSessionDialogBinding.wedStartTimeTxt.setText("Add");
                addSessionDialogBinding.wedStartAddSessionBtn.setText("+");
                addSessionDialogBinding.wedStartAddSessionBtn.setTextColor(getResources().getColor(R.color.blue));
                addSessionDialogBinding.wedStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                addSessionDialogBinding.wedEndTimeTxt.setText("Add");
                addSessionDialogBinding.wedEndAddSessionBtn.setText("+");
                addSessionDialogBinding.wedEndAddSessionBtn.setTextColor(getResources().getColor(R.color.blue));
                addSessionDialogBinding.wedEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                addSessionDialogBinding.thuStartTimeTxt.setText("Add");
                addSessionDialogBinding.thuStartAddSessionBtn.setText("+");
                addSessionDialogBinding.thuStartAddSessionBtn.setTextColor(getResources().getColor(R.color.blue));
                addSessionDialogBinding.thuStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                addSessionDialogBinding.thuEndTimeTxt.setText("Add");
                addSessionDialogBinding.thuEndAddSessionBtn.setText("+");
                addSessionDialogBinding.thuEndAddSessionBtn.setTextColor(getResources().getColor(R.color.blue));
                addSessionDialogBinding.thuEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                addSessionDialogBinding.friStartTimeTxt.setText("Add");
                addSessionDialogBinding.friStartAddSessionBtn.setText("+");
                addSessionDialogBinding.friStartAddSessionBtn.setTextColor(getResources().getColor(R.color.blue));
                addSessionDialogBinding.friStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                addSessionDialogBinding.friEndTimeTxt.setText("Add");
                addSessionDialogBinding.friEndAddSessionBtn.setText("+");
                addSessionDialogBinding.friEndAddSessionBtn.setTextColor(getResources().getColor(R.color.blue));
                addSessionDialogBinding.friEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                addSessionDialogBinding.satStartTimeTxt.setText("Add");
                addSessionDialogBinding.satStartAddSessionBtn.setText("+");
                addSessionDialogBinding.satStartAddSessionBtn.setTextColor(getResources().getColor(R.color.blue));
                addSessionDialogBinding.satStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                addSessionDialogBinding.satEndTimeTxt.setText("Add");
                addSessionDialogBinding.satEndAddSessionBtn.setText("+");
                addSessionDialogBinding.satEndAddSessionBtn.setTextColor(getResources().getColor(R.color.blue));
                addSessionDialogBinding.satEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_btn));
            }

        } else {
            if (doneStartDate.equalsIgnoreCase(startDateStr)) {
                if (doneEndDate.equalsIgnoreCase(endDateStr)) {

                } else {
                }
            } else {
                addSessionDialogBinding.sunStartTimeTxt.setText("Add");
                addSessionDialogBinding.sunStartAddSessionBtn.setText("+");
                addSessionDialogBinding.sunStartAddSessionBtn.setTextColor(getResources().getColor(R.color.blue));
                addSessionDialogBinding.sunStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                addSessionDialogBinding.sunEndTimeTxt.setText("Add");
                addSessionDialogBinding.sunEndAddSessionBtn.setText("+");
                addSessionDialogBinding.sunEndAddSessionBtn.setTextColor(getResources().getColor(R.color.blue));
                addSessionDialogBinding.sunEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                addSessionDialogBinding.monStartTimeTxt.setText("Add");
                addSessionDialogBinding.monStartAddSessionBtn.setText("+");
                addSessionDialogBinding.monStartAddSessionBtn.setTextColor(getResources().getColor(R.color.blue));
                addSessionDialogBinding.monStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                addSessionDialogBinding.monEndTimeTxt.setText("Add");
                addSessionDialogBinding.monEndAddSessionBtn.setText("+");
                addSessionDialogBinding.monEndAddSessionBtn.setTextColor(getResources().getColor(R.color.blue));
                addSessionDialogBinding.monEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                addSessionDialogBinding.tueStartTimeTxt.setText("Add");
                addSessionDialogBinding.tueStartAddSessionBtn.setText("+");
                addSessionDialogBinding.tueStartAddSessionBtn.setTextColor(getResources().getColor(R.color.blue));
                addSessionDialogBinding.tueStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                addSessionDialogBinding.tueEndTimeTxt.setText("Add");
                addSessionDialogBinding.tueEndAddSessionBtn.setText("+");
                addSessionDialogBinding.tueEndAddSessionBtn.setTextColor(getResources().getColor(R.color.blue));
                addSessionDialogBinding.tueEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                addSessionDialogBinding.wedStartTimeTxt.setText("Add");
                addSessionDialogBinding.wedStartAddSessionBtn.setText("+");
                addSessionDialogBinding.wedStartAddSessionBtn.setTextColor(getResources().getColor(R.color.blue));
                addSessionDialogBinding.wedStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                addSessionDialogBinding.wedEndTimeTxt.setText("Add");
                addSessionDialogBinding.wedEndAddSessionBtn.setText("+");
                addSessionDialogBinding.wedEndAddSessionBtn.setTextColor(getResources().getColor(R.color.blue));
                addSessionDialogBinding.wedEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                addSessionDialogBinding.thuStartTimeTxt.setText("Add");
                addSessionDialogBinding.thuStartAddSessionBtn.setText("+");
                addSessionDialogBinding.thuStartAddSessionBtn.setTextColor(getResources().getColor(R.color.blue));
                addSessionDialogBinding.thuStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                addSessionDialogBinding.thuEndTimeTxt.setText("Add");
                addSessionDialogBinding.thuEndAddSessionBtn.setText("+");
                addSessionDialogBinding.thuEndAddSessionBtn.setTextColor(getResources().getColor(R.color.blue));
                addSessionDialogBinding.thuEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                addSessionDialogBinding.friStartTimeTxt.setText("Add");
                addSessionDialogBinding.friStartAddSessionBtn.setText("+");
                addSessionDialogBinding.friStartAddSessionBtn.setTextColor(getResources().getColor(R.color.blue));
                addSessionDialogBinding.friStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                addSessionDialogBinding.friEndTimeTxt.setText("Add");
                addSessionDialogBinding.friEndAddSessionBtn.setText("+");
                addSessionDialogBinding.friEndAddSessionBtn.setTextColor(getResources().getColor(R.color.blue));
                addSessionDialogBinding.friEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                addSessionDialogBinding.satStartTimeTxt.setText("Add");
                addSessionDialogBinding.satStartAddSessionBtn.setText("+");
                addSessionDialogBinding.satStartAddSessionBtn.setTextColor(getResources().getColor(R.color.blue));
                addSessionDialogBinding.satStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                addSessionDialogBinding.satEndTimeTxt.setText("Add");
                addSessionDialogBinding.satEndAddSessionBtn.setText("+");
                addSessionDialogBinding.satEndAddSessionBtn.setTextColor(getResources().getColor(R.color.blue));
                addSessionDialogBinding.satEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_btn));
            }
        }


        List<String> days = getDates(addSessionDialogBinding.startDateTxt.getText().toString(), addSessionDialogBinding.endDateTxt.getText().toString());
        System.out.println(days);

    }

    //Use for GetInsertClassDetail
    public void getSelectedSessionTimeValue() {
        sessionNameStr = addSessionBinding.sessionNameEdt.getText().toString();
        lessionTypeNameStr = addSessionBinding.subjectEdt.getText().toString();
        boardStr = addSessionBinding.boardNameEdt.getText().toString();
        standardStr = addSessionBinding.standardEdt.getText().toString();
        streamStr = addSessionBinding.streamEdt.getText().toString();
        address1Str = addSessionBinding.addressEdt.getText().toString();
        address2Str = addSessionBinding.addressEdt1.getText().toString();
        regionStr = addSessionBinding.areaEdt.getText().toString();
        cityStr = addSessionBinding.cityEdt.getText().toString();
        stateStr = addSessionBinding.stateEdt.getText().toString();
        zipcodeStr = addSessionBinding.zipcodeEdt.getText().toString();
        descriptionStr = addSessionBinding.descriptionEdt.getText().toString();
        sessioncapacityStr = addSessionBinding.sportsEdt.getText().toString();
        alerttimeStr = addSessionBinding.alertBtn.getText().toString();
        if (selectprice.equalsIgnoreCase("Free")) {
            sessionamtStr = "0";
        } else {
            sessionamtStr = addSessionBinding.sessionPriceEdt.getText().toString();
        }

        if (addSessionBinding.recurringRb.isChecked()) {
            if (sessionTypeStr.equalsIgnoreCase("Academic")) {
                sessionTypeValueStr = "1";
            } else {
                sessionTypeValueStr = "3";
            }
        } else {
        }
        if (addSessionBinding.singleRb.isChecked()) {
            if (sessionTypeStr.equalsIgnoreCase("Academic")) {

                sessionTypeValueStr = "2";
            } else {
                sessionTypeValueStr = "4";
            }

        } else {
            // sessionTypeValueStr = "";
        }
        Log.d("sessionTypeValue", sessionTypeValueStr);
    }

    //Use for Create Session
    public void callCreateSessionApi() {
        if (Utils.isNetworkConnected(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().getCreate_Session(getNewSessionDetail(), new retrofit.Callback<SessionDetailModel>() {
                @Override
                public void success(SessionDetailModel sessionDetailModel, Response response) {
                    Utils.dismissDialog();
                    if (sessionDetailModel == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (sessionDetailModel.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (sessionDetailModel.getSuccess().equalsIgnoreCase("false")) {
                        Utils.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (sessionDetailModel.getSuccess().equalsIgnoreCase("True")) {
                        Utils.dismissDialog();
                        Utils.ping(mContext, "Class created successfully");
                        Fragment fragment = new SessionFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
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

    private Map<String, String> getNewSessionDetail() {

        Map<String, String> map = new HashMap<>();
        map.put("CoachID", coachIdStr);
        map.put("SessionTypeID", sessionTypeValueStr);
        map.put("SessionName", sessionNameStr);
        map.put("Board", boardStr);
        map.put("Standard", standardStr);
        map.put("Stream", streamStr);
        map.put("LessionTypeName", lessionTypeNameStr);
        map.put("Address1", address1Str);
        map.put("Address2", address2Str);
        map.put("Region", regionStr);
        map.put("City", cityStr);
        map.put("State", stateStr);
        map.put("Zipcode", zipcodeStr);
        map.put("Description", descriptionStr);
        map.put("SessionAmount", sessionamtStr);
        map.put("SessionCapacity", sessioncapacityStr);
        map.put("AlertTime", alerttimeStr);
        map.put("StartDate", startDateStr);
        map.put("EndDate", endDateStr);
        map.put("Schedule", scheduleStr);
        return map;
    }

    //Use for GetBoard
    public void callBoardApi() {
        if (Utils.isNetworkConnected(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_Board(getBoardDetail(), new retrofit.Callback<SessionDetailModel>() {
                @Override
                public void success(SessionDetailModel boardInfo, Response response) {
                    Utils.dismissDialog();
                    if (boardInfo == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (boardInfo.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (boardInfo.getSuccess().equalsIgnoreCase("false")) {
                        Utils.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (boardInfo.getSuccess().equalsIgnoreCase("True")) {
                        Utils.dismissDialog();
                        if (boardInfo.getData().size() > 0) {
                            dataResponse = boardInfo;
                            fillBoard();
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

    private Map<String, String> getBoardDetail() {
        Map<String, String> map = new HashMap<>();
        return map;
    }

    //Use for fill ClassBoard
    public void fillBoard() {
        ArrayList<String> BoardName = new ArrayList<String>();
        for (int j = 0; j < dataResponse.getData().size(); j++) {
            BoardName.add(dataResponse.getData().get(j).getBoardName());
        }
        ArrayAdapter<String> adapterTerm = new ArrayAdapter<String>(mContext, R.layout.autocomplete_layout, BoardName);
        addSessionBinding.boardNameEdt.setThreshold(1);
        addSessionBinding.boardNameEdt.setAdapter(adapterTerm);

    }

    //Use for Getstandard
    public void callstandardApi() {
        if (Utils.isNetworkConnected(mContext)) {

//            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_Standard(getstandardDetail(), new retrofit.Callback<SessionDetailModel>() {
                @Override
                public void success(SessionDetailModel boardInfo, Response response) {
                    Utils.dismissDialog();
                    if (boardInfo == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (boardInfo.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (boardInfo.getSuccess().equalsIgnoreCase("false")) {
                        Utils.dismissDialog();

                        Utils.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (boardInfo.getSuccess().equalsIgnoreCase("True")) {
                        Utils.dismissDialog();
                        if (boardInfo.getData().size() > 0) {
                            dataResponse = boardInfo;
                            fillStandard();
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

    private Map<String, String> getstandardDetail() {
        Map<String, String> map = new HashMap<>();
        return map;
    }

    //Use for fill ClassStandard
    public void fillStandard() {
        ArrayList<String> StandardName = new ArrayList<String>();
        for (int j = 0; j < dataResponse.getData().size(); j++) {
            StandardName.add(dataResponse.getData().get(j).getStandardName());
        }
        ArrayAdapter<String> adapterTerm = new ArrayAdapter<String>(mContext, R.layout.autocomplete_layout, StandardName);
        addSessionBinding.standardEdt.setThreshold(1);
        addSessionBinding.standardEdt.setAdapter(adapterTerm);
    }

    //Use for GetStream
    public void callStreamApi() {
        if (Utils.isNetworkConnected(mContext)) {

//            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_Stream(getStreamDeatil(), new retrofit.Callback<SessionDetailModel>() {
                @Override
                public void success(SessionDetailModel boardInfo, Response response) {
                    Utils.dismissDialog();
                    if (boardInfo == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (boardInfo.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (boardInfo.getSuccess().equalsIgnoreCase("false")) {
                        Utils.dismissDialog();
                        Utils.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (boardInfo.getSuccess().equalsIgnoreCase("True")) {
                        Utils.dismissDialog();
                        if (boardInfo.getData().size() > 0) {
                            dataResponse = boardInfo;
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
        } else {
            Utils.ping(mContext, getString(R.string.internet_connection_error));
        }
    }

    private Map<String, String> getStreamDeatil() {
        Map<String, String> map = new HashMap<>();
        return map;
    }

    //Use for fill ClassStream
    public void fillStream() {

        ArrayList<String> StreamName = new ArrayList<String>();
        for (int j = 0; j < dataResponse.getData().size(); j++) {
            StreamName.add(dataResponse.getData().get(j).getStreamName());
        }
        ArrayAdapter<String> adapterTerm = new ArrayAdapter<String>(mContext, R.layout.autocomplete_layout, StreamName);
        addSessionBinding.streamEdt.setThreshold(1);
        addSessionBinding.streamEdt.setAdapter(adapterTerm);
    }

    //Use for GetLession
    public void callLessionApi() {
        if (Utils.isNetworkConnected(mContext)) {

//            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_Lesson(getLessionDeatil(), new retrofit.Callback<SessionDetailModel>() {
                @Override
                public void success(SessionDetailModel boardInfo, Response response) {
                    Utils.dismissDialog();
                    if (boardInfo == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (boardInfo.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (boardInfo.getSuccess().equalsIgnoreCase("false")) {
                        Utils.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (boardInfo.getSuccess().equalsIgnoreCase("True")) {
                        Utils.dismissDialog();
                        if (boardInfo.getData().size() > 0) {
                            dataResponse = boardInfo;
                            fillLession();
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

    private Map<String, String> getLessionDeatil() {
        Map<String, String> map = new HashMap<>();
        return map;
    }

    //Use for fill ClassLession
    public void fillLession() {

        ArrayList<String> LessionName = new ArrayList<String>();
        for (int j = 0; j < dataResponse.getData().size(); j++) {
            LessionName.add(dataResponse.getData().get(j).getLessonTypeName());
        }

        ArrayAdapter<String> adapterTerm = new ArrayAdapter<String>(mContext, R.layout.autocomplete_layout, LessionName);
        addSessionBinding.subjectEdt.setThreshold(1);
        addSessionBinding.subjectEdt.setAdapter(adapterTerm);
    }

    //Use for GetRegion
    public void callRegionApi() {
        if (Utils.isNetworkConnected(mContext)) {

//            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_Region(getRegionDeatil(), new retrofit.Callback<SessionDetailModel>() {
                @Override
                public void success(SessionDetailModel boardInfo, Response response) {
                    Utils.dismissDialog();
                    if (boardInfo == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (boardInfo.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (boardInfo.getSuccess().equalsIgnoreCase("false")) {
                        Utils.dismissDialog();
                        Utils.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (boardInfo.getSuccess().equalsIgnoreCase("True")) {
                        Utils.dismissDialog();
                        if (boardInfo.getData().size() > 0) {
                            dataResponse = boardInfo;
                            fillArea();
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

    private Map<String, String> getRegionDeatil() {
        Map<String, String> map = new HashMap<>();
        return map;
    }

    //Use for fill ClassArea
    public void fillArea() {
        ArrayList<String> AreaName = new ArrayList<String>();
        for (int j = 0; j < dataResponse.getData().size(); j++) {
            AreaName.add(dataResponse.getData().get(j).getRegionName());
        }
        ArrayAdapter<String> adapterTerm = new ArrayAdapter<String>(mContext, R.layout.autocomplete_layout, AreaName);
        addSessionBinding.areaEdt.setThreshold(1);
        addSessionBinding.areaEdt.setAdapter(adapterTerm);
    }

    //Use for GetAddress
    public void callAddressApi() {
        if (Utils.isNetworkConnected(mContext)) {

//            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_SessionAddressList_By_CoachID(getAddressDetail(), new retrofit.Callback<SessionDetailModel>() {
                @Override
                public void success(SessionDetailModel addressInfo, Response response) {
                    Utils.dismissDialog();
                    if (addressInfo == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (addressInfo.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (addressInfo.getSuccess().equalsIgnoreCase("false")) {
                        Utils.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (addressInfo.getSuccess().equalsIgnoreCase("True")) {
                        Utils.dismissDialog();
                        if (addressInfo.getData().size() > 0) {
                            addressResponse = addressInfo;
                            if (flag.equalsIgnoreCase("view")) {
                                addSessionBinding.sessionSelectAddressLinear.setVisibility(View.GONE);
                            } else {
                                addSessionBinding.sessionSelectAddressLinear.setVisibility(View.VISIBLE);
                            }
                        } else {
                            addSessionBinding.selectSessionAddressBtn.setVisibility(View.GONE);
                            addSessionBinding.sessionSelectAddressTxt.setText("Add Class Address");
                            // addSessionBinding.sessionSelectAddressLinear.setVisibility(View.GONE);
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

    private Map<String, String> getAddressDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("CoachID", coachIdStr);
        return map;
    }

    //Use for Select Class Address
    public void AddressDialog() {
        displayTimeGridBinding = DataBindingUtil.
                inflate(LayoutInflater.from(mContext), R.layout.display_time_grid, (ViewGroup) addSessionBinding.getRoot(), false);
        addressDialog = new Dialog(mContext, R.style.Theme_Dialog);
        Window window = addressDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        addressDialog.getWindow().getAttributes().verticalMargin = 0.20f;
        wlp.gravity = Gravity.TOP;
        window.setAttributes(wlp);

        //addressDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);
        addressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        addressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addressDialog.setCancelable(false);
        addressDialog.setContentView(displayTimeGridBinding.getRoot());


        addressListAdapter = new AddressListAdapter(mContext, addressResponse, new onViewClick() {
            @Override
            public void getViewClick() {
                SelectedAddressList = new ArrayList<>();
                String SelectedAddressListStr, strPosition;
                SelectedAddressListStr = String.valueOf(addressListAdapter.getAddress());
                strPosition = SelectedAddressListStr.substring(1, SelectedAddressListStr.length() - 1);
                Log.d("Address", strPosition);
                for (int i = 0; i < addressResponse.getData().size(); i++) {
                    if (String.valueOf(i).equalsIgnoreCase(strPosition)) {
                        addSessionBinding.sessionAddressLinear.setVisibility(View.VISIBLE);
                        addSessionBinding.sessionAddressLinear1.setVisibility(View.VISIBLE);
                        addSessionBinding.sessionAreaLinear.setVisibility(View.VISIBLE);
                        addSessionBinding.stateLinear.setVisibility(View.VISIBLE);
                        addSessionBinding.addressEdt.setText(addressResponse.getData().get(i).getAddressLine1());
                        if (addressResponse.getData().get(i).getAddressLine2().equalsIgnoreCase("")) {
                            addSessionBinding.sessionAddressLinear1.setVisibility(View.GONE);
                        } else {
                            addSessionBinding.sessionAddressLinear1.setVisibility(View.VISIBLE);
                            addSessionBinding.addressEdt1.setText(addressResponse.getData().get(i).getAddressLine2());
                        }
                        addSessionBinding.areaEdt.setText(addressResponse.getData().get(i).getRegionName());
                        addSessionBinding.cityEdt.setText(addressResponse.getData().get(i).getAddressCity());
                        addSessionBinding.stateEdt.setText(addressResponse.getData().get(i).getAddressState());
                        addSessionBinding.zipcodeEdt.setText(addressResponse.getData().get(i).getAddressZipCode());
                    }
                }
                addressDialog.dismiss();
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        displayTimeGridBinding.addressList.setLayoutManager(mLayoutManager);
        displayTimeGridBinding.addressList.setItemAnimator(new DefaultItemAnimator());
        displayTimeGridBinding.addressList.setAdapter(addressListAdapter);

        displayTimeGridBinding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addressDialog.dismiss();
            }
        });
        addressDialog.show();
    }

    //Use for EditClass
    public void callEditSessionApi() {
        if (Utils.isNetworkConnected(mContext)) {

//            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_SessionDetailBySessionID(getEditSessionDeatil(), new retrofit.Callback<SessionDetailModel>() {
                @Override
                public void success(SessionDetailModel editsessionInfo, Response response) {
                    Utils.dismissDialog();
                    if (editsessionInfo == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (editsessionInfo.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (editsessionInfo.getSuccess().equalsIgnoreCase("false")) {
                        Utils.dismissDialog();
                        Utils.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (editsessionInfo.getSuccess().equalsIgnoreCase("True")) {
                        Utils.dismissDialog();
                        if (editsessionInfo.getData().size() > 0) {
                            dataResponse = editsessionInfo;
                            fillEditSessionFiled();
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

    private Map<String, String> getEditSessionDeatil() {
        Map<String, String> map = new HashMap<>();
        map.put("CoachID", coachIdStr);//coachIdStr
        map.put("SessionID", SeslectedsessionID);
        return map;
    }

    //Use for fill ClassEdited Detail
    public void fillEditSessionFiled() {
        checkfalse = true;
        scheduleStr = "";
        for (int i = 0; i < dataResponse.getData().size(); i++) {
            switch (dataResponse.getData().get(i).getSessionType()) {
                case "1":
                    addSessionBinding.AcademicRb.setChecked(true);
                    addSessionBinding.recurringRb.setChecked(true);

                    break;
                case "2":
                    addSessionBinding.AcademicRb.setChecked(true);
                    addSessionBinding.singleRb.setChecked(true);

                    break;
                case "3":
                    addSessionBinding.playRb.setChecked(true);
                    addSessionBinding.recurringRb.setChecked(true);

                    break;
                case "4":
                    addSessionBinding.playRb.setChecked(true);
                    addSessionBinding.singleRb.setChecked(true);
                    break;
                default:
            }
            addSessionBinding.sessionNameEdt.setText(dataResponse.getData().get(i).getSessionName());
            addSessionBinding.boardNameEdt.setText(dataResponse.getData().get(i).getBoard());
            addSessionBinding.standardEdt.setText(dataResponse.getData().get(i).getStandard());
            addSessionBinding.streamEdt.setText(dataResponse.getData().get(i).getStream());
            addSessionBinding.subjectEdt.setText(dataResponse.getData().get(i).getLessionTypeName());
            EditStartDateStr = dataResponse.getData().get(i).getStartDate();
            EditEndDateStr = dataResponse.getData().get(i).getEndDate();
            EditScheduleStr = dataResponse.getData().get(i).getSchedule();
            startDateStr = EditStartDateStr;
            endDateStr = EditEndDateStr;
            scheduleStr = EditScheduleStr;
            Log.d("EditscheduleStr ", scheduleStr);
            addSessionBinding.sessionAddressLinear.setVisibility(View.VISIBLE);
            addSessionBinding.sessionAreaLinear.setVisibility(View.VISIBLE);
            addSessionBinding.stateLinear.setVisibility(View.VISIBLE);
            addSessionBinding.stateEdt.setVisibility(View.VISIBLE);

            if (dataResponse.getData().get(i).getAddressLine2().equalsIgnoreCase("")) {
                addSessionBinding.addressEdt.setText(dataResponse.getData().get(i).getAddressLine1());
                addSessionBinding.areaEdt.setText(dataResponse.getData().get(i).getRegionName());
                addSessionBinding.cityEdt.setText(dataResponse.getData().get(i).getAddressCity());
                addSessionBinding.stateEdt.setText(dataResponse.getData().get(i).getAddressState());
                addSessionBinding.zipcodeEdt.setText(dataResponse.getData().get(i).getAddressZipCode());
            } else {
                addSessionBinding.sessionAddressLinear1.setVisibility(View.VISIBLE);
                addSessionBinding.addressEdt.setText(dataResponse.getData().get(i).getAddressLine1());
                addSessionBinding.addressEdt1.setText(dataResponse.getData().get(i).getAddressLine2());
                addSessionBinding.areaEdt.setText(dataResponse.getData().get(i).getRegionName());
                addSessionBinding.cityEdt.setText(dataResponse.getData().get(i).getAddressCity());
                addSessionBinding.stateEdt.setText(dataResponse.getData().get(i).getAddressState());
                addSessionBinding.zipcodeEdt.setText(dataResponse.getData().get(i).getAddressZipCode());
            }
            addSessionBinding.descriptionEdt.setText(dataResponse.getData().get(i).getDescription());
            if (dataResponse.getData().get(i).getSessionAmount().equalsIgnoreCase("0.00")) {
                addSessionBinding.freeRb.setChecked(true);
            } else {
                addSessionBinding.paidRb.setChecked(true);
                addSessionBinding.sessionPriceEdt.setText(String.valueOf(Math.round(Float.parseFloat(dataResponse.getData().get(i).getSessionAmount()))));
            }
            addSessionBinding.sportsEdt.setText(dataResponse.getData().get(i).getSessionCapacity());
            addSessionBinding.alertBtn.setText(dataResponse.getData().get(i).getAlertTime());
            fillTimeGrid();
        }
    }

    //Use for UpdateClass
    public void callUpdateSessionApi() {
        if (Utils.isNetworkConnected(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_Update_Session(getUpdateSessionDetail(), new retrofit.Callback<SessionDetailModel>() {
                @Override
                public void success(SessionDetailModel updatesessionDetailModel, Response response) {
                    Utils.dismissDialog();
                    if (updatesessionDetailModel == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (updatesessionDetailModel.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (updatesessionDetailModel.getSuccess().equalsIgnoreCase("false")) {
                        Utils.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (updatesessionDetailModel.getSuccess().equalsIgnoreCase("True")) {
                        Utils.dismissDialog();
                        Utils.ping(mContext, "Class updated successfully");
                        Fragment fragment = new SessionFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
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

    private Map<String, String> getUpdateSessionDetail() {
//        getSelectedSessionTimeValue();
        Map<String, String> map = new HashMap<>();
        map.put("SessionID", SeslectedsessionID);
        map.put("CoachID", coachIdStr);//coachIdStr
        map.put("SessionTypeID", sessionTypeValueStr);
        map.put("SessionName", sessionNameStr);
        map.put("Board", boardStr);
        map.put("Standard", standardStr);
        map.put("Stream", streamStr);
        map.put("LessionTypeName", lessionTypeNameStr);
        map.put("Address1", address1Str);
        map.put("Address2", address2Str);
        map.put("Region", regionStr);
        map.put("City", cityStr);
        map.put("State", stateStr);
        map.put("Zipcode", zipcodeStr);
        map.put("Description", descriptionStr);
        map.put("SessionAmount", sessionamtStr);
        map.put("SessionCapacity", sessioncapacityStr);
        map.put("AlertTime", alerttimeStr);
        map.put("StartDate", startDateStr);
        map.put("EndDate", endDateStr);
        map.put("Schedule", scheduleStr);
        return map;
    }

    //Use for GetClassInsertFiled validation
    public void editSessionValidation() {
        getSelectedSessionTimeValue();

        if (!sessionTypeStr.equalsIgnoreCase("Academic")) {
            if (flag.equalsIgnoreCase("edit")) {
                if (!sessionTypeValueStr.equalsIgnoreCase("")) {
                    if (!coachIdStr.equalsIgnoreCase("") && !sessionNameStr.equalsIgnoreCase("")) {
                        if (!lessionTypeNameStr.equalsIgnoreCase("")) {
                            if (!address1Str.equalsIgnoreCase("")) {
                                if (!regionStr.equalsIgnoreCase("")) {
                                    if (!cityStr.equalsIgnoreCase("")) {
                                        if (!stateStr.equalsIgnoreCase("")) {
                                            if (!zipcodeStr.equalsIgnoreCase("")) {
                                                if (!sessioncapacityStr.equalsIgnoreCase("")) {
                                                    if (!sessionamtStr.equalsIgnoreCase("")) {
                                                        if (sessiontypeStr.equalsIgnoreCase("1")) {
                                                            callUpdateSessionApi();
                                                        } else {
                                                            if (startDateStr.equalsIgnoreCase(endDateStr)) {
                                                                callUpdateSessionApi();
                                                            } else {
                                                                Utils.ping(mContext, getString(R.string.Date));
                                                            }
                                                        }
                                                    } else {
                                                        addSessionBinding.sessionPriceEdt.setError(getString(R.string.sessionAmount));
                                                    }
                                                } else {
                                                    addSessionBinding.sportsEdt.setError(getString(R.string.sessionCapacity));
                                                }
                                            } else {
                                                addSessionBinding.zipcodeEdt.setError(getString(R.string.sessionZipcode));
                                            }
                                        } else {
                                            addSessionBinding.stateEdt.setError(getString(R.string.sessionState));
                                        }
                                    } else {
                                        addSessionBinding.cityEdt.setError(getString(R.string.sessionCity));
                                    }
                                } else {
                                    addSessionBinding.areaEdt.setError(getString(R.string.sessionArea));
                                }
                            } else {
                                Utils.ping(mContext, getString(R.string.sessionAddress));
                            }
                        } else {
                            addSessionBinding.subjectEdt.setError(getString(R.string.lessonName));
                        }
                    } else {
                        addSessionBinding.sessionNameEdt.setError(getString(R.string.sessionName));
                        addSessionBinding.scrollView.setScrollY(0);
                    }
                } else {
                    Utils.ping(mContext, getResources().getString(R.string.class_type));
                }
            } else {
                if (!sessionTypeValueStr.equalsIgnoreCase("")) {
                    if (!coachIdStr.equalsIgnoreCase("") && !sessionNameStr.equalsIgnoreCase("")) {
                        if (!lessionTypeNameStr.equalsIgnoreCase("")) {
                            if (!startDateStr.equalsIgnoreCase("")) {
                                if (!endDateStr.equalsIgnoreCase("")) {
                                    if (!scheduleStr.equalsIgnoreCase("")) {
                                        if (!address1Str.equalsIgnoreCase("")) {
                                            if (!regionStr.equalsIgnoreCase("")) {
                                                if (!cityStr.equalsIgnoreCase("")) {
                                                    if (!stateStr.equalsIgnoreCase("")) {
                                                        if (!zipcodeStr.equalsIgnoreCase("")) {
                                                            if (!sessioncapacityStr.equalsIgnoreCase("")) {
                                                                if (!sessionamtStr.equalsIgnoreCase("")) {
                                                                    if (sessiontypeStr.equalsIgnoreCase("1")) {
                                                                        callCreateSessionApi();
                                                                    } else {
                                                                        if (startDateStr.equalsIgnoreCase(endDateStr)) {
                                                                            callCreateSessionApi();
                                                                        } else {
                                                                            Utils.ping(mContext, getString(R.string.Date));
                                                                        }
                                                                    }
                                                                } else {
                                                                    addSessionBinding.sessionPriceEdt.setError(getString(R.string.sessionAmount));
                                                                }
                                                            } else {
                                                                addSessionBinding.sportsEdt.setError(getString(R.string.sessionCapacity));
                                                            }
                                                        } else {
                                                            addSessionBinding.zipcodeEdt.setError(getString(R.string.sessionZipcode));
                                                        }
                                                    } else {
                                                        addSessionBinding.stateEdt.setError(getString(R.string.sessionState));
                                                    }
                                                } else {
                                                    addSessionBinding.cityEdt.setError(getString(R.string.sessionCity));
                                                }
                                            } else {
                                                addSessionBinding.areaEdt.setError(getString(R.string.sessionArea));
                                            }
                                        } else {
                                            Utils.ping(mContext, getString(R.string.sessionAddress));
                                        }
                                    } else {
                                        Utils.ping(mContext, getString(R.string.sessionTime));
                                    }
                                } else {
                                    Utils.ping(mContext, getString(R.string.sessionTime));
                                }
                            } else {
                                Utils.ping(mContext, getString(R.string.sessionTime));
                            }
                        } else {
                            addSessionBinding.subjectEdt.setError(getString(R.string.lessonName));
                        }
                    } else {
                        addSessionBinding.sessionNameEdt.setError(getString(R.string.sessionName));
                        addSessionBinding.scrollView.setScrollY(0);
                    }
                } else {
                    Utils.ping(mContext, getResources().getString(R.string.class_type));
                }
            }
        } else {
            if (flag.equalsIgnoreCase("edit")) {
                if (!sessionTypeValueStr.equalsIgnoreCase("")) {
                    if (!coachIdStr.equalsIgnoreCase("") && !sessionNameStr.equalsIgnoreCase("")) {
                        if (!boardStr.equalsIgnoreCase("")) {
                            if (!standardStr.equalsIgnoreCase("")) {
                                if (!streamStr.equalsIgnoreCase("")) {
                                    if (!lessionTypeNameStr.equalsIgnoreCase("")) {
                                        if (!address1Str.equalsIgnoreCase("")) {
                                            if (!regionStr.equalsIgnoreCase("")) {
                                                if (!cityStr.equalsIgnoreCase("")) {
                                                    if (!stateStr.equalsIgnoreCase("")) {
                                                        if (!zipcodeStr.equalsIgnoreCase("")) {
                                                            if (!sessioncapacityStr.equalsIgnoreCase("")) {
                                                                if (!sessionamtStr.equalsIgnoreCase("")) {
                                                                    if (sessiontypeStr.equalsIgnoreCase("1")) {
                                                                        callUpdateSessionApi();
                                                                    } else {
                                                                        if (startDateStr.equalsIgnoreCase(endDateStr)) {
                                                                            callUpdateSessionApi();
                                                                        } else {
                                                                            Utils.ping(mContext, getString(R.string.Date));
                                                                        }
                                                                    }
                                                                } else {
                                                                    addSessionBinding.sessionPriceEdt.setError(getString(R.string.sessionAmount));
                                                                }
                                                            } else {
                                                                addSessionBinding.sportsEdt.setError(getString(R.string.sessionCapacity));
                                                            }
                                                        } else {
                                                            addSessionBinding.zipcodeEdt.setError(getString(R.string.sessionZipcode));
                                                        }
                                                    } else {
                                                        addSessionBinding.stateEdt.setError(getString(R.string.sessionState));
                                                    }
                                                } else {
                                                    addSessionBinding.cityEdt.setError(getString(R.string.sessionCity));
                                                }
                                            } else {
                                                addSessionBinding.areaEdt.setError(getString(R.string.sessionArea));
                                            }
                                        } else {
                                            Utils.ping(mContext, getString(R.string.sessionAddress));
                                        }
                                    } else {
                                        addSessionBinding.subjectEdt.setError(getString(R.string.lessonName));
                                    }
                                } else {
                                    addSessionBinding.streamEdt.setError(getString(R.string.streamName));
                                }
                            } else {
                                addSessionBinding.standardEdt.setError(getString(R.string.standardName));
                            }
                        } else {
                            addSessionBinding.boardNameEdt.setError(getString(R.string.boardName));
                        }
                    } else {
                        addSessionBinding.sessionNameEdt.setError(getString(R.string.sessionName));
                        addSessionBinding.scrollView.setScrollY(0);
                    }
                } else {
                    Utils.ping(mContext, getResources().getString(R.string.class_type));
                }
            } else {
                if (!sessionTypeValueStr.equalsIgnoreCase("")) {
                    if (!coachIdStr.equalsIgnoreCase("") && !sessionNameStr.equalsIgnoreCase("")) {
                        if (!boardStr.equalsIgnoreCase("")) {
                            if (!standardStr.equalsIgnoreCase("")) {
                                if (!streamStr.equalsIgnoreCase("")) {
                                    if (!lessionTypeNameStr.equalsIgnoreCase("")) {
                                        if (!startDateStr.equalsIgnoreCase("")) {
                                            if (!endDateStr.equalsIgnoreCase("")) {
                                                if (!scheduleStr.equalsIgnoreCase("")) {
                                                    if (!address1Str.equalsIgnoreCase("")) {
                                                        if (!regionStr.equalsIgnoreCase("")) {
                                                            if (!cityStr.equalsIgnoreCase("")) {
                                                                if (!stateStr.equalsIgnoreCase("")) {
                                                                    if (!zipcodeStr.equalsIgnoreCase("")) {
                                                                        if (!sessioncapacityStr.equalsIgnoreCase("")) {
                                                                            if (!sessionamtStr.equalsIgnoreCase("")) {
                                                                                if (sessiontypeStr.equalsIgnoreCase("1")) {
                                                                                    callCreateSessionApi();
                                                                                } else {
                                                                                    if (startDateStr.equalsIgnoreCase(endDateStr)) {
                                                                                        callCreateSessionApi();
                                                                                    } else {
                                                                                        Utils.ping(mContext, getString(R.string.Date));
                                                                                    }
                                                                                }
                                                                            } else {
                                                                                addSessionBinding.sessionPriceEdt.setError(getString(R.string.sessionAmount));
                                                                            }
                                                                        } else {
                                                                            addSessionBinding.sportsEdt.setError(getString(R.string.sessionCapacity));
                                                                        }
                                                                    } else {
                                                                        addSessionBinding.zipcodeEdt.setError(getString(R.string.sessionZipcode));
                                                                    }
                                                                } else {
                                                                    addSessionBinding.stateEdt.setError(getString(R.string.sessionState));
                                                                }
                                                            } else {
                                                                addSessionBinding.cityEdt.setError(getString(R.string.sessionCity));
                                                            }
                                                        } else {
                                                            addSessionBinding.areaEdt.setError(getString(R.string.sessionArea));
                                                        }
                                                    } else {
                                                        Utils.ping(mContext, getString(R.string.sessionAddress));
                                                    }
                                                } else {
                                                    addSessionBinding.sessionTimeTxt.setError(getString(R.string.sessionTime));
                                                }
                                            } else {
                                                Utils.ping(mContext, getString(R.string.sessionTime));
                                            }
                                        } else {
                                            Utils.ping(mContext, getString(R.string.sessionTime));
                                        }
                                    } else {
                                        addSessionBinding.subjectEdt.setError(getString(R.string.lessonName));
                                    }
                                } else {
                                    addSessionBinding.streamEdt.setError(getString(R.string.streamName));
                                }
                            } else {
                                addSessionBinding.standardEdt.setError(getString(R.string.standardName));
                            }
                        } else {
                            addSessionBinding.boardNameEdt.setError(getString(R.string.boardName));
                        }
                    } else {
                        addSessionBinding.sessionNameEdt.setError(getString(R.string.sessionName));
                        addSessionBinding.scrollView.setScrollY(0);
                    }
                } else {
                    Utils.ping(mContext, getResources().getString(R.string.class_type));
                }
            }
        }

    }

    //Use for disable ClassDetail Edited
    public void disableControl() {
        addSessionBinding.sessionTimeTxt.setText("View Class Time");
        addSessionBinding.sessionTimeLinear.setVisibility(View.VISIBLE);
        addSessionBinding.submitBtn.setVisibility(View.GONE);
        addSessionBinding.recurringRb.setEnabled(false);
        addSessionBinding.singleRb.setEnabled(false);
        addSessionBinding.sessionNameEdt.setEnabled(false);
        addSessionBinding.boardNameEdt.setEnabled(false);
        addSessionBinding.standardEdt.setEnabled(false);
        addSessionBinding.streamEdt.setEnabled(false);
        addSessionBinding.subjectEdt.setEnabled(false);
        addSessionBinding.addressEdt.setEnabled(false);
        addSessionBinding.addressEdt1.setEnabled(false);
        addSessionBinding.areaEdt.setEnabled(false);
        addSessionBinding.cityEdt.setEnabled(false);
        addSessionBinding.stateEdt.setEnabled(false);
        addSessionBinding.zipcodeEdt.setEnabled(false);
        addSessionBinding.freeRb.setEnabled(false);
        addSessionBinding.paidRb.setEnabled(false);
        addSessionBinding.sportsEdt.setEnabled(false);
        addSessionBinding.alertBtn.setEnabled(false);
        addSessionBinding.sessionPriceEdt.setEnabled(false);
        addSessionBinding.AcademicRb.setEnabled(false);
        addSessionBinding.playRb.setEnabled(false);
        addSessionBinding.selectSessionAddressBtn.setVisibility(View.GONE);
        addSessionBinding.newSessionAddressBtn.setVisibility(View.GONE);
        addSessionBinding.sessionSelectAddressLinear.setVisibility(View.VISIBLE);
        addSessionBinding.sessionSelectAddressTxt.setText("View Class Address");
        addSessionBinding.descriptionEdt.setEnabled(true);
        addSessionBinding.descriptionEdt.setFocusableInTouchMode(false);
        addSessionBinding.descriptionEdt.clearFocus();
    }

    //Use for disable ClassTimeDetail Edited
    public void diableDialogControl() {

        addSessionDialogBinding.startDateTxt.setEnabled(false);
        addSessionDialogBinding.endDateTxt.setEnabled(false);

        addSessionDialogBinding.sunStartLinear.setEnabled(false);
        addSessionDialogBinding.sunEndLinear.setEnabled(false);
        addSessionDialogBinding.sunStartTimeTxt.setEnabled(false);
        addSessionDialogBinding.sunEndTimeTxt.setEnabled(false);
        addSessionDialogBinding.sunStartAddSessionBtn.setEnabled(false);
        addSessionDialogBinding.sunEndAddSessionBtn.setEnabled(false);
        addSessionDialogBinding.sunStartLinear.setAlpha(1);
        addSessionDialogBinding.sunEndLinear.setAlpha(1);

        addSessionDialogBinding.monStartLinear.setEnabled(false);
        addSessionDialogBinding.monEndLinear.setEnabled(false);
        addSessionDialogBinding.monStartTimeTxt.setEnabled(false);
        addSessionDialogBinding.monEndTimeTxt.setEnabled(false);
        addSessionDialogBinding.monStartAddSessionBtn.setEnabled(false);
        addSessionDialogBinding.monEndAddSessionBtn.setEnabled(false);
        addSessionDialogBinding.monStartLinear.setAlpha(1);
        addSessionDialogBinding.monEndLinear.setAlpha(1);

        addSessionDialogBinding.tueStartLinear.setEnabled(false);
        addSessionDialogBinding.tueEndLinear.setEnabled(false);
        addSessionDialogBinding.tueStartTimeTxt.setEnabled(false);
        addSessionDialogBinding.tueEndTimeTxt.setEnabled(false);
        addSessionDialogBinding.tueStartAddSessionBtn.setEnabled(false);
        addSessionDialogBinding.tueEndAddSessionBtn.setEnabled(false);
        addSessionDialogBinding.tueStartLinear.setAlpha(1);
        addSessionDialogBinding.tueEndLinear.setAlpha(1);

        addSessionDialogBinding.wedStartLinear.setEnabled(false);
        addSessionDialogBinding.wedEndLinear.setEnabled(false);
        addSessionDialogBinding.wedStartTimeTxt.setEnabled(false);
        addSessionDialogBinding.wedEndTimeTxt.setEnabled(false);
        addSessionDialogBinding.wedStartAddSessionBtn.setEnabled(false);
        addSessionDialogBinding.wedEndAddSessionBtn.setEnabled(false);
        addSessionDialogBinding.wedStartLinear.setAlpha(1);
        addSessionDialogBinding.wedEndLinear.setAlpha(1);

        addSessionDialogBinding.thuStartLinear.setEnabled(false);
        addSessionDialogBinding.thuEndLinear.setEnabled(false);
        addSessionDialogBinding.thuStartTimeTxt.setEnabled(false);
        addSessionDialogBinding.thuEndTimeTxt.setEnabled(false);
        addSessionDialogBinding.thuStartAddSessionBtn.setEnabled(false);
        addSessionDialogBinding.thuEndAddSessionBtn.setEnabled(false);
        addSessionDialogBinding.thuStartLinear.setAlpha(1);
        addSessionDialogBinding.thuEndLinear.setAlpha(1);


        addSessionDialogBinding.friStartLinear.setEnabled(false);
        addSessionDialogBinding.friEndLinear.setEnabled(false);
        addSessionDialogBinding.friStartTimeTxt.setEnabled(false);
        addSessionDialogBinding.friEndTimeTxt.setEnabled(false);
        addSessionDialogBinding.friStartAddSessionBtn.setEnabled(false);
        addSessionDialogBinding.friEndAddSessionBtn.setEnabled(false);
        addSessionDialogBinding.friStartLinear.setAlpha(1);
        addSessionDialogBinding.friEndLinear.setAlpha(1);

        addSessionDialogBinding.satStartLinear.setEnabled(false);
        addSessionDialogBinding.satEndLinear.setEnabled(false);
        addSessionDialogBinding.satStartTimeTxt.setEnabled(false);
        addSessionDialogBinding.satEndTimeTxt.setEnabled(false);
        addSessionDialogBinding.satStartAddSessionBtn.setEnabled(false);
        addSessionDialogBinding.satEndAddSessionBtn.setEnabled(false);
        addSessionDialogBinding.satStartLinear.setAlpha(1);
        addSessionDialogBinding.satEndLinear.setAlpha(1);

    }

    //Use for fill ClassCity
    public void fillCity() {
        ArrayList<String> CityName = new ArrayList<String>();
        ArrayList<String> StateName = new ArrayList<>();
        lineArray = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(mContext.getAssets().open("world_cities.txt")));

            Log.e("Reader Stuff", reader.readLine());
            while ((line = reader.readLine()) != null) {
                Log.e("code", line);
                RowData = line.split(",");
                CityName.add(RowData[0]);
                StateName.add(RowData[2]);
                lineArray.add(line);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        HashSet hs = new HashSet();
        hs.addAll(CityName);
        CityName.clear();
        CityName.addAll(hs);

        HashSet sh = new HashSet();
        sh.addAll(StateName);
        StateName.clear();
        StateName.addAll(sh);

        ArrayAdapter<String> adapterTerm = new ArrayAdapter<String>(mContext, R.layout.autocomplete_layout, CityName);
        addSessionBinding.cityEdt.setAdapter(adapterTerm);
        addSessionBinding.cityEdt.setThreshold(1);

        ArrayAdapter<String> adapterTerm1 = new ArrayAdapter<String>(mContext, R.layout.autocomplete_layout, StateName);
        addSessionBinding.stateEdt.setAdapter(adapterTerm1);
        addSessionBinding.stateEdt.setThreshold(1);

    }

    //Use for  fill ClassTime
    public void fillTimeGrid() {
        if (!scheduleStr.equalsIgnoreCase("")) {
            totalHours = new ArrayList<>();
            totalMinit = new ArrayList<>();

            addSessionBinding.linearClick.setVisibility(View.VISIBLE);
            addSessionBinding.startDateTxt.setText(EditStartDateStr);
            addSessionBinding.endDateTxt.setText(EditEndDateStr);


            addSessionBinding.sunTimeTxt.setEnabled(false);
            addSessionBinding.sundayBtn.setEnabled(false);
            addSessionBinding.sunHoursTxt.setEnabled(false);
            addSessionBinding.sunTimeTxt.setAlpha(0.2f);
            addSessionBinding.sundayBtn.setAlpha(0.2f);
            addSessionBinding.sunTimeTxt.setText("");
            addSessionBinding.sunHoursTxt.setAlpha(0.2f);
            addSessionBinding.sunHoursTxt.setText("");

            addSessionBinding.monTimeTxt.setEnabled(false);
            addSessionBinding.mondayBtn.setEnabled(false);
            addSessionBinding.monHoursTxt.setEnabled(false);
            addSessionBinding.monTimeTxt.setAlpha(0.2f);
            addSessionBinding.mondayBtn.setAlpha(0.2f);
            addSessionBinding.monTimeTxt.setText("");
            addSessionBinding.monHoursTxt.setAlpha(0.2f);
            addSessionBinding.monHoursTxt.setText("");

            addSessionBinding.tuesTimeTxt.setEnabled(false);
            addSessionBinding.tuesdayBtn.setEnabled(false);
            addSessionBinding.tuesHoursTxt.setEnabled(false);
            addSessionBinding.tuesTimeTxt.setAlpha(0.2f);
            addSessionBinding.tuesdayBtn.setAlpha(0.2f);
            addSessionBinding.tuesTimeTxt.setText("");
            addSessionBinding.tuesHoursTxt.setAlpha(0.2f);
            addSessionBinding.tuesHoursTxt.setText("");

            addSessionBinding.wedTimeTxt.setEnabled(false);
            addSessionBinding.wednesdayBtn.setEnabled(false);
            addSessionBinding.wedHoursTxt.setEnabled(false);
            addSessionBinding.wedTimeTxt.setAlpha(0.2f);
            addSessionBinding.wednesdayBtn.setAlpha(0.2f);
            addSessionBinding.wedTimeTxt.setText("");
            addSessionBinding.wedHoursTxt.setAlpha(0.2f);
            addSessionBinding.wedHoursTxt.setText("");

            addSessionBinding.thurTimeTxt.setEnabled(false);
            addSessionBinding.thursdayBtn.setEnabled(false);
            addSessionBinding.thurHoursTxt.setEnabled(false);
            addSessionBinding.thurTimeTxt.setAlpha(0.2f);
            addSessionBinding.thursdayBtn.setAlpha(0.2f);
            addSessionBinding.thurTimeTxt.setText("");
            addSessionBinding.thurHoursTxt.setAlpha(0.2f);
            addSessionBinding.thurHoursTxt.setText("");

            addSessionBinding.friTimeTxt.setEnabled(false);
            addSessionBinding.fridayBtn.setEnabled(false);
            addSessionBinding.friHoursTxt.setEnabled(false);
            addSessionBinding.friTimeTxt.setAlpha(0.2f);
            addSessionBinding.fridayBtn.setAlpha(0.2f);
            addSessionBinding.friTimeTxt.setText("");
            addSessionBinding.friHoursTxt.setAlpha(0.2f);
            addSessionBinding.friHoursTxt.setText("");

            addSessionBinding.satTimeTxt.setEnabled(false);
            addSessionBinding.saturdayBtn.setEnabled(false);
            addSessionBinding.satHoursTxt.setEnabled(false);
            addSessionBinding.satTimeTxt.setAlpha(0.2f);
            addSessionBinding.saturdayBtn.setAlpha(0.2f);
            addSessionBinding.satTimeTxt.setText("");
            addSessionBinding.satHoursTxt.setAlpha(0.2f);
            addSessionBinding.satHoursTxt.setText("");

            String[] spiltPipes = scheduleStr.split("\\|");
            String[] spiltComma;
            String[] spiltDash;
            Log.d("spilt", "" + spiltPipes.toString());
            for (int i = 0; i < spiltPipes.length; i++) {
                spiltComma = spiltPipes[i].split("\\,");
                spiltDash = spiltComma[1].split("\\-");
                calculateHours(spiltDash[0], spiltDash[1]);
                switch (spiltComma[0]) {
                    case "sun":
                        addSessionBinding.sunTimeTxt.setEnabled(true);
                        addSessionBinding.sundayBtn.setEnabled(true);
                        addSessionBinding.sunHoursTxt.setAlpha(1);
                        addSessionBinding.sunHoursTxt.setEnabled(true);
                        addSessionBinding.sunTimeTxt.setAlpha(1);
                        addSessionBinding.sundayBtn.setAlpha(1);
                        addSessionBinding.sunHoursTxt.setAlpha(1);
                        addSessionBinding.sunTimeTxt.setText(spiltDash[0]);
                        addSessionBinding.sunHoursTxt.setText(SessionDuration);

                        break;
                    case "mon":
                        addSessionBinding.monTimeTxt.setEnabled(true);
                        addSessionBinding.mondayBtn.setEnabled(true);
                        addSessionBinding.monTimeTxt.setAlpha(1);
                        addSessionBinding.mondayBtn.setAlpha(1);
                        addSessionBinding.monHoursTxt.setAlpha(1);
                        addSessionBinding.monTimeTxt.setText(spiltDash[0]);
                        addSessionBinding.monHoursTxt.setText(SessionDuration);
                        break;
                    case "tue":
                        addSessionBinding.tuesTimeTxt.setEnabled(true);
                        addSessionBinding.tuesdayBtn.setEnabled(true);
                        addSessionBinding.tuesTimeTxt.setAlpha(1);
                        addSessionBinding.tuesdayBtn.setAlpha(1);
                        addSessionBinding.tuesHoursTxt.setAlpha(1);
                        addSessionBinding.tuesTimeTxt.setText(spiltDash[0]);
                        addSessionBinding.tuesHoursTxt.setText(SessionDuration);
                        break;
                    case "wed":
                        addSessionBinding.wedTimeTxt.setEnabled(true);
                        addSessionBinding.wednesdayBtn.setEnabled(true);
                        addSessionBinding.wedTimeTxt.setAlpha(1);
                        addSessionBinding.wednesdayBtn.setAlpha(1);
                        addSessionBinding.wedHoursTxt.setAlpha(1);
                        addSessionBinding.wedTimeTxt.setText(spiltDash[0]);
                        addSessionBinding.wedHoursTxt.setText(SessionDuration);
                        break;
                    case "thu":
                        addSessionBinding.thurTimeTxt.setEnabled(true);
                        addSessionBinding.thursdayBtn.setEnabled(true);
                        addSessionBinding.thurTimeTxt.setAlpha(1);
                        addSessionBinding.thursdayBtn.setAlpha(1);
                        addSessionBinding.thurHoursTxt.setAlpha(1);
                        addSessionBinding.thurTimeTxt.setText(spiltDash[0]);
                        addSessionBinding.thurHoursTxt.setText(SessionDuration);
                        break;
                    case "fri":
                        addSessionBinding.friTimeTxt.setEnabled(true);
                        addSessionBinding.fridayBtn.setEnabled(true);
                        addSessionBinding.friTimeTxt.setAlpha(1);
                        addSessionBinding.fridayBtn.setAlpha(1);
                        addSessionBinding.friHoursTxt.setAlpha(1);
                        addSessionBinding.friTimeTxt.setText(spiltDash[0]);
                        addSessionBinding.friHoursTxt.setText(SessionDuration);
                        break;
                    case "sat":
                        addSessionBinding.satTimeTxt.setEnabled(true);
                        addSessionBinding.saturdayBtn.setEnabled(true);
                        addSessionBinding.satTimeTxt.setAlpha(1);
                        addSessionBinding.saturdayBtn.setAlpha(1);
                        addSessionBinding.satHoursTxt.setAlpha(1);
                        addSessionBinding.satTimeTxt.setText(spiltDash[0]);
                        addSessionBinding.satHoursTxt.setText(SessionDuration);
                        break;
                    default:

                }

//                Log.d("totalHours + totalMinit", totalHours.toString() + "  " + totalMinit.toString());
//
//
////                if (SessionHour < 10) {
////                    hours = "0" + SessionHour;
////                } else {
//                hours = String.valueOf(SessionHour);
////                }
////                if (SessionMinit < 10) {
////                    minit = "0" + SessionMinit;
////                } else {
//                minit = String.valueOf(SessionMinit);
////
            }
//            averageHours(totalHours);
//            averageMinit(totalMinit);
//
//            if (totalHours.size() > 0) {
//                for (int i = 0; i < totalHours.size(); i++) {
//
//                }
//            }


//            if (avgMinitvalue == 0) {
//                addSessionBinding.displayDurationTxt.setText("Duration : " + avgHoursvalue + " hr ");
//            } else {
//                addSessionBinding.displayDurationTxt.setText("Duration : " + avgHoursvalue + " hr " + avgMinitvalue + " min");
//            }
        }
    }

    //Use for calculate ClassTime
    public void calculateHours(String time1, String time2) {

        Date date1, date2;
        int days, hours, min;
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
            totalMinit.add(SessionMinit);

            if (SessionMinit > 0) {
                if (SessionMinit < 10) {
                    SessionDuration = String.valueOf(SessionHour) + ":" + String.valueOf("0" + SessionMinit + " hrs");
                } else {
                    SessionDuration = String.valueOf(SessionHour) + ":" + String.valueOf(SessionMinit + " hrs");

                }
            } else {
                SessionDuration = String.valueOf(SessionHour) + ":" + "00" + " hrs";
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    //Use for Get AllSession Detail
    public void callGetSessionDetailApi() {
        if (Utils.isNetworkConnected(mContext)) {
            //Utils.showDialog(mContext);
            ApiHandler.getApiService().get_SessionDetailByCoachID(getsessionDetail(), new retrofit.Callback<SessionDetailModel>() {
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
                        Utils.dismissDialog();
                        if (sessionInfo.getData() != null) {
                            Utils.ping(mContext, getString(R.string.false_msg));
                        }
                        return;
                    }
                    if (sessionInfo.getSuccess().equalsIgnoreCase("True")) {
                        Utils.dismissDialog();
                        if (sessionInfo.getData() != null) {
                            finalsessionfullDetailModel = sessionInfo;

                            Log.d("DataModel", "" + finalsessionfullDetailModel.getData().size());

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
        map.put("CoachID", Utils.getPref(mContext, "coachID"));//Util.getPref(mContext, "coachID"));
        return map;
    }

    //Use for select ClassTime
    public static class TimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            TimePickerDialog tpd4 = new TimePickerDialog(getActivity(),
                    android.app.AlertDialog.THEME_HOLO_LIGHT, this, hour, minute, android.text.format.DateFormat.is24HourFormat(getActivity()));
            return tpd4;
        }

        @Override
        public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
            String status = "AM";

            if (hourOfDay > 11) {
                // If the hour is greater than or equal to 12
                // Then the current AM PM status is PM
                status = "PM";
            }

            // Initialize a new variable to hold 12 hour format hour value
            int hour_of_12_hour_format;
            String hour = "";
            if (hourOfDay > 11) {
                // If the hour is greater than or equal to 12
                // Then we subtract 12 from the hour to make it 12 hour format time

                hour_of_12_hour_format = hourOfDay - 12;

            } else {
                hour_of_12_hour_format = hourOfDay;
            }
            if (hour_of_12_hour_format == 0) {
                hour_of_12_hour_format = 12;
            }

            if (hour_of_12_hour_format < 10) {
                hour = "0" + hour_of_12_hour_format;
                hourFinal = hour;
            } else {
                hourFinal = String.valueOf(hour_of_12_hour_format);
            }

            String m = "";
            if (minute < 10) {
                m = "0" + minute;
                minuteFinal = m;
            } else {
                minuteFinal = String.valueOf(minute);
            }

            FinalTimeStr = hourFinal + ":" + minuteFinal + " " + status;
            switch (Tag) {
                case "0":
                    if (finalsessionfullDetailModel.getData().size()>0) {
                        for (int i = 0; i < finalsessionfullDetailModel.getData().size(); i++) {
                            for (int j = 0; j < finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().size(); j++) {
                                if (finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionDate()
                                        .equalsIgnoreCase(addSessionDialogBinding.startDateTxt.getText().toString())) {
                                    String[] splittime = finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionTime().split("\\-");
                                    try {
                                        SimpleDateFormat sdft = new SimpleDateFormat("hh:mm aa", Locale.US);
                                        Date inTimet = sdft.parse(splittime[0]);
                                        Date outTimet = sdft.parse(splittime[1]);
                                        Date inTimeSelected = sdft.parse(FinalTimeStr);
                                        // Date outTimeSelected = sdf16.parse("10:00 PM");
                                        if (inTimeSelected.equals(inTimet) || inTimeSelected.equals(outTimet)) {
                                            Utils.ping(mContext, "You have already class at this time");
                                        } else if (inTimeSelected.after(inTimet) && inTimeSelected.before(outTimet)) {
                                            Utils.ping(mContext, "You have already class at this time");
                                        } else {
                                            addSessionDialogBinding.sunStartTimeTxt.setText(FinalTimeStr);
                                            addSessionDialogBinding.sunStartAddSessionBtn.setText("x");
                                            addSessionDialogBinding.sunStartAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                                            addSessionDialogBinding.sunStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                                            SimpleDateFormat sdf16 = new SimpleDateFormat("hh:mm aa", Locale.US);
                                            try {
                                                Date inTime = sdf16.parse(addSessionDialogBinding.sunStartTimeTxt.getText().toString());
                                                Date outTime = sdf16.parse(addSessionDialogBinding.sunEndTimeTxt.getText().toString());
                                                if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                                    addSessionDialogBinding.sunEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                                    addSessionDialogBinding.sunEndLinear.setBackgroundResource(R.drawable.red_linear);
                                                    checkTime_sun = true;
                                                } else {
                                                    checkTime_sun = false;
                                                    addSessionDialogBinding.sunEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                                    addSessionDialogBinding.sunEndLinear.setBackgroundResource(R.drawable.linear_shape);
                                                }
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    addSessionDialogBinding.sunStartTimeTxt.setText(FinalTimeStr);
                                    addSessionDialogBinding.sunStartAddSessionBtn.setText("x");
                                    addSessionDialogBinding.sunStartAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                                    addSessionDialogBinding.sunStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                                    SimpleDateFormat sdf16 = new SimpleDateFormat("hh:mm aa", Locale.US);
                                    try {
                                        Date inTime = sdf16.parse(addSessionDialogBinding.sunStartTimeTxt.getText().toString());
                                        Date outTime = sdf16.parse(addSessionDialogBinding.sunEndTimeTxt.getText().toString());
                                        if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                            addSessionDialogBinding.sunEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                            addSessionDialogBinding.sunEndLinear.setBackgroundResource(R.drawable.red_linear);
                                            checkTime_sun = true;
                                        } else {
                                            checkTime_sun = false;
                                            addSessionDialogBinding.sunEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                            addSessionDialogBinding.sunEndLinear.setBackgroundResource(R.drawable.linear_shape);
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        }
                    }else{
                        addSessionDialogBinding.sunStartTimeTxt.setText(FinalTimeStr);
                        addSessionDialogBinding.sunStartAddSessionBtn.setText("x");
                        addSessionDialogBinding.sunStartAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                        addSessionDialogBinding.sunStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                        SimpleDateFormat sdf16 = new SimpleDateFormat("hh:mm aa", Locale.US);
                        try {
                            Date inTime = sdf16.parse(addSessionDialogBinding.sunStartTimeTxt.getText().toString());
                            Date outTime = sdf16.parse(addSessionDialogBinding.sunEndTimeTxt.getText().toString());
                            if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                addSessionDialogBinding.sunEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                addSessionDialogBinding.sunEndLinear.setBackgroundResource(R.drawable.red_linear);
                                checkTime_sun = true;
                            } else {
                                checkTime_sun = false;
                                addSessionDialogBinding.sunEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                addSessionDialogBinding.sunEndLinear.setBackgroundResource(R.drawable.linear_shape);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "1":
                    if (finalsessionfullDetailModel.getData().size()>0) {
                        for (int i = 0; i < finalsessionfullDetailModel.getData().size(); i++) {
                            for (int j = 0; j < finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().size(); j++) {
                                if (finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionDate()
                                        .equalsIgnoreCase(addSessionDialogBinding.startDateTxt.getText().toString())) {
                                    String[] splittime = finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionTime().split("\\-");
                                    try {
                                        SimpleDateFormat sdft = new SimpleDateFormat("hh:mm aa", Locale.US);
                                        Date inTimet = sdft.parse(splittime[0]);
                                        Date outTimet = sdft.parse(splittime[1]);
                                        Date inTimeSelected = sdft.parse(FinalTimeStr);
                                        Date starttime = sdft.parse(addSessionDialogBinding.sunStartTimeTxt.getText().toString());
                                        // Date outTimeSelected = sdf16.parse("10:00 PM");
                                        if (starttime.after(outTimet)) {
                                            addSessionDialogBinding.sunEndTimeTxt.setText(FinalTimeStr);
                                            addSessionDialogBinding.sunEndAddSessionBtn.setText("x");
                                            addSessionDialogBinding.sunEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                                            addSessionDialogBinding.sunEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                                            SimpleDateFormat sdf3 = new SimpleDateFormat("hh:mm aa", Locale.US);
                                            try {
                                                Date inTime = sdf3.parse(addSessionDialogBinding.sunStartTimeTxt.getText().toString());
                                                Date outTime = sdf3.parse(addSessionDialogBinding.sunEndTimeTxt.getText().toString());
                                                if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                                    addSessionDialogBinding.sunEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                                    addSessionDialogBinding.sunEndLinear.setBackgroundResource(R.drawable.red_linear);
                                                    checkTime_sun = true;
                                                } else {
                                                    checkTime_sun = false;
                                                    addSessionDialogBinding.sunEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                                    addSessionDialogBinding.sunEndLinear.setBackgroundResource(R.drawable.linear_shape);
                                                }
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            if (inTimeSelected.equals(inTimet) || inTimeSelected.equals(outTimet)) {
                                                Utils.ping(mContext, "You have already class at this time");
                                            } else if (inTimeSelected.after(inTimet) && inTimeSelected.before(outTimet)) {
                                                Utils.ping(mContext, "You have already class at this time");
                                            } else if (inTimeSelected.after(inTimet) && inTimeSelected.after(outTimet)) {
                                                Utils.ping(mContext, "You have already class at this time");
                                            } else {
                                                addSessionDialogBinding.sunEndTimeTxt.setText(FinalTimeStr);
                                                addSessionDialogBinding.sunEndAddSessionBtn.setText("x");
                                                addSessionDialogBinding.sunEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                                                addSessionDialogBinding.sunEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                                                SimpleDateFormat sdf3 = new SimpleDateFormat("hh:mm aa", Locale.US);
                                                try {
                                                    Date inTime = sdf3.parse(addSessionDialogBinding.sunStartTimeTxt.getText().toString());
                                                    Date outTime = sdf3.parse(addSessionDialogBinding.sunEndTimeTxt.getText().toString());
                                                    if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                                        addSessionDialogBinding.sunEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                                        addSessionDialogBinding.sunEndLinear.setBackgroundResource(R.drawable.red_linear);
                                                        checkTime_sun = true;
                                                    } else {
                                                        checkTime_sun = false;
                                                        addSessionDialogBinding.sunEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                                        addSessionDialogBinding.sunEndLinear.setBackgroundResource(R.drawable.linear_shape);
                                                    }
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    addSessionDialogBinding.sunEndTimeTxt.setText(FinalTimeStr);
                                    addSessionDialogBinding.sunEndAddSessionBtn.setText("x");
                                    addSessionDialogBinding.sunEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                                    addSessionDialogBinding.sunEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                                    SimpleDateFormat sdf3 = new SimpleDateFormat("hh:mm aa", Locale.US);
                                    try {
                                        Date inTime = sdf3.parse(addSessionDialogBinding.sunStartTimeTxt.getText().toString());
                                        Date outTime = sdf3.parse(addSessionDialogBinding.sunEndTimeTxt.getText().toString());
                                        if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                            addSessionDialogBinding.sunEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                            addSessionDialogBinding.sunEndLinear.setBackgroundResource(R.drawable.red_linear);
                                            checkTime_sun = true;
                                        } else {
                                            checkTime_sun = false;
                                            addSessionDialogBinding.sunEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                            addSessionDialogBinding.sunEndLinear.setBackgroundResource(R.drawable.linear_shape);
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }else{
                        addSessionDialogBinding.sunEndTimeTxt.setText(FinalTimeStr);
                        addSessionDialogBinding.sunEndAddSessionBtn.setText("x");
                        addSessionDialogBinding.sunEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                        addSessionDialogBinding.sunEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                        SimpleDateFormat sdf3 = new SimpleDateFormat("hh:mm aa", Locale.US);
                        try {
                            Date inTime = sdf3.parse(addSessionDialogBinding.sunStartTimeTxt.getText().toString());
                            Date outTime = sdf3.parse(addSessionDialogBinding.sunEndTimeTxt.getText().toString());
                            if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                addSessionDialogBinding.sunEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                addSessionDialogBinding.sunEndLinear.setBackgroundResource(R.drawable.red_linear);
                                checkTime_sun = true;
                            } else {
                                checkTime_sun = false;
                                addSessionDialogBinding.sunEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                addSessionDialogBinding.sunEndLinear.setBackgroundResource(R.drawable.linear_shape);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "2":
                    if (finalsessionfullDetailModel.getData().size()>0) {
                        for (int i = 0; i < finalsessionfullDetailModel.getData().size(); i++) {
                            for (int j = 0; j < finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().size(); j++) {
                                if (finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionDate()
                                        .equalsIgnoreCase(addSessionDialogBinding.startDateTxt.getText().toString())) {
                                    String[] splittime = finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionTime().split("\\-");
                                    try {
                                        SimpleDateFormat sdft = new SimpleDateFormat("hh:mm aa", Locale.US);
                                        Date inTimet = sdft.parse(splittime[0]);
                                        Date outTimet = sdft.parse(splittime[1]);
                                        Date inTimeSelected = sdft.parse(FinalTimeStr);
                                        // Date outTimeSelected = sdf16.parse("10:00 PM");
                                        if (inTimeSelected.equals(inTimet) || inTimeSelected.equals(outTimet)) {
                                            Utils.ping(mContext, "You have already class at this time");
                                        } else if (inTimeSelected.after(inTimet) && inTimeSelected.before(outTimet)) {
                                            Utils.ping(mContext, "You have already class at this time");
                                        } else {
                                            addSessionDialogBinding.monStartTimeTxt.setText(FinalTimeStr);
                                            addSessionDialogBinding.monStartAddSessionBtn.setText("x");
                                            addSessionDialogBinding.monStartAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                                            addSessionDialogBinding.monStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                                            SimpleDateFormat sdf16 = new SimpleDateFormat("hh:mm aa", Locale.US);
                                            try {
                                                Date inTime = sdf16.parse(addSessionDialogBinding.monStartTimeTxt.getText().toString());
                                                Date outTime = sdf16.parse(addSessionDialogBinding.monEndTimeTxt.getText().toString());
                                                if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                                    addSessionDialogBinding.monEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                                    addSessionDialogBinding.monEndLinear.setBackgroundResource(R.drawable.red_linear);
                                                    checkTime_mon = true;
                                                } else {
                                                    checkTime_mon = false;
                                                    addSessionDialogBinding.monEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                                    addSessionDialogBinding.monEndLinear.setBackgroundResource(R.drawable.linear_shape);
                                                }
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    addSessionDialogBinding.monStartTimeTxt.setText(FinalTimeStr);
                                    addSessionDialogBinding.monStartAddSessionBtn.setText("x");
                                    addSessionDialogBinding.monStartAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                                    addSessionDialogBinding.monStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                                    SimpleDateFormat sdf16 = new SimpleDateFormat("hh:mm aa", Locale.US);
                                    try {
                                        Date inTime = sdf16.parse(addSessionDialogBinding.monStartTimeTxt.getText().toString());
                                        Date outTime = sdf16.parse(addSessionDialogBinding.monEndTimeTxt.getText().toString());
                                        if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                            addSessionDialogBinding.monEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                            addSessionDialogBinding.monEndLinear.setBackgroundResource(R.drawable.red_linear);
                                            checkTime_mon = true;
                                        } else {
                                            checkTime_mon = false;
                                            addSessionDialogBinding.monEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                            addSessionDialogBinding.monEndLinear.setBackgroundResource(R.drawable.linear_shape);
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        }
                    }else{
                        addSessionDialogBinding.monStartTimeTxt.setText(FinalTimeStr);
                        addSessionDialogBinding.monStartAddSessionBtn.setText("x");
                        addSessionDialogBinding.monStartAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                        addSessionDialogBinding.monStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                        SimpleDateFormat sdf16 = new SimpleDateFormat("hh:mm aa", Locale.US);
                        try {
                            Date inTime = sdf16.parse(addSessionDialogBinding.monStartTimeTxt.getText().toString());
                            Date outTime = sdf16.parse(addSessionDialogBinding.monEndTimeTxt.getText().toString());
                            if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                addSessionDialogBinding.monEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                addSessionDialogBinding.monEndLinear.setBackgroundResource(R.drawable.red_linear);
                                checkTime_mon = true;
                            } else {
                                checkTime_mon = false;
                                addSessionDialogBinding.monEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                addSessionDialogBinding.monEndLinear.setBackgroundResource(R.drawable.linear_shape);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "3":
                    if (finalsessionfullDetailModel.getData().size()>0) {
                        for (int i = 0; i < finalsessionfullDetailModel.getData().size(); i++) {
                            for (int j = 0; j < finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().size(); j++) {
                                if (finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionDate()
                                        .equalsIgnoreCase(addSessionDialogBinding.startDateTxt.getText().toString())) {
                                    String[] splittime = finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionTime().split("\\-");
                                    try {
                                        SimpleDateFormat sdft = new SimpleDateFormat("hh:mm aa", Locale.US);
                                        Date inTimet = sdft.parse(splittime[0]);
                                        Date outTimet = sdft.parse(splittime[1]);
                                        Date inTimeSelected = sdft.parse(FinalTimeStr);
                                        Date starttime = sdft.parse(addSessionDialogBinding.monStartTimeTxt.getText().toString());
                                        // Date outTimeSelected = sdf16.parse("10:00 PM");
                                        if (starttime.after(outTimet)) {
                                            addSessionDialogBinding.monEndTimeTxt.setText(FinalTimeStr);
                                            addSessionDialogBinding.monEndAddSessionBtn.setText("x");
                                            addSessionDialogBinding.monEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                                            addSessionDialogBinding.monEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                                            SimpleDateFormat sdf3 = new SimpleDateFormat("hh:mm aa", Locale.US);
                                            try {
                                                Date inTime = sdf3.parse(addSessionDialogBinding.monStartTimeTxt.getText().toString());
                                                Date outTime = sdf3.parse(addSessionDialogBinding.monEndTimeTxt.getText().toString());
                                                if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                                    addSessionDialogBinding.monEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                                    addSessionDialogBinding.monEndLinear.setBackgroundResource(R.drawable.red_linear);
                                                    checkTime_mon = true;
                                                } else {
                                                    checkTime_mon = false;
                                                    addSessionDialogBinding.monEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                                    addSessionDialogBinding.monEndLinear.setBackgroundResource(R.drawable.linear_shape);
                                                }
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            if (inTimeSelected.equals(inTimet) || inTimeSelected.equals(outTimet)) {
                                                Utils.ping(mContext, "You have already class at this time");
                                            } else if (inTimeSelected.after(inTimet) && inTimeSelected.before(outTimet)) {
                                                Utils.ping(mContext, "You have already class at this time");
                                            } else if (inTimeSelected.after(inTimet) && inTimeSelected.after(outTimet)) {
                                                Utils.ping(mContext, "You have already class at this time");
                                            } else {
                                                addSessionDialogBinding.monEndTimeTxt.setText(FinalTimeStr);
                                                addSessionDialogBinding.monEndAddSessionBtn.setText("x");
                                                addSessionDialogBinding.monEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                                                addSessionDialogBinding.monEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                                                SimpleDateFormat sdf3 = new SimpleDateFormat("hh:mm aa", Locale.US);
                                                try {
                                                    Date inTime = sdf3.parse(addSessionDialogBinding.monStartTimeTxt.getText().toString());
                                                    Date outTime = sdf3.parse(addSessionDialogBinding.monEndTimeTxt.getText().toString());
                                                    if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                                        addSessionDialogBinding.monEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                                        addSessionDialogBinding.monEndLinear.setBackgroundResource(R.drawable.red_linear);
                                                        checkTime_mon = true;
                                                    } else {
                                                        checkTime_mon = false;
                                                        addSessionDialogBinding.monEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                                        addSessionDialogBinding.monEndLinear.setBackgroundResource(R.drawable.linear_shape);
                                                    }
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    addSessionDialogBinding.monEndTimeTxt.setText(FinalTimeStr);
                                    addSessionDialogBinding.monEndAddSessionBtn.setText("x");
                                    addSessionDialogBinding.monEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                                    addSessionDialogBinding.monEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                                    SimpleDateFormat sdf3 = new SimpleDateFormat("hh:mm aa", Locale.US);
                                    try {
                                        Date inTime = sdf3.parse(addSessionDialogBinding.monStartTimeTxt.getText().toString());
                                        Date outTime = sdf3.parse(addSessionDialogBinding.monEndTimeTxt.getText().toString());
                                        if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                            addSessionDialogBinding.monEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                            addSessionDialogBinding.monEndLinear.setBackgroundResource(R.drawable.red_linear);
                                            checkTime_mon = true;
                                        } else {
                                            checkTime_mon = false;
                                            addSessionDialogBinding.monEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                            addSessionDialogBinding.monEndLinear.setBackgroundResource(R.drawable.linear_shape);
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }else{
                        addSessionDialogBinding.monEndTimeTxt.setText(FinalTimeStr);
                        addSessionDialogBinding.monEndAddSessionBtn.setText("x");
                        addSessionDialogBinding.monEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                        addSessionDialogBinding.monEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                        SimpleDateFormat sdf3 = new SimpleDateFormat("hh:mm aa", Locale.US);
                        try {
                            Date inTime = sdf3.parse(addSessionDialogBinding.monStartTimeTxt.getText().toString());
                            Date outTime = sdf3.parse(addSessionDialogBinding.monEndTimeTxt.getText().toString());
                            if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                addSessionDialogBinding.monEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                addSessionDialogBinding.monEndLinear.setBackgroundResource(R.drawable.red_linear);
                                checkTime_mon = true;
                            } else {
                                checkTime_mon = false;
                                addSessionDialogBinding.monEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                addSessionDialogBinding.monEndLinear.setBackgroundResource(R.drawable.linear_shape);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "4":
                    if (finalsessionfullDetailModel.getData().size()>0) {
                        for (int i = 0; i < finalsessionfullDetailModel.getData().size(); i++) {
                            for (int j = 0; j < finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().size(); j++) {
                                if (finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionDate()
                                        .equalsIgnoreCase(addSessionDialogBinding.startDateTxt.getText().toString())) {
                                    String[] splittime = finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionTime().split("\\-");
                                    try {
                                        SimpleDateFormat sdft = new SimpleDateFormat("hh:mm aa", Locale.US);
                                        Date inTimet = sdft.parse(splittime[0]);
                                        Date outTimet = sdft.parse(splittime[1]);
                                        Date inTimeSelected = sdft.parse(FinalTimeStr);
                                        // Date outTimeSelected = sdf16.parse("10:00 PM");
                                        if (inTimeSelected.equals(inTimet) || inTimeSelected.equals(outTimet)) {
                                            Utils.ping(mContext, "You have already class at this time");
                                        } else if (inTimeSelected.after(inTimet) && inTimeSelected.before(outTimet)) {
                                            Utils.ping(mContext, "You have already class at this time");
                                        } else {
                                            addSessionDialogBinding.tueStartTimeTxt.setText(FinalTimeStr);
                                            addSessionDialogBinding.tueStartAddSessionBtn.setText("x");
                                            addSessionDialogBinding.tueStartAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                                            addSessionDialogBinding.tueStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                                            SimpleDateFormat sdf16 = new SimpleDateFormat("hh:mm aa", Locale.US);
                                            try {
                                                Date inTime = sdf16.parse(addSessionDialogBinding.tueStartTimeTxt.getText().toString());
                                                Date outTime = sdf16.parse(addSessionDialogBinding.tueEndTimeTxt.getText().toString());
                                                if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                                    addSessionDialogBinding.tueEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                                    addSessionDialogBinding.tueEndLinear.setBackgroundResource(R.drawable.red_linear);
                                                    checkTime_tue = true;
                                                } else {
                                                    checkTime_tue = false;
                                                    addSessionDialogBinding.tueEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                                    addSessionDialogBinding.tueEndLinear.setBackgroundResource(R.drawable.linear_shape);
                                                }
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    addSessionDialogBinding.tueStartTimeTxt.setText(FinalTimeStr);
                                    addSessionDialogBinding.tueStartAddSessionBtn.setText("x");
                                    addSessionDialogBinding.tueStartAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                                    addSessionDialogBinding.tueStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                                    SimpleDateFormat sdf16 = new SimpleDateFormat("hh:mm aa", Locale.US);
                                    try {
                                        Date inTime = sdf16.parse(addSessionDialogBinding.tueStartTimeTxt.getText().toString());
                                        Date outTime = sdf16.parse(addSessionDialogBinding.tueEndTimeTxt.getText().toString());
                                        if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                            addSessionDialogBinding.tueEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                            addSessionDialogBinding.tueEndLinear.setBackgroundResource(R.drawable.red_linear);
                                            checkTime_tue = true;
                                        } else {
                                            checkTime_tue = false;
                                            addSessionDialogBinding.tueEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                            addSessionDialogBinding.tueEndLinear.setBackgroundResource(R.drawable.linear_shape);
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        }
                    }else{
                        addSessionDialogBinding.tueStartTimeTxt.setText(FinalTimeStr);
                        addSessionDialogBinding.tueStartAddSessionBtn.setText("x");
                        addSessionDialogBinding.tueStartAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                        addSessionDialogBinding.tueStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                        SimpleDateFormat sdf16 = new SimpleDateFormat("hh:mm aa", Locale.US);
                        try {
                            Date inTime = sdf16.parse(addSessionDialogBinding.tueStartTimeTxt.getText().toString());
                            Date outTime = sdf16.parse(addSessionDialogBinding.tueEndTimeTxt.getText().toString());
                            if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                addSessionDialogBinding.tueEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                addSessionDialogBinding.tueEndLinear.setBackgroundResource(R.drawable.red_linear);
                                checkTime_tue = true;
                            } else {
                                checkTime_tue = false;
                                addSessionDialogBinding.tueEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                addSessionDialogBinding.tueEndLinear.setBackgroundResource(R.drawable.linear_shape);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "5":
                    if (finalsessionfullDetailModel.getData().size()>0) {
                        for (int i = 0; i < finalsessionfullDetailModel.getData().size(); i++) {
                            for (int j = 0; j < finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().size(); j++) {
                                if (finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionDate()
                                        .equalsIgnoreCase(addSessionDialogBinding.startDateTxt.getText().toString())) {
                                    String[] splittime = finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionTime().split("\\-");
                                    try {
                                        SimpleDateFormat sdft = new SimpleDateFormat("hh:mm aa", Locale.US);
                                        Date inTimet = sdft.parse(splittime[0]);
                                        Date outTimet = sdft.parse(splittime[1]);
                                        Date inTimeSelected = sdft.parse(FinalTimeStr);
                                        Date starttime = sdft.parse(addSessionDialogBinding.tueStartTimeTxt.getText().toString());
                                        // Date outTimeSelected = sdf16.parse("10:00 PM");
                                        if (starttime.after(outTimet)) {
                                            addSessionDialogBinding.tueEndTimeTxt.setText(FinalTimeStr);
                                            addSessionDialogBinding.tueEndAddSessionBtn.setText("x");
                                            addSessionDialogBinding.tueEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                                            addSessionDialogBinding.tueEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                                            SimpleDateFormat sdf3 = new SimpleDateFormat("hh:mm aa", Locale.US);
                                            try {
                                                Date inTime = sdf3.parse(addSessionDialogBinding.tueStartTimeTxt.getText().toString());
                                                Date outTime = sdf3.parse(addSessionDialogBinding.tueEndTimeTxt.getText().toString());
                                                if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                                    addSessionDialogBinding.tueEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                                    addSessionDialogBinding.tueEndLinear.setBackgroundResource(R.drawable.red_linear);
                                                    checkTime_tue = true;
                                                } else {
                                                    checkTime_tue = false;
                                                    addSessionDialogBinding.tueEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                                    addSessionDialogBinding.tueEndLinear.setBackgroundResource(R.drawable.linear_shape);
                                                }
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            if (inTimeSelected.equals(inTimet) || inTimeSelected.equals(outTimet)) {
                                                Utils.ping(mContext, "You have already class at this time");
                                            } else if (inTimeSelected.after(inTimet) && inTimeSelected.before(outTimet)) {
                                                Utils.ping(mContext, "You have already class at this time");
                                            } else if (inTimeSelected.after(inTimet) && inTimeSelected.after(outTimet)) {
                                                Utils.ping(mContext, "You have already class at this time");
                                            } else {
                                                addSessionDialogBinding.tueEndTimeTxt.setText(FinalTimeStr);
                                                addSessionDialogBinding.tueEndAddSessionBtn.setText("x");
                                                addSessionDialogBinding.tueEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                                                addSessionDialogBinding.tueEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                                                SimpleDateFormat sdf3 = new SimpleDateFormat("hh:mm aa", Locale.US);
                                                try {
                                                    Date inTime = sdf3.parse(addSessionDialogBinding.tueStartTimeTxt.getText().toString());
                                                    Date outTime = sdf3.parse(addSessionDialogBinding.tueEndTimeTxt.getText().toString());
                                                    if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                                        addSessionDialogBinding.tueEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                                        addSessionDialogBinding.tueEndLinear.setBackgroundResource(R.drawable.red_linear);
                                                        checkTime_tue = true;
                                                    } else {
                                                        checkTime_tue = false;
                                                        addSessionDialogBinding.tueEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                                        addSessionDialogBinding.tueEndLinear.setBackgroundResource(R.drawable.linear_shape);
                                                    }
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    addSessionDialogBinding.tueEndTimeTxt.setText(FinalTimeStr);
                                    addSessionDialogBinding.tueEndAddSessionBtn.setText("x");
                                    addSessionDialogBinding.tueEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                                    addSessionDialogBinding.tueEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                                    SimpleDateFormat sdf3 = new SimpleDateFormat("hh:mm aa", Locale.US);
                                    try {
                                        Date inTime = sdf3.parse(addSessionDialogBinding.tueStartTimeTxt.getText().toString());
                                        Date outTime = sdf3.parse(addSessionDialogBinding.tueEndTimeTxt.getText().toString());
                                        if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                            addSessionDialogBinding.tueEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                            addSessionDialogBinding.tueEndLinear.setBackgroundResource(R.drawable.red_linear);
                                            checkTime_tue = true;
                                        } else {
                                            checkTime_tue = false;
                                            addSessionDialogBinding.tueEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                            addSessionDialogBinding.tueEndLinear.setBackgroundResource(R.drawable.linear_shape);
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }else{
                        addSessionDialogBinding.tueEndTimeTxt.setText(FinalTimeStr);
                        addSessionDialogBinding.tueEndAddSessionBtn.setText("x");
                        addSessionDialogBinding.tueEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                        addSessionDialogBinding.tueEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                        SimpleDateFormat sdf3 = new SimpleDateFormat("hh:mm aa", Locale.US);
                        try {
                            Date inTime = sdf3.parse(addSessionDialogBinding.tueStartTimeTxt.getText().toString());
                            Date outTime = sdf3.parse(addSessionDialogBinding.tueEndTimeTxt.getText().toString());
                            if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                addSessionDialogBinding.tueEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                addSessionDialogBinding.tueEndLinear.setBackgroundResource(R.drawable.red_linear);
                                checkTime_tue = true;
                            } else {
                                checkTime_tue = false;
                                addSessionDialogBinding.tueEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                addSessionDialogBinding.tueEndLinear.setBackgroundResource(R.drawable.linear_shape);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "6":
                    if (finalsessionfullDetailModel.getData().size()>0) {
                        for (int i = 0; i < finalsessionfullDetailModel.getData().size(); i++) {
                            for (int j = 0; j < finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().size(); j++) {
                                if (finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionDate()
                                        .equalsIgnoreCase(addSessionDialogBinding.startDateTxt.getText().toString())) {
                                    String[] splittime = finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionTime().split("\\-");
                                    try {
                                        SimpleDateFormat sdft = new SimpleDateFormat("hh:mm aa", Locale.US);
                                        Date inTimet = sdft.parse(splittime[0]);
                                        Date outTimet = sdft.parse(splittime[1]);
                                        Date inTimeSelected = sdft.parse(FinalTimeStr);
                                        // Date outTimeSelected = sdf16.parse("10:00 PM");
                                        if (inTimeSelected.equals(inTimet) || inTimeSelected.equals(outTimet)) {
                                            Utils.ping(mContext, "You have already class at this time");
                                        } else if (inTimeSelected.after(inTimet) && inTimeSelected.before(outTimet)) {
                                            Utils.ping(mContext, "You have already class at this time");
                                        } else {
                                            addSessionDialogBinding.wedStartTimeTxt.setText(FinalTimeStr);
                                            addSessionDialogBinding.wedStartAddSessionBtn.setText("x");
                                            addSessionDialogBinding.wedStartAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                                            addSessionDialogBinding.wedStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                                            SimpleDateFormat sdf16 = new SimpleDateFormat("hh:mm aa", Locale.US);
                                            try {
                                                Date inTime = sdf16.parse(addSessionDialogBinding.wedStartTimeTxt.getText().toString());
                                                Date outTime = sdf16.parse(addSessionDialogBinding.wedEndTimeTxt.getText().toString());
                                                if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                                    addSessionDialogBinding.wedEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                                    addSessionDialogBinding.wedEndLinear.setBackgroundResource(R.drawable.red_linear);
                                                    checkTime_wed = true;
                                                } else {
                                                    checkTime_wed = false;
                                                    addSessionDialogBinding.wedEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                                    addSessionDialogBinding.wedEndLinear.setBackgroundResource(R.drawable.linear_shape);
                                                }
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    addSessionDialogBinding.wedStartTimeTxt.setText(FinalTimeStr);
                                    addSessionDialogBinding.wedStartAddSessionBtn.setText("x");
                                    addSessionDialogBinding.wedStartAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                                    addSessionDialogBinding.wedStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                                    SimpleDateFormat sdf16 = new SimpleDateFormat("hh:mm aa", Locale.US);
                                    try {
                                        Date inTime = sdf16.parse(addSessionDialogBinding.wedStartTimeTxt.getText().toString());
                                        Date outTime = sdf16.parse(addSessionDialogBinding.wedEndTimeTxt.getText().toString());
                                        if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                            addSessionDialogBinding.wedEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                            addSessionDialogBinding.wedEndLinear.setBackgroundResource(R.drawable.red_linear);
                                            checkTime_wed = true;
                                        } else {
                                            checkTime_wed = false;
                                            addSessionDialogBinding.wedEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                            addSessionDialogBinding.wedEndLinear.setBackgroundResource(R.drawable.linear_shape);
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        }
                    }else{
                        addSessionDialogBinding.wedStartTimeTxt.setText(FinalTimeStr);
                        addSessionDialogBinding.wedStartAddSessionBtn.setText("x");
                        addSessionDialogBinding.wedStartAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                        addSessionDialogBinding.wedStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                        SimpleDateFormat sdf16 = new SimpleDateFormat("hh:mm aa", Locale.US);
                        try {
                            Date inTime = sdf16.parse(addSessionDialogBinding.wedStartTimeTxt.getText().toString());
                            Date outTime = sdf16.parse(addSessionDialogBinding.wedEndTimeTxt.getText().toString());
                            if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                addSessionDialogBinding.wedEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                addSessionDialogBinding.wedEndLinear.setBackgroundResource(R.drawable.red_linear);
                                checkTime_wed = true;
                            } else {
                                checkTime_wed = false;
                                addSessionDialogBinding.wedEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                addSessionDialogBinding.wedEndLinear.setBackgroundResource(R.drawable.linear_shape);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "7":
                    if (finalsessionfullDetailModel.getData().size()>0) {
                        for (int i = 0; i < finalsessionfullDetailModel.getData().size(); i++) {
                            for (int j = 0; j < finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().size(); j++) {
                                if (finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionDate()
                                        .equalsIgnoreCase(addSessionDialogBinding.startDateTxt.getText().toString())) {
                                    String[] splittime = finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionTime().split("\\-");
                                    try {
                                        SimpleDateFormat sdft = new SimpleDateFormat("hh:mm aa", Locale.US);
                                        Date inTimet = sdft.parse(splittime[0]);
                                        Date outTimet = sdft.parse(splittime[1]);
                                        Date inTimeSelected = sdft.parse(FinalTimeStr);
                                        Date starttime = sdft.parse(addSessionDialogBinding.wedStartTimeTxt.getText().toString());
                                        // Date outTimeSelected = sdf16.parse("10:00 PM");
                                        if (starttime.after(outTimet)) {
                                            addSessionDialogBinding.wedEndTimeTxt.setText(FinalTimeStr);
                                            addSessionDialogBinding.wedEndAddSessionBtn.setText("x");
                                            addSessionDialogBinding.wedEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                                            addSessionDialogBinding.wedEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                                            SimpleDateFormat sdf3 = new SimpleDateFormat("hh:mm aa", Locale.US);
                                            try {
                                                Date inTime = sdf3.parse(addSessionDialogBinding.wedStartTimeTxt.getText().toString());
                                                Date outTime = sdf3.parse(addSessionDialogBinding.wedEndTimeTxt.getText().toString());
                                                if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                                    addSessionDialogBinding.wedEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                                    addSessionDialogBinding.wedEndLinear.setBackgroundResource(R.drawable.red_linear);
                                                    checkTime_wed = true;
                                                } else {
                                                    checkTime_wed = false;
                                                    addSessionDialogBinding.wedEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                                    addSessionDialogBinding.wedEndLinear.setBackgroundResource(R.drawable.linear_shape);
                                                }
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            if (inTimeSelected.equals(inTimet) || inTimeSelected.equals(outTimet)) {
                                                Utils.ping(mContext, "You have already class at this time");
                                            } else if (inTimeSelected.after(inTimet) && inTimeSelected.before(outTimet)) {
                                                Utils.ping(mContext, "You have already class at this time");
                                            } else if (inTimeSelected.after(inTimet) && inTimeSelected.after(outTimet)) {
                                                Utils.ping(mContext, "You have already class at this time");
                                            } else {
                                                addSessionDialogBinding.wedEndTimeTxt.setText(FinalTimeStr);
                                                addSessionDialogBinding.wedEndAddSessionBtn.setText("x");
                                                addSessionDialogBinding.wedEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                                                addSessionDialogBinding.wedEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                                                SimpleDateFormat sdf3 = new SimpleDateFormat("hh:mm aa", Locale.US);
                                                try {
                                                    Date inTime = sdf3.parse(addSessionDialogBinding.wedStartTimeTxt.getText().toString());
                                                    Date outTime = sdf3.parse(addSessionDialogBinding.wedEndTimeTxt.getText().toString());
                                                    if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                                        addSessionDialogBinding.wedEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                                        addSessionDialogBinding.wedEndLinear.setBackgroundResource(R.drawable.red_linear);
                                                        checkTime_wed = true;
                                                    } else {
                                                        checkTime_wed = false;
                                                        addSessionDialogBinding.wedEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                                        addSessionDialogBinding.wedEndLinear.setBackgroundResource(R.drawable.linear_shape);
                                                    }
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    addSessionDialogBinding.wedEndTimeTxt.setText(FinalTimeStr);
                                    addSessionDialogBinding.wedEndAddSessionBtn.setText("x");
                                    addSessionDialogBinding.wedEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                                    addSessionDialogBinding.wedEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                                    SimpleDateFormat sdf3 = new SimpleDateFormat("hh:mm aa", Locale.US);
                                    try {
                                        Date inTime = sdf3.parse(addSessionDialogBinding.wedStartTimeTxt.getText().toString());
                                        Date outTime = sdf3.parse(addSessionDialogBinding.wedEndTimeTxt.getText().toString());
                                        if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                            addSessionDialogBinding.wedEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                            addSessionDialogBinding.wedEndLinear.setBackgroundResource(R.drawable.red_linear);
                                            checkTime_wed = true;
                                        } else {
                                            checkTime_wed = false;
                                            addSessionDialogBinding.wedEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                            addSessionDialogBinding.wedEndLinear.setBackgroundResource(R.drawable.linear_shape);
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }else{
                        addSessionDialogBinding.wedEndTimeTxt.setText(FinalTimeStr);
                        addSessionDialogBinding.wedEndAddSessionBtn.setText("x");
                        addSessionDialogBinding.wedEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                        addSessionDialogBinding.wedEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                        SimpleDateFormat sdf3 = new SimpleDateFormat("hh:mm aa", Locale.US);
                        try {
                            Date inTime = sdf3.parse(addSessionDialogBinding.wedStartTimeTxt.getText().toString());
                            Date outTime = sdf3.parse(addSessionDialogBinding.wedEndTimeTxt.getText().toString());
                            if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                addSessionDialogBinding.wedEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                addSessionDialogBinding.wedEndLinear.setBackgroundResource(R.drawable.red_linear);
                                checkTime_wed = true;
                            } else {
                                checkTime_wed = false;
                                addSessionDialogBinding.wedEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                addSessionDialogBinding.wedEndLinear.setBackgroundResource(R.drawable.linear_shape);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "8":
                    if (finalsessionfullDetailModel.getData().size()>0) {
                        for (int i = 0; i < finalsessionfullDetailModel.getData().size(); i++) {
                            for (int j = 0; j < finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().size(); j++) {
                                if (finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionDate()
                                        .equalsIgnoreCase(addSessionDialogBinding.startDateTxt.getText().toString())) {
                                    String[] splittime = finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionTime().split("\\-");
                                    try {
                                        SimpleDateFormat sdft = new SimpleDateFormat("hh:mm aa", Locale.US);
                                        Date inTimet = sdft.parse(splittime[0]);
                                        Date outTimet = sdft.parse(splittime[1]);
                                        Date inTimeSelected = sdft.parse(FinalTimeStr);
                                        // Date outTimeSelected = sdf16.parse("10:00 PM");
                                        if (inTimeSelected.equals(inTimet) || inTimeSelected.equals(outTimet)) {
                                            Utils.ping(mContext, "You have already class at this time");
                                        } else if (inTimeSelected.after(inTimet) && inTimeSelected.before(outTimet)) {
                                            Utils.ping(mContext, "You have already class at this time");
                                        } else {
                                            addSessionDialogBinding.thuStartTimeTxt.setText(FinalTimeStr);
                                            addSessionDialogBinding.thuStartAddSessionBtn.setText("x");
                                            addSessionDialogBinding.thuStartAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                                            addSessionDialogBinding.thuStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                                            SimpleDateFormat sdf16 = new SimpleDateFormat("hh:mm aa", Locale.US);
                                            try {
                                                Date inTime = sdf16.parse(addSessionDialogBinding.thuStartTimeTxt.getText().toString());
                                                Date outTime = sdf16.parse(addSessionDialogBinding.thuEndTimeTxt.getText().toString());
                                                if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                                    addSessionDialogBinding.thuEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                                    addSessionDialogBinding.thuEndLinear.setBackgroundResource(R.drawable.red_linear);
                                                    checkTime_thu = true;
                                                } else {
                                                    checkTime_thu = false;
                                                    addSessionDialogBinding.thuEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                                    addSessionDialogBinding.thuEndLinear.setBackgroundResource(R.drawable.linear_shape);
                                                }
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    addSessionDialogBinding.thuStartTimeTxt.setText(FinalTimeStr);
                                    addSessionDialogBinding.thuStartAddSessionBtn.setText("x");
                                    addSessionDialogBinding.thuStartAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                                    addSessionDialogBinding.thuStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                                    SimpleDateFormat sdf16 = new SimpleDateFormat("hh:mm aa", Locale.US);
                                    try {
                                        Date inTime = sdf16.parse(addSessionDialogBinding.thuStartTimeTxt.getText().toString());
                                        Date outTime = sdf16.parse(addSessionDialogBinding.thuEndTimeTxt.getText().toString());
                                        if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                            addSessionDialogBinding.thuEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                            addSessionDialogBinding.thuEndLinear.setBackgroundResource(R.drawable.red_linear);
                                            checkTime_thu = true;
                                        } else {
                                            checkTime_thu = false;
                                            addSessionDialogBinding.thuEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                            addSessionDialogBinding.thuEndLinear.setBackgroundResource(R.drawable.linear_shape);
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        }
                    }else{
                        addSessionDialogBinding.thuStartTimeTxt.setText(FinalTimeStr);
                        addSessionDialogBinding.thuStartAddSessionBtn.setText("x");
                        addSessionDialogBinding.thuStartAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                        addSessionDialogBinding.thuStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                        SimpleDateFormat sdf16 = new SimpleDateFormat("hh:mm aa", Locale.US);
                        try {
                            Date inTime = sdf16.parse(addSessionDialogBinding.thuStartTimeTxt.getText().toString());
                            Date outTime = sdf16.parse(addSessionDialogBinding.thuEndTimeTxt.getText().toString());
                            if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                addSessionDialogBinding.thuEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                addSessionDialogBinding.thuEndLinear.setBackgroundResource(R.drawable.red_linear);
                                checkTime_thu = true;
                            } else {
                                checkTime_thu = false;
                                addSessionDialogBinding.thuEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                addSessionDialogBinding.thuEndLinear.setBackgroundResource(R.drawable.linear_shape);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "9":
                    if (finalsessionfullDetailModel.getData().size()>0) {
                        for (int i = 0; i < finalsessionfullDetailModel.getData().size(); i++) {
                            for (int j = 0; j < finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().size(); j++) {
                                if (finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionDate()
                                        .equalsIgnoreCase(addSessionDialogBinding.startDateTxt.getText().toString())) {
                                    String[] splittime = finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionTime().split("\\-");
                                    try {
                                        SimpleDateFormat sdft = new SimpleDateFormat("hh:mm aa", Locale.US);
                                        Date inTimet = sdft.parse(splittime[0]);
                                        Date outTimet = sdft.parse(splittime[1]);
                                        Date inTimeSelected = sdft.parse(FinalTimeStr);
                                        Date starttime = sdft.parse(addSessionDialogBinding.thuStartTimeTxt.getText().toString());
                                        // Date outTimeSelected = sdf16.parse("10:00 PM");
                                        if (starttime.after(outTimet)) {
                                            addSessionDialogBinding.thuEndTimeTxt.setText(FinalTimeStr);
                                            addSessionDialogBinding.thuEndAddSessionBtn.setText("x");
                                            addSessionDialogBinding.thuEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                                            addSessionDialogBinding.thuEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                                            SimpleDateFormat sdf3 = new SimpleDateFormat("hh:mm aa", Locale.US);
                                            try {
                                                Date inTime = sdf3.parse(addSessionDialogBinding.thuStartTimeTxt.getText().toString());
                                                Date outTime = sdf3.parse(addSessionDialogBinding.thuEndTimeTxt.getText().toString());
                                                if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                                    addSessionDialogBinding.thuEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                                    addSessionDialogBinding.thuEndLinear.setBackgroundResource(R.drawable.red_linear);
                                                    checkTime_thu = true;
                                                } else {
                                                    checkTime_thu = false;
                                                    addSessionDialogBinding.thuEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                                    addSessionDialogBinding.thuEndLinear.setBackgroundResource(R.drawable.linear_shape);
                                                }
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            if (inTimeSelected.equals(inTimet) || inTimeSelected.equals(outTimet)) {
                                                Utils.ping(mContext, "You have already class at this time");
                                            } else if (inTimeSelected.after(inTimet) && inTimeSelected.before(outTimet)) {
                                                Utils.ping(mContext, "You have already class at this time");
                                            } else if (inTimeSelected.after(inTimet) && inTimeSelected.after(outTimet)) {
                                                Utils.ping(mContext, "You have already class at this time");
                                            } else {
                                                addSessionDialogBinding.thuEndTimeTxt.setText(FinalTimeStr);
                                                addSessionDialogBinding.thuEndAddSessionBtn.setText("x");
                                                addSessionDialogBinding.thuEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                                                addSessionDialogBinding.thuEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                                                SimpleDateFormat sdf3 = new SimpleDateFormat("hh:mm aa", Locale.US);
                                                try {
                                                    Date inTime = sdf3.parse(addSessionDialogBinding.thuStartTimeTxt.getText().toString());
                                                    Date outTime = sdf3.parse(addSessionDialogBinding.thuEndTimeTxt.getText().toString());
                                                    if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                                        addSessionDialogBinding.thuEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                                        addSessionDialogBinding.thuEndLinear.setBackgroundResource(R.drawable.red_linear);
                                                        checkTime_thu = true;
                                                    } else {
                                                        checkTime_thu = false;
                                                        addSessionDialogBinding.thuEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                                        addSessionDialogBinding.thuEndLinear.setBackgroundResource(R.drawable.linear_shape);
                                                    }
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    addSessionDialogBinding.thuEndTimeTxt.setText(FinalTimeStr);
                                    addSessionDialogBinding.thuEndAddSessionBtn.setText("x");
                                    addSessionDialogBinding.thuEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                                    addSessionDialogBinding.thuEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                                    SimpleDateFormat sdf3 = new SimpleDateFormat("hh:mm aa", Locale.US);
                                    try {
                                        Date inTime = sdf3.parse(addSessionDialogBinding.thuStartTimeTxt.getText().toString());
                                        Date outTime = sdf3.parse(addSessionDialogBinding.thuEndTimeTxt.getText().toString());
                                        if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                            addSessionDialogBinding.thuEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                            addSessionDialogBinding.thuEndLinear.setBackgroundResource(R.drawable.red_linear);
                                            checkTime_thu = true;
                                        } else {
                                            checkTime_thu = false;
                                            addSessionDialogBinding.thuEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                            addSessionDialogBinding.thuEndLinear.setBackgroundResource(R.drawable.linear_shape);
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }else{
                        addSessionDialogBinding.thuEndTimeTxt.setText(FinalTimeStr);
                        addSessionDialogBinding.thuEndAddSessionBtn.setText("x");
                        addSessionDialogBinding.thuEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                        addSessionDialogBinding.thuEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                        SimpleDateFormat sdf3 = new SimpleDateFormat("hh:mm aa", Locale.US);
                        try {
                            Date inTime = sdf3.parse(addSessionDialogBinding.thuStartTimeTxt.getText().toString());
                            Date outTime = sdf3.parse(addSessionDialogBinding.thuEndTimeTxt.getText().toString());
                            if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                addSessionDialogBinding.thuEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                addSessionDialogBinding.thuEndLinear.setBackgroundResource(R.drawable.red_linear);
                                checkTime_thu = true;
                            } else {
                                checkTime_thu = false;
                                addSessionDialogBinding.thuEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                addSessionDialogBinding.thuEndLinear.setBackgroundResource(R.drawable.linear_shape);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "10":
                    if (finalsessionfullDetailModel.getData().size()>0) {
                        for (int i = 0; i < finalsessionfullDetailModel.getData().size(); i++) {
                            for (int j = 0; j < finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().size(); j++) {
                                if (finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionDate()
                                        .equalsIgnoreCase(addSessionDialogBinding.startDateTxt.getText().toString())) {
                                    String[] splittime = finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionTime().split("\\-");
                                    try {
                                        SimpleDateFormat sdft = new SimpleDateFormat("hh:mm aa", Locale.US);
                                        Date inTimet = sdft.parse(splittime[0]);
                                        Date outTimet = sdft.parse(splittime[1]);
                                        Date inTimeSelected = sdft.parse(FinalTimeStr);
                                        // Date outTimeSelected = sdf16.parse("10:00 PM");
                                        if (inTimeSelected.equals(inTimet) || inTimeSelected.equals(outTimet)) {
                                            Utils.ping(mContext, "You have already class at this time");
                                        } else if (inTimeSelected.after(inTimet) && inTimeSelected.before(outTimet)) {
                                            Utils.ping(mContext, "You have already class at this time");
                                        } else {
                                            addSessionDialogBinding.friStartTimeTxt.setText(FinalTimeStr);
                                            addSessionDialogBinding.friStartAddSessionBtn.setText("x");
                                            addSessionDialogBinding.friStartAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                                            addSessionDialogBinding.friStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                                            SimpleDateFormat sdf16 = new SimpleDateFormat("hh:mm aa", Locale.US);
                                            try {
                                                Date inTime = sdf16.parse(addSessionDialogBinding.friStartTimeTxt.getText().toString());
                                                Date outTime = sdf16.parse(addSessionDialogBinding.friEndTimeTxt.getText().toString());
                                                if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                                    addSessionDialogBinding.friEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                                    addSessionDialogBinding.friEndLinear.setBackgroundResource(R.drawable.red_linear);
                                                    checkTime_fri = true;
                                                } else {
                                                    checkTime_fri = false;
                                                    addSessionDialogBinding.friEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                                    addSessionDialogBinding.friEndLinear.setBackgroundResource(R.drawable.linear_shape);
                                                }
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    addSessionDialogBinding.friStartTimeTxt.setText(FinalTimeStr);
                                    addSessionDialogBinding.friStartAddSessionBtn.setText("x");
                                    addSessionDialogBinding.friStartAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                                    addSessionDialogBinding.friStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                                    SimpleDateFormat sdf16 = new SimpleDateFormat("hh:mm aa", Locale.US);
                                    try {
                                        Date inTime = sdf16.parse(addSessionDialogBinding.friStartTimeTxt.getText().toString());
                                        Date outTime = sdf16.parse(addSessionDialogBinding.friEndTimeTxt.getText().toString());
                                        if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                            addSessionDialogBinding.friEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                            addSessionDialogBinding.friEndLinear.setBackgroundResource(R.drawable.red_linear);
                                            checkTime_fri = true;
                                        } else {
                                            checkTime_fri = false;
                                            addSessionDialogBinding.friEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                            addSessionDialogBinding.friEndLinear.setBackgroundResource(R.drawable.linear_shape);
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        }
                    }else{
                        addSessionDialogBinding.friStartTimeTxt.setText(FinalTimeStr);
                        addSessionDialogBinding.friStartAddSessionBtn.setText("x");
                        addSessionDialogBinding.friStartAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                        addSessionDialogBinding.friStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                        SimpleDateFormat sdf16 = new SimpleDateFormat("hh:mm aa", Locale.US);
                        try {
                            Date inTime = sdf16.parse(addSessionDialogBinding.friStartTimeTxt.getText().toString());
                            Date outTime = sdf16.parse(addSessionDialogBinding.friEndTimeTxt.getText().toString());
                            if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                addSessionDialogBinding.friEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                addSessionDialogBinding.friEndLinear.setBackgroundResource(R.drawable.red_linear);
                                checkTime_fri = true;
                            } else {
                                checkTime_fri = false;
                                addSessionDialogBinding.friEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                addSessionDialogBinding.friEndLinear.setBackgroundResource(R.drawable.linear_shape);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "11":
                    if (finalsessionfullDetailModel.getData().size()>0) {
                        for (int i = 0; i < finalsessionfullDetailModel.getData().size(); i++) {
                            for (int j = 0; j < finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().size(); j++) {
                                if (finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionDate()
                                        .equalsIgnoreCase(addSessionDialogBinding.startDateTxt.getText().toString())) {
                                    String[] splittime = finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionTime().split("\\-");
                                    try {
                                        SimpleDateFormat sdft = new SimpleDateFormat("hh:mm aa", Locale.US);
                                        Date inTimet = sdft.parse(splittime[0]);
                                        Date outTimet = sdft.parse(splittime[1]);
                                        Date inTimeSelected = sdft.parse(FinalTimeStr);
                                        Date starttime = sdft.parse(addSessionDialogBinding.friStartTimeTxt.getText().toString());
                                        // Date outTimeSelected = sdf16.parse("10:00 PM");
                                        if (starttime.after(outTimet)) {
                                            addSessionDialogBinding.friEndTimeTxt.setText(FinalTimeStr);
                                            addSessionDialogBinding.friEndAddSessionBtn.setText("x");
                                            addSessionDialogBinding.friEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                                            addSessionDialogBinding.friEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                                            SimpleDateFormat sdf3 = new SimpleDateFormat("hh:mm aa", Locale.US);
                                            try {
                                                Date inTime = sdf3.parse(addSessionDialogBinding.friStartTimeTxt.getText().toString());
                                                Date outTime = sdf3.parse(addSessionDialogBinding.friEndTimeTxt.getText().toString());
                                                if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                                    addSessionDialogBinding.friEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                                    addSessionDialogBinding.friEndLinear.setBackgroundResource(R.drawable.red_linear);
                                                    checkTime_fri = true;
                                                } else {
                                                    checkTime_fri = false;
                                                    addSessionDialogBinding.friEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                                    addSessionDialogBinding.friEndLinear.setBackgroundResource(R.drawable.linear_shape);
                                                }
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            if (inTimeSelected.equals(inTimet) || inTimeSelected.equals(outTimet)) {
                                                Utils.ping(mContext, "You have already class at this time");
                                            } else if (inTimeSelected.after(inTimet) && inTimeSelected.before(outTimet)) {
                                                Utils.ping(mContext, "You have already class at this time");
                                            } else if (inTimeSelected.after(inTimet) && inTimeSelected.after(outTimet)) {
                                                Utils.ping(mContext, "You have already class at this time");
                                            } else {
                                                addSessionDialogBinding.friEndTimeTxt.setText(FinalTimeStr);
                                                addSessionDialogBinding.friEndAddSessionBtn.setText("x");
                                                addSessionDialogBinding.friEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                                                addSessionDialogBinding.friEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                                                SimpleDateFormat sdf3 = new SimpleDateFormat("hh:mm aa", Locale.US);
                                                try {
                                                    Date inTime = sdf3.parse(addSessionDialogBinding.friStartTimeTxt.getText().toString());
                                                    Date outTime = sdf3.parse(addSessionDialogBinding.friEndTimeTxt.getText().toString());
                                                    if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                                        addSessionDialogBinding.friEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                                        addSessionDialogBinding.friEndLinear.setBackgroundResource(R.drawable.red_linear);
                                                        checkTime_fri = true;
                                                    } else {
                                                        checkTime_fri = false;
                                                        addSessionDialogBinding.friEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                                        addSessionDialogBinding.friEndLinear.setBackgroundResource(R.drawable.linear_shape);
                                                    }
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    addSessionDialogBinding.friEndTimeTxt.setText(FinalTimeStr);
                                    addSessionDialogBinding.friEndAddSessionBtn.setText("x");
                                    addSessionDialogBinding.friEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                                    addSessionDialogBinding.friEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                                    SimpleDateFormat sdf3 = new SimpleDateFormat("hh:mm aa", Locale.US);
                                    try {
                                        Date inTime = sdf3.parse(addSessionDialogBinding.friStartTimeTxt.getText().toString());
                                        Date outTime = sdf3.parse(addSessionDialogBinding.friEndTimeTxt.getText().toString());
                                        if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                            addSessionDialogBinding.friEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                            addSessionDialogBinding.friEndLinear.setBackgroundResource(R.drawable.red_linear);
                                            checkTime_fri = true;
                                        } else {
                                            checkTime_fri = false;
                                            addSessionDialogBinding.friEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                            addSessionDialogBinding.friEndLinear.setBackgroundResource(R.drawable.linear_shape);
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }else{
                        addSessionDialogBinding.friEndTimeTxt.setText(FinalTimeStr);
                        addSessionDialogBinding.friEndAddSessionBtn.setText("x");
                        addSessionDialogBinding.friEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                        addSessionDialogBinding.friEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                        SimpleDateFormat sdf3 = new SimpleDateFormat("hh:mm aa", Locale.US);
                        try {
                            Date inTime = sdf3.parse(addSessionDialogBinding.friStartTimeTxt.getText().toString());
                            Date outTime = sdf3.parse(addSessionDialogBinding.friEndTimeTxt.getText().toString());
                            if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                addSessionDialogBinding.friEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                addSessionDialogBinding.friEndLinear.setBackgroundResource(R.drawable.red_linear);
                                checkTime_fri = true;
                            } else {
                                checkTime_fri = false;
                                addSessionDialogBinding.friEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                addSessionDialogBinding.friEndLinear.setBackgroundResource(R.drawable.linear_shape);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "12":
                    if (finalsessionfullDetailModel.getData().size()>0) {
                        for (int i = 0; i < finalsessionfullDetailModel.getData().size(); i++) {
                            for (int j = 0; j < finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().size(); j++) {
                                if (finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionDate()
                                        .equalsIgnoreCase(addSessionDialogBinding.startDateTxt.getText().toString())) {
                                    String[] splittime = finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionTime().split("\\-");
                                    try {
                                        SimpleDateFormat sdft = new SimpleDateFormat("hh:mm aa", Locale.US);
                                        Date inTimet = sdft.parse(splittime[0]);
                                        Date outTimet = sdft.parse(splittime[1]);
                                        Date inTimeSelected = sdft.parse(FinalTimeStr);
                                        // Date outTimeSelected = sdf16.parse("10:00 PM");
                                        if (inTimeSelected.equals(inTimet) || inTimeSelected.equals(outTimet)) {
                                            Utils.ping(mContext, "You have already class at this time");
                                        } else if (inTimeSelected.after(inTimet) && inTimeSelected.before(outTimet)) {
                                            Utils.ping(mContext, "You have already class at this time");
                                        } else {
                                            addSessionDialogBinding.satStartTimeTxt.setText(FinalTimeStr);
                                            addSessionDialogBinding.satStartAddSessionBtn.setText("x");
                                            addSessionDialogBinding.satStartAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                                            addSessionDialogBinding.satStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                                            SimpleDateFormat sdf16 = new SimpleDateFormat("hh:mm aa", Locale.US);
                                            try {
                                                Date inTime = sdf16.parse(addSessionDialogBinding.satStartTimeTxt.getText().toString());
                                                Date outTime = sdf16.parse(addSessionDialogBinding.satEndTimeTxt.getText().toString());
                                                if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                                    addSessionDialogBinding.satEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                                    addSessionDialogBinding.satEndLinear.setBackgroundResource(R.drawable.red_linear);
                                                    checkTime_sat = true;
                                                } else {
                                                    checkTime_sat = false;
                                                    addSessionDialogBinding.satEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                                    addSessionDialogBinding.satEndLinear.setBackgroundResource(R.drawable.linear_shape);
                                                }
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    addSessionDialogBinding.satStartTimeTxt.setText(FinalTimeStr);
                                    addSessionDialogBinding.satStartAddSessionBtn.setText("x");
                                    addSessionDialogBinding.satStartAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                                    addSessionDialogBinding.satStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                                    SimpleDateFormat sdf16 = new SimpleDateFormat("hh:mm aa", Locale.US);
                                    try {
                                        Date inTime = sdf16.parse(addSessionDialogBinding.satStartTimeTxt.getText().toString());
                                        Date outTime = sdf16.parse(addSessionDialogBinding.satEndTimeTxt.getText().toString());
                                        if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                            addSessionDialogBinding.satEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                            addSessionDialogBinding.satEndLinear.setBackgroundResource(R.drawable.red_linear);
                                            checkTime_sat = true;
                                        } else {
                                            checkTime_sat = false;
                                            addSessionDialogBinding.satEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                            addSessionDialogBinding.satEndLinear.setBackgroundResource(R.drawable.linear_shape);
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        }
                    }else{
                        addSessionDialogBinding.satStartTimeTxt.setText(FinalTimeStr);
                        addSessionDialogBinding.satStartAddSessionBtn.setText("x");
                        addSessionDialogBinding.satStartAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                        addSessionDialogBinding.satStartAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                        SimpleDateFormat sdf16 = new SimpleDateFormat("hh:mm aa", Locale.US);
                        try {
                            Date inTime = sdf16.parse(addSessionDialogBinding.satStartTimeTxt.getText().toString());
                            Date outTime = sdf16.parse(addSessionDialogBinding.satEndTimeTxt.getText().toString());
                            if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                addSessionDialogBinding.satEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                addSessionDialogBinding.satEndLinear.setBackgroundResource(R.drawable.red_linear);
                                checkTime_sat = true;
                            } else {
                                checkTime_sat = false;
                                addSessionDialogBinding.satEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                addSessionDialogBinding.satEndLinear.setBackgroundResource(R.drawable.linear_shape);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "13":
                    if (finalsessionfullDetailModel.getData().size()>0) {
                        for (int i = 0; i < finalsessionfullDetailModel.getData().size(); i++) {
                            for (int j = 0; j < finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().size(); j++) {
                                if (finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionDate()
                                        .equalsIgnoreCase(addSessionDialogBinding.startDateTxt.getText().toString())) {
                                    String[] splittime = finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionTime().split("\\-");
                                    try {
                                        SimpleDateFormat sdft = new SimpleDateFormat("hh:mm aa", Locale.US);
                                        Date inTimet = sdft.parse(splittime[0]);
                                        Date outTimet = sdft.parse(splittime[1]);
                                        Date inTimeSelected = sdft.parse(FinalTimeStr);
                                        Date starttime = sdft.parse(addSessionDialogBinding.satStartTimeTxt.getText().toString());
                                        // Date outTimeSelected = sdf16.parse("10:00 PM");
                                        if (starttime.after(outTimet)) {
                                            addSessionDialogBinding.satEndTimeTxt.setText(FinalTimeStr);
                                            addSessionDialogBinding.satEndAddSessionBtn.setText("x");
                                            addSessionDialogBinding.satEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                                            addSessionDialogBinding.satEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                                            SimpleDateFormat sdf3 = new SimpleDateFormat("hh:mm aa", Locale.US);
                                            try {
                                                Date inTime = sdf3.parse(addSessionDialogBinding.satStartTimeTxt.getText().toString());
                                                Date outTime = sdf3.parse(addSessionDialogBinding.satEndTimeTxt.getText().toString());
                                                if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                                    addSessionDialogBinding.satEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                                    addSessionDialogBinding.satEndLinear.setBackgroundResource(R.drawable.red_linear);
                                                    checkTime_sat = true;
                                                } else {
                                                    checkTime_sat = false;
                                                    addSessionDialogBinding.satEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                                    addSessionDialogBinding.satEndLinear.setBackgroundResource(R.drawable.linear_shape);
                                                }
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            if (inTimeSelected.equals(inTimet) || inTimeSelected.equals(outTimet)) {
                                                Utils.ping(mContext, "You have already class at this time");
                                            } else if (inTimeSelected.after(inTimet) && inTimeSelected.before(outTimet)) {
                                                Utils.ping(mContext, "You have already class at this time");
                                            } else if (inTimeSelected.after(inTimet) && inTimeSelected.after(outTimet)) {
                                                Utils.ping(mContext, "You have already class at this time");
                                            } else {
                                                addSessionDialogBinding.satEndTimeTxt.setText(FinalTimeStr);
                                                addSessionDialogBinding.satEndAddSessionBtn.setText("x");
                                                addSessionDialogBinding.satEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                                                addSessionDialogBinding.satEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                                                SimpleDateFormat sdf3 = new SimpleDateFormat("hh:mm aa", Locale.US);
                                                try {
                                                    Date inTime = sdf3.parse(addSessionDialogBinding.satStartTimeTxt.getText().toString());
                                                    Date outTime = sdf3.parse(addSessionDialogBinding.satEndTimeTxt.getText().toString());
                                                    if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                                        addSessionDialogBinding.satEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                                        addSessionDialogBinding.satEndLinear.setBackgroundResource(R.drawable.red_linear);
                                                        checkTime_sat = true;
                                                    } else {
                                                        checkTime_sat = false;
                                                        addSessionDialogBinding.satEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                                        addSessionDialogBinding.satEndLinear.setBackgroundResource(R.drawable.linear_shape);
                                                    }
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    addSessionDialogBinding.satEndTimeTxt.setText(FinalTimeStr);
                                    addSessionDialogBinding.satEndAddSessionBtn.setText("x");
                                    addSessionDialogBinding.satEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                                    addSessionDialogBinding.satEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                                    SimpleDateFormat sdf3 = new SimpleDateFormat("hh:mm aa", Locale.US);
                                    try {
                                        Date inTime = sdf3.parse(addSessionDialogBinding.satStartTimeTxt.getText().toString());
                                        Date outTime = sdf3.parse(addSessionDialogBinding.satEndTimeTxt.getText().toString());
                                        if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                            addSessionDialogBinding.satEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                            addSessionDialogBinding.satEndLinear.setBackgroundResource(R.drawable.red_linear);
                                            checkTime_sat = true;
                                        } else {
                                            checkTime_sat = false;
                                            addSessionDialogBinding.satEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                            addSessionDialogBinding.satEndLinear.setBackgroundResource(R.drawable.linear_shape);
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }else {
                        addSessionDialogBinding.satEndTimeTxt.setText(FinalTimeStr);
                        addSessionDialogBinding.satEndAddSessionBtn.setText("x");
                        addSessionDialogBinding.satEndAddSessionBtn.setTextColor(getResources().getColor(R.color.search_boder));
                        addSessionDialogBinding.satEndAddSessionBtn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                        SimpleDateFormat sdf3 = new SimpleDateFormat("hh:mm aa", Locale.US);
                        try {
                            Date inTime = sdf3.parse(addSessionDialogBinding.satStartTimeTxt.getText().toString());
                            Date outTime = sdf3.parse(addSessionDialogBinding.satEndTimeTxt.getText().toString());
                            if (outTime.before(inTime)) { //Same way you can check with after() method also.
                                addSessionDialogBinding.satEndTimeTxt.setTextColor(getResources().getColor(R.color.search_boder));
                                addSessionDialogBinding.satEndLinear.setBackgroundResource(R.drawable.red_linear);
                                checkTime_sat = true;
                            } else {
                                checkTime_sat = false;
                                addSessionDialogBinding.satEndTimeTxt.setTextColor(getResources().getColor(R.color.text_color));
                                addSessionDialogBinding.satEndLinear.setBackgroundResource(R.drawable.linear_shape);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
            }
        }

    }
}
