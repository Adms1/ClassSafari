package com.adms.classsafari.Adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.adms.classsafari.Activites.SessionName;
import com.adms.classsafari.Interface.bookClick;
import com.adms.classsafari.Interface.onViewClick;
import com.adms.classsafari.Model.Session.sessionDataModel;
import com.adms.classsafari.R;
import com.adms.classsafari.databinding.CardLayoutBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by admsandroid on 3/5/2018.
 */

public class ClassDetailAdapter extends RecyclerView.Adapter<ClassDetailAdapter.MyViewHolder> {

    List<sessionDataModel> arrayList;

    String searchByStr, locationStr, classNameStr,
            address, boardStr, streamStr, standardStr,
            searchTypeStr, wheretoComeStr, searchfront, searchPlaystudy,RegionName,
            sessionType, firsttimesearch, TeacherName;
    onViewClick onViewClick;
    bookClick bookClick;
    CardLayoutBinding cardLayoutBinding;

    private Context mContext;
    private ArrayList<String> SessionDetail;
    private ArrayList<String> SessionBookDetail;

//    public ClassDetailAdapter(Context mContext, List<sessionDataModel> arrayList, String searchByStr, String locationStr,
//                              String classNameStr, String boardStr, String streamStr, String standardStr,
//                              String searchfront, String teacherName, bookClick bookClick, onViewClick onViewClick) {
//        this.mContext = mContext;
//        this.arrayList = arrayList;
//        this.searchByStr = searchByStr;
//        this.locationStr = locationStr;
//        this.classNameStr = classNameStr;
//        this.boardStr = boardStr;
//        this.streamStr = streamStr;
//        this.standardStr = standardStr;
//        this.searchTypeStr = searchTypeStr;
//        this.wheretoComeStr = wheretoComeStr;
//        this.searchfront = searchfront;
//        this.onViewClick = onViewClick;
//        this.sessionType = sessionType;
//        this.firsttimesearch = firsttimesearch;
//        this.bookClick = bookClick;
//        this.TeacherName = TeacherName;
//    }

    public ClassDetailAdapter(Context mContext, List<sessionDataModel> arrayList, String locationStr,
                              String classNameStr, String boardStr, String streamStr,
                              String standardStr, String searchPlaystudy,
                              String searchfront, String firsttimesearch,
                              String regionName, String teacherName,
                              bookClick bookClick, onViewClick onViewClick) {

        this.mContext = mContext;
        this.arrayList = arrayList;
        this.searchByStr = searchByStr;
        this.locationStr = locationStr;
        this.classNameStr = classNameStr;
        this.boardStr = boardStr;
        this.streamStr = streamStr;
        this.standardStr = standardStr;
        this.searchPlaystudy = searchPlaystudy;
        this.searchfront = searchfront;
        this.onViewClick = onViewClick;
        this.firsttimesearch = firsttimesearch;
        this.bookClick = bookClick;
        this.TeacherName = teacherName;
        this.RegionName=regionName;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);

//        cardLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.card_layout, parent, false);
//        View itemView = cardLayoutBinding.getRoot();



        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

//        Typeface custom_font = Typeface.createFromAsset(mContext.getAssets(), "font/TitilliumWeb-Regular.ttf");
//        holder.session_name_txt.setTypeface(custom_font);
//        holder.price_txt.setTypeface(custom_font);
//        holder.start_date_txt.setTypeface(custom_font);
//        holder.end_date_txt.setTypeface(custom_font);
//        holder.mon_time_txt.setTypeface(custom_font);
//        holder.tues_time_txt.setTypeface(custom_font);
//        holder.wed_time_txt.setTypeface(custom_font);
//        holder.thur_time_txt.setTypeface(custom_font);
//        holder.fri_time_txt.setTypeface(custom_font);
//        holder.sat_time_txt.setTypeface(custom_font);
//        holder.sun_time_txt.setTypeface(custom_font);
//        holder.location_txt.setTypeface(custom_font);
////        holder.duration_txt.setTypeface(custom_font);
//        holder.tutor_name_txt.setTypeface(custom_font);
//        holder.mon_hours_txt.setTypeface(custom_font);
//        holder.tues_hours_txt.setTypeface(custom_font);
//        holder.wed_hours_txt.setTypeface(custom_font);
//        holder.thur_hours_txt.setTypeface(custom_font);
//        holder.fri_hours_txt.setTypeface(custom_font);
//        holder.sat_hours_txt.setTypeface(custom_font);
//        holder.sun_hours_txt.setTypeface(custom_font);
//        holder.rating_user_txt.setTypeface(custom_font);
//        holder.view_more_session_btn.setTypeface(custom_font);
//        holder.monday_btn.setTypeface(custom_font);
//        holder.tuesday_btn.setTypeface(custom_font);
//        holder.wednesday_btn.setTypeface(custom_font);
//        holder.thursday_btn.setTypeface(custom_font);
//        holder.friday_btn.setTypeface(custom_font);
//        holder.saturday_btn.setTypeface(custom_font);
//        holder.sunday_btn.setTypeface(custom_font);
//        holder.book_session_btn.setTypeface(custom_font);

