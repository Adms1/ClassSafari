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
import com.adms.classsafari.databinding.FragmentAddFamilyBinding;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class AddFamilyFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private FragmentAddFamilyBinding addFamilyBinding;
    private View rootView;
    private Context mContext;
    String MonthInt;
    int Year, Month, Day;
    Calendar calendar;
    private DatePickerDialog datePickerDialog;
    int mYear, mMonth, mDay;
    String pageTitle, type, firstNameStr, lastNameStr, emailStr = "", passwordStr, phonenoStr, gendarIdStr = "1", dateofbirthStr, contactTypeIDStr, familyIDStr, contatIDstr, orderIDStr, sessionIDStr, classStr = "Child", familyNameStr;
    Dialog confimDialog;
    TextView cancel_txt, confirm_txt, session_student_txt, session_name_txt, location_txt, duration_txt, time_txt, session_fee_txt, session_student_txt_view;
    TeacherInfoModel classListInfo;
    HashMap<Integer, String> spinnerClassMap;

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
        sessionIDStr = Utils.getPref(mContext, "SessionID");
        ((DashBoardActivity) getActivity()).setActionBar(Integer.parseInt(pageTitle), "false");
        initViews();
        setListners();
        Log.d("type", type);
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
            addFamilyBinding.familynameTxt.setVisibility(View.GONE);
            addFamilyBinding.familynameTxtView.setVisibility(View.GONE);
        } else {
            addFamilyBinding.classTypeGroup.setVisibility(View.VISIBLE);
            addFamilyBinding.familynameTxtView.setVisibility(View.VISIBLE);
            addFamilyBinding.familynameTxt.setVisibility(View.VISIBLE);
            addFamilyBinding.familynameTxt.setText(familyNameStr);
        }
    }

    public void setListners() {
        addFamilyBinding.dateOfBirthEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFamilyBinding.dateOfBirthEdt.setError(null);
                datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(AddFamilyFragment.this, Year, Month, Day);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.setOkText("Done");
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.setAccentColor(Color.parseColor("#f2552c"));
//                datePickerDialog.setTitle("Select Date From DatePickerDialog");
                datePickerDialog.show(getActivity().getFragmentManager(), "Datepickerdialog");
            }
        });

        addFamilyBinding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new OldFamilyListFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        addFamilyBinding.emailEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                emailStr = addFamilyBinding.emailEdt.getText().toString();
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    if (!emailStr.equalsIgnoreCase("") && Utils.isValidEmaillId(emailStr)) {
                    } else {
                        addFamilyBinding.emailEdt.setError("Please Enter Valid Email Address.");
                    }

                }
                return false;
            }
        });
        addFamilyBinding.passwordEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                passwordStr = addFamilyBinding.passwordEdt.getText().toString();
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    if (!passwordStr.equalsIgnoreCase("") && passwordStr.length() >= 4 && passwordStr.length() <= 8) {

                    } else {
                        addFamilyBinding.passwordEdt.setError("Password must be 4-8 Characters.");
                    }
                }
                return false;
            }
        });
        addFamilyBinding.genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
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
            }
        });
        addFamilyBinding.phoneNoEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
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

                return false;
            }
        });
        addFamilyBinding.classTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int radioButtonId = addFamilyBinding.classTypeGroup.getCheckedRadioButtonId();
                switch (radioButtonId) {
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
            }
        });
