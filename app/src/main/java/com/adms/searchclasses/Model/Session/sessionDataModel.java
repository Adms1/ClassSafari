package com.adms.searchclasses.Model.Session;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Megha on 3/22/2018.
 */

public class sessionDataModel {
    // SessionDuration time

    public String mondayTimeStr = "", tuesdayTimeStr = "", weddayTimeStr = "", thursdayTimeStr = "", fridayTimeStr = "", satdayTimeStr = "", sundayTimeStr = "",
            mondayHoursStr = "", tuesdayHoursStr = "", weddayHoursStr = "", thursdayHoursStr = "", fridayHoursStr = "", satdayHoursStr = "", sundayHoursStr = "";
    int SessionHour = 0;
    Integer SessionMinit = 0;
    String SessionDurationminit = "", SessionDurationHours = "";
    private int count = 1;

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
    @SerializedName("LessonType_ID")
    @Expose
    private String lessonTypeID;
    @SerializedName("LessonTypeName")
    @Expose
    private String lessonTypeName;
    @SerializedName("LessonTypeAbbr")
    @Expose
    private String lessonTypeAbbr;
    @SerializedName("LessonTypeOrder")
    @Expose
    private String lessonTypeOrder;
    @SerializedName("LessonTypeDescription")
    @Expose
    private String lessonTypeDescription;
    @SerializedName("Region_ID")
    @Expose
    private String regionID;
    @SerializedName("RegionDescription")
    @Expose
    private String regionDescription;
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
    @SerializedName("Stream_ID")
    @Expose
    private String streamID;
    @SerializedName("StreamName")
    @Expose
    private String streamName;
    @SerializedName("Qualification")
    @Expose
    private String qualification;
    @SerializedName("StreamAbbr")
    @Expose
    private String streamAbbr;
    @SerializedName("StreamOrder")
    @Expose
    private String streamOrder;
    @SerializedName("StreamDescription")
    @Expose
    private String streamDescription;
    @SerializedName("IsActive")
    @Expose
    private String isActive;
    @SerializedName("CreateDate")
    @Expose
    private String createDate;
    @SerializedName("SessionDate")
    @Expose
    private String sessionDate;
    @SerializedName("SessionDetail")
    @Expose
    private List<SessionFullDetail> sessionFullDetails = new ArrayList<SessionFullDetail>();
    @SerializedName("Session_ID")
    @Expose
    private String sessionID;
    @SerializedName("SessionType")
    @Expose
    private String sessionType;
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
    @SerializedName("ContactEnrollment_ID")
    @Expose
    private String contactEnrollmentID;
    @SerializedName("FirstName")
    @Expose
    private String firstName;
    @SerializedName("LastName")
    @Expose
    private String lastName;
    @SerializedName("PhoneNumber")
    @Expose
    private String phoneNumber;
    //====================================
    @SerializedName("Session")
    @Expose
    private String session;
    @SerializedName("WeekDay_ID")
    @Expose
    private String weekDayID;
    @SerializedName("SessionStartDate")
    @Expose
    private String sessionStartDate;
    @SerializedName("SessionEndDate")
    @Expose
    private String sessionEndDate;
    @SerializedName("ClassTypeID")
    @Expose
    private String classTypeID;
    @SerializedName("ClassType")
    @Expose
    private String classType;
    @SerializedName("AttendanceData")
    @Expose
    private List<sessionDataModel> attendanceData = new ArrayList<sessionDataModel>();
    //    ===================Session_List========================
    @SerializedName("SessionDetail_ID")
    @Expose
    private String sessionDetailID;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("EmailAddress")
    @Expose
    private String emailAddress;
    @SerializedName("ContactPhoneNumber")
    @Expose
    private String contactPhoneNumber;
    @SerializedName("DateTime")
    @Expose
    private String dateTime;
    @SerializedName("Duration")
    @Expose
    private String duration;
    @SerializedName("Gender_ID")
    @Expose
    private String genderID;
    @SerializedName("Rating")
    @Expose
    private String rating;
    @SerializedName("CoachType_ID")
    @Expose
    private String coachTypeID;
    @SerializedName("TotalRatingUser")
    @Expose
    private String totalRatingUser;
    @SerializedName("Coach_ID")
    @Expose
    private String coachID;
    //==============attendance==========================
    @SerializedName("Contact_ID")
    @Expose
    private String contactID;
    @SerializedName("Attendance_ID")
    @Expose
    private String attendanceID;
    @SerializedName("Reason")
    @Expose
    private String reason;
    @SerializedName("CheckStatus")
    @Expose
    private String checkStatus;
    @SerializedName("CheckValue")
    @Expose
    private String checkValue;

    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("SessionPrice")
    @Expose
    private String sessionPrice;

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    //==========================SessionRatingDetail======================
    @SerializedName("RatingValue")
    @Expose
    private Double ratingValue;
    @SerializedName("Comment")
    @Expose
    private String comment;