        if (arrayList.get(position).getTotalRatingUser().equalsIgnoreCase("0")) {
            holder.rating_user_txt.setVisibility(View.GONE);
        } else {
            holder.rating_user_txt.setVisibility(View.VISIBLE);
            holder.rating_user_txt.setText("(" + arrayList.get(position).getTotalRatingUser() + ")");
        }
        holder.rating_bar.setRating(Float.parseFloat(arrayList.get(position).getRating()));
        address = arrayList.get(position).getAddressLine1() +
                "," + arrayList.get(position).getRegionName() +
                "," + arrayList.get(position).getAddressCity() +
                "," + arrayList.get(position).getAddressState() +
                "-" + arrayList.get(position).getAddressZipCode();
        holder.session_name_txt.setText(arrayList.get(position).getSessionName());
        if (arrayList.get(position).getSessionAmount().equalsIgnoreCase("0.00")) {
            holder.price_txt.setText("Free");
        } else {
            holder.price_txt.setText("₹" + arrayList.get(position).getSessionAmount());
        }
        holder.start_date_txt.setText(arrayList.get(position).getStartDate());
        holder.end_date_txt.setText(arrayList.get(position).getEndDate());
        holder.location_txt.setText(arrayList.get(position).getRegionName());
        if (!arrayList.get(position).getMondayTimeStr().equalsIgnoreCase("")) {
            holder.mon_time_txt.setEnabled(true);
            holder.monday_btn.setEnabled(true);
            holder.mon_hours_txt.setEnabled(true);
            holder.mon_time_txt.setAlpha(1);
            holder.monday_btn.setAlpha(1);
            holder.mon_hours_txt.setAlpha(1);
            holder.mon_time_txt.setText(arrayList.get(position).getMondayTimeStr());
            holder.mon_hours_txt.setText(arrayList.get(position).getMondayHoursStr());
        }
        if (!arrayList.get(position).getTuesdayTimeStr().equalsIgnoreCase("")) {
            holder.tues_time_txt.setEnabled(true);
            holder.tues_time_txt.setAlpha(1);
            holder.tues_time_txt.setText(arrayList.get(position).getTuesdayTimeStr());
            holder.tuesday_btn.setEnabled(true);
            holder.tuesday_btn.setAlpha(1);
            holder.tues_hours_txt.setEnabled(true);
            holder.tues_hours_txt.setAlpha(1);
            holder.tues_hours_txt.setText(arrayList.get(position).getTuesdayHoursStr());
        }
        if (!arrayList.get(position).getWeddayTimeStr().equalsIgnoreCase("")) {
            holder.wed_time_txt.setEnabled(true);
            holder.wed_time_txt.setAlpha(1);
            holder.wed_time_txt.setText(arrayList.get(position).getWeddayTimeStr());
            holder.wednesday_btn.setEnabled(true);
            holder.wednesday_btn.setAlpha(1);
            holder.wed_hours_txt.setEnabled(true);
            holder.wed_hours_txt.setAlpha(1);
            holder.wed_hours_txt.setText(arrayList.get(position).getWeddayHoursStr());
        }
        if (!arrayList.get(position).getThursdayTimeStr().equalsIgnoreCase("")) {
            holder.thur_time_txt.setEnabled(true);
            holder.thur_time_txt.setAlpha(1);
            holder.thur_time_txt.setText(arrayList.get(position).getThursdayTimeStr());
            holder.thursday_btn.setEnabled(true);
            holder.thursday_btn.setAlpha(1);
            holder.thur_hours_txt.setEnabled(true);
            holder.thur_hours_txt.setAlpha(1);
            holder.thur_hours_txt.setText(arrayList.get(position).getThursdayHoursStr());
        }
        if (!arrayList.get(position).getFridayTimeStr().equalsIgnoreCase("")) {
            holder.fri_time_txt.setEnabled(true);
            holder.fri_time_txt.setAlpha(1);
            holder.fri_time_txt.setText(arrayList.get(position).getFridayTimeStr());
            holder.friday_btn.setEnabled(true);
            holder.friday_btn.setAlpha(1);
            holder.fri_hours_txt.setEnabled(true);
            holder.fri_hours_txt.setAlpha(1);
            holder.fri_hours_txt.setText(arrayList.get(position).getFridayHoursStr());
        }
        if (!arrayList.get(position).getSatdayTimeStr().equalsIgnoreCase("")) {
            holder.sat_time_txt.setEnabled(true);
            holder.sat_time_txt.setAlpha(1);
            holder.sat_time_txt.setText(arrayList.get(position).getSatdayTimeStr());
            holder.saturday_btn.setEnabled(true);
            holder.saturday_btn.setAlpha(1);
            holder.sat_hours_txt.setEnabled(true);
            holder.sat_hours_txt.setAlpha(1);
            holder.sat_hours_txt.setText(arrayList.get(position).getSatdayHoursStr());
        }
        if (!arrayList.get(position).getSundayTimeStr().equalsIgnoreCase("")) {
            holder.sun_time_txt.setEnabled(true);
            holder.sun_time_txt.setAlpha(1);
            holder.sun_time_txt.setText(arrayList.get(position).getSundayTimeStr());
            holder.sunday_btn.setEnabled(true);
            holder.sunday_btn.setAlpha(1);
            holder.sun_hours_txt.setEnabled(true);
            holder.sun_hours_txt.setAlpha(1);
            holder.sun_hours_txt.setText(arrayList.get(position).getSundayHoursStr());
        }


