package com.adms.classsafari.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClassDetailModel {
    @SerializedName("Session_ID")
    @Expose
    private String sessionID;
    @SerializedName("SessionTypeID")
    @Expose
    private Integer sessionTypeID;
    @SerializedName("SessionName")
    @Expose
    private String sessionName;
    @SerializedName("Board")
    @Expose
    private String board;
    @SerializedName("Standard")
    @Expose
    private String standard;
    @SerializedName("Stream")
    @Expose
    private String stream;
    @SerializedName("LessionTypeName")
    @Expose
    private String lessionTypeName;
    @SerializedName("AddressLine1")
    @Expose
    private String addressLine1;
    @SerializedName("AddressLine2")
    @Expose
    private String addressLine2;
    @SerializedName("RegionName")
    @Expose
    private String regionName;
    @SerializedName("AddressCity")
    @Expose
    private String addressCity;
    @SerializedName("AddressState")
    @Expose
    private String addressState;
    @SerializedName("AddressZipCode")
    @Expose
    private String addressZipCode;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("SessionAmount")
    @Expose
    private String sessionAmount;
    @SerializedName("SessionCapacity")
    @Expose
    private String sessionCapacity;
    @SerializedName("AlertTime")
    @Expose
    private String alertTime;
    @SerializedName("StartDate")
    @Expose
    private String startDate;
    @SerializedName("EndDate")
    @Expose
    private String endDate;
    @SerializedName("Schedule")
    @Expose
    private String schedule;
    @SerializedName("DateTime")
    @Expose
    private String dateTime;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public Integer getSessionTypeID() {
        return sessionTypeID;
    }

    public void setSessionTypeID(Integer sessionTypeID) {
        this.sessionTypeID = sessionTypeID;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
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

    public String getLessionTypeName() {
        return lessionTypeName;
    }

    public void setLessionTypeName(String lessionTypeName) {
        this.lessionTypeName = lessionTypeName;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public String getAddressState() {
        return addressState;
    }

    public void setAddressState(String addressState) {
        this.addressState = addressState;
    }

    public String getAddressZipCode() {
        return addressZipCode;
    }

    public void setAddressZipCode(String addressZipCode) {
        this.addressZipCode = addressZipCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSessionAmount() {
        return sessionAmount;
    }

    public void setSessionAmount(String sessionAmount) {
        this.sessionAmount = sessionAmount;
    }

    public String getSessionCapacity() {
        return sessionCapacity;
    }

    public void setSessionCapacity(String sessionCapacity) {
        this.sessionCapacity = sessionCapacity;
    }

    public String getAlertTime() {
        return alertTime;
    }

    public void setAlertTime(String alertTime) {
        this.alertTime = alertTime;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }


    //    =====================Board API=========
    @SerializedName("Board_ID")
    @Expose
    private String boardID;
    @SerializedName("BoardName")
    @Expose
    private String boardName;
    @SerializedName("BoardAbbr")
    @Expose
    private String boardAbbr;
    @SerializedName("BoardOrder")
    @Expose
    private String boardOrder;
    @SerializedName("BoardDiscription")
    @Expose
    private String boardDiscription;
    @SerializedName("IsActive")
    @Expose
    private String isActive;
    @SerializedName("CreateDate")
    @Expose
    private String createDate;

    public String getBoardID() {
        return boardID;
    }

    public void setBoardID(String boardID) {
        this.boardID = boardID;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public String getBoardAbbr() {
        return boardAbbr;
    }

    public void setBoardAbbr(String boardAbbr) {
        this.boardAbbr = boardAbbr;
    }

    public String getBoardOrder() {
        return boardOrder;
    }

    public void setBoardOrder(String boardOrder) {
        this.boardOrder = boardOrder;
    }

    public String getBoardDiscription() {
        return boardDiscription;
    }

    public void setBoardDiscription(String boardDiscription) {
        this.boardDiscription = boardDiscription;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
//    =======================================

    //    ================Standanrd API===================
    @SerializedName("Standard_ID")
    @Expose
    private String standardID;
    @SerializedName("StandardName")
    @Expose
    private String standardName;
    @SerializedName("StandardAbbr")
    @Expose
    private String standardAbbr;
    @SerializedName("StandardOrder")
    @Expose
    private String standardOrder;
    @SerializedName("StandardDescription")
    @Expose
    private String standardDescription;

    public String getStandardID() {
        return standardID;
    }

    public void setStandardID(String standardID) {
        this.standardID = standardID;
    }

    public String getStandardName() {
        return standardName;
    }

    public void setStandardName(String standardName) {
        this.standardName = standardName;
    }

    public String getStandardAbbr() {
        return standardAbbr;
    }

    public void setStandardAbbr(String standardAbbr) {
        this.standardAbbr = standardAbbr;
    }

    public String getStandardOrder() {
        return standardOrder;
    }

    public void setStandardOrder(String standardOrder) {
        this.standardOrder = standardOrder;
    }

    public String getStandardDescription() {
        return standardDescription;
    }

    public void setStandardDescription(String standardDescription) {
        this.standardDescription = standardDescription;
    }

//    ================================================

    //    ==================Stream API====================
    @SerializedName("Stream_ID")
    @Expose
    private String streamID;
    @SerializedName("StreamName")
    @Expose
    private String streamName;
    @SerializedName("StreamAbbr")
    @Expose
    private String streamAbbr;
    @SerializedName("StreamOrder")
    @Expose
    private String streamOrder;
    @SerializedName("StreamDescription")
    @Expose
    private String streamDescription;

    public String getStreamID() {
        return streamID;
    }

    public void setStreamID(String streamID) {
        this.streamID = streamID;
    }

    public String getStreamName() {
        return streamName;
    }

    public void setStreamName(String streamName) {
        this.streamName = streamName;
    }

    public String getStreamAbbr() {
        return streamAbbr;
    }

    public void setStreamAbbr(String streamAbbr) {
        this.streamAbbr = streamAbbr;
    }

    public String getStreamOrder() {
        return streamOrder;
    }

    public void setStreamOrder(String streamOrder) {
        this.streamOrder = streamOrder;
    }

    public String getStreamDescription() {
        return streamDescription;
    }

    public void setStreamDescription(String streamDescription) {
        this.streamDescription = streamDescription;
    }
//    ================================================
}
