package com.congnt.emergencyassistance.entity.firebase;

/**
 * Created by congnt24 on 23/10/2016.
 */

public class User {
    public String email;
    public String name;
    public String date;
    public String address;
    public String gender;
    public String phone;
    public String photoUrl;
    public User() {
    }
    public User(String name, String date, String address, String gender, String phone) {
        this.name = name;
        this.date = date;
        this.address = address;
        this.gender = gender;
        this.phone = phone;
    }

    public User(String name, String date, String address, String gender, String phone, String photoUrl) {
        this.name = name;
        this.date = date;
        this.address = address;
        this.gender = gender;
        this.phone = phone;
        this.photoUrl = photoUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
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

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", address='" + address + '\'' +
                ", gender='" + gender + '\'' +
                ", phone='" + phone + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                '}';
    }
}
