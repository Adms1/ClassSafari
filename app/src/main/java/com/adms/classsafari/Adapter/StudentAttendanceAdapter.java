package com.adms.classsafari.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.adms.classsafari.Model.Session.SessionDetailModel;
import com.adms.classsafari.Model.Session.sessionDataModel;
import com.adms.classsafari.R;

/**
 * Created by admsandroid on 3/12/2018.
 */

public class StudentAttendanceAdapter extends BaseAdapter {
    private Context mContext;
//    private List<sessionDataModel> studentList;
    private SessionDetailModel studentList;
    public StudentAttendanceAdapter(Context mContext, SessionDetailModel studentList) {
        this.mContext = mContext;
        this.studentList = studentList;
    }

    private class ViewHolder {
        TextView name_txt,no_txt;
        EditText remark_txt;
        RadioGroup attendance_status_rg;
        RadioButton present_chk, absent_chk, leave_chk;

    }

    @Override
    public int getCount() {
        return studentList.getData().size();
    }

    @Override
    public sessionDataModel getItem(int position) {
        return studentList.getData().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        View view = null;
        convertView = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_item_student_attendance, null);
            viewHolder.name_txt = (TextView) convertView.findViewById(R.id.name_txt);
            viewHolder.attendance_status_rg = (RadioGroup) convertView.findViewById(R.id.attendance_status_rg);
            viewHolder.present_chk = (RadioButton) convertView.findViewById(R.id.present_chk);
            viewHolder.absent_chk = (RadioButton) convertView.findViewById(R.id.absent_chk);
            viewHolder.leave_chk = (RadioButton) convertView.findViewById(R.id.leave_chk);
            viewHolder.remark_txt = (EditText) convertView.findViewById(R.id.remark_txt);
            viewHolder.no_txt=(TextView)convertView.findViewById(R.id.no_txt);

            Typeface custom_font = Typeface.createFromAsset(mContext.getAssets(), "font/TitilliumWeb-Regular.ttf");
           viewHolder.name_txt.setTypeface(custom_font);
            viewHolder.present_chk.setTypeface(custom_font);
            viewHolder.absent_chk.setTypeface(custom_font);
            viewHolder.leave_chk.setTypeface(custom_font);
            viewHolder.remark_txt.setTypeface(custom_font);

            String sr = String.valueOf(position + 1);
            viewHolder.no_txt.setText(sr);

            final sessionDataModel session = studentList.getData().get(position);
            viewHolder.name_txt.setText(session.getFirstName() + " " + session.getLastName());
            viewHolder.remark_txt.setText(session.getReason());
            viewHolder.remark_txt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    session.setReason(viewHolder.remark_txt.getText().toString());
                }
            });


            viewHolder.attendance_status_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    int RadioButtonId = viewHolder.attendance_status_rg.getCheckedRadioButtonId();
                    switch (RadioButtonId) {
                        case R.id.present_chk:
                            session.setAttendanceID("1");
                            session.setStatus("1");
                            break;

                        case R.id.absent_chk:
                            session.setAttendanceID("2");
                            session.setStatus("1");
                            break;

                        case R.id.leave_chk:
                            session.setAttendanceID("3");
                            session.setStatus("1");
                            break;
                        default:
                    }

                }
            });

            switch (Integer.parseInt(session.getAttendanceID())) {
                case 0:
                    session.setStatus("0");
                    break;
                case 1:
                    viewHolder.present_chk.setChecked(true);
                    break;
                case 2:
                    viewHolder.absent_chk.setChecked(true);
                    break;
                case 3:
                    viewHolder.leave_chk.setChecked(true);
                    break;
                default:
            }
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }
}
