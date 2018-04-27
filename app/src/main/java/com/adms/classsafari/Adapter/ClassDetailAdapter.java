package com.adms.classsafari.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.adms.classsafari.Activites.SessionName;
import com.adms.classsafari.Model.Session.sessionDataModel;
import com.adms.classsafari.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by admsandroid on 3/5/2018.
 */

public class ClassDetailAdapter extends RecyclerView.Adapter<ClassDetailAdapter.MyViewHolder> {

    private Context mContext;
    List<sessionDataModel> arrayList;
    int SessionHour = 0;
    Integer SessionMinit = 0;
    String searchByStr, locationStr, classNameStr, address, boardStr, streamStr, standardStr;

    public ClassDetailAdapter(Context mContext, List<sessionDataModel> arrayList, String searchByStr, String locationStr,
                              String classNameStr, String boardStr, String streamStr, String standardStr) {
        this.mContext = mContext;
        this.arrayList = arrayList;
        this.searchByStr = searchByStr;
        this.locationStr = locationStr;
        this.classNameStr = classNameStr;
        this.boardStr = boardStr;
        this.streamStr = streamStr;
        this.standardStr = standardStr;
//        setHasStableIds(true);

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView session_name_txt, price_txt, start_date_txt, end_date_txt,
                mon_time_txt, tues_time_txt, wed_time_txt, thur_time_txt, fri_time_txt, sat_time_txt, sun_time_txt,
                location_txt, duration_txt;
        public Button monday_btn, tuesday_btn, wednesday_btn, thursday_btn, friday_btn, saturday_btn, sunday_btn;

        public MyViewHolder(View view) {
            super(view);
            session_name_txt = (TextView) view.findViewById(R.id.session_name_txt);
            price_txt = (TextView) view.findViewById(R.id.price_txt);
            start_date_txt = (TextView) view.findViewById(R.id.start_date_txt);
            end_date_txt = (TextView) view.findViewById(R.id.end_date_txt);

            mon_time_txt = (TextView) view.findViewById(R.id.mon_time_txt);
            tues_time_txt = (TextView) view.findViewById(R.id.tues_time_txt);
            wed_time_txt = (TextView) view.findViewById(R.id.wed_time_txt);
            thur_time_txt = (TextView) view.findViewById(R.id.thur_time_txt);
            fri_time_txt = (TextView) view.findViewById(R.id.fri_time_txt);
            sat_time_txt = (TextView) view.findViewById(R.id.sat_time_txt);
            sun_time_txt = (TextView) view.findViewById(R.id.sun_time_txt);

            location_txt = (TextView) view.findViewById(R.id.location_txt);
            duration_txt = (TextView) view.findViewById(R.id.duration_txt);

            monday_btn = (Button) view.findViewById(R.id.monday_btn);
            tuesday_btn = (Button) view.findViewById(R.id.tuesday_btn);
            wednesday_btn = (Button) view.findViewById(R.id.wednesday_btn);
            thursday_btn = (Button) view.findViewById(R.id.thursday_btn);
            friday_btn = (Button) view.findViewById(R.id.friday_btn);
            saturday_btn = (Button) view.findViewById(R.id.saturday_btn);
            sunday_btn = (Button) view.findViewById(R.id.sunday_btn);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        address = arrayList.get(position).getAddressLine1() +
                "," + arrayList.get(position).getRegionName() +
                "," + arrayList.get(position).getAddressCity() +
                "," + arrayList.get(position).getAddressState() +
                "-" + arrayList.get(position).getAddressZipCode();
        holder.session_name_txt.setText(arrayList.get(position).getSessionName());
        if (arrayList.get(position).getSessionAmount().equalsIgnoreCase("Free")) {
            holder.price_txt.setText(arrayList.get(position).getSessionAmount());
        } else {
            holder.price_txt.setText("₹ " + arrayList.get(position).getSessionAmount());
        }
        holder.start_date_txt.setText(arrayList.get(position).getStartDate());
        holder.end_date_txt.setText(arrayList.get(position).getEndDate());
        holder.location_txt.setText(arrayList.get(position).getRegionName());
        holder.session_name_txt.setOnClickListener(new View.OnClickListener() {
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
                inSession.putExtra("sessiondate", holder.start_date_txt.getText().toString() + " To " + holder.end_date_txt.getText().toString());
                inSession.putExtra("duration", holder.duration_txt.getText().toString());
                mContext.startActivity(inSession);
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
                    holder.sun_time_txt.setEnabled(true);
                    holder.sunday_btn.setEnabled(true);
                    holder.sun_time_txt.setAlpha(1);
                    holder.sunday_btn.setAlpha(1);
                    holder.sun_time_txt.setText(arrayList.get(position).getDateTime());
                    break;
                case "mon":
                    holder.mon_time_txt.setEnabled(true);
                    holder.monday_btn.setEnabled(true);
                    holder.mon_time_txt.setAlpha(1);
                    holder.monday_btn.setAlpha(1);
                    holder.mon_time_txt.setText(arrayList.get(position).getDateTime());
                    break;
                case "tue":
                    holder.tues_time_txt.setEnabled(true);
                    holder.tuesday_btn.setEnabled(true);
                    holder.tues_time_txt.setAlpha(1);
                    holder.tuesday_btn.setAlpha(1);
                    holder.tues_time_txt.setText(arrayList.get(position).getDateTime());
                    break;
                case "wed":
                    holder.wed_time_txt.setEnabled(true);
                    holder.wednesday_btn.setEnabled(true);
                    holder.wed_time_txt.setAlpha(1);
                    holder.wednesday_btn.setAlpha(1);
                    holder.wed_time_txt.setText(arrayList.get(position).getDateTime());
                    break;
                case "thu":
                    holder.thur_time_txt.setEnabled(true);
                    holder.thursday_btn.setEnabled(true);
                    holder.thur_time_txt.setAlpha(1);
                    holder.thursday_btn.setAlpha(1);
                    holder.thur_time_txt.setText(arrayList.get(position).getDateTime());
                    break;
                case "fri":
                    holder.fri_time_txt.setEnabled(true);
                    holder.friday_btn.setEnabled(true);
                    holder.fri_time_txt.setAlpha(1);
                    holder.friday_btn.setAlpha(1);
                    holder.fri_time_txt.setText(arrayList.get(position).getDateTime());
                    break;
                case "sat":
                    holder.sat_time_txt.setEnabled(true);
                    holder.saturday_btn.setEnabled(true);
                    holder.sat_time_txt.setAlpha(1);
                    holder.saturday_btn.setAlpha(1);
                    holder.sat_time_txt.setText(arrayList.get(position).getDateTime());
                    break;
                default:

            }
        }
        arrayList.get(position).setDuration(SessionHour + " hr " + SessionMinit + " min");
        holder.duration_txt.setText(arrayList.get(position).getDuration());
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
//            if(min>0){
            SessionMinit = min;
//            }else{
//                SessionMinit=null;
//            }

            Log.i("======= Hours", " :: " + hours + ":" + min);

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

}