    public String getCoachID() {
        return coachID;
    }

    public void setCoachID(String coachID) {
        this.coachID = coachID;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int i) {
        this.count = i;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getWeekDayID() {
        return weekDayID;
    }

    public void setWeekDayID(String weekDayID) {
        this.weekDayID = weekDayID;
    }


    public String getSessionStartDate() {
        return sessionStartDate;
    }

    public void setSessionStartDate(String sessionStartDate) {
        this.sessionStartDate = sessionStartDate;
    }

    public String getSessionEndDate() {
        return sessionEndDate;
    }

    public void setSessionEndDate(String sessionEndDate) {
        this.sessionEndDate = sessionEndDate;
    }

    public String getClassTypeID() {
        return classTypeID;
    }

    public void setClassTypeID(String classTypeID) {
        this.classTypeID = classTypeID;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public List<sessionDataModel> getAttendanceData() {
        return attendanceData;
    }

    public void setAttendanceData(List<sessionDataModel> attendanceData) {
        this.attendanceData = attendanceData;
    }

    public String getCoachTypeID() {
        return coachTypeID;
    }

    public void setCoachTypeID(String coachTypeID) {
        this.coachTypeID = coachTypeID;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getGenderID() {
        return genderID;
    }

    public void setGenderID(String genderID) {
        this.genderID = genderID;
    }

    public String getDuration() {
        return duration;
    }

    //    =======================================================

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getSessionDetailID() {
        return sessionDetailID;
    }

    public void setSessionDetailID(String sessionDetailID) {
        this.sessionDetailID = sessionDetailID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getContactPhoneNumber() {
        return contactPhoneNumber;
    }

    public void setContactPhoneNumber(String contactPhoneNumber) {
        this.contactPhoneNumber = contactPhoneNumber;
    }

    public String getTotalRatingUser() {
        return totalRatingUser;
    }

    public void setTotalRatingUser(String totalRatingUser) {
        this.totalRatingUser = totalRatingUser;
    }

    public String getContactID() {
        return contactID;
    }

    public void setContactID(String contactID) {
        this.contactID = contactID;
    }

    public String getAttendanceID() {
        return attendanceID;
    }

    //====================

    public void setAttendanceID(String attendanceID) {
        this.attendanceID = attendanceID;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCheckValue() {
        return checkValue;
    }

    public void setCheckValue(String checkValue) {
        this.checkValue = checkValue;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getSessionType() {
        return sessionType;
    }

    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
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

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
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

    public String getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(String sessionDate) {
        this.sessionDate = sessionDate;
    }

    public List<SessionFullDetail> getSessionFullDetails() {
        return sessionFullDetails;
    }

    public void setSessionFullDetails(List<SessionFullDetail> sessionFullDetails) {
        this.sessionFullDetails = sessionFullDetails;
    }

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

    public String getLessonTypeID() {
        return lessonTypeID;
    }

    public void setLessonTypeID(String lessonTypeID) {
        this.lessonTypeID = lessonTypeID;
    }

    public String getLessonTypeName() {
        return lessonTypeName;
    }

    public void setLessonTypeName(String lessonTypeName) {
        this.lessonTypeName = lessonTypeName;
    }

    public String getLessonTypeAbbr() {
        return lessonTypeAbbr;
    }

    public void setLessonTypeAbbr(String lessonTypeAbbr) {
        this.lessonTypeAbbr = lessonTypeAbbr;
    }

    public String getLessonTypeOrder() {
        return lessonTypeOrder;
    }

    public void setLessonTypeOrder(String lessonTypeOrder) {
        this.lessonTypeOrder = lessonTypeOrder;
    }

    public String getLessonTypeDescription() {
        return lessonTypeDescription;
    }

    public void setLessonTypeDescription(String lessonTypeDescription) {
        this.lessonTypeDescription = lessonTypeDescription;
    }

    public String getRegionID() {
        return regionID;
    }

    public void setRegionID(String regionID) {
        this.regionID = regionID;
    }

    public String getRegionDescription() {
        return regionDescription;
    }

    public void setRegionDescription(String regionDescription) {
        this.regionDescription = regionDescription;
    }

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


    //======================User Session_List =========================

    public void setStreamDescription(String streamDescription) {
        this.streamDescription = streamDescription;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
    //=================================================================

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getContactEnrollmentID() {
        return contactEnrollmentID;
    }

    public void setContactEnrollmentID(String contactEnrollmentID) {
        this.contactEnrollmentID = contactEnrollmentID;
    }

    public String getSessionPrice() {
        return sessionPrice;
    }

    public void setSessionPrice(String sessionPrice) {
        this.sessionPrice = sessionPrice;
    }
    //===================================================================

    public Double getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(Double ratingValue) {
        this.ratingValue = ratingValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setWeekDays() {
//        String data = "Mon,10:00 AM - 12:30PM|Tue,10:00 AM - 12:30PM";
        String data = schedule;
        if (schedule != null) {
            List<String> list = new ArrayList<String>(Arrays.asList(data.split("\\|")));
            for (String item : list) {
                if (item.contains("mon")) {
                    String[] split = item.substring(4).split("\\-");
                    mondayTimeStr = split[0];
                    String[] split1 = split[1].split("\\,");
                    calculateHours(split[0], split1[0]);
                    mondayHoursStr = SessionDurationHours;
                }
                if (item.contains("tue")) {
                    String[] split = item.substring(4).split("\\-");
                    tuesdayTimeStr = split[0];
                    String[] split1 = split[1].split("\\,");
                    calculateHours(split[0], split1[0]);
                    tuesdayHoursStr = SessionDurationHours;
                }
                if (item.contains("wed")) {
                    String[] split = item.substring(4).split("\\-");
                    weddayTimeStr = split[0];
                    String[] split1 = split[1].split("\\,");
                    calculateHours(split[0], split1[0]);
                    weddayHoursStr = SessionDurationHours;
                }
                if (item.contains("thu")) {
                    String[] split = item.substring(4).split("\\-");
                    thursdayTimeStr = split[0];
                    String[] split1 = split[1].split("\\,");
                    calculateHours(split[0], split1[0]);
                    thursdayHoursStr = SessionDurationHours;
                }
                if (item.contains("fri")) {
                    String[] split = item.substring(4).split("\\-");
                    fridayTimeStr = split[0];
                    String[] split1 = split[1].split("\\,");
                    calculateHours(split[0], split1[0]);
                    fridayHoursStr = SessionDurationHours;
                }
                if (item.contains("sat")) {
                    String[] split = item.substring(4).split("\\-");
                    satdayTimeStr = split[0];
                    String[] split1 = split[1].split("\\,");
                    calculateHours(split[0], split1[0]);
                    satdayHoursStr = SessionDurationHours;
                }
                if (item.contains("sun")) {
                    String[] split = item.substring(4).split("\\-");
                    sundayTimeStr = split[0];
                    String[] split1 = split[1].split("\\,");
                    calculateHours(split[0], split1[0]);
                    sundayHoursStr = SessionDurationHours;
                }
            }
        }

    }

    public void calculateHours(String time1, String time2) {
        Date date1, date2;
        int days, hours, min;
        String hourstr, minstr;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa", Locale.US);
        try {
            date1 = simpleDateFormat.parse(time1);
            date2 = simpleDateFormat.parse(time2);

            long difference = date2.getTime() - date1.getTime();
            days = (int) (difference / (1000 * 60 * 60 * 24));
            hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
            min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
            hours = (hours < 0 ? -hours : hours);
            SessionHour = hours;
            SessionMinit = min;
            Log.i("======= Hours", " :: " + hours + ":" + min);
            if (SessionMinit > 0) {
                if (SessionMinit < 10) {
                    SessionDurationHours = SessionHour + ":" + "0" + SessionMinit + " hrs";
                } else {
                    SessionDurationHours = SessionHour + ":" + SessionMinit + " hrs";
                }
            } else {
                SessionDurationHours = SessionHour + ":" + "00" + " hrs";
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int getSessionHour() {
        return SessionHour;
    }

    public void setSessionHour(int sessionHour) {
        SessionHour = sessionHour;
    }

    public Integer getSessionMinit() {
        return SessionMinit;
    }

    public void setSessionMinit(Integer sessionMinit) {
        SessionMinit = sessionMinit;
    }

    public String getSessionDurationminit() {
        return SessionDurationminit;
    }

    public void setSessionDurationminit(String sessionDurationminit) {
        SessionDurationminit = sessionDurationminit;
    }

    public String getSessionDurationHours() {
        return SessionDurationHours;
    }

    public void setSessionDurationHours(String sessionDurationHours) {
        SessionDurationHours = sessionDurationHours;
    }

    public String getMondayTimeStr() {
        return mondayTimeStr;
    }

    public void setMondayTimeStr(String mondayTimeStr) {
        this.mondayTimeStr = mondayTimeStr;
    }

    public String getTuesdayTimeStr() {
        return tuesdayTimeStr;
    }

    public void setTuesdayTimeStr(String tuesdayTimeStr) {
        this.tuesdayTimeStr = tuesdayTimeStr;
    }

    public String getWeddayTimeStr() {
        return weddayTimeStr;
    }

    public void setWeddayTimeStr(String weddayTimeStr) {
        this.weddayTimeStr = weddayTimeStr;
    }

    public String getThursdayTimeStr() {
        return thursdayTimeStr;
    }

    public void setThursdayTimeStr(String thursdayTimeStr) {
        this.thursdayTimeStr = thursdayTimeStr;
    }

    public String getFridayTimeStr() {
        return fridayTimeStr;
    }

    public void setFridayTimeStr(String fridayTimeStr) {
        this.fridayTimeStr = fridayTimeStr;
    }

    public String getSatdayTimeStr() {
        return satdayTimeStr;
    }

    public void setSatdayTimeStr(String satdayTimeStr) {
        this.satdayTimeStr = satdayTimeStr;
    }

    public String getSundayTimeStr() {
        return sundayTimeStr;
    }

    public void setSundayTimeStr(String sundayTimeStr) {
        this.sundayTimeStr = sundayTimeStr;
    }

    public String getMondayHoursStr() {
        return mondayHoursStr;
    }

    public void setMondayHoursStr(String mondayHoursStr) {
        this.mondayHoursStr = mondayHoursStr;
    }

    public String getTuesdayHoursStr() {
        return tuesdayHoursStr;
    }

    public void setTuesdayHoursStr(String tuesdayHoursStr) {
        this.tuesdayHoursStr = tuesdayHoursStr;
    }

    public String getWeddayHoursStr() {
        return weddayHoursStr;
    }

    public void setWeddayHoursStr(String weddayHoursStr) {
        this.weddayHoursStr = weddayHoursStr;
    }

    public String getThursdayHoursStr() {
        return thursdayHoursStr;
    }

    public void setThursdayHoursStr(String thursdayHoursStr) {
        this.thursdayHoursStr = thursdayHoursStr;
    }

    public String getFridayHoursStr() {
        return fridayHoursStr;
    }

    public void setFridayHoursStr(String fridayHoursStr) {
        this.fridayHoursStr = fridayHoursStr;
    }

    public String getSatdayHoursStr() {
        return satdayHoursStr;
    }

    public void setSatdayHoursStr(String satdayHoursStr) {
        this.satdayHoursStr = satdayHoursStr;
    }

    public String getSundayHoursStr() {
        return sundayHoursStr;
    }

    public void setSundayHoursStr(String sundayHoursStr) {
        this.sundayHoursStr = sundayHoursStr;
    }


    //Upcoming classes
    @SerializedName("StudentName")
    @Expose
    private String studentName;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("Time")
    @Expose
    private String time;
    @SerializedName("TeacherName")
    @Expose
    private String teacherName;
    @SerializedName("Subject")
    @Expose
    private String subject;
    @SerializedName("ClassName")
    @Expose
    private String className;
    @SerializedName("Mobile")
    @Expose
    private String mobile;
    @SerializedName("EmailID")
    @Expose
    private String emailID;

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }
}