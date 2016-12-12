package com.congnt.emergencyassistance.view.activity;

import android.location.Location;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.congnt.androidbasecomponent.Awesome.AwesomeActivity;
import com.congnt.androidbasecomponent.Awesome.AwesomeLayout;
import com.congnt.androidbasecomponent.annotation.Activity;
import com.congnt.androidbasecomponent.annotation.NavigateUp;
import com.congnt.androidbasecomponent.utility.CommunicationUtil;
import com.congnt.androidbasecomponent.view.fragment.MapFragmentWithFusedLocation;
import com.congnt.androidbasecomponent.view.widget.FlatButtonWithIconTop;
import com.congnt.emergencyassistance.AppConfig;
import com.congnt.emergencyassistance.MainActionBar;
import com.congnt.emergencyassistance.MainApplication;
import com.congnt.emergencyassistance.MySharedPreferences;
import com.congnt.emergencyassistance.R;
import com.congnt.emergencyassistance.entity.ItemCountryEmergencyNumber;
import com.congnt.emergencyassistance.entity.ItemHistory;
import com.congnt.emergencyassistance.entity.parse.ParseFollow;
import com.google.android.gms.maps.model.LatLng;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


@Activity(transitionAnim = Activity.AnimationType.ANIM_LEFT_TO_RIGHT
        , actionbarType = Activity.ActionBarType.ACTIONBAR_CUSTOM
        , mainLayoutId = R.layout.layout_follow
        , enableSearch = true)
@NavigateUp
public class FollowActivity extends AwesomeActivity implements View.OnClickListener, GetCallback<ParseFollow> {
    private static final String TAG = "FollowActivity";
    private MapFragmentWithFusedLocation mapFragment;
    private EditText etParseId;
    private Button btnSubmit;
    private Handler handler;
    private List<LatLng> cacheList = new ArrayList<>();
    private FlatButtonWithIconTop btn_police;
    private FlatButtonWithIconTop btn_fire;
    private FlatButtonWithIconTop btn_ambulance;

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected AwesomeLayout getCustomActionBar() {
        return new MainActionBar(this);
    }

    @Override
    protected void initialize(View mainView) {
        etParseId = (EditText) mainView.findViewById(R.id.et_parse_id);
        btnSubmit = (Button) mainView.findViewById(R.id.btn_submit);
        mapFragment = (MapFragmentWithFusedLocation) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.setUpdatable(false);
        mapFragment.setScrollGesturesEnabled(true);
        mapFragment.setOnMapListener(new MapFragmentWithFusedLocation.OnMapListener() {
            @Override
            public void onLocationChange(Location location) {
                mapFragment.movingCamera(((MainApplication) getApplication()).getLastLocation(), 13);
            }

            @Override
            public void onConnected() {
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapFragment.getMap().clear();
                getFollowFromParse(etParseId.getText().toString());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                mapFragment.getMap().clear();
                        getFollowFromParse(etParseId.getText().toString());
                        handler.postDelayed(this, AppConfig.PARSE_DELAY_DURATION);
                    }
                }, AppConfig.PARSE_DELAY_DURATION);
            }
        });
        handler = new Handler();
        setupEmergencyNumber(mainView);
        if (getIntent() != null) {
            ItemHistory item = getIntent().getParcelableExtra("history");
            if (item != null) {
                List<LatLng> listLatLng = item.getListLocation();
                mapFragment.addPolyline(listLatLng);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_call_ambulance:
                CommunicationUtil.callTo(this, btn_ambulance.getText());
                break;
            case R.id.btn_call_fire:
                CommunicationUtil.callTo(this, btn_fire.getText());
                break;
            case R.id.btn_call_police:
                CommunicationUtil.callTo(this, btn_police.getText());
                break;
        }
    }

    public void getFollowFromParse(String currentParseId) {
        ParseQuery<ParseFollow> query = ParseQuery.getQuery(ParseFollow.class);
        query.getInBackground(currentParseId, this);
    }

    @Override
    public void done(ParseFollow object, ParseException e) {
        if (e == null) {
            //TODO: Get parse follow
            List<LatLng> listLatLng = object.getListLatLng();
            if (listLatLng.size() > cacheList.size()) {
                mapFragment.addPolyline(listLatLng.subList(cacheList.size(), listLatLng.size() - 1));
                cacheList = object.getListLatLng();
            }
        }
    }

    @Override
    protected void onPause() {
        handler.removeCallbacksAndMessages(null);
        super.onPause();
    }

    /**
     * Setup emergency number for a country
     *
     * @param rootView
     */
    public void setupEmergencyNumber(View rootView) {
        btn_police = (FlatButtonWithIconTop) rootView.findViewById(R.id.btn_call_police);
        btn_fire = (FlatButtonWithIconTop) rootView.findViewById(R.id.btn_call_fire);
        btn_ambulance = (FlatButtonWithIconTop) rootView.findViewById(R.id.btn_call_ambulance);
        btn_police.setOnClickListener(this);
        btn_fire.setOnClickListener(this);
        btn_ambulance.setOnClickListener(this);
        //Set Value
        setupEmergencyText();
    }

    private void setupEmergencyText() {
        ItemCountryEmergencyNumber countrynumber = MySharedPreferences.getInstance(this).countryNumber.load(null);
        if (countrynumber == null) return;
        btn_police.setText("" + countrynumber.police);
        btn_fire.setText("" + countrynumber.fire);
        btn_ambulance.setText("" + countrynumber.ambulance);
    }


}