        holder.linear_click.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    Intent inSession = new Intent(mContext, SessionName.class);
                    inSession.putExtra("sessionID", arrayList.get(position).getSessionID());
                    inSession.putExtra("SearchBy", searchByStr);
                    inSession.putExtra("city", locationStr);
                    inSession.putExtra("sessionName", "");//classNameStr
                    inSession.putExtra("board", boardStr);
                    inSession.putExtra("stream", streamStr);
                    inSession.putExtra("standard", standardStr);
//                    inSession.putExtra("lessionName", arrayList.get(position).getLessionTypeName());
                    inSession.putExtra("sessiondate", holder.start_date_txt.getText().toString() + " To " + holder.end_date_txt.getText().toString());
//                    inSession.putExtra("duration", cardLayoutBinding.durationTxt.getText().toString());
                    inSession.putExtra("gender", arrayList.get(position).getGenderID());
                    inSession.putExtra("searchType", searchTypeStr);
                    inSession.putExtra("withOR", wheretoComeStr);
                    inSession.putExtra("searchfront", searchfront);
                    inSession.putExtra("sessionType", sessionType);
                    inSession.putExtra("firsttimesearch", firsttimesearch);
                    inSession.putExtra("SearchPlaystudy",searchPlaystudy);
                    inSession.putExtra("TeacherName", TeacherName);
                    inSession.putExtra("RegionName",RegionName);
                    mContext.startActivity(inSession);
                }
                return true;
            }
        });
        holder.view_more_session_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inSession = new Intent(mContext, SessionName.class);
                inSession.putExtra("sessionID", arrayList.get(position).getSessionID());
                inSession.putExtra("SearchBy", searchByStr);
                inSession.putExtra("city", locationStr);
                inSession.putExtra("sessionName", "");//classNameStr
                inSession.putExtra("board", boardStr);
                inSession.putExtra("stream", streamStr);
                inSession.putExtra("standard", standardStr);
//                inSession.putExtra("lessionName", arrayList.get(position).getLessionTypeName());
                inSession.putExtra("sessiondate", holder.start_date_txt.getText().toString() + " To " + holder.end_date_txt.getText().toString());
//                inSession.putExtra("duration", cardLayoutBinding.durationTxt.getText().toString());
                inSession.putExtra("gender", arrayList.get(position).getGenderID());
                inSession.putExtra("searchType", searchTypeStr);
                inSession.putExtra("withOR", wheretoComeStr);
                inSession.putExtra("firsttimesearch", firsttimesearch);
                inSession.putExtra("SearchPlaystudy",searchPlaystudy);
                inSession.putExtra("TeacherName", TeacherName);
                inSession.putExtra("RegionName",RegionName);
                mContext.startActivity(inSession);
            }
        });
        holder.session_name_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inSession = new Intent(mContext, SessionName.class);
                inSession.putExtra("sessionID", arrayList.get(position).getSessionID());
                inSession.putExtra("SearchBy", searchByStr);
                inSession.putExtra("city", locationStr);
                inSession.putExtra("sessionName", "");//classNameStr
                inSession.putExtra("board", boardStr);
                inSession.putExtra("stream", streamStr);
                inSession.putExtra("standard", standardStr);
//                inSession.putExtra("lessionName", arrayList.get(position).getLessionTypeName());
                inSession.putExtra("sessiondate", holder.start_date_txt.getText().toString() + " To " + holder.end_date_txt.getText().toString());
//                inSession.putExtra("duration", cardLayoutBinding.durationTxt.getText().toString());
                inSession.putExtra("gender", arrayList.get(position).getGenderID());
                inSession.putExtra("searchType", searchTypeStr);
                inSession.putExtra("withOR", wheretoComeStr);
                inSession.putExtra("firsttimesearch", firsttimesearch);
                inSession.putExtra("SearchPlaystudy",searchPlaystudy);
                inSession.putExtra("TeacherName", TeacherName);
                inSession.putExtra("RegionName",RegionName);
                mContext.startActivity(inSession);
            }
        });
        holder.rating_bar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                SessionDetail = new ArrayList<>();
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    SessionDetail.add(arrayList.get(position).getSessionName() + "|" + arrayList.get(position).getSessionID() + "|" + arrayList.get(position).getName());
                    onViewClick.getViewClick();
                }
                return true;
            }
        });

        holder.book_session_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionBookDetail = new ArrayList<>();
                SessionBookDetail.add(arrayList.get(position).getSessionID());
                bookClick.bookClick();
            }
        });
