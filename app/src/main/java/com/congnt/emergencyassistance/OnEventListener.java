package com.congnt.emergencyassistance;

/**
 * Created by congnt24 on 23/11/2016.
 */

public interface OnEventListener<T> {
    void onSuccess(T t);

    void onError(T t);
}
