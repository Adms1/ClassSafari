package com.adms.searchclasses.Activites;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.adms.searchclasses.AppConstant.ApiHandler;
import com.adms.searchclasses.AppConstant.Utils;
import com.adms.searchclasses.Fragment.AddSessionFragment;
import com.adms.searchclasses.Fragment.BankDetailFragment;
import com.adms.searchclasses.Fragment.PaymentReportFragment;
import com.adms.searchclasses.Fragment.SessionFragment;
import com.adms.searchclasses.Fragment.TeacherProfileFragment;
import com.adms.searchclasses.Model.TeacherInfo.TeacherInfoModel;
import com.adms.searchclasses.R;

import java.util.HashMap;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class DashBoardActivity extends AppCompatActivity {
    private static final String TAG_Session = "Session";
    private static final String TAG_Add_Session = "Add Session";
    private static final String TAG_Bank_Detail = "Bank Detail";
    private static final String TAG_My_Profile = "My Profile";
    private static final String TAG_Payment_Report = "Payment Report";
    private static final String TAG_Logout = "Logout";
    private static final String TAG_CHANGE_PASSWORD = "Change Password";
    private static final String TAG_Student_Attendance = "Student Attendance";
    public static String CURRENT_TAG = TAG_Session;
    public static int navItemIndex = 0;
    public static String position;
    Context mContex;
    //Use for dialog
    Dialog changeDialog;
    EditText edtnewpassword, edtconfirmpassword, edtcurrentpassword;
    Button changepwd_btn, cancel_btn;
    String EmailIdStr, passWordStr, confirmpassWordStr, currentpasswordStr, whereTocomestr;
    ImageView session_cal;
    //    public static String CURRENT_TAG = TAG_Calendar;
//    public static String CURRENT_TAG = TAG_Student_Attendance;
    Fragment fragment = null;
    int myid;
    boolean first_time_trans = true;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite, title_txt;
    private Toolbar toolbar;
    private String[] activityTitles;
    private int drawerLayoutGravity = Gravity.LEFT;
    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        position = getIntent().getStringExtra("position");
        if (getIntent().getStringExtra("position") != null) {
            navItemIndex = Integer.parseInt(position);
        } else {
            navItemIndex = 0;
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        session_cal = (ImageView) findViewById(R.id.session_cal);
//        setSupportActionBar(toolbar);
        mHandler = new Handler();
        mContex = DashBoardActivity.this;
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        title_txt = (TextView) findViewById(R.id.title_txt);

        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.teacher_name_txt);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website_txt);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.profile_image);
        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);
        // toolbar.setNavigationIcon(R.drawable.menubar);
        if (getIntent().getStringExtra("position") != null) {
            navItemIndex = Integer.parseInt(position);
            if (getIntent().getStringExtra("position").equalsIgnoreCase("0")) {
//                getSupportFragmentManager().beginTransaction().replace(R.id.frame, new SessionFragment()).addToBackStack(null).commit();
//                getSupportActionBar().setTitle(activityTitles[navItemIndex]);
                displayView(navItemIndex);
            } else {
                displayView(navItemIndex);
//                getSupportFragmentManager().beginTransaction().replace(R.id.frame, new AddSessionFragment()).addToBackStack(null).commit();
//                getSupportActionBar().setTitle(activityTitles[navItemIndex]);
            }
        } else {
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, new SessionFragment()).addToBackStack(null).commit();
                title_txt.setText(activityTitles[navItemIndex]);//getSupportActionBar().setTitle(
            } else {
                title_txt.setText(activityTitles[navItemIndex]);//getSupportActionBar().setTitle(
            }
        }
        whereTocomestr = getIntent().getStringExtra("frontLogin");
        session_cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new SessionFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();


//        navigationView.setNavigationItemSelectedListener(this);
    }

    private void loadNavHeader() {
        // name, website
        txtName.setText(Utils.getPref(mContex, "RegisterUserName"));
        txtWebsite.setText(Utils.getPref(mContex, "RegisterEmail"));
        imgProfile.setImageResource(R.drawable.person_placeholder);

    }

    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();
        if (navItemIndex == 5 || navItemIndex == 6) {

        } else {
            // set toolbar title
            setToolbarTitle();
        }
        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
//        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
//            drawer.closeDrawers();
//
//            return;
//        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
//        Runnable mPendingRunnable = new Runnable() {
//            @Override
//            public void run() {
//                // update the main content by replacing fragments
//                Fragment fragment = getHomeFragment();
//                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
//                        android.R.anim.fade_out);
//                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
//                fragmentTransaction.addToBackStack(SessionFragment.class.getName());
//                fragmentTransaction.commit();
//            }
//        };
//
//        // If mPendingRunnable is not null, then add to the message queue
//        if (mPendingRunnable != null) {
//            mHandler.post(mPendingRunnable);
//        }
//Closing drawer on item click

        displayView(navItemIndex);
        drawer.closeDrawers();
//        toggleDrawerState();
        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private void setToolbarTitle() {
        //getSupportActionBar().setTitle(activityTitles[navItemIndex]);
        title_txt.setText(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    public void displayView(int position) {
        switch (position) {
            case 0:
                fragment = new SessionFragment();
                myid = fragment.getId();
                break;
            case 1:
                fragment = new AddSessionFragment();
                Bundle args = new Bundle();
                args.putString("flag", "Add");
                fragment.setArguments(args);
                myid = fragment.getId();
                break;
            case 2:
                fragment = new BankDetailFragment();
                myid = fragment.getId();
                break;
            case 3:
                fragment = new TeacherProfileFragment();
                myid = fragment.getId();
                break;
            case 4:
                fragment = new PaymentReportFragment();
                myid = fragment.getId();
                break;
            case 5:
                changePasswordDialog();
                break;
            case 6:
                new AlertDialog.Builder(new ContextThemeWrapper(mContex, R.style.AlertDialogTheme))
                        .setCancelable(false)
//                        .setTitle("Logout")
//                        .setIcon(mContex.getResources().getDrawable(R.drawable.safari))
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Utils.setPref(mContex, "coachID", "");
                                Utils.setPref(mContex, "SessionID", "");
                                Utils.setPref(mContex, "FamilyID", "");
                                Utils.setPref(mContex, "sessionDetailID", "");
                                Utils.setPref(mContex, "RegisterUserName", "");
                                Utils.setPref(mContex, "RegisterEmail", "");
                                Utils.setPref(mContex, "ClassName", "");
                                Intent intentLogin = new Intent(DashBoardActivity.this, SearchByUser.class);
                                intentLogin.putExtra("frontLogin", whereTocomestr);
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
                break;
        }
        if (fragment != null) {

            FragmentManager fragmentManager = getSupportFragmentManager();

            if (fragment instanceof SessionFragment) {
                if (first_time_trans) {
                    first_time_trans = false;
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(0, 0)
                            .replace(R.id.frame, fragment).commit();

                } else {
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(0, 0)
                            .replace(R.id.frame, fragment).commit();
                }
            } else {
                fragmentManager.beginTransaction()
                        .setCustomAnimations(0, 0)
                        .replace(R.id.frame, fragment).commit();
            }
        } else {
            // error in creating fragment
            Log.e("Dashboard", "Error in creating fragment");
        }
    }


    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                loadNavHeader();
                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.session:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_Session;
                        break;
                    case R.id.add_session:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_Add_Session;
                        break;
                    case R.id.add_bank_detail:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_Bank_Detail;
                        break;
                    case R.id.teacher_profile:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_My_Profile;
                        break;
                    case R.id.payment_report:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_Payment_Report;
                        break;
                    case R.id.change_password:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_CHANGE_PASSWORD;
                        break;
                    case R.id.logout:
                        navItemIndex = 6;
                        CURRENT_TAG = TAG_Logout;
                        break;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();
                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
                // toolbar.setNavigationIcon(R.drawable.menubar);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
                //  toolbar.setNavigationIcon(R.drawable.white_arrow);
                loadNavHeader();
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_Session;
                loadHomeFragment();
                return;
            } else {

//                loadHomeFragment();
                Utils.ping(mContex, "Press again to exist");
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);//***Change Here***
                startActivity(intent);
                finish();
                System.exit(0);
//                finish();
//                System.exit(0);
//                    moveTaskToBack(true);
            }
        }
        super.onBackPressed();


    }

    public void setActionBar(int session, String flag) {
        if (session == 1 && flag.equalsIgnoreCase("edit")) {
            title_txt.setText("EDIT CLASS");
            session_cal.setVisibility(View.VISIBLE);
        } else if (session == 1 && flag.equalsIgnoreCase("add")) {
            title_txt.setText("ADD CLASS");
            session_cal.setVisibility(View.VISIBLE);
        } else if (session == 1 && flag.equalsIgnoreCase("view")) {
            title_txt.setText("VIEW CLASS");
            session_cal.setVisibility(View.VISIBLE);
        } else if (session == 10 && flag.equalsIgnoreCase("false")) {
            title_txt.setText("ADD FAMILY");
            session_cal.setVisibility(View.GONE);
        } else if (session == 11 && flag.equalsIgnoreCase("false")) {
            title_txt.setText("ADD CONTACT");
            session_cal.setVisibility(View.GONE);
        } else if (session == 12 && flag.equalsIgnoreCase("false")) {
            title_txt.setText("PAYMENT");
            session_cal.setVisibility(View.VISIBLE);
        } else if (session == 13 && flag.equalsIgnoreCase("false")) {
            title_txt.setText("FAMILY LIST");
            session_cal.setVisibility(View.VISIBLE);
        } else if (session == 14 && flag.equalsIgnoreCase("false")) {
            title_txt.setText("ENROLLMENT FAILURE");//getSupportActionBar().setTitle(
            session_cal.setVisibility(View.GONE);
            // toolbar.setNavigationIcon(null);
        } else if (session == 14 && flag.equalsIgnoreCase("true")) {
            title_txt.setText("ENROLLMENT SUCCESSFUL");
            session_cal.setVisibility(View.GONE);
            toolbar.setNavigationIcon(null);
        } else if (session == 5 && flag.equalsIgnoreCase("true")) {
            title_txt.setText("STUDENT ATTENDANCE");
            session_cal.setVisibility(View.VISIBLE);
        } else if (session == 2 && flag.equalsIgnoreCase("true")) {
            title_txt.setText("BANK DETAILS");
            session_cal.setVisibility(View.VISIBLE);
        } else if (session == 3 && flag.equalsIgnoreCase("true")) {
            title_txt.setText("MY PROFILE");
            session_cal.setVisibility(View.VISIBLE);

        }
//        else if (session == 15 && flag.equalsIgnoreCase("false")) {
//            getSupportActionBar().setTitle("Payment Report");
//        }
        else {
            if (session == 0) {
                // toolbar.setNavigationIcon(R.drawable.menubar);
                session_cal.setVisibility(View.GONE);
            }
//            getSupportActionBar().setTitle(activityTitles[session]);
            title_txt.setText(activityTitles[session]);
        }
    }

    public void changePasswordDialog() {
        changeDialog = new Dialog(mContex, R.style.Theme_Dialog);
        Window window = changeDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        changeDialog.getWindow().getAttributes().verticalMargin = 0.0f;
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);

        //changeDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);
        changeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        changeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        changeDialog.setCancelable(false);
        changeDialog.setContentView(R.layout.change_password_dialog);

        cancel_btn = (Button) changeDialog.findViewById(R.id.cancel_btn);
        changepwd_btn = (Button) changeDialog.findViewById(R.id.changepwd_btn);
        edtconfirmpassword = (EditText) changeDialog.findViewById(R.id.edtconfirmpassword);
        edtnewpassword = (EditText) changeDialog.findViewById(R.id.edtnewpassword);
        edtcurrentpassword = (EditText) changeDialog.findViewById(R.id.edtcurrentpassword);

        changepwd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentpasswordStr = edtcurrentpassword.getText().toString();
                confirmpassWordStr = edtconfirmpassword.getText().toString();
                passWordStr = edtnewpassword.getText().toString();
                if (currentpasswordStr.equalsIgnoreCase(Utils.getPref(mContex, "Password"))) {
                    if (!passWordStr.equalsIgnoreCase("") && passWordStr.length() >= 4 && passWordStr.length() <= 8) {
                        if (passWordStr.equalsIgnoreCase(confirmpassWordStr)) {
                            callChangePasswordApi();
                        } else {
                            edtconfirmpassword.setError("Confirm password does not match");
                        }
                    } else {
//                    Utils.ping(mContex, "Confirm Password does not match.");
                        edtnewpassword.setError("Password must be 4-8 characters");
                        edtnewpassword.setText("");
                        edtnewpassword.setText("");
                    }
                } else {
                    edtcurrentpassword.setError("Password does not match to current password");
                }


            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDialog.dismiss();
            }
        });

        changeDialog.show();

    }

    //USe for Change Password
    public void callChangePasswordApi() {
        if (Utils.isNetworkConnected(mContex)) {

            Utils.showDialog(mContex);
            ApiHandler.getApiService().get_Change_Password(getChangePasswordDetail(), new retrofit.Callback<TeacherInfoModel>() {
                @Override
                public void success(TeacherInfoModel forgotInfoModel, Response response) {
                    Utils.dismissDialog();
                    if (forgotInfoModel == null) {
                        Utils.ping(mContex, getString(R.string.something_wrong));
                        return;
                    }
                    if (forgotInfoModel.getSuccess() == null) {
                        Utils.ping(mContex, getString(R.string.something_wrong));
                        return;
                    }
                    if (forgotInfoModel.getSuccess().equalsIgnoreCase("false")) {
                        Utils.ping(mContex, "Please enter valid password");
                        return;
                    }
                    if (forgotInfoModel.getSuccess().equalsIgnoreCase("True")) {
                        Utils.ping(mContex, getResources().getString(R.string.changPassword));
                        Utils.setPref(mContex, "Password", passWordStr);
                        changeDialog.dismiss();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Utils.dismissDialog();
                    error.printStackTrace();
                    error.getMessage();
                    Utils.ping(mContex, getString(R.string.something_wrong));
                }
            });
        } else {
            Utils.ping(mContex, getString(R.string.internet_connection_error));
        }
    }

    private Map<String, String> getChangePasswordDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("EmailAddress", Utils.getPref(mContex, "RegisterEmail"));
        map.put("Password", passWordStr);
        return map;
    }
}
