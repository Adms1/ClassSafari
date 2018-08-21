package com.adms.searchclasses.Fragment;

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

import com.adms.searchclasses.Activites.DashBoardActivity;
import com.adms.searchclasses.Activites.DrawableCalendarEvent;
import com.adms.searchclasses.Adapter.SessionViewStudentListAdapter;
import com.adms.searchclasses.AppConstant.ApiHandler;
import com.adms.searchclasses.AppConstant.AppConfiguration;
import com.adms.searchclasses.AppConstant.Utils;
import com.adms.searchclasses.Interface.onViewClick;
import com.adms.searchclasses.Model.Session.SessionDetailModel;
import com.adms.searchclasses.Model.Session.sessionDataModel;
import com.adms.searchclasses.Model.TeacherInfo.TeacherInfoModel;
import com.adms.searchclasses.R;
import com.adms.searchclasses.databinding.DialogViewSessionBinding;
import com.adms.searchclasses.databinding.FragmentCalendarBinding;
import com.adms.searchclasses.databinding.SessiondetailConfirmationDialogBinding;
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
    DialogViewSessionBinding dialogViewSessionBinding;
    String sessionnameStr, sessionstrattimeStr = "", sessionendtimeStr = "", sessionDateStr = "", sessionIDStr, sessionDetailIDStr, priceStr;;
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
    int k;
    ArrayList<Integer> totalHours;
    ArrayList<Integer> totalMinit;
    String SessionDuration;
    //Use for PaymentConfirmation Dialog
    Dialog confimDialog;
    String contatIDstr, orderIDStr, familyNameStr;
    ArrayList<String> selectedId;
    SessiondetailConfirmationDialogBinding sessiondetailConfirmationDialogBinding;
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

        setListner();
        return rootView;
    }

    //Use for initlize view
    public void init() {
//        if (Utils.checkAndRequestPermissions(mContext)) {
//        }
        Calendar minDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();

        minDate.add(Calendar.MONTH, -2);
        maxDate.add(Calendar.YEAR, 3);

        mockList(eventList);
        calendarBinding.agendaCalendarView.init(eventList, minDate, maxDate, Locale.US, this);
    }

    //Use for Click Event
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

    //Use for Day selection
    @Override
    public void onDaySelected(DayItem dayItem) {
        Log.d("CurrentDay", "" + dayItem);
    }

    //Use for select class
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
                        AppConfiguration.SessionName = finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionName();

                        if (!finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getTotalRatingUser().equalsIgnoreCase("0")) {
                            AppConfiguration.SessionUserRating = "( " + finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getTotalRatingUser() + " )";
                        } else {
                            AppConfiguration.SessionUserRating = "( " + "0" + " )";
                        }
                        AppConfiguration.SessionRating = finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getRating();
                        AppConfiguration.SessionTime = finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionTime();
                        AppConfiguration.SessionPrice = String.valueOf(Math.round(Float.parseFloat(finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionPrice())));
                        AppConfiguration.SessionDate = finalsessionfullDetailModel.getData().get(i).getSessionDate();
                        AppConfiguration.RegionName = finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getRegionName();
                        Log.d("price + RegionName", AppConfiguration.SessionPrice + " " + AppConfiguration.RegionName);
                        Utils.setPref(mContext, "sessionID", sessionIDStr);
                    }

                }
            }
            SessionDialog();
        } else {
//            Util.ping(mContext, "No Event Available...");
        }

    }

    //Use for get scrolldate
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

    //Use for convert date as per our requirement
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

    //Use for get class and student detail
    public void SessionDialog() {
        dialogViewSessionBinding = DataBindingUtil.
                inflate(LayoutInflater.from(mContext), R.layout.dialog_view_session,null, false);

        sessionDialog = new Dialog(mContext, R.style.Theme_Dialog);
        Window window = sessionDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        sessionDialog.getWindow().getAttributes().verticalMargin = 0.0f;
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);

        // changeDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);
        sessionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        sessionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sessionDialog.setCancelable(false);
        sessionDialog.setContentView(dialogViewSessionBinding.getRoot());

        sessionDialog.show();
        callGetSessionStudentDetailApi();

        dialogViewSessionBinding.dateTxt.setText(sessionDateStr);
        dialogViewSessionBinding.sessionTitleTxt.setText(sessionnameStr);
        dialogViewSessionBinding.startTimeTxt.setText(sessionstrattimeStr);
        dialogViewSessionBinding.endTimeTxt.setText(sessionendtimeStr);


        dialogViewSessionBinding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionDialog.dismiss();
            }
        });

        dialogViewSessionBinding.editSessionBtn.setOnClickListener(new View.OnClickListener() {
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

        dialogViewSessionBinding.addAttendanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DashBoardActivity.navItemIndex = 1;
                AppConfiguration.DateStr = dialogViewSessionBinding.dateTxt.getText().toString();
                AppConfiguration.TimeStr = dialogViewSessionBinding.startTimeTxt.getText().toString() + " to " + dialogViewSessionBinding.endTimeTxt.getText().toString();
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

        dialogViewSessionBinding.addStudentBtn.setOnClickListener(new View.OnClickListener() {
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

    //Use for add class in calender
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
                                + " " + "( " + SessionDuration + " )" + System.getProperty("line.separator")
                                + Address, startDate, endDate, 0, String.valueOf(SessionHour), R.drawable.location);
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
                        int count = 0;
                        for (int i = 0; i < arrayList.size(); i++) {
                            if (arrayList.get(i).getIsActive().equalsIgnoreCase("1")) {
                                count++;
                            }
                        }
                        Log.d("count", "" + count);
                        arraySize = arrayList.size();
                        studentAvailability = sessionCapacity - count;
                        Log.d("capacity", "" + sessionCapacity + "arraySize :" + arraySize + "studentAvailability :" + studentAvailability);
                        if (sessionStudentInfo.getData().size() > 0) {
                            dialogViewSessionBinding.listLinear.setVisibility(View.VISIBLE);
                            dialogViewSessionBinding.noRecordTxt.setVisibility(View.GONE);

                            sessionViewStudentListAdapter = new SessionViewStudentListAdapter(mContext, arrayList, new onViewClick() {
                                @Override
                                public void getViewClick() {
                                    getFamilyID();
//                                    ConformationDialog();
                                    SessionConfirmationDialog();
                                }
                            });
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                            dialogViewSessionBinding.studentNameListRcView.setLayoutManager(mLayoutManager);
                            dialogViewSessionBinding.studentNameListRcView.setItemAnimator(new DefaultItemAnimator());
                            dialogViewSessionBinding.studentNameListRcView.setAdapter(sessionViewStudentListAdapter);

                        } else {
                            dialogViewSessionBinding.listLinear.setVisibility(View.GONE);
                            dialogViewSessionBinding.noRecordTxt.setVisibility(View.VISIBLE);
                        }
                        dialogViewSessionBinding.totalSpotTxt.setText(String.valueOf(sessionCapacity));
                        dialogViewSessionBinding.spotLeftTxt.setText(String.valueOf(studentAvailability));
                        if (arraySize > 0) {
                            dialogViewSessionBinding.addAttendanceBtn.setEnabled(true);
                            dialogViewSessionBinding.addAttendanceBtn.setAlpha(1);
                        } else {
                            dialogViewSessionBinding.addAttendanceBtn.setEnabled(false);
                            dialogViewSessionBinding.addAttendanceBtn.setAlpha(0.5f);
                        }
                        if (studentAvailability > 0) {
                            dialogViewSessionBinding.addStudentBtn.setEnabled(true);
                            dialogViewSessionBinding.addStudentBtn.setAlpha(1);
                        } else {
                            dialogViewSessionBinding.addStudentBtn.setEnabled(false);
                            dialogViewSessionBinding.addStudentBtn.setAlpha(0.5f);
                        }
                        if (arraySize == 0) {
                            dialogViewSessionBinding.editSessionBtn.setText("Edit");
                            flag = "edit";
                        } else {
                            dialogViewSessionBinding.editSessionBtn.setText("View");
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
                                .setMessage(getResources().getString(R.string.fail_msg))
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
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

    //Use for calculate class time
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

    //Use for class confirmation
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

    //Use for get selected student detail
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

