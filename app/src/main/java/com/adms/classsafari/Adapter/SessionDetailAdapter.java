package com.adms.classsafari.Adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.adms.classsafari.AppConstant.Utils;
import com.adms.classsafari.Interface.onChlidClick;
import com.adms.classsafari.Interface.onViewClick;
import com.adms.classsafari.Model.Session.sessionDataModel;
import com.adms.classsafari.R;
import com.adms.classsafari.databinding.ListItemReviewsBinding;
import com.adms.classsafari.databinding.SessionCardLayout1Binding;
import com.adms.classsafari.databinding.SessionDescriptionItemBinding;
import com.adms.classsafari.databinding.SessionReviewCardLayout1Binding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admsandroid on 3/5/2018.
 */

public class SessionDetailAdapter extends RecyclerView.Adapter {

    private final static int HEADER_VIEW = 0;
    private final static int ROW_VIEW = 1;
    private final static int CONTENT_VIEW = 2;
    //    private final static int DESCRIPTION_ROW_VIEW = 1;
//    private final static int DESCRIPTION_VIEW = 1;
    SessionCardLayout1Binding sessionCardLayout1Binding;
    SessionDescriptionItemBinding descriptionItemBinding;
    ListItemReviewsBinding itemReviewsBinding;
    SessionReviewCardLayout1Binding reviewCardLayout1Binding;

    List<sessionDataModel> arrayList, sessionRatingList;
    String address, ratinguserStr;
    onViewClick onViewClick;
    ArrayList<String> reviewarray;
    ArrayList<String> descriptionviewarray;
    private Context mContext;
    private ArrayList<String> SessionDetail;
    private ArrayList<String> CoachId;
    onChlidClick onChlidClick;


    public SessionDetailAdapter(Context mContext, List<sessionDataModel> arrayList,
                                ArrayList<String> descriptionviewarray,
                                ArrayList<String> reviewarray,
                                List<sessionDataModel> sessionRatingList,
                                String ratinguserStr, onChlidClick onChlidClick, onViewClick onViewClick) {
        this.mContext = mContext;
        this.arrayList = arrayList;
        this.onViewClick = onViewClick;
        this.onChlidClick=onChlidClick;
        this.sessionRatingList = sessionRatingList;
        this.reviewarray = reviewarray;
        this.descriptionviewarray = descriptionviewarray;
        this.ratinguserStr = ratinguserStr;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == HEADER_VIEW) {

            sessionCardLayout1Binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.session_card_layout_1, parent, false);
            view = sessionCardLayout1Binding.getRoot();
            return new SessionCard(view);
        }
