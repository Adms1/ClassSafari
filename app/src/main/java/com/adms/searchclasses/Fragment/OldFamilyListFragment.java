package com.adms.searchclasses.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.adms.searchclasses.Activites.DashBoardActivity;
import com.adms.searchclasses.Adapter.ExpandableSelectStudentListAdapter;
import com.adms.searchclasses.AppConstant.ApiHandler;
import com.adms.searchclasses.AppConstant.AppConfiguration;
import com.adms.searchclasses.AppConstant.Utils;
import com.adms.searchclasses.Interface.DrawableClickListener;
import com.adms.searchclasses.Interface.onChlidClick;
import com.adms.searchclasses.Interface.onViewClick;
import com.adms.searchclasses.Model.Session.SessionDetailModel;
import com.adms.searchclasses.Model.TeacherInfo.ChildDetailModel;
import com.adms.searchclasses.Model.TeacherInfo.FamilyDetailModel;
import com.adms.searchclasses.Model.TeacherInfo.TeacherInfoModel;
import com.adms.searchclasses.R;
import com.adms.searchclasses.databinding.FragmentOldFamilyListBinding;
import com.adms.searchclasses.databinding.SessiondetailConfirmationDialogBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;


public class OldFamilyListFragment extends Fragment {


    List<FamilyDetailModel> finalFamilyDetail;
    List<String> listDataHeader;
    HashMap<String, List<ChildDetailModel>> listDataChild;
    ExpandableSelectStudentListAdapter expandableSelectStudentListAdapter;
    Dialog confimDialog;
    String familyIdStr = "", contatIDstr, orderIDStr, sessionIDStr, type, familyNameStr = "", familyMobileStr = "", paymentStatusstr, arraowStr, valueStr;
    ArrayList<String> selectedId;
    String froncontanctStr;
    SessiondetailConfirmationDialogBinding sessiondetailConfirmationDialogBinding;
    TeacherInfoModel familyresponse;
    SessionDetailModel dataResponse;
    int SessionHour = 0;
    Integer SessionMinit = 0;
    String SessionDuration;
    //Purchase dialog
    ArrayList<String> purchaseSessionIDArray;
    String purchaseSessionIDStr = "";
    private FragmentOldFamilyListBinding oldFamilyListBinding;
    private View rootView;
    private Context mContext;
    private int lastExpandedPosition = -1;

    public OldFamilyListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        oldFamilyListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_old_family_list, container, false);

        rootView = oldFamilyListBinding.getRoot();
//        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mContext = getActivity();
        arraowStr = "Fragment";
        ((DashBoardActivity) getActivity()).setActionBar(13, "false");
        sessionIDStr = Utils.getPref(mContext, "sessionID");
        Log.d("sessionID", sessionIDStr);
        initViews();
        setListners();
        callFamilyListApi();
        froncontanctStr = "false";
        return rootView;
    }

    //Use for initilize view
    public void initViews() {}

    //Use for Click Event
    public void setListners() {
        oldFamilyListBinding.searchEdt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (oldFamilyListBinding.searchEdt.getRight() - oldFamilyListBinding.searchEdt.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        oldFamilyListBinding.searchEdt.setText("");
                        oldFamilyListBinding.searchEdt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.search_white, 0);
                        return true;
                    }
                }
                return false;
            }
        });
        oldFamilyListBinding.addFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new AddFamilyFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle args = new Bundle();
                args.putString("session", "10");
                args.putString("type", "Family");
                args.putString("sessionID", sessionIDStr);
                fragment.setArguments(args);
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        oldFamilyListBinding.lvExpfamilylist.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    oldFamilyListBinding.lvExpfamilylist.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }

        });
        oldFamilyListBinding.searchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {
                if (charSequence.length() > 0) {
                    oldFamilyListBinding.searchEdt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.cross_11, 0);
                } else {
                    oldFamilyListBinding.searchEdt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.search_white, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    oldFamilyListBinding.lvExpfamilylist.setVisibility(View.VISIBLE);
                    List<FamilyDetailModel> filterFinalArray = new ArrayList<FamilyDetailModel>();
                    valueStr = oldFamilyListBinding.searchEdt.getText().toString();

                    for (FamilyDetailModel arrayObj : familyresponse.getData()) {
                        if (arrayObj.getContactPhoneNumber().trim().contains(valueStr.trim()) ||
                                arrayObj.getFirstName().trim().toLowerCase().contains(valueStr.trim().toLowerCase()) ||
                                arrayObj.getLastName().trim().toLowerCase().contains(valueStr.trim().toLowerCase())) {
                            filterFinalArray.add(arrayObj);
                        }
                    }
                    Log.d("FilterArray", "" + filterFinalArray.size());
                    if (filterFinalArray.size() > 0) {
                        setExpandableListView(filterFinalArray);
                    } else {
                        for (int k = 0; k < familyresponse.getData().size(); k++) {
                            for (int h = 0; h < familyresponse.getData().get(k).getFamilyContact().size(); h++) {
                                if (familyresponse.getData().get(k).getFamilyContact().get(h).getContactPhoneNumber().contains(valueStr.trim()) ||
                                        familyresponse.getData().get(k).getFamilyContact().get(h).getFirstName().trim().toLowerCase().contains(valueStr.trim().toLowerCase())
                                        || familyresponse.getData().get(k).getFamilyContact().get(h).getLastName().trim().toLowerCase().contains(valueStr.trim().toLowerCase())) {
                                    filterFinalArray.add(familyresponse.getData().get(k));
                                }
                            }
                        }
                        Log.d("FilterArray", "" + filterFinalArray.size());
                        if (filterFinalArray.size() > 0) {
                            setExpandableListView(filterFinalArray);
                        } else {
                            oldFamilyListBinding.lvExpfamilylist.setVisibility(View.GONE);
                        }
                    }
                } else {
                    if (familyresponse.getData().size() > 0) {
                        oldFamilyListBinding.lvExpfamilylist.setVisibility(View.VISIBLE);
                        List<FamilyDetailModel> filterFinalArray = new ArrayList<FamilyDetailModel>();
                        for (FamilyDetailModel arrayObj : familyresponse.getData()) {
                            filterFinalArray.add(arrayObj);
                        }
                        setExpandableListView(filterFinalArray);
                    }
                }
            }
        });

        oldFamilyListBinding.searchEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    List<FamilyDetailModel> filterFinalArray = new ArrayList<FamilyDetailModel>();
                    valueStr = oldFamilyListBinding.searchEdt.getText().toString();

                    for (FamilyDetailModel arrayObj : familyresponse.getData()) {
                        if (arrayObj.getContactPhoneNumber().trim().contains(valueStr.trim()) ||
                                arrayObj.getFirstName().trim().toLowerCase().contains(valueStr.trim().toLowerCase()) ||
                                arrayObj.getLastName().trim().toLowerCase().contains(valueStr.trim().toLowerCase())) {
                            filterFinalArray.add(arrayObj);
                        }
                    }
                    Log.d("FilterArray", "" + filterFinalArray.size());
                    if (filterFinalArray.size() > 0) {
                        setExpandableListView(filterFinalArray);
                    } else {
                        for (int k = 0; k < familyresponse.getData().size(); k++) {
                            for (int h = 0; h < familyresponse.getData().get(k).getFamilyContact().size(); h++) {
                                if (familyresponse.getData().get(k).getFamilyContact().get(h).getContactPhoneNumber().contains(valueStr.trim()) ||
                                        familyresponse.getData().get(k).getFamilyContact().get(h).getFirstName().trim().toLowerCase().contains(valueStr.trim().toLowerCase())
                                        || familyresponse.getData().get(k).getFamilyContact().get(h).getLastName().trim().toLowerCase().contains(valueStr.trim().toLowerCase())) {
                                    filterFinalArray.add(familyresponse.getData().get(k));
                                }
                            }
                        }
                        Log.d("FilterArray", "" + filterFinalArray.size());
                        if (filterFinalArray.size() > 0) {
                            setExpandableListView(filterFinalArray);
                        } else {
                            if (valueStr.matches("^[0-9]+$")) {
                                if (valueStr.length() == 10) {
                                    callFamilyRegisterStatusListApi();
                                } else {
                                    Utils.ping(mContext, "Enter proper number");
                                }
                            } else {
                                callFamilyRegisterStatusListApi();
                            }

                        }
                    }
                }
                return false;
            }
        });
    }

    //Use for Class Confirmation
    public void SessionConfirmationDialog() {
        sessiondetailConfirmationDialogBinding = DataBindingUtil.
                inflate(LayoutInflater.from(mContext), R.layout.sessiondetail_confirmation_dialog,(ViewGroup) oldFamilyListBinding.getRoot(), false);// (ViewGroup) oldFamilyListBinding.getRoot()
        confimDialog = new Dialog(mContext, R.style.Theme_Dialog);
        Window window = confimDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        confimDialog.getWindow().getAttributes().verticalMargin = 0.20f;
        wlp.gravity = Gravity.TOP;
        window.setAttributes(wlp);

        //confimDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);
        confimDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        confimDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        confimDialog.setCancelable(false);
        confimDialog.setContentView(sessiondetailConfirmationDialogBinding.getRoot());

        callEditSessionApi();
        AppConfiguration.UserName = familyNameStr;
        sessiondetailConfirmationDialogBinding.tutorNameTxt.setText(Utils.getPref(mContext, "RegisterUserName"));
        sessiondetailConfirmationDialogBinding.sessionNameTxt.setText(AppConfiguration.SessionName);
        sessiondetailConfirmationDialogBinding.locationTxt.setText(AppConfiguration.RegionName);


        if (AppConfiguration.SessionPrice.equalsIgnoreCase("0")) {
            sessiondetailConfirmationDialogBinding.priceTxt.setText("Free");
        } else {
            sessiondetailConfirmationDialogBinding.priceTxt.setText("₹" + AppConfiguration.SessionPrice);
        }
        sessiondetailConfirmationDialogBinding.ratingBar.setRating(Float.parseFloat(AppConfiguration.SessionRating));
        if (!AppConfiguration.SessionUserRating.equalsIgnoreCase("( 0 )")) {
            sessiondetailConfirmationDialogBinding.ratingUserTxt.setText(AppConfiguration.SessionUserRating);
        } else {
            sessiondetailConfirmationDialogBinding.ratingUserTxt.setText("");
        }


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
                        AppConfiguration.TeacherSessionIdStr = sessionIDStr;
                        AppConfiguration.TeacherSessionContactIdStr = contatIDstr;
                        if (!contatIDstr.equalsIgnoreCase("") && !sessionIDStr.equalsIgnoreCase("") ){//&& !AppConfiguration.SessionPrice.equalsIgnoreCase("0")) {
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

    //Use for calculate Class Time
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
//            totalHours.add(SessionHour);
//            totalMinit.add(SessionMinit);
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

    //Use for Get FamilyList
    public void callFamilyListApi() {
        if (Utils.isNetworkConnected(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_ContactEnrollmentByCoachID(getFamilyListDetail(), new retrofit.Callback<TeacherInfoModel>() {
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
                        oldFamilyListBinding.listLinear.setVisibility(View.GONE);
                        oldFamilyListBinding.noRecordTxt.setVisibility(View.VISIBLE);
                        return;
                    }
                    if (familyInfoModel.getSuccess().equalsIgnoreCase("True")) {
                        finalFamilyDetail = familyInfoModel.getData();
                        familyresponse = familyInfoModel;
                        if (familyInfoModel.getData() != null) {
                            if (familyInfoModel.getData().size() > 0) {
                                oldFamilyListBinding.text.setVisibility(View.VISIBLE);
                                oldFamilyListBinding.listLinear.setVisibility(View.VISIBLE);
                                oldFamilyListBinding.noRecordTxt.setVisibility(View.GONE);
                                //fillExpLV();

                                List<FamilyDetailModel> filterFinalArray = new ArrayList<FamilyDetailModel>();
                                for (FamilyDetailModel arrayObj : familyresponse.getData()) {
                                    filterFinalArray.add(arrayObj);
                                }
                                setExpandableListView(filterFinalArray);


                            } else {
                                oldFamilyListBinding.searchLinear.setVisibility(View.GONE);
                                oldFamilyListBinding.text.setVisibility(View.GONE);
                                oldFamilyListBinding.listLinear.setVisibility(View.GONE);
                                oldFamilyListBinding.noRecordTxt.setVisibility(View.VISIBLE);
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
        map.put("CoachID", Utils.getPref(mContext, "coachID"));
        return map;
    }

    //Use for fill Family Detail
    private void setExpandableListView(List<FamilyDetailModel> array) {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<String, List<ChildDetailModel>>();
        for (int i = 0; i < array.size(); i++) {
            listDataHeader.add(array.get(i).getFirstName() + "|"
                    + array.get(i).getLastName() + "|"
                    + array.get(i).getContactPhoneNumber() + "|"
                    + array.get(i).getFamilyID() + "|"
                    + array.get(i).getContactID());
            Log.d("header", "" + listDataHeader);
            ArrayList<ChildDetailModel> row = new ArrayList<ChildDetailModel>();
            for (int j = 0; j < array.get(i).getFamilyContact().size(); j++) {
//                if (finalFamilyDetail.get(i).getFamilyContact().get(i) != null) {
                row.add(array.get(i).getFamilyContact().get(j));
                Log.d("row", "" + row);
//                }
            }
            listDataChild.put(listDataHeader.get(i), row);
            Log.d("child", "" + listDataChild);
        }
        expandableSelectStudentListAdapter = new ExpandableSelectStudentListAdapter(getActivity(), listDataHeader, listDataChild, froncontanctStr, arraowStr, new onChlidClick() {
            @Override
            public void getChilClick() {
                getFamilyID();
                Fragment fragment = new AddFamilyFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle args = new Bundle();
                args.putString("session", "11");
                args.putString("type", "Child");
                args.putString("familyID", familyIdStr);
                args.putString("familyNameStr", familyNameStr);
                args.putString("familyMobileStr", familyMobileStr);
                fragment.setArguments(args);
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }, new onViewClick() {
            @Override
            public void getViewClick() {
//                                        ConformationDialog();
                getsessionID();
                callSessionReportApi();
                //SessionConfirmationDialog();
            }
        });
        oldFamilyListBinding.lvExpfamilylist.setAdapter(expandableSelectStudentListAdapter);
        oldFamilyListBinding.lvExpfamilylist.expandGroup(0);
    }

    //Use for get selected data
    public void getFamilyID() {
        selectedId = new ArrayList<String>();

        selectedId = expandableSelectStudentListAdapter.getFamilyID();
        Log.d("selectedId", "" + selectedId);
        for (int i = 0; i < selectedId.size(); i++) {
            String[] spiltValue = selectedId.get(i).split("\\|");
            familyIdStr = spiltValue[0];
            familyNameStr = spiltValue[1] + " " + spiltValue[2];
            familyMobileStr = spiltValue[3];
            Log.d("selectedIdStr", familyIdStr);
            Log.d("familyname", familyNameStr);
        }
    }

    //Use for get selected data
    public void getsessionID() {
        selectedId = new ArrayList<String>();

        selectedId = expandableSelectStudentListAdapter.getSessionDetail();
        Log.d("selectedId", "" + selectedId);
        for (int i = 0; i < selectedId.size(); i++) {
            String[] spilt = selectedId.get(i).split("\\|");
            contatIDstr = spilt[2];
            Utils.setPref(mContext, "FamilyID", contatIDstr);
            familyNameStr = spilt[0] + " " + spilt[1];
            type = spilt[4];

            AppConfiguration.TeacherBookTypeStr = type;
            Utils.setPref(mContext, "Type", type);
            Log.d("selectedIdStr", contatIDstr);
        }
    }

    //Use for GetSession Report
    public void callSessionReportApi() {
        if (Utils.isNetworkConnected(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_FamilySessionList_ByContactID(getSessionReportDetail(), new retrofit.Callback<SessionDetailModel>() {
                @Override
                public void success(SessionDetailModel sessionModel, Response response) {
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
                        purchaseSessionIDArray = new ArrayList<>();
                        SessionConfirmationDialog();
                        return;
                    }
                    if (sessionModel.getSuccess().equalsIgnoreCase("True")) {
                        Utils.dismissDialog();
                        purchaseSessionIDStr="";
                        if (sessionModel.getData().size() > 0) {
                            purchaseSessionIDArray = new ArrayList<>();
                            for (int i = 0; i < sessionModel.getData().size(); i++) {
                                if (sessionIDStr.equalsIgnoreCase(sessionModel.getData().get(i).getSessionID())) {
                                    purchaseSessionIDStr = sessionModel.getData().get(i).getSessionID();
                                }
                            }
                            if (!purchaseSessionIDStr.equalsIgnoreCase("")) {
                                new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AppTheme))
                                        .setMessage("You are already purchase.")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        })
                                        .show();
                            } else {
                                SessionConfirmationDialog();
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

    private Map<String, String> getSessionReportDetail() {

        Map<String, String> map = new HashMap<>();
        map.put("ContactID", contatIDstr);//contatIDstr  //Utils.getPref(mContext, "coachID")
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
                        Utils.ping(mContext, "Confirmation successfully");
                        Fragment fragment = new PaymentSucessFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        Bundle args = new Bundle();
                        args.putString("order_id", orderIDStr);
                        args.putString("sessionId", sessionIDStr);
                        args.putString("responseCode", "0");
                        args.putString("transactionId", orderIDStr);
                        args.putString("username", AppConfiguration.UserName);
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

    private Map<String, String> getSessionConfirmationdetail() {
        Map<String, String> map = new HashMap<>();
        map.put("SessionID", sessionIDStr);
        map.put("ContactID", contatIDstr);
        map.put("PaymentStatus", paymentStatusstr);
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
                        if (!AppConfiguration.SessionPrice.equalsIgnoreCase("0")) {
                            Fragment fragment = new PaymentFragment();
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            Bundle args = new Bundle();
                            args.putString("orderID", orderIDStr);
                            args.putString("amount", AppConfiguration.SessionPrice);
                            args.putString("mode", AppConfiguration.Mode);
                            args.putString("username", AppConfiguration.UserName);
                            args.putString("sessionID", sessionIDStr);
                            args.putString("contactID", contatIDstr);
                            args.putString("type", type);
                            fragment.setArguments(args);
                            fragmentTransaction.replace(R.id.frame, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
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
        map.put("Amount", AppConfiguration.SessionPrice);
        return map;
    }

    //Use for Get FamilyListRegister
    public void callFamilyRegisterStatusListApi() {
        if (Utils.isNetworkConnected(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().getFamily_RegisterStatus(getFamilyRegisterStatusDetail(), new retrofit.Callback<SessionDetailModel>() {
                @Override
                public void success(SessionDetailModel familyInfoModel, Response response) {
                    Utils.dismissDialog();
                    if (familyInfoModel == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (familyInfoModel.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (familyInfoModel.getSuccess().equalsIgnoreCase("False")) {
                        oldFamilyListBinding.noRecordTxt.setVisibility(View.VISIBLE);
                        oldFamilyListBinding.lvExpfamilylist.setVisibility(View.GONE);

                        Utils.ping(mContext, "No family found");
                        return;
                    }
                    if (familyInfoModel.getSuccess().equalsIgnoreCase("True")) {
                        new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AppTheme))
                                .setMessage("The family you are searching for is already a member of search classes. Please request them to go to the application and register for your class")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (familyresponse.getData().size() > 0) {
                                            oldFamilyListBinding.lvExpfamilylist.setVisibility(View.VISIBLE);
                                            List<FamilyDetailModel> filterFinalArray = new ArrayList<FamilyDetailModel>();
                                            for (FamilyDetailModel arrayObj : familyresponse.getData()) {
                                                filterFinalArray.add(arrayObj);
                                            }
                                            setExpandableListView(filterFinalArray);
                                        }
                                    }
                                })
                                .show();
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

    private Map<String, String> getFamilyRegisterStatusDetail() {
        Map<String, String> map = new HashMap<>();
        if (valueStr.matches("^[0-9]+$")) {
            map.put("mobileno", valueStr);
            map.put("name", "");
        } else {
            map.put("mobileno", "");
            map.put("name", valueStr);
        }

        return map;
    }
}

