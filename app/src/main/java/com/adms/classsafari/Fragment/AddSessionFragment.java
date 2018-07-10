package com.adms.classsafari.Fragment;

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

import com.adms.classsafari.Activites.DashBoardActivity;
import com.adms.classsafari.Adapter.AddressListAdapter;
import com.adms.classsafari.Adapter.AlertListAdapter;
import com.adms.classsafari.AppConstant.ApiHandler;
import com.adms.classsafari.AppConstant.Utils;
import com.adms.classsafari.Interface.onViewClick;
import com.adms.classsafari.Model.Session.SessionDetailModel;
import com.adms.classsafari.R;
import com.adms.classsafari.databinding.DisplayTimeGridBinding;
import com.adms.classsafari.databinding.FragmentAddSessionBinding;

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

import retrofit.RetrofitError;
import retrofit.client.Response;


public class AddSessionFragment extends Fragment implements com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {
    public static AddSessionFragment fragment;
    static boolean checkTime_sun = false, checkTime_mon = false,
            checkTime_tue = false, checkTime_wed = false, checkTime_thu = false, checkTime_fri = false, checkTime_sat = false;
    //    AddSessionDialogBinding sessionDialogBinding;
    static TextView sun_start_time_txt, sun_end_time_txt, mon_start_time_txt, mon_end_time_txt, tue_start_time_txt,
            tue_end_time_txt, wed_start_time_txt, wed_end_time_txt, thu_start_time_txt, thu_end_time_txt, fri_end_time_txt,
            fri_start_time_txt, sat_end_time_txt, sat_start_time_txt, confirm_title_txt;
    static String Tag;
    static Button sun_start_add_session_btn, sun_end_add_session_btn, mon_start_add_session_btn, mon_end_add_session_btn,
            tue_start_add_session_btn, tue_end_add_session_btn, wed_start_add_session_btn, wed_end_add_session_btn,
            thu_start_add_session_btn, thu_end_add_session_btn, fri_start_add_session_btn, fri_end_add_session_btn,
            sat_start_add_session_btn, sat_end_add_session_btn;
    static RadioGroup rg;
    static RadioButton recurringrb, singlerb;
    static LinearLayout sun_start_linear, mon_start_linear, tue_start_linear, wed_start_linear, thu_start_linear, fri_start_linear, sat_start_linear,
            sun_end_linear, mon_end_linear, tue_end_linear, wed_end_linear, thu_end_linear, fri_end_linear, sat_end_linear;
    static ArrayList<String> days;
    private static String dateFinal;
    private static String minuteFinal, hourFinal, FinalTimeStr;
    private static boolean isFromDate = false;
    public Dialog popularDialog;
    //Use for Alert Dialog
    ArrayList<String> timegapArray;
    AlertListAdapter alertListAdapter;
    //Use for AddSession Time Dialog
    TextView start_date_txt, end_date_txt;
    RecyclerView day_name_rcView;
    Button done_btn, cancel_btn;
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
    String line, citytxt = "";
    String[] RowData = new String[0];
    ArrayList<String> lineArray;
    DisplayTimeGridBinding displayTimeGridBinding;
    Dialog addressDialog;
    ArrayList<String> SelectedAddressList;
    AddressListAdapter addressListAdapter;
    int SessionHour = 0;
    Integer SessionMinit = 0;
    String SessionDuration;
    String hours, minit;
    ArrayList<Integer> totalHours;
    ArrayList<Integer> totalMinit;
    int avgHoursvalue, avgMinitvalue;
    boolean checkfalse = false;
    private FragmentAddSessionBinding addSessionBinding;
    private View rootView;
    private Context mContext;
    private com.wdullaer.materialdatetimepicker.date.DatePickerDialog datePickerDialog;

    public AddSessionFragment() {
    }

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


        sun_start_linear.setEnabled(false);
        sun_end_linear.setEnabled(false);
        sun_start_linear.setAlpha(0.2f);
        sun_end_linear.setAlpha(0.2f);
        sun_start_add_session_btn.setEnabled(false);
        sun_end_add_session_btn.setEnabled(false);

        mon_start_linear.setEnabled(false);
        mon_end_linear.setEnabled(false);
        mon_start_linear.setAlpha(0.2f);
        mon_end_linear.setAlpha(0.2f);
        mon_start_add_session_btn.setEnabled(false);
        mon_end_add_session_btn.setEnabled(false);

        tue_start_linear.setEnabled(false);
        tue_end_linear.setEnabled(false);
        tue_start_linear.setAlpha(0.2f);
        tue_end_linear.setAlpha(0.2f);
        tue_start_add_session_btn.setEnabled(false);
        tue_end_add_session_btn.setEnabled(false);

        wed_start_linear.setEnabled(false);
        wed_end_linear.setEnabled(false);
        wed_start_linear.setAlpha(0.2f);
        wed_end_linear.setAlpha(0.2f);
        wed_start_add_session_btn.setEnabled(false);
        wed_end_add_session_btn.setEnabled(false);

        thu_start_linear.setEnabled(false);
        thu_end_linear.setEnabled(false);
        thu_start_linear.setAlpha(0.2f);
        thu_end_linear.setAlpha(0.2f);
        thu_start_add_session_btn.setEnabled(false);
        thu_end_add_session_btn.setEnabled(false);

        fri_start_linear.setEnabled(false);
        fri_end_linear.setEnabled(false);
        fri_start_linear.setAlpha(0.2f);
        fri_end_linear.setAlpha(0.2f);
        fri_start_add_session_btn.setEnabled(false);
        fri_end_add_session_btn.setEnabled(false);

