package com.adms.classsafari.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adms.classsafari.Model.Session.sessionDataModel;
import com.adms.classsafari.R;

import java.util.List;

/**
 * Created by admsandroid on 3/5/2018.
 */

public class SessionDetailAdapter extends RecyclerView.Adapter {

    private Context mContext;
    List<sessionDataModel> arrayList;
    private final static int HEADER_VIEW = 0;
    private final static int ROW_VIEW = 2;
    private final static int CONTENT_VIEW = 1;
    String address;
    public SessionDetailAdapter(Context mContext, List<sessionDataModel> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }


    public class SessionCard extends RecyclerView.ViewHolder {
        public TextView session_name_txt, session_type_txt, session_detail_name_txt,
                session_board_txt, session_standard_txt, session_stream_txt, session_total_student_txt,
                address_txt,email_txt,phone_txt;


        public SessionCard(View view) {
            super(view);
            session_name_txt = (TextView) view.findViewById(R.id.session_name_txt);
            session_type_txt = (TextView) view.findViewById(R.id.session_type_txt);
            session_detail_name_txt = (TextView) view.findViewById(R.id.session_detail_name_txt);
            session_board_txt = (TextView) view.findViewById(R.id.session_board_txt);
            session_standard_txt = (TextView) view.findViewById(R.id.session_standard_txt);
            session_stream_txt = (TextView) view.findViewById(R.id.session_stream_txt);
            session_total_student_txt = (TextView) view.findViewById(R.id.session_total_student_txt);
            address_txt = (TextView) view.findViewById(R.id.address_txt);
            email_txt=(TextView)view.findViewById(R.id.email_txt);
            phone_txt=(TextView)view.findViewById(R.id.phone_txt);

        }
    }

    public class RowView extends RecyclerView.ViewHolder {

        public RowView(View itemView) {
            super(itemView);
        }
    }

    public class ReviewCard extends RecyclerView.ViewHolder {
        public TextView session_name_txt;


        public ReviewCard(View view) {
            super(view);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case HEADER_VIEW:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.session_card_layout_1, parent, false);
                return new SessionCard(view);

//            case ROW_VIEW:
//                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_reviews, parent, false);
//                return new RowView(view);

            case CONTENT_VIEW:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.session_review_card_layout_1, parent, false);
                return new ReviewCard(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SessionCard) {
            final SessionCard headerHolder = (SessionCard) holder;
            headerHolder.session_name_txt.setText(arrayList.get(position).getSessionName());
            if (arrayList.get(position).getSessionType().equalsIgnoreCase("1")) {
                headerHolder.session_type_txt.setText("Academic");
            } else {
                headerHolder.session_type_txt.setText("Sport");
            }
            address = arrayList.get(position).getAddressLine1() +
                    "," + arrayList.get(position).getRegionName() +
                    "," + arrayList.get(position).getAddressCity() +
                    "," + arrayList.get(position).getAddressState() +
                    "-" + arrayList.get(position).getAddressZipCode();
            headerHolder.session_detail_name_txt.setText(arrayList.get(position).getLessionTypeName());
            headerHolder.session_board_txt.setText(arrayList.get(position).getBoard());
            headerHolder.session_standard_txt.setText(arrayList.get(position).getStandard());
            headerHolder.session_stream_txt.setText(arrayList.get(position).getStream());
            headerHolder.session_total_student_txt.setText(arrayList.get(position).getSessionCapacity());
            headerHolder.address_txt.setText(address);
            headerHolder.email_txt.setText(arrayList.get(position).getEmailAddress());
            headerHolder.phone_txt.setText(arrayList.get(position).getContactPhoneNumber());

            headerHolder.phone_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.fromParts("tel", headerHolder.phone_txt.getText().toString(), null));
                    mContext.startActivity(intent);
                }
            });
            headerHolder.address_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    mContext.startActivity(mapIntent);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {

        switch (position) {
            case 0:
                return HEADER_VIEW;
//            case 0:
//                return ROW_VIEW;
            default:
                return CONTENT_VIEW;
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size() + 1;
    }
}

