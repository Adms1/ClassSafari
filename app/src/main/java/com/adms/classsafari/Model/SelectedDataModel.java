package com.adms.classsafari.Model;

import android.os.Parcel;
import android.os.Parcelable;


public class SelectedDataModel implements Parcelable {

    public static final Parcelable.Creator<SelectedDataModel> CREATOR = new Parcelable.Creator<SelectedDataModel>() {
        public SelectedDataModel createFromParcel(Parcel in) {
            return new SelectedDataModel(in);
        }

        public SelectedDataModel[] newArray(int size) {
            return new SelectedDataModel[size];
        }
    };
//    String ;
    String sessionName, frontLogin, city, sessionTypeflag,
            withOR, SearchBy, sessionType, ratingLogin,
            searchfront, froncontanct, wheretocometype,
            familyNameStr, familyID, flag, sessionID,
        lessionName,board,standard,stream,gender;

    public SelectedDataModel() {

    }

    // example constructor that takes a Parcel and gives you an object populated with it's values
    public SelectedDataModel(Parcel in) {
        sessionName = in.readString();
        city = in.readString();
        frontLogin = in.readString();
        sessionTypeflag = in.readString();
        withOR = in.readString();
        SearchBy = in.readString();
        sessionType = in.readString();
        ratingLogin = in.readString();
        searchfront = in.readString();
        froncontanct = in.readString();
        wheretocometype = in.readString();
        familyNameStr = in.readString();
        familyID = in.readString();
        flag = in.readString();
        sessionID = in.readString();
        lessionName=in.readString();
        board=in.readString();
        standard=in.readString();
        stream=in.readString();
        gender=in.readString();
    }

    public static Creator<SelectedDataModel> getCREATOR() {
        return CREATOR;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLessionName() {
        return lessionName;
    }

    public void setLessionName(String lessionName) {
        this.lessionName = lessionName;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFrontLogin() {
        return frontLogin;
    }

    public void setFrontLogin(String frontLogin) {
        this.frontLogin = frontLogin;
    }

    public String getSessionTypeflag() {
        return sessionTypeflag;
    }

    public void setSessionTypeflag(String sessionTypeflag) {
        this.sessionTypeflag = sessionTypeflag;
    }

    public String getWithOR() {
        return withOR;
    }

    public void setWithOR(String withOR) {
        this.withOR = withOR;
    }

    public String getSearchBy() {
        return SearchBy;
    }

    public void setSearchBy(String searchBy) {
        SearchBy = searchBy;
    }

    public String getSessionType() {
        return sessionType;
    }

    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }

    public String getRatingLogin() {
        return ratingLogin;
    }

    public void setRatingLogin(String ratingLogin) {
        this.ratingLogin = ratingLogin;
    }

    public String getSearchfront() {
        return searchfront;
    }

    public void setSearchfront(String searchfront) {
        this.searchfront = searchfront;
    }

    public String getFroncontanct() {
        return froncontanct;
    }

    public void setFroncontanct(String froncontanct) {
        this.froncontanct = froncontanct;
    }

    public String getWheretocometype() {
        return wheretocometype;
    }

    public void setWheretocometype(String wheretocometype) {
        this.wheretocometype = wheretocometype;
    }

    public String getFamilyNameStr() {
        return familyNameStr;
    }

    public void setFamilyNameStr(String familyNameStr) {
        this.familyNameStr = familyNameStr;
    }

    public String getFamilyID() {
        return familyID;
    }

    public void setFamilyID(String familyID) {
        this.familyID = familyID;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(sessionName);
        parcel.writeString(city);
        parcel.writeString(frontLogin);
        parcel.writeString(sessionTypeflag);
        parcel.writeString(withOR);
        parcel.writeString(SearchBy);
        parcel.writeString(sessionType);
        parcel.writeString(ratingLogin);
        parcel.writeString(searchfront);
        parcel.writeString(froncontanct);
        parcel.writeString(wheretocometype);
        parcel.writeString(familyNameStr);
        parcel.writeString(familyID);
        parcel.writeString(flag);
        parcel.writeString(sessionID);
        parcel.writeString(lessionName);
        parcel.writeString(board);
        parcel.writeString(standard);
        parcel.writeString(stream);
        parcel.writeString(gender);
    }
}
