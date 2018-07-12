package com.adms.classsafari.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adms.classsafari.Activites.DashBoardActivity;
import com.adms.classsafari.Activites.DrawableCalendarEvent;
import com.adms.classsafari.Adapter.SessionViewStudentListAdapter;
import com.adms.classsafari.AppConstant.ApiHandler;
import com.adms.classsafari.AppConstant.AppConfiguration;
import com.adms.classsafari.AppConstant.Utils;
import com.adms.classsafari.Interface.onViewClick;
import com.adms.classsafari.Model.Session.SessionDetailModel;
import com.adms.classsafari.Model.Session.sessionDataModel;
import com.adms.classsafari.Model.TeacherInfo.TeacherInfoModel;
import com.adms.classsafari.R;
import com.adms.classsafari.databinding.ConfirmSessionDialogBinding;
import com.adms.classsafari.databinding.FragmentCalendarBinding;
import com.adms.classsafari.databinding.SessiondetailConfirmationDialogBinding;
import com.github.tibolte.agendacalendarview.CalendarPickerController;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import com.github.tibolte.agendacalendarview.models.DayItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;


public class SessionFragment extends Fragment implements CalendarPickerController {

    public static SessionFragment fragment;
    public Dialog sessionDialog;
    FragmentCalendarBinding calendarBinding;
    ConfirmSessionDialogBinding confirmSessionDialogBinding;
    Button cancel_btn, add_attendance_btn, add_student_btn;
    String sessionnameStr, sessionstrattimeStr = "", sessionendtimeStr = "", sessionDateStr = "", sessionIDStr, sessionDetailIDStr, priceStr;
    TextView start_time_txt, end_time_txt, session_title_txt, date_txt, total_spot_txt, spot_left_txt, no_record_txt, edit_session_btn;
    RecyclerView studentnamelist_rcView;
    LinearLayout list_linear;
    SessionViewStudentListAdapter sessionViewStudentListAdapter;
    List<sessionDataModel> arrayList;
    SessionDetailModel finalsessionfullDetailModel;
    List<CalendarEvent> eventList = new ArrayList<>();
    ArrayList<Integer> colorList = new ArrayList<>();
    int sessionCapacity, arraySize, studentAvailability;
    String Address;
    int SessionHour = 0;
    Integer SessionMinit = 0;
    String flag;
    Calendar calendar;
    String dateStr;
    int k;
    ArrayList<Integer> totalHours;
    ArrayList<Integer> totalMinit;
    int avgHoursvalue, avgMinitvalue;
    String SessionDuration;
    //Use for PaymentConfirmation Dialog
    Dialog confimDialog;
    String contatIDstr, orderIDStr, familyNameStr;
    ArrayList<String> selectedId;
    SessiondetailConfirmationDialogBinding sessiondetailConfirmationDialogBinding;
    String hours, minit;
    SessionDetailModel dataResponse;
    private View rootView;
    private Context mContext;

    public SessionFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            calendarBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false);

            rootView = calendarBinding.getRoot();
//            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            setRetainInstance(true);
            mContext = getActivity();
            ((DashBoardActivity) getActivity()).setActionBar(0, "true");
            colorList.add(getResources().getColor(R.color.yellow_dark));
            colorList.add(getResources().getColor(R.color.green_dark));
            colorList.add(getResources().getColor(R.color.blue_dark));

            callGetSessionDetailApi();

        } else {

        }
        setTypeface();
        setListner();
        return rootView;
    }

    public void setTypeface() {
        Typeface custom_font = Typeface.createFromAsset(mContext.getAssets(), "font/TitilliumWeb-Regular.ttf");
        calendarBinding.monthYearTxt.setTypeface(custom_font);
    }


    public void init() {
        if (Utils.checkAndRequestPermissions(mContext)) {
        }
        Calendar minDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();

        minDate.add(Calendar.MONTH, -2);
        maxDate.add(Calendar.YEAR, 3);

        mockList(eventList);
        calendarBinding.agendaCalendarView.init(eventList, minDate, maxDate, Locale.US, this);
    }

    public void setListner() {
        calendarBinding.addEventFabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DashBoardActivity.navItemIndex = 1;
                Fragment fragment = new AddSessionFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle args = new Bundle();
                args.putString("flag", "Add");
                fragment.setArguments(args);
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public void onDaySelected(DayItem dayItem) {
        Log.d("CurrentDay", "" + dayItem);
    }

    @Override
    public void onEventSelected(CalendarEvent event) {
        if (!event.getTitle().equals("No events")) {
            totalHours = new ArrayList<>();
            totalMinit = new ArrayList<>();

            parseTodaysDate(String.valueOf(event.getStartTime().getTime()), String.valueOf(event.getEndTime().getTime()));

            sessionnameStr = event.getTitle();
            sessionDetailIDStr = String.valueOf(event.getId());
            Log.d("sessionDetailIDStr", sessionDetailIDStr);
            Utils.setPref(mContext, "sessionDetailID", sessionDetailIDStr);
            for (int i = 0; i < finalsessionfullDetailModel.getData().size(); i++) {
                for (int j = 0; j < finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().size(); j++) {
                    if (sessionDetailIDStr.equalsIgnoreCase(finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionDetailID())) {
                        sessionIDStr = finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionID();
                        sessionCapacity = Integer.parseInt(finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionCapacity());
                        priceStr = finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionPrice();
                        AppConfiguration.SessionLocation = finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getAddressLine1()
                                + ", " + finalsessionfullDetailModel.getData().get(i).getAddressLine2()
                                + ", " + finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getRegionName()
                                + ", " + finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getAddressCity()
                                + ", " + finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getAddressState()
                                + "- " + finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getAddressZipCode();


                        String[] spiltTime = finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionTime().split("\\-");
                        //calculateHours(spiltTime[0], spiltTime[1]);
                        AppConfiguration.SessionName = finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionName();
//                        if (SessionHour < 10) {
//                            hours= "0"+SessionHour;
//                        }else{
//                            hours= String.valueOf(SessionHour);
//                        }
//                        if(SessionMinit<10){
//                            minit="0"+SessionMinit;
//                        }else{
//                            minit=String.valueOf(SessionMinit);
//                        }
                        if (!finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getTotalRatingUser().equalsIgnoreCase("0")) {
                            AppConfiguration.SessionUserRating = "( " + finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getTotalRatingUser() + " )";
                        } else {
                            AppConfiguration.SessionUserRating = "( " + "0" + " )";
                        }
                        AppConfiguration.SessionRating = finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getRating();
                        AppConfiguration.SessionTime = finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionTime();
                        AppConfiguration.SessionPrice = String.valueOf(Math.round(Float.parseFloat(finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionPrice())));
//                        AppConfiguration.SessionPrice=finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionPrice();
                        AppConfiguration.SessionDate = finalsessionfullDetailModel.getData().get(i).getSessionDate();
                        AppConfiguration.RegionName = finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getRegionName();
                        Log.d("price + RegionName", AppConfiguration.SessionPrice + " " + AppConfiguration.RegionName);
                        Utils.setPref(mContext, "sessionID", sessionIDStr);
                    }

                }
            }

//            averageHours(totalHours);
//            averageMinit(totalMinit);
//            if(avgMinitvalue==0) {
//                AppConfiguration.SessionDuration =avgHoursvalue + " hr ";
//            }else{
//                AppConfiguration.SessionDuration =avgHoursvalue + " hr " + avgMinitvalue + " min";//+ " min"
//            }

            SessionDialog();
        } else {
//            Util.ping(mContext, "No Event Available...");
        }

    }

    @Override
    public void onScrollToDate(Calendar calendar) {
        Log.d("month", String.valueOf(calendar.getTime()));
        String inputPattern = "EEE MMM d HH:mm:ss zzz yyyy";
        String outputPattern = "MMMM yyyy";

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;
        try {
            date = inputFormat.parse(String.valueOf(calendar.getTime()));
            str = outputFormat.format(date);

            Log.i("mini", "Month:" + str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendarBinding.monthYearTxt.setText(str);
    }

    public String parseTodaysDate(String Starttime, String Endtime) {
        String inputPattern = "EEE MMM d HH:mm:ss zzz yyyy";
        String outputPattern = "EEEE,MMM dd yyyy";
        String outputTimePattern = "hh:mm a";

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        SimpleDateFormat outputFormat1 = new SimpleDateFormat(outputTimePattern);

        Date date = null, startdateTime = null, enddateTime = null;
        String str = null, StartTimeStr = null, EndTimeStr = null;

        try {
            date = inputFormat.parse(Starttime);
            str = outputFormat.format(date);

            startdateTime = inputFormat.parse(Starttime);
            StartTimeStr = outputFormat1.format(startdateTime);

            enddateTime = inputFormat.parse(Endtime);
            EndTimeStr = outputFormat1.format(enddateTime);

            sessionDateStr = str;
            sessionstrattimeStr = StartTimeStr;
            sessionendtimeStr = EndTimeStr;
            Log.i("mini", "Converted Date Today:" + StartTimeStr + "=" + EndTimeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public void SessionDialog() {
        sessionDialog = new Dialog(getActivity(), R.style.Theme_Dialog);
        Window window = sessionDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setAttributes(wlp);

        //sessionDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);
        sessionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        sessionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sessionDialog.setCancelable(false);
        sessionDialog.setContentView(R.layout.dialog_view_session);
        sessionDialog.show();

        cancel_btn = (Button) sessionDialog.findViewById(R.id.cancel_btn);
        add_attendance_btn = (Button) sessionDialog.findViewById(R.id.add_attendance_btn);
        edit_session_btn = (TextView) sessionDialog.findViewById(R.id.edit_session_btn);
        session_title_txt = (TextView) sessionDialog.findViewById(R.id.session_title_txt);
        start_time_txt = (TextView) sessionDialog.findViewById(R.id.start_time_txt);
        end_time_txt = (TextView) sessionDialog.findViewById(R.id.end_time_txt);
        date_txt = (TextView) sessionDialog.findViewById(R.id.date_txt);
        studentnamelist_rcView = (RecyclerView) sessionDialog.findViewById(R.id.student_name_list_rcView);
        list_linear = (LinearLayout) sessionDialog.findViewById(R.id.list_linear);
        add_student_btn = (Button) sessionDialog.findViewById(R.id.add_student_btn);
        total_spot_txt = (TextView) sessionDialog.findViewById(R.id.total_spot_txt);
        spot_left_txt = (TextView) sessionDialog.findViewById(R.id.spot_left_txt);
        no_record_txt = (TextView) sessionDialog.findViewById(R.id.no_record_txt);
        callGetSessionStudentDetailApi();

        date_txt.setText(sessionDateStr);
        session_title_txt.setText(sessionnameStr);
        start_time_txt.setText(sessionstrattimeStr);
        end_time_txt.setText(sessionendtimeStr);


        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionDialog.dismiss();
            }
        });

        edit_session_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DashBoardActivity.navItemIndex = 1;
                Fragment fragment = new AddSessionFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle args = new Bundle();
                args.putString("flag", flag);
                args.putString("sessionID", sessionIDStr);
                args.putString("studentAvailable", String.valueOf(arraySize));
                fragment.setArguments(args);
                fragmentTransaction.replace(R.id.frame, fragment);
//                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                sessionDialog.dismiss();
            }
        });

        add_attendance_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DashBoardActivity.navItemIndex = 1;
                AppConfiguration.DateStr = date_txt.getText().toString();
                AppConfiguration.TimeStr = start_time_txt.getText().toString() + "to" + end_time_txt.getText().toString();
                Fragment fragment = new StudentAttendanceFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle args = new Bundle();
                args.putString("session", "2");
                fragment.setArguments(args);
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.commit();
                sessionDialog.dismiss();
            }
        });

        add_student_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DashBoardActivity.navItemIndex = 1;
                Fragment fragment = new OldFamilyListFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle args = new Bundle();
                args.putString("session", "13");
                args.putString("sessionID", sessionIDStr);
                fragmentTransaction.replace(R.id.frame, fragment);
