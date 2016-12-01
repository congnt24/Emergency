package com.congnt.emergencyassistance.view.activity;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.congnt.androidbasecomponent.Awesome.AwesomeActivity;
import com.congnt.androidbasecomponent.Awesome.AwesomeLayout;
import com.congnt.androidbasecomponent.annotation.Activity;
import com.congnt.androidbasecomponent.annotation.NavigateUp;
import com.congnt.androidbasecomponent.view.fragment.MapFragmentWithFusedLocation;
import com.congnt.emergencyassistance.AppConfig;
import com.congnt.emergencyassistance.MainActionBar;
import com.congnt.emergencyassistance.R;
import com.congnt.emergencyassistance.entity.parse.LocationFollow;
import com.congnt.emergencyassistance.entity.parse.ParseFollow;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
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
        btnSubmit.setOnClickListener(this);
        handler = new Handler();
    }

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
                mapFragment.addPolyline(listLatLng.subList(cacheList.size(), listLatLng.size()-1));
                cacheList = object.getListLatLng();
            }
        }
    }

    @Override
    protected void onPause() {
        handler.removeCallbacksAndMessages(null);
        super.onPause();
    }
}
