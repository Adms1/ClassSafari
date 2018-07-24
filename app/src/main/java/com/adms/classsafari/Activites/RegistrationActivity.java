package com.adms.classsafari.Activites;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.adms.classsafari.AppConstant.ApiHandler;
import com.adms.classsafari.AppConstant.AppConfiguration;
import com.adms.classsafari.AppConstant.Utils;
import com.adms.classsafari.Model.Session.SessionDetailModel;
import com.adms.classsafari.Model.TeacherInfo.TeacherInfoModel;
import com.adms.classsafari.R;
import com.adms.classsafari.databinding.ActivityRegistrationBinding;
import com.adms.classsafari.databinding.OptionDialogBinding;
import com.adms.classsafari.databinding.RegisterOptionDialogBinding;
import com.adms.classsafari.databinding.SessiondetailConfirmationDialogBinding;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

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

public class RegistrationActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        View.OnClickListener, TextView.OnEditorActionListener, RadioGroup.OnCheckedChangeListener {

    ActivityRegistrationBinding registrationBinding;
    OptionDialogBinding optionDialogBinding;
    RegisterOptionDialogBinding registerOptionDialogBinding;
    SessiondetailConfirmationDialogBinding sessiondetailConfirmationDialogBinding;
    Context mContext;
    String finalDate;
    int Year, Month, Day;
    Calendar calendar;
    int mYear, mMonth, mDay;
    String firstNameStr, lastNameStr, emailStr, passwordStr, phonenoStr, gendarIdStr = "1", dateofbirthStr, coachTypeIDStr = "1",oraganisationStr="",
            registerTypeStr = "", contatIDstr, type, sessionIDStr,commentStr, ratingValueStr, paymentStatusstr, orderIDStr, frontloginStr,
            boardStr, standardStr, streamStr, locationStr, classNameStr, sessionType, durationStr, sessionDateStr,registerOptionStr="",
            subjectStr, genderStr, ratingLoginStr, searchfront, firsttimesearch, backStr,TeacherName, SearchPlaystudy, termscondition = "", frontRegister = "";
    //Use for Confirmation Dialog
    Dialog confimDialog, optionDialog,registerDialog;
    boolean firsttime = false;

    SessionDetailModel dataResponse;
    int SessionHour = 0;
    Integer SessionMinit = 0;
    String SessionDuration;
    String hours, minit,viewStr;
    boolean callservice = false;
    ArrayList<Integer> totalHours;
    ArrayList<Integer> totalMinit;
    int avgHoursvalue, avgMinitvalue;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registrationBinding = DataBindingUtil.setContentView(this, R.layout.activity_registration);
        mContext = RegistrationActivity.this;
        frontloginStr = getIntent().getStringExtra("frontLogin");
        sessionIDStr = getIntent().getStringExtra("sessionID");
        boardStr = getIntent().getStringExtra("board");
        standardStr = getIntent().getStringExtra("standard");
        streamStr = getIntent().getStringExtra("stream");
        locationStr = getIntent().getStringExtra("city");
        classNameStr = getIntent().getStringExtra("sessionName");
        subjectStr = getIntent().getStringExtra("lessionName");
        genderStr = getIntent().getStringExtra("gender");
        ratingLoginStr = getIntent().getStringExtra("ratingLogin");
        searchfront = getIntent().getStringExtra("searchfront");
        sessionType = getIntent().getStringExtra("sessionType");
        sessionDateStr = getIntent().getStringExtra("sessiondate");
        durationStr = getIntent().getStringExtra("duration");
        firsttimesearch = getIntent().getStringExtra("firsttimesearch");
        backStr = getIntent().getStringExtra("back");
        SearchPlaystudy = getIntent().getStringExtra("SearchPlaystudy");
        frontRegister = getIntent().getStringExtra("frontRegister");
        TeacherName=getIntent().getStringExtra("TeacherName");
//        setTypeface();

        init();
        setListner();
    }

