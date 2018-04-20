package com.adms.classsafari.Model.TeacherInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChildDetailModel {
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
}
