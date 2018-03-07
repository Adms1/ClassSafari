package com.adms.classsafari.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adms.classsafari.Activites.SessionName;
import com.adms.classsafari.R;

import java.util.ArrayList;

/**
 * Created by admsandroid on 3/5/2018.
 */

public class SessionDetailAdapter extends RecyclerView.Adapter {

    private Context mContext;
    ArrayList<String> arrayList;
    private final static int HEADER_VIEW = 0;
    private final static int ROW_VIEW = 0;
    private final static int CONTENT_VIEW = 1;

    public SessionDetailAdapter(Context mContext, ArrayList<String> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }


    public class SessionCard extends RecyclerView.ViewHolder {
        public TextView session_name_txt;


        public SessionCard(View view) {
            super(view);

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
//            case HEADER_VIEW:
//                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.session_card_layout_1, parent, false);
//                return new SessionCard(view);

            case ROW_VIEW:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_reviews, parent, false);
                return new RowView(view);

            case CONTENT_VIEW:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.session_review_card_layout_1, parent, false);
                return new ReviewCard(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {

        switch (position) {
//            case 0:
//                return HEADER_VIEW;
            case 0:
                return ROW_VIEW;
            default:
                return CONTENT_VIEW;
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size()+1;
    }
}