//        if (viewType == DESCRIPTION_VIEW) {
//            descriptionItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.session_description_item, parent, false);
//            view = descriptionItemBinding.getRoot();
//            return new DescriptionView(view);
//        }
        if (viewType == ROW_VIEW) {
            itemReviewsBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_reviews, parent, false);
            view = itemReviewsBinding.getRoot();
            return new RowView(view);
        }
        if (viewType == CONTENT_VIEW) {
            reviewCardLayout1Binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.session_review_card_layout_1, parent, false);
            view = reviewCardLayout1Binding.getRoot();
            return new ReviewCard(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof SessionCard) {
            final SessionCard headerHolder = (SessionCard) holder;
            sessionCardLayout1Binding.sessionNameTxt.setText(arrayList.get(position).getSessionName());
            sessionCardLayout1Binding.sessionRatingbar.setRating(Float.parseFloat(arrayList.get(position).getRating()));
            if (arrayList.get(position).getCoachTypeID().equalsIgnoreCase("1")) {
                sessionCardLayout1Binding.linearBoard.setVisibility(View.VISIBLE);
                sessionCardLayout1Binding.linearStandard.setVisibility(View.VISIBLE);
                sessionCardLayout1Binding.linearStream.setVisibility(View.VISIBLE);
                sessionCardLayout1Binding.sessionTypeTxt.setText("Academic");
            } else {
                sessionCardLayout1Binding.linearBoard.setVisibility(View.GONE);
                sessionCardLayout1Binding.linearStandard.setVisibility(View.GONE);
                sessionCardLayout1Binding.linearStream.setVisibility(View.GONE);
                sessionCardLayout1Binding.sessionTypeTxt.setText("Sports");
            }
            address = arrayList.get(position).getAddressLine1() +
                    "," + arrayList.get(position).getAddressLine2() +
                    "," + arrayList.get(position).getRegionName() +
                    "," + arrayList.get(position).getAddressCity() +
                    "," + arrayList.get(position).getAddressState() +
                    "-" + arrayList.get(position).getAddressZipCode();
            sessionCardLayout1Binding.sessionDetailNameTxt.setText(arrayList.get(position).getLessionTypeName());
            sessionCardLayout1Binding.sessionBoardTxt.setText(arrayList.get(position).getBoard());
            sessionCardLayout1Binding.sessionStandardTxt.setText(arrayList.get(position).getStandard());
            sessionCardLayout1Binding.sessionStreamTxt.setText(arrayList.get(position).getStream());
            sessionCardLayout1Binding.sessionTotalStudentTxt.setText(arrayList.get(position).getSessionCapacity());
            if (arrayList.get(position).getAddressLine2().equalsIgnoreCase("")) {
                sessionCardLayout1Binding.addressTxt.setText(Html.fromHtml(arrayList.get(position).getAddressLine1()
                        + "<br>" + arrayList.get(position).getRegionName() +
                        ", " + arrayList.get(position).getAddressCity() + "<br>" +
                        arrayList.get(position).getAddressState() + " " + arrayList.get(position).getAddressZipCode()));
            } else {
                sessionCardLayout1Binding.addressTxt.setText(Html.fromHtml(arrayList.get(position).getAddressLine1() + "<br>" +
                        arrayList.get(position).getAddressLine2() + "<br>" + arrayList.get(position).getRegionName() +
                        ", " + arrayList.get(position).getAddressCity() + "<br>" +
                        arrayList.get(position).getAddressState() + " " + arrayList.get(position).getAddressZipCode()));
            }
            sessionCardLayout1Binding.emailTxt.setText(arrayList.get(position).getEmailAddress());
            sessionCardLayout1Binding.phoneTxt.setText(arrayList.get(position).getContactPhoneNumber());
            sessionCardLayout1Binding.teacherNameTxt.setText(arrayList.get(position).getName());
            if (arrayList.get(position).getDescription().equalsIgnoreCase("")) {
                sessionCardLayout1Binding.descriptionTxt.setVisibility(View.GONE);
                sessionCardLayout1Binding.descriptionTxtView.setVisibility(View.GONE);
            } else {
                sessionCardLayout1Binding.descriptionTxt.setVisibility(View.VISIBLE);
                sessionCardLayout1Binding.descriptionTxtView.setVisibility(View.VISIBLE);
                sessionCardLayout1Binding.descriptionTxt.setText(arrayList.get(position).getDescription());
            }
            if (ratinguserStr.equalsIgnoreCase("0")) {
                sessionCardLayout1Binding.ratingTxt.setVisibility(View.GONE);
            } else {
                sessionCardLayout1Binding.ratingTxt.setVisibility(View.VISIBLE);
                sessionCardLayout1Binding.ratingTxt.setText("(" + ratinguserStr + ")");
            }
            if (arrayList.get(position).getCoachTypeID().equalsIgnoreCase("1")) {
                sessionCardLayout1Binding.sessionImage.setImageResource(R.drawable.physics_img);
            } else {
                sessionCardLayout1Binding.sessionImage.setImageResource(R.drawable.all_sports_image);
            }

            sessionCardLayout1Binding.phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Utils.checkAndRequestPermissions(mContext)) {
                    }
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.fromParts("tel", sessionCardLayout1Binding.phoneTxt.getText().toString(), null));
                    mContext.startActivity(intent);
                }
            });
            sessionCardLayout1Binding.addressLoc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    mContext.startActivity(mapIntent);
                }
            });
            sessionCardLayout1Binding.emailImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    String[] recipients = {arrayList.get(position).getEmailAddress()};
                    intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                    intent.setType("text/html");
                    intent.setPackage("com.google.android.gm");
                    mContext.startActivity(Intent.createChooser(intent, "Send mail"));
                }
            });
            sessionCardLayout1Binding.phoneTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Utils.checkAndRequestPermissions(mContext)) {
                    }
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.fromParts("tel", sessionCardLayout1Binding.phoneTxt.getText().toString(), null));
                    mContext.startActivity(intent);
                }
            });
            sessionCardLayout1Binding.addressTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    mContext.startActivity(mapIntent);
                }
            });
            sessionCardLayout1Binding.sessionRatingbar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    SessionDetail = new ArrayList<>();
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        SessionDetail.add(arrayList.get(position).getSessionName() + "|" + arrayList.get(position).getSessionID() + "|" + arrayList.get(position).getName());
                        onViewClick.getViewClick();
                    }
                    return true;
                }
            });
            sessionCardLayout1Binding.emailTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    String[] recipients = {arrayList.get(position).getEmailAddress()};
                    intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                    intent.setType("text/html");
                    intent.setPackage("com.google.android.gm");
                    mContext.startActivity(Intent.createChooser(intent, "Send mail"));
                }
            });
            sessionCardLayout1Binding.viewProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CoachId = new ArrayList<>();
                    CoachId.add(arrayList.get(position).getCoachID());
                    onChlidClick.getChilClick();
                }
            });
            sessionCardLayout1Binding.teacherNameTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CoachId = new ArrayList<>();
                    CoachId.add(arrayList.get(position).getCoachID());
                    onChlidClick.getChilClick();
                }
            });
        }
