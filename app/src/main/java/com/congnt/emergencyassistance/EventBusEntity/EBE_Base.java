package com.congnt.emergencyassistance.EventBusEntity;

/**
 * Created by congnt24 on 01/10/2016.
 */

public abstract class EBE_Base {
    public boolean aBoolean;
    public int anInt;
    public String aString;
    public float aFloat;

    public EBE_Base(boolean aBoolean) {
        this.aBoolean = aBoolean;
    }

    public EBE_Base(int anInt) {

        this.anInt = anInt;
    }

    public EBE_Base(String aString) {

        this.aString = aString;
    }

    public EBE_Base(float aFloat) {

        this.aFloat = aFloat;
    }

    public EBE_Base(boolean aBoolean, int anInt, String aString, float aFloat) {
        this.aBoolean = aBoolean;
        this.anInt = anInt;
        this.aString = aString;
        this.aFloat = aFloat;
    }
}
