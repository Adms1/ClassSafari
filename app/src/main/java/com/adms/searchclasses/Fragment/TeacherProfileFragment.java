package com.adms.searchclasses.Fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.Uri;
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
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.adms.searchclasses.Activites.DashBoardActivity;
import com.adms.searchclasses.Activites.RegistrationActivity;
import com.adms.searchclasses.AppConstant.ApiHandler;
import com.adms.searchclasses.AppConstant.Utils;
import com.adms.searchclasses.Model.Session.SessionDetailModel;
import com.adms.searchclasses.Model.TeacherInfo.TeacherInfoModel;
import com.adms.searchclasses.R;
import com.adms.searchclasses.databinding.FragmentBankDetailBinding;
import com.adms.searchclasses.databinding.FragmentTeacherProfileBinding;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.internal.Util;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class TeacherProfileFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    FragmentTeacherProfileBinding teacherProfileBinding;
    String firstNameStr, lastNameStr, classNameStr, phoneNumberStr, dobStr, genderIdStr = "", yearStr = "0", monthStr = "0", qualificationStr = "", aboutusStr = "";
    TeacherInfoModel teacherInfoResponse;
    String finalDate;
    int Year, Month, Day;
    Calendar calendar;
    int mYear, mMonth, mDay;
    TeacherInfoModel qualificationResponse;
    ArrayList<String> yearofExperiance;
    ArrayList<String> monthofExperiance;
    private View rootView;
    private Context mContext;
    private DatePickerDialog datePickerDialog;

    public TeacherProfileFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        teacherProfileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_teacher_profile, container, false);
        rootView = teacherProfileBinding.getRoot();
