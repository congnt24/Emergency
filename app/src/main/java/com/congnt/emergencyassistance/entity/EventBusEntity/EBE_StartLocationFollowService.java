package com.congnt.emergencyassistance.entity.EventBusEntity;

/**
 * Created by congnt24 on 03/10/2016.
 */

public class EBE_StartLocationFollowService extends EBE_StartBase<Boolean> {
    public EBE_StartLocationFollowService(Boolean aBoolean) {
        super(aBoolean);
    }
    public String objectId;

    public EBE_StartLocationFollowService(Boolean value, String objectId) {
        super(value);
        this.objectId = objectId;
    }
}
