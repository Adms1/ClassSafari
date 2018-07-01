package com.adms.classsafari.Adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RadioGroup;

import com.adms.classsafari.Interface.onChlidClick;
import com.adms.classsafari.Interface.onViewClick;
import com.adms.classsafari.Model.TeacherInfo.ChildDetailModel;
import com.adms.classsafari.R;
import com.adms.classsafari.databinding.AddStudentHeaderBinding;
import com.adms.classsafari.databinding.ListGroupFamilyListBinding;
import com.adms.classsafari.databinding.ListItemSelectStudentBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by admsandroid on 3/26/2018.
 */

public class ExpandableSelectStudentListAdapter extends BaseExpandableListAdapter {

    String FamilyID;
    ArrayList<String> value;
    String froncontanctStr, arraowStr;
    private Context mContext;
    private List<String> _listDataHeader;
    private HashMap<String, List<ChildDetailModel>> _listDataChild;
    private onViewClick onViewClick;
    private onChlidClick onChlidClick;
    private ArrayList<String> familyIdCheck;
    private ArrayList<String> sesionDeatil;

    public ExpandableSelectStudentListAdapter(Context mContext, List<String> listDataHeader,
                                              HashMap<String, List<ChildDetailModel>> listDataChild, String froncontanctStr, String arraowStr, onChlidClick onChlidClick, onViewClick session) {
        this.mContext = mContext;
        this._listDataChild = listDataChild;
        this._listDataHeader = listDataHeader;
        this.onViewClick = session;
        this.onChlidClick = onChlidClick;
        this.froncontanctStr = froncontanctStr;
        this.arraowStr = arraowStr;
    }


    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final ListItemSelectStudentBinding itembinding;
        AddStudentHeaderBinding headerBinding;

        if (childPosition > 0) {// && childPosition < getChildrenCount(groupPosition) - 1
            itembinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.list_item_select_student, parent, false);
            convertView = itembinding.getRoot();
            Typeface custom_font = Typeface.createFromAsset(mContext.getAssets(), "font/TitilliumWeb-Regular.ttf");

            itembinding.nameRb.setTypeface(custom_font);
            itembinding.phoneNoTxt.setTypeface(custom_font);
            itembinding.ageTxt.setTypeface(custom_font);
            itembinding.genderTxt.setTypeface(custom_font);
            itembinding.phoneNoTxt.setTypeface(custom_font);

            final ChildDetailModel childDetail = getChild(groupPosition, childPosition - 1);
            itembinding.nameRb.setText(childDetail.getFirstName() + " " + childDetail.getLastName());

