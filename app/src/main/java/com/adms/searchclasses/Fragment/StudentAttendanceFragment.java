package com.adms.searchclasses.Fragment;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.adms.searchclasses.Activites.DashBoardActivity;
import com.adms.searchclasses.Adapter.StudentAttendanceAdapter;
import com.adms.searchclasses.AppConstant.ApiHandler;
import com.adms.searchclasses.AppConstant.AppConfiguration;
import com.adms.searchclasses.AppConstant.Utils;
import com.adms.searchclasses.Model.Session.SessionDetailModel;
import com.adms.searchclasses.Model.Session.sessionDataModel;
import com.adms.searchclasses.Model.TeacherInfo.TeacherInfoModel;
import com.adms.searchclasses.R;
import com.adms.searchclasses.databinding.FragmentStudentAttendanceBinding;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;


public class StudentAttendanceFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    String monthDisplayStr, MonthInt;
    int mYear, mMonth, mDay;
    StudentAttendanceAdapter studentAttendanceAdapter;
    String ContactEnrollmentIDStr = "",dateStr, classTypeIDStr = "1", totalstudetnStr;//, SesionDetailIDStr, sessionDateStr, sessionTimeStr;
    SessionDetailModel dataResponse;
    TeacherInfoModel classListInfo;
    HashMap<Integer, String> spinnerClassMap;
    String classType = "";
    private FragmentStudentAttendanceBinding studentAttendanceBinding;
    private View rootView;
    private Context mContext;

    public StudentAttendanceFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        studentAttendanceBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_student_attendance, container, false);

        rootView = studentAttendanceBinding.getRoot();
//        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mContext = getActivity();
        ((DashBoardActivity) getActivity()).setActionBar(5, "true");
        initViews();
        setListners();

        return rootView;
    }

    //Use for initilize view
    public void initViews() {
        callSessionDetailApi(); }

    //Use for clcik event
    public void setListners() {
        studentAttendanceBinding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertAttendanceDetail();
                if (!ContactEnrollmentIDStr.equalsIgnoreCase("")){// && !classTypeIDStr.equalsIgnoreCase("")) {
                    callGetSessionStudentAttendanceApi();
                } else {
                    Utils.ping(mContext, "Select attendnance");
                }

            }
        });
        studentAttendanceBinding.classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String name = studentAttendanceBinding.classSpinner.getSelectedItem().toString();
                String getid = spinnerClassMap.get(studentAttendanceBinding.classSpinner.getSelectedItemPosition());

                Log.d("value", name + " " + getid);
                classTypeIDStr = getid.toString();
                Log.d("classTypeIDStr", classTypeIDStr);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    //Use for select Attendance date
    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
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

        MonthInt = d + "/" + m + "/" + y;

        studentAttendanceBinding.dateTxt.setText(MonthInt);
    }

    //Use for Get SessionStudentAttendace Detail
    public void callGetSessionStudentAttendanceApi() {
        if (Utils.isNetworkConnected(mContext)) {
            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_Add_ClassAttendance(getsessionStudentAttendanceDetail(), new retrofit.Callback<TeacherInfoModel>() {
                @Override
                public void success(TeacherInfoModel attendanceInfo, Response response) {
                    Utils.dismissDialog();
                    if (attendanceInfo == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (attendanceInfo.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (attendanceInfo.getSuccess().equalsIgnoreCase("false")) {
                        Utils.dismissDialog();
                        if (attendanceInfo.getData() != null) {
                            Utils.ping(mContext, getString(R.string.false_msg));
                        }
                        return;
                    }
                    if (attendanceInfo.getSuccess().equalsIgnoreCase("True")) {
                        Utils.dismissDialog();
                        if (classType.equalsIgnoreCase("")) {
                            Utils.ping(mContext, "Attendance added successfully");
                        } else {
                            Utils.ping(mContext, "Attendance updated successfully");
                        }
                        studentAttendanceBinding.submitBtn.setText("Update");
                        //callGetSessionStudentAttendanceApi();
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

    private Map<String, String> getsessionStudentAttendanceDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("ContactEnrollmentID", ContactEnrollmentIDStr);
        map.put("ClassTypeID", classTypeIDStr);
        map.put("AttanDate",dateStr);
        return map;
    }

    //Use for SessionDetail
    public void callSessionDetailApi() {
        if (Utils.isNetworkConnected(mContext)) {

//            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_SessionDetailBySessionID(getSessionDeatil(), new retrofit.Callback<SessionDetailModel>() {
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
                        Utils.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (sessionInfo.getSuccess().equalsIgnoreCase("True")) {
                        Utils.dismissDialog();
                        if (sessionInfo.getData().size() > 0) {
                            dataResponse = sessionInfo;
                            fillSessionData();

                        }
                        callClassAttendanceDetailApi();
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

    private Map<String, String> getSessionDeatil() {
        Map<String, String> map = new HashMap<>();
        map.put("CoachID", Utils.getPref(mContext, "coachID"));//coachIdStr
        map.put("SessionID", Utils.getPref(mContext, "sessionID"));
        return map;
    }

    //Use for fill Attendance data
    public void fillSessionData() {
        for (int i = 0; i < dataResponse.getData().size(); i++) {
            studentAttendanceBinding.boardTxt.setText(dataResponse.getData().get(i).getBoard());
            studentAttendanceBinding.standardTxt.setText(dataResponse.getData().get(i).getStandard() + "-" + dataResponse.getData().get(i).getStream());
            studentAttendanceBinding.subjectTxt.setText(dataResponse.getData().get(i).getLessionTypeName());
            studentAttendanceBinding.sessionNameTxt.setText(dataResponse.getData().get(i).getSessionName());
            studentAttendanceBinding.dateTxt.setText(AppConfiguration.DateStr);
            studentAttendanceBinding.timeTxt.setText(AppConfiguration.TimeStr);

        }
        DateConvert();
    }

    //Use for Get Insert Attendance detail
    public void InsertAttendanceDetail() {
        String responseString = "";

        ArrayList<String> newArray = new ArrayList<>();
        for (int i = 0; i < studentAttendanceAdapter.getCount(); i++) {
            sessionDataModel sessionInfoObj = studentAttendanceAdapter.getItem(i);
            int stuId = Integer.parseInt(sessionInfoObj.getContactEnrollmentID());
            boolean isEnable = false;
            String studentString = "";
            String status = sessionInfoObj.getStatus();
            if (status.equalsIgnoreCase("1")) {
                if (sessionInfoObj.getReason().equalsIgnoreCase("null")) {
                    sessionInfoObj.setReason("");
                }
                if (!isEnable) {
                    studentString = String.valueOf(stuId) + "@" + sessionInfoObj.getAttendanceID() + "@" + sessionInfoObj.getReason();
                    isEnable = true;
                } else {
                    studentString = studentString + "|" + String.valueOf(stuId) + "," + sessionInfoObj.getAttendanceID() + "@" + sessionInfoObj.getReason();
                }
            }
            newArray.add(studentString);
        }

        for (String s : newArray) {
            if (!s.equals("")) {
                responseString = responseString + "|" + s;
            }
        }
        if (!responseString.equalsIgnoreCase("")) {
            responseString = responseString.substring(1, responseString.length());
            Log.d("responseString ", responseString);

            ContactEnrollmentIDStr = responseString;
            Log.d("ContactEnrollmentIDStr ", ContactEnrollmentIDStr);
        }
    }

    //Use for ClassTypeDetail
    public void callClassDetailApi() {
        if (Utils.isNetworkConnected(mContext)) {

//            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_ClassTypeList(getClassDeatil(), new retrofit.Callback<TeacherInfoModel>() {
                @Override
                public void success(TeacherInfoModel classInfo, Response response) {
                    Utils.dismissDialog();
                    if (classInfo == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (classInfo.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (classInfo.getSuccess().equalsIgnoreCase("false")) {
                        Utils.dismissDialog();
                        Utils.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (classInfo.getSuccess().equalsIgnoreCase("True")) {
                        Utils.dismissDialog();
                        if (classInfo.getData().size() > 0) {
                            classListInfo = classInfo;
                            fillClassTypeSpinner();
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

    private Map<String, String> getClassDeatil() {
        Map<String, String> map = new HashMap<>();
        return map;
    }

    //Use for fill classtype
    public void fillClassTypeSpinner() {
        ArrayList<Integer> classTypeId = new ArrayList<Integer>();
        for (int i = 0; i < classListInfo.getData().size(); i++) {
            classTypeId.add(Integer.valueOf(classListInfo.getData().get(i).getClassTypeID()));
        }
        ArrayList<String> className = new ArrayList<String>();
        for (int j = 0; j < classListInfo.getData().size(); j++) {
            className.add(classListInfo.getData().get(j).getClassTypeName());
        }

        String[] spinnerclassIdArray = new String[classTypeId.size()];

        spinnerClassMap = new HashMap<Integer, String>();
        for (int i = 0; i < classTypeId.size(); i++) {
            spinnerClassMap.put(i, String.valueOf(classTypeId.get(i)));
            spinnerclassIdArray[i] = className.get(i).trim();
        }
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(studentAttendanceBinding.classSpinner);

            popupWindow.setHeight(spinnerclassIdArray.length > 4 ? 500 : spinnerclassIdArray.length * 100);
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }

        ArrayAdapter<String> adapterTerm = new ArrayAdapter<String>(mContext, R.layout.autocomplete_layout, spinnerclassIdArray);
        studentAttendanceBinding.classSpinner.setAdapter(adapterTerm);
        if (!classType.equalsIgnoreCase("")) {
            for (int m = 0; m < spinnerclassIdArray.length; m++) {
                if (classType.equalsIgnoreCase((spinnerclassIdArray[m]))) {
                    Log.d("spinnerValue", spinnerclassIdArray[m]);
                    int index = m;
                    Log.d("indexOf", String.valueOf(index));
                    studentAttendanceBinding.classSpinner.setSelection(m);
                }
            }
        } else {
            String classValue;
            if (AppConfiguration.SessionPrice.equalsIgnoreCase("0")) {
                classValue = "Free Class";
            } else {
                classValue = "Regular Class";
            }
            for (int m = 0; m < spinnerclassIdArray.length; m++) {
                if (classValue.equalsIgnoreCase((spinnerclassIdArray[m]))) {
                    Log.d("spinnerValue", spinnerclassIdArray[m]);
                    int index = m;
                    Log.d("indexOf", String.valueOf(index));
                    studentAttendanceBinding.classSpinner.setSelection(m);
                }

            }
        }
    }

    //Use for ClassAttendanceDetail
    public void callClassAttendanceDetailApi() {
        if (Utils.isNetworkConnected(mContext)) {

//            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_ClassAttendance(getClassAttendanceDeatil(), new retrofit.Callback<SessionDetailModel>() {
                @Override
                public void success(SessionDetailModel classattendanceInfo, Response response) {
                    Utils.dismissDialog();
                    if (classattendanceInfo == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (classattendanceInfo.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (classattendanceInfo.getSuccess().equalsIgnoreCase("False")) {
                        Utils.dismissDialog();
                        if (classattendanceInfo.getData().size() == 0) {

                            //callGetSessionStudentDetailApi();
                            Log.d("false", "hello");
                        }

                        return;
                    }
                    if (classattendanceInfo.getSuccess().equalsIgnoreCase("True")) {
                        Utils.dismissDialog();
                        if (classattendanceInfo.getData().size() > 0) {
                            dataResponse = classattendanceInfo;

                            if (classattendanceInfo.getData()!= null) {
                                studentAttendanceBinding.listLinear.setVisibility(View.VISIBLE);
                                studentAttendanceBinding.headerLinear.setVisibility(View.VISIBLE);
                                studentAttendanceBinding.submitBtn.setVisibility(View.VISIBLE);
                                studentAttendanceBinding.noRecordTxt.setVisibility(View.GONE);

                                for (int i = 0; i < classattendanceInfo.getData().size(); i++) {
                                    classattendanceInfo.getData().get(i).setStatus("1");
                                    if (classattendanceInfo.getData().get(i).getAttendanceID().equalsIgnoreCase("0")){
                                        classattendanceInfo.getData().get(i).setAttendanceID("1");
                                        studentAttendanceBinding.submitBtn.setText("SUBMIT");
                                    }else{
                                        studentAttendanceBinding.submitBtn.setText("UPDATE");
                                    }
                                }
                                totalstudetnStr = String.valueOf(classattendanceInfo.getData().size());
                                Log.d("totalStudent", totalstudetnStr);
                                studentAttendanceBinding.totalStudentTxt.setText(totalstudetnStr);
                                studentAttendanceAdapter = new StudentAttendanceAdapter(mContext, classattendanceInfo);
                                studentAttendanceBinding.studentListRcView.setAdapter(studentAttendanceAdapter);
                            } else {
                                studentAttendanceBinding.listLinear.setVisibility(View.GONE);
                                studentAttendanceBinding.headerLinear.setVisibility(View.GONE);
                                studentAttendanceBinding.submitBtn.setVisibility(View.GONE);
                                studentAttendanceBinding.noRecordTxt.setVisibility(View.VISIBLE);
                            }
                        }
                        callClassDetailApi();
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

    private Map<String, String> getClassAttendanceDeatil() {
        Map<String, String> map = new HashMap<>();
        map.put("SessionID", Utils.getPref(mContext, "sessionID"));
        map.put("AttanDate",dateStr);
        return map;
    }

    //Use for convert date
    public void DateConvert(){
        String inputPattern = "EEEE,MMM dd yyyy";
        String outputPattern = "dd/MM/yyyy";


        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);


        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(studentAttendanceBinding.dateTxt.getText().toString());
            str = outputFormat.format(date);

            dateStr=str;
            Log.i("mini", "Converted Date Today:" + str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}