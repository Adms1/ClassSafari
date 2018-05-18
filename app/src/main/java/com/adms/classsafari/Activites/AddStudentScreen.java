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
import android.widget.RadioGroup;
import android.widget.TextView;

import com.adms.classsafari.AppConstant.ApiHandler;
import com.adms.classsafari.AppConstant.AppConfiguration;
import com.adms.classsafari.AppConstant.Utils;
import com.adms.classsafari.Model.TeacherInfo.TeacherInfoModel;
import com.adms.classsafari.R;
import com.adms.classsafari.databinding.ActivityAddStudentScreenBinding;
import com.adms.classsafari.databinding.ConfirmSessionDialogBinding;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class AddStudentScreen extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    ActivityAddStudentScreenBinding addStudentScreenBinding;
    ConfirmSessionDialogBinding confirmSessionDialogBinding;
    Context mContext;
    String MonthInt;
    int Year, Month, Day;
    Calendar calendar;
    private DatePickerDialog datePickerDialog;
    int mYear, mMonth, mDay;
    String pageTitle, type, firstNameStr, lastNameStr, emailStr = "", passwordStr,
            phonenoStr, gendarIdStr = "1", dateofbirthStr, contactTypeIDStr,
            familyIDStr, contatIDstr, orderIDStr, sessionIDStr, classStr = "Child",
            familyNameStr, paymentStatusstr, familylocationStr, familysessionStudentStr,
            sessionDateStr, durationStr, familysessionfeesStr, familysessionnameStr, locationStr,
            boardStr, standardStr, streamStr, searchTypeStr, subjectStr, froncontanctStr,
            wheretoComeStr, searchByStr, genderStr, wheretocometypeStr, searchfront,sessionType,firsttimesearch;
    Dialog confimDialog;
//    TextView cancel_txt, confirm_txt, session_student_txt, session_name_txt,session_teacher_txt,
//            location_txt, duration_txt, time_txt, session_fee_txt, session_student_txt_view, time_txt_view;
    TeacherInfoModel classListInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addStudentScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_student_screen);
        mContext = AddStudentScreen.this;
        init();
        setListner();
    }

    public void init() {
        calendar = Calendar.getInstance();
        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);

        callClassTypeDetailApi();

        type = getIntent().getStringExtra("type");
        familyIDStr = getIntent().getStringExtra("familyID");
        familyNameStr = getIntent().getStringExtra("familyNameStr");
        sessionIDStr = getIntent().getStringExtra("sessionID");
        familysessionStudentStr = getIntent().getStringExtra("sessionStudent");
        familylocationStr = getIntent().getStringExtra("location");
        durationStr = getIntent().getStringExtra("duration");
        familysessionfeesStr = getIntent().getStringExtra("sessionfees");
        familysessionnameStr = getIntent().getStringExtra("sessionName");
        sessionDateStr = getIntent().getStringExtra("sessiondate");
        locationStr = getIntent().getStringExtra("city");
        subjectStr = getIntent().getStringExtra("lessionName");
        searchByStr = getIntent().getStringExtra("SearchBy");
        standardStr = getIntent().getStringExtra("standard");
        streamStr = getIntent().getStringExtra("stream");
        boardStr = getIntent().getStringExtra("board");
        searchTypeStr = getIntent().getStringExtra("searchType");
        genderStr = getIntent().getStringExtra("gender");
        wheretoComeStr = getIntent().getStringExtra("withOR");
        froncontanctStr = getIntent().getStringExtra("froncontanct");
        wheretocometypeStr = getIntent().getStringExtra("wheretocometype");
        searchfront = getIntent().getStringExtra("searchfront");
        sessionType=getIntent().getStringExtra("sessionType");
        firsttimesearch=getIntent().getStringExtra("firsttimesearch");
        Log.d("familyName", familyNameStr + familyIDStr);
        addStudentScreenBinding.familynameTxt.setText(Utils.getPref(mContext, "RegisterUserName"));
    }

    public void setListner() {
        addStudentScreenBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type.equalsIgnoreCase("book")) {
                    Intent intent = new Intent(mContext, FamilyListActivity.class);
                    intent.putExtra("type", "book");
                    intent.putExtra("familyID", familyIDStr);
                    intent.putExtra("familyNameStr", familyNameStr);
                    intent.putExtra("sessionID", sessionIDStr);
                    intent.putExtra("duration", durationStr);
                    intent.putExtra("sessiondate", sessionDateStr);
                    intent.putExtra("sessionName", familysessionnameStr);
                    intent.putExtra("location", familylocationStr);
                    intent.putExtra("sessionfees", familysessionfeesStr);
                    intent.putExtra("sessionStudent", familysessionStudentStr);
                    intent.putExtra("city", locationStr);
                    intent.putExtra("SearchBy", searchByStr);
                    intent.putExtra("board", boardStr);
                    intent.putExtra("stream", streamStr);
                    intent.putExtra("standard", standardStr);
                    intent.putExtra("searchType", searchTypeStr);
                    intent.putExtra("lessionName", subjectStr);
                    intent.putExtra("gender", genderStr);
                    intent.putExtra("withOR", wheretoComeStr);
                    intent.putExtra("searchfront",searchfront);
                    intent.putExtra("sessionType",sessionType);
                    intent.putExtra("froncontanct",froncontanctStr);
                    intent.putExtra("firsttimesearch",firsttimesearch);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, FamilyListActivity.class);
                    intent.putExtra("froncontanct", froncontanctStr);
                    intent.putExtra("wheretocometype", wheretocometypeStr);
                    intent.putExtra("searchfront",searchfront);
                    intent.putExtra("froncontanct",froncontanctStr);
                    intent.putExtra("firsttimesearch",firsttimesearch);
                    startActivity(intent);
                }
            }
        });
        addStudentScreenBinding.dateOfBirthEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addStudentScreenBinding.dateOfBirthEdt.setError(null);
                datePickerDialog = DatePickerDialog.newInstance(AddStudentScreen.this, Year, Month, Day);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.setOkText("Done");
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.setAccentColor(Color.parseColor("#f2552c"));
//                datePickerDialog.setTitle("Select Date From DatePickerDialog");
                datePickerDialog.show(getFragmentManager(), "Datepickerdialog");
            }
        });
        addStudentScreenBinding.emailEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                emailStr = addStudentScreenBinding.emailEdt.getText().toString();
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    if (!emailStr.equalsIgnoreCase("") && Utils.isValidEmaillId(emailStr)) {
//                        callCheckEmailIdApi();
                    } else {
                        addStudentScreenBinding.emailEdt.setError("Please Enter Valid Email Address.");
                    }

                }
                return false;
            }
        });
        addStudentScreenBinding.passwordEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                passwordStr = addStudentScreenBinding.passwordEdt.getText().toString();
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    if (!passwordStr.equalsIgnoreCase("") && passwordStr.length() >= 4 && passwordStr.length() <= 8) {

                    } else {
                        addStudentScreenBinding.passwordEdt.setError("Password must be 4-8 Characters.");
                    }
                }
                return false;
            }
        });
        addStudentScreenBinding.genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                int radioButtonId = addStudentScreenBinding.genderGroup.getCheckedRadioButtonId();
                switch (radioButtonId) {
                    case R.id.male_chk:
                        gendarIdStr = addStudentScreenBinding.maleChk.getTag().toString();
                        break;
                    case R.id.female_chk:
                        gendarIdStr = addStudentScreenBinding.femaleChk.getTag().toString();
                        break;
                    default:
                }
            }
        });
        addStudentScreenBinding.phoneNoEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    addStudentScreenBinding.dateOfBirthEdt.setError(null);
                    datePickerDialog = DatePickerDialog.newInstance(AddStudentScreen.this, Year, Month, Day);
                    datePickerDialog.setThemeDark(false);
                    datePickerDialog.setOkText("Done");
                    datePickerDialog.showYearPickerFirst(false);
                    datePickerDialog.setAccentColor(Color.parseColor("#f2552c"));