//        String[] spiltPipes = arrayList.get(position).getSchedule().split("\\|");
//        String[] spiltComma;
//        String[] spiltDash;
//        Log.d("spilt", "" + spiltPipes.toString());
//        for (int i = 0; i < spiltPipes.length; i++) {
//            spiltComma = spiltPipes[i].split("\\,");
//            spiltDash = spiltComma[1].split("\\-");
//            calculateHours(spiltDash[0], spiltDash[1]);
//            arrayList.get(position).setDateTime(spiltDash[0]);
//            Log.d("DateTime", spiltDash[0]);
//            switch (spiltComma[0]) {
//                case "sun":
//                    cardLayoutBinding.sunTimeTxt.setEnabled(true);
//                    cardLayoutBinding.sundayBtn.setEnabled(true);
//                    cardLayoutBinding.sunTimeTxt.setAlpha(1);
//                    cardLayoutBinding.sundayBtn.setAlpha(1);
//                    cardLayoutBinding.sunTimeTxt.setText(arrayList.get(position).getDateTime());
//                    break;
//                case "mon":
//                    cardLayoutBinding.monTimeTxt.setEnabled(true);
//                    cardLayoutBinding.mondayBtn.setEnabled(true);
//                    cardLayoutBinding.monTimeTxt.setAlpha(1);
//                    cardLayoutBinding.mondayBtn.setAlpha(1);
//                    cardLayoutBinding.monTimeTxt.setText(arrayList.get(position).getDateTime());
//                    break;
//                case "tue":
//                    cardLayoutBinding.tuesTimeTxt.setEnabled(true);
//                    cardLayoutBinding.tuesdayBtn.setEnabled(true);
//                    cardLayoutBinding.tuesTimeTxt.setAlpha(1);
//                    cardLayoutBinding.tuesdayBtn.setAlpha(1);
//                    cardLayoutBinding.tuesTimeTxt.setText(arrayList.get(position).getDateTime());
//                    break;
//                case "wed":
//                    cardLayoutBinding.wedTimeTxt.setEnabled(true);
//                    cardLayoutBinding.wednesdayBtn.setEnabled(true);
//                    cardLayoutBinding.wedTimeTxt.setAlpha(1);
//                    cardLayoutBinding.wednesdayBtn.setAlpha(1);
//                    cardLayoutBinding.wedTimeTxt.setText(arrayList.get(position).getDateTime());
//                    break;
//                case "thu":
//                    cardLayoutBinding.thurTimeTxt.setEnabled(true);
//                    cardLayoutBinding.thursdayBtn.setEnabled(true);
//                    cardLayoutBinding.thurTimeTxt.setAlpha(1);
//                    cardLayoutBinding.thursdayBtn.setAlpha(1);
//                    cardLayoutBinding.thurTimeTxt.setText(arrayList.get(position).getDateTime());
//                    break;
//                case "fri":
//                    cardLayoutBinding.friTimeTxt.setEnabled(true);
//                    cardLayoutBinding.fridayBtn.setEnabled(true);
//                    cardLayoutBinding.friTimeTxt.setAlpha(1);
//                    cardLayoutBinding.fridayBtn.setAlpha(1);
//                    cardLayoutBinding.friTimeTxt.setText(arrayList.get(position).getDateTime());
//                    break;
//                case "sat":
//                    cardLayoutBinding.satTimeTxt.setEnabled(true);
//                    cardLayoutBinding.saturdayBtn.setEnabled(true);
//                    cardLayoutBinding.satTimeTxt.setAlpha(1);
//                    cardLayoutBinding.saturdayBtn.setAlpha(1);
//                    cardLayoutBinding.satTimeTxt.setText(arrayList.get(position).getDateTime());
//                    break;
//                default:
//
//            }
//        }
//        sessionDataModel.setWeekDay(arrayList.get(position).getSchedule());


        if (arrayList.get(position).getCoachTypeID().equalsIgnoreCase("1")) {
            holder.tutor_name_txt.setText(arrayList.get(position).getName());
        } else {
            holder.tutor_name_txt.setText(arrayList.get(position).getName());
        }

        holder.location_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                mContext.startActivity(mapIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public ArrayList<String> getSessionDetail() {
        return SessionDetail;
    }

    public ArrayList<String> getSessionBookDetail() {
        return SessionBookDetail;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView session_name_txt, price_txt, start_date_txt, end_date_txt,
                mon_time_txt, tues_time_txt, wed_time_txt, thur_time_txt, fri_time_txt, sat_time_txt, sun_time_txt,
                location_txt, duration_txt, tutor_name_txt, mon_hours_txt, tues_hours_txt, wed_hours_txt, thur_hours_txt,
                fri_hours_txt, sat_hours_txt, sun_hours_txt, rating_user_txt;
        public Button monday_btn, tuesday_btn, wednesday_btn, thursday_btn, friday_btn, saturday_btn, sunday_btn, book_session_btn, view_more_session_btn;
        public RatingBar rating_bar;
        public LinearLayout linear_click;

        public MyViewHolder(View view) {
            super(view);
            book_session_btn = (Button) view.findViewById(R.id.book_session_btn);
            sunday_btn = (Button) view.findViewById(R.id.sunday_btn);
            saturday_btn = (Button) view.findViewById(R.id.saturday_btn);
            friday_btn = (Button) view.findViewById(R.id.friday_btn);
            thursday_btn = (Button) view.findViewById(R.id.thursday_btn);
            wednesday_btn = (Button) view.findViewById(R.id.wednesday_btn);
            tuesday_btn = (Button) view.findViewById(R.id.tuesday_btn);
            monday_btn = (Button) view.findViewById(R.id.monday_btn);
            view_more_session_btn = (Button) view.findViewById(R.id.view_more_session_btn);


            sun_hours_txt = (TextView) view.findViewById(R.id.sun_hours_txt);
            sat_hours_txt = (TextView) view.findViewById(R.id.sat_hours_txt);
            fri_hours_txt = (TextView) view.findViewById(R.id.fri_hours_txt);
            thur_hours_txt = (TextView) view.findViewById(R.id.thur_hours_txt);
            wed_hours_txt = (TextView) view.findViewById(R.id.wed_hours_txt);
            tues_hours_txt = (TextView) view.findViewById(R.id.tues_hours_txt);
            mon_hours_txt = (TextView) view.findViewById(R.id.mon_hours_txt);

            sun_time_txt = (TextView) view.findViewById(R.id.sun_time_txt);
            sat_time_txt = (TextView) view.findViewById(R.id.sat_time_txt);
            fri_time_txt = (TextView) view.findViewById(R.id.fri_time_txt);
            thur_time_txt = (TextView) view.findViewById(R.id.thur_time_txt);
            wed_time_txt = (TextView) view.findViewById(R.id.wed_time_txt);
            tues_time_txt = (TextView) view.findViewById(R.id.tues_time_txt);
            mon_time_txt = (TextView) view.findViewById(R.id.mon_time_txt);

            price_txt = (TextView) view.findViewById(R.id.price_txt);
            end_date_txt = (TextView) view.findViewById(R.id.end_date_txt);
            start_date_txt = (TextView) view.findViewById(R.id.start_date_txt);
            location_txt = (TextView) view.findViewById(R.id.location_txt);
            tutor_name_txt = (TextView) view.findViewById(R.id.tutor_name_txt);
            rating_user_txt = (TextView) view.findViewById(R.id.rating_user_txt);
            session_name_txt = (TextView) view.findViewById(R.id.session_name_txt);

            rating_bar = (RatingBar) view.findViewById(R.id.rating_bar);

            linear_click = (LinearLayout) view.findViewById(R.id.linear_click);
        }
    }
}

