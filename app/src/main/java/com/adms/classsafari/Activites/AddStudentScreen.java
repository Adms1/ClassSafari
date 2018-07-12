package com.adms.classsafari.Activites;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.RadioGroup;
import android.widget.TextView;

import com.adms.classsafari.Adapter.ExapndableListAdapterFromFront;
import com.adms.classsafari.Adapter.ExpandableListAdapterIn;
import com.adms.classsafari.AppConstant.ApiHandler;
import com.adms.classsafari.AppConstant.AppConfiguration;
import com.adms.classsafari.AppConstant.Utils;
import com.adms.classsafari.Interface.onChlidClick;
import com.adms.classsafari.Interface.onViewClick;
import com.adms.classsafari.Model.Session.SessionDetailModel;
import com.adms.classsafari.Model.TeacherInfo.TeacherInfoModel;
import com.adms.classsafari.R;
import com.adms.classsafari.databinding.ActivityAddStudentScreenBinding;
import com.adms.classsafari.databinding.ChangePasswordDialogBinding;
import com.adms.classsafari.databinding.ConfirmSessionDialogBinding;
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
import rx.internal.operators.BackpressureUtils;

public class AddStudentScreen extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    ActivityAddStudentScreenBinding addStudentScreenBinding;
    ConfirmSessionDialogBinding confirmSessionDialogBinding;
    ChangePasswordDialogBinding changePasswordDialogBinding;
    SessiondetailConfirmationDialogBinding sessiondetailConfirmationDialogBinding;
    SessionDetailModel dataResponse;
    String fNstr, lNstr, cIdstr, eAstr, gIdstr, dobstr, pNstr, cnIdstr, updateprofilestr = "", updateTypestr;
    Context mContext;
    String MonthInt;
    int Year, Month, Day;
    Calendar calendar;
    int mYear, mMonth, mDay;
    String pageTitle, type, firstNameStr, lastNameStr, emailStr = "", passwordStr = "",
            phonenoStr, gendarIdStr = "1", dateofbirthStr, contactTypeIDStr,
            familyIDStr, contatIDstr, orderIDStr, sessionIDStr, classStr = "Child",
            familyNameStr, paymentStatusstr, familylocationStr, familysessionStudentStr, TeacherName,
            sessionDateStr, durationStr, familysessionfeesStr, familysessionnameStr, locationStr,
            boardStr, standardStr, streamStr, subjectStr, froncontanctStr, RegionName,
    /*wheretoComeStr, searchByStr,searchTypeStr,*/ genderStr, wheretocometypeStr, myprofile, searchfront, sessionType, firsttimesearch, backStr, SearchPlaystudy;
    Dialog confimDialog;
    TeacherInfoModel classListInfo;
    int SessionHour = 0;
    Integer SessionMinit = 0;
    String SessionDuration;
    String hours, minit;
    boolean callservice = false;
    ArrayList<Integer> totalHours;
    ArrayList<Integer> totalMinit;
    int avgHoursvalue, avgMinitvalue;
    //Use for Menu Dialog
    String passWordStr, confirmpassWordStr, currentpasswordStr;
    Dialog menuDialog, changeDialog;
    Button btnHome, btnMyReport, btnMySession, btnChangePassword, btnaddChild, btnLogout, btnmyfamily, btnMyenroll, btnMyprofile;
    TextView userNameTxt;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addStudentScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_student_screen);
        mContext = AddStudentScreen.this;
        init();
        setListner();
    }

    public void init() {

        if (!Utils.getPref(mContext, "LoginType").equalsIgnoreCase("Family")) {
            addStudentScreenBinding.menu.setVisibility(View.GONE);
        } else {
            addStudentScreenBinding.menu.setVisibility(View.VISIBLE);
        }

        calendar = Calendar.getInstance();
        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);

        callClassTypeDetailApi();
        backStr = getIntent().getStringExtra("back");
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
//        searchByStr = getIntent().getStringExtra("SearchBy");
        standardStr = getIntent().getStringExtra("standard");
        streamStr = getIntent().getStringExtra("stream");
        boardStr = getIntent().getStringExtra("board");
        genderStr = getIntent().getStringExtra("gender");