//                datePickerDialog.setTitle("Select Date From DatePickerDialog");
                    datePickerDialog.show(getFragmentManager(), "Datepickerdialog");
                }

                return false;
            }
        });
        addStudentScreenBinding.classTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int radioButtonId = addStudentScreenBinding.classTypeGroup.getCheckedRadioButtonId();
                switch (radioButtonId) {
                    case R.id.self_chk:
                        classStr = addStudentScreenBinding.selfChk.getText().toString();
                        break;
                    case R.id.child_chk:
                        classStr = addStudentScreenBinding.childChk.getText().toString();
                        break;
                    case R.id.spouse_chk:
                        classStr = addStudentScreenBinding.spouseChk.getText().toString();
                        break;
                }
            }
        });
        addStudentScreenBinding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getInsertedValue();
                if (!contactTypeIDStr.equalsIgnoreCase("")) {
                    if (!firstNameStr.equalsIgnoreCase("")) {
                        if (!lastNameStr.equalsIgnoreCase("")) {
                            if (!emailStr.equalsIgnoreCase("") && Utils.isValidEmaillId(emailStr)) {
                                if (!passwordStr.equalsIgnoreCase("") && passwordStr.length() >= 4 && passwordStr.length() <= 8) {
                                    if (!phonenoStr.equalsIgnoreCase("") && phonenoStr.length() >= 10) {
                                        if (!gendarIdStr.equalsIgnoreCase("")) {
                                            if (!dateofbirthStr.equalsIgnoreCase("")) {
                                                callCheckEmailIdApi();
                                            } else {
                                                addStudentScreenBinding.dateOfBirthEdt.setError("Please Select Your Birth Date.");
                                            }
                                        } else {
                                            addStudentScreenBinding.femaleChk.setError("Select Gender.");
                                        }
                                    } else {
                                        addStudentScreenBinding.phoneNoEdt.setError("Enter 10 digit Phone Number.");
                                    }
                                } else {
                                    addStudentScreenBinding.passwordEdt.setError("Password must be 4-8 Characters.");
                                }
                            } else {
                                addStudentScreenBinding.emailEdt.setError("Please Enter Valid Email Addres.");
                            }
                        } else {
                            addStudentScreenBinding.lastNameEdt.setError("Please Enter LastName.");
                        }
                    } else {
                        addStudentScreenBinding.firstNameEdt.setError("Please Enter First Name");
                    }
                } else {
                    Utils.ping(mContext, "Please Select ClassType.");
                }
            }
        });
        addStudentScreenBinding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type.equalsIgnoreCase("book")) {
                    Intent intent = new Intent(mContext, FamilyListActivity.class);
                    intent.putExtra("type", "book");
                    intent.putExtra("familyID", familyIDStr);
                    intent.putExtra("familyNameStr", familyNameStr);
                    intent.putExtra("sessionID", sessionIDStr);
                    intent.putExtra("duration", durationStr);
                    intent.putExtra("sessiondate", sessionDateStr);
                    intent.putExtra("sessionName", familysessionnameStr);
                    intent.putExtra("location", familylocationStr);
                    intent.putExtra("sessionfees", familysessionfeesStr);
                    intent.putExtra("sessionStudent", familysessionStudentStr);
                    intent.putExtra("city", locationStr);
                    intent.putExtra("SearchBy", searchByStr);
                    intent.putExtra("board", boardStr);
                    intent.putExtra("stream", streamStr);
                    intent.putExtra("standard", standardStr);
                    intent.putExtra("searchType", searchTypeStr);
                    intent.putExtra("lessionName", subjectStr);
                    intent.putExtra("gender", genderStr);
                    intent.putExtra("withOR", wheretoComeStr);
                    intent.putExtra("searchfront",searchfront);
                    intent.putExtra("sessionType",sessionType);
                    intent.putExtra("froncontanct",froncontanctStr);
                    intent.putExtra("firsttimesearch",firsttimesearch);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, FamilyListActivity.class);
                    intent.putExtra("froncontanct", froncontanctStr);
                    intent.putExtra("wheretocometype", wheretocometypeStr);
                    intent.putExtra("searchfront",searchfront);
                    intent.putExtra("firsttimesearch",firsttimesearch);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

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

        MonthInt = d + "/" + m + "/" + y;
        addStudentScreenBinding.dateOfBirthEdt.setText(MonthInt);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        int age = 0;
        try {
            Date date1 = dateFormat.parse(addStudentScreenBinding.dateOfBirthEdt.getText().toString());
            Calendar now = Calendar.getInstance();
            Calendar dob = Calendar.getInstance();
            dob.setTime(date1);
            if (dob.after(now)) {
//                throw new IllegalArgumentException("Can't be born in the future");
//                Util.ping(mContext, "Can't be born in the future");
                addStudentScreenBinding.dateOfBirthEdt.setError("Can't be born in the future");
                addStudentScreenBinding.dateOfBirthEdt.setText("");
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
//            Util.ping(mContext, "Please Enter Valid Birthdate.");
            addStudentScreenBinding.dateOfBirthEdt.setError(getResources().getString(R.string.agevalidation));
            addStudentScreenBinding.dateOfBirthEdt.setText("");

        }
    }

    public void getInsertedValue() {
        firstNameStr = addStudentScreenBinding.firstNameEdt.getText().toString();
        lastNameStr = addStudentScreenBinding.lastNameEdt.getText().toString();
        emailStr = addStudentScreenBinding.emailEdt.getText().toString();
        passwordStr = addStudentScreenBinding.passwordEdt.getText().toString();
        phonenoStr = addStudentScreenBinding.phoneNoEdt.getText().toString();
        dateofbirthStr = addStudentScreenBinding.dateOfBirthEdt.getText().toString();

        for (int i = 0; i < classListInfo.getData().size(); i++) {
            if (classStr.equalsIgnoreCase(classListInfo.getData().get(i).getContactTypeName())) {
                contactTypeIDStr = classListInfo.getData().get(i).getContactTypeID();
            }
        }
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
                        callNewChildApi();

                        return;
                    }
                    if (teacherInfoModel.getSuccess().equalsIgnoreCase("True")) {
                        addStudentScreenBinding.emailEdt.setError("Already Exist!");
                        addStudentScreenBinding.emailEdt.setText("");
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
        map.put("EmailAddress", addStudentScreenBinding.emailEdt.getText().toString());

        return map;
    }

    //Use for Family add NewChild
    public void callNewChildApi() {
        if (Utils.isNetworkConnected(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_AddFamilyContact(getNewChildetail(), new retrofit.Callback<TeacherInfoModel>() {
                @Override
                public void success(TeacherInfoModel childInfoModel, Response response) {
                    Utils.dismissDialog();
                    if (childInfoModel == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (childInfoModel.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (childInfoModel.getSuccess().equalsIgnoreCase("false")) {
                        Utils.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (childInfoModel.getSuccess().equalsIgnoreCase("True")) {

                        Utils.setPref(mContext, "FamilyID", childInfoModel.getContactID());
                        contatIDstr = childInfoModel.getContactID();
//                        Utils.ping(mContext, "Child Added Sucessfully.");
                        if (type.equalsIgnoreCase("book")) {
                            ConformSessionDialog();
                        } else {
                            Intent intent = new Intent(mContext, FamilyListActivity.class);
                            intent.putExtra("froncontanct", froncontanctStr);
                            intent.putExtra("wheretocometype", wheretocometypeStr);
                            startActivity(intent);
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

    private Map<String, String> getNewChildetail() {
        Map<String, String> map = new HashMap<>();
        map.put("FamilyID", Utils.getPref(mContext, "coachTypeID"));
        map.put("FirstName", firstNameStr);
        map.put("LastName", lastNameStr);
        map.put("EmailAddress", emailStr);
        map.put("Password", passwordStr);
        map.put("GenderID", gendarIdStr);
        map.put("DateOfBirth", dateofbirthStr);
        map.put("PhoneNumber", phonenoStr);
        map.put("ContactTypeID", contactTypeIDStr);
        return map;
    }

    public void ConformSessionDialog() {

        confirmSessionDialogBinding = DataBindingUtil.
                inflate(LayoutInflater.from(mContext), R.layout.confirm_session_dialog, (ViewGroup) addStudentScreenBinding.getRoot(), false);

        confimDialog = new Dialog(mContext, R.style.Theme_Dialog);
        Window window = confimDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        confimDialog.getWindow().getAttributes().verticalMargin = 0.20f;
        wlp.gravity = Gravity.TOP;
        window.setAttributes(wlp);

        confimDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);

        confimDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        confimDialog.setCancelable(false);
        confimDialog.setContentView(confirmSessionDialogBinding.getRoot());

//        session_student_txt_view.setText("TEACHER NAME");

        confirmSessionDialogBinding.sessionTeacherTxt.setText(familysessionStudentStr);
        confirmSessionDialogBinding.sessionStudentTxt.setText(firstNameStr + " " + lastNameStr);
        confirmSessionDialogBinding.sessionNameTxt.setText(familysessionnameStr);
        confirmSessionDialogBinding.locationTxt.setText(familylocationStr);
        confirmSessionDialogBinding.durationTxt.setText(durationStr);
        confirmSessionDialogBinding.timeTxt.setText(sessionDateStr);

        if (familysessionfeesStr.equalsIgnoreCase("0.00")) {
            confirmSessionDialogBinding.sessionFeeTxt.setText("Free");
        } else {
            confirmSessionDialogBinding.sessionFeeTxt.setText("₹ " + familysessionfeesStr + " /-");
        }
        confirmSessionDialogBinding.cancelTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confimDialog.dismiss();
            }
        });
        confirmSessionDialogBinding.confirmTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!contatIDstr.equalsIgnoreCase("") && !sessionIDStr.equalsIgnoreCase("") && !familysessionfeesStr.equalsIgnoreCase("0.00")) {
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
        map.put("ContactID", Utils.getPref(mContext, "coachID"));
        map.put("SessionID", sessionIDStr);
        map.put("Amount", familysessionfeesStr);

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

    //Use for ClassTypeDetail
    public void callClassTypeDetailApi() {
        if (Utils.isNetworkConnected(mContext)) {

//            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_ContactType(getClassTypeDeatil(), new retrofit.Callback<TeacherInfoModel>() {
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

    private Map<String, String> getClassTypeDeatil() {
        Map<String, String> map = new HashMap<>();
        return map;
    }
}
