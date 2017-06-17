package com.example.parkingApp.parkme.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Toast;
import com.example.parkingApp.parkme.R;
import com.example.parkingApp.parkme.activities.EditParking;
import com.example.parkingApp.parkme.activities.ParkingDetailsActivity;
import com.example.parkingApp.parkme.model.Parking;
import com.example.parkingApp.parkme.servicecall.ApiUtils;
import com.example.parkingApp.parkme.servicecall.ParkingService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapFragment extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;
    private Marker myMarker;
    Location mLastLocation;
    List<Marker> markerList;
    private ParkingService mAPIService;
    List<Parking> parkings;
    private String username;
    Polyline line = null;
    int i = 50;

    public MapFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_map, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mAPIService = ApiUtils.getAPIService();

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        markerList = new ArrayList<>();

        SharedPreferences preferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        username = preferences.getString("username", "");

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    googleMap.setMyLocationEnabled(true);
                }

                getParkings();

                // For dropping a marker at a point on the Map
                LatLng NoviSad = new LatLng(45.2622, 19.8519);
                //myMarker = googleMap.addMarker(new MarkerOptions().position(NoviSad).title("Parking1").snippet("Parking"));


                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(NoviSad).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        if(username.equals("admin") || username.equals("manager")){
                            SharedPreferences pref = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor edit = pref.edit();
                            edit.putString("parkingTitle", marker.getTitle());
                            edit.apply();
                            Intent in = new Intent(getActivity(), EditParking.class);
                            startActivity(in);
                        }else{
                            if(!marker.getTitle().equals("Current Position")){
                                SharedPreferences pref = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor edit = pref.edit();
                                edit.putString("parkingTitle", marker.getTitle());
                                edit.apply();

                                Intent in = new Intent(getActivity(), ParkingDetailsActivity.class);
                                startActivity(in);
                            }
                            return false;
                        }
                        return false;
                    }
                });
            }
        });

        return rootView;
    }

    private void getParkings() {
        mAPIService.getParkings().enqueue(new Callback<List<Parking>>() {
            @Override
            public void onResponse(@NonNull Call<List<Parking>> call, @NonNull Response<List<Parking>> response) {
                if (response.body() != null) {
                    parkings = response.body();
                    for (int i = 0; i < parkings.size(); i++) {
                        double latitude = Double.parseDouble(parkings.get(i).latitude);
                        double longitude = Double.parseDouble(parkings.get(i).longitude);

                        LatLng parking = new LatLng(latitude, longitude);
                        myMarker = googleMap.addMarker(new MarkerOptions().position(parking).title(parkings.get(i).parkingName).snippet("Parking"));
                        markerList.add(myMarker);
                    }
                    if(markerList.size() != 0){
                        if(username.equals("admin")){
                            markerList.get(0).setVisible(false);
                            markerList.get(1).setVisible(false);
                        } else if(username.equals("manager")){
                            markerList.get(1).setVisible(false);
                            markerList.get(2).setVisible(false);
                        }
                    }
                        getMyLocation();
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Parking>> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "Backend call failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getMyLocation() {
        LocationManager mlocManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria locationCritera = new Criteria();
        String providerName = mlocManager.getBestProvider(locationCritera,
                true);
        if (providerName != null)
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
        mlocManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean networkEnabled = mlocManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if(networkEnabled) {
            mLastLocation = mlocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (mLastLocation != null) {
                LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                myMarker = googleMap.addMarker(markerOptions);
            }
        }
    }

    public void getNearestParking() {
        Collections.sort(markerList, new Comparator<Marker>() {
            @Override
            public int compare(Marker marker2, Marker marker1) {
                if (getDistanceBetweenPoints(marker1.getPosition().latitude, marker1.getPosition().longitude, mLastLocation.getLatitude(), mLastLocation.getLongitude()) > getDistanceBetweenPoints(marker2.getPosition().latitude, marker2.getPosition().longitude, mLastLocation.getLatitude(), mLastLocation.getLongitude())) {
                    //Toast.makeText(getActivity(),"wohoo",Toast.LENGTH_LONG).show();
                    return -1;
                } else {
                    //Toast.makeText(getActivity(),":(((((",Toast.LENGTH_LONG).show();
                    return 1;
                }
            }
        });
        Toast.makeText(getActivity(),markerList.get(0).getTitle(),Toast.LENGTH_LONG).show();
        line=googleMap.addPolyline(new PolylineOptions().add(new LatLng(markerList.get(0).getPosition().latitude,
                                markerList.get(0).getPosition().longitude),
                        new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
                        .width(5).color(Color.RED));
    }

    public static float getDistanceBetweenPoints(double firstLatitude, double firstLongitude, double secondLatitude, double secondLongitude) {
        float[] results = new float[1];
        Location.distanceBetween(firstLatitude, firstLongitude, secondLatitude, secondLongitude, results);
        return results[0];
    }

    public void getCheapestParking(){
        Collections.sort(parkings, new Comparator<Parking>() {
            @Override
            public int compare(Parking p1, Parking p2) {
                return Double.valueOf(p1.getWorkingDayPrice()).compareTo(p2.getWorkingDayPrice());
            }
        });
        for (Marker m:markerList) {
            if(m.getTitle().equals(parkings.get(0).getParkingName())){
                setMarkerBounce(m);
            }
        }
    }

    private void setMarkerBounce(final Marker marker) {
        i = 50;
        final Handler handler = new Handler();
        final long startTime = SystemClock.uptimeMillis();
        final long duration = 2000;
        final Interpolator interpolator = new BounceInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - startTime;
                float t = Math.max(1 - interpolator.getInterpolation((float) elapsed/duration), 0);
                marker.setAnchor(0.5f, 1.0f +  t);
                i = i - 1;
                if(i <= 0){
                    return;
                }
                if (t > 0.0) {
                    handler.postDelayed(this, 16);
                } else {
                    setMarkerBounce(marker);
                }
            }
        });
    }

    public void searchParking(String searchParameter){
        if(line != null){
            line.remove();
        }
        for(Parking p:parkings){
            if(!p.getParkingName().toLowerCase().contains(searchParameter.toLowerCase())){
                for(Marker mar : markerList){
                    if(mar.getTitle().equals(p.getParkingName())){
                        mar.setVisible(false);
                    }
                }
            }
        }
    }

    public void setMarkersOnMap(){
        for(Marker mar:markerList){
            mar.setVisible(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        setMarkersOnMap();
        getParkings();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

}