        sat_start_linear.setEnabled(false);
        sat_end_linear.setEnabled(false);
        sat_start_linear.setAlpha(0.2f);
        sat_end_linear.setAlpha(0.2f);
        sat_start_add_session_btn.setEnabled(false);
        sat_end_add_session_btn.setEnabled(false);
        for (int i = 0; i < days.size(); i++) {
            switch (days.get(i)) {
                case "Sun":
                    sun_start_linear.setEnabled(true);
                    sun_end_linear.setEnabled(true);
                    sun_start_linear.setAlpha(1);
                    sun_end_linear.setAlpha(1);
                    sun_start_add_session_btn.setEnabled(true);
                    sun_end_add_session_btn.setEnabled(true);
                    break;
                case "Mon":
                    mon_start_linear.setEnabled(true);
                    mon_end_linear.setEnabled(true);
                    mon_start_linear.setAlpha(1);
                    mon_end_linear.setAlpha(1);
                    mon_start_add_session_btn.setEnabled(true);
                    mon_end_add_session_btn.setEnabled(true);
                    break;
                case "Tue":
                    tue_start_linear.setEnabled(true);
                    tue_end_linear.setEnabled(true);
                    tue_start_linear.setAlpha(1);
                    tue_end_linear.setAlpha(1);
                    tue_start_add_session_btn.setEnabled(true);
                    tue_end_add_session_btn.setEnabled(true);
                    break;
                case "Wed":
                    wed_start_linear.setEnabled(true);
                    wed_end_linear.setEnabled(true);
                    wed_start_linear.setAlpha(1);
                    wed_end_linear.setAlpha(1);
                    wed_start_add_session_btn.setEnabled(true);
                    wed_end_add_session_btn.setEnabled(true);
                    break;
                case "Thu":
                    thu_start_linear.setEnabled(true);
                    thu_end_linear.setEnabled(true);
                    thu_start_linear.setAlpha(1);
                    thu_end_linear.setAlpha(1);
                    thu_start_add_session_btn.setEnabled(true);
                    thu_end_add_session_btn.setEnabled(true);
                    break;
                case "Fri":
                    fri_start_linear.setEnabled(true);
                    fri_end_linear.setEnabled(true);
                    fri_start_linear.setAlpha(1);
                    fri_end_linear.setAlpha(1);
                    fri_start_add_session_btn.setEnabled(true);
                    fri_end_add_session_btn.setEnabled(true);
                    break;
                case "Sat":
                    sat_start_linear.setEnabled(true);
                    sat_end_linear.setEnabled(true);
                    sat_start_linear.setAlpha(1);
                    sat_end_linear.setAlpha(1);
                    sat_start_add_session_btn.setEnabled(true);
                    sat_end_add_session_btn.setEnabled(true);
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
        callBoardApi();
        callstandardApi();
        callStreamApi();
        callLessionApi();
        callRegionApi();
        fillCity();
        callAddressApi();
        //setTypeface();
        initViews();
        setListners();

    }

    public void setTypeface() {
//        Typeface custom_font = Typeface.createFromAsset(mContext.getAssets(), "font/TitilliumWeb-Regular.ttf");
//
//        addSessionBinding.AcademicRb.setTypeface(custom_font);
//        addSessionBinding.playRb.setTypeface(custom_font);
//        addSessionBinding.recurringRb.setTypeface(custom_font);
//        addSessionBinding.singleRb.setTypeface(custom_font);
//        addSessionBinding.sessionNameEdt.setTypeface(custom_font);
//        addSessionBinding.boardNameEdt.setTypeface(custom_font);
//        addSessionBinding.standardEdt.setTypeface(custom_font);
//        addSessionBinding.streamEdt.setTypeface(custom_font);
//        addSessionBinding.subjectEdt.setTypeface(custom_font);
//        addSessionBinding.descriptionEdt.setTypeface(custom_font);
//        addSessionBinding.addressEdt.setTypeface(custom_font);
//        addSessionBinding.areaEdt.setTypeface(custom_font);
//        addSessionBinding.cityEdt.setTypeface(custom_font);
//        addSessionBinding.stateEdt.setTypeface(custom_font);
//        addSessionBinding.zipcodeEdt.setTypeface(custom_font);
//        addSessionBinding.addressEdt1.setTypeface(custom_font);
//
//        addSessionBinding.mondayBtn.setTypeface(custom_font);
//        addSessionBinding.tuesdayBtn.setTypeface(custom_font);
//        addSessionBinding.wednesdayBtn.setTypeface(custom_font);
//        addSessionBinding.thursdayBtn.setTypeface(custom_font);
//        addSessionBinding.fridayBtn.setTypeface(custom_font);
//        addSessionBinding.saturdayBtn.setTypeface(custom_font);
//        addSessionBinding.sundayBtn.setTypeface(custom_font);
//        addSessionBinding.startDateTxt.setTypeface(custom_font);
//        addSessionBinding.endDateTxt.setTypeface(custom_font);
//
//        addSessionBinding.monTimeTxt.setTypeface(custom_font);
//        addSessionBinding.thurTimeTxt.setTypeface(custom_font);
//        addSessionBinding.wedTimeTxt.setTypeface(custom_font);
//        addSessionBinding.thurTimeTxt.setTypeface(custom_font);
//        addSessionBinding.friTimeTxt.setTypeface(custom_font);
//        addSessionBinding.satTimeTxt.setTypeface(custom_font);
//        addSessionBinding.sunTimeTxt.setTypeface(custom_font);
//
//        addSessionBinding.monHoursTxt.setTypeface(custom_font);
//        addSessionBinding.tuesHoursTxt.setTypeface(custom_font);
//        addSessionBinding.wedHoursTxt.setTypeface(custom_font);
//        addSessionBinding.thurHoursTxt.setTypeface(custom_font);
//        addSessionBinding.friHoursTxt.setTypeface(custom_font);
//        addSessionBinding.satHoursTxt.setTypeface(custom_font);
//        addSessionBinding.sunHoursTxt.setTypeface(custom_font);
//
//
//        addSessionBinding.sessionTimeTxt.setTypeface(custom_font);
//        addSessionBinding.addSessionBtn.setTypeface(custom_font);
//
//        addSessionBinding.sessionSelectAddressTxt.setTypeface(custom_font);
//        addSessionBinding.selectSessionAddressBtn.setTypeface(custom_font);
//        addSessionBinding.submitBtn.setTypeface(custom_font);
    }

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


        addSessionBinding.sessionNameEdt.setText(Utils.getPref(mContext, "ClassName"));
    }

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
                        if (flag.equalsIgnoreCase("edit")) {
                            addSessionBinding.recurringRb.setChecked(false);
                            addSessionBinding.singleRb.setChecked(false);
                        }
//                    addSessionBinding.singleRb.setChecked(false);
                        addSessionBinding.subjectEdt.setHint("Subject");
                        break;
                    case R.id.play_rb:
                        sessionTypeStr = addSessionBinding.playRb.getTag().toString();
                        addSessionBinding.sessionBoardLinear.setVisibility(View.GONE);
                        addSessionBinding.sessionStandardLinear.setVisibility(View.GONE);
                        addSessionBinding.sessionStreamLinear.setVisibility(View.GONE);
                        addSessionBinding.subjectEdt.setHint("Activity");
                        if (flag.equalsIgnoreCase("edit")) {
                            addSessionBinding.recurringRb.setChecked(false);
                            addSessionBinding.singleRb.setChecked(false);
                        }
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
                            if (sessionTypeStr.equalsIgnoreCase("Academic")) {
                                if (addSessionBinding.recurringRb.isChecked()) {
                                    sessionTypeValueStr = "1";
                                }else{
                                    sessionTypeValueStr = "";
                                }
                            } else {
                                if (addSessionBinding.recurringRb.isChecked()) {
                                    sessionTypeValueStr = "3";
                                }else{
                                    sessionTypeValueStr = "";
                                }

                            }
                            break;
                        case R.id.single_rb:
                                sessiontypeStr = "2";
                                if (sessionTypeStr.equalsIgnoreCase("Academic")) {
                                    if (addSessionBinding.singleRb.isChecked()) {
                                        sessionTypeValueStr = "2";
                                    }else{
                                        sessionTypeValueStr = "";
                                    }
                                } else {
                                    if (addSessionBinding.singleRb.isChecked()) {
                                        sessionTypeValueStr = "4";
                                    }else{
                                        sessionTypeValueStr = "";
                                    }
                                }
                            break;
                        default:
                }
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

    public void getSelectedTimeAlert() {
        String rowValueStr = "";
        for (int k = 0; k < alertListAdapter.getTime().size(); k++) {
            rowValueStr = alertListAdapter.getTime().get(k);
            Log.d("rowValueStr", rowValueStr);
        }
        addSessionBinding.alertBtn.setText(rowValueStr);
    }

    public void SessionTimeDialog() {
        popularDialog = new Dialog(getActivity(), R.style.Theme_Dialog);
        Window window = popularDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setAttributes(wlp);

        //popularDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);
        popularDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popularDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popularDialog.setCancelable(false);
        if (flag.equalsIgnoreCase("view")) {
            popularDialog.setContentView(R.layout.view_session_dialog);
        } else {
            popularDialog.setContentView(R.layout.add_session_dialog);
        }
        popularDialog.show();

        calendar = Calendar.getInstance();
        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);
        start_date_txt = (TextView) popularDialog.findViewById(R.id.start_date_txt);
        end_date_txt = (TextView) popularDialog.findViewById(R.id.end_date_txt);

        sun_start_time_txt = (TextView) popularDialog.findViewById(R.id.sun_start_time_txt);
        sun_end_time_txt = (TextView) popularDialog.findViewById(R.id.sun_end_time_txt);
        mon_start_time_txt = (TextView) popularDialog.findViewById(R.id.mon_start_time_txt);
        mon_end_time_txt = (TextView) popularDialog.findViewById(R.id.mon_end_time_txt);
        tue_start_time_txt = (TextView) popularDialog.findViewById(R.id.tue_start_time_txt);
        tue_end_time_txt = (TextView) popularDialog.findViewById(R.id.tue_end_time_txt);
        wed_start_time_txt = (TextView) popularDialog.findViewById(R.id.wed_start_time_txt);
        wed_end_time_txt = (TextView) popularDialog.findViewById(R.id.wed_end_time_txt);
        thu_start_time_txt = (TextView) popularDialog.findViewById(R.id.thu_start_time_txt);
        thu_end_time_txt = (TextView) popularDialog.findViewById(R.id.thu_end_time_txt);
        fri_end_time_txt = (TextView) popularDialog.findViewById(R.id.fri_end_time_txt);
        fri_start_time_txt = (TextView) popularDialog.findViewById(R.id.fri_start_time_txt);
        sat_end_time_txt = (TextView) popularDialog.findViewById(R.id.sat_end_time_txt);
        sat_start_time_txt = (TextView) popularDialog.findViewById(R.id.sat_start_time_txt);
        confirm_title_txt = (TextView) popularDialog.findViewById(R.id.confirm_title_txt);

        sun_start_linear = (LinearLayout) popularDialog.findViewById(R.id.sun_start_linear);
        mon_start_linear = (LinearLayout) popularDialog.findViewById(R.id.mon_start_linear);
        tue_start_linear = (LinearLayout) popularDialog.findViewById(R.id.tue_start_linear);
        wed_start_linear = (LinearLayout) popularDialog.findViewById(R.id.wed_start_linear);
        thu_start_linear = (LinearLayout) popularDialog.findViewById(R.id.thu_start_linear);
        fri_start_linear = (LinearLayout) popularDialog.findViewById(R.id.fri_start_linear);
        sat_start_linear = (LinearLayout) popularDialog.findViewById(R.id.sat_start_linear);

        sun_end_linear = (LinearLayout) popularDialog.findViewById(R.id.sun_end_linear);
        mon_end_linear = (LinearLayout) popularDialog.findViewById(R.id.mon_end_linear);
        tue_end_linear = (LinearLayout) popularDialog.findViewById(R.id.tue_end_linear);
        wed_end_linear = (LinearLayout) popularDialog.findViewById(R.id.wed_end_linear);
        thu_end_linear = (LinearLayout) popularDialog.findViewById(R.id.thu_end_linear);
        fri_end_linear = (LinearLayout) popularDialog.findViewById(R.id.fri_end_linear);
        sat_end_linear = (LinearLayout) popularDialog.findViewById(R.id.sat_end_linear);

        sun_start_add_session_btn = (Button) popularDialog.findViewById(R.id.sun_start_add_session_btn);
        sun_end_add_session_btn = (Button) popularDialog.findViewById(R.id.sun_end_add_session_btn);
        mon_start_add_session_btn = (Button) popularDialog.findViewById(R.id.mon_start_add_session_btn);
        mon_end_add_session_btn = (Button) popularDialog.findViewById(R.id.mon_end_add_session_btn);
        tue_start_add_session_btn = (Button) popularDialog.findViewById(R.id.tue_start_add_session_btn);
        tue_end_add_session_btn = (Button) popularDialog.findViewById(R.id.tue_end_add_session_btn);
        wed_start_add_session_btn = (Button) popularDialog.findViewById(R.id.wed_start_add_session_btn);
        wed_end_add_session_btn = (Button) popularDialog.findViewById(R.id.wed_end_add_session_btn);
        thu_start_add_session_btn = (Button) popularDialog.findViewById(R.id.thu_start_add_session_btn);
        thu_end_add_session_btn = (Button) popularDialog.findViewById(R.id.thu_end_add_session_btn);
        fri_start_add_session_btn = (Button) popularDialog.findViewById(R.id.fri_start_add_session_btn);
        fri_end_add_session_btn = (Button) popularDialog.findViewById(R.id.fri_end_add_session_btn);
        sat_start_add_session_btn = (Button) popularDialog.findViewById(R.id.sat_start_add_session_btn);
        sat_end_add_session_btn = (Button) popularDialog.findViewById(R.id.sat_end_add_session_btn);

        rg = (RadioGroup) popularDialog.findViewById(R.id.rg);
        recurringrb = (RadioButton) popularDialog.findViewById(R.id.recurring_rb);
        singlerb = (RadioButton) popularDialog.findViewById(R.id.single_rb);


        day_name_rcView = (RecyclerView) popularDialog.findViewById(R.id.day_name_rcView);

        done_btn = (Button) popularDialog.findViewById(R.id.done_btn);
        cancel_btn = (Button) popularDialog.findViewById(R.id.cancel_btn);

        if (!sessionTypeValueStr.equalsIgnoreCase("")) {
            switch (sessionTypeValueStr) {
                case "1":
                    recurringrb.setChecked(true);
                    break;
                case "2":
                    singlerb.setChecked(true);
                    break;
                case "3":
                    recurringrb.setChecked(true);
                    break;
                case "4":
                    singlerb.setChecked(true);
                    break;
            }
        }


        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int radioButtonId = rg.getCheckedRadioButtonId();
                switch (radioButtonId) {
                    case R.id.recurring_rb:
                        sessiontypeStr = "1";
                        if (sessionTypeStr.equalsIgnoreCase("Academic")) {
                            sessionTypeValueStr = "1";
                        } else {
                            sessionTypeValueStr = "3";

                        }
                        break;
                    case R.id.single_rb:
                        sessiontypeStr = "2";
                        if (sessionTypeStr.equalsIgnoreCase("Academic")) {
                            sessionTypeValueStr = "2";
                        } else {
                            sessionTypeValueStr = "4";

                        }
                        break;
                    default:
                }
            }
        });