//    public void setTypeface() {
//        Typeface custom_font = Typeface.createFromAsset(getAssets(), "font/TitilliumWeb-Regular.ttf");
//
//        registrationBinding.activityName.setTypeface(custom_font);
//        registrationBinding.typeOfRegi.setTypeface(custom_font);
//        registrationBinding.clickHere.setTypeface(custom_font);
//        registrationBinding.firstNameEdt.setTypeface(custom_font);
//        registrationBinding.lastNameEdt.setTypeface(custom_font);
//        registrationBinding.emailEdt.setTypeface(custom_font);
//        registrationBinding.passwordEdt.setTypeface(custom_font);
//        registrationBinding.phoneNoEdt.setTypeface(custom_font);
//        registrationBinding.dateOfBirthEdt.setTypeface(custom_font);
//        registrationBinding.maleChk.setTypeface(custom_font);
//        registrationBinding.femaleChk.setTypeface(custom_font);
//        registrationBinding.chkTermsAndCondi.setTypeface(custom_font);
//        registrationBinding.registerBtn.setTypeface(custom_font);
//        registrationBinding.viewTxt.setTypeface(custom_font);
//
//
//    }

    public void init() {
        selectRegisterOptionDialog();

        calendar = Calendar.getInstance();
        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    public void setListner() {
        registrationBinding.back.setOnClickListener(this);
        registrationBinding.registerBtn.setOnClickListener(this);
        registrationBinding.dateOfBirthEdt.setOnClickListener(this);
        registrationBinding.clickHere.setOnClickListener(this);
        registrationBinding.viewTxt.setOnClickListener(this);


        registrationBinding.emailEdt.setOnEditorActionListener(this);
        registrationBinding.passwordEdt.setOnEditorActionListener(this);
        registrationBinding.phoneNoEdt.setOnEditorActionListener(this);

        registrationBinding.genderGroup.setOnCheckedChangeListener(this);
        registrationBinding.session1TypeRg.setOnCheckedChangeListener(this);
        registrationBinding.registerTypeRg.setOnCheckedChangeListener(this);

        registrationBinding.chkTermsAndCondi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    termscondition = registrationBinding.chkTermsAndCondi.getTag().toString();
                }else{
                    termscondition="";
                }
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (radioGroup.getId()) {
            case R.id.session1_type_rg:
                int radioButtonId = registrationBinding.session1TypeRg.getCheckedRadioButtonId();
                switch (radioButtonId) {
                    case R.id.Academic_rb:
                        coachTypeIDStr = registrationBinding.AcademicRb.getTag().toString();
                        break;
                    case R.id.play_rb:
                        coachTypeIDStr = registrationBinding.playRb.getTag().toString();
                        break;
                }
                break;
            case R.id.gender_group:
                int radioButtonId1 = registrationBinding.genderGroup.getCheckedRadioButtonId();
                switch (radioButtonId1) {
                    case R.id.male_chk:
                        gendarIdStr = registrationBinding.maleChk.getTag().toString();
                        break;
                    case R.id.female_chk:
                        gendarIdStr = registrationBinding.femaleChk.getTag().toString();
                        break;
                    default:
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_txt:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://classsafari.admssvc.com/terms.aspx"));
                startActivity(browserIntent);
                break;
            case R.id.date_of_birth_edt:
                registrationBinding.dateOfBirthEdt.setError(null);
                datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(RegistrationActivity.this, Year, Month, Day);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.setOkText("Done");
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.setAccentColor(Color.parseColor("#f2552c"));
//                datePickerDialog.setTitle("Select Date From DatePickerDialog");
                datePickerDialog.show(getFragmentManager(), "Datepickerdialog");
                break;
            case R.id.register_btn:
                getInsertedValue();
                if (!firstNameStr.equalsIgnoreCase("")) {
                    if (!lastNameStr.equalsIgnoreCase("")) {
                        if (!emailStr.equalsIgnoreCase("") && Utils.isValidEmaillId(emailStr)) {
                            if (!passwordStr.equalsIgnoreCase("") && passwordStr.length() >= 4 && passwordStr.length() <= 8) {
                                if (!phonenoStr.equalsIgnoreCase("") && phonenoStr.length() >= 10) {
                                    if (!gendarIdStr.equalsIgnoreCase("")) {
                                        if (!dateofbirthStr.equalsIgnoreCase("")) {
                                            if (!termscondition.equalsIgnoreCase("")) {
                                                callservice = true;
                                                callCheckEmailIdApi();
                                            } else {
                                                Utils.ping(mContext, "Please accept the terms of service before proceeding");
                                            }
                                        } else {
                                            registrationBinding.dateOfBirthEdt.setError("Please select your birthdate");
                                        }
                                    } else {
                                        registrationBinding.femaleChk.setError("Select gender");
                                    }
                                } else {
                                    registrationBinding.phoneNoEdt.setError("Enter 10 digits phone number");
                                }
                            } else {
                                registrationBinding.passwordEdt.setError("Password must be 4-8 characters");
                            }
                        } else {
                            registrationBinding.emailEdt.setError("Please enter email address");
                        }
                    } else {
                        registrationBinding.lastNameEdt.setError("Please enter last name");
                    }
                } else {
                    registrationBinding.firstNameEdt.setError("Please enter first name");
                }
                break;
            case R.id.back:
                if (frontloginStr.equalsIgnoreCase("beforeLogin")) {
                    Intent iSearchByUser = new Intent(mContext, SearchByUser.class);
                    startActivity(iSearchByUser);
                } else {
                    Intent inback = new Intent(mContext, LoginActivity.class);
                    inback.putExtra("frontLogin", frontloginStr);
                    inback.putExtra("searchfront", searchfront);
                    inback.putExtra("sessionType", sessionType);
                    inback.putExtra("sessionID", sessionIDStr);
                    inback.putExtra("board", boardStr);
                    inback.putExtra("stream", streamStr);
                    inback.putExtra("standard", standardStr);
                    inback.putExtra("city", locationStr);
                    inback.putExtra("sessionName", classNameStr);
                    inback.putExtra("lessionName", subjectStr);
                    inback.putExtra("gender", genderStr);
                    inback.putExtra("frontLogin", frontloginStr);
                    inback.putExtra("ratingLogin", ratingLoginStr);
                    inback.putExtra("duration", durationStr);
                    inback.putExtra("sessiondate", sessionDateStr);
                    inback.putExtra("firsttimesearch", firsttimesearch);
                    inback.putExtra("back", backStr);
                    inback.putExtra("SearchPlaystudy", SearchPlaystudy);
                    inback.putExtra("TeacherName",TeacherName);
                    startActivity(inback);
                }
                break;
            case R.id.click_here:
                if (firsttime == false) {
                    registrationBinding.typeOfRegi.setText("If you are a Student");
                    registrationBinding.session1TypeRg.setVisibility(View.GONE);
                    registerTypeStr = registrationBinding.clickHere.getTag().toString();

                    firsttime = true;
                } else {
                    registrationBinding.typeOfRegi.setText("If you are a Teacher / Instructor");
                    registrationBinding.session1TypeRg.setVisibility(View.GONE);
                    registerTypeStr = "family";
                    firsttime = false;
                }
                if (registrationBinding.typeOfRegi.getText().toString().equalsIgnoreCase("If you are a Teacher / Instructor"))
                {
                    registrationBinding.classNameEdt.setVisibility(View.GONE);
                }else{
                    registrationBinding.classNameEdt.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        switch (textView.getId()) {
            case R.id.email_edt:
                emailStr = registrationBinding.emailEdt.getText().toString();
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    callservice = false;
                    if (!emailStr.equalsIgnoreCase("") && Utils.isValidEmaillId(emailStr)) {
                        callCheckEmailIdApi();
                    } else {
                        registrationBinding.emailEdt.setError("Please Enter Valid Email Address.");
                    }
                }
                break;
            case R.id.password_edt:
                passwordStr = registrationBinding.passwordEdt.getText().toString();
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    if (!passwordStr.equalsIgnoreCase("") && passwordStr.length() >= 4 && passwordStr.length() <= 8) {
                    } else {
                        registrationBinding.passwordEdt.setError("Password must be 4-8 Characters.");
                    }
                }
                break;
            case R.id.phone_no_edt:
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                    registrationBinding.dateOfBirthEdt.setError(null);
                    datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(RegistrationActivity.this, Year, Month, Day);
                    datePickerDialog.setThemeDark(false);
                    datePickerDialog.setOkText("Done");
                    datePickerDialog.showYearPickerFirst(false);
                    datePickerDialog.setAccentColor(Color.parseColor("#f2552c"));
//                datePickerDialog.setTitle("Select Date From DatePickerDialog");
                    datePickerDialog.show(getFragmentManager(), "Datepickerdialog");
                }
                break;
        }
        return false;
    }

    public void getInsertedValue() {
        firstNameStr = registrationBinding.firstNameEdt.getText().toString();
        lastNameStr = registrationBinding.lastNameEdt.getText().toString();
        emailStr = registrationBinding.emailEdt.getText().toString();
        passwordStr = registrationBinding.passwordEdt.getText().toString();
        phonenoStr = registrationBinding.phoneNoEdt.getText().toString();
        dateofbirthStr = registrationBinding.dateOfBirthEdt.getText().toString();
        oraganisationStr=registrationBinding.classNameEdt.getText().toString();
        Utils.setPref(mContext, "Password", passwordStr);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (frontloginStr.equalsIgnoreCase("beforeLogin")) {
            Intent iSearchByUser = new Intent(mContext, SearchByUser.class);
            startActivity(iSearchByUser);
        } else {
            Intent inback = new Intent(mContext, LoginActivity.class);
//        inback.putExtra("SearchBy", searchByStr);
            inback.putExtra("sessionID", sessionIDStr);
            inback.putExtra("board", boardStr);
            inback.putExtra("stream", streamStr);
            inback.putExtra("standard", standardStr);
            inback.putExtra("city", locationStr);
            inback.putExtra("sessionName", classNameStr);
            inback.putExtra("lessionName", subjectStr);
            inback.putExtra("gender", genderStr);
            inback.putExtra("frontLogin", frontloginStr);
            inback.putExtra("ratingLogin", ratingLoginStr);
            inback.putExtra("frontLogin", frontloginStr);
            inback.putExtra("searchfront", searchfront);
            inback.putExtra("sessionType", sessionType);
            inback.putExtra("duration", durationStr);
            inback.putExtra("sessiondate", sessionDateStr);
            inback.putExtra("firsttimesearch", firsttimesearch);
            inback.putExtra("back", backStr);
            inback.putExtra("SearchPlaystudy", SearchPlaystudy);
            inback.putExtra("TeacherName",TeacherName);
            startActivity(inback);
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

        registrationBinding.dateOfBirthEdt.setText(finalDate);
        //Age Validation

//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        int age = 0;
//        try {
//            Date date1 = dateFormat.parse(registrationBinding.dateOfBirthEdt.getText().toString());
//            Calendar now = Calendar.getInstance();
//            Calendar dob = Calendar.getInstance();
//            dob.setTime(date1);
//            if (dob.after(now)) {
////                throw new IllegalArgumentException("Can't be born in the future");
////                Utils.ping(mContext, "Can't be born in the future");
//                registrationBinding.dateOfBirthEdt.setError("Can't be born in the future");
//                registrationBinding.dateOfBirthEdt.setText("");
//            }
//            int year1 = now.get(Calendar.YEAR);
//            int year2 = dob.get(Calendar.YEAR);
//            age = year1 - year2;
//            int month1 = now.get(Calendar.MONTH);
//            int month2 = dob.get(Calendar.MONTH);
//            if (month2 > month1) {
//                age--;
//            } else if (month1 == month2) {
//                int day1 = now.get(Calendar.DAY_OF_MONTH);
//                int day2 = dob.get(Calendar.DAY_OF_MONTH);
//                if (day2 > day1) {
//                    age--;
//                }
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        if (age >= 1) {
//        } else {
////            Utils.ping(mContext, "Please Enter Valid Birthdate.");
//            registrationBinding.dateOfBirthEdt.setError(getResources().getString(R.string.agevalidation));
//            registrationBinding.dateOfBirthEdt.setText("");
//
//        }
    }


    //Use for Register New Teacher
    public void callTeacherApi() {
        if (Utils.isNetworkConnected(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().getCreateTeacher(getNewTeacherDetail(), new retrofit.Callback<TeacherInfoModel>() {
                @Override
                public void success(TeacherInfoModel teacherInfoModel, Response response) {
                    Utils.dismissDialog();
                    if (teacherInfoModel == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (teacherInfoModel.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (teacherInfoModel.getSuccess().equalsIgnoreCase("false")) {
                        Utils.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (teacherInfoModel.getSuccess().equalsIgnoreCase("True")) {
//                        Intent inLogin = new Intent(mContext, LoginActivity.class);
//                        startActivity(inLogin);
                        Utils.ping(mContext, "Thank you for registration");
                        callTeacherLoginApi();
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

    private Map<String, String> getNewTeacherDetail() {

        Map<String, String> map = new HashMap<>();
        map.put("FirstName", firstNameStr);
        map.put("LastName", lastNameStr);
        map.put("EmailAddress", emailStr);
        map.put("Password", passwordStr);
        map.put("PhoneNumber", phonenoStr);
        map.put("GenderID", gendarIdStr);
        map.put("DateOfBirth", dateofbirthStr);
        map.put("CoachTypeID", coachTypeIDStr);
        map.put("ClassName",oraganisationStr);
        return map;
    }

    //Use for Check Register emailId exist or not

    public void callCheckEmailIdApi() {
        if (Utils.isNetworkConnected(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().getCheckEmailAddress(getcheckEmailidDetail(), new retrofit.Callback<TeacherInfoModel>() {
                @Override
                public void success(TeacherInfoModel teacherInfoModel, Response response) {
                    Utils.dismissDialog();
                    if (teacherInfoModel == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (teacherInfoModel.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (teacherInfoModel.getSuccess().equalsIgnoreCase("False")) {
//                        Utils.ping(mContext, getString(R.string.false_msg));
                        if (callservice == true) {
                            if (registerTypeStr.equalsIgnoreCase("family")) {
                                callFamilyApi();
                            } else {
                                callTeacherApi();
                            }
                        }
                        callservice = false;
                        return;
                    }
                    if (teacherInfoModel.getSuccess().equalsIgnoreCase("True")) {
                        registrationBinding.emailEdt.setError("Already Exist!");
                        registrationBinding.emailEdt.setText("");
                        callservice = false;
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

    private Map<String, String> getcheckEmailidDetail() {

        Map<String, String> map = new HashMap<>();
        map.put("EmailAddress", registrationBinding.emailEdt.getText().toString());

        return map;
    }

    //Use for Login Teacher
    public void callTeacherLoginApi() {
        if (Utils.isNetworkConnected(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().getTeacherLogin(getTeacherLoginDetail(), new retrofit.Callback<TeacherInfoModel>() {
                @Override
                public void success(TeacherInfoModel teacherInfoModel, Response response) {
                    Utils.dismissDialog();
                    if (teacherInfoModel == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (teacherInfoModel.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (teacherInfoModel.getSuccess().equalsIgnoreCase("false")) {
                        Utils.ping(mContext, "Invalid email address or password");
                        return;
                    }
                    if (teacherInfoModel.getSuccess().equalsIgnoreCase("True")) {
                        String[] splitCoachID = teacherInfoModel.getCoachID().split("\\,");
                        Utils.setPref(mContext, "coachID", splitCoachID[0]);
                        Utils.setPref(mContext, "coachTypeID", splitCoachID[1]);
                        Utils.setPref(mContext, "RegisterUserName", teacherInfoModel.getName());
                        Utils.setPref(mContext, "RegisterEmail", teacherInfoModel.getEmailID());
                        Utils.setPref(mContext, "LoginType", teacherInfoModel.getLoginType());
                        Utils.setPref(mContext,"ClassName",teacherInfoModel.getClassName());
                        AppConfiguration.RegisterEmail = emailStr;
                        AppConfiguration.coachId = teacherInfoModel.getCoachID();
                        type = teacherInfoModel.getLoginType();
                        contatIDstr = splitCoachID[0];
                        if (teacherInfoModel.getLoginType().equalsIgnoreCase("Family") && !Utils.getPref(mContext, "coachID").equalsIgnoreCase("")) {
                            if (frontloginStr.equalsIgnoreCase("beforeLogin")) {
                                Intent iSearchByUser = new Intent(mContext, SearchByUser.class);
                                startActivity(iSearchByUser);
                                finish();
                            } else {
                                if (ratingLoginStr.equalsIgnoreCase("ratingLoginclass")) {
                                addRating();
                                }else if(ratingLoginStr.equalsIgnoreCase("ratingLoginSession")){
                                    addRating();
                                }else {
                                    SessionConfirmationDialog();
                                }
                            }

                        } else {
//                            Intent inLogin = new Intent(mContext, DashBoardActivity.class);
//                            startActivity(inLogin);
                            selectOptionDialog();
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

    private Map<String, String> getTeacherLoginDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("EmailAddress", emailStr);
        map.put("Password", passwordStr);

        return map;
    }

    //Use for New Family
    public void callFamilyApi() {
        if (Utils.isNetworkConnected(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_Create_Family(getNewFamilyetail(), new retrofit.Callback<TeacherInfoModel>() {
                @Override
                public void success(TeacherInfoModel familyInfoModel, Response response) {
                    Utils.dismissDialog();
                    if (familyInfoModel == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (familyInfoModel.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (familyInfoModel.getSuccess().equalsIgnoreCase("false")) {
                        Utils.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (familyInfoModel.getSuccess().equalsIgnoreCase("True")) {

                        Utils.setPref(mContext, "FamilyID", familyInfoModel.getContactID());
                        contatIDstr = familyInfoModel.getContactID();
                        callTeacherLoginApi();
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

    private Map<String, String> getNewFamilyetail() {
        Map<String, String> map = new HashMap<>();
        map.put("FirstName", firstNameStr);
        map.put("LastName", lastNameStr);
        map.put("EmailAddress", emailStr);
        map.put("Password", passwordStr);
        map.put("GenderID", gendarIdStr);
        map.put("DateOfBirth", dateofbirthStr);
        map.put("PhoneNumber", phonenoStr);
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
                        if (!AppConfiguration.classsessionPrice.equalsIgnoreCase("0.00")) {
                            Intent ipayment = new Intent(mContext, PaymentActivity.class);
                            ipayment.putExtra("orderID", orderIDStr);
                            ipayment.putExtra("amount", AppConfiguration.classsessionPrice);
                            ipayment.putExtra("mode", AppConfiguration.Mode);
                            ipayment.putExtra("username", Utils.getPref(mContext, "RegisterUserName"));
                            ipayment.putExtra("sessionID", sessionIDStr);
                            ipayment.putExtra("contactID", Utils.getPref(mContext, "coachID"));
                            ipayment.putExtra("type", Utils.getPref(mContext, "LoginType"));
                            startActivity(ipayment);
                            finish();
                        } else {
                            paymentStatusstr = "1";
                            callSessionConfirmationApi();
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

    private Map<String, String> getpaymentRequestdetail() {

        Map<String, String> map = new HashMap<>();
        map.put("ContactID", contatIDstr);
        map.put("SessionID", sessionIDStr);
        map.put("Amount", AppConfiguration.classsessionPrice);

        return map;
    }


    //Use for Family and Child Session Confirmation
    public void callSessionConfirmationApi() {
        if (Utils.isNetworkConnected(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_Session_ContactEnrollment(getSessionConfirmationdetail(), new retrofit.Callback<TeacherInfoModel>() {
                @Override
                public void success(TeacherInfoModel sessionconfirmationInfoModel, Response response) {
                    Utils.dismissDialog();
                    if (sessionconfirmationInfoModel == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (sessionconfirmationInfoModel.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (sessionconfirmationInfoModel.getSuccess().equalsIgnoreCase("false")) {
                        Utils.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (sessionconfirmationInfoModel.getSuccess().equalsIgnoreCase("True")) {
                        Utils.ping(mContext, "Login succesfully");
                        confimDialog.dismiss();
                        Intent isearchBYuser = new Intent(mContext, PaymentSuccessActivity.class);
                        isearchBYuser.putExtra("transactionId", orderIDStr);
                        isearchBYuser.putExtra("responseCode", "0");
                        isearchBYuser.putExtra("order_id",orderIDStr);
                        startActivity(isearchBYuser);
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

    private Map<String, String> getSessionConfirmationdetail() {
        Map<String, String> map = new HashMap<>();
        map.put("SessionID", sessionIDStr);
        map.put("ContactID", contatIDstr);
        map.put("PaymentStatus", paymentStatusstr);
        return map;
    }

    public void SessionConfirmationDialog() {
        sessiondetailConfirmationDialogBinding = DataBindingUtil.
                inflate(LayoutInflater.from(mContext), R.layout.sessiondetail_confirmation_dialog, (ViewGroup) registrationBinding.getRoot(), false);
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
//        confimDialog.setContentView(R.layout.confirm_session_dialog);
        confimDialog.setContentView(sessiondetailConfirmationDialogBinding.getRoot());
        callSessionListApi();
        sessiondetailConfirmationDialogBinding.cancelTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confimDialog.dismiss();
            }
        });
        sessiondetailConfirmationDialogBinding.confirmTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppConfiguration.TeacherSessionIdStr = sessionIDStr;
                AppConfiguration.TeacherSessionContactIdStr = contatIDstr;
                callSessioncapacityApi();
                confimDialog.dismiss();
            }
        });
        confimDialog.show();

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
                        if (!contatIDstr.equalsIgnoreCase("") && !sessionIDStr.equalsIgnoreCase("")){// && !AppConfiguration.classsessionPrice.equalsIgnoreCase("0.00")) {
                            callpaymentRequestApi();
                        }
//                        else {
//                            paymentStatusstr = "1";
//                            callSessionConfirmationApi();
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
        } else {
            Utils.ping(mContext, getString(R.string.internet_connection_error));
        }
    }

    private Map<String, String> getSessioncapacityDetail() {

        Map<String, String> map = new HashMap<>();
        map.put("SessionID", sessionIDStr);//contatIDstr  //Utils.getPref(mContext, "coachID")
        return map;
    }

    //Use for SessionList
    public void callSessionListApi() {
        if (Utils.checkNetwork(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_SessionBySessionID(getSessionListDetail(), new retrofit.Callback<SessionDetailModel>() {
                @Override
                public void success(SessionDetailModel sessionDetailInfo, Response response) {
                    Utils.dismissDialog();
                    if (sessionDetailInfo == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (sessionDetailInfo.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (sessionDetailInfo.getSuccess().equalsIgnoreCase("false")) {
                        Utils.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (sessionDetailInfo.getSuccess().equalsIgnoreCase("True")) {

                        if (sessionDetailInfo.getData().size() > 0) {
                            dataResponse = sessionDetailInfo;
                            for (int j = 0; j < dataResponse.getData().size(); j++) {
                                AppConfiguration.bookingsubjectName=dataResponse.getData().get(j).getSessionName();
                                AppConfiguration.bookingteacherName=dataResponse.getData().get(j).getName();
                                AppConfiguration.bookingdate=dataResponse.getData().get(j).getStartDate();
                                AppConfiguration.bookingtime=dataResponse.getData().get(j).getSchedule();
                                AppConfiguration.bookingamount=dataResponse.getData().get(j).getSessionAmount();
                                AppConfiguration.bookinhEnddate=dataResponse.getData().get(j).getEndDate();
                                sessiondetailConfirmationDialogBinding.sessionNameTxt.setText(dataResponse.getData().get(j).getSessionName());
                                sessiondetailConfirmationDialogBinding.ratingBar.setRating(Float.parseFloat(dataResponse.getData().get(j).getRating()));
                                sessiondetailConfirmationDialogBinding.tutorNameTxt.setText(dataResponse.getData().get(j).getName());
                                sessiondetailConfirmationDialogBinding.locationTxt.setText(dataResponse.getData().get(j).getRegionName());
                                sessiondetailConfirmationDialogBinding.startDateTxt.setText(dataResponse.getData().get(j).getStartDate());
                                sessiondetailConfirmationDialogBinding.endDateTxt.setText(dataResponse.getData().get(j).getEndDate());
                                if (dataResponse.getData().get(j).getSessionAmount().equalsIgnoreCase("0.00")){
                                    sessiondetailConfirmationDialogBinding.priceTxt.setText("Free");
                                }else {
                                    sessiondetailConfirmationDialogBinding.priceTxt.setText("₹" + dataResponse.getData().get(j).getSessionAmount());
                                }
                                if (!dataResponse.getData().get(j).getTotalRatingUser().equalsIgnoreCase("0")) {
                                    sessiondetailConfirmationDialogBinding.ratingUserTxt.setText("( " + dataResponse.getData().get(j).getTotalRatingUser() + " )");
                                }


                                AppConfiguration.classsessionPrice = dataResponse.getData().get(j).getSessionAmount();
                                totalHours = new ArrayList<>();
                                totalMinit = new ArrayList<>();
                                String[] spiltPipes = dataResponse.getData().get(j).getSchedule().split("\\|");
                                String[] spiltComma;
                                String[] spiltDash;
                                Log.d("spilt", "" + spiltPipes.toString());
                                for (int i = 0; i < spiltPipes.length; i++) {
                                    spiltComma = spiltPipes[i].split("\\,");
                                    spiltDash = spiltComma[1].split("\\-");
                                    calculateHours(spiltDash[0], spiltDash[1]);
                                    dataResponse.getData().get(j).setDateTime(spiltDash[0]);
                                    Log.d("DateTime", spiltDash[0]);
//                                    if (SessionHour < 10) {
//                                        hours = "0" + SessionHour;
//                                    } else {
//                                        hours = String.valueOf(SessionHour);
//                                    }
//                                    if (SessionMinit < 10) {
//                                        minit = "0" + SessionMinit;
//                                    } else {
//                                        minit = String.valueOf(SessionMinit);
//                                    }
//                                    if (minit.equalsIgnoreCase(("00"))) {
//                                        sessiondetailConfirmationDialogBinding.durationTxt.setText(hours + " hr ");
//                                    } else {
//                                        sessiondetailConfirmationDialogBinding.durationTxt.setText(hours + " hr " + minit + " min");//+ " min"
//                                    }
                                    switch (spiltComma[0]) {
                                        case "sun":
                                            sessiondetailConfirmationDialogBinding.sunHoursTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.sunHoursTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.sunTimeTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.sundayBtn.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.sunTimeTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.sundayBtn.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.sunTimeTxt.setText(dataResponse.getData().get(j).getDateTime());
                                            sessiondetailConfirmationDialogBinding.sunHoursTxt.setText(SessionDuration);
                                            break;
                                        case "mon":
                                            sessiondetailConfirmationDialogBinding.monHoursTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.monHoursTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.monTimeTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.mondayBtn.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.monTimeTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.mondayBtn.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.monTimeTxt.setText(dataResponse.getData().get(j).getDateTime());
                                            sessiondetailConfirmationDialogBinding.monHoursTxt.setText(SessionDuration);
                                            break;
                                        case "tue":
                                            sessiondetailConfirmationDialogBinding.tuesHoursTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.tuesHoursTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.tuesTimeTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.tuesdayBtn.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.tuesTimeTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.tuesdayBtn.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.tuesTimeTxt.setText(dataResponse.getData().get(j).getDateTime());
                                            sessiondetailConfirmationDialogBinding.tuesHoursTxt.setText(SessionDuration);
                                            break;
                                        case "wed":
                                            sessiondetailConfirmationDialogBinding.wedHoursTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.wedHoursTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.wedTimeTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.wednesdayBtn.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.wedTimeTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.wednesdayBtn.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.wedTimeTxt.setText(dataResponse.getData().get(j).getDateTime());
                                            sessiondetailConfirmationDialogBinding.wedHoursTxt.setText(SessionDuration);
                                            break;
                                        case "thu":
                                            sessiondetailConfirmationDialogBinding.thurHoursTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.thurHoursTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.thurTimeTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.thursdayBtn.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.thurTimeTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.thursdayBtn.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.thurTimeTxt.setText(dataResponse.getData().get(j).getDateTime());
                                            sessiondetailConfirmationDialogBinding.thurHoursTxt.setText(SessionDuration);
                                            break;
                                        case "fri":
                                            sessiondetailConfirmationDialogBinding.friHoursTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.friHoursTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.friTimeTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.fridayBtn.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.friTimeTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.fridayBtn.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.friTimeTxt.setText(dataResponse.getData().get(j).getDateTime());
                                            sessiondetailConfirmationDialogBinding.friHoursTxt.setText(SessionDuration);
                                            break;
                                        case "sat":
                                            sessiondetailConfirmationDialogBinding.satHoursTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.satHoursTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.satTimeTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.saturdayBtn.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.satTimeTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.saturdayBtn.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.satTimeTxt.setText(dataResponse.getData().get(j).getDateTime());
                                            sessiondetailConfirmationDialogBinding.satHoursTxt.setText(SessionDuration);
                                            break;
                                        default:

                                    }
                                }
                            }
//                            averageHours(totalHours);
//                            averageMinit(totalMinit);

//                            if(avgMinitvalue==0) {
//                                sessiondetailConfirmationDialogBinding.durationTxt.setText(avgHoursvalue + " hr ");
//                            }else{
//                                sessiondetailConfirmationDialogBinding.durationTxt.setText(avgHoursvalue + " hr " + avgMinitvalue + " min");//+ " min"
//                            }
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

    private Map<String, String> getSessionListDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("SessionID", sessionIDStr);
        return map;
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
//            if(SessionHour>0) {
//                totalHours.add(SessionHour);
//            }
////            if(SessionMinit>0) {
//            totalMinit.add(SessionMinit);
////            }
            if (SessionMinit > 0) {
                if (SessionMinit < 10) {
                    SessionDuration = String.valueOf(SessionHour) + ":" + String.valueOf("0" + SessionMinit + " hrs");
                } else {
                    SessionDuration = String.valueOf(SessionHour) + ":" + String.valueOf(SessionMinit + " hrs");

                }
            } else {
                SessionDuration = String.valueOf(SessionHour) +":" + "00" +" hrs";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }
    public void selectOptionDialog() {
        optionDialogBinding = DataBindingUtil.
                inflate(LayoutInflater.from(mContext), R.layout.option_dialog, (ViewGroup) registrationBinding.getRoot(), false);

        optionDialog = new Dialog(mContext, R.style.Theme_Dialog);
        Window window = optionDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        optionDialog.getWindow().getAttributes().verticalMargin = 0.0f;
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);

        optionDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);

        optionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        optionDialog.setCancelable(false);
        optionDialog.setContentView(optionDialogBinding.getRoot());
        String[] userName = Utils.getPref(mContext, "RegisterUserName").split("\\s+");

        optionDialogBinding.titleTxt.setText(Html.fromHtml("Welcome " + "<b>" + userName[0] + "</b>"));
        optionDialogBinding.addClassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inLogin = new Intent(mContext, DashBoardActivity.class);
                inLogin.putExtra("frontLogin", frontloginStr);
                inLogin.putExtra("position", "1");
                startActivity(inLogin);
                optionDialog.dismiss();
            }
        });
        optionDialogBinding.viewClassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inLogin = new Intent(mContext, DashBoardActivity.class);
                inLogin.putExtra("frontLogin", frontloginStr);
                inLogin.putExtra("position", "0");
                startActivity(inLogin);
                optionDialog.dismiss();
            }
        });
//        optionDialogBinding.cancelBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                registrationBinding.emailEdt.setText("");
//                registrationBinding.passwordEdt.setText("");
//                Utils.setPref(mContext, "coachID", "");
//                Utils.setPref(mContext, "coachTypeID", "");
//                Utils.setPref(mContext, "RegisterUserName", "");
//                Utils.setPref(mContext, "RegisterEmail", "");
//                Utils.setPref(mContext, "LoginType", "");
//                optionDialog.dismiss();
//            }
//        });
        optionDialog.show();

    }

    public void selectRegisterOptionDialog() {
        registerOptionDialogBinding = DataBindingUtil.
                inflate(LayoutInflater.from(mContext), R.layout.register_option_dialog, (ViewGroup) registrationBinding.getRoot(), false);

        registerDialog = new Dialog(mContext, R.style.Theme_Dialog);
        Window window = registerDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
//        registerDialog.getWindow().getAttributes().verticalMargin = 0.0f;
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);

        registerDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);

        registerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        registerDialog.setCancelable(false);
        registerDialog.setContentView(registerOptionDialogBinding.getRoot());


        registerOptionDialogBinding.addStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerOptionStr="student";
                registerTypeStr = "family";
                registrationBinding.classNameEdt.setVisibility(View.GONE);
                registerDialog.dismiss();
            }
        });
        registerOptionDialogBinding.teacherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerOptionStr="teacher";
                registerTypeStr = "teacher";
                registrationBinding.classNameEdt.setVisibility(View.VISIBLE);
                registerDialog.dismiss();
            }
        });


        registerDialog.show();

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

        android.app.AlertDialog.Builder sayWindows = new android.app.AlertDialog.Builder(
                mContext);

        sayWindows.setPositiveButton("Rate", null);
        sayWindows.setNegativeButton("Not Now", null);
        sayWindows.setView(alertLayout);

        final android.app.AlertDialog mAlertDialog = sayWindows.create();
        mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button b = mAlertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE);
                Button b1 = mAlertDialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE);
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
                        Intent i=new Intent(mContext,SearchByUser.class);
                        startActivity(i);
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
        map.put("SessionID", sessionIDStr);
        map.put("ContactID", Utils.getPref(mContext, "coachID"));
        map.put("Comment", commentStr);
        map.put("RatingValue", ratingValueStr);

        return map;
    }
}
