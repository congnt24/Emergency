package com.congnt.emergencyassistance.entity.EventBusEntity;

/**
 * Created by congnt24 on 01/10/2016.
 */

public abstract class EBE_StartBase<T extends Boolean> {
    public T value;

    public EBE_StartBase(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
