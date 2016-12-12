package com.congnt.emergencyassistance.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by congnt24 on 20/10/2016.
 */

public class ItemHistory implements Parcelable {
    public static final Parcelable.Creator<ItemHistory> CREATOR = new Parcelable.Creator<ItemHistory>() {
        @Override
        public ItemHistory createFromParcel(Parcel source) {
            return new ItemHistory(source);
        }

        @Override
        public ItemHistory[] newArray(int size) {
            return new ItemHistory[size];
        }
    };
    private String time;    //12h00 14/11/2016
    private String location;    //ha noi
    private String command; //help me
    private String type;    //speech or volumn
    private String communication;   //call or sms
    private String frontCamImagePath;   //EmergencyAssistance/IMG_ddMMMyyyy.jpg
    private String rearCamImagePath;   //EmergencyAssistance/IMG_ddMMMyyyy.jpg
    private String audio;   //EmergencyAssistance/IMG_ddMMMyyyy.jpg
    private List<LatLng> listLocation;

    public ItemHistory() {
    }

    public ItemHistory(String time, String location, String type) {
        this.time = time;
        this.location = location;
        this.type = type;
    }

    protected ItemHistory(Parcel in) {
        this.time = in.readString();
        this.location = in.readString();
        this.command = in.readString();
        this.type = in.readString();
        this.communication = in.readString();
        this.frontCamImagePath = in.readString();
        this.rearCamImagePath = in.readString();
        this.audio = in.readString();
        this.listLocation = in.createTypedArrayList(LatLng.CREATOR);
    }

    public List<LatLng> getListLocation() {
        return listLocation;
    }

    public void setListLocation(List<LatLng> listLocation) {
        this.listLocation = listLocation;
    }

    public String getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }

    public String getCommand() {
        return command;
    }

    public String getType() {
        return type;
    }

    public String getCommunication() {
        return communication;
    }

    public String getFrontCamImagePath() {
        return frontCamImagePath;
    }

    public String getRearCamImagePath() {
        return rearCamImagePath;
    }

    public String getAudio() {
        return audio;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.time);
        dest.writeString(this.location);
        dest.writeString(this.command);
        dest.writeString(this.type);
        dest.writeString(this.communication);
        dest.writeString(this.frontCamImagePath);
        dest.writeString(this.rearCamImagePath);
        dest.writeString(this.audio);
        dest.writeTypedList(this.listLocation);
    }
}
