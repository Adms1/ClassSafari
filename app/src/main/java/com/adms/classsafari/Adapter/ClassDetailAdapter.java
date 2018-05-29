package com.adms.classsafari.Adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.adms.classsafari.Activites.SessionName;
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
    int SessionHour = 0;
    Integer SessionMinit = 0;
    String searchByStr, locationStr, classNameStr,
            address, boardStr, streamStr, standardStr,
            searchTypeStr, wheretoComeStr, searchfront,
            sessionType, firsttimesearch;
    onViewClick onViewClick;
    CardLayoutBinding cardLayoutBinding;

    private Context mContext;
    private ArrayList<String> SessionDetail;

    public ClassDetailAdapter(Context mContext, List<sessionDataModel> arrayList, String searchByStr, String locationStr,
                              String classNameStr, String boardStr, String streamStr, String standardStr, String searchTypeStr,
                              String wheretoComeStr, String searchfront, String sessionType, String firsttimesearch, onViewClick onViewClick) {
        this.mContext = mContext;
        this.arrayList = arrayList;
        this.searchByStr = searchByStr;
        this.locationStr = locationStr;
        this.classNameStr = classNameStr;
        this.boardStr = boardStr;
        this.streamStr = streamStr;
        this.standardStr = standardStr;
        this.searchTypeStr = searchTypeStr;
        this.wheretoComeStr = wheretoComeStr;
        this.searchfront = searchfront;
        this.onViewClick = onViewClick;
        this.sessionType = sessionType;
        this.firsttimesearch = firsttimesearch;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.card_layout, parent, false);

        cardLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.card_layout, parent, false);

        View itemView = cardLayoutBinding.getRoot();

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (arrayList.get(position).getTotalRatingUser().equalsIgnoreCase("0")) {
            cardLayoutBinding.ratingUserTxt.setVisibility(View.GONE);
        } else {
            cardLayoutBinding.ratingUserTxt.setVisibility(View.VISIBLE);
            cardLayoutBinding.ratingUserTxt.setText("(" + arrayList.get(position).getTotalRatingUser() + ")");
        }
        cardLayoutBinding.ratingBar.setRating(Float.parseFloat(arrayList.get(position).getRating()));
        address = arrayList.get(position).getAddressLine1() +
                "," + arrayList.get(position).getRegionName() +
                "," + arrayList.get(position).getAddressCity() +
                "," + arrayList.get(position).getAddressState() +
                "-" + arrayList.get(position).getAddressZipCode();
        cardLayoutBinding.sessionNameTxt.setText(arrayList.get(position).getSessionName());
        if (arrayList.get(position).getSessionAmount().equalsIgnoreCase("0.00")) {
            cardLayoutBinding.priceTxt.setText("Free");
        } else {
            cardLayoutBinding.priceTxt.setText("₹ " + arrayList.get(position).getSessionAmount());
        }
        cardLayoutBinding.startDateTxt.setText(arrayList.get(position).getStartDate());
        cardLayoutBinding.endDateTxt.setText(arrayList.get(position).getEndDate());
        cardLayoutBinding.locationTxt.setText(arrayList.get(position).getRegionName());
        cardLayoutBinding.linearClick.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    Intent inSession = new Intent(mContext, SessionName.class);
                    inSession.putExtra("sessionID", arrayList.get(position).getSessionID());
                    inSession.putExtra("SearchBy", searchByStr);
                    inSession.putExtra("city", locationStr);
                    inSession.putExtra("sessionName", classNameStr);
                    inSession.putExtra("board", boardStr);
                    inSession.putExtra("stream", streamStr);
                    inSession.putExtra("standard", standardStr);
                    inSession.putExtra("lessionName", arrayList.get(position).getLessionTypeName());
                    inSession.putExtra("sessiondate", cardLayoutBinding.startDateTxt.getText().toString() + " To " + cardLayoutBinding.endDateTxt.getText().toString());
                    inSession.putExtra("duration", cardLayoutBinding.durationTxt.getText().toString());
                    inSession.putExtra("gender", arrayList.get(position).getGenderID());
                    inSession.putExtra("searchType", searchTypeStr);
                    inSession.putExtra("withOR", wheretoComeStr);
                    inSession.putExtra("searchfront", searchfront);
                    inSession.putExtra("sessionType", sessionType);
                    inSession.putExtra("firsttimesearch", firsttimesearch);
                    mContext.startActivity(inSession);
                }
                return true;
            }
        });
