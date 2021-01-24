package com.e_sathiuser;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.tabs.TabLayout;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.core.constants.Constants;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.utils.PolylineUtils;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mmi.services.api.directions.DirectionsCriteria;
import com.mmi.services.api.directions.MapmyIndiaDirections;
import com.mmi.services.api.directions.models.DirectionsResponse;
import com.mmi.services.api.directions.models.DirectionsRoute;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Direction extends AppCompatActivity implements OnMapReadyCallback, LocationEngineListener {
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 100;
    private LocationComponent locationComponent;
    private MapboxMap mapmyIndiaMaps;
    private LocationEngine locationEngine;
    private MapView mapView;
    double fare;
    private double lat1,long1,lat2,long2;
    private TransparentProgressDialog transparentProgressDialog;
    private String profile = DirectionsCriteria.PROFILE_DRIVING;
    private TabLayout profileTabLayout;
    private String resource = DirectionsCriteria.RESOURCE_ROUTE;
    private LinearLayout directionDetailsLayout;
    private TextView tvDistance, tvDuration ,tvPlacename,tvfare,tvOtp,tvdrivername,tvNo;
    private DirectionPolylinePlugin directionPolylinePlugin;
    private String placename,name,vehcile,phone,otp;
    private int vt=0,t=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);
        mapView = findViewById(R.id.map_view);
        Bundle b = getIntent().getExtras();
        lat2 = b.getDouble("lat2");
        long2 = b.getDouble("long2");
        lat1 = b.getDouble("lat3");
        long1 = b.getDouble("long3");
        fare = b.getDouble("fare");
        otp=b.getString("otp");
        placename=b.getString("placename");
        name=b.getString("name");
        vehcile=b.getString("vehcile");
        phone=b.getString("phone_no");
        profileTabLayout = findViewById(R.id.tab_layout_profile);
        RadioGroup rgResource = findViewById(R.id.rg_resource_type);
        directionDetailsLayout = findViewById(R.id.Distance_direction);
        tvDistance = findViewById(R.id.Distance);
        tvDuration = findViewById(R.id.duration);
        tvPlacename=findViewById(R.id.pl_name);
        tvfare=findViewById(R.id.estimated_fare);
        tvPlacename.setText(placename);
        tvOtp = findViewById(R.id.Otp);
        tvdrivername=findViewById(R.id.driver_tv);
        tvNo=findViewById(R.id.car_no);
        tvNo.setText(vehcile);
        tvdrivername.setText(name);
        tvOtp.setText(otp);
        profileTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (mapmyIndiaMaps == null) {
                    if (profileTabLayout.getTabAt(0) != null) {
                        Objects.requireNonNull(profileTabLayout.getTabAt(0)).select();
                        return;
                    }
                }
                switch (tab.getPosition()) {
                    case 0:
                        profile = DirectionsCriteria.PROFILE_DRIVING;
                        rgResource.setVisibility(View.VISIBLE);
                        break;

                    case 1:
                        profile = DirectionsCriteria.PROFILE_BIKING;
                        rgResource.check(R.id.rb_without_traffic);
                        rgResource.setVisibility(View.GONE);
                        break;

                    case 2:
                        profile = DirectionsCriteria.PROFILE_WALKING;
                        rgResource.check(R.id.rb_without_traffic);
                        rgResource.setVisibility(View.GONE);
                        break;

                    default:
                        break;
                }

                getDirections();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        rgResource.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (radioGroup.getCheckedRadioButtonId()) {
                case R.id.rb_without_traffic:
                    resource = DirectionsCriteria.RESOURCE_ROUTE;
                    break;

                case R.id.rb_with_traffic:
                    resource = DirectionsCriteria.RESOURCE_ROUTE_TRAFFIC;
                    break;

                case R.id.rb_with_route_eta:
                    resource = DirectionsCriteria.RESOURCE_ROUTE_ETA;
                    break;

                default:
                    break;
            }

            getDirections();
        });
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        transparentProgressDialog = new TransparentProgressDialog(this, R.drawable.circle_loader, "");

    }

    @Override
    public void onMapReady(MapboxMap mapmyIndiaMaps) {
        this.mapmyIndiaMaps = mapmyIndiaMaps;
        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(
                lat1, long1)).zoom(15).tilt(0).build();
        mapmyIndiaMaps.setCameraPosition(cameraPosition);
        mapmyIndiaMaps.addMarker(new MarkerOptions().position(new LatLng(
                lat2, long2)).title(placename));
        enableLocation();
        if (CheckInternet.isNetworkAvailable(this)) {
            getDirections();
        } else {
            Toast.makeText(this, getString(R.string.pleaseCheckInternetConnection), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapError(int i, String s) {

    }

    private void enableLocation() {
        LocationComponentOptions options = LocationComponentOptions.builder(this)
                .trackingGesturesManagement(true)
                .accuracyColor(ContextCompat.getColor(this, R.color.teal_200))
                .foregroundDrawable(R.drawable.location_pointer)
                .build();
        locationComponent = mapmyIndiaMaps.getLocationComponent();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            enableLocation();
            return;
        }
        locationComponent.activateLocationComponent(this, options);
        locationComponent.setLocationComponentEnabled(true);
        locationEngine = locationComponent.getLocationEngine();
        locationEngine.addLocationEngineListener(this);
        locationComponent.setCameraMode(CameraMode.TRACKING);
        locationComponent.setRenderMode(RenderMode.COMPASS);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        if (locationEngine != null) {
            locationEngine.removeLocationEngineListener(this);
            locationEngine.addLocationEngineListener(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        if (locationEngine != null)
            locationEngine.removeLocationEngineListener(this);
    }


    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
        if (locationEngine != null) {
            locationEngine.removeLocationEngineListener(this);
            locationEngine.removeLocationUpdates();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (locationEngine != null) {
            locationEngine.deactivate();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onConnected() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this, // Activity
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            return;
        }
        locationEngine.requestLocationUpdates();

    }

    @Override
    public void onLocationChanged(Location location) {
        //mapmyIndiaMaps.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16));
        lat1=location.getLatitude();
        long1=location.getLongitude();
        vt=1;
        getDirections();
        if(t==1)
            nav();
    }
    private void progressDialogShow() {
        transparentProgressDialog.show();
    }
    private void progressDialogHide() {
        transparentProgressDialog.dismiss();
    }
    private void getDirections() {
        if(vt==0)
            progressDialogShow();

        MapmyIndiaDirections.builder()
                .origin(Point.fromLngLat(long1, lat1))
                .destination(Point.fromLngLat(long2, lat2))
                .profile(profile)
                .resource(resource)
                .steps(true)
                .alternatives(false)
                .overview(DirectionsCriteria.OVERVIEW_FULL).build().enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(@NonNull Call<DirectionsResponse> call, @NonNull Response<DirectionsResponse> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        DirectionsResponse directionsResponse = response.body();
                        List<DirectionsRoute> results = directionsResponse.routes();

                        if (results.size() > 0) {
                            mapmyIndiaMaps.clear();
                            mapmyIndiaMaps.addMarker(new MarkerOptions().position(new LatLng(
                                    lat2, long2)).title(placename));
                            DirectionsRoute directionsRoute = results.get(0);
                            if (directionsRoute != null && directionsRoute.geometry() != null) {
                                drawPath(PolylineUtils.decode(directionsRoute.geometry(), Constants.PRECISION_6));
                                updateData(directionsRoute);
                            }
                        }
                    }
                } else {
                    Toast.makeText(Direction.this, response.message() + response.code(), Toast.LENGTH_LONG).show();
                }
                progressDialogHide();
            }

            @Override
            public void onFailure(@NonNull Call<DirectionsResponse> call, @NonNull Throwable t) {
                progressDialogHide();
                t.printStackTrace();

            }
        });


    }
    private void updateData(@NonNull DirectionsRoute directionsRoute) {
        if (directionsRoute.distance() != null && directionsRoute.distance() != null) {
            directionDetailsLayout.setVisibility(View.VISIBLE);
            DecimalFormat df = new DecimalFormat("0.00");

            tvDuration.setText(getFormattedDuration(directionsRoute.duration()));
            tvDistance.setText(getFormattedDistance(directionsRoute.distance()));
            tvfare.setText(df.format(fare));

            //CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(
            //      lat2, long2)).zoom(7).tilt(0).build();
            //mapmyIndiaMaps.setCameraPosition(cameraPosition);
        }
    }

    private String getFormattedDistance(double distance) {
        //fare=(distance/1000)*10;

        if ((distance / 1000) < 1) {
            return distance + "mtr.";
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        return decimalFormat.format(distance / 1000) + "Km.";
    }
    private String getFormattedDuration(double duration) {
        long min = (long) (duration % 3600 / 60);
        long hours = (long) (duration % 86400 / 3600);
        long days = (long) (duration / 86400);
        if (days > 0L) {
            return days + " " + (days > 1L ? "Days" : "Day") + " " + hours + " " + "hr" + (min > 0L ? " " + min + " " + "min." : "");
        } else {
            return hours > 0L ? hours + " " + "hr" + (min > 0L ? " " + min + " " + "min" : "") : min + " " + "min.";
        }
    }

    private void drawPath(@NonNull List<Point> waypoints) {
        ArrayList<LatLng> listOfLatLng = new ArrayList<>();
        for (Point point : waypoints) {
            listOfLatLng.add(new LatLng(point.latitude(), point.longitude()));
        }

        if(directionPolylinePlugin == null) {
            directionPolylinePlugin = new DirectionPolylinePlugin(mapmyIndiaMaps, mapView, profile);
            directionPolylinePlugin.createPolyline(listOfLatLng);
        } else {
            directionPolylinePlugin.updatePolyline(profile, listOfLatLng);

        }
//        mapmyIndiaMap.addPolyline(new PolylineOptions().addAll(listOfLatLng).color(Color.parseColor("#3bb2d0")).width(4));
        LatLngBounds latLngBounds = new LatLngBounds.Builder().includes(listOfLatLng).build();
        //mapmyIndiaMaps.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 30));
    }

    private void nav(){
        @SuppressLint("Range") CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(
                lat1, long1)).zoom(20).tilt(85).build();
        mapmyIndiaMaps.setCameraPosition(cameraPosition);
    }
    public void navigate(View view) {
        nav();
        t=1;
    }
}