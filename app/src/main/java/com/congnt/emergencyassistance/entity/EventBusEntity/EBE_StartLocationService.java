package com.congnt.emergencyassistance.entity.EventBusEntity;

/**
 * Created by congnt24 on 03/10/2016.
 */

public class EBE_StartLocationService extends EBE_StartBase<Boolean> {
    public EBE_StartLocationService(Boolean aBoolean) {
        super(aBoolean);
    }
    public Boolean isForeground = false;

    public EBE_StartLocationService(Boolean value, Boolean isForeground) {
        super(value);
        this.isForeground = isForeground;
    }
}
