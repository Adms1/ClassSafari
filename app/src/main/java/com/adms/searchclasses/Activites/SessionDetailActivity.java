package com.adms.searchclasses.Activites;

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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.adms.searchclasses.Adapter.PurchaseSessionDetailAdapter;
import com.adms.searchclasses.AppConstant.ApiHandler;
import com.adms.searchclasses.AppConstant.AppConfiguration;
import com.adms.searchclasses.AppConstant.Utils;
import com.adms.searchclasses.Interface.onChlidClick;
import com.adms.searchclasses.Interface.onViewClick;
import com.adms.searchclasses.Model.Session.SessionDetailModel;
import com.adms.searchclasses.Model.Session.sessionDataModel;
import com.adms.searchclasses.Model.TeacherInfo.TeacherInfoModel;
import com.adms.searchclasses.R;
import com.adms.searchclasses.databinding.ActivitySessionDetailBinding;
import com.adms.searchclasses.databinding.ChangePasswordDialogBinding;
import com.adms.searchclasses.databinding.DialogViewTeacherProfileBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class SessionDetailActivity extends AppCompatActivity implements View.OnClickListener {

    ActivitySessionDetailBinding sessionDetailBinding;
    ChangePasswordDialogBinding changePasswordDialogBinding;
    DialogViewTeacherProfileBinding dialogViewTeacherProfileBinding;
    TeacherInfoModel teacherInfoResponse;

    Context mContext;
    String sessionIDStr, wheretocometypeStr, commentStr, ratingValueStr, ratinguserStr,sessionName = "",coachIdStr,emailstr, phonestr;
    SessionDetailModel dataResponse, dataResponseRating;
    List<sessionDataModel> arrayList;
    List<sessionDataModel> sessionRatingList;
    ArrayList<String> reviewarray;
    ArrayList<String> descriptionarray;
    ArrayList<String> descriptionviewarray;

    PurchaseSessionDetailAdapter purchaseSessionDetailAdapter;

    //Use for Menu Dialog
    String passWordStr, confirmpassWordStr, currentpasswordStr;
    Dialog menuDialog, changeDialog,profileDialog;
    Button btnHome,btnMyReport, btnMySession, btnChangePassword, btnaddChild, btnLogout, btnmyfamily, btnMyenroll, btnMyprofile;
    TextView userNameTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_session_detail);
        mContext = this;
       // setTypeface();
        init();
        setListner();
    }

    //Use for initilize view
    public void init() {
        wheretocometypeStr = getIntent().getStringExtra("wheretocometype");
        sessionIDStr = getIntent().getStringExtra("sessionID");
        callSessionListApi();
    }

    //Use for Click Event
    public void setListner() {
        sessionDetailBinding.back.setOnClickListener(this);
        sessionDetailBinding.menu.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                Intent intent = new Intent(mContext, MySession.class);
                intent.putExtra("wheretocometype", wheretocometypeStr);
                startActivity(intent);
                break;
            case R.id.menu:
                menuDialog();
                break;
        }
    }

    //Use for Get Teacher detail
    public void callTeacherProfileApi() {
        if (Utils.isNetworkConnected(mContext)) {
//            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_TeacherContactDetail_Coach_ID(getTeacherdetail(), new retrofit.Callback<TeacherInfoModel>() {
                @Override
                public void success(final TeacherInfoModel teacherInfoModel, Response response) {
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

                        for (int i = 0; i < teacherInfoResponse.getData().size(); i++) {
                            dialogViewTeacherProfileBinding.teacherNameTxt.setText(teacherInfoModel.getData().get(i).getFirstName() + " " +
                                    teacherInfoModel.getData().get(i).getLastName());


                            if (!teacherInfoModel.getData().get(i).getExpYear().equalsIgnoreCase("0")) {
                                dialogViewTeacherProfileBinding.classNameTxt.setText(teacherInfoModel.getData().get(i).getClassName() + " ("
                                        + teacherInfoModel.getData().get(i).getExpYear() + " year" + " " + teacherInfoModel.getData().get(i).getExpMonth() + " month" + ")");
                            } else {
                                dialogViewTeacherProfileBinding.classNameTxt.setText(teacherInfoModel.getData().get(i).getClassName());
                            }
                            if (!teacherInfoModel.getData().get(i).getQualification().equalsIgnoreCase("")) {
                                dialogViewTeacherProfileBinding.qualificationTxt.setText(teacherInfoModel.getData().get(i).getQualification());
                            } else {
                                dialogViewTeacherProfileBinding.qualificationTxt.setVisibility(View.GONE);
                            }
                            if (!teacherInfoModel.getData().get(i).getAboutUs().equalsIgnoreCase("")) {
                                dialogViewTeacherProfileBinding.aboutUsTxt.setText(teacherInfoModel.getData().get(i).getAboutUs());
                            } else {
                                dialogViewTeacherProfileBinding.aboutUsTxt.setVisibility(View.GONE);
                            }
                            emailstr = teacherInfoModel.getData().get(i).getEmailID();
                            phonestr = teacherInfoModel.getData().get(i).getMobile();

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

    private Map<String, String> getTeacherdetail() {
        Map<String, String> map = new HashMap<>();
        map.put("Coach_ID", coachIdStr);
        return map;
    }

    //Use for View TeacherProfile
    public void viewTeacherProfile() {
        dialogViewTeacherProfileBinding = DataBindingUtil.
                inflate(LayoutInflater.from(mContext), R.layout.dialog_view_teacher_profile, (ViewGroup) sessionDetailBinding.getRoot(), false);

        profileDialog = new Dialog(mContext, R.style.Theme_Dialog);
        Window window = profileDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        profileDialog.getWindow().getAttributes().verticalMargin = 0.0f;
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);

        //changeDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);
        profileDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        profileDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        profileDialog.setCancelable(false);
        profileDialog.setContentView(dialogViewTeacherProfileBinding.getRoot());
        callTeacherProfileApi();
        dialogViewTeacherProfileBinding.viewTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileDialog.dismiss();
            }
        });
        dialogViewTeacherProfileBinding.emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                String[] recipients = {emailstr};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                mContext.startActivity(Intent.createChooser(intent, "Send mail"));
            }
        });
        dialogViewTeacherProfileBinding.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (Utils.checkAndRequestPermissions(mContext)) {
//                }
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.fromParts("tel", phonestr, null));
                mContext.startActivity(intent);
            }
        });

        profileDialog.show();

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
                            callSessionRatingApi();
                            Utils.dismissDialog();
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

    //Use for Display Class Detail
    public void setData() {
        arrayList = new ArrayList<sessionDataModel>();
        descriptionarray = new ArrayList<>();
        descriptionviewarray = new ArrayList<String>();


        for (int i = 0; i < dataResponse.getData().size(); i++) {
            arrayList.add(dataResponse.getData().get(i));
            AppConfiguration.classsessionPrice = dataResponse.getData().get(i).getSessionAmount();
            if (dataResponse.getData().get(i).getSessionAmount().equalsIgnoreCase("0.00")) {
                dataResponse.getData().get(i).setSessionAmount("Free");
            }
            descriptionviewarray.add(dataResponse.getData().get(i).getDescription());

        }

        if (!descriptionviewarray.get(0).equalsIgnoreCase("")) {
            descriptionarray.add("Description");
        } else {
            descriptionarray = new ArrayList<>();
            descriptionviewarray = new ArrayList<>();
        }
        //,descriptionarray
        purchaseSessionDetailAdapter = new PurchaseSessionDetailAdapter(mContext, arrayList, descriptionviewarray,
                reviewarray, ratinguserStr, sessionRatingList, new onChlidClick() {
            @Override
            public void getChilClick() {
                ArrayList<String> coachId = new ArrayList<String>();
                coachId = purchaseSessionDetailAdapter.getCoach();
                Log.d("coachId", "" + coachId);
                for (int i = 0; i < coachId.size(); i++) {
                    String[] splitvalue = coachId.get(i).split("\\|");
                    coachIdStr = splitvalue[0];
                }
                viewTeacherProfile();
            }
        }, new onViewClick() {
            @Override
            public void getViewClick() {
                ArrayList<String> selectedId = new ArrayList<String>();
                String flag = "";
                selectedId = purchaseSessionDetailAdapter.getSessionDetail();
                Log.d("selectedId", "" + selectedId);
                for (int i = 0; i < selectedId.size(); i++) {
                    String[] splitvalue = selectedId.get(i).split("\\|");
                    sessionName = splitvalue[0];
                    sessionIDStr = splitvalue[1];
//                    flag = splitvalue[3];
                }

                    if (Utils.getPref(mContext, "LoginType").equalsIgnoreCase("Family")) {
                        addRating();
                    }



            }
        });
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
        sessionDetailBinding.sessionListRecView.setLayoutManager(mLayoutManager);
        sessionDetailBinding.sessionListRecView.setItemAnimator(new DefaultItemAnimator());
        sessionDetailBinding.sessionListRecView.setAdapter(purchaseSessionDetailAdapter);


    }

    //Use for SessionRating
    public void callSessionRatingApi() {
        if (Utils.checkNetwork(mContext)) {

//            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_SessionRating_By_Session_ID(getSessionRatingDetail(), new retrofit.Callback<SessionDetailModel>() {
                @Override
                public void success(SessionDetailModel sessionRatingInfo, Response response) {
                    Utils.dismissDialog();
                    if (sessionRatingInfo == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (sessionRatingInfo.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (sessionRatingInfo.getSuccess().equalsIgnoreCase("false")) {
//                        Utils.ping(mContext, getString(R.string.false_msg));
                        dataResponseRating = sessionRatingInfo;
                        sessionRatingList = new ArrayList<sessionDataModel>();
                        if (dataResponseRating.getData().size() > 0) {
                            for (int i = 0; i < dataResponseRating.getData().size(); i++) {
                                sessionRatingList.add(dataResponseRating.getData().get(i));
                            }
                        }
                        reviewarray = new ArrayList<>();
                        ratinguserStr = String.valueOf(sessionRatingList.size());
                        setData();
                        Utils.dismissDialog();
                        return;
                    }
                    if (sessionRatingInfo.getSuccess().equalsIgnoreCase("True")) {

                        if (sessionRatingInfo.getData().size() > 0) {
                            dataResponseRating = sessionRatingInfo;
                            sessionRatingList = new ArrayList<sessionDataModel>();
                            for (int i = 0; i < dataResponseRating.getData().size(); i++) {
                                sessionRatingList.add(dataResponseRating.getData().get(i));
                            }
                            ratinguserStr = String.valueOf(sessionRatingList.size());
                            reviewarray = new ArrayList<>();
                            reviewarray.add("Reviews");
                            setData();
                        }
                        Utils.dismissDialog();
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

    private Map<String, String> getSessionRatingDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("Session_ID", sessionIDStr);
        return map;
    }

    //Use for Give Rating on class
    public void addRating() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.rating_dialog_layout, null);
        final RatingBar ratingBar = alertLayout.findViewById(R.id.rating_bar);
        final TextView sessionNametxt = alertLayout.findViewById(R.id.session_name_txt);
        final TextView session_rating_view_txt = alertLayout.findViewById(R.id.session_rating_view_txt);
        final TextView cancel_txt = alertLayout.findViewById(R.id.cancel_txt);
        final TextView confirm_txt = alertLayout.findViewById(R.id.confirm_txt);
        final EditText comment_edt = alertLayout.findViewById(R.id.comment_edt);
        sessionNametxt.setText(sessionName);

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
                        callSessionListApi();
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

    //Use for Menu
    public void menuDialog() {
        menuDialog = new Dialog(mContext);//, R.style.Theme_Dialog);
        Window window = menuDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.x = 10;
        menuDialog.getWindow().getAttributes().verticalMargin = 0.07F;
        wlp.gravity = Gravity.TOP|Gravity.RIGHT;
        window.setAttributes(wlp);

        menuDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        menuDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        menuDialog.setCanceledOnTouchOutside(true);
        menuDialog.setContentView(R.layout.layout_menu);

        btnHome=(Button)menuDialog.findViewById(R.id.btnHome);
        btnMyReport = (Button) menuDialog.findViewById(R.id.btnMyReport);
        btnMySession = (Button) menuDialog.findViewById(R.id.btnMySession);
        btnChangePassword = (Button) menuDialog.findViewById(R.id.btnChangePassword);
        btnLogout = (Button) menuDialog.findViewById(R.id.btnLogout);
        btnmyfamily = (Button) menuDialog.findViewById(R.id.btnmyfamily);
        btnMyenroll = (Button) menuDialog.findViewById(R.id.btnMyenroll);
        btnMyprofile = (Button) menuDialog.findViewById(R.id.btnMyprofile);
        userNameTxt = (TextView) menuDialog.findViewById(R.id.user_name_txt);
        userNameTxt.setText(Utils.getPref(mContext, "RegisterUserName"));

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(mContext,SearchByUser.class);
                startActivity(i);
            }
        });
        btnMyprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imyaccount = new Intent(mContext, AddStudentScreen.class);
                imyaccount.putExtra("wheretocometype", "menu");
                imyaccount.putExtra("myprofile", "true");
                imyaccount.putExtra("type", "myprofile");
                startActivity(imyaccount);
                menuDialog.dismiss();
            }
        });
        btnMyReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imyaccount = new Intent(mContext, MyAccountActivity.class);
                imyaccount.putExtra("wheretocometype", "menu");
                startActivity(imyaccount);
                menuDialog.dismiss();
            }
        });
        btnMyenroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent isession = new Intent(mContext, MySession.class);
                isession.putExtra("wheretocometype", "menu");
                startActivity(isession);
                menuDialog.dismiss();
            }
        });
        btnMySession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, UpcomingActivity.class);
                intent.putExtra("wheretocometype", "menu");
                startActivity(intent);
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
                intent.putExtra("wheretocometype", "menu");
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
//                        .setIcon(R.drawable.safari)
                        .show();
            }
        });
        menuDialog.show();
    }

    //Use for Change Password
    public void changePasswordDialog() {
        changePasswordDialogBinding = DataBindingUtil.
                inflate(LayoutInflater.from(mContext), R.layout.change_password_dialog, (ViewGroup) sessionDetailBinding.getRoot(), false);

        changeDialog = new Dialog(mContext, R.style.Theme_Dialog);
        Window window = changeDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        changeDialog.getWindow().getAttributes().verticalMargin = 0.0f;
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);

        // changeDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);
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


}
