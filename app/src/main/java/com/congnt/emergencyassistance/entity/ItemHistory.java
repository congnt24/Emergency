package com.congnt.emergencyassistance.entity;

/**
 * Created by congnt24 on 20/10/2016.
 */

public class ItemHistory {
    private String time;    //12h00 14/11/2016
    private String location;    //ha noi
    private String command; //help me
    private String type;    //speech or volumn
    private String communication;   //call or sms
    private String frontCamImagePath;   //EmergencyAssistance/IMG_ddMMMyyyy.jpg
    private String rearCamImagePath;   //EmergencyAssistance/IMG_ddMMMyyyy.jpg
    private String audio;   //EmergencyAssistance/IMG_ddMMMyyyy.jpg

    public ItemHistory() {
    }

    public ItemHistory(String time, String location, String type) {
        this.time = time;
        this.location = location;
        this.type = type;
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
}
