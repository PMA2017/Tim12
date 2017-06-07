package com.example.parkingApp.parkme.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.parkingApp.parkme.MainActivity;
import com.example.parkingApp.parkme.R;
import com.example.parkingApp.parkme.activities.ParkingDetailsActivity;
import com.example.parkingApp.parkme.model.Parking;
import com.example.parkingApp.parkme.servicecall.ApiUtils;
import com.example.parkingApp.parkme.servicecall.ParkingService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    List<Marker> markerList;

    private ParkingService mAPIService;

    public MapFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    googleMap.setMyLocationEnabled(true);
                }

                mAPIService.getParkings().enqueue(new Callback<List<Parking>>() {
                    @Override
                    public void onResponse(Call<List<Parking>> call, Response<List<Parking>> response) {
                        if (response.body() != null) {
                            List<Parking> parkings = (List<Parking>) response.body();
                            for (int i = 0; i < parkings.size(); i++) {
                                double latitude = Double.parseDouble(parkings.get(i).latitude);
                                double longitude = Double.parseDouble(parkings.get(i).longitude);

                                LatLng parking = new LatLng(latitude, longitude);
                                myMarker = googleMap.addMarker(new MarkerOptions().position(parking).title(parkings.get(i).parkingName).snippet("Parking"));
                                markerList.add(myMarker);
                            }

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
                            mLastLocation = mlocManager.getLastKnownLocation(providerName);

                            LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(latLng);
                            markerOptions.title("Current Position");
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                            myMarker = googleMap.addMarker(markerOptions);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Parking>> call, Throwable t) {
                        Toast.makeText(getActivity(), "Milana", Toast.LENGTH_LONG).show();
                    }
                });

                // For dropping a marker at a point on the Map
                LatLng NoviSad = new LatLng(45.2622, 19.8519);
                //myMarker = googleMap.addMarker(new MarkerOptions().position(NoviSad).title("Parking1").snippet("Parking"));


                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(NoviSad).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
//                        if(marker.equals(myMarker)){
                        SharedPreferences pref = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = pref.edit();
                        edit.putString("parkingTitle", marker.getTitle());
                        edit.commit();

//                            Bundle bundle = new Bundle();
//                            bundle.putString("parkingTitle", marker.getTitle());
                        Intent in = new Intent(getActivity(), ParkingDetailsActivity.class);
                        startActivity(in);
//                        }
                        return false;
                    }
                });
            }
        });

        return rootView;
    }

    public void getNearestParking() {
        /*Collections.sort(markerList, new Comparator<Marker>() {
            @Override
            public int compare(Marker marker2, Marker marker1) {
                //
                if(getDistanceBetweenPoints(marker1.get,location)>getDistanceBetweenPoints(marker2.getLocation(),location)){
                    return -1;
                } else {
                    return 1;
                }
            }
        });*/
    }

    public static float getDistanceBetweenPoints(double firstLatitude, double firstLongitude, double secondLatitude, double secondLongitude) {
        float[] results = new float[1];
        Location.distanceBetween(firstLatitude, firstLongitude, secondLatitude, secondLongitude, results);
        return results[0];
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
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
