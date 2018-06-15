package com.adms.classsafari.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class SessionConfirmationDetailModel implements Parcelable {

    public static final Parcelable.Creator<SessionConfirmationDetailModel> CREATOR = new Parcelable.Creator<SessionConfirmationDetailModel>() {
        public SessionConfirmationDetailModel createFromParcel(Parcel in) {
            return new SessionConfirmationDetailModel(in);
        }

        public SessionConfirmationDetailModel[] newArray(int size) {
            return new SessionConfirmationDetailModel[size];
        }
    };
    //    String ;
    String sessionName, sessionId;
    String rating;
    String ratingValue;
    String teacherName;
    String region;
    String startdate;
    String enddate;
    String schdule;
    String duration;
    String price;
    String dateTime;
    String back, location, wheretoComeStr, SearchBy, board, stream, standard, lessionName, searchType, firsttimesearch, searchfront, gender;


    String froncontanct;

    public SessionConfirmationDetailModel() {

    }

    // example constructor that takes a Parcel and gives you an object populated with it's values
    public SessionConfirmationDetailModel(Parcel in) {
        sessionName = in.readString();
        rating = in.readString();
        ratingValue = in.readString();
        teacherName = in.readString();
        location = in.readString();
        startdate = in.readString();
        enddate = in.readString();
        schdule = in.readString();
        duration = in.readString();
        price = in.readString();
        froncontanct = in.readString();
        dateTime = in.readString();
        back = in.readString();
        region = in.readString();
        wheretoComeStr = in.readString();
        sessionId = in.readString();
        SearchBy = in.readString();
        board = in.readString();
        stream = in.readString();
        standard = in.readString();
        lessionName = in.readString();
        searchType = in.readString();
        firsttimesearch = in.readString();
        searchfront = in.readString();
        gender = in.readString();
    }

    public static Creator<SessionConfirmationDetailModel> getCREATOR() {
        return CREATOR;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSearchBy() {
        return SearchBy;
    }

    public void setSearchBy(String searchBy) {
        SearchBy = searchBy;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getLessionName() {
        return lessionName;
    }

    public void setLessionName(String lessionName) {
        this.lessionName = lessionName;
    }

    public String getSearchfront() {
        return searchfront;
    }

    public void setSearchfront(String searchfront) {
        this.searchfront = searchfront;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getFirsttimesearch() {
        return firsttimesearch;
    }

    public void setFirsttimesearch(String firsttimesearch) {
        this.firsttimesearch = firsttimesearch;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }

    public String getWheretoComeStr() {
        return wheretoComeStr;
    }

    public void setWheretoComeStr(String wheretoComeStr) {
        this.wheretoComeStr = wheretoComeStr;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(String ratingValue) {
        this.ratingValue = ratingValue;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getSchdule() {
        return schdule;
    }

    public void setSchdule(String schdule) {
        this.schdule = schdule;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFroncontanct() {
        return froncontanct;
    }

    public void setFroncontanct(String froncontanct) {
        this.froncontanct = froncontanct;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(sessionName);
        parcel.writeString(rating);
        parcel.writeString(ratingValue);
        parcel.writeString(teacherName);
        parcel.writeString(location);
        parcel.writeString(startdate);
        parcel.writeString(enddate);
        parcel.writeString(schdule);
        parcel.writeString(duration);
        parcel.writeString(price);
        parcel.writeString(froncontanct);
        parcel.writeString(dateTime);
        parcel.writeString(back);
        parcel.writeString(region);
        parcel.writeString(wheretoComeStr);
        parcel.writeString(sessionId);
        parcel.writeString(SearchBy);
        parcel.writeString(board);
        parcel.writeString(stream);
        parcel.writeString(standard);
        parcel.writeString(lessionName);
        parcel.writeString(searchType);
        parcel.writeString(firsttimesearch);
        parcel.writeString(searchfront);
        parcel.writeString(gender);

    }
}
