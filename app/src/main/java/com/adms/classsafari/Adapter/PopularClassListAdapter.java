package com.adms.classsafari.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.adms.classsafari.Interface.onViewClick;
import com.adms.classsafari.Model.Session.sessionDataModel;
import com.adms.classsafari.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admsandroid on 3/8/2018.
 */

public class PopularClassListAdapter extends RecyclerView.Adapter<PopularClassListAdapter.MyViewHolder> {

    private Context mContext;
    List<sessionDataModel> arrayList;
    onViewClick onViewClick;
    private ArrayList<String> SessionDetail;


    public PopularClassListAdapter(Context mContext, List<sessionDataModel> arrayList, onViewClick onViewClick) {
        this.mContext = mContext;
        this.arrayList = arrayList;
        this.onViewClick = onViewClick;
    }

    boolean clickflag = true;

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        TextView popular_session_name_txt;
        RatingBar popular_rating_bar;
        ImageView popular_Session_image_img;
        LinearLayout main_linear;

        public MyViewHolder(View view) {
            super(view);

            popular_session_name_txt = (TextView) view.findViewById(R.id.popular_session_name_txt);
            popular_rating_bar = (RatingBar) view.findViewById(R.id.popular_rating_bar);
            popular_Session_image_img = (ImageView) view.findViewById(R.id.popular_Session_image_img);
            main_linear = (LinearLayout) view.findViewById(R.id.main_linear);
        }


    }

    @Override
    public PopularClassListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.popular_list_item, parent, false);

        return new PopularClassListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PopularClassListAdapter.MyViewHolder holder, final int position) {

        holder.popular_session_name_txt.setText(arrayList.get(position).getSessionName());
        holder.popular_rating_bar.setRating(Float.parseFloat(String.valueOf(arrayList.get(position).getRatingValue())));

        holder.popular_Session_image_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    holder.main_linear.setBackgroundResource(R.drawable.linear_shape);
                    SessionDetail = new ArrayList<>();
                    SessionDetail.add(arrayList.get(position).getSessionID());
                    onViewClick.getViewClick();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public ArrayList<String> getSessionDetail() {
        return SessionDetail;
    }
}

