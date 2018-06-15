package com.adms.classsafari.Activites;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.adms.classsafari.AppConstant.ApiHandler;
import com.adms.classsafari.AppConstant.AppConfiguration;
import com.adms.classsafari.AppConstant.Utils;
import com.adms.classsafari.Model.Session.SessionDetailModel;
import com.adms.classsafari.Model.TeacherInfo.TeacherInfoModel;
import com.adms.classsafari.R;
import com.adms.classsafari.databinding.ActivityRegistrationBinding;
import com.adms.classsafari.databinding.ConfirmSessionDialogBinding;
import com.adms.classsafari.databinding.SessiondetailConfirmationDialogBinding;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class RegistrationActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        View.OnClickListener, TextView.OnEditorActionListener, RadioGroup.OnCheckedChangeListener {

    ActivityRegistrationBinding registrationBinding;
    ConfirmSessionDialogBinding confirmSessionDialogBinding;
    Context mContext;
    String finalDate;
    int Year, Month, Day;
    Calendar calendar;
    int mYear, mMonth, mDay;
    String firstNameStr, lastNameStr, emailStr, passwordStr, phonenoStr, gendarIdStr = "1", dateofbirthStr, coachTypeIDStr = "1",
            registerTypeStr = "family", contatIDstr, type, whereTocomestr, sessionIDStr, paymentStatusstr, orderIDStr, frontloginStr,
            boardStr, standardStr, streamStr, locationStr, classNameStr, sessionType, durationStr, sessionDateStr,
            searchTypeStr, subjectStr, genderStr, searchByStr, ratingLoginStr, searchfront, firsttimesearch, backStr;
    //Use for Confirmation Dialog
    Dialog confimDialog;
    boolean firsttime = false;
    private DatePickerDialog datePickerDialog;
    SessiondetailConfirmationDialogBinding sessiondetailConfirmationDialogBinding;
    SessionDetailModel dataResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registrationBinding = DataBindingUtil.setContentView(this, R.layout.activity_registration);
        mContext = RegistrationActivity.this;
        frontloginStr = getIntent().getStringExtra("frontLogin");
        sessionIDStr = getIntent().getStringExtra("sessionID");
        whereTocomestr = getIntent().getStringExtra("withOR");
        searchByStr = getIntent().getStringExtra("SearchBy");
        boardStr = getIntent().getStringExtra("board");
        standardStr = getIntent().getStringExtra("standard");
        streamStr = getIntent().getStringExtra("stream");
        locationStr = getIntent().getStringExtra("city");
        classNameStr = getIntent().getStringExtra("sessionName");
        searchTypeStr = getIntent().getStringExtra("searchType");
        subjectStr = getIntent().getStringExtra("lessionName");
        genderStr = getIntent().getStringExtra("gender");
        ratingLoginStr = getIntent().getStringExtra("ratingLogin");
        searchfront = getIntent().getStringExtra("searchfront");
        sessionType = getIntent().getStringExtra("sessionType");
        sessionDateStr = getIntent().getStringExtra("sessiondate");
        durationStr = getIntent().getStringExtra("duration");
        firsttimesearch = getIntent().getStringExtra("firsttimesearch");
        backStr = getIntent().getStringExtra("back");
        init();
        setListner();
    }

    public void init() {
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

        registrationBinding.emailEdt.setOnEditorActionListener(this);
        registrationBinding.passwordEdt.setOnEditorActionListener(this);
        registrationBinding.phoneNoEdt.setOnEditorActionListener(this);

        registrationBinding.genderGroup.setOnCheckedChangeListener(this);
        registrationBinding.session1TypeRg.setOnCheckedChangeListener(this);
        registrationBinding.registerTypeRg.setOnCheckedChangeListener(this);
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
                                            callCheckEmailIdApi();
                                        } else {
                                            registrationBinding.dateOfBirthEdt.setError("Please Select Your Birth Date.");
                                        }
                                    } else {
                                        registrationBinding.femaleChk.setError("Select Gender.");
                                    }
                                } else {
                                    registrationBinding.phoneNoEdt.setError("Enter 10 digit Phone Number.");
                                }
                            } else {
                                registrationBinding.passwordEdt.setError("Password must be 4-8 Characters.");
                            }
                        } else {
                            registrationBinding.emailEdt.setError("Please Enter Email Address.");
                        }
                    } else {
                        registrationBinding.lastNameEdt.setError("Please Enter LastName.");
                    }
                } else {
                    registrationBinding.firstNameEdt.setError("Please Enter FirstName.");
                }
                break;
            case R.id.back:
                Intent inback = new Intent(mContext, LoginActivity.class);
                inback.putExtra("frontLogin", frontloginStr);
                inback.putExtra("searchfront", searchfront);
                inback.putExtra("sessionType", sessionType);
                inback.putExtra("SearchBy", searchByStr);
                inback.putExtra("sessionID", sessionIDStr);
                inback.putExtra("board", boardStr);
                inback.putExtra("stream", streamStr);
                inback.putExtra("standard", standardStr);
                inback.putExtra("city", locationStr);
                inback.putExtra("sessionName", classNameStr);
                inback.putExtra("searchType", searchTypeStr);
                inback.putExtra("lessionName", subjectStr);
                inback.putExtra("gender", genderStr);
                inback.putExtra("frontLogin", frontloginStr);
                inback.putExtra("withOR", whereTocomestr);
                inback.putExtra("ratingLogin", ratingLoginStr);
                inback.putExtra("duration", durationStr);
                inback.putExtra("sessiondate", sessionDateStr);
                inback.putExtra("firsttimesearch", firsttimesearch);
                inback.putExtra("back", backStr);
                startActivity(inback);
                break;
            case R.id.click_here:
                if (firsttime == false) {
                    registrationBinding.session1TypeRg.setVisibility(View.VISIBLE);
                    registerTypeStr = registrationBinding.clickHere.getTag().toString();
                    firsttime = true;
                } else {
                    registrationBinding.session1TypeRg.setVisibility(View.GONE);
                    registerTypeStr = "family";
                    firsttime = false;
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
                    if (!emailStr.equalsIgnoreCase("") && Utils.isValidEmaillId(emailStr)) {
//                        callCheckEmailIdApi();
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

        Utils.setPref(mContext, "Password", passwordStr);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent inback = new Intent(mContext, LoginActivity.class);
        inback.putExtra("SearchBy", searchByStr);
        inback.putExtra("sessionID", sessionIDStr);
        inback.putExtra("board", boardStr);
        inback.putExtra("stream", streamStr);
        inback.putExtra("standard", standardStr);
        inback.putExtra("city", locationStr);
        inback.putExtra("sessionName", classNameStr);
        inback.putExtra("searchType", searchTypeStr);
        inback.putExtra("lessionName", subjectStr);
        inback.putExtra("gender", genderStr);
        inback.putExtra("frontLogin", frontloginStr);
        inback.putExtra("withOR", whereTocomestr);
        inback.putExtra("ratingLogin", ratingLoginStr);
        inback.putExtra("frontLogin", frontloginStr);
        inback.putExtra("searchfront", searchfront);
        inback.putExtra("sessionType", sessionType);
        inback.putExtra("duration", durationStr);
        inback.putExtra("sessiondate", sessionDateStr);
        inback.putExtra("firsttimesearch", firsttimesearch);
        inback.putExtra("back", backStr);
        startActivity(inback);
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        int age = 0;
        try {
            Date date1 = dateFormat.parse(registrationBinding.dateOfBirthEdt.getText().toString());
            Calendar now = Calendar.getInstance();
            Calendar dob = Calendar.getInstance();
            dob.setTime(date1);
            if (dob.after(now)) {
//                throw new IllegalArgumentException("Can't be born in the future");
//                Utils.ping(mContext, "Can't be born in the future");
                registrationBinding.dateOfBirthEdt.setError("Can't be born in the future");
                registrationBinding.dateOfBirthEdt.setText("");
            }
            int year1 = now.get(Calendar.YEAR);
            int year2 = dob.get(Calendar.YEAR);
            age = year1 - year2;
            int month1 = now.get(Calendar.MONTH);
            int month2 = dob.get(Calendar.MONTH);
            if (month2 > month1) {
                age--;
            } else if (month1 == month2) {
                int day1 = now.get(Calendar.DAY_OF_MONTH);
                int day2 = dob.get(Calendar.DAY_OF_MONTH);
                if (day2 > day1) {
                    age--;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (age >= 1) {
        } else {
//            Utils.ping(mContext, "Please Enter Valid Birthdate.");
            registrationBinding.dateOfBirthEdt.setError(getResources().getString(R.string.agevalidation));
            registrationBinding.dateOfBirthEdt.setText("");

        }
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
                        Utils.ping(mContext, "Thank you for registration.");
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
                    if (teacherInfoModel.getSuccess().equalsIgnoreCase("false")) {
//                        Utils.ping(mContext, getString(R.string.false_msg));
                        if (registerTypeStr.equalsIgnoreCase("family")) {
                            callFamilyApi();
                        } else {
                            callTeacherApi();
                        }
                        return;
                    }
                    if (teacherInfoModel.getSuccess().equalsIgnoreCase("True")) {
                        registrationBinding.emailEdt.setError("Already Exist!");
                        registrationBinding.emailEdt.setText("");
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
                        Utils.ping(mContext, "Invalid Email Address or Password.");
                        return;
                    }
                    if (teacherInfoModel.getSuccess().equalsIgnoreCase("True")) {
                        String[] splitCoachID = teacherInfoModel.getCoachID().split("\\,");
                        Utils.setPref(mContext, "coachID", splitCoachID[0]);
                        Utils.setPref(mContext, "coachTypeID", splitCoachID[1]);
                        Utils.setPref(mContext, "RegisterUserName", teacherInfoModel.getName());
                        Utils.setPref(mContext, "RegisterEmail", teacherInfoModel.getEmailID());
                        Utils.setPref(mContext, "LoginType", teacherInfoModel.getLoginType());
                        AppConfiguration.RegisterEmail = emailStr;
                        AppConfiguration.coachId = teacherInfoModel.getCoachID();
                        type = teacherInfoModel.getLoginType();
                        contatIDstr = splitCoachID[0];
                        if (teacherInfoModel.getLoginType().equalsIgnoreCase("Family") && !Utils.getPref(mContext, "coachID").equalsIgnoreCase("")) {
                            if (frontloginStr.equalsIgnoreCase("beforeLogin")) {
                                Intent iSearchByUser = new Intent(mContext, SearchByUser.class);
                                startActivity(iSearchByUser);
                            } else {
                                SessionConfirmationDialog();
                            }

                        } else {
                            Intent inLogin = new Intent(mContext, DashBoardActivity.class);
                            startActivity(inLogin);
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
                        Intent ipayment = new Intent(mContext, PaymentActivity.class);
                        ipayment.putExtra("orderID", orderIDStr);
                        ipayment.putExtra("amount", AppConfiguration.classsessionPrice);
                        ipayment.putExtra("mode", AppConfiguration.Mode);
                        ipayment.putExtra("username", Utils.getPref(mContext, "RegisterUserName"));
                        ipayment.putExtra("sessionID", sessionIDStr);
                        ipayment.putExtra("contactID", Utils.getPref(mContext, "coachID"));
                        ipayment.putExtra("type", Utils.getPref(mContext, "LoginType"));
                        startActivity(ipayment);

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
                        Utils.ping(mContext, "Login Succesfully");
                        confimDialog.dismiss();
                        Intent isearchBYuser = new Intent(mContext, SearchByUser.class);
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

        confimDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);

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
                if (!contatIDstr.equalsIgnoreCase("") && !sessionIDStr.equalsIgnoreCase("") && !AppConfiguration.classsessionPrice.equalsIgnoreCase("0.00")) {
                    callpaymentRequestApi();
                } else {
                    paymentStatusstr = "1";
                    callSessionConfirmationApi();
                }
                confimDialog.dismiss();
            }
        });
        confimDialog.show();

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
                                sessiondetailConfirmationDialogBinding.sessionNameTxt.setText(dataResponse.getData().get(j).getSessionName());
                                sessiondetailConfirmationDialogBinding.ratingBar.setRating(Float.parseFloat(dataResponse.getData().get(j).getRating()));
                                sessiondetailConfirmationDialogBinding.tutorNameTxt.setText(dataResponse.getData().get(j).getName());
                                sessiondetailConfirmationDialogBinding.locationTxt.setText(dataResponse.getData().get(j).getRegionName());
                                sessiondetailConfirmationDialogBinding.startDateTxt.setText(dataResponse.getData().get(j).getStartDate());
                                sessiondetailConfirmationDialogBinding.endDateTxt.setText(dataResponse.getData().get(j).getEndDate());
                                sessiondetailConfirmationDialogBinding.durationTxt.setText(durationStr);
                                AppConfiguration.classsessionPrice = dataResponse.getData().get(j).getSessionAmount();
                                String[] spiltPipes = dataResponse.getData().get(j).getSchedule().split("\\|");
                                String[] spiltComma;
                                String[] spiltDash;
                                Log.d("spilt", "" + spiltPipes.toString());
                                for (int i = 0; i < spiltPipes.length; i++) {
                                    spiltComma = spiltPipes[i].split("\\,");
                                    spiltDash = spiltComma[1].split("\\-");
                                    dataResponse.getData().get(j).setDateTime(spiltDash[0]);
                                    Log.d("DateTime", spiltDash[0]);
                                    switch (spiltComma[0]) {
                                        case "sun":
                                            sessiondetailConfirmationDialogBinding.sunTimeTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.sundayBtn.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.sunTimeTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.sundayBtn.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.sunTimeTxt.setText(dataResponse.getData().get(j).getDateTime());
                                            break;
                                        case "mon":
                                            sessiondetailConfirmationDialogBinding.monTimeTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.mondayBtn.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.monTimeTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.mondayBtn.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.monTimeTxt.setText(dataResponse.getData().get(j).getDateTime());
                                            break;
                                        case "tue":
                                            sessiondetailConfirmationDialogBinding.tuesTimeTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.tuesdayBtn.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.tuesTimeTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.tuesdayBtn.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.tuesTimeTxt.setText(dataResponse.getData().get(j).getDateTime());
                                            break;
                                        case "wed":
                                            sessiondetailConfirmationDialogBinding.wedTimeTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.wednesdayBtn.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.wedTimeTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.wednesdayBtn.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.wedTimeTxt.setText(dataResponse.getData().get(j).getDateTime());
                                            break;
                                        case "thu":
                                            sessiondetailConfirmationDialogBinding.thurTimeTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.thursdayBtn.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.thurTimeTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.thursdayBtn.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.thurTimeTxt.setText(dataResponse.getData().get(j).getDateTime());
                                            break;
                                        case "fri":
                                            sessiondetailConfirmationDialogBinding.friTimeTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.fridayBtn.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.friTimeTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.fridayBtn.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.friTimeTxt.setText(dataResponse.getData().get(j).getDateTime());
                                            break;
                                        case "sat":
                                            sessiondetailConfirmationDialogBinding.satTimeTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.saturdayBtn.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.satTimeTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.saturdayBtn.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.satTimeTxt.setText(dataResponse.getData().get(j).getDateTime());
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

    private Map<String, String> getSessionListDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("SessionID", sessionIDStr);
        return map;
    }
}