//        addFamilyBinding.classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                ((TextView)adapterView.getChildAt(i)).setTextColor(Color.parseColor("#ffffff"));
//                String name = addFamilyBinding.classSpinner.getSelectedItem().toString();
//                String getid = spinnerClassMap.get(addFamilyBinding.classSpinner.getSelectedItemPosition());
//
//                Log.d("value", name + " " + getid);
//                contactTypeIDStr = getid.toString();
//                Log.d("classTypeIDStr", contactTypeIDStr);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
        addFamilyBinding.registerBtn.setOnClickListener(new View.OnClickListener() {
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
                                                if (type.equalsIgnoreCase("Family")) {
                                                    callFamilyApi();
                                                } else {
                                                    callNewChildApi();
                                                }
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
            }
        });
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
//                throw new IllegalArgumentException("Can't be born in the future");
//                Utils.ping(mContext, "Can't be born in the future");
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

        if (age >= 5) {
        } else {
//            Utils.ping(mContext, "Please Enter Valid Birthdate.");
            addFamilyBinding.dateOfBirthEdt.setError("Please Enter Valid Birthdate.");
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
                        ConformationDialog();
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
                        Utils.ping(mContext, getString(R.string.false_msg));
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
                        contatIDstr = childInfoModel.getContactID();
//                        Utils.ping(mContext, "Child Added Sucessfully.");
                        ConformationDialog();
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
        confimDialog = new Dialog(getActivity(), R.style.Theme_Dialog);
        Window window = confimDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        confimDialog.getWindow().getAttributes().verticalMargin = 0.10f;
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);

        confimDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);

        confimDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        confimDialog.setCancelable(false);
        confimDialog.setContentView(R.layout.confirm_session_dialog);
        session_student_txt_view = (TextView) confimDialog.findViewById(R.id.session_student_txt_view);
        session_student_txt = (TextView) confimDialog.findViewById(R.id.session_student_txt);
        session_name_txt = (TextView) confimDialog.findViewById(R.id.session_name_txt);
        location_txt = (TextView) confimDialog.findViewById(R.id.location_txt);
        duration_txt = (TextView) confimDialog.findViewById(R.id.duration_txt);
        time_txt = (TextView) confimDialog.findViewById(R.id.time_txt);
        session_fee_txt = (TextView) confimDialog.findViewById(R.id.session_fee_txt);
        confirm_txt = (TextView) confimDialog.findViewById(R.id.confirm_txt);
        cancel_txt = (TextView) confimDialog.findViewById(R.id.cancel_txt);

        cancel_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confimDialog.dismiss();
            }
        });
        confirm_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!contatIDstr.equalsIgnoreCase("") && !sessionIDStr.equalsIgnoreCase("") && !AppConfiguration.SessionPrice.equalsIgnoreCase("0")) {
                    callpaymentRequestApi();
                } else {
                    callSessionConfirmationApi();
                }
                confimDialog.dismiss();
            }
        });
        if (type.equalsIgnoreCase("Child")) {
            session_student_txt_view.setText("STUDENT NAME");
        } else {
            session_student_txt_view.setText("FAMILY NAME");
        }
        session_name_txt.setText(AppConfiguration.SessionName);
        location_txt.setText(AppConfiguration.SessionLocation);
        duration_txt.setText(AppConfiguration.SessionDuration);
        time_txt.setText(AppConfiguration.SessionTime);
        session_student_txt.setText(firstNameStr + " " + lastNameStr);
        if (AppConfiguration.SessionPrice.equalsIgnoreCase("0")) {
            session_fee_txt.setText("Free");
        } else {
            session_fee_txt.setText("₹ " + AppConfiguration.SessionPrice);
        }
        AppConfiguration.UserName = session_student_txt.getText().toString();
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
                        args.putString("mode", "LIVE");
                        args.putString("username", session_student_txt.getText().toString());
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

    public void fillClassTypeSpinner() {
//        for (int i = 0; i < classListInfo.getData().size(); i++) {
//
//        }
//        ArrayList<Integer> classTypeId = new ArrayList<Integer>();
//        for (int i = 0; i < classListInfo.getData().size(); i++) {
//            classTypeId.add(Integer.valueOf(classListInfo.getData().get(i).getContactTypeID()));
//        }
//        ArrayList<String> className = new ArrayList<String>();
//        for (int j = 0; j < classListInfo.getData().size(); j++) {
//            className.add(classListInfo.getData().get(j).getContactTypeName());
//        }
//
//        String[] spinnerclassIdArray = new String[classTypeId.size()];
//
//        spinnerClassMap = new HashMap<Integer, String>();
//        for (int i = 0; i < classTypeId.size(); i++) {
//            spinnerClassMap.put(i, String.valueOf(classTypeId.get(i)));
//            spinnerclassIdArray[i] = className.get(i).trim();
//        }
//        try {
//            Field popup = Spinner.class.getDeclaredField("mPopup");
//            popup.setAccessible(true);
//
//            // Get private mPopup member variable and try cast to ListPopupWindow
//            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(addFamilyBinding.classSpinner);
//
//            popupWindow.setHeight(spinnerclassIdArray.length > 4 ? 500 : spinnerclassIdArray.length * 100);
//        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
//            // silently fail...
//        }
//
//        ArrayAdapter<String> adapterTerm = new ArrayAdapter<String>(mContext, R.layout.spinner_layout, spinnerclassIdArray);
//        addFamilyBinding.classSpinner.setAdapter(adapterTerm);
    }
}

