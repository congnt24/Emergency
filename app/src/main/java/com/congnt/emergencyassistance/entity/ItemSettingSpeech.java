package com.congnt.emergencyassistance.entity;

import com.congnt.emergencyassistance.EmergencyType;

/**
 * Created by congnt24 on 12/10/2016.
 */

public class ItemSettingSpeech {
    public String command;
    public EmergencyType emergencyType;

    public ItemSettingSpeech(String command, EmergencyType emergencyType) {
        this.command = command;
        this.emergencyType = emergencyType;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public EmergencyType getEmergencyType() {
        return emergencyType;
    }

    public void setEmergencyType(EmergencyType emergencyType) {
        this.emergencyType = emergencyType;
    }
}
