package com.congnt.emergencyassistance.entity.firebase;

/**
 * Created by congnt24 on 23/10/2016.
 */

public class User {
    public String name;
    public String date;
    public String address;
    public String gender;
    public String phone;

    public User() {
    }

    public User(String name, String date, String address, String gender, String phone) {
        this.name = name;
        this.date = date;
        this.address = address;
        this.gender = gender;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}