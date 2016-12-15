package com.congnt.emergencyassistance.entity;

/**
 * Created by congnt24 on 12/12/2016.
 */

public class ItemAudio {
    public String url;
    public String name;
    public boolean isPlay = false;

    public ItemAudio(String url) {
        this.url = url;
    }

    public ItemAudio(String url, String name) {
        this.url = url;
        this.name = name;
    }
}