//=======================================
// holder.session_name_txt.setText(arrayList.get(position).getSessionName());
//        if (arrayList.get(position).getSessionAmount().equalsIgnoreCase("0.00")) {
//            holder.price_txt.setText("Free");
//        } else {
//            holder.price_txt.setText("₹ " + arrayList.get(position).getSessionAmount());
//        }
//        holder.start_date_txt.setText(arrayList.get(position).getStartDate());
//        holder.end_date_txt.setText(arrayList.get(position).getEndDate());
//        holder.location_txt.setText(arrayList.get(position).getRegionName());
//        holder.linear_click.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
//                    Intent inSession = new Intent(mContext, SessionName.class);
//                    inSession.putExtra("sessionID", arrayList.get(position).getSessionID());
//                    inSession.putExtra("SearchBy", searchByStr);
//                    inSession.putExtra("city", locationStr);
//                    inSession.putExtra("sessionName", classNameStr);
//                    inSession.putExtra("board", boardStr);
//                    inSession.putExtra("stream", streamStr);
//                    inSession.putExtra("standard", standardStr);
//                    inSession.putExtra("lessionName", arrayList.get(position).getLessionTypeName());
//                    inSession.putExtra("sessiondate", holder.start_date_txt.getText().toString() + " To " + holder.end_date_txt.getText().toString());
//                    inSession.putExtra("duration", holder.duration_txt.getText().toString());
//                    inSession.putExtra("gender", arrayList.get(position).getGenderID());
//                    inSession.putExtra("searchType", searchTypeStr);
//                    inSession.putExtra("withOR", wheretoComeStr);
//                    inSession.putExtra("searchfront", searchfront);
//                    inSession.putExtra("sessionType", sessionType);
//                    mContext.startActivity(inSession);
//                }
//                return true;
//            }
//        });
////        holder.linear_click.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                Intent inSession = new Intent(mContext, SessionName.class);
////                inSession.putExtra("sessionID", arrayList.get(position).getSessionID());
////                inSession.putExtra("SearchBy", searchByStr);
////                inSession.putExtra("city", locationStr);
////                inSession.putExtra("sessionName", classNameStr);
////                inSession.putExtra("board", boardStr);
////                inSession.putExtra("stream", streamStr);
////                inSession.putExtra("standard", standardStr);
////                inSession.putExtra("lessionName", arrayList.get(position).getLessionTypeName());
////                inSession.putExtra("sessiondate", holder.start_date_txt.getText().toString() + " To " + holder.end_date_txt.getText().toString());
////                inSession.putExtra("duration", holder.duration_txt.getText().toString());
////                inSession.putExtra("gender", arrayList.get(position).getGenderID());
////                inSession.putExtra("searchType", searchTypeStr);
////                inSession.putExtra("withOR", wheretoComeStr);
////                mContext.startActivity(inSession);
////            }
////        });
//        holder.session_name_txt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Intent inSession = new Intent(mContext, SessionName.class);
////                inSession.putExtra("sessionID", arrayList.get(position).getSessionID());
////                inSession.putExtra("SearchBy", searchByStr);
////                inSession.putExtra("city", locationStr);
////                inSession.putExtra("sessionName", classNameStr);
////                inSession.putExtra("board", boardStr);
////                inSession.putExtra("stream", streamStr);
////                inSession.putExtra("standard", standardStr);
////                inSession.putExtra("lessionName", arrayList.get(position).getLessionTypeName());
////                inSession.putExtra("sessiondate", holder.start_date_txt.getText().toString() + " To " + holder.end_date_txt.getText().toString());
////                inSession.putExtra("duration", holder.duration_txt.getText().toString());
////                inSession.putExtra("gender", arrayList.get(position).getGenderID());
////                inSession.putExtra("searchType", searchTypeStr);
////                inSession.putExtra("withOR", wheretoComeStr);
////                mContext.startActivity(inSession);
//            }
//        });
//        holder.rating_bar.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                SessionDetail = new ArrayList<>();
//                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
//                    SessionDetail.add(arrayList.get(position).getSessionName() + "|" + arrayList.get(position).getSessionID());
//                    onViewClick.getViewClick();
//                }
//                return true;
//            }
//        });
//        String[] spiltPipes = arrayList.get(position).getSchedule().split("\\|");
//        String[] spiltComma;
//        String[] spiltDash;
//        Log.d("spilt", "" + spiltPipes.toString());
//        for (int i = 0; i < spiltPipes.length; i++) {
//            spiltComma = spiltPipes[i].split("\\,");
//            spiltDash = spiltComma[1].split("\\-");
//            calculateHours(spiltDash[0], spiltDash[1]);
//            arrayList.get(position).setDateTime(spiltDash[0]);
//            Log.d("DateTime", spiltDash[0]);
//            switch (spiltComma[0]) {
//                case "sun":
//                    holder.sun_time_txt.setEnabled(true);
//                    holder.sunday_btn.setEnabled(true);
//                    holder.sun_time_txt.setAlpha(1);
//                    holder.sunday_btn.setAlpha(1);
//                    holder.sun_time_txt.setText(arrayList.get(position).getDateTime());
//                    break;
//                case "mon":
//                    holder.mon_time_txt.setEnabled(true);
//                    holder.monday_btn.setEnabled(true);
//                    holder.mon_time_txt.setAlpha(1);
//                    holder.monday_btn.setAlpha(1);
//                    holder.mon_time_txt.setText(arrayList.get(position).getDateTime());
//                    break;
//                case "tue":
//                    holder.tues_time_txt.setEnabled(true);
//                    holder.tuesday_btn.setEnabled(true);
//                    holder.tues_time_txt.setAlpha(1);
//                    holder.tuesday_btn.setAlpha(1);
//                    holder.tues_time_txt.setText(arrayList.get(position).getDateTime());
//                    break;
//                case "wed":
//                    holder.wed_time_txt.setEnabled(true);
//                    holder.wednesday_btn.setEnabled(true);
//                    holder.wed_time_txt.setAlpha(1);
//                    holder.wednesday_btn.setAlpha(1);
//                    holder.wed_time_txt.setText(arrayList.get(position).getDateTime());
//                    break;
//                case "thu":
//                    holder.thur_time_txt.setEnabled(true);
//                    holder.thursday_btn.setEnabled(true);
//                    holder.thur_time_txt.setAlpha(1);
//                    holder.thursday_btn.setAlpha(1);
//                    holder.thur_time_txt.setText(arrayList.get(position).getDateTime());
//                    break;
//                case "fri":
//                    holder.fri_time_txt.setEnabled(true);
//                    holder.friday_btn.setEnabled(true);
//                    holder.fri_time_txt.setAlpha(1);
//                    holder.friday_btn.setAlpha(1);
//                    holder.fri_time_txt.setText(arrayList.get(position).getDateTime());
//                    break;
//                case "sat":
//                    holder.sat_time_txt.setEnabled(true);
//                    holder.saturday_btn.setEnabled(true);
//                    holder.sat_time_txt.setAlpha(1);
//                    holder.saturday_btn.setAlpha(1);
//                    holder.sat_time_txt.setText(arrayList.get(position).getDateTime());
//                    break;
//                default:
//
//            }
//        }
//        if (arrayList.get(position).getCoachTypeID().equalsIgnoreCase("1")) {
//            holder.tutor_name_txt.setText("Academic " + "/" + arrayList.get(position).getLessionTypeName());
//        } else {
//            holder.tutor_name_txt.setText("Sport " + "/" + arrayList.get(position).getLessionTypeName());
//        }
//
//        arrayList.get(position).setDuration(SessionHour + " hr " + SessionMinit + " min");
//        holder.duration_txt.setText(arrayList.get(position).getDuration());
//        holder.location_txt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
//                Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
//                mapIntent.setPackage("com.google.android.apps.maps");
//                mContext.startActivity(mapIntent);
//            }
//        });

