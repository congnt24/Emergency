package com.congnt.emergencyassistance.view.fragment;

import android.Manifest;
import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Location;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import com.congnt.androidbasecomponent.Awesome.AwesomeFragment;
import com.congnt.androidbasecomponent.R;
import com.congnt.androidbasecomponent.utility.LocationUtil;
import com.congnt.androidbasecomponent.utility.PermissionUtil;
import com.congnt.androidbasecomponent.view.fragment.MapFragmentWithFusedLocation;
import com.congnt.emergencyassistance.MainApplication;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class MapFragmentWithFusedLocationLite extends AwesomeFragment implements OnMapReadyCallback {
    private String[] locationPermission = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    private GoogleMap mMap;
    private boolean updatable;
    private OnMapListener listener;

    public void setAddress(String s) {
        this.tv_address.setText(s);
    }

    private TextView tv_address;
    private SupportMapFragment mapFragment;
    private Context context;
    private boolean enableGestures;

    public MapFragmentWithFusedLocationLite() {

    }

    public GoogleMap getMap() {
        return mMap;
    }

    public void setScrollGesturesEnabled(boolean enableGestures) {
        this.enableGestures = enableGestures;
        if (mMap != null) mMap.getUiSettings().setScrollGesturesEnabled(enableGestures);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_map;
    }

    @Override
    protected void initAll(View rootView) {
        context = getActivity();
//        EventBus.getDefault().register(context);
        tv_address = (TextView) rootView.findViewById(R.id.uLocationAddress);
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void setUpdatable(boolean updatable) {
        this.updatable = updatable;
    }

    public void movingCamera(Location location, int zoom) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude()))
                .zoom(zoom)                   // Sets the zoom
                .build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void animateCamera(Location location, int zoom) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude()))
                .zoom(zoom)                   // Sets the zoom
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void updateLocation(final Location location) {
        new AsyncTask<Void, Void, List<Address>>() {
            @Override
            protected List<Address> doInBackground(Void... params) {
                return LocationUtil.getAddress(context, location);
            }

            @Override
            protected void onPostExecute(List<Address> addresses) {
                if (addresses == null) return;
                if (addresses.isEmpty()) {
                    tv_address.setText("Waiting for location");
                } else {
                    tv_address.setText(LocationUtil.getAddress(addresses, 4));
//                    ((MainApplication) getActivity().getApplication()).setLastAddress(tv_address.getText().toString());
//                    movingCamera(location, 13);
                }
            }
        }.execute();
    }

    //Marker

    /**
     * Add marker to the map
     *
     * @param title    title of marker
     * @param location location of marker
     */
    public void addMarker(String title, Location location) {
        addMarker(title, location.getLatitude(), location.getLongitude());
    }

    public void addMarker(String title, String lat, String lng) {
        addMarker(title, Double.parseDouble(lat), Double.parseDouble(lng));
    }

    public void addMarker(String title, double lat, double lng) {
        MarkerOptions markerOptions = new MarkerOptions();
        LatLng latLng = new LatLng(lat, lng);
        // Position of Marker on Map
        markerOptions.position(latLng);
        // Adding Title to the Marker
        markerOptions.title(title);
        // Adding Marker to the Camera.
        Marker m = getMap().addMarker(markerOptions);
        // Adding colour to the marker
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
    }
    //Polyline

    public void addPolyline(Iterable<LatLng> list) {
        PolylineOptions options = new PolylineOptions()
                .addAll(list)
                .color(Color.BLUE);
        getMap().addPolyline(options);
    }

    public void addPolyline(LatLng... list) {
        PolylineOptions options = new PolylineOptions()
                .add(list)
                .color(Color.BLUE);
        getMap().addPolyline(options);
    }

    public void setMyLocationEnabled(boolean value){
        if (PermissionUtil.getInstance(context).checkMultiPermission(locationPermission)) {
            mMap.setMyLocationEnabled(value);
        }
    }

    public void setListener(OnMapListener listener) {
        this.listener = listener;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setScrollGesturesEnabled(enableGestures);
        setMyLocationEnabled(true);
        if (listener != null) {
            listener.onMapReady();
        }
    }

    public interface OnMapListener{
        void onMapReady();
    }
}