//        if (holder instanceof DescriptionView) {
//            final DescriptionView descriptionHolder = (DescriptionView) holder;
//            descriptionItemBinding.descriptionTxt.setText(descriptionviewarray.get(position - arrayList.size()));
//        }
        if (holder instanceof RowView) {

        }
        if (holder instanceof ReviewCard) {
            final ReviewCard rowHolder = (ReviewCard) holder;
            reviewCardLayout1Binding.sessionReviewsRatingBar.setRating(Float.parseFloat
                    (String.valueOf(sessionRatingList.get(position - arrayList.size() - reviewarray.size()).getRatingValue())));
            reviewCardLayout1Binding.reviewDateTxt.setText(sessionRatingList.get(position - arrayList.size() - reviewarray.size()).getCreateDate());
            reviewCardLayout1Binding.userNameTxt.setText(sessionRatingList.get(position - arrayList.size() - reviewarray.size()).getFirstName()
                    + " " + sessionRatingList.get(position - arrayList.size() - reviewarray.size()).getLastName());
            reviewCardLayout1Binding.commentTxt.setText(sessionRatingList.get(position - arrayList.size() - reviewarray.size()).getComment());// - descriptionviewarray.size()
            reviewCardLayout1Binding.ratingTxt.setText(String.valueOf("(" + sessionRatingList.get(position - arrayList.size() - reviewarray.size()).getRatingValue() + ")"));
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (position < arrayList.size()) {
            return HEADER_VIEW;
        }
//        if (position - arrayList.size() < descriptionviewarray.size()) {
//            return DESCRIPTION_VIEW;
//        }
        if (position - arrayList.size() < reviewarray.size()) {//- descriptionviewarray.size()
            return ROW_VIEW;
        }
        if (position - arrayList.size() - reviewarray.size() < sessionRatingList.size()) {//- descriptionviewarray.size()
            return CONTENT_VIEW;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return arrayList.size() + reviewarray.size() + sessionRatingList.size();// + descriptionviewarray.size()
    }

    public ArrayList<String> getSessionDetail() {
        return SessionDetail;
    }

    public ArrayList<String> getCoach() {
        return CoachId;
    }

    public class SessionCard extends RecyclerView.ViewHolder {

        public SessionCard(View view) {
            super(view);
        }
    }

    public class DescriptionView extends RecyclerView.ViewHolder {


        public DescriptionView(View itemView) {
            super(itemView);
        }
    }

    public class RowView extends RecyclerView.ViewHolder {

        public RowView(View itemView) {
            super(itemView);
        }
    }

    public class ReviewCard extends RecyclerView.ViewHolder {

        public ReviewCard(View view) {
            super(view);

        }
    }
}

