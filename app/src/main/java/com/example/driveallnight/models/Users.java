package com.example.driveallnight.models;

public class Users
{
    public String email, fullName, contactNumber;
    long time;

    public Users(String email, String fullName, String contactNumber, long time)
    {
        this.email = email;
        this.fullName = fullName;
        this.contactNumber = contactNumber;
        this.time = time;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public long getTime() {
        return time;
    }

    public void setPassword(long time) {
        this.time = time;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}
