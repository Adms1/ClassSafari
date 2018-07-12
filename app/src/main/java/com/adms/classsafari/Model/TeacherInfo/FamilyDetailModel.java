package com.adms.classsafari.Model.TeacherInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FamilyDetailModel {
    @SerializedName("Family_ID")
    @Expose
    private String familyID;
    @SerializedName("AreSeparated")
    @Expose
    private String areSeparated;
    @SerializedName("FamilyCreateDate")
    @Expose
    private String familyCreateDate;
    @SerializedName("Status_ID")
    @Expose
    private String statusID;
    @SerializedName("Contact_ID")
    @Expose
    private String contactID;
    @SerializedName("FamilyContact_ID")
    @Expose
    private String familyContactID;
    @SerializedName("ContactTypeName")
    @Expose
    private String contactTypeName;
    @SerializedName("FirstName")
    @Expose
    private String firstName;
    @SerializedName("LastName")
    @Expose
    private String lastName;
    @SerializedName("EmailAddress")
    @Expose
    private String emailAddress;
    @SerializedName("Gender_ID")
    @Expose
    private String genderID;
    @SerializedName("DateofBirth")
    @Expose
    private String dateofBirth;
    @SerializedName("ContactPhoneNumber")
    @Expose
    private String contactPhoneNumber;
    @SerializedName("FamilyContactCreateDate")
    @Expose
    private String familyContactCreateDate;
    @SerializedName("FamilyContact")
    @Expose
    private List<ChildDetailModel> familyContact = new ArrayList<ChildDetailModel>();

    //    ================PAyment Report====================
    @SerializedName("PaymentAmount")
    @Expose
    private String paymentAmount;
    @SerializedName("PaymentDate")
    @Expose
    private String paymentDate;
    @SerializedName("TrackAndPayPayment_ID")
    @Expose
    private String trackAndPayPaymentID;
    @SerializedName("Order_ID")
    @Expose
    private String orderID;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("SessionName")
    @Expose
    private String sessionName;
    @SerializedName("PaymentStatus")
    @Expose
    private String paymentStatus;

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getTrackAndPayPaymentID() {
        return trackAndPayPaymentID;
    }

    public void setTrackAndPayPaymentID(String trackAndPayPaymentID) {
        this.trackAndPayPaymentID = trackAndPayPaymentID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    //    ===========================
//==============ClassType========================
    @SerializedName("ContactType_ID")
    @Expose
    private String contactTypeID;
    @SerializedName("ContactTypeOrder")
    @Expose
    private String contactTypeOrder;


    public String getContactTypeID() {
        return contactTypeID;
    }

    public void setContactTypeID(String contactTypeID) {
        this.contactTypeID = contactTypeID;
    }

    public String getContactTypeOrder() {
        return contactTypeOrder;
    }

    public void setContactTypeOrder(String contactTypeOrder) {
        this.contactTypeOrder = contactTypeOrder;
    }

    //    ===========================
    public String getFamilyID() {
        return familyID;
    }

    public void setFamilyID(String familyID) {
        this.familyID = familyID;
    }

    public String getAreSeparated() {
        return areSeparated;
    }

    public void setAreSeparated(String areSeparated) {
        this.areSeparated = areSeparated;
    }

    public String getFamilyCreateDate() {
        return familyCreateDate;
    }

    public void setFamilyCreateDate(String familyCreateDate) {
        this.familyCreateDate = familyCreateDate;
    }

    public String getStatusID() {
        return statusID;
    }

    public void setStatusID(String statusID) {
        this.statusID = statusID;
    }

    public String getContactID() {
        return contactID;
    }

    public void setContactID(String contactID) {
        this.contactID = contactID;
    }

    public String getFamilyContactID() {
        return familyContactID;
    }

    public void setFamilyContactID(String familyContactID) {
        this.familyContactID = familyContactID;
    }

    public String getContactTypeName() {
        return contactTypeName;
    }

    public void setContactTypeName(String contactTypeName) {
        this.contactTypeName = contactTypeName;
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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getGenderID() {
        return genderID;
    }

    public void setGenderID(String genderID) {
        this.genderID = genderID;
    }

    public String getDateofBirth() {
        return dateofBirth;
    }

    public void setDateofBirth(String dateofBirth) {
        this.dateofBirth = dateofBirth;
    }

    public String getContactPhoneNumber() {
        return contactPhoneNumber;
    }

    public void setContactPhoneNumber(String contactPhoneNumber) {
        this.contactPhoneNumber = contactPhoneNumber;
    }

    public String getFamilyContactCreateDate() {
        return familyContactCreateDate;
    }

    public void setFamilyContactCreateDate(String familyContactCreateDate) {
        this.familyContactCreateDate = familyContactCreateDate;
    }

    public List<ChildDetailModel> getFamilyContact() {
        return familyContact;
    }

    public void setFamilyContact(List<ChildDetailModel> familyContact) {
        this.familyContact = familyContact;
    }

    @SerializedName("ClassType_ID")
    @Expose
    private String classTypeID;
    @SerializedName("ClassTypeName")
    @Expose
    private String classTypeName;
    @SerializedName("IsActive")
    @Expose
    private String isActive;
    @SerializedName("CreateDate")
    @Expose
    private String createDate;

    public String getClassTypeID() {
        return classTypeID;
    }

    public void setClassTypeID(String classTypeID) {
        this.classTypeID = classTypeID;
    }

    public String getClassTypeName() {
        return classTypeName;
    }

    public void setClassTypeName(String classTypeName) {
        this.classTypeName = classTypeName;
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

    //===============TeacherDetail===========
    @SerializedName("Mobile")
    @Expose
    private String mobile;
    @SerializedName("DOB")
    @Expose
    private String dOB;
    @SerializedName("EmailID")
    @Expose
    private String emailID;
    @SerializedName("ClassName")
    @Expose
    private String className;
    @SerializedName("Gender")
    @Expose
    private Integer gender;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDOB() {
        return dOB;
    }

    public void setDOB(String dOB) {
        this.dOB = dOB;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

//    ===============Bank Detail=========

    @SerializedName("Coach_ID")
    @Expose
    private Integer coachID;
    @SerializedName("BankName")
    @Expose
    private String bankName;
    @SerializedName("NameonAccount")
    @Expose
    private String nameonAccount;
    @SerializedName("TypeofAccount")
    @Expose
    private Integer typeofAccount;
    @SerializedName("AccountNumber")
    @Expose
    private String accountNumber;
    @SerializedName("IFCCode")
    @Expose
    private String iFCCode;

    public Integer getCoachID() {
        return coachID;
    }

    public void setCoachID(Integer coachID) {
        this.coachID = coachID;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getNameonAccount() {
        return nameonAccount;
    }

    public void setNameonAccount(String nameonAccount) {
        this.nameonAccount = nameonAccount;
    }

    public Integer getTypeofAccount() {
        return typeofAccount;
    }

    public void setTypeofAccount(Integer typeofAccount) {
        this.typeofAccount = typeofAccount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getIFCCode() {
        return iFCCode;
    }

    public void setIFCCode(String iFCCode) {
        this.iFCCode = iFCCode;
    }

}