//        wheretoComeStr = getIntent().getStringExtra("withOR");
        froncontanctStr = getIntent().getStringExtra("froncontanct");
        wheretocometypeStr = getIntent().getStringExtra("wheretocometype");
        searchfront = getIntent().getStringExtra("searchfront");
        sessionType = getIntent().getStringExtra("sessionType");
        firsttimesearch = getIntent().getStringExtra("firsttimesearch");
        RegionName = getIntent().getStringExtra("RegionName");
        TeacherName = getIntent().getStringExtra("TeacherName");
        SearchPlaystudy = getIntent().getStringExtra("SearchPlaystudy");
        fNstr = getIntent().getStringExtra("firstName");
        lNstr = getIntent().getStringExtra("lastName");
        cIdstr = getIntent().getStringExtra("contactId");
        eAstr = getIntent().getStringExtra("email");
        gIdstr = getIntent().getStringExtra("gender");
        dobstr = getIntent().getStringExtra("dob");
        pNstr = getIntent().getStringExtra("phone");
        cnIdstr = getIntent().getStringExtra("contacttypeId");
        updateprofilestr = getIntent().getStringExtra("updateProfile");
        updateTypestr = getIntent().getStringExtra("Family");
        myprofile = getIntent().getStringExtra("myprofile");
        Log.d("familyName", familyNameStr + familyIDStr);
        addStudentScreenBinding.familynameTxt.setText(Utils.getPref(mContext, "RegisterUserName"));


        if (getIntent().getStringExtra("updateProfile") != null) {
            if (updateprofilestr.equalsIgnoreCase("true")) {
                setUpdateData();
            }
        }
        if (getIntent().getStringExtra("myprofile") != null) {
            addStudentScreenBinding.familyNameLinear.setVisibility(View.GONE);
            addStudentScreenBinding.registerBtn.setVisibility(View.GONE);

            addStudentScreenBinding.firstNameEdt.setEnabled(false);
            addStudentScreenBinding.lastNameEdt.setEnabled(false);
            addStudentScreenBinding.emailEdt.setEnabled(false);
            addStudentScreenBinding.phoneNoEdt.setEnabled(false);
            addStudentScreenBinding.dateOfBirthEdt.setEnabled(false);
            addStudentScreenBinding.maleChk.setEnabled(false);
            addStudentScreenBinding.femaleChk.setEnabled(false);
            callFamilyListApi();
        }
    }

    public void setUpdateData() {
        if (updateTypestr.equalsIgnoreCase("Family")) {
            addStudentScreenBinding.familyNameLinear.setVisibility(View.GONE);
            addStudentScreenBinding.addStudentTxt.setText("Update Family");
            addStudentScreenBinding.classTypeGroup.setVisibility(View.GONE);
            addStudentScreenBinding.emailEdt.setVisibility(View.VISIBLE);
            addStudentScreenBinding.emailEdt.setText(eAstr);
            addStudentScreenBinding.emailEdt.setEnabled(false);
            addStudentScreenBinding.phoneNoEdt.setVisibility(View.VISIBLE);
            addStudentScreenBinding.phoneNoEdt.setText(pNstr);

        } else {
            addStudentScreenBinding.addStudentTxt.setText("Update Contact");
            addStudentScreenBinding.firstNameEdt.setText(fNstr);
            addStudentScreenBinding.lastNameEdt.setText(lNstr);
            addStudentScreenBinding.familynameTxt.setText(familyNameStr);
            addStudentScreenBinding.dateOfBirthEdt.setText(dobstr);
            if (cnIdstr.equalsIgnoreCase("Child")) {
                addStudentScreenBinding.childChk.setChecked(true);
            } else if (cnIdstr.equalsIgnoreCase("Spouse")) {
                addStudentScreenBinding.spouseChk.setChecked(true);
            }
                addStudentScreenBinding.lastNameEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        if (i == EditorInfo.IME_ACTION_NEXT) {
                            InputMethodManager imm = (InputMethodManager) textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);

                            addStudentScreenBinding.dateOfBirthEdt.requestFocus();
                        }
                        return false;
                    }
                });
        }
        addStudentScreenBinding.registerBtn.setText("Update");
        addStudentScreenBinding.firstNameEdt.setText(fNstr);
        addStudentScreenBinding.lastNameEdt.setText(lNstr);
        String[] splitdate = dobstr.split("\\s+");
        addStudentScreenBinding.dateOfBirthEdt.setText(splitdate[0]);

        if (gIdstr.equalsIgnoreCase("1")) {
            addStudentScreenBinding.maleChk.setChecked(true);
        } else {
            addStudentScreenBinding.femaleChk.setChecked(true);
        }
    }

    public void setListner() {

        addStudentScreenBinding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuDialog();
            }
        });
        addStudentScreenBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type.equalsIgnoreCase("book")) {
                    Intent intent = new Intent(mContext, FamilyListActivity.class);
                    intent.putExtra("type", "book");
                    intent.putExtra("familyID", familyIDStr);
                    intent.putExtra("familyNameStr", familyNameStr);
                    intent.putExtra("sessionID", sessionIDStr);
//                    intent.putExtra("duration", durationStr);
//                    intent.putExtra("sessiondate", sessionDateStr);
                    intent.putExtra("sessionName", familysessionnameStr);
//                    intent.putExtra("location", familylocationStr);
//                    intent.putExtra("sessionfees", familysessionfeesStr);
//                    intent.putExtra("sessionStudent", familysessionStudentStr);
                    intent.putExtra("city", locationStr);
//                    intent.putExtra("SearchBy", searchByStr);
                    intent.putExtra("board", boardStr);
                    intent.putExtra("stream", streamStr);
                    intent.putExtra("standard", standardStr);
                    intent.putExtra("lessionName", subjectStr);
//                    intent.putExtra("gender", genderStr);
//                    intent.putExtra("withOR", wheretoComeStr);
                    intent.putExtra("searchfront", searchfront);
//                    intent.putExtra("sessionType", sessionType);
                    intent.putExtra("froncontanct", froncontanctStr);
                    intent.putExtra("firsttimesearch", firsttimesearch);
                    intent.putExtra("RegionName", RegionName);
                    intent.putExtra("back", backStr);
                    intent.putExtra("TeacherName", TeacherName);
                    intent.putExtra("SearchPlaystudy", SearchPlaystudy);
                    startActivity(intent);
                } else if (type.equalsIgnoreCase("menu")) {
                    Intent intent = new Intent(mContext, FamilyListActivity.class);
                    intent.putExtra("froncontanct", froncontanctStr);
                    intent.putExtra("wheretocometype", wheretocometypeStr);
                    intent.putExtra("searchfront", searchfront);
                    intent.putExtra("froncontanct", froncontanctStr);
                    intent.putExtra("firsttimesearch", firsttimesearch);
                    startActivity(intent);
                } else if (type.equalsIgnoreCase("myprofile")) {
                    if (wheretocometypeStr.equalsIgnoreCase("mySession")) {
                        Intent intent = new Intent(mContext, MySession.class);
                        intent.putExtra("wheretocometype", wheretocometypeStr);
                        startActivity(intent);
                    } else if (wheretocometypeStr.equalsIgnoreCase("myAccount")) {
                        Intent intent = new Intent(mContext, MyAccountActivity.class);
                        intent.putExtra("wheretocometype", wheretocometypeStr);
                        startActivity(intent);
                    } else if (wheretocometypeStr.equalsIgnoreCase("menu")) {
                        Intent intent = new Intent(mContext, SearchByUser.class);
                        intent.putExtra("wheretocometype", wheretocometypeStr);
                        startActivity(intent);
                    }
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
                    callservice = false;
                    if (!emailStr.equalsIgnoreCase("") && Utils.isValidEmaillId(emailStr)) {
                        //callCheckEmailIdApi();
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
//                            if (!emailStr.equalsIgnoreCase("") && Utils.isValidEmaillId(emailStr)) {
//                                if (!passwordStr.equalsIgnoreCase("") && passwordStr.length() >= 4 && passwordStr.length() <= 8) {
//                                    if (!phonenoStr.equalsIgnoreCase("") && phonenoStr.length() >= 10) {
                            if (!gendarIdStr.equalsIgnoreCase("")) {
                                if (!dateofbirthStr.equalsIgnoreCase("")) {
                                    //callservice=true;
                                    //callCheckEmailIdApi();
                                    if (updateprofilestr.equalsIgnoreCase("true")) {
                                        callUpdateFamilyContactApi();
                                    } else {
                                        callNewChildApi();
                                    }
                                } else {
                                    addStudentScreenBinding.dateOfBirthEdt.setError("Please select your birthdate");
                                }
                            } else {
                                addStudentScreenBinding.femaleChk.setError("Select gender");
                            }
//                                    } else {
//                                        addStudentScreenBinding.phoneNoEdt.setError("Enter 10 digit Phone Number.");
//                                    }
//                                } else {
//                                    addStudentScreenBinding.passwordEdt.setError("Password must be 4-8 Characters.");
//                                }
//                            } else {
//                                addStudentScreenBinding.emailEdt.setError("Please Enter Valid Email Addres.");
//                            }
                        } else {
                            addStudentScreenBinding.lastNameEdt.setError("Please enter last Name");
                        }
                    } else {
                        addStudentScreenBinding.firstNameEdt.setError("Please enter first name");
                    }
                } else {
                    Utils.ping(mContext, "Please select class type");
                }
            }
        });
//        addStudentScreenBinding.cancelBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (type.equalsIgnoreCase("book")) {
//                    Intent intent = new Intent(mContext, FamilyListActivity.class);
//                    intent.putExtra("type", "book");
//                    intent.putExtra("familyID", familyIDStr);
//                    intent.putExtra("familyNameStr", familyNameStr);
//                    intent.putExtra("sessionID", sessionIDStr);
////                    intent.putExtra("duration", durationStr);
////                    intent.putExtra("sessiondate", sessionDateStr);
//                    intent.putExtra("sessionName", familysessionnameStr);
////                    intent.putExtra("location", familylocationStr);
////                    intent.putExtra("sessionfees", familysessionfeesStr);
////                    intent.putExtra("sessionStudent", familysessionStudentStr);
//                    intent.putExtra("city", locationStr);
////                    intent.putExtra("SearchBy", searchByStr);
//                    intent.putExtra("board", boardStr);
//                    intent.putExtra("stream", streamStr);
//                    intent.putExtra("standard", standardStr);
//                    intent.putExtra("lessionName", subjectStr);
////                    intent.putExtra("gender", genderStr);
////                    intent.putExtra("withOR", wheretoComeStr);
//                    intent.putExtra("searchfront", searchfront);
////                    intent.putExtra("sessionType", sessionType);
//                    intent.putExtra("froncontanct", froncontanctStr);
//                    intent.putExtra("firsttimesearch", firsttimesearch);
//                    intent.putExtra("RegionName", RegionName);
//                    intent.putExtra("back", backStr);
//                    intent.putExtra("TeacherName",TeacherName);
//                    intent.putExtra("SearchPlaystudy",SearchPlaystudy);
//                    startActivity(intent);
//                } else {
//                    Intent intent = new Intent(mContext, FamilyListActivity.class);
//                    intent.putExtra("froncontanct", froncontanctStr);
//                    intent.putExtra("wheretocometype", wheretocometypeStr);
//                    intent.putExtra("searchfront", searchfront);
//                    intent.putExtra("firsttimesearch", firsttimesearch);
//                    startActivity(intent);
//                }
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (type.equalsIgnoreCase("book")) {
            Intent intent = new Intent(mContext, FamilyListActivity.class);
            intent.putExtra("type", "book");
            intent.putExtra("familyID", familyIDStr);
            intent.putExtra("familyNameStr", familyNameStr);
            intent.putExtra("sessionID", sessionIDStr);
//                    intent.putExtra("duration", durationStr);
//                    intent.putExtra("sessiondate", sessionDateStr);
            intent.putExtra("sessionName", familysessionnameStr);
//                    intent.putExtra("location", familylocationStr);
//                    intent.putExtra("sessionfees", familysessionfeesStr);
//                    intent.putExtra("sessionStudent", familysessionStudentStr);
            intent.putExtra("city", locationStr);
//                    intent.putExtra("SearchBy", searchByStr);
            intent.putExtra("board", boardStr);
            intent.putExtra("stream", streamStr);
            intent.putExtra("standard", standardStr);
            intent.putExtra("lessionName", subjectStr);
//                    intent.putExtra("gender", genderStr);
//                    intent.putExtra("withOR", wheretoComeStr);
            intent.putExtra("searchfront", searchfront);
//                    intent.putExtra("sessionType", sessionType);
            intent.putExtra("froncontanct", froncontanctStr);
            intent.putExtra("firsttimesearch", firsttimesearch);
            intent.putExtra("RegionName", RegionName);
            intent.putExtra("back", backStr);
            intent.putExtra("TeacherName", TeacherName);
            intent.putExtra("SearchPlaystudy", SearchPlaystudy);
            startActivity(intent);
        } else if (type.equalsIgnoreCase("menu")) {
            Intent intent = new Intent(mContext, FamilyListActivity.class);
            intent.putExtra("froncontanct", froncontanctStr);
            intent.putExtra("wheretocometype", wheretocometypeStr);
            intent.putExtra("searchfront", searchfront);
            intent.putExtra("froncontanct", froncontanctStr);
            intent.putExtra("firsttimesearch", firsttimesearch);
            startActivity(intent);
        } else if (type.equalsIgnoreCase("myprofile")) {
            if (wheretocometypeStr.equalsIgnoreCase("mySession")) {
                Intent intent = new Intent(mContext, MySession.class);
                intent.putExtra("wheretocometype", wheretocometypeStr);
                startActivity(intent);
            } else if (wheretocometypeStr.equalsIgnoreCase("myAccount")) {
                Intent intent = new Intent(mContext, MyAccountActivity.class);
                intent.putExtra("wheretocometype", wheretocometypeStr);
                startActivity(intent);
            } else if (wheretocometypeStr.equalsIgnoreCase("menu")) {
                Intent intent = new Intent(mContext, SearchByUser.class);
                intent.putExtra("wheretocometype", wheretocometypeStr);
                startActivity(intent);
            }
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

        MonthInt = d + "/" + m + "/" + y;
        addStudentScreenBinding.dateOfBirthEdt.setText(MonthInt);
        //Age Validation
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        int age = 0;
//        try {
//            Date date1 = dateFormat.parse(addStudentScreenBinding.dateOfBirthEdt.getText().toString());
//            Calendar now = Calendar.getInstance();
//            Calendar dob = Calendar.getInstance();
//            dob.setTime(date1);
//            if (dob.after(now)) {
////                throw new IllegalArgumentException("Can't be born in the future");
////                Util.ping(mContext, "Can't be born in the future");
//                addStudentScreenBinding.dateOfBirthEdt.setError("Can't be born in the future");
//                addStudentScreenBinding.dateOfBirthEdt.setText("");
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
////            Util.ping(mContext, "Please Enter Valid Birthdate.");
//            addStudentScreenBinding.dateOfBirthEdt.setError(getResources().getString(R.string.agevalidation));
//            addStudentScreenBinding.dateOfBirthEdt.setText("");
//
//        }
    }

    public void getInsertedValue() {
        firstNameStr = addStudentScreenBinding.firstNameEdt.getText().toString();
        lastNameStr = addStudentScreenBinding.lastNameEdt.getText().toString();
        emailStr = addStudentScreenBinding.emailEdt.getText().toString();
        passwordStr = addStudentScreenBinding.passwordEdt.getText().toString();
        phonenoStr = addStudentScreenBinding.phoneNoEdt.getText().toString();
        dateofbirthStr = addStudentScreenBinding.dateOfBirthEdt.getText().toString();

        for (int i = 0; i < classListInfo.getData().size(); i++) {
            if (updateprofilestr.equalsIgnoreCase("true")) {
                if (updateTypestr.equalsIgnoreCase("Family")) {
                    contactTypeIDStr = "1";
                } else {
                    if (classStr.equalsIgnoreCase(classListInfo.getData().get(i).getContactTypeName())) {
                        contactTypeIDStr = classListInfo.getData().get(i).getContactTypeID();
                    }
                    phonenoStr = pNstr;
                }
            } else {
                phonenoStr = pNstr;
                if (classStr.equalsIgnoreCase(classListInfo.getData().get(i).getContactTypeName())) {
                    contactTypeIDStr = classListInfo.getData().get(i).getContactTypeID();
                }
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

                        if (callservice == true) {
                            callNewChildApi();
                        }
                        callservice = false;
                        return;
                    }
                    if (teacherInfoModel.getSuccess().equalsIgnoreCase("True")) {
                        addStudentScreenBinding.emailEdt.setError("Already exist!");
                        addStudentScreenBinding.emailEdt.setText("");
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
                            SessionConfirmationDialog();
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

    //Use for Get FamilyList
    public void callFamilyListApi() {
        if (Utils.isNetworkConnected(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_FamiliyByFamilyID(getFamilyListDetail(), new retrofit.Callback<TeacherInfoModel>() {
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
                        return;
                    }
                    if (familyInfoModel.getSuccess().equalsIgnoreCase("True")) {
                        if (familyInfoModel.getData() != null) {
                            if (familyInfoModel.getData().size() > 0) {
                                if (myprofile.equalsIgnoreCase("true")) {
                                    addStudentScreenBinding.addStudentTxt.setText("My Profile");

                                    addStudentScreenBinding.classTypeGroup.setVisibility(View.GONE);
                                    addStudentScreenBinding.emailEdt.setVisibility(View.VISIBLE);
                                    addStudentScreenBinding.phoneNoEdt.setVisibility(View.VISIBLE);
                                    addStudentScreenBinding.dateOfBirthEdt.setVisibility(View.VISIBLE);

                                    for (int i = 0; i < familyInfoModel.getData().size(); i++) {
                                        addStudentScreenBinding.firstNameEdt.setText(familyInfoModel.getData().get(i).getFirstName());
                                        addStudentScreenBinding.lastNameEdt.setText(familyInfoModel.getData().get(i).getLastName());
                                        addStudentScreenBinding.emailEdt.setText(familyInfoModel.getData().get(i).getEmailAddress());
                                        addStudentScreenBinding.phoneNoEdt.setText(familyInfoModel.getData().get(i).getContactPhoneNumber());
                                        String[] splitdate = familyInfoModel.getData().get(i).getDateofBirth().split("\\s+");
                                        addStudentScreenBinding.dateOfBirthEdt.setText(splitdate[0]);

                                        if (familyInfoModel.getData().get(i).getGenderID().equalsIgnoreCase("1")) {
                                            addStudentScreenBinding.maleChk.setChecked(true);
                                        } else {
                                            addStudentScreenBinding.femaleChk.setChecked(false);
                                        }
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

    private Map<String, String> getFamilyListDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("FamilyID", Utils.getPref(mContext, "coachTypeID"));
        return map;
    }

    //Use for Family add NewChild
    public void callUpdateFamilyContactApi() {
        if (Utils.isNetworkConnected(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_Update_FamilyContact(getUpdateFamilyContactdetail(), new retrofit.Callback<TeacherInfoModel>() {
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
                        Utils.setPref(mContext, "RegisterUserName", firstNameStr + " " + lastNameStr);
                        Intent intent = new Intent(mContext, FamilyListActivity.class);
                        intent.putExtra("froncontanct", froncontanctStr);
                        intent.putExtra("wheretocometype", wheretocometypeStr);
                        startActivity(intent);
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

    private Map<String, String> getUpdateFamilyContactdetail() {
        Map<String, String> map = new HashMap<>();
        map.put("ContactID", cIdstr);
        map.put("FirstName", firstNameStr);
        map.put("LastName", lastNameStr);
        map.put("EmailAddress", emailStr);
        map.put("GenderID", gendarIdStr);
        map.put("DateOfBirth", dateofbirthStr);
        map.put("PhoneNumber", phonenoStr);
        map.put("ContactType_ID", contactTypeIDStr);
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
        map.put("ContactID", contatIDstr); // Utils.getPref(mContext, "coachID")
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
//                        Utils.ping(mContext, "Login succesfully");
                        confimDialog.dismiss();
                        Intent isearchBYuser = new Intent(mContext, PaymentSuccessActivity.class);
                        isearchBYuser.putExtra("transactionId", orderIDStr);
                        isearchBYuser.putExtra("responseCode", "0");
                        isearchBYuser.putExtra("order_id", orderIDStr);
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

    public void SessionConfirmationDialog() {
        sessiondetailConfirmationDialogBinding = DataBindingUtil.
                inflate(LayoutInflater.from(mContext), R.layout.sessiondetail_confirmation_dialog, (ViewGroup) addStudentScreenBinding.getRoot(), false);
        confimDialog = new Dialog(mContext, R.style.Theme_Dialog);
        Window window = confimDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        confimDialog.getWindow().getAttributes().verticalMargin = 0.20f;
        wlp.gravity = Gravity.TOP;
        window.setAttributes(wlp);

        //  confimDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);
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
                        if (!contatIDstr.equalsIgnoreCase("") && !sessionIDStr.equalsIgnoreCase("")) {////&& !AppConfiguration.classsessionPrice.equalsIgnoreCase("0.00")) {
                            callpaymentRequestApi();
                        }
//                        } else {
//                            paymentStatusstr = "1";
//                           callpaymentRequestApi();
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
                                AppConfiguration.bookingsubjectName = dataResponse.getData().get(j).getSessionName();
                                AppConfiguration.bookingteacherName = dataResponse.getData().get(j).getName();
                                AppConfiguration.bookingdate = dataResponse.getData().get(j).getStartDate();
                                AppConfiguration.bookingtime = dataResponse.getData().get(j).getSchedule();
                                AppConfiguration.bookingamount = dataResponse.getData().get(j).getSessionAmount();
                                sessiondetailConfirmationDialogBinding.sessionNameTxt.setText(dataResponse.getData().get(j).getSessionName());
                                sessiondetailConfirmationDialogBinding.ratingBar.setRating(Float.parseFloat(dataResponse.getData().get(j).getRating()));
//                                sessiondetailConfirmationDialogBinding.ratingUserTxt.setText();
                                sessiondetailConfirmationDialogBinding.tutorNameTxt.setText(dataResponse.getData().get(j).getName());
                                sessiondetailConfirmationDialogBinding.locationTxt.setText(dataResponse.getData().get(j).getRegionName());
                                sessiondetailConfirmationDialogBinding.startDateTxt.setText(dataResponse.getData().get(j).getStartDate());
                                sessiondetailConfirmationDialogBinding.endDateTxt.setText(dataResponse.getData().get(j).getEndDate());
                                if (dataResponse.getData().get(j).getSessionAmount().equalsIgnoreCase("0.00")) {
                                    sessiondetailConfirmationDialogBinding.priceTxt.setText("Free");
                                } else {
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
//                                        hours= "0"+SessionHour;
//                                    }else{
//                                        hours= String.valueOf(SessionHour);
//                                    }
//                                    if(SessionMinit<10){
//                                        minit="0"+SessionMinit;
//                                    }else{
//                                        minit=String.valueOf(SessionMinit);
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
//
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

    public void changePasswordDialog() {
        changePasswordDialogBinding = DataBindingUtil.
                inflate(LayoutInflater.from(mContext), R.layout.change_password_dialog, (ViewGroup) addStudentScreenBinding.getRoot(), false);

        changeDialog = new Dialog(mContext, R.style.Theme_Dialog);
        Window window = changeDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        changeDialog.getWindow().getAttributes().verticalMargin = 0.0f;
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);

        //changeDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);
        changeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        changeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        changeDialog.setCancelable(false);
        changeDialog.setContentView(changePasswordDialogBinding.getRoot());

        changePasswordDialogBinding.changepwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentpasswordStr = changePasswordDialogBinding.edtcurrentpassword.getText().toString();
                confirmpassWordStr = changePasswordDialogBinding.edtconfirmpassword.getText().toString();
                passWordStr = changePasswordDialogBinding.edtnewpassword.getText().toString();
                if (currentpasswordStr.equalsIgnoreCase(Utils.getPref(mContext, "Password"))) {
                    if (!passWordStr.equalsIgnoreCase("") && passWordStr.length() >= 4 && passWordStr.length() <= 8) {
                        if (passWordStr.equalsIgnoreCase(confirmpassWordStr)) {
                            callChangePasswordApi();
                        } else {
                            changePasswordDialogBinding.edtconfirmpassword.setError("Confirm Password does not match.");
                        }
                    } else {
                        changePasswordDialogBinding.edtnewpassword.setError("Password must be 4-8 Characters.");
                        changePasswordDialogBinding.edtnewpassword.setText("");
                        changePasswordDialogBinding.edtconfirmpassword.setText("");
                    }
                } else {
                    changePasswordDialogBinding.edtcurrentpassword.setError("Password does not match to current password.");
                }


            }
        });
        changePasswordDialogBinding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDialog.dismiss();
            }
        });

        changeDialog.show();

    }

    //USe for Change Password
    public void callChangePasswordApi() {
        if (Utils.isNetworkConnected(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_Change_Password(getChangePasswordDetail(), new retrofit.Callback<TeacherInfoModel>() {
                @Override
                public void success(TeacherInfoModel forgotInfoModel, Response response) {
                    Utils.dismissDialog();
                    if (forgotInfoModel == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (forgotInfoModel.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (forgotInfoModel.getSuccess().equalsIgnoreCase("false")) {
                        Utils.ping(mContext, "Please enter valid password");
                        return;
                    }
                    if (forgotInfoModel.getSuccess().equalsIgnoreCase("True")) {
                        Utils.ping(mContext, getResources().getString(R.string.changPassword));
                        Utils.setPref(mContext, "Password", passWordStr);
                        changeDialog.dismiss();
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

    private Map<String, String> getChangePasswordDetail() {

        Map<String, String> map = new HashMap<>();
        map.put("EmailAddress", Utils.getPref(mContext, "RegisterEmail"));
        map.put("Password", passWordStr);
        return map;
    }

    public void menuDialog() {
        menuDialog = new Dialog(mContext);//, R.style.Theme_Dialog);
        Window window = menuDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.x = 10;
        menuDialog.getWindow().getAttributes().verticalMargin = 0.07F;
        wlp.gravity = Gravity.TOP | Gravity.RIGHT;
        window.setAttributes(wlp);

        menuDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        menuDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        menuDialog.setCanceledOnTouchOutside(true);
//        menuDialog.setContentView(menuBinding.getRoot());
        menuDialog.setContentView(R.layout.layout_menu);
        btnHome = (Button) menuDialog.findViewById(R.id.btnHome);
        btnMyReport = (Button) menuDialog.findViewById(R.id.btnMyReport);
        btnMySession = (Button) menuDialog.findViewById(R.id.btnMySession);
        btnChangePassword = (Button) menuDialog.findViewById(R.id.btnChangePassword);
//        btnaddChild = (Button) menuDialog.findViewById(R.id.btnaddChild);
        btnLogout = (Button) menuDialog.findViewById(R.id.btnLogout);
        btnmyfamily = (Button) menuDialog.findViewById(R.id.btnmyfamily);
        btnMyenroll = (Button) menuDialog.findViewById(R.id.btnMyenroll);
        btnMyprofile = (Button) menuDialog.findViewById(R.id.btnMyprofile);

        userNameTxt = (TextView) menuDialog.findViewById(R.id.user_name_txt);
        userNameTxt.setText(Utils.getPref(mContext, "RegisterUserName"));
        if (getIntent().getStringExtra("myprofile") != null) {
            btnMyprofile.setVisibility(View.GONE);
        }

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, SearchByUser.class);
                startActivity(i);
            }
        });
        btnMyprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imyaccount = new Intent(mContext, AddStudentScreen.class);
                imyaccount.putExtra("wheretocometype", "session");
                imyaccount.putExtra("myprofile", "true");
                imyaccount.putExtra("type", "myprofile");
                startActivity(imyaccount);
                menuDialog.dismiss();
            }
        });
        btnMyenroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent isession = new Intent(mContext, MySession.class);
                isession.putExtra("wheretocometype", "session");
                startActivity(isession);
                menuDialog.dismiss();

            }
        });
        btnMySession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, UpcomingActivity.class);
                intent.putExtra("wheretocometype", "session");
                startActivity(intent);
                menuDialog.dismiss();
            }
        });
        btnMyReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imyaccount = new Intent(mContext, MyAccountActivity.class);
                imyaccount.putExtra("wheretocometype", "session");
                startActivity(imyaccount);
                menuDialog.dismiss();
            }
        });
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuDialog.dismiss();
                changePasswordDialog();
            }
        });
        btnmyfamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, FamilyListActivity.class);
                intent.putExtra("froncontanct", "true");
                intent.putExtra("wheretocometype", "session");
                intent.putExtra("familyNameStr", Utils.getPref(mContext, "RegisterUserName"));
                intent.putExtra("familyID", Utils.getPref(mContext, "coachTypeID"));
                startActivity(intent);
                menuDialog.dismiss();
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuDialog.dismiss();
                new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AlertDialogTheme))
                        .setCancelable(false)
                        //  .setTitle("Logout")
                        // .setIcon(mContext.getResources().getDrawable(R.drawable.safari))
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Utils.setPref(mContext, "coachID", "");
                                Utils.setPref(mContext, "coachTypeID", "");
                                Utils.setPref(mContext, "RegisterUserName", "");
                                Utils.setPref(mContext, "RegisterEmail", "");
                                Utils.setPref(mContext, "LoginType", "");
                                Utils.setPref(mContext, "Password", "");
                                Utils.setPref(mContext, "FamilyID", "");
                                Utils.setPref(mContext, "location", "");
                                Utils.setPref(mContext, "sessionName", "");
                                Intent intentLogin = new Intent(mContext, SearchByUser.class);
                                intentLogin.putExtra("frontLogin", "beforeLogin");
                                startActivity(intentLogin);
                                finish();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing

                            }
                        })
                        // .setIcon(R.drawable.safari)
                        .show();
            }
        });
        menuDialog.show();
    }
}