//        holder.linear_click.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent inSession = new Intent(mContext, SessionName.class);
//                inSession.putExtra("sessionID", arrayList.get(position).getSessionID());
//                inSession.putExtra("SearchBy", searchByStr);
//                inSession.putExtra("city", locationStr);
//                inSession.putExtra("sessionName", classNameStr);
//                inSession.putExtra("board", boardStr);
//                inSession.putExtra("stream", streamStr);
//                inSession.putExtra("standard", standardStr);
//                inSession.putExtra("lessionName", arrayList.get(position).getLessionTypeName());
//                inSession.putExtra("sessiondate", holder.start_date_txt.getText().toString() + " To " + holder.end_date_txt.getText().toString());
//                inSession.putExtra("duration", holder.duration_txt.getText().toString());
//                inSession.putExtra("gender", arrayList.get(position).getGenderID());
//                inSession.putExtra("searchType", searchTypeStr);
//                inSession.putExtra("withOR", wheretoComeStr);
//                mContext.startActivity(inSession);
//            }
//        });
        cardLayoutBinding.sessionNameTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inSession = new Intent(mContext, SessionName.class);
                inSession.putExtra("sessionID", arrayList.get(position).getSessionID());
                inSession.putExtra("SearchBy", searchByStr);
                inSession.putExtra("city", locationStr);
                inSession.putExtra("sessionName", classNameStr);
                inSession.putExtra("board", boardStr);
                inSession.putExtra("stream", streamStr);
                inSession.putExtra("standard", standardStr);
                inSession.putExtra("lessionName", arrayList.get(position).getLessionTypeName());
                inSession.putExtra("sessiondate", cardLayoutBinding.startDateTxt.getText().toString() + " To " + cardLayoutBinding.endDateTxt.getText().toString());
                inSession.putExtra("duration", cardLayoutBinding.durationTxt.getText().toString());
                inSession.putExtra("gender", arrayList.get(position).getGenderID());
                inSession.putExtra("searchType", searchTypeStr);
                inSession.putExtra("withOR", wheretoComeStr);
                inSession.putExtra("firsttimesearch", firsttimesearch);
                mContext.startActivity(inSession);
            }
        });
        cardLayoutBinding.ratingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                SessionDetail = new ArrayList<>();
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    SessionDetail.add(arrayList.get(position).getSessionName() + "|" + arrayList.get(position).getSessionID());
                    onViewClick.getViewClick();
                }
                return true;
            }
        });
        String[] spiltPipes = arrayList.get(position).getSchedule().split("\\|");
        String[] spiltComma;
        String[] spiltDash;
        Log.d("spilt", "" + spiltPipes.toString());
        for (int i = 0; i < spiltPipes.length; i++) {
            spiltComma = spiltPipes[i].split("\\,");
            spiltDash = spiltComma[1].split("\\-");
            calculateHours(spiltDash[0], spiltDash[1]);
            arrayList.get(position).setDateTime(spiltDash[0]);
            Log.d("DateTime", spiltDash[0]);
            switch (spiltComma[0]) {
                case "sun":
                    cardLayoutBinding.sunTimeTxt.setEnabled(true);
                    cardLayoutBinding.sundayBtn.setEnabled(true);
                    cardLayoutBinding.sunTimeTxt.setAlpha(1);
                    cardLayoutBinding.sundayBtn.setAlpha(1);
                    cardLayoutBinding.sunTimeTxt.setText(arrayList.get(position).getDateTime());
                    break;
                case "mon":
                    cardLayoutBinding.monTimeTxt.setEnabled(true);
                    cardLayoutBinding.mondayBtn.setEnabled(true);
                    cardLayoutBinding.monTimeTxt.setAlpha(1);
                    cardLayoutBinding.mondayBtn.setAlpha(1);
                    cardLayoutBinding.monTimeTxt.setText(arrayList.get(position).getDateTime());
                    break;
                case "tue":
                    cardLayoutBinding.tuesTimeTxt.setEnabled(true);
                    cardLayoutBinding.tuesdayBtn.setEnabled(true);
                    cardLayoutBinding.tuesTimeTxt.setAlpha(1);
                    cardLayoutBinding.tuesdayBtn.setAlpha(1);
                    cardLayoutBinding.tuesTimeTxt.setText(arrayList.get(position).getDateTime());
                    break;
                case "wed":
                    cardLayoutBinding.wedTimeTxt.setEnabled(true);
                    cardLayoutBinding.wednesdayBtn.setEnabled(true);
                    cardLayoutBinding.wedTimeTxt.setAlpha(1);
                    cardLayoutBinding.wednesdayBtn.setAlpha(1);
                    cardLayoutBinding.wedTimeTxt.setText(arrayList.get(position).getDateTime());
                    break;
                case "thu":
                    cardLayoutBinding.thurTimeTxt.setEnabled(true);
                    cardLayoutBinding.thursdayBtn.setEnabled(true);
                    cardLayoutBinding.thurTimeTxt.setAlpha(1);
                    cardLayoutBinding.thursdayBtn.setAlpha(1);
                    cardLayoutBinding.thurTimeTxt.setText(arrayList.get(position).getDateTime());
                    break;
                case "fri":
                    cardLayoutBinding.friTimeTxt.setEnabled(true);
                    cardLayoutBinding.fridayBtn.setEnabled(true);
                    cardLayoutBinding.friTimeTxt.setAlpha(1);
                    cardLayoutBinding.fridayBtn.setAlpha(1);
                    cardLayoutBinding.friTimeTxt.setText(arrayList.get(position).getDateTime());
                    break;
                case "sat":
                    cardLayoutBinding.satTimeTxt.setEnabled(true);
                    cardLayoutBinding.saturdayBtn.setEnabled(true);
                    cardLayoutBinding.satTimeTxt.setAlpha(1);
                    cardLayoutBinding.saturdayBtn.setAlpha(1);
                    cardLayoutBinding.satTimeTxt.setText(arrayList.get(position).getDateTime());
                    break;
                default:

            }
        }
        if (arrayList.get(position).getCoachTypeID().equalsIgnoreCase("1")) {
            cardLayoutBinding.tutorNameTxt.setText("Academic " + "/" + arrayList.get(position).getLessionTypeName());
        } else {
            cardLayoutBinding.tutorNameTxt.setText("Sport " + "/" + arrayList.get(position).getLessionTypeName());
        }

        arrayList.get(position).setDuration(SessionHour + " hr " + SessionMinit + " min");
        cardLayoutBinding.durationTxt.setText(arrayList.get(position).getDuration());
        cardLayoutBinding.locationTxt.setOnClickListener(new View.OnClickListener() {
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

    public void calculateHours(String time1, String time2) {
        Date date1, date2;
        int days, hours, min;
        String hourstr, minstr;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
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

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    public ArrayList<String> getSessionDetail() {
        return SessionDetail;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View view) {
            super(view);

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