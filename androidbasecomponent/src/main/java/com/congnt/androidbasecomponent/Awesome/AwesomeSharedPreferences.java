package com.congnt.androidbasecomponent.Awesome;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by NGUYEN TRUNG CONG on 09/13/2016
 */
public abstract class AwesomeSharedPreferences {
    protected final SharedPreferences pref;
    protected final SharedPreferences.Editor editor;
    protected Context context;

    public AwesomeSharedPreferences(Context context) {
        this.context = context;
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        editor = pref.edit();
    }

    public abstract class CollectionSharedPreferences<T> extends SingleSharedPreferences<T> {

        public void put(Object id, Object obj) {
        }

        ;

        public abstract boolean put(Object obj);

        public abstract Object get(Object id);

        public abstract boolean has(Object id);

    }


    public abstract class SingleSharedPreferences<T> {
        protected abstract String ID();

        /**
         *  new TypeToken<T>(){}.getType()
         * @return TypeToken
         */
        protected Type getType(){
            return new TypeToken<T>() {}.getType();
        }
//        protected final Type type = ;
        public void save(T t) {
            editor.putString(ID(), new Gson().toJson(t, getType()));
            editor.commit();
        }

        public T load(T defaultT) {
            String str = pref.getString(ID(), "");
            return str.equals("") ? defaultT : (T) new Gson().fromJson(str, getType());
        }
    }

}