//        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mContext = getActivity();
        ((DashBoardActivity) getActivity()).setActionBar(3, "true");

        init();
        setListner();
        return rootView;
    }


    public void init() {
        calendar = Calendar.getInstance();
        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);
        callTeacherProfileApi();
        callQualificationApi();

    }

    public void setListner() {
        teacherProfileBinding.dateOfBirthEdt.setOnClickListener(this);
        teacherProfileBinding.registerBtn.setOnClickListener(this);
        teacherProfileBinding.genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int radioButtonId = teacherProfileBinding.genderGroup.getCheckedRadioButtonId();
                switch (radioButtonId) {
                    case R.id.male_chk:
                        genderIdStr = teacherProfileBinding.maleChk.getTag().toString();
                        break;
                    case R.id.female_chk:
                        genderIdStr = teacherProfileBinding.femaleChk.getTag().toString();
                        break;
                }
            }
        });
        teacherProfileBinding.yearOfSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String name = teacherProfileBinding.yearOfSpinner.getSelectedItem().toString();
                Log.d("value", name);
                String[] stryear = name.split("\\s+");
                name = stryear[0];


                if (name.equalsIgnoreCase("30+")) {
                    yearStr = "30";
                } else {
                    yearStr = name;
                }
                Log.d("yearStr", yearStr);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        teacherProfileBinding.monthOfSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String name = teacherProfileBinding.monthOfSpinner.getSelectedItem().toString();
                Log.d("value", name);
                String[] strmonth = name.split("\\s+");
                name = strmonth[0];
                monthStr = name;
                Log.d("monthStr", monthStr);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    //Use for Update Teacher detail
    public void callUpdateProfileApi() {
        if (Utils.isNetworkConnected(mContext)) {
//            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_Update_Teacher(getUpdateTeacherdetail(), new retrofit.Callback<TeacherInfoModel>() {
                @Override
                public void success(TeacherInfoModel teacherUpdateInfoModel, Response response) {
                    Utils.dismissDialog();
                    if (teacherUpdateInfoModel == null) {
                        Utils.dismissDialog();
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (teacherUpdateInfoModel.getSuccess() == null) {
                        Utils.dismissDialog();
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (teacherUpdateInfoModel.getSuccess().equalsIgnoreCase("false")) {
                        Utils.dismissDialog();
                        Utils.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (teacherUpdateInfoModel.getSuccess().equalsIgnoreCase("True")) {
                        Utils.dismissDialog();
                        Utils.ping(mContext, getString(R.string.updateteacher));
                        Utils.setPref(mContext, "ClassName", classNameStr);
                        Utils.setPref(mContext, "RegisterUserName", firstNameStr + " " + lastNameStr);
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

    private Map<String, String> getUpdateTeacherdetail() {
        Map<String, String> map = new HashMap<>();
        map.put("Coach_ID", Utils.getPref(mContext, "coachID"));
        map.put("FirstName", firstNameStr);
        map.put("LastName", lastNameStr);
        map.put("EmailAddress", Utils.getPref(mContext, "RegisterEmail"));
        map.put("GenderID", genderIdStr);
        map.put("DateOfBirth", dobStr);
        map.put("PhoneNumber", phoneNumberStr);
        map.put("ClassName", classNameStr);
        map.put("Year", yearStr);
        map.put("Month", monthStr);
        map.put("Qualification", qualificationStr);
        map.put("AboutUs", aboutusStr);
        return map;
    }


    //Use for Get Teacher detail
    public void callTeacherProfileApi() {
        if (Utils.isNetworkConnected(mContext)) {
//            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_TeacherContactDetail_Coach_ID(getTeacherdetail(), new retrofit.Callback<TeacherInfoModel>() {
                @Override
                public void success(TeacherInfoModel teacherInfoModel, Response response) {
                    Utils.dismissDialog();
                    if (teacherInfoModel == null) {
                        Utils.dismissDialog();
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (teacherInfoModel.getSuccess() == null) {
                        Utils.dismissDialog();
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (teacherInfoModel.getSuccess().equalsIgnoreCase("false")) {
                        Utils.dismissDialog();
                        Utils.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (teacherInfoModel.getSuccess().equalsIgnoreCase("True")) {
                        teacherInfoResponse = teacherInfoModel;
                        Utils.dismissDialog();
                        setTeacherData();
                        fillExperience();
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

    private Map<String, String> getTeacherdetail() {
        Map<String, String> map = new HashMap<>();
        map.put("Coach_ID", Utils.getPref(mContext, "coachID"));
        return map;
    }

    //Use for Qualification
    public void callQualificationApi() {
        if (Utils.isNetworkConnected(mContext)) {

//            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_Qualification_AutoComplate(getQualificationDeatil(), new retrofit.Callback<TeacherInfoModel>() {
                @Override
                public void success(TeacherInfoModel qualificationInfo, Response response) {
                    Utils.dismissDialog();
                    if (qualificationInfo == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (qualificationInfo.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (qualificationInfo.getSuccess().equalsIgnoreCase("false")) {
                        Utils.dismissDialog();
                        Utils.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (qualificationInfo.getSuccess().equalsIgnoreCase("True")) {
                        Utils.dismissDialog();
                        if (qualificationInfo.getData().size() > 0) {
                            qualificationResponse = qualificationInfo;
                            fillQualification();
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

    private Map<String, String> getQualificationDeatil() {
        Map<String, String> map = new HashMap<>();
        map.put("Coach_ID", Utils.getPref(mContext, "coachID"));
        return map;
    }

    public void fillQualification() {
        ArrayList<String> QualificationName = new ArrayList<String>();
        for (int j = 0; j < qualificationResponse.getData().size(); j++) {
            QualificationName.add(qualificationResponse.getData().get(j).getQualification());
        }
        ArrayAdapter<String> adapterTerm = new ArrayAdapter<String>(mContext, R.layout.autocomplete_layout, QualificationName);
        teacherProfileBinding.qualificationEdt.setThreshold(1);
        teacherProfileBinding.qualificationEdt.setAdapter(adapterTerm);
    }

    public void fillExperience() {
        yearofExperiance = new ArrayList<>();
        monthofExperiance = new ArrayList<>();

        monthofExperiance.add("Experience(month)");
        for (int i = 0; i <= 12; i++) {
            monthofExperiance.add(String.valueOf(i) + " " + "months");
        }
        yearofExperiance.add("Experience(year)");
        for (int i = 0; i <= 30; i++) {
            if (i == 30) {
                yearofExperiance.add(String.valueOf(i) + "+" + " " + "years");
            } else {
                yearofExperiance.add(String.valueOf(i) + " " + "years");
            }
        }

        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(teacherProfileBinding.monthOfSpinner);

            popupWindow.setHeight(monthofExperiance.size() > 5 ? 500 : monthofExperiance.size() * 70);
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }
        ArrayAdapter<String> adaptermonth = new ArrayAdapter<String>(mContext, R.layout.spinner_layout, monthofExperiance);
        teacherProfileBinding.monthOfSpinner.setAdapter(adaptermonth);

        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(teacherProfileBinding.yearOfSpinner);

            popupWindow.setHeight(yearofExperiance.size() > 5 ? 500 : yearofExperiance.size() * 70);
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }
        ArrayAdapter<String> adapterYear = new ArrayAdapter<String>(mContext, R.layout.spinner_layout, yearofExperiance);
        teacherProfileBinding.yearOfSpinner.setAdapter(adapterYear);
        for (int i = 1; i < yearofExperiance.size(); i++) {
            String[] splityear = yearofExperiance.get(i).split("\\s+");
            if (teacherInfoResponse.getData().get(0).getExpYear().equalsIgnoreCase(splityear[0])) {
                teacherProfileBinding.yearOfSpinner.setSelection(i);
                break;
            } else {
                teacherProfileBinding.yearOfSpinner.setSelection(0);
            }
        }
        for (int i = 1; i < monthofExperiance.size(); i++) {
            String[] splitmonth = monthofExperiance.get(i).split("\\s+");
            if (teacherInfoResponse.getData().get(0).getExpMonth().equalsIgnoreCase(splitmonth[0])) {
                teacherProfileBinding.monthOfSpinner.setSelection(i);
                break;
            } else {
                teacherProfileBinding.monthOfSpinner.setSelection(0);
            }
        }


    }

    public void getInsertValue() {
        firstNameStr = teacherProfileBinding.firstNameEdt.getText().toString();
        lastNameStr = teacherProfileBinding.lastNameEdt.getText().toString();
        dobStr = teacherProfileBinding.dateOfBirthEdt.getText().toString();
        phoneNumberStr = teacherProfileBinding.phoneNoEdt.getText().toString();
        classNameStr = teacherProfileBinding.classNameEdt.getText().toString();
        aboutusStr = teacherProfileBinding.aboutUsEdt.getText().toString();
        qualificationStr = teacherProfileBinding.qualificationEdt.getText().toString();
        if (yearStr.equalsIgnoreCase("Experience(year)")) {
            yearStr = "0";
        }
        if (monthStr.equalsIgnoreCase("Experience(month)")) {
            monthStr = "0";
        }
    }

    public void setTeacherData() {
        for (int i = 0; i < teacherInfoResponse.getData().size(); i++) {
            teacherProfileBinding.firstNameEdt.setText(teacherInfoResponse.getData().get(i).getFirstName());
            teacherProfileBinding.lastNameEdt.setText(teacherInfoResponse.getData().get(i).getLastName());
            teacherProfileBinding.classNameEdt.setText(teacherInfoResponse.getData().get(i).getClassName());
            teacherProfileBinding.phoneNoEdt.setText(teacherInfoResponse.getData().get(i).getMobile());
            String[] date = teacherInfoResponse.getData().get(i).getDOB().split("\\s+");
            teacherProfileBinding.dateOfBirthEdt.setText(date[0]);
            genderIdStr = String.valueOf(teacherInfoResponse.getData().get(i).getGender());
            if (teacherInfoResponse.getData().get(i).getGender() == 1) {
                teacherProfileBinding.maleChk.setChecked(true);
            } else {
                teacherProfileBinding.femaleChk.setChecked(true);
            }
            teacherProfileBinding.aboutUsEdt.setText(teacherInfoResponse.getData().get(i).getAboutUs());
            teacherProfileBinding.qualificationEdt.setText(teacherInfoResponse.getData().get(i).getQualification());

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_btn:
                getInsertValue();
                if (!firstNameStr.equalsIgnoreCase("")) {
                    if (!lastNameStr.equalsIgnoreCase("")) {
                        if (!classNameStr.equalsIgnoreCase("")) {
                            if (!phoneNumberStr.equalsIgnoreCase("")) {
                                if (!dobStr.equalsIgnoreCase("")) {
                                    if (!genderIdStr.equalsIgnoreCase("")) {
                                        callUpdateProfileApi();
                                    } else {
                                        Utils.ping(mContext, "Please select gender");
                                    }
                                } else {
                                    teacherProfileBinding.dateOfBirthEdt.setError("Please enter birthdate");
                                }
                            } else {
                                teacherProfileBinding.phoneNoEdt.setError("Please enter account number");
                            }
                        } else {
                            teacherProfileBinding.classNameEdt.setError("Please enter class name");
                        }
                    } else {
                        teacherProfileBinding.lastNameEdt.setError("Please enter last name");
                    }
                } else {
                    teacherProfileBinding.firstNameEdt.setError("Please enter first name");
                }

                break;
            case R.id.date_of_birth_edt:
                teacherProfileBinding.dateOfBirthEdt.setError(null);
                datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(TeacherProfileFragment.this, Year, Month, Day);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.setOkText("Done");
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.setAccentColor(Color.parseColor("#f2552c"));
//                datePickerDialog.setTitle("Select Date From DatePickerDialog");
                datePickerDialog.show(getActivity().getFragmentManager(), "Datepickerdialog");
                break;
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
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

        finalDate = d + "/" + m + "/" + y;

        teacherProfileBinding.dateOfBirthEdt.setText(finalDate);
    }
}

