package com.adms.classsafari.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
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
import com.adms.classsafari.Interface.onChlidClick;
import com.adms.classsafari.Interface.onViewClick;
import com.adms.classsafari.Model.Session.sessionDataModel;
import com.adms.classsafari.R;
import com.adms.classsafari.databinding.CardLayoutBinding;

import java.util.ArrayList;
import java.util.List;

public class UserSessionListAdapter1 extends RecyclerView.Adapter<UserSessionListAdapter1.MyViewHolder> {

    List<sessionDataModel> arrayList;

    String searchByStr, locationStr, classNameStr,
            address, boardStr, streamStr, standardStr,
            searchTypeStr, wheretoComeStr, searchfront,
            sessionType, firsttimesearch, TeacherName;
    onViewClick onViewClick;
onChlidClick onChlidClick;

    private Context mContext;
    private ArrayList<String> SessionDetail;
    private ArrayList<String> SessionBookDetail;
    private ArrayList<String> familyIdCheck;
    public UserSessionListAdapter1(Context mContext, List<sessionDataModel> arrayList, onViewClick onViewClick, onChlidClick onChlidClick) {
        this.mContext = mContext;
        this.arrayList = arrayList;
        this.onViewClick = onViewClick;
        this.onChlidClick=onChlidClick;
    }



    @Override
    public UserSessionListAdapter1.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.upcoming_class_list_item, parent, false);

        return new UserSessionListAdapter1.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final UserSessionListAdapter1.MyViewHolder holder, final int position) {

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
        final String paidStr = arrayList.get(position).getIsActive();
//        if (paidStr.equalsIgnoreCase("0")) {
//          holder.book_session_btn.setVisibility(View.VISIBLE);
//            holder.book_session_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    familyIdCheck = new ArrayList<String>();
//                    familyIdCheck.add(arrayList.get(position).getSessionID()+"|"+"2"+"|"+arrayList.get(position).getSessionName());
//                    onViewClick.getViewClick();
//                }
//            });
//        } else {
//           holder.book_session_btn.setVisibility(View.GONE);
//        }

        holder.rating_bar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                SessionDetail = new ArrayList<>();
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    SessionDetail.add(arrayList.get(position).getSessionName() + "|" + arrayList.get(position).getSessionID() + "|" + arrayList.get(position).getName());
                    onChlidClick.getChilClick();
                }
                return true;
            }
        });

        holder.view_more_session_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                familyIdCheck = new ArrayList<String>();
                familyIdCheck.add(arrayList.get(position).getSessionID()+"|"+"1"+"|"+arrayList.get(position).getSessionName());
                onViewClick.getViewClick();
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
    public ArrayList<String> getContactID() {
        return familyIdCheck;
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