//        Typeface custom_font = Typeface.createFromAsset(mContext.getAssets(), "font/TitilliumWeb-Regular.ttf");
//
//        sun_start_time_txt.setTypeface(custom_font);
//        sun_end_time_txt.setTypeface(custom_font);
//        mon_start_time_txt.setTypeface(custom_font);
//        mon_end_time_txt.setTypeface(custom_font);
//        tue_start_time_txt.setTypeface(custom_font);
//        tue_end_time_txt.setTypeface(custom_font);
//        wed_start_time_txt.setTypeface(custom_font);
//        wed_end_time_txt.setTypeface(custom_font);
//        thu_start_time_txt.setTypeface(custom_font);
//        thu_end_time_txt.setTypeface(custom_font);
//        fri_end_time_txt.setTypeface(custom_font);
//        fri_start_time_txt.setTypeface(custom_font);
//        sat_end_time_txt.setTypeface(custom_font);
//        sat_start_time_txt.setTypeface(custom_font);
//        confirm_title_txt.setTypeface(custom_font);
//        done_btn.setTypeface(custom_font);
//        cancel_btn.setTypeface(custom_font);

        Log.d("flag", flag);
        if (flag.equalsIgnoreCase("edit")) {
            confirm_title_txt.setText("EDIT CLASS TIME");
            start_date_txt.setText(EditStartDateStr);
            end_date_txt.setText(EditEndDateStr);
            String[] spiltPipes = EditScheduleStr.split("\\|");
            String[] spiltComma;
            String[] spiltDash;
            Log.d("spilt", "" + spiltPipes.toString());
            for (int i = 0; i < spiltPipes.length; i++) {
                spiltComma = spiltPipes[i].split("\\,");
                spiltDash = spiltComma[1].split("\\-");
                switch (spiltComma[0]) {
                    case "sun":
                        sun_start_time_txt.setText(spiltDash[0]);
                        sun_end_time_txt.setText(spiltDash[1]);
                        sun_start_add_session_btn.setText("x");
                        sun_start_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                        sun_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                        sun_end_add_session_btn.setText("x");
                        sun_end_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                        sun_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                        break;
                    case "mon":
                        mon_start_time_txt.setText(spiltDash[0]);
                        mon_end_time_txt.setText(spiltDash[1]);
                        mon_start_add_session_btn.setText("x");
                        mon_start_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                        mon_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                        mon_end_add_session_btn.setText("x");
                        mon_end_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                        mon_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                        break;
                    case "tue":
                        tue_start_time_txt.setText(spiltDash[0]);
                        tue_end_time_txt.setText(spiltDash[1]);
                        tue_start_add_session_btn.setText("x");
                        tue_start_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                        tue_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                        tue_end_add_session_btn.setText("x");
                        tue_end_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                        tue_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                        break;
                    case "wed":
                        wed_start_time_txt.setText(spiltDash[0]);
                        wed_end_time_txt.setText(spiltDash[1]);
                        wed_start_add_session_btn.setText("x");
                        wed_start_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                        wed_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                        wed_end_add_session_btn.setText("x");
                        wed_end_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                        wed_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                        break;
                    case "thu":
                        thu_start_time_txt.setText(spiltDash[0]);
                        thu_end_time_txt.setText(spiltDash[1]);
                        thu_start_add_session_btn.setText("x");
                        thu_start_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                        thu_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                        thu_end_add_session_btn.setText("x");
                        thu_end_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                        thu_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                        break;
                    case "fri":
                        fri_start_time_txt.setText(spiltDash[0]);
                        fri_end_time_txt.setText(spiltDash[1]);
                        fri_start_add_session_btn.setText("x");
                        fri_start_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                        fri_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                        fri_end_add_session_btn.setText("x");
                        fri_end_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                        fri_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                        break;
                    case "sat":
                        sat_start_time_txt.setText(spiltDash[0]);
                        sat_end_time_txt.setText(spiltDash[1]);
                        sat_start_add_session_btn.setText("x");
                        sat_start_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                        sat_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                        sat_end_add_session_btn.setText("x");
                        sat_end_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                        sat_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                        break;
                    default:

                }
            }
            final List<String> days = getDates(start_date_txt.getText().toString(), end_date_txt.getText().toString());
            System.out.println(days);
        } else if (flag.equalsIgnoreCase("view")) {
            confirm_title_txt.setText("VIEW CLASS TIME");
            if (!scheduleStr.equalsIgnoreCase("")) {
                diableDialogControl();
                start_date_txt.setText(EditStartDateStr);
                end_date_txt.setText(EditEndDateStr);
                String[] spiltPipes = scheduleStr.split("\\|");
                String[] spiltComma;
                String[] spiltDash;
                Log.d("spilt", "" + spiltPipes.toString());
                for (int i = 0; i < spiltPipes.length; i++) {
                    spiltComma = spiltPipes[i].split("\\,");
                    spiltDash = spiltComma[1].split("\\-");
                    switch (spiltComma[0]) {
                        case "sun":
                            sun_start_time_txt.setText(spiltDash[0]);
                            sun_end_time_txt.setText(spiltDash[1]);
                            sun_start_add_session_btn.setText("x");
                            sun_start_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                            sun_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            sun_end_add_session_btn.setText("x");
                            sun_end_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                            sun_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            break;
                        case "mon":
                            mon_start_time_txt.setText(spiltDash[0]);
                            mon_end_time_txt.setText(spiltDash[1]);
                            mon_start_add_session_btn.setText("x");
                            mon_start_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                            mon_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            mon_end_add_session_btn.setText("x");
                            mon_end_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                            mon_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            break;
                        case "tue":
                            tue_start_time_txt.setText(spiltDash[0]);
                            tue_end_time_txt.setText(spiltDash[1]);
                            tue_start_add_session_btn.setText("x");
                            tue_start_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                            tue_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            tue_end_add_session_btn.setText("x");
                            tue_end_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                            tue_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            break;
                        case "wed":
                            wed_start_time_txt.setText(spiltDash[0]);
                            wed_end_time_txt.setText(spiltDash[1]);
                            wed_start_add_session_btn.setText("x");
                            wed_start_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                            wed_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            wed_end_add_session_btn.setText("x");
                            wed_end_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                            wed_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            break;
                        case "thu":
                            thu_start_time_txt.setText(spiltDash[0]);
                            thu_end_time_txt.setText(spiltDash[1]);
                            thu_start_add_session_btn.setText("x");
                            thu_start_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                            thu_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            thu_end_add_session_btn.setText("x");
                            thu_end_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                            thu_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            break;
                        case "fri":
                            fri_start_time_txt.setText(spiltDash[0]);
                            fri_end_time_txt.setText(spiltDash[1]);
                            fri_start_add_session_btn.setText("x");
                            fri_start_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                            fri_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            fri_end_add_session_btn.setText("x");
                            fri_end_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                            fri_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            break;
                        case "sat":
                            sat_start_time_txt.setText(spiltDash[0]);
                            sat_end_time_txt.setText(spiltDash[1]);
                            sat_start_add_session_btn.setText("x");
                            sat_start_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                            sat_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            sat_end_add_session_btn.setText("x");
                            sat_end_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                            sat_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            break;
                        default:
                    }

                }

            } else {
                start_date_txt.setText(Utils.getTodaysDate());
                end_date_txt.setText(Utils.getTodaysDate());

                final List<String> days = getDates(start_date_txt.getText().toString(), end_date_txt.getText().toString());
                System.out.println(days);
            }
        } else if (flag.equalsIgnoreCase("reedit")) {
            if (!scheduleStr.equalsIgnoreCase("")) {
                start_date_txt.setText(EditStartDateStr);
                end_date_txt.setText(EditEndDateStr);

                String[] spiltPipes = scheduleStr.split("\\|");
                String[] spiltComma;
                String[] spiltDash;
                Log.d("spilt", "" + spiltPipes.toString());
                for (int i = 0; i < spiltPipes.length; i++) {
                    spiltComma = spiltPipes[i].split("\\,");
                    spiltDash = spiltComma[1].split("\\-");
                    switch (spiltComma[0]) {
                        case "sun":
                            sun_start_time_txt.setText(spiltDash[0]);
                            sun_end_time_txt.setText(spiltDash[1]);
                            sun_start_add_session_btn.setText("x");
                            sun_start_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                            sun_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            sun_end_add_session_btn.setText("x");
                            sun_end_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                            sun_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            sun_start_linear.setAlpha(1);
                            sun_end_linear.setAlpha(1);
                            break;
                        case "mon":
                            mon_start_time_txt.setText(spiltDash[0]);
                            mon_end_time_txt.setText(spiltDash[1]);
                            mon_start_add_session_btn.setText("x");
                            mon_start_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                            mon_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            mon_end_add_session_btn.setText("x");
                            mon_end_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                            mon_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            mon_start_linear.setAlpha(1);
                            mon_end_linear.setAlpha(1);
                            break;
                        case "tue":
                            tue_start_time_txt.setText(spiltDash[0]);
                            tue_end_time_txt.setText(spiltDash[1]);
                            tue_start_add_session_btn.setText("x");
                            tue_start_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                            tue_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            tue_end_add_session_btn.setText("x");
                            tue_end_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                            tue_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            tue_start_linear.setAlpha(1);
                            tue_end_linear.setAlpha(1);
                            break;
                        case "wed":
                            wed_start_time_txt.setText(spiltDash[0]);
                            wed_end_time_txt.setText(spiltDash[1]);
                            wed_start_add_session_btn.setText("x");
                            wed_start_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                            wed_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            wed_end_add_session_btn.setText("x");
                            wed_end_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                            wed_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            wed_start_linear.setAlpha(1);
                            wed_end_linear.setAlpha(1);
                            break;
                        case "thu":
                            thu_start_time_txt.setText(spiltDash[0]);
                            thu_end_time_txt.setText(spiltDash[1]);
                            thu_start_add_session_btn.setText("x");
                            thu_start_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                            thu_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            thu_end_add_session_btn.setText("x");
                            thu_end_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                            thu_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            thu_start_linear.setAlpha(1);
                            thu_end_linear.setAlpha(1);
                            break;
                        case "fri":
                            fri_start_time_txt.setText(spiltDash[0]);
                            fri_end_time_txt.setText(spiltDash[1]);
                            fri_start_add_session_btn.setText("x");
                            fri_start_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                            fri_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            fri_end_add_session_btn.setText("x");
                            fri_end_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                            fri_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            fri_start_linear.setAlpha(1);
                            fri_end_linear.setAlpha(1);
                            break;
                        case "sat":
                            sat_start_time_txt.setText(spiltDash[0]);
                            sat_end_time_txt.setText(spiltDash[1]);
                            sat_start_add_session_btn.setText("x");
                            sat_start_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                            sat_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            sat_end_add_session_btn.setText("x");
                            sat_end_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                            sat_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                            sun_start_linear.setAlpha(1);
                            sun_end_linear.setAlpha(1);
                            break;
                        default:
                    }

                }
                final List<String> days = getDates(start_date_txt.getText().toString(), end_date_txt.getText().toString());
                System.out.println(days);
            } else {
                start_date_txt.setText(Utils.getTodaysDate());
                end_date_txt.setText(Utils.getTodaysDate());

                final List<String> days = getDates(start_date_txt.getText().toString(), end_date_txt.getText().toString());
                System.out.println(days);
            }
        } else {
            start_date_txt.setText(Utils.getTodaysDate());
            end_date_txt.setText(Utils.getTodaysDate());

            final List<String> days = getDates(start_date_txt.getText().toString(), end_date_txt.getText().toString());
            System.out.println(days);
        }


        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popularDialog.dismiss();
            }
        });
        if (!flag.equalsIgnoreCase("view")) {
            done_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getArguments().getString("flag").equalsIgnoreCase("Add")) {
                        flag = "reedit";
                    }
                    doneStartDate = start_date_txt.getText().toString();
                    doneEndDate = end_date_txt.getText().toString();

                    EditStartDateStr = doneStartDate;
                    EditEndDateStr = doneEndDate;
                    scheduleArray = new ArrayList<>();
                    newEnteryArray = new ArrayList<>();
                    scheduleStr = "";
                    if (!checkTime_sun && !checkTime_mon && !checkTime_thu && !checkTime_wed && !checkTime_tue && !checkTime_fri && !checkTime_sat) {
                        if (!sun_start_time_txt.getText().toString().equalsIgnoreCase("Add") && !sun_end_time_txt.getText().toString().equalsIgnoreCase("Add")) {
                            sunstartTimeStr = sun_start_time_txt.getText().toString();
                            sunendTimeStr = sun_end_time_txt.getText().toString();
                            finalsunTimeStr = "sun" + "," + sunstartTimeStr + "-" + sunendTimeStr;
                            Log.d("SundayTime", finalsunTimeStr);

                            scheduleArray.add(finalsunTimeStr);

                        } else {
                        }
                        if (!mon_start_time_txt.getText().toString().equalsIgnoreCase("Add") && !mon_end_time_txt.getText().toString().equalsIgnoreCase("")) {
                            monstartTimeStr = mon_start_time_txt.getText().toString();
                            monendTimeStr = mon_end_time_txt.getText().toString();
                            finalmonTimeStr = "mon" + "," + monstartTimeStr + "-" + monendTimeStr;
                            scheduleArray.add(finalmonTimeStr);

                        } else {
//                    Util.ping(mContext, "Please Select Time.");

                        }
                        if (!tue_start_time_txt.getText().toString().equalsIgnoreCase("Add") && !tue_end_time_txt.getText().toString().equalsIgnoreCase("Add")) {
                            tuestartTimeStr = tue_start_time_txt.getText().toString();
                            tueendTimeStr = tue_end_time_txt.getText().toString();
                            finaltueTimeStr = "tue" + "," + tuestartTimeStr + "-" + tueendTimeStr;
                            scheduleArray.add(finaltueTimeStr);
                        } else {
//                    Util.ping(mContext, "Please Select Time.");

                        }
                        if (!wed_start_time_txt.getText().toString().equalsIgnoreCase("Add") && !wed_end_time_txt.getText().toString().equalsIgnoreCase("Add")) {
                            wedstartTimeStr = wed_start_time_txt.getText().toString();
                            wedendTimeStr = wed_end_time_txt.getText().toString();
                            finalwedTimeStr = "wed" + "," + wedstartTimeStr + "-" + wedendTimeStr;
                            scheduleArray.add(finalwedTimeStr);
                        } else {
//                    Util.ping(mContext, "Please Select Time.");

                        }
                        if (!thu_start_time_txt.getText().toString().equalsIgnoreCase("Add") && !thu_end_time_txt.getText().toString().equalsIgnoreCase("Add")) {
                            thustartTimeStr = thu_start_time_txt.getText().toString();
                            thuendTimeStr = thu_end_time_txt.getText().toString();
                            finalthuTimeStr = "thu" + "," + thustartTimeStr + "-" + thuendTimeStr;

                            scheduleArray.add(finalthuTimeStr);
                        } else {
//                    Util.ping(mContext, "Please Select Time.");

                        }
                        if (!fri_start_time_txt.getText().toString().equalsIgnoreCase("Add") && !fri_end_time_txt.getText().toString().equalsIgnoreCase("Add")) {
                            fristartTimeStr = fri_start_time_txt.getText().toString();
                            friendTimeStr = fri_end_time_txt.getText().toString();
                            finalfriTimeStr = "fri" + "," + fristartTimeStr + "-" + friendTimeStr;

                            scheduleArray.add(finalfriTimeStr);

                        } else {
//                    Util.ping(mContext, "Please Select Time.");

                        }
                        if (!sat_start_time_txt.getText().toString().equalsIgnoreCase("Add") && !sat_end_time_txt.getText().toString().equalsIgnoreCase("Add")) {
                            satstartTimeStr = sat_start_time_txt.getText().toString();
                            satendTimeStr = sat_end_time_txt.getText().toString();
                            finalsatTimeStr = "sat" + "," + satstartTimeStr + "-" + satendTimeStr;

                            scheduleArray.add(finalsatTimeStr);
                        } else {
//                    Util.ping(mContext, "Please Select Time.");
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
//                if (days.size() == scheduleArray.size()) {

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


//                }else{
//                    Utils.ping(mContext,"Please Enter Proper Time");
//                }

                    } else {
                        Utils.ping(mContext, "Please select propertime");

                    }
                    fillTimeGrid();

                }
            });

        }
        start_date_txt.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                isFromDate = true;
                datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(AddSessionFragment.this, Year, Month, Day);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.setOkText("Done");
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.setAccentColor(Color.parseColor("#f2552c"));
//                datePickerDialog.setMinDate(Calendar.getInstance());
//                datePickerDialog.setTitle("Select Date From DatePickerDialog");
                datePickerDialog.show(getActivity().getFragmentManager(), "DatePickerDialog");
            }
        });
        end_date_txt.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                isFromDate = false;
                datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(AddSessionFragment.this, Year, Month, Day);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.setOkText("Done");
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.setAccentColor(Color.parseColor("#f2552c"));
//                datePickerDialog.setTitle("Select Date From DatePickerDialog");
//                datePickerDialog.setMinDate(Calendar.getInstance());
                datePickerDialog.show(getActivity().getFragmentManager(), "DatePickerDialog");

            }
        });
        sun_start_linear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });

        sun_end_linear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });
        mon_start_linear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });

        mon_end_linear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });
        tue_start_linear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });

        tue_end_linear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });
        wed_start_linear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });

        wed_end_linear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });
        thu_start_linear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });

        thu_end_linear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });
        fri_start_linear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });

        fri_end_linear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });
        sat_start_linear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });

        sat_end_linear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });

        sun_start_add_session_btn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
                sun_start_add_session_btn.setText("+");
                sun_start_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                sun_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));
                if (!sun_start_time_txt.getText().toString().equalsIgnoreCase("Add")) {
                    sun_start_time_txt.setText("Add");
                }

            }
        });
        sun_end_add_session_btn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
                sun_end_add_session_btn.setText("+");
                sun_end_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                sun_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));
                if (!sun_end_time_txt.getText().toString().equalsIgnoreCase("Add")) {
                    sun_end_time_txt.setText("Add");
                }
            }
        });
        mon_start_add_session_btn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
                mon_start_add_session_btn.setText("+");
                mon_start_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                mon_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));
                if (!mon_start_time_txt.getText().toString().equalsIgnoreCase("Add")) {
                    mon_start_time_txt.setText("Add");
                }
            }
        });
        mon_end_add_session_btn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
                mon_end_add_session_btn.setText("+");
                mon_end_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                mon_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));
                if (!mon_end_time_txt.getText().toString().equalsIgnoreCase("Add")) {
                    mon_end_time_txt.setText("Add");
                }
            }
        });
        tue_start_add_session_btn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
                tue_start_add_session_btn.setText("+");
                tue_start_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                tue_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));
                if (!tue_start_time_txt.getText().toString().equalsIgnoreCase("Add")) {
                    tue_start_time_txt.setText("Add");
                }
            }
        });
        tue_end_add_session_btn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
                tue_end_add_session_btn.setText("+");
                tue_end_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                tue_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));
                if (!tue_end_time_txt.getText().toString().equalsIgnoreCase("Add")) {
                    tue_end_time_txt.setText("Add");
                }
            }
        });
        wed_start_add_session_btn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
                wed_start_add_session_btn.setText("+");
                wed_start_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                wed_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));
                if (!wed_start_time_txt.getText().toString().equalsIgnoreCase("Add")) {
                    wed_start_time_txt.setText("Add");
                }
            }
        });
        wed_end_add_session_btn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
                wed_end_add_session_btn.setText("+");
                wed_end_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                wed_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));
                if (!wed_end_time_txt.getText().toString().equalsIgnoreCase("Add")) {
                    wed_end_time_txt.setText("Add");
                }
            }
        });
        thu_start_add_session_btn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
                thu_start_add_session_btn.setText("+");
                thu_start_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                thu_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));
                if (!thu_start_time_txt.getText().toString().equalsIgnoreCase("Add")) {
                    thu_start_time_txt.setText("Add");
                }
            }
        });
        thu_end_add_session_btn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
                thu_end_add_session_btn.setText("+");
                thu_end_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                thu_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));
                if (!thu_end_time_txt.getText().toString().equalsIgnoreCase("Add")) {
                    thu_end_time_txt.setText("Add");
                }
            }
        });
        fri_start_add_session_btn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
                fri_start_add_session_btn.setText("+");
                fri_start_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                fri_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));
                if (!fri_start_time_txt.getText().toString().equalsIgnoreCase("Add")) {
                    fri_start_time_txt.setText("Add");
                }
            }
        });
        fri_end_add_session_btn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
                fri_end_add_session_btn.setText("+");
                fri_end_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                fri_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));
                if (!fri_end_time_txt.getText().toString().equalsIgnoreCase("Add")) {
                    fri_end_time_txt.setText("Add");
                }
            }
        });
        sat_start_add_session_btn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
                sat_start_add_session_btn.setText("+");
                sat_start_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                sat_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));
                if (!sat_start_time_txt.getText().toString().equalsIgnoreCase("Add")) {
                    sat_start_time_txt.setText("Add");
                }
            }
        });
        sat_end_add_session_btn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
                sat_end_add_session_btn.setText("+");
                sat_end_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                sat_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));
                if (!sat_end_time_txt.getText().toString().equalsIgnoreCase("Add")) {
                    sat_end_time_txt.setText("Add");
                }
            }
        });

        startDateStr = start_date_txt.getText().

                toString();

        endDateStr = end_date_txt.getText().

                toString();
    }

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
                start_date_txt.setText(dateFinal);
            } else {
                end_date_txt.setText(dateFinal);
            }
        } else {
            start_date_txt.setText(dateFinal);
            end_date_txt.setText(dateFinal);
            end_date_txt.setEnabled(false);
        }
        startDateStr = start_date_txt.getText().toString();
        endDateStr = end_date_txt.getText().toString();

        if (flag.equalsIgnoreCase("edit")) {
            if (EditStartDateStr.equalsIgnoreCase(startDateStr)) {
                if (EditEndDateStr.equalsIgnoreCase(endDateStr)) {

                }
            } else {
                sun_start_time_txt.setText("Add");
                sun_start_add_session_btn.setText("+");
                sun_start_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                sun_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                sun_end_time_txt.setText("Add");
                sun_end_add_session_btn.setText("+");
                sun_end_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                sun_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                mon_start_time_txt.setText("Add");
                mon_start_add_session_btn.setText("+");
                mon_start_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                mon_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                mon_end_time_txt.setText("Add");
                mon_end_add_session_btn.setText("+");
                mon_end_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                mon_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                tue_start_time_txt.setText("Add");
                tue_start_add_session_btn.setText("+");
                tue_start_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                tue_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                tue_end_time_txt.setText("Add");
                tue_end_add_session_btn.setText("+");
                tue_end_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                tue_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                wed_start_time_txt.setText("Add");
                wed_start_add_session_btn.setText("+");
                wed_start_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                wed_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                wed_end_time_txt.setText("Add");
                wed_end_add_session_btn.setText("+");
                wed_end_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                wed_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                thu_start_time_txt.setText("Add");
                thu_start_add_session_btn.setText("+");
                thu_start_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                thu_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                thu_end_time_txt.setText("Add");
                thu_end_add_session_btn.setText("+");
                thu_end_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                thu_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                fri_start_time_txt.setText("Add");
                fri_start_add_session_btn.setText("+");
                fri_start_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                fri_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                fri_end_time_txt.setText("Add");
                fri_end_add_session_btn.setText("+");
                fri_end_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                fri_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                sat_start_time_txt.setText("Add");
                sat_start_add_session_btn.setText("+");
                sat_start_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                sat_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                sat_end_time_txt.setText("Add");
                sat_end_add_session_btn.setText("+");
                sat_end_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                sat_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));
            }

        } else {
            if (doneStartDate.equalsIgnoreCase(startDateStr)) {
                if (doneEndDate.equalsIgnoreCase(endDateStr)) {

                } else {
                }
            } else {
                sun_start_time_txt.setText("Add");
                sun_start_add_session_btn.setText("+");
                sun_start_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                sun_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                sun_end_time_txt.setText("Add");
                sun_end_add_session_btn.setText("+");
                sun_end_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                sun_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                mon_start_time_txt.setText("Add");
                mon_start_add_session_btn.setText("+");
                mon_start_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                mon_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                mon_end_time_txt.setText("Add");
                mon_end_add_session_btn.setText("+");
                mon_end_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                mon_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                tue_start_time_txt.setText("Add");
                tue_start_add_session_btn.setText("+");
                tue_start_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                tue_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                tue_end_time_txt.setText("Add");
                tue_end_add_session_btn.setText("+");
                tue_end_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                tue_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                wed_start_time_txt.setText("Add");
                wed_start_add_session_btn.setText("+");
                wed_start_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                wed_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                wed_end_time_txt.setText("Add");
                wed_end_add_session_btn.setText("+");
                wed_end_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                wed_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                thu_start_time_txt.setText("Add");
                thu_start_add_session_btn.setText("+");
                thu_start_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                thu_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                thu_end_time_txt.setText("Add");
                thu_end_add_session_btn.setText("+");
                thu_end_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                thu_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                fri_start_time_txt.setText("Add");
                fri_start_add_session_btn.setText("+");
                fri_start_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                fri_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                fri_end_time_txt.setText("Add");
                fri_end_add_session_btn.setText("+");
                fri_end_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                fri_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                sat_start_time_txt.setText("Add");
                sat_start_add_session_btn.setText("+");
                sat_start_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                sat_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));

                sat_end_time_txt.setText("Add");
                sat_end_add_session_btn.setText("+");
                sat_end_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                sat_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));
            }
        }


        List<String> days = getDates(start_date_txt.getText().toString(), end_date_txt.getText().toString());
        System.out.println(days);

    }

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

    //Use for Board
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

    public void fillBoard() {
        ArrayList<String> BoardName = new ArrayList<String>();
        for (int j = 0; j < dataResponse.getData().size(); j++) {
            BoardName.add(dataResponse.getData().get(j).getBoardName());
        }
        ArrayAdapter<String> adapterTerm = new ArrayAdapter<String>(mContext, R.layout.autocomplete_layout, BoardName);
        addSessionBinding.boardNameEdt.setThreshold(1);
        addSessionBinding.boardNameEdt.setAdapter(adapterTerm);

    }

    //Use for standard
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

    public void fillStandard() {
        ArrayList<String> StandardName = new ArrayList<String>();
        for (int j = 0; j < dataResponse.getData().size(); j++) {
            StandardName.add(dataResponse.getData().get(j).getStandardName());
        }
        ArrayAdapter<String> adapterTerm = new ArrayAdapter<String>(mContext, R.layout.autocomplete_layout, StandardName);
        addSessionBinding.standardEdt.setThreshold(1);
        addSessionBinding.standardEdt.setAdapter(adapterTerm);
    }

    //Use for Stream
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

    public void fillStream() {

        ArrayList<String> StreamName = new ArrayList<String>();
        for (int j = 0; j < dataResponse.getData().size(); j++) {
            StreamName.add(dataResponse.getData().get(j).getStreamName());
        }
        ArrayAdapter<String> adapterTerm = new ArrayAdapter<String>(mContext, R.layout.autocomplete_layout, StreamName);
        addSessionBinding.streamEdt.setThreshold(1);
        addSessionBinding.streamEdt.setAdapter(adapterTerm);
    }

    //Use for Lession
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

    public void fillLession() {

        ArrayList<String> LessionName = new ArrayList<String>();
        for (int j = 0; j < dataResponse.getData().size(); j++) {
            LessionName.add(dataResponse.getData().get(j).getLessonTypeName());
        }

        ArrayAdapter<String> adapterTerm = new ArrayAdapter<String>(mContext, R.layout.autocomplete_layout, LessionName);
        addSessionBinding.subjectEdt.setThreshold(1);
        addSessionBinding.subjectEdt.setAdapter(adapterTerm);
    }

    //Use for Region
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

    public void fillArea() {
        ArrayList<String> AreaName = new ArrayList<String>();
        for (int j = 0; j < dataResponse.getData().size(); j++) {
            AreaName.add(dataResponse.getData().get(j).getRegionName());
        }
        ArrayAdapter<String> adapterTerm = new ArrayAdapter<String>(mContext, R.layout.autocomplete_layout, AreaName);
        addSessionBinding.areaEdt.setThreshold(1);
        addSessionBinding.areaEdt.setAdapter(adapterTerm);
    }

    //Use for Address
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

    //Use for EditSession
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

    public void fillEditSessionFiled() {
        checkfalse = true;
        scheduleStr = "";
        for (int i = 0; i < dataResponse.getData().size(); i++) {
            switch (dataResponse.getData().get(i).getSessionType()) {
                case "1":
                    addSessionBinding.AcademicRb.setChecked(true);
                    //sessionTypeStr="Academic";
                    addSessionBinding.recurringRb.setChecked(true);

                    break;
                case "2":
                    addSessionBinding.AcademicRb.setChecked(true);
                    //sessionTypeStr="Academic";
                    addSessionBinding.singleRb.setChecked(true);

                    break;
                case "3":
                    addSessionBinding.playRb.setChecked(true);
                    //sessionTypeStr="Sports";
                    addSessionBinding.recurringRb.setChecked(true);

                    break;
                case "4":
                    addSessionBinding.playRb.setChecked(true);
                    //sessionTypeStr="Sports";
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
            }else{
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

    //Use for Update Session
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

    public void editSessionValidation() {
//        sessionamtStr = addSessionBinding.sessionPriceEdt.getText().toString();
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
                                addSessionBinding.addressEdt.setError(getString(R.string.sessionAddress));
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
                                            addSessionBinding.addressEdt.setError(getString(R.string.sessionAddress));
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
                                            addSessionBinding.addressEdt.setError(getString(R.string.sessionAddress));
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
                                                        addSessionBinding.addressEdt.setError(getString(R.string.sessionAddress));
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
        addSessionBinding.descriptionEdt.setEnabled(false);
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

    }

    public void diableDialogControl() {
        start_date_txt.setEnabled(false);
        end_date_txt.setEnabled(false);

        sun_start_linear.setEnabled(false);
        sun_end_linear.setEnabled(false);
        sun_start_time_txt.setEnabled(false);
        sun_end_time_txt.setEnabled(false);
        sun_start_add_session_btn.setEnabled(false);
        sun_end_add_session_btn.setEnabled(false);
        sun_start_linear.setAlpha(1);
        sun_end_linear.setAlpha(1);

        mon_start_linear.setEnabled(false);
        mon_end_linear.setEnabled(false);
        mon_start_time_txt.setEnabled(false);
        mon_end_time_txt.setEnabled(false);
        mon_start_add_session_btn.setEnabled(false);
        mon_end_add_session_btn.setEnabled(false);
        mon_start_linear.setAlpha(1);
        mon_end_linear.setAlpha(1);

        tue_start_linear.setEnabled(false);
        tue_end_linear.setEnabled(false);
        tue_start_linear.setAlpha(1);
        tue_end_linear.setAlpha(1);
        tue_start_time_txt.setEnabled(false);
        tue_end_time_txt.setEnabled(false);
        tue_start_add_session_btn.setEnabled(false);
        tue_end_add_session_btn.setEnabled(false);

        wed_start_linear.setEnabled(false);
        wed_end_linear.setEnabled(false);
        wed_start_linear.setAlpha(1);
        wed_end_linear.setAlpha(1);
        wed_start_add_session_btn.setEnabled(false);
        wed_end_add_session_btn.setEnabled(false);

        thu_start_linear.setEnabled(false);
        thu_end_linear.setEnabled(false);
        thu_start_linear.setAlpha(1);
        thu_end_linear.setAlpha(1);
        thu_start_add_session_btn.setEnabled(false);
        thu_end_add_session_btn.setEnabled(false);

        fri_start_linear.setEnabled(false);
        fri_end_linear.setEnabled(false);
        fri_start_linear.setAlpha(1);
        fri_end_linear.setAlpha(1);
        fri_start_add_session_btn.setEnabled(false);
        fri_end_add_session_btn.setEnabled(false);

        sat_start_linear.setEnabled(false);
        sat_end_linear.setEnabled(false);
        sat_start_linear.setAlpha(1);
        sat_end_linear.setAlpha(1);
        sat_start_add_session_btn.setEnabled(false);
        sat_end_add_session_btn.setEnabled(false);

    }

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

    public void averageHours(List<Integer> list) {
        int sum = 0;
        int n = list.size();
        for (int i = 0; i < n; i++)
            sum += list.get(i);
        avgHoursvalue = (sum) / n;
        Log.d("value", "" + avgHoursvalue);
    }

    public void averageMinit(List<Integer> list) {
        int sum = 0;
        int n = list.size();
        for (int i = 0; i < n; i++)
            sum += list.get(i);
        avgMinitvalue = (sum) / n;
        Log.d("value", "" + avgMinitvalue);
    }

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
                SessionDuration = String.valueOf(SessionHour) + " hrs";
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

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
                    sun_start_time_txt.setText(FinalTimeStr);
                    sun_start_add_session_btn.setText("x");
                    sun_start_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                    sun_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                    break;
                case "1":
                    sun_end_time_txt.setText(FinalTimeStr);
                    sun_end_add_session_btn.setText("x");
                    sun_end_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                    sun_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                    try {
                        Date inTime = sdf.parse(sun_start_time_txt.getText().toString());
                        Date outTime = sdf.parse(sun_end_time_txt.getText().toString());
                        if (outTime.before(inTime)) { //Same way you can check with after() method also.
                            sun_end_time_txt.setTextColor(getResources().getColor(R.color.search_boder));
                            sun_end_linear.setBackgroundResource(R.drawable.red_linear);
                            checkTime_sun = true;
                        } else {
                            checkTime_sun = false;
                            sun_end_time_txt.setTextColor(getResources().getColor(R.color.text_color));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case "2":
                    mon_start_time_txt.setText(FinalTimeStr);
                    mon_start_add_session_btn.setText("x");
                    mon_start_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                    mon_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                    break;
                case "3":
                    mon_end_time_txt.setText(FinalTimeStr);
                    mon_end_add_session_btn.setText("x");
                    mon_end_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                    mon_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                    SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm a");
                    try {
                        Date inTime = sdf1.parse(mon_start_time_txt.getText().toString());
                        Date outTime = sdf1.parse(mon_end_time_txt.getText().toString());
                        if (outTime.before(inTime)) { //Same way you can check with after() method also.
                            mon_end_time_txt.setTextColor(getResources().getColor(R.color.search_boder));
                            mon_end_linear.setBackgroundResource(R.drawable.red_linear);
                            checkTime_mon = true;
                        } else {
                            checkTime_mon = false;
                            mon_end_time_txt.setTextColor(getResources().getColor(R.color.text_color));
                            mon_end_linear.setBackgroundResource(R.drawable.linear_shape);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case "4":
                    tue_start_time_txt.setText(FinalTimeStr);
                    tue_start_add_session_btn.setText("x");
                    tue_start_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                    tue_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                    break;
                case "5":
                    tue_end_time_txt.setText(FinalTimeStr);
                    tue_end_add_session_btn.setText("x");
                    tue_end_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                    tue_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                    SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm a");
                    try {
                        Date inTime = sdf2.parse(tue_start_time_txt.getText().toString());
                        Date outTime = sdf2.parse(tue_end_time_txt.getText().toString());
                        if (outTime.before(inTime)) { //Same way you can check with after() method also.
                            tue_end_time_txt.setTextColor(getResources().getColor(R.color.search_boder));
                            tue_end_linear.setBackgroundResource(R.drawable.red_linear);
                            checkTime_tue = true;
                        } else {
                            checkTime_tue = false;
                            tue_end_time_txt.setTextColor(getResources().getColor(R.color.text_color));
                            tue_end_linear.setBackgroundResource(R.drawable.linear_shape);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case "6":
                    wed_start_time_txt.setText(FinalTimeStr);
                    wed_start_add_session_btn.setText("x");
                    wed_start_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                    wed_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                    break;
                case "7":
                    wed_end_time_txt.setText(FinalTimeStr);
                    wed_end_add_session_btn.setText("x");
                    wed_end_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                    wed_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));

                    SimpleDateFormat sdf3 = new SimpleDateFormat("hh:mm a");
                    try {
                        Date inTime = sdf3.parse(wed_start_time_txt.getText().toString());
                        Date outTime = sdf3.parse(wed_end_time_txt.getText().toString());
                        if (outTime.before(inTime)) { //Same way you can check with after() method also.
                            wed_end_time_txt.setTextColor(getResources().getColor(R.color.search_boder));
                            wed_end_linear.setBackgroundResource(R.drawable.red_linear);
                            checkTime_wed = true;
                        } else {
                            checkTime_wed = false;
                            wed_end_time_txt.setTextColor(getResources().getColor(R.color.text_color));
                            wed_end_linear.setBackgroundResource(R.drawable.linear_shape);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case "8":
                    thu_start_time_txt.setText(FinalTimeStr);
                    thu_start_add_session_btn.setText("x");
                    thu_start_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                    thu_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                    break;
                case "9":
                    thu_end_time_txt.setText(FinalTimeStr);
                    thu_end_add_session_btn.setText("x");
                    thu_end_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                    thu_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));

                    SimpleDateFormat sdf4 = new SimpleDateFormat("hh:mm a");
                    try {
                        Date inTime = sdf4.parse(thu_start_time_txt.getText().toString());
                        Date outTime = sdf4.parse(thu_end_time_txt.getText().toString());
                        if (outTime.before(inTime)) { //Same way you can check with after() method also.
                            thu_end_time_txt.setTextColor(getResources().getColor(R.color.search_boder));
                            thu_end_linear.setBackgroundResource(R.drawable.red_linear);
                            checkTime_thu = true;
                        } else {
                            checkTime_thu = false;
                            thu_end_time_txt.setTextColor(getResources().getColor(R.color.text_color));
                            thu_end_linear.setBackgroundResource(R.drawable.linear_shape);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case "10":
                    fri_start_time_txt.setText(FinalTimeStr);
                    fri_start_add_session_btn.setText("x");
                    fri_start_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                    fri_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                    break;
                case "11":
                    fri_end_time_txt.setText(FinalTimeStr);
                    fri_end_add_session_btn.setText("x");
                    fri_end_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                    fri_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));

                    SimpleDateFormat sdf5 = new SimpleDateFormat("hh:mm a");
                    try {
                        Date inTime = sdf5.parse(fri_start_time_txt.getText().toString());
                        Date outTime = sdf5.parse(fri_end_time_txt.getText().toString());
                        if (outTime.before(inTime)) { //Same way you can check with after() method also.
                            fri_end_time_txt.setTextColor(getResources().getColor(R.color.search_boder));
                            fri_end_linear.setBackgroundResource(R.drawable.red_linear);
                            checkTime_fri = true;
                        } else {
                            checkTime_fri = false;
                            fri_end_time_txt.setTextColor(getResources().getColor(R.color.text_color));
                            fri_end_linear.setBackgroundResource(R.drawable.linear_shape);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case "12":
                    sat_start_time_txt.setText(FinalTimeStr);
                    sat_start_add_session_btn.setText("x");
                    sat_start_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                    sat_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                    break;
                case "13":
                    sat_end_time_txt.setText(FinalTimeStr);
                    sat_end_add_session_btn.setText("x");
                    sat_end_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                    sat_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));

                    SimpleDateFormat sdf6 = new SimpleDateFormat("hh:mm a");
                    try {
                        Date inTime = sdf6.parse(sat_start_time_txt.getText().toString());
                        Date outTime = sdf6.parse(sat_end_time_txt.getText().toString());
                        if (outTime.before(inTime)) { //Same way you can check with after() method also.
                            sat_end_time_txt.setTextColor(getResources().getColor(R.color.search_boder));
                            sat_end_linear.setBackgroundResource(R.drawable.red_linear);
                            checkTime_sat = true;
                        } else {
                            checkTime_sat = false;
                            sat_end_time_txt.setTextColor(getResources().getColor(R.color.text_color));
                            sat_end_linear.setBackgroundResource(R.drawable.linear_shape);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
            }
        }

    }

}
