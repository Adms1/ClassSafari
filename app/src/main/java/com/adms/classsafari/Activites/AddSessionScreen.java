package com.adms.classsafari.Activites;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import com.adms.classsafari.R;
import com.adms.classsafari.Utils;
import com.adms.classsafari.databinding.ActivityAddSessionScreenBinding;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddSessionScreen extends AppCompatActivity implements com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    ActivityAddSessionScreenBinding addSessionScreenBinding;
    Context mContext;
    String monthDisplayStr, MonthInt, TimeInt, finaldateStr;
    String[] spiltmonth;
    String[] spilttime;
    int Year, Month, Day;
    Calendar calendar;
    private DatePickerDialog datePickerDialog;
    int mYear, mMonth, mDay;
    com.wdullaer.materialdatetimepicker.time.TimePickerDialog timePickerDialog;
    int CalendarHour, CalendarMinute;
    private static boolean isFromTime = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSessionScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_session_screen);
        mContext = AddSessionScreen.this;


        MonthInt = Utils.getTodaysDate();
        Log.d("Date", MonthInt);
        spiltmonth = MonthInt.split("\\/");
        getMonthFun(Integer.parseInt(spiltmonth[1]));

        TimeInt = Utils.getCurrentTime();
        Log.d("Time", TimeInt);
        spilttime = TimeInt.split("\\:");

        init();
        setListner();
    }

    public void init() {
        calendar = Calendar.getInstance();
        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);
        CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
        CalendarMinute = calendar.get(Calendar.MINUTE);

        if (Integer.parseInt(spilttime[0]) > 12) {
            addSessionScreenBinding.displayStarttimeTxt.setText(Utils.getCurrentTime() + "PM");
            addSessionScreenBinding.displayEndtimeTxt.setText(Utils.getCurrentTime() + "PM");
        } else {
            addSessionScreenBinding.displayStarttimeTxt.setText(Utils.getCurrentTime() + "AM");
            addSessionScreenBinding.displayEndtimeTxt.setText(Utils.getCurrentTime() + "AM");
        }
        finaldateStr = spiltmonth[0] + ", " + monthDisplayStr + " " + spiltmonth[2];
        addSessionScreenBinding.displayDateTxt.setText(finaldateStr);
    }

    public void setListner() {
        addSessionScreenBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        addSessionScreenBinding.dateLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog = DatePickerDialog.newInstance(AddSessionScreen.this, Year, Month, Day);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.setOkText("Done");
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.setAccentColor(Color.parseColor("#f2552c"));
                datePickerDialog.setTitle("Select Date From DatePickerDialog");
                datePickerDialog.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        addSessionScreenBinding.displayStarttimeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFromTime = true;
                timePickerDialog = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(AddSessionScreen.this, CalendarHour, CalendarMinute, false);
                timePickerDialog.setThemeDark(false);
                timePickerDialog.setOkText("Done");
                timePickerDialog.setAccentColor(Color.parseColor("#f2552c"));
                timePickerDialog.setTitle("Select Time From TimePickerDialog");
                timePickerDialog.show(getFragmentManager(), "Timepickerdialog");
                timePickerDialog.setVersion(com.wdullaer.materialdatetimepicker.time.TimePickerDialog.Version.VERSION_1);
            }
        });
        addSessionScreenBinding.displayEndtimeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFromTime = false;
                timePickerDialog = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(AddSessionScreen.this, CalendarHour, CalendarMinute, false);
                timePickerDialog.setThemeDark(false);
                timePickerDialog.setOkText("Done");
                timePickerDialog.setAccentColor(Color.parseColor("#f2552c"));
                timePickerDialog.setTitle("Select Time From TimePickerDialog");
                timePickerDialog.show(getFragmentManager(), "Timepickerdialog");
            }
        });
    }

    public void getMonthFun(int month) {
        SimpleDateFormat monthParse = new SimpleDateFormat("MM");
        SimpleDateFormat monthDisplay = new SimpleDateFormat("MMM");
        try {
            monthDisplayStr = monthDisplay.format(monthParse.parse(String.valueOf(month)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("month", "" + monthDisplayStr);
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
        getMonthFun(Integer.parseInt(m));

        addSessionScreenBinding.displayDateTxt.setText(d + ", " + monthDisplayStr + " " + y);
    }

    @Override
    public void onTimeSet(com.wdullaer.materialdatetimepicker.time.TimePickerDialog view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;

        String minuteString = minute < 10 ? "0" + minute : "" + minute;

        String secondString = second < 10 ? "0" + second : "" + second;

        String time = hourString + "/" + minuteString + "/" + secondString;
        Log.d("hours", "" + hourOfDay);
        if (isFromTime) {
            if (hourOfDay > 12) {
                addSessionScreenBinding.displayStarttimeTxt.setText(hourString + ":" + minuteString + " PM");
            } else {
                addSessionScreenBinding.displayStarttimeTxt.setText(hourString + ":" + minuteString + " AM");
            }
            Log.d("Starttime", time);
        } else {
            if (hourOfDay > 12) {
                addSessionScreenBinding.displayEndtimeTxt.setText(hourString + ":" + minuteString + " PM");
            } else {
                addSessionScreenBinding.displayEndtimeTxt.setText(hourString + ":" + minuteString + " AM");
            }
            Log.d("Endtime", time);
        }

        CalculateTime(addSessionScreenBinding.displayStarttimeTxt.getText().toString(), addSessionScreenBinding.displayEndtimeTxt.getText().toString());
    }

    public void CalculateTime(String stTime, String enTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date startDate = null, endDate = null;
        try {
            startDate = simpleDateFormat.parse(stTime);
            endDate = simpleDateFormat.parse(enTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long difference = endDate.getTime() - startDate.getTime();
        if (difference < 0) {
            Date dateMax = null, dateMin = null;
            try {
                dateMax = simpleDateFormat.parse("24:00");
                dateMin = simpleDateFormat.parse("00:00");

            } catch (ParseException e) {
                e.printStackTrace();
            }

            difference = (dateMax.getTime() - startDate.getTime()) + (endDate.getTime() - dateMin.getTime());
        }
        int days = (int) (difference / (1000 * 60 * 60 * 24));
        int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
        int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
        Log.d("log_tag", "Hours: " + hours + ", Mins: " + min);

        addSessionScreenBinding.displayDurationTxt.setText("Duration : " + hours + " hr");
    }
}
