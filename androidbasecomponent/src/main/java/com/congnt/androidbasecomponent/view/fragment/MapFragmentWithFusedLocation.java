package com.congnt.androidbasecomponent.view.fragment;

import android.Manifest;
import android.content.Context;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.congnt.androidbasecomponent.Awesome.AwesomeFragment;
import com.congnt.androidbasecomponent.R;
import com.congnt.androidbasecomponent.utility.LocationUtil;
import com.congnt.androidbasecomponent.utility.PermissionUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

public class MapFragmentWithFusedLocation extends AwesomeFragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private String[] locationPermission = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private boolean updatable;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    private LocationRequest mLocationRequest;
    private LocationManager locationManager;
    private TextView tv_address;
    private SupportMapFragment mapFragment;
    private Context context;
    private boolean enableGestures;
    private OnMapListener onMapListener;

    public MapFragmentWithFusedLocation() {

    }

    public Location getLastLocation() {
        return mLastLocation;
    }

    public void setOnMapListener(OnMapListener onMapListener) {
        this.onMapListener = onMapListener;
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
        tv_address = (TextView) rootView.findViewById(R.id.uLocationAddress);
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Check GPS
//        boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        if (!enabled) {
//            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            startActivity(intent);
//        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (PermissionUtil.getInstance(context).checkMultiPermission(locationPermission)) {
            mMap.getUiSettings().setScrollGesturesEnabled(enableGestures);
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
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
                    movingCamera(location, 16);
                }
            }
        }.execute();
    }

    public void requestLocationUpdate() {
        if (mGoogleApiClient != null && PermissionUtil.getInstance(getActivity())
                .checkMultiPermission(locationPermission)) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    public void removeLocationUpdate() {
        if (mGoogleApiClient != null && PermissionUtil.getInstance(getActivity())
                .checkMultiPermission(locationPermission)) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        updateLocation(location);
        if (onMapListener != null) {
            onMapListener.onLocationChange(location);
        }
        /*//Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));*/

        if (!updatable) {
            //stop location updates
            removeLocationUpdate();
        }
    }

    public void setUpdatable(boolean updatable) {
        this.updatable = updatable;
    }

    //Methods were generated by implements

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setSmallestDisplacement(1000);
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        requestLocationUpdate();
        if (onMapListener != null) {
            onMapListener.onConnected();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    public interface OnMapListener {
        void onLocationChange(Location location);

        void onConnected();
    }
}
