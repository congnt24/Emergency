package com.congnt.emergencyassistance;

import android.content.Context;

import com.congnt.androidbasecomponent.Awesome.AwesomeSharedPreferences;
import com.congnt.emergencyassistance.entity.ItemContact;
import com.congnt.emergencyassistance.entity.ItemCountryEmergencyNumber;
import com.congnt.emergencyassistance.entity.ItemHistory;
import com.congnt.emergencyassistance.entity.ItemSettingSpeech;
import com.congnt.emergencyassistance.entity.SelfDefense;
import com.congnt.emergencyassistance.entity.firebase.User;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by congnt24 on 12/10/2016.
 */

public class MySharedPreferences extends AwesomeSharedPreferences {
    public static MySharedPreferences instance;
    public SingleSharedPreferences<Boolean> isListening = new SingleSharedPreferences<Boolean>() {
        @Override
        protected String ID() {
            return "IS_LISTENING";
        }
    };
    //Contain list emergency command
    public SingleSharedPreferences<List<ItemSettingSpeech>> emergency_command = new SingleSharedPreferences<List<ItemSettingSpeech>>() {
        @Override
        protected String ID() {
            return "emergency_command";
        }

        @Override
        protected Type getType() {
            return new TypeToken<List<ItemSettingSpeech>>() {}.getType();
        }
    };
    public SingleSharedPreferences<List<ItemSettingSpeech>> emergency_command_custom = new SingleSharedPreferences<List<ItemSettingSpeech>>() {
        @Override
        protected String ID() {
            return "emergency_command_custom";
        }

        @Override
        protected Type getType() {
            return new TypeToken<List<ItemSettingSpeech>>() {
            }.getType();
        }
    };
    //Contain list contact
    public CollectionSharedPreferences<List<ItemContact>> listContact = new CollectionSharedPreferences<List<ItemContact>>() {
        @Override
        public boolean put(Object obj) {
            if (!has(obj)) {
                List<ItemContact> list = load(new ArrayList<ItemContact>());
                list.add((ItemContact) obj);
                save(list);
                return true;
            }
            return false;
        }

        @Override
        public Object get(Object id) {
            List<ItemContact> list = load(new ArrayList<ItemContact>());
            return list.get((int) id);
        }

        @Override
        public boolean has(Object obj) {
            ItemContact contact = (ItemContact) obj;
            List<ItemContact> list = load(new ArrayList<ItemContact>());
            for (ItemContact item : list) {
                if (item.getContactNumber() == null) {
                    return false;
                }
                if (item.getContactNumber().equalsIgnoreCase(contact.getContactNumber())) {
                    return true;
                }
            }
            return false;
        }

        @Override
        protected String ID() {
            return "emergency_contact";
        }

        @Override
        protected Type getType() {
            return new TypeToken<List<ItemContact>>() {
            }.getType();
        }
    };
    //Contain current emergency number
    public SingleSharedPreferences<ItemCountryEmergencyNumber> countryNumber = new SingleSharedPreferences<ItemCountryEmergencyNumber>() {
        @Override
        protected String ID() {
            return "country_emergency_number";
        }

        @Override
        protected Type getType() {
            return new TypeToken<ItemCountryEmergencyNumber>() {
            }.getType();
        }
    };
    //Share location state
    public SingleSharedPreferences<Boolean> shareLocationState = new SingleSharedPreferences<Boolean>() {
        @Override
        protected String ID() {
            return "share_location";
        }
    };
    //Save User Profile
    public SingleSharedPreferences<User> userProfile = new SingleSharedPreferences<User>() {
        @Override
        protected String ID() {
            return "user_profile";
        }

        @Override
        protected Type getType() {
            return new TypeToken<User>() {
            }.getType();
        }
    };

    public SingleSharedPreferences<SelfDefense> selfDefense = new SingleSharedPreferences<SelfDefense>() {
        @Override
        protected String ID() {
            return "self_defense";
        }

        @Override
        protected Type getType() {
            return new TypeToken<SelfDefense>() {
            }.getType();
        }
    };
    public SingleSharedPreferences<Boolean> isFirstTime = new SingleSharedPreferences<Boolean>() {
        @Override
        protected String ID() {
            return "first_time";
        }
    };
    /**
     * Save history
     */
    public SingleSharedPreferences<List<ItemHistory>> listHistoty = new SingleSharedPreferences<List<ItemHistory>>() {
        @Override
        protected String ID() {
            return "emergency_history";
        }

        @Override
        protected Type getType() {
            return new TypeToken<List<ItemHistory>>() {
            }.getType();
        }
    };

    public MySharedPreferences(Context context) {
        super(context);
    }

    public static MySharedPreferences getInstance(Context context) {
        if (instance == null) instance = new MySharedPreferences(context);
        return instance;
    }

}
