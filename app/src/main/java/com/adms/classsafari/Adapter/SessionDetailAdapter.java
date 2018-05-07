package com.adms.classsafari.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.adms.classsafari.Interface.onViewClick;
import com.adms.classsafari.Model.Session.sessionDataModel;
import com.adms.classsafari.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admsandroid on 3/5/2018.
 */

public class SessionDetailAdapter extends RecyclerView.Adapter {

    private Context mContext;
    List<sessionDataModel> arrayList, sessionRatingList;
    private final static int HEADER_VIEW = 0;
    private final static int ROW_VIEW = 2;
    private final static int CONTENT_VIEW = 1;
    String address;
    private ArrayList<String> SessionDetail;
    onViewClick onViewClick;

    public SessionDetailAdapter(Context mContext, List<sessionDataModel> arrayList,
                                List<sessionDataModel> sessionRatingList, onViewClick onViewClick) {
        this.mContext = mContext;
        this.arrayList = arrayList;
        this.onViewClick = onViewClick;
        this.sessionRatingList = sessionRatingList;
    }


    public class SessionCard extends RecyclerView.ViewHolder {
        public TextView session_name_txt, session_type_txt, session_detail_name_txt,
                session_board_txt, session_standard_txt, session_stream_txt, session_total_student_txt,
                address_txt, email_txt, phone_txt;

        RatingBar session_ratingbar;

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
            email_txt = (TextView) view.findViewById(R.id.email_txt);
            phone_txt = (TextView) view.findViewById(R.id.phone_txt);
            session_ratingbar = (RatingBar) view.findViewById(R.id.session_ratingbar);

        }
    }

    public class RowView extends RecyclerView.ViewHolder {

        public RowView(View itemView) {
            super(itemView);
        }
    }

    public class ReviewCard extends RecyclerView.ViewHolder {
        public TextView reviewcomment_type_txt, re_review_date_txt, reviewcomment_txt, reviewuser_name_txt;
        RatingBar session_reviews_rating_bar;
        ImageView person_image;


        public ReviewCard(View view) {
            super(view);
            reviewcomment_type_txt = (TextView) view.findViewById(R.id.comment_type_txt);
            re_review_date_txt = (TextView) view.findViewById(R.id.review_date_txt);
            reviewcomment_txt = (TextView) view.findViewById(R.id.comment_txt);
            reviewuser_name_txt = (TextView) view.findViewById(R.id.user_name_txt);
            session_reviews_rating_bar = (RatingBar) view.findViewById(R.id.session_reviews_rating_bar);
            person_image = (ImageView) view.findViewById(R.id.person_image);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        if (viewType == HEADER_VIEW) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.session_card_layout_1, parent, false);
            return new SessionCard(view);
        }

        if (viewType == CONTENT_VIEW) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.session_review_card_layout_1, parent, false);
                return new ReviewCard(view);
        }
        return null;
//        switch (viewType) {
//            case HEADER_VIEW:
//                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.session_card_layout_1, parent, false);
////                return new SessionCard(view);
//
////            case ROW_VIEW:
////                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_reviews, parent, false);
////                return new RowView(view);
//
//            case CONTENT_VIEW:
//                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.session_review_card_layout_1, parent, false);
//                return new ReviewCard(view);
//        }
//        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof SessionCard) {
            final SessionCard headerHolder = (SessionCard) holder;
            headerHolder.session_name_txt.setText(arrayList.get(position).getSessionName());
            headerHolder.session_ratingbar.setRating(Float.parseFloat(arrayList.get(position).getRating()));
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
            headerHolder.session_ratingbar.setOnTouchListener(new View.OnTouchListener() {
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
        }
        if (holder instanceof ReviewCard) {
            final ReviewCard rowHolder = (ReviewCard) holder;
            if(sessionRatingList.get(position-arrayList.size()).getRatingValue()==1){
                rowHolder.reviewcomment_type_txt.setText("Very poor");
            }else if(sessionRatingList.get(position-arrayList.size()).getRatingValue()==2){
                rowHolder.reviewcomment_type_txt.setText("Poor");
            }else if(sessionRatingList.get(position-arrayList.size()).getRatingValue()==3){
                rowHolder.reviewcomment_type_txt.setText("Average");
            }else if(sessionRatingList.get(position-arrayList.size()).getRatingValue()==4){
                rowHolder.reviewcomment_type_txt.setText("Good");
            }else if(sessionRatingList.get(position-arrayList.size()).getRatingValue()==5){
                rowHolder.reviewcomment_type_txt.setText("Excellent");
            }
            rowHolder.session_reviews_rating_bar.setRating(Float.parseFloat(String.valueOf(sessionRatingList.get(position-arrayList.size()).getRatingValue())));
            rowHolder.re_review_date_txt.setText(sessionRatingList.get(position-arrayList.size()).getCreateDate());
            rowHolder.reviewuser_name_txt.setText(sessionRatingList.get(position-arrayList.size()).getFirstName() + " " + sessionRatingList.get(position-arrayList.size()).getLastName());
            rowHolder.reviewcomment_txt.setText(sessionRatingList.get(position-arrayList.size()).getComment());
        }
    }

    @Override
    public int getItemViewType(int position) {

        if(position<arrayList.size()){
            return HEADER_VIEW;
        }
        if(position-arrayList.size()<sessionRatingList.size()){
            return CONTENT_VIEW;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return arrayList.size() + sessionRatingList.size();
    }

    public ArrayList<String> getSessionDetail() {
        return SessionDetail;
    }
}

