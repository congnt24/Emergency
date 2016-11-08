package com.congnt.emergencyassistance.view.fragment;

import android.location.Location;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import com.congnt.androidbasecomponent.Awesome.AwesomeFragment;
import com.congnt.androidbasecomponent.adapter.AwesomeRecyclerAdapter;
import com.congnt.androidbasecomponent.network.RetrofitBuilder;
import com.congnt.androidbasecomponent.view.fragment.MapFragmentWithFusedLocation;
import com.congnt.emergencyassistance.AppConfig;
import com.congnt.emergencyassistance.R;
import com.congnt.emergencyassistance.adapter.NearByAdapter;
import com.congnt.emergencyassistance.entity.RetrofitPlaceEntity.PlaceDetailEntity;
import com.congnt.emergencyassistance.entity.RetrofitPlaceEntity.PlaceEntity;
import com.congnt.emergencyassistance.entity.RetrofitPlaceEntity.Result;
import com.congnt.emergencyassistance.network.RetrofitPlace;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import info.hoang8f.android.segmented.SegmentedGroup;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by congnt24 on 25/09/2016.
 */

public class NearByFragment extends AwesomeFragment implements View.OnClickListener, MapFragmentWithFusedLocation.OnMapListener {
    private static final int PROXIMITY_RADIUS = 10000;
    private static final long DURATION = 1000;
    private MapFragmentWithFusedLocation mapFragment;
    private Retrofit retrofit;
    private RecyclerView recyclerView;
    private List<Result> listNearBy = new ArrayList<>();
    private NearByAdapter adapter;
    private SegmentedGroup segmentedgroup_nearby;
    private String nearbyName = "police";
    private long lastTime = 0;

    public static AwesomeFragment newInstance() {
        return new NearByFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_nearby;
    }

    @Override
    protected void initAll(View rootView) {
        segmentedgroup_nearby = (SegmentedGroup) rootView.findViewById(R.id.segmentedgroup_nearby);
        //InitView
        setupRecyclerView(rootView);
        //Init Function
        retrofit = RetrofitBuilder.getRetrofit(AppConfig.GOOGLE_PLACE_URL_BASE, null, 0, 0);
        mapFragment = (MapFragmentWithFusedLocation) getChildFragmentManager().findFragmentById(R.id.map_fragment2);
//        mapFragment.setUpdatable(true);
        mapFragment.setScrollGesturesEnabled(true);
        mapFragment.setOnMapListener(this);
        segmentedgroup_nearby.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_nearby_police:
                        nearbyName = "police";
                        break;
                    case R.id.rb_nearby_fire:
                        nearbyName = "fire station";
                        break;
                    case R.id.rb_nearby_hospital:
                        nearbyName = "hospital";
                        break;
                }
                try {
                    getNearByLocation(nearbyName);
                } catch (Exception e) {

                }
            }
        });
    }

    public void setupRecyclerView(View rootView) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new NearByAdapter(getActivity(), listNearBy, new AwesomeRecyclerAdapter.OnClickListener<Result>() {
            @Override
            public void onClick(Result item, int position) {
                Location location = new Location("");
                location.setLatitude(item.getGeometry().getLocation().getLat());
                location.setLongitude(item.getGeometry().getLocation().getLng());
                mapFragment.animateCamera(location, 16);
            }
        });
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    private void getNearByLocation(String type) {
        if ((System.currentTimeMillis() - lastTime) > DURATION) {//First tap
            lastTime = System.currentTimeMillis();
        } else {
            return;
        }
        final GoogleMap mMap = mapFragment.getMap();
        final Location currentLocation = mapFragment.getLastLocation();
        RetrofitPlace service = retrofit.create(RetrofitPlace.class);
        Call<PlaceEntity> call = service.getNearbyPlaces(type, currentLocation.getLatitude() + "," + currentLocation.getLongitude(), PROXIMITY_RADIUS);
        call.enqueue(new Callback<PlaceEntity>() {
            @Override
            public void onResponse(Call<PlaceEntity> call, Response<PlaceEntity> response) {
                try {
                    listNearBy.clear();
                    listNearBy.addAll(response.body().getResults());
                    mMap.clear();
                    // This loop will go through all the results and add marker on each location.
                    for (int i = 0; i < listNearBy.size(); i++) {
//                        getDetailPlace(i);
                        Double lat = listNearBy.get(i).getGeometry().getLocation().getLat();
                        Double lng = listNearBy.get(i).getGeometry().getLocation().getLng();
                        Location location = new Location("Point " + i);
                        location.setLatitude(lat);
                        location.setLongitude(lng);

                        listNearBy.get(i).setDistance(currentLocation.distanceTo(location));
                        String placeName = listNearBy.get(i).getName();
                        String vicinity = listNearBy.get(i).getVicinity();
                        //Marker
                        mapFragment.addMarker(placeName + " : " + vicinity, location);
                    }
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
                    Collections.sort(listNearBy, new Comparator<Result>() {
                        @Override
                        public int compare(Result o1, Result o2) {
                            return o1.getDistance() < o2.getDistance() ? -1 : 1;
                        }
                    });
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<PlaceEntity> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });
    }

    private void getDetailPlace(final int index) {
        RetrofitPlace service = retrofit.create(RetrofitPlace.class);
        Call<PlaceDetailEntity> call = service.getDetailPlace(listNearBy.get(index).getPlaceId());
        call.enqueue(new Callback<PlaceDetailEntity>() {
            @Override
            public void onResponse(Call<PlaceDetailEntity> call, Response<PlaceDetailEntity> response) {
                PlaceDetailEntity detail = response.body();
                listNearBy.get(index).setPhone(detail.result.phone);
                adapter.notifyItemChanged(index);
            }

            @Override
            public void onFailure(Call<PlaceDetailEntity> call, Throwable t) {
                Log.d("onFailure", " get details place " + t.toString());
            }
        });
    }

    @Override
    public void onLocationChange(final Location location) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                segmentedgroup_nearby.check(R.id.rb_nearby_police);
                mapFragment.animateCamera(location, 13);
            }
        }, 1000);
    }

    @Override
    public void onConnected() {
    }
//
//    private void makeMarker(GoogleMap mMap,String title, Location location){
//        MarkerOptions markerOptions = new MarkerOptions();
//        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        // Position of Marker on Map
//        markerOptions.position(latLng);
//        // Adding Title to the Marker
//        markerOptions.title(title);
//        // Adding Marker to the Camera.
//        Marker m = mMap.addMarker(markerOptions);
//        // Adding colour to the marker
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//    }
}