//                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                sessionDialog.dismiss();
            }
        });
    }


    //Use for Get AllSession Detail
    public void callGetSessionDetailApi() {
        if (Utils.isNetworkConnected(mContext)) {
            Utils.showDialog(mContext);
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
                            init();
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

    public void mockList(List<CalendarEvent> eventList) {
        long startDate = 0, endDate = 0;
        totalHours = new ArrayList<>();
        totalMinit = new ArrayList<>();
        for (int i = 0; i < finalsessionfullDetailModel.getData().size(); i++) {


            for (int j = 0; j < finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().size(); j++) {

                String[] spiltTime = finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionTime().split("\\-");
                try {
                    String dateString = finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionDate() + " " + spiltTime[0];
                    String enddateString = finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionDate() + " " + spiltTime[1];
                    Log.d("StartDate and EndDate :", dateString + " " + enddateString);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.US);//, Locale.getDefault());
                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.US);//, Locale.getDefault());
                    Date date = sdf.parse(dateString);
                    Date date1 = sdf1.parse(enddateString);

                    startDate = date.getTime();
                    endDate = date1.getTime();
                    Log.d("FirstTime", "first event :" + startDate + endDate);


                } catch (ParseException e) {
                    e.printStackTrace();
                }
                calculateHours(spiltTime[0], spiltTime[1]);
                if (k == 2) {
                    k = 0;
                } else {
                    k++;
                }

                Address = finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getAddressLine1()
                        + finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getAddressLine2()
                        + ", " + finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getRegionName()
                        + ", " + finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getAddressCity()
                        + ", " + finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getAddressState()
                        + " " + finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getAddressZipCode();
                DrawableCalendarEvent event = new DrawableCalendarEvent(Integer.parseInt(finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionDetailID()),
                        colorList.get(k), finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionName(),
                        finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionName(),
                        finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionTime()
                                + " " + "( " + SessionHour + " hr" + ", " + SessionMinit + " min )" + System.getProperty("line.separator")
                                + Address, startDate, endDate, 0, String.valueOf(SessionHour), R.drawable.email);
                eventList.add(event);

            }
        }
    }

    //Use for Get SessionStudent Detail
    public void callGetSessionStudentDetailApi() {
        if (Utils.isNetworkConnected(mContext)) {

            ApiHandler.getApiService().get_Session_StudentDetail(getsessionStudentDetail(), new retrofit.Callback<SessionDetailModel>() {
                @Override
                public void success(SessionDetailModel sessionStudentInfo, Response response) {
                    Utils.dismissDialog();
                    if (sessionStudentInfo == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (sessionStudentInfo.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (sessionStudentInfo.getSuccess().equalsIgnoreCase("false")) {
                        Utils.dismissDialog();
                        if (sessionStudentInfo.getData() != null) {
                            Utils.ping(mContext, getString(R.string.false_msg));
                        }
                        return;
                    }
                    if (sessionStudentInfo.getSuccess().equalsIgnoreCase("True")) {
                        Utils.dismissDialog();
                        arrayList = sessionStudentInfo.getData();

                        arraySize = arrayList.size();
                        studentAvailability = sessionCapacity - arraySize;
                        Log.d("capacity", "" + sessionCapacity + "arraySize :" + arraySize + "studentAvailability :" + studentAvailability);
                        if (sessionStudentInfo.getData().size() > 0) {
                            list_linear.setVisibility(View.VISIBLE);
                            no_record_txt.setVisibility(View.GONE);

                            sessionViewStudentListAdapter = new SessionViewStudentListAdapter(mContext, arrayList, new onViewClick() {
                                @Override
                                public void getViewClick() {
                                    getFamilyID();
//                                    ConformationDialog();
                                    SessionConfirmationDialog();
                                }
                            });
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                            studentnamelist_rcView.setLayoutManager(mLayoutManager);
                            studentnamelist_rcView.setItemAnimator(new DefaultItemAnimator());
                            studentnamelist_rcView.setAdapter(sessionViewStudentListAdapter);

                        } else {
                            list_linear.setVisibility(View.GONE);
                            no_record_txt.setVisibility(View.VISIBLE);
                        }
                        total_spot_txt.setText(String.valueOf(sessionCapacity));
                        spot_left_txt.setText(String.valueOf(studentAvailability));
                        if (arraySize > 0) {
                            add_attendance_btn.setEnabled(true);
                            add_attendance_btn.setAlpha(1);
                        } else {
                            add_attendance_btn.setEnabled(false);
                            add_attendance_btn.setAlpha(0.5f);
                        }
                        if (studentAvailability > 0) {
                            add_student_btn.setEnabled(true);
                            add_student_btn.setAlpha(1);
                        } else {
                            add_student_btn.setEnabled(false);
                            add_student_btn.setAlpha(0.5f);
                        }
                        if (arraySize == 0) {
                            edit_session_btn.setText("Edit");
                            flag = "edit";
                        } else {
                            edit_session_btn.setText("View");
                            flag = "view";
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

    private Map<String, String> getsessionStudentDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("SessionID", sessionIDStr);
        return map;
    }

    //Use for GetSession Report
    public void callSessioncapacityApi() {
        if (Utils.isNetworkConnected(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_Check_SpotAvailability_By_SessionID(getSessioncapacityDetail(), new retrofit.Callback<TeacherInfoModel>() {
                @Override
                public void success(TeacherInfoModel sessionModel, Response response) {
                    Utils.dismissDialog();
                    if (sessionModel == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (sessionModel.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (sessionModel.getSuccess().equalsIgnoreCase("false")) {
                        new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AppTheme))
                                .setIcon(mContext.getResources().getDrawable(R.drawable.safari))
                                .setMessage(getResources().getString(R.string.fail_msg))
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setIcon(R.drawable.safari)
                                .show();
                        return;
                    }
                    if (sessionModel.getSuccess().equalsIgnoreCase("True")) {
                        Utils.dismissDialog();
                        AppConfiguration.TeacherSessionIdStr = sessionIDStr;
                        AppConfiguration.TeacherSessionContactIdStr = contatIDstr;
                        if (!contatIDstr.equalsIgnoreCase("") && !sessionIDStr.equalsIgnoreCase("")) {
                            callpaymentRequestApi();
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

    private Map<String, String> getSessioncapacityDetail() {

        Map<String, String> map = new HashMap<>();
        map.put("SessionID", sessionIDStr);//contatIDstr  //Utils.getPref(mContext, "coachID")
        return map;
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
            totalHours.add(SessionHour);
            totalMinit.add(SessionMinit);
            Log.i("======= Hours", " :: " + hours + ":" + min);

            if (SessionMinit > 0) {
                if (SessionMinit < 10) {
                    SessionDuration = SessionHour + ":" + "0" + SessionMinit + " hrs";
                } else {
                    SessionDuration = SessionHour + ":" + SessionMinit + " hrs";
                }
            } else {
                SessionDuration = SessionHour + ":" + "00" + " hrs";
            }

        } catch (ParseException e) {
            e.printStackTrace();
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

    public void SessionConfirmationDialog() {
        sessiondetailConfirmationDialogBinding = DataBindingUtil.
                inflate(LayoutInflater.from(mContext), R.layout.sessiondetail_confirmation_dialog, (ViewGroup) calendarBinding.getRoot(), false);
        confimDialog = new Dialog(mContext, R.style.Theme_Dialog);
        Window window = confimDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        confimDialog.getWindow().getAttributes().verticalMargin = 0.20f;
        wlp.gravity = Gravity.TOP;
        window.setAttributes(wlp);

        // confimDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);
        confimDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        confimDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        confimDialog.setCancelable(false);
        confimDialog.setContentView(sessiondetailConfirmationDialogBinding.getRoot());
        callEditSessionApi();
        sessiondetailConfirmationDialogBinding.tutorNameTxt.setText(Utils.getPref(mContext, "RegisterUserName"));
        sessiondetailConfirmationDialogBinding.sessionNameTxt.setText(AppConfiguration.SessionName);
        sessiondetailConfirmationDialogBinding.locationTxt.setText(AppConfiguration.RegionName);
        sessiondetailConfirmationDialogBinding.durationTxt.setText(AppConfiguration.SessionDuration);

        if (AppConfiguration.SessionPrice.equalsIgnoreCase("0")) {
            sessiondetailConfirmationDialogBinding.priceTxt.setText("Free");
        } else {
            sessiondetailConfirmationDialogBinding.priceTxt.setText("₹" + AppConfiguration.SessionPrice);
        }
        sessiondetailConfirmationDialogBinding.ratingBar.setRating(Float.parseFloat(AppConfiguration.SessionRating));
        if (AppConfiguration.SessionUserRating.equalsIgnoreCase("( 0 )")) {
            sessiondetailConfirmationDialogBinding.ratingUserTxt.setText("");
        } else {
            sessiondetailConfirmationDialogBinding.ratingUserTxt.setText(AppConfiguration.SessionUserRating);
        }
        //  String[] spiltTime = AppConfiguration.SessionTime.split("\\-");
        AppConfiguration.UserName = familyNameStr;


        sessiondetailConfirmationDialogBinding.cancelTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confimDialog.dismiss();
            }
        });
        sessiondetailConfirmationDialogBinding.confirmTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callSessioncapacityApi();
                confimDialog.dismiss();
                sessionDialog.dismiss();

            }
        });
        confimDialog.show();

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
                            for (int i = 0; i < dataResponse.getData().size(); i++) {
                                sessiondetailConfirmationDialogBinding.startDateTxt.setText(dataResponse.getData().get(i).getStartDate());
                                sessiondetailConfirmationDialogBinding.endDateTxt.setText(dataResponse.getData().get(i).getEndDate());


                                String[] spiltPipes = dataResponse.getData().get(i).getSchedule().split("\\|");
                                String[] spiltComma;
                                String[] spiltDash;
                                Log.d("spilt", "" + spiltPipes.toString());
                                for (int j = 0; j < spiltPipes.length; j++) {
                                    spiltComma = spiltPipes[j].split("\\,");
                                    spiltDash = spiltComma[1].split("\\-");
                                    calculateHours(spiltDash[0], spiltDash[1]);

//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//
//        SimpleDateFormat outsdf = new SimpleDateFormat("EEE");
//        Date date = null;
//        String inputstr = null;
//        try {
//            date = sdf.parse(AppConfiguration.SessionDate);
//            inputstr = outsdf.format(date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        Log.d("DateTime", inputstr);
                                    switch (spiltComma[0]) {
                                        case "sun":
                                            sessiondetailConfirmationDialogBinding.sunTimeTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.sundayBtn.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.sunTimeTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.sundayBtn.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.sunTimeTxt.setText(spiltDash[0]);
                                            sessiondetailConfirmationDialogBinding.sunHoursTxt.setText(SessionDuration);
                                            sessiondetailConfirmationDialogBinding.sunHoursTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.sunHoursTxt.setAlpha(1);
                                            break;
                                        case "mon":
                                            sessiondetailConfirmationDialogBinding.monTimeTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.mondayBtn.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.monTimeTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.mondayBtn.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.monTimeTxt.setText(spiltDash[0]);
                                            sessiondetailConfirmationDialogBinding.monHoursTxt.setText(SessionDuration);
                                            sessiondetailConfirmationDialogBinding.monHoursTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.monHoursTxt.setAlpha(1);
                                            break;
                                        case "tue":
                                            sessiondetailConfirmationDialogBinding.tuesTimeTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.tuesdayBtn.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.tuesTimeTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.tuesdayBtn.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.tuesTimeTxt.setText(spiltDash[0]);
                                            sessiondetailConfirmationDialogBinding.tuesHoursTxt.setText(SessionDuration);
                                            sessiondetailConfirmationDialogBinding.tuesHoursTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.tuesHoursTxt.setAlpha(1);
                                            break;
                                        case "wed":
                                            sessiondetailConfirmationDialogBinding.wedTimeTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.wednesdayBtn.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.wedTimeTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.wednesdayBtn.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.wedTimeTxt.setText(spiltDash[0]);
                                            sessiondetailConfirmationDialogBinding.wedHoursTxt.setText(SessionDuration);
                                            sessiondetailConfirmationDialogBinding.wedHoursTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.wedHoursTxt.setAlpha(1);
                                            break;
                                        case "thu":
                                            sessiondetailConfirmationDialogBinding.thurTimeTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.thursdayBtn.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.thurTimeTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.thursdayBtn.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.thurTimeTxt.setText(spiltDash[0]);
                                            sessiondetailConfirmationDialogBinding.thurHoursTxt.setText(SessionDuration);
                                            sessiondetailConfirmationDialogBinding.thurHoursTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.thurHoursTxt.setAlpha(1);
                                            break;
                                        case "fri":
                                            sessiondetailConfirmationDialogBinding.friTimeTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.fridayBtn.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.friTimeTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.fridayBtn.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.friTimeTxt.setText(spiltDash[0]);
                                            sessiondetailConfirmationDialogBinding.friHoursTxt.setText(SessionDuration);
                                            sessiondetailConfirmationDialogBinding.friHoursTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.friHoursTxt.setAlpha(1);
                                            break;
                                        case "sat":
                                            sessiondetailConfirmationDialogBinding.satTimeTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.saturdayBtn.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.satTimeTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.saturdayBtn.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.satTimeTxt.setText(spiltDash[0]);
                                            sessiondetailConfirmationDialogBinding.satHoursTxt.setText(SessionDuration);
                                            sessiondetailConfirmationDialogBinding.satHoursTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.satHoursTxt.setAlpha(1);
                                            break;
                                        default:
                                    }
                                }
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

    private Map<String, String> getEditSessionDeatil() {
        Map<String, String> map = new HashMap<>();
        map.put("CoachID", Utils.getPref(mContext, "coachID"));//coachIdStr
        map.put("SessionID", sessionIDStr);
        return map;
    }

    //Use for paymentRequest
    public void callpaymentRequestApi() {
        if (Utils.isNetworkConnected(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_GeneratePaymentRequest(getpaymentRequestdetail(), new retrofit.Callback<TeacherInfoModel>() {
                @Override
                public void success(TeacherInfoModel paymentRequestModel, Response response) {
                    Utils.dismissDialog();
                    if (paymentRequestModel == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (paymentRequestModel.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (paymentRequestModel.getSuccess().equalsIgnoreCase("false")) {
                        Utils.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (paymentRequestModel.getSuccess().equalsIgnoreCase("True")) {

                        orderIDStr = paymentRequestModel.getOrderID();

                        Fragment fragment = new PaymentFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        Bundle args = new Bundle();
                        args.putString("orderID", orderIDStr);
                        args.putString("amount", AppConfiguration.SessionPrice);
                        args.putString("mode", AppConfiguration.Mode);
                        args.putString("username", familyNameStr);
                        args.putString("sessionID", sessionIDStr);
                        args.putString("contactID", contatIDstr);
                        args.putString("type", Utils.getPref(mContext, "Type"));
                        fragment.setArguments(args);
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

    private Map<String, String> getpaymentRequestdetail() {
        Map<String, String> map = new HashMap<>();
        map.put("ContactID", contatIDstr);
        map.put("SessionID", sessionIDStr);
        map.put("Amount", AppConfiguration.SessionPrice);
        return map;
    }

    public void getFamilyID() {
        selectedId = new ArrayList<String>();

        selectedId = sessionViewStudentListAdapter.getContactID();
        Log.d("selectedId", "" + selectedId);
        for (int i = 0; i < selectedId.size(); i++) {
            String[] spiltValue = selectedId.get(i).split("\\|");
            contatIDstr = spiltValue[0];
            familyNameStr = spiltValue[1] + " " + spiltValue[2];
            Log.d("selectedIdStr", contatIDstr);
        }
        AppConfiguration.UserName = familyNameStr;
    }
}

