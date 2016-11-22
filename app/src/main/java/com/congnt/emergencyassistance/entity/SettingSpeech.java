package com.congnt.emergencyassistance.entity;

import java.util.List;

/**
 * Created by congnt24 on 12/11/2016.
 */

public class SettingSpeech {
    public String countryCode;
    public String countryName;
    public List<Command> police;
    public List<Command> fire;
    public List<Command> ambulance;
    public List<TemplateMessage> templateMessage;

    @Override
    public String toString() {
        return "SettingSpeech{" +
                "countryCode='" + countryCode + '\'' +
                ", countryName='" + countryName + '\'' +
                ", police=" + police +
                ", fire=" + fire +
                ", ambulance=" + ambulance +
                ", templateMessage=" + templateMessage +
                '}';
    }

    public class Command {
        public String command;
    }

    public class TemplateMessage {
        public String message;
    }
}