// extends RecyclerView.Adapter<ClassDetailAdapter.MyViewHolder> {
//
//    List<sessionDataModel> arrayList;
//    int SessionHour = 0;
//    Integer SessionMinit = 0;
//    String locationStr, classNameStr,
//            address, boardStr, streamStr, standardStr,
//            searchfront,
//            firsttimesearch, SearchPlaystudy, RegionName;//searchTypeStr, wheretoComeStr,
//    onViewClick onViewClick;
//    bookClick bookClick;
//    CardLayoutBinding cardLayoutBinding;
//    String hours, minit;
//    private Context mContext;
//    private ArrayList<String> SessionDetail;
//    private ArrayList<String> SessionBookDetail;
//
//    public ClassDetailAdapter(Context mContext, List<sessionDataModel> arrayList, String locationStr,
//                              String classNameStr, String boardStr, String streamStr, String standardStr,
//                              String SearchPlaystudy, String searchfront, String firsttimesearch, String regionName,
//                              bookClick bookClick, onViewClick onViewClick) {// searchByStr,   String wheretoComeStr, String searchTypeStr,
//        this.mContext = mContext;
//        this.arrayList = arrayList;
//
//        this.locationStr = locationStr;
//        this.classNameStr = classNameStr;
//        this.boardStr = boardStr;
//        this.streamStr = streamStr;
//        this.standardStr = standardStr;
//
//        this.searchfront = searchfront;
//        this.onViewClick = onViewClick;
//        this.SearchPlaystudy = SearchPlaystudy;
//        this.firsttimesearch = firsttimesearch;
//        this.RegionName = regionName;
//        this.bookClick = bookClick;
//    }
//
//    @Override
//    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//
//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.card_layout, parent, false);
//
////        cardLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.card_layout, parent, false);
////
////        View itemView = cardLayoutBinding.getRoot();
//
//        return new MyViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(MyViewHolder holder, final int position) {
//        if (arrayList.get(position).getTotalRatingUser().equalsIgnoreCase("0")) {
//            holder.rating_user_txt.setVisibility(View.GONE);
//        } else {
//            holder.rating_user_txt.setVisibility(View.VISIBLE);
//            holder.rating_user_txt.setText("(" + arrayList.get(position).getTotalRatingUser() + ")");
//        }
//
//        holder.rating_bar.setRating(Float.parseFloat(arrayList.get(position).getRating()));
//        address = arrayList.get(position).getAddressLine1() +
//                "," + arrayList.get(position).getRegionName() +
//                "," + arrayList.get(position).getAddressCity() +
//                "," + arrayList.get(position).getAddressState() +
//                "-" + arrayList.get(position).getAddressZipCode();
//        holder.session_name_txt.setText(arrayList.get(position).getSessionName());
//        if (arrayList.get(position).getSessionAmount().equalsIgnoreCase("0.00")) {
//            holder.price_txt.setText("Free");
//        } else {
//            holder.price_txt.setText("₹" + arrayList.get(position).getSessionAmount());
//        }
//        holder.start_date_txt.setText(arrayList.get(position).getStartDate());
//        holder.end_date_txt.setText(arrayList.get(position).getEndDate());
//        holder.location_txt.setText(arrayList.get(position).getRegionName());
//        holder.linear_click.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
//                    Intent inSession = new Intent(mContext, SessionName.class);
//                    inSession.putExtra("sessionID", arrayList.get(position).getSessionID());
//                    inSession.putExtra("city", locationStr);
//                    inSession.putExtra("sessionName", classNameStr);
//                    inSession.putExtra("board", boardStr);
//                    inSession.putExtra("stream", streamStr);
//                    inSession.putExtra("standard", standardStr);
//                    inSession.putExtra("lessionName", "");//arrayList.get(position).getLessionTypeName()
//                    inSession.putExtra("sessiondate", cardLayoutBinding.startDateTxt.getText().toString() + " To " + cardLayoutBinding.endDateTxt.getText().toString());
////                    inSession.putExtra("duration", cardLayoutBinding.durationTxt.getText().toString());
//                    inSession.putExtra("gender", arrayList.get(position).getGenderID());
////                    inSession.putExtra("searchType", searchTypeStr);
////                    inSession.putExtra("withOR", wheretoComeStr);
//                    inSession.putExtra("SearchPlaystudy", SearchPlaystudy);
//                    inSession.putExtra("searchfront", searchfront);
//                    inSession.putExtra("firsttimesearch", firsttimesearch);
//                    inSession.putExtra("RegionName", RegionName);
//                    mContext.startActivity(inSession);
//                }
//                return true;
//            }
//        });
//        holder.session_name_txt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent inSession = new Intent(mContext, SessionName.class);
//                inSession.putExtra("sessionID", arrayList.get(position).getSessionID());
//                inSession.putExtra("city", locationStr);
//                inSession.putExtra("sessionName", classNameStr);
//                inSession.putExtra("board", boardStr);
//                inSession.putExtra("stream", streamStr);
//                inSession.putExtra("standard", standardStr);
//                inSession.putExtra("lessionName", "");//arrayList.get(position).getLessionTypeName()
//                inSession.putExtra("sessiondate", cardLayoutBinding.startDateTxt.getText().toString() + " To " + cardLayoutBinding.endDateTxt.getText().toString());
////                inSession.putExtra("duration", cardLayoutBinding.durationTxt.getText().toString());
//                inSession.putExtra("gender", arrayList.get(position).getGenderID());
////                inSession.putExtra("searchType", searchTypeStr);
////                inSession.putExtra("withOR", wheretoComeStr);
//                inSession.putExtra("SearchPlaystudy", SearchPlaystudy);
//                inSession.putExtra("firsttimesearch", firsttimesearch);
//                inSession.putExtra("RegionName", RegionName);
//                mContext.startActivity(inSession);
//            }
//        });
//        holder.book_session_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SessionBookDetail = new ArrayList<>();
//                SessionBookDetail.add(arrayList.get(position).getSessionID());
//                bookClick.bookClick();
//            }
//        });
//
//
//        holder.rating_bar.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                SessionDetail = new ArrayList<>();
//                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
//                    SessionDetail.add(arrayList.get(position).getSessionName() + "|" + arrayList.get(position).getSessionID());
//                    onViewClick.getViewClick();
//                }
//                return true;
//            }
//        });
//        String[] spiltPipes = arrayList.get(position).getSchedule().split("\\|");
//        String[] spiltComma;
//        String[] spiltDash;
//        Log.d("spilt", "" + spiltPipes.toString());
//        for (int i = 0; i < spiltPipes.length; i++) {
//            spiltComma = spiltPipes[i].split("\\,");
//            spiltDash = spiltComma[1].split("\\-");
//            calculateHours(spiltDash[0], spiltDash[1]);
//            arrayList.get(position).setDateTime(spiltDash[0]);
//            Log.d("DateTime", spiltDash[0]);
//            switch (spiltComma[0]) {
//                case "sun":
//                    holder.sun_time_txt.setEnabled(true);
//                    holder.sunday_btn.setEnabled(true);
//                    holder.sun_time_txt.setAlpha(1);
//                    holder.sunday_btn.setAlpha(1);
//                    holder.sun_time_txt.setText(arrayList.get(position).getDateTime());
//                    break;
//                case "mon":
//                    holder.mon_time_txt.setEnabled(true);
//                    holder.monday_btn.setEnabled(true);
//                    holder.mon_time_txt.setAlpha(1);
//                    holder.monday_btn.setAlpha(1);
//                    holder.mon_time_txt.setText(arrayList.get(position).getDateTime());
//                    break;
//                case "tue":
//                    holder.tues_time_txt.setEnabled(true);
//                    holder.tuesday_btn.setEnabled(true);
//                    holder.tues_time_txt.setAlpha(1);
//                    holder.tuesday_btn.setAlpha(1);
//                    holder.tues_time_txt.setText(arrayList.get(position).getDateTime());
//                    break;
//                case "wed":
//                    holder.wed_time_txt.setEnabled(true);
//                    holder.wednesday_btn.setEnabled(true);
//                    holder.wed_time_txt.setAlpha(1);
//                    holder.wednesday_btn.setAlpha(1);
//                    holder.wed_time_txt.setText(arrayList.get(position).getDateTime());
//                    break;
//                case "thu":
//                    holder.thur_time_txt.setEnabled(true);
//                    holder.thursday_btn.setEnabled(true);
//                    holder.thur_time_txt.setAlpha(1);
//                    holder.thursday_btn.setAlpha(1);
//                    holder.thur_time_txt.setText(arrayList.get(position).getDateTime());
//                    break;
//                case "fri":
//                    holder.fri_time_txt.setEnabled(true);
//                    holder.friday_btn.setEnabled(true);
//                    holder.fri_time_txt.setAlpha(1);
//                    holder.friday_btn.setAlpha(1);
//                    holder.fri_time_txt.setText(arrayList.get(position).getDateTime());
//                    break;
//                case "sat":
//                    holder.sat_time_txt.setEnabled(true);
//                    holder.saturday_btn.setEnabled(true);
//                    holder.sat_time_txt.setAlpha(1);
//                    holder.saturday_btn.setAlpha(1);
//                    holder.sat_time_txt.setText(arrayList.get(position).getDateTime());
//                    break;
//                default:
//
//            }
//        }
////        if (arrayList.get(position).getCoachTypeID().equalsIgnoreCase("1")) {
////            cardLayoutBinding.tutorNameTxt.setText("Academic " + "/" + arrayList.get(position).getName());
////        } else {
//       holder.tutor_name_txt.setText(arrayList.get(position).getName());//"Sports " + "/" +
////        }
//        //String hours,minit;
////        if (SessionHour < 10) {
////            hours = "0" + SessionHour;
////        } else {
////            hours = String.valueOf(SessionHour);
////        }
////        if (SessionMinit < 10) {
////            minit = "0" + SessionMinit;
////        } else {
////            minit = String.valueOf(SessionMinit);
////        }
////        if (minit.equalsIgnoreCase(("00"))) {
////            arrayList.get(position).setDuration(hours + " hr ");//+ " min"
////        } else {
////            arrayList.get(position).setDuration(hours + " hr " + minit + " min");//+ " min"
////        }
////        cardLayoutBinding.durationTxt.setText(arrayList.get(position).getDuration());
//       holder.location_txt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
//                Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
//                mapIntent.setPackage("com.google.android.apps.maps");
//
//                mContext.startActivity(mapIntent);
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return arrayList.size();
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return arrayList.size();
//    }
//
//    public void calculateHours(String time1, String time2) {
//        Date date1, date2;
//        int days, hours, min;
//        String hourstr, minstr;
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa", Locale.US);
//        try {
//            date1 = simpleDateFormat.parse(time1);
//            date2 = simpleDateFormat.parse(time2);
//
//            long difference = date2.getTime() - date1.getTime();
//            days = (int) (difference / (1000 * 60 * 60 * 24));
//            hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
//            min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
//            hours = (hours < 0 ? -hours : hours);
//            SessionHour = hours;
//            SessionMinit = min;
//            Log.i("======= Hours", " :: " + hours + ":" + min);
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//    public ArrayList<String> getSessionDetail() {
//        return SessionDetail;
//    }
//
//    public ArrayList<String> getSessionBookDetail() {
//        return SessionBookDetail;
//    }
//
//    public class MyViewHolder extends RecyclerView.ViewHolder {
//        Button book_session_btn, sunday_btn, saturday_btn, friday_btn, thursday_btn, wednesday_btn, tuesday_btn, monday_btn;
//        TextView sun_hours_txt, sat_hours_txt, fri_hours_txt, thur_hours_txt, wed_hours_txt, tues_hours_txt, mon_hours_txt;
//        TextView sun_time_txt, sat_time_txt, fri_time_txt, thur_time_txt, wed_time_txt, tues_time_txt, mon_time_txt,
//                price_txt, end_date_txt, start_date_txt, location_txt, tutor_name_txt, rating_user_txt, session_name_txt;
//        RatingBar rating_bar;
//        LinearLayout linear_click;
//
//        public MyViewHolder(View view) {
//            super(view);
//            book_session_btn = (Button) view.findViewById(R.id.book_session_btn);
//            sunday_btn = (Button) view.findViewById(R.id.sunday_btn);
//            saturday_btn = (Button) view.findViewById(R.id.saturday_btn);
//            friday_btn = (Button) view.findViewById(R.id.friday_btn);
//            thursday_btn = (Button) view.findViewById(R.id.thursday_btn);
//            wednesday_btn = (Button) view.findViewById(R.id.wednesday_btn);
//            tuesday_btn = (Button) view.findViewById(R.id.tuesday_btn);
//            monday_btn = (Button) view.findViewById(R.id.monday_btn);
//
//
//            sun_hours_txt = (TextView) view.findViewById(R.id.sun_hours_txt);
//            sat_hours_txt = (TextView) view.findViewById(R.id.sat_hours_txt);
//            fri_hours_txt = (TextView) view.findViewById(R.id.fri_hours_txt);
//            thur_hours_txt = (TextView) view.findViewById(R.id.thur_hours_txt);
//            wed_hours_txt = (TextView) view.findViewById(R.id.wed_hours_txt);
//            tues_hours_txt = (TextView) view.findViewById(R.id.tues_hours_txt);
//            mon_hours_txt = (TextView) view.findViewById(R.id.mon_hours_txt);
//
//            sun_time_txt = (TextView) view.findViewById(R.id.sun_time_txt);
//            sat_time_txt = (TextView) view.findViewById(R.id.sat_time_txt);
//            fri_time_txt = (TextView) view.findViewById(R.id.fri_time_txt);
//            thur_time_txt = (TextView) view.findViewById(R.id.thur_time_txt);
//            wed_time_txt = (TextView) view.findViewById(R.id.wed_time_txt);
//            tues_time_txt = (TextView) view.findViewById(R.id.tues_time_txt);
//            mon_time_txt = (TextView) view.findViewById(R.id.mon_time_txt);
//
//            price_txt = (TextView) view.findViewById(R.id.price_txt);
//            end_date_txt = (TextView) view.findViewById(R.id.end_date_txt);
//            start_date_txt = (TextView) view.findViewById(R.id.start_date_txt);
//            location_txt = (TextView) view.findViewById(R.id.location_txt);
//            tutor_name_txt = (TextView) view.findViewById(R.id.tutor_name_txt);
//            rating_user_txt = (TextView) view.findViewById(R.id.rating_user_txt);
//            session_name_txt = (TextView) view.findViewById(R.id.session_name_txt);
//
//            rating_bar = (RatingBar) view.findViewById(R.id.rating_bar);
//
//            linear_click = (LinearLayout) view.findViewById(R.id.linear_click);
//
//        }
//    }
//}