            if (childDetail.getGenderID().equalsIgnoreCase("1")) {
                itembinding.genderTxt.setText("Male");
            } else {
                itembinding.genderTxt.setText("Female");
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
            Date d = null;
            try {
                d = sdf.parse(childDetail.getDateofBirth());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String formatedate = output.format(d);
            String[] spilt = formatedate.split("\\/");
            int dd, mm, yy;
            dd = Integer.parseInt(spilt[0]);
            mm = Integer.parseInt(spilt[1]);
            yy = Integer.parseInt(spilt[2]);

            Calendar dob = Calendar.getInstance();
            Calendar today = Calendar.getInstance();
            dob.set(yy, mm, dd);

            int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

            if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }

            Integer ageInt = new Integer(age);
            String ageS = ageInt.toString();
            Log.d("age", ageS);
            itembinding.ageTxt.setText(ageS);
            itembinding.phoneNoTxt.setText(childDetail.getContactPhoneNumber());
            if (!froncontanctStr.equalsIgnoreCase("true")) {
                itembinding.nameRb.setChecked(false);
                itembinding.nameRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        int RadioButtonid = itembinding.nameRg.getCheckedRadioButtonId();
                        switch (RadioButtonid) {
                            case R.id.name_rb:
                                sesionDeatil = new ArrayList<String>();
                                sesionDeatil.add(childDetail.getFirstName() + "|" + childDetail.getLastName() + "|" + childDetail.getContactID() + "|" + "STUDENT NAME" + "|" + "Child");
                                onViewClick.getViewClick();
                                itembinding.nameRb.setChecked(false);
                                break;
                        }

                    }
                });
            } else {
                itembinding.nameRb.setChecked(true);
            }
            itembinding.phoneNoTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.fromParts("tel", itembinding.phoneNoTxt.getText().toString(), null));
                    mContext.startActivity(intent);

                }
            });

        } else {
            headerBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.add_student_header, parent, false);

            String headerTitle = (String) getGroup(groupPosition);
            String valueStr;
            value = new ArrayList<>();
            for (String key : _listDataChild.keySet()) {
                System.out.println("------------------------------------------------");
                System.out.println("Iterating or looping map using java5 foreach loop");
                System.out.println("key: " + key + " value: " + _listDataChild.get(key));
                value.add(key);


            }
            for (int i = 0; i < value.size(); i++) {
                if (headerTitle.equalsIgnoreCase(value.get(i))) {
                    valueStr = String.valueOf(_listDataChild.get(value.get(i)));
                    Log.d("value", valueStr);

                    if (valueStr.equalsIgnoreCase("[]")) {
                        headerBinding.tableRowNodata.setVisibility(View.VISIBLE);
                        headerBinding.tableRowHeader.setVisibility(View.GONE);
                    } else {
                        headerBinding.tableRowNodata.setVisibility(View.GONE);
                        headerBinding.tableRowHeader.setVisibility(View.GONE);
                    }
                }
            }
            convertView = headerBinding.getRoot();
        }


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size() + 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public ChildDetailModel getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        final ListGroupFamilyListBinding groupbinding;
        String headerTitle = (String) getGroup(groupPosition);
        final String[] spiltValue = headerTitle.split("\\|");
        if (convertView == null) {

        }
        groupbinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.list_group_family_list, parent, false);
        convertView = groupbinding.getRoot();

            Typeface custom_font = Typeface.createFromAsset(mContext.getAssets(), "font/TitilliumWeb-Regular.ttf");

        groupbinding.familynameRb.setTypeface(custom_font);
        groupbinding.noTxt.setTypeface(custom_font);
        groupbinding.addchildTxt.setTypeface(custom_font);

        if (arraowStr.equalsIgnoreCase("Activity")) {
            groupbinding.arrowImg.setVisibility(View.GONE);
            groupbinding.addchildTxt.setVisibility(View.GONE);
        } else {
            groupbinding.arrowImg.setVisibility(View.VISIBLE);
            groupbinding.addchildTxt.setVisibility(View.VISIBLE);
            groupbinding.arrowImg.setImageResource(R.drawable.down_add_family);
        }
//        if (isExpanded) {
//            groupbinding.llHeader.setBackgroundColor(mContext.getResources().getColor(R.color.blue));
////            groupbinding.arrowImg.setBackgroundResource(R.drawable.round_yello);
//            groupbinding.arrowImg.setImageResource(R.drawable.up_add_family);
//        } else {
//            groupbinding.llHeader.setBackgroundColor(mContext.getResources().getColor(R.color.white));
////            convertView.setBackgroundResource(R.drawable.unselected_header);
////            groupbinding.arrowImg.setBackgroundResource(R.drawable.round_yello);
//            groupbinding.arrowImg.setImageResource(R.drawable.down_add_family);
//        }
        groupbinding.familynameRb.setText(spiltValue[0] + " " + spiltValue[1]);
        groupbinding.noTxt.setText(spiltValue[2]);
        FamilyID = spiltValue[3];

        if (!froncontanctStr.equalsIgnoreCase("true")) {
            groupbinding.familynameRb.setChecked(false);
            groupbinding.familynameRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    int RadioButtonid = groupbinding.familynameRg.getCheckedRadioButtonId();
                    switch (RadioButtonid) {
                        case R.id.familyname_rb:
                            sesionDeatil = new ArrayList<String>();
                            sesionDeatil.add(spiltValue[0] + "|" + spiltValue[1] + "|" + spiltValue[4] + "|" + "FAMILY NAME" + "|" + "Family");
                            onViewClick.getViewClick();
                            groupbinding.familynameRb.setChecked(false);
                            break;
                    }
                }
            });
        } else {
            groupbinding.familynameRb.setChecked(true);

        }
        groupbinding.addchildTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                familyIdCheck = new ArrayList<String>();
                familyIdCheck.add(spiltValue[3] + "|" + spiltValue[0] + "|" + spiltValue[1]+"|"+spiltValue[2]);
                onChlidClick.getChilClick();
            }
        });

        groupbinding.noTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                boolean result = Util.checkPermission(mContext);
//                if (result) {
//
//                }
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.fromParts("tel", spiltValue[2], null));
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public ArrayList<String> getFamilyID() {
        return familyIdCheck;
    }

    public ArrayList<String> getSessionDetail() {
        return sesionDeatil;
    }

    private String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();
        Log.d("age", ageS);
        return ageS;
    }
}



