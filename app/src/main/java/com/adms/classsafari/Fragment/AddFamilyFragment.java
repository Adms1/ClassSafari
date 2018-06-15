package com.adms.classsafari.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import com.adms.classsafari.Activites.DashBoardActivity;
import com.adms.classsafari.AppConstant.ApiHandler;
import com.adms.classsafari.AppConstant.AppConfiguration;
import com.adms.classsafari.AppConstant.Utils;
import com.adms.classsafari.Model.TeacherInfo.TeacherInfoModel;
import com.adms.classsafari.R;
import com.adms.classsafari.databinding.ConfirmSessionTeacherBinding;
import com.adms.classsafari.databinding.FragmentAddFamilyBinding;
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

public class AddFamilyFragment extends Fragment implements DatePickerDialog.OnDateSetListener,
        View.OnClickListener, TextView.OnEditorActionListener, RadioGroup.OnCheckedChangeListener {

//    ConfirmSessionDialogBinding confirmSessionDialogBinding;
    ConfirmSessionTeacherBinding confirmSessionTeacherBinding;
    String MonthInt;
    int Year, Month, Day;
    Calendar calendar;
    int mYear, mMonth, mDay;
    String pageTitle, type, firstNameStr, lastNameStr, emailStr = "", passwordStr, phonenoStr, gendarIdStr = "1", dateofbirthStr, contactTypeIDStr, familyIDStr, contatIDstr, orderIDStr, sessionIDStr, classStr = "Child", familyNameStr, paymentStatusstr;
    Dialog confimDialog;
    //    TextView cancel_txt, confirm_txt, session_student_txt, session_name_txt, location_txt, duration_txt, time_txt, session_fee_txt, session_student_txt_view;
    TeacherInfoModel classListInfo;
    HashMap<Integer, String> spinnerClassMap;
    private FragmentAddFamilyBinding addFamilyBinding;
    private View rootView;
    private Context mContext;
    private DatePickerDialog datePickerDialog;
    SessiondetailConfirmationDialogBinding sessiondetailConfirmationDialogBinding;
    public AddFamilyFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        addFamilyBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_family, container, false);

        rootView = addFamilyBinding.getRoot();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mContext = getActivity();
        pageTitle = getArguments().getString("session");
        type = getArguments().getString("type");
        familyIDStr = getArguments().getString("familyID");
        familyNameStr = getArguments().getString("familyNameStr");
        sessionIDStr = Utils.getPref(mContext, "sessionID");
        ((DashBoardActivity) getActivity()).setActionBar(Integer.parseInt(pageTitle), "false");
        initViews();
        setListners();
        Log.d("ADMStype", type + familyNameStr);
        return rootView;
    }

    public void initViews() {
        calendar = Calendar.getInstance();
        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);
        callClassTypeDetailApi();
        if (type.equalsIgnoreCase("Family")) {
            addFamilyBinding.classTypeGroup.setVisibility(View.GONE);
            addFamilyBinding.linearFamilyName.setVisibility(View.GONE);
        } else {
            addFamilyBinding.classTypeGroup.setVisibility(View.VISIBLE);
            addFamilyBinding.linearFamilyName.setVisibility(View.VISIBLE);
            addFamilyBinding.familynameTxt.setText(familyNameStr);
        }
    }

    public void setListners() {
        addFamilyBinding.dateOfBirthEdt.setOnClickListener(this);
        addFamilyBinding.cancelBtn.setOnClickListener(this);
        addFamilyBinding.registerBtn.setOnClickListener(this);

        addFamilyBinding.emailEdt.setOnEditorActionListener(this);
        addFamilyBinding.passwordEdt.setOnEditorActionListener(this);
        addFamilyBinding.phoneNoEdt.setOnEditorActionListener(this);

        addFamilyBinding.genderGroup.setOnCheckedChangeListener(this);
        addFamilyBinding.classTypeGroup.setOnCheckedChangeListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_btn:
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
                                                addFamilyBinding.dateOfBirthEdt.setError("Please Select Your Birth Date.");
                                            }
                                        } else {
                                            addFamilyBinding.femaleChk.setError("Select Gender.");
                                        }
                                    } else {
                                        addFamilyBinding.phoneNoEdt.setError("Enter 10 digit Phone Number.");
                                    }
                                } else {
                                    addFamilyBinding.passwordEdt.setError("Password must be 4-8 Characters.");
                                }
                            } else {
                                addFamilyBinding.emailEdt.setError("Please Enter Valid Email Addres.");
                            }
                        } else {
                            addFamilyBinding.lastNameEdt.setError("Please Enter LastName.");
                        }
                    } else {
                        addFamilyBinding.firstNameEdt.setError("Please Enter First Name");
                    }
                } else {
                    Utils.ping(mContext, "Please Select ClassType.");
                }
                break;
            case R.id.cancel_btn:
                Fragment fragment = new OldFamilyListFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.date_of_birth_edt:
                addFamilyBinding.dateOfBirthEdt.setError(null);
                datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(AddFamilyFragment.this, Year, Month, Day);
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
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (radioGroup.getId()) {
            case R.id.gender_group:
                int radioButtonId = addFamilyBinding.genderGroup.getCheckedRadioButtonId();
                switch (radioButtonId) {
                    case R.id.male_chk:
                        gendarIdStr = addFamilyBinding.maleChk.getTag().toString();
                        break;
                    case R.id.female_chk:
                        gendarIdStr = addFamilyBinding.femaleChk.getTag().toString();
                        break;
                    default:
                }
                break;
            case R.id.class_type_group:
                int radioButtonId1 = addFamilyBinding.classTypeGroup.getCheckedRadioButtonId();
                switch (radioButtonId1) {
                    case R.id.self_chk:
                        classStr = addFamilyBinding.selfChk.getText().toString();
                        break;
                    case R.id.child_chk:
                        classStr = addFamilyBinding.childChk.getText().toString();
                        break;
                    case R.id.spouse_chk:
                        classStr = addFamilyBinding.spouseChk.getText().toString();
                        break;
                }
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        switch (textView.getId()) {
            case R.id.phone_no_edt:
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    addFamilyBinding.dateOfBirthEdt.setError(null);
                    datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(AddFamilyFragment.this, Year, Month, Day);
                    datePickerDialog.setThemeDark(false);
                    datePickerDialog.setOkText("Done");
                    datePickerDialog.showYearPickerFirst(false);
                    datePickerDialog.setAccentColor(Color.parseColor("#f2552c"));
//                datePickerDialog.setTitle("Select Date From DatePickerDialog");
                    datePickerDialog.show(getActivity().getFragmentManager(), "Datepickerdialog");
                }
                break;
            case R.id.password_edt:
                passwordStr = addFamilyBinding.passwordEdt.getText().toString();
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    if (!passwordStr.equalsIgnoreCase("") && passwordStr.length() >= 4 && passwordStr.length() <= 8) {

                    } else {
                        addFamilyBinding.passwordEdt.setError("Password must be 4-8 Characters.");
                    }
                }
                break;
            case R.id.email_edt:
                emailStr = addFamilyBinding.emailEdt.getText().toString();
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    if (!emailStr.equalsIgnoreCase("") && Utils.isValidEmaillId(emailStr)) {
//                        callCheckEmailIdApi();
                    } else {
                        addFamilyBinding.emailEdt.setError("Please Enter Valid Email Address.");
                    }

                }
                break;
        }
        return false;
    }

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
        addFamilyBinding.dateOfBirthEdt.setText(MonthInt);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        int age = 0;
        try {
            Date date1 = dateFormat.parse(addFamilyBinding.dateOfBirthEdt.getText().toString());
            Calendar now = Calendar.getInstance();
            Calendar dob = Calendar.getInstance();
            dob.setTime(date1);
            if (dob.after(now)) {
                addFamilyBinding.dateOfBirthEdt.setError("Can't be born in the future");
                addFamilyBinding.dateOfBirthEdt.setText("");
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
            addFamilyBinding.dateOfBirthEdt.setError(getResources().getString(R.string.agevalidation));
            addFamilyBinding.dateOfBirthEdt.setText("");

        }
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
//                        Utils.ping(mContext, "Family Added Sucessfully.");
//                        ConformationDialog();
                        SessionConfirmationDialog();
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
                        if (type.equalsIgnoreCase("Family")) {
                            callFamilyApi();
                        } else {
                            callNewChildApi();
                        }
                        return;
                    }
                    if (teacherInfoModel.getSuccess().equalsIgnoreCase("True")) {
                        addFamilyBinding.emailEdt.setError("Already Exist!");
                        addFamilyBinding.emailEdt.setText("");
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
        map.put("EmailAddress", addFamilyBinding.emailEdt.getText().toString());

        return map;
    }


    public void getInsertedValue() {
        firstNameStr = addFamilyBinding.firstNameEdt.getText().toString();
        lastNameStr = addFamilyBinding.lastNameEdt.getText().toString();
        emailStr = addFamilyBinding.emailEdt.getText().toString();
        passwordStr = addFamilyBinding.passwordEdt.getText().toString();
        phonenoStr = addFamilyBinding.phoneNoEdt.getText().toString();
        dateofbirthStr = addFamilyBinding.dateOfBirthEdt.getText().toString();
        if (type.equalsIgnoreCase("Family")) {
            contactTypeIDStr = "1";
        } else {
            for (int i = 0; i < classListInfo.getData().size(); i++) {
                if (classStr.equalsIgnoreCase(classListInfo.getData().get(i).getContactTypeName())) {
                    contactTypeIDStr = classListInfo.getData().get(i).getContactTypeID();
                }
            }
        }
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
//                        ConformationDialog();
                        SessionConfirmationDialog();
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
        map.put("FamilyID", familyIDStr);
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

    public void ConformationDialog() {
        confirmSessionTeacherBinding = DataBindingUtil.
                inflate(LayoutInflater.from(mContext), R.layout.confirm_session_teacher, (ViewGroup) addFamilyBinding.getRoot(), false);

        confimDialog = new Dialog(getActivity(), R.style.Theme_Dialog);
        Window window = confimDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        confimDialog.getWindow().getAttributes().verticalMargin = 0.10f;
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);

        confimDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);

        confimDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        confimDialog.setCancelable(false);
        confimDialog.setContentView(confirmSessionTeacherBinding.getRoot());
