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
import java.util.Locale;

/**
 * Created by admsandroid on 3/5/2018.
 */

public class ClassDetailAdapter extends RecyclerView.Adapter<ClassDetailAdapter.MyViewHolder> {

    List<sessionDataModel> arrayList;
    int SessionHour = 0;
    Integer SessionMinit = 0;
    String locationStr, classNameStr,
            address, boardStr, streamStr, standardStr,
            searchfront,
            firsttimesearch, SearchPlaystudy, RegionName;//searchTypeStr, wheretoComeStr,
    onViewClick onViewClick;
    bookClick bookClick;
    CardLayoutBinding cardLayoutBinding;
    String hours, minit;
    private Context mContext;
    private ArrayList<String> SessionDetail;
    private ArrayList<String> SessionBookDetail;

    public ClassDetailAdapter(Context mContext, List<sessionDataModel> arrayList, String locationStr,
                              String classNameStr, String boardStr, String streamStr, String standardStr,
                              String SearchPlaystudy, String searchfront, String firsttimesearch, String regionName,
                              bookClick bookClick, onViewClick onViewClick) {// searchByStr,   String wheretoComeStr, String searchTypeStr,
        this.mContext = mContext;
        this.arrayList = arrayList;

        this.locationStr = locationStr;
        this.classNameStr = classNameStr;
        this.boardStr = boardStr;
        this.streamStr = streamStr;
        this.standardStr = standardStr;

        this.searchfront = searchfront;
        this.onViewClick = onViewClick;
        this.SearchPlaystudy = SearchPlaystudy;
        this.firsttimesearch = firsttimesearch;
        this.RegionName = regionName;
        this.bookClick = bookClick;
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
    public void onBindViewHolder(MyViewHolder holder, final int position) {
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
            cardLayoutBinding.priceTxt.setText("₹" + arrayList.get(position).getSessionAmount());
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
                    inSession.putExtra("city", locationStr);
                    inSession.putExtra("sessionName", classNameStr);
                    inSession.putExtra("board", boardStr);
                    inSession.putExtra("stream", streamStr);
                    inSession.putExtra("standard", standardStr);
                    inSession.putExtra("lessionName", "");//arrayList.get(position).getLessionTypeName()
                    inSession.putExtra("sessiondate", cardLayoutBinding.startDateTxt.getText().toString() + " To " + cardLayoutBinding.endDateTxt.getText().toString());
//                    inSession.putExtra("duration", cardLayoutBinding.durationTxt.getText().toString());
                    inSession.putExtra("gender", arrayList.get(position).getGenderID());
//                    inSession.putExtra("searchType", searchTypeStr);
//                    inSession.putExtra("withOR", wheretoComeStr);
                    inSession.putExtra("SearchPlaystudy", SearchPlaystudy);
                    inSession.putExtra("searchfront", searchfront);
                    inSession.putExtra("firsttimesearch", firsttimesearch);
                    inSession.putExtra("RegionName", RegionName);
                    mContext.startActivity(inSession);
                }
                return true;
            }
        });
        cardLayoutBinding.sessionNameTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inSession = new Intent(mContext, SessionName.class);
                inSession.putExtra("sessionID", arrayList.get(position).getSessionID());
                inSession.putExtra("city", locationStr);
                inSession.putExtra("sessionName", classNameStr);
                inSession.putExtra("board", boardStr);
                inSession.putExtra("stream", streamStr);
                inSession.putExtra("standard", standardStr);
                inSession.putExtra("lessionName", "");//arrayList.get(position).getLessionTypeName()
                inSession.putExtra("sessiondate", cardLayoutBinding.startDateTxt.getText().toString() + " To " + cardLayoutBinding.endDateTxt.getText().toString());
//                inSession.putExtra("duration", cardLayoutBinding.durationTxt.getText().toString());
                inSession.putExtra("gender", arrayList.get(position).getGenderID());
//                inSession.putExtra("searchType", searchTypeStr);
//                inSession.putExtra("withOR", wheretoComeStr);
                inSession.putExtra("SearchPlaystudy", SearchPlaystudy);
                inSession.putExtra("firsttimesearch", firsttimesearch);
                inSession.putExtra("RegionName", RegionName);
                mContext.startActivity(inSession);
            }
        });
        cardLayoutBinding.bookSessionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionBookDetail = new ArrayList<>();
                SessionBookDetail.add(arrayList.get(position).getSessionID());
                bookClick.bookClick();
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
//        if (arrayList.get(position).getCoachTypeID().equalsIgnoreCase("1")) {
//            cardLayoutBinding.tutorNameTxt.setText("Academic " + "/" + arrayList.get(position).getName());
//        } else {
        cardLayoutBinding.tutorNameTxt.setText(arrayList.get(position).getName());//"Sports " + "/" +
//        }
        //String hours,minit;
//        if (SessionHour < 10) {
//            hours = "0" + SessionHour;
//        } else {
//            hours = String.valueOf(SessionHour);
//        }
//        if (SessionMinit < 10) {
//            minit = "0" + SessionMinit;
//        } else {
//            minit = String.valueOf(SessionMinit);
//        }
//        if (minit.equalsIgnoreCase(("00"))) {
//            arrayList.get(position).setDuration(hours + " hr ");//+ " min"
//        } else {
//            arrayList.get(position).setDuration(hours + " hr " + minit + " min");//+ " min"
//        }
//        cardLayoutBinding.durationTxt.setText(arrayList.get(position).getDuration());
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
        return arrayList.size();
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

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    public ArrayList<String> getSessionDetail() {
        return SessionDetail;
    }

    public ArrayList<String> getSessionBookDetail() {
        return SessionBookDetail;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        Button book_session_btn,sunday_btn,saturday_btn,friday_btn,thursday_btn,wednesday_btn,tuesday_btn,monday_btn;
        TextView sun_hours_txt, sat_hours_txt, fri_hours_txt, thur_hours_txt, wed_hours_txt, tues_hours_txt, mon_hours_txt;
        TextView sun_time_txt, sat_time_txt, fri_time_txt, thur_time_txt, wed_time_txt, tues_time_txt, mon_time_txt,
                price_txt,end_date_txt,start_date_txt,location_txt,tutor_name_txt,rating_user_txt,session_name_txt;
        RatingBar rating_bar;
        LinearLayout linear_click;

        public MyViewHolder(View view) {
            super(view);

        }
    }
}
