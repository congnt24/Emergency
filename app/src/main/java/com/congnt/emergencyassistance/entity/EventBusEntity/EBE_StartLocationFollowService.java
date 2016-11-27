package com.congnt.emergencyassistance.entity.EventBusEntity;

/**
 * Created by congnt24 on 03/10/2016.
 */

public class EBE_StartLocationFollowService extends EBE_StartBase<Boolean> {
    public EBE_StartLocationFollowService(Boolean aBoolean) {
        super(aBoolean);
    }
    public Boolean isForeground = false;

    public EBE_StartLocationFollowService(Boolean value, Boolean isForeground) {
        super(value);
        this.isForeground = isForeground;
    }
}