//        confirmSessionTeacherBinding.cancelTxt.setVisibility(View.GONE);
//
//        confirmSessionTeacherBinding.cancelTxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                confimDialog.dismiss();
//            }
//        });
        confirmSessionTeacherBinding.confirmTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!contatIDstr.equalsIgnoreCase("") && !sessionIDStr.equalsIgnoreCase("") && !AppConfiguration.SessionPrice.equalsIgnoreCase("0")) {
                    callpaymentRequestApi();
                } else {
                    paymentStatusstr = "1";
                    callSessionConfirmationApi();
                }
                confimDialog.dismiss();
            }
        });
        if (type.equalsIgnoreCase("Child")) {
            confirmSessionTeacherBinding.sessionStudentTxtView.setText("STUDENT NAME");
        } else {
            confirmSessionTeacherBinding.sessionStudentTxtView.setText("FAMILY NAME");
        }
        confirmSessionTeacherBinding.sessionTeacherTxt.setText(Utils.getPref(mContext, "RegisterUserName"));
        confirmSessionTeacherBinding.sessionNameTxt.setText(AppConfiguration.SessionName);
        confirmSessionTeacherBinding.locationTxt.setText(AppConfiguration.SessionLocation);
        confirmSessionTeacherBinding.durationTxt.setText(AppConfiguration.SessionDuration);
        confirmSessionTeacherBinding.timeTxt.setText(AppConfiguration.SessionTime);
        confirmSessionTeacherBinding.sessionStudentTxt.setText(firstNameStr + " " + lastNameStr);
        if (AppConfiguration.SessionPrice.equalsIgnoreCase("0")) {
            confirmSessionTeacherBinding.sessionFeeTxt.setText("Free");
        } else {
            confirmSessionTeacherBinding.sessionFeeTxt.setText("₹ " + AppConfiguration.SessionPrice);
        }
        AppConfiguration.UserName = confirmSessionTeacherBinding.sessionStudentTxt.getText().toString();
        confimDialog.show();

    }

    public void SessionConfirmationDialog() {
        sessiondetailConfirmationDialogBinding = DataBindingUtil.
                inflate(LayoutInflater.from(mContext), R.layout.sessiondetail_confirmation_dialog, (ViewGroup) addFamilyBinding.getRoot(), false);
        confimDialog = new Dialog(mContext, R.style.Theme_Dialog);
        Window window = confimDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        confimDialog.getWindow().getAttributes().verticalMargin = 0.20f;
        wlp.gravity = Gravity.TOP;
        window.setAttributes(wlp);

        confimDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);

        confimDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        confimDialog.setCancelable(false);
        confimDialog.setContentView(sessiondetailConfirmationDialogBinding.getRoot());
        AppConfiguration.UserName = firstNameStr + " " + lastNameStr;
        sessiondetailConfirmationDialogBinding.tutorNameTxt.setText(Utils.getPref(mContext, "RegisterUserName"));
        sessiondetailConfirmationDialogBinding.sessionNameTxt.setText(AppConfiguration.SessionName);
        sessiondetailConfirmationDialogBinding.locationTxt.setText(AppConfiguration.RegionName);
        sessiondetailConfirmationDialogBinding.durationTxt.setText(AppConfiguration.SessionDuration);
        sessiondetailConfirmationDialogBinding.startDateTxt.setText(AppConfiguration.SessionDate);
        sessiondetailConfirmationDialogBinding.endDateTxt.setText(AppConfiguration.SessionDate);

        String[] spiltTime = AppConfiguration.SessionTime.split("\\-");
        AppConfiguration.UserName=familyNameStr;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        SimpleDateFormat outsdf = new SimpleDateFormat("EEE");
        Date date = null;
        String inputstr = null;
        try {
            date = sdf.parse(AppConfiguration.SessionDate);
            inputstr = outsdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.d("DateTime", inputstr);
        switch (inputstr.toLowerCase()) {
            case "sun":
                sessiondetailConfirmationDialogBinding.sunTimeTxt.setEnabled(true);
                sessiondetailConfirmationDialogBinding.sundayBtn.setEnabled(true);
                sessiondetailConfirmationDialogBinding.sunTimeTxt.setAlpha(1);
                sessiondetailConfirmationDialogBinding.sundayBtn.setAlpha(1);
                sessiondetailConfirmationDialogBinding.sunTimeTxt.setText(spiltTime[0]);
                break;
            case "mon":
                sessiondetailConfirmationDialogBinding.monTimeTxt.setEnabled(true);
                sessiondetailConfirmationDialogBinding.mondayBtn.setEnabled(true);
                sessiondetailConfirmationDialogBinding.monTimeTxt.setAlpha(1);
                sessiondetailConfirmationDialogBinding.mondayBtn.setAlpha(1);
                sessiondetailConfirmationDialogBinding.monTimeTxt.setText(spiltTime[0]);
                break;
            case "tue":
                sessiondetailConfirmationDialogBinding.tuesTimeTxt.setEnabled(true);
                sessiondetailConfirmationDialogBinding.tuesdayBtn.setEnabled(true);
                sessiondetailConfirmationDialogBinding.tuesTimeTxt.setAlpha(1);
                sessiondetailConfirmationDialogBinding.tuesdayBtn.setAlpha(1);
                sessiondetailConfirmationDialogBinding.tuesTimeTxt.setText(spiltTime[0]);
                break;
            case "wed":
                sessiondetailConfirmationDialogBinding.wedTimeTxt.setEnabled(true);
                sessiondetailConfirmationDialogBinding.wednesdayBtn.setEnabled(true);
                sessiondetailConfirmationDialogBinding.wedTimeTxt.setAlpha(1);
                sessiondetailConfirmationDialogBinding.wednesdayBtn.setAlpha(1);
                sessiondetailConfirmationDialogBinding.wedTimeTxt.setText(spiltTime[0]);
                break;
            case "thu":
                sessiondetailConfirmationDialogBinding.thurTimeTxt.setEnabled(true);
                sessiondetailConfirmationDialogBinding.thursdayBtn.setEnabled(true);
                sessiondetailConfirmationDialogBinding.thurTimeTxt.setAlpha(1);
                sessiondetailConfirmationDialogBinding.thursdayBtn.setAlpha(1);
                sessiondetailConfirmationDialogBinding.thurTimeTxt.setText(spiltTime[0]);
                break;
            case "fri":
                sessiondetailConfirmationDialogBinding.friTimeTxt.setEnabled(true);
                sessiondetailConfirmationDialogBinding.fridayBtn.setEnabled(true);
                sessiondetailConfirmationDialogBinding.friTimeTxt.setAlpha(1);
                sessiondetailConfirmationDialogBinding.fridayBtn.setAlpha(1);
                sessiondetailConfirmationDialogBinding.friTimeTxt.setText(spiltTime[0]);
                break;
            case "sat":
                sessiondetailConfirmationDialogBinding.satTimeTxt.setEnabled(true);
                sessiondetailConfirmationDialogBinding.saturdayBtn.setEnabled(true);
                sessiondetailConfirmationDialogBinding.satTimeTxt.setAlpha(1);
                sessiondetailConfirmationDialogBinding.saturdayBtn.setAlpha(1);
                sessiondetailConfirmationDialogBinding.satTimeTxt.setText(spiltTime[0]);
                break;
            default:

        }
//        sessiondetailConfirmationDialogBinding.cancelTxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                confimDialog.dismiss();
//            }
//        });
        sessiondetailConfirmationDialogBinding.confirmTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!contatIDstr.equalsIgnoreCase("") && !sessionIDStr.equalsIgnoreCase("") && !AppConfiguration.SessionPrice.equalsIgnoreCase("0")) {
                    callpaymentRequestApi();
                } else {
                    paymentStatusstr="1";
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
                        Fragment fragment = new PaymentFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        Bundle args = new Bundle();
                        args.putString("orderID", orderIDStr);
                        args.putString("amount", AppConfiguration.SessionPrice);
                        args.putString("mode",AppConfiguration.Mode);
                        args.putString("username",firstNameStr + " " + lastNameStr);
                        args.putString("sessionID", sessionIDStr);
                        args.putString("contactID", contatIDstr);
                        args.putString("type", type);
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
                        if (type.equalsIgnoreCase("Child")) {
                            Utils.ping(mContext, "Child Confirmation Successfully.");
                        } else {
                            Utils.ping(mContext, "Family Confirmation Successfully.");
                        }
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

