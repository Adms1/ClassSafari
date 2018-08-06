package com.adms.searchclasses.Adapter;

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

import com.adms.searchclasses.Activites.SessionName;
import com.adms.searchclasses.Interface.bookClick;
import com.adms.searchclasses.Interface.onViewClick;
import com.adms.searchclasses.Model.Session.sessionDataModel;
import com.adms.searchclasses.R;
import com.adms.searchclasses.databinding.CardLayoutBinding;

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

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

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