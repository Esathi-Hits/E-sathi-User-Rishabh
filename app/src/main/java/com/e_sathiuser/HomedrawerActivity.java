package com.e_sathiuser;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e_sathiuser.AutoSuggestAdapter;
import com.e_sathiuser.CheckInternet;
import com.e_sathiuser.Direction;
import com.e_sathiuser.R;
import com.e_sathiuser.SendNotificationPack.APIService;
import com.e_sathiuser.SendNotificationPack.Client;
import com.e_sathiuser.SendNotificationPack.Data;
import com.e_sathiuser.SendNotificationPack.MyResponse;
import com.e_sathiuser.SendNotificationPack.NotificationSender;
import com.e_sathiuser.DirectionPolylinePlugin;
import com.e_sathiuser.TransparentProgressDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.core.constants.Constants;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.utils.PolylineUtils;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapmyindia.sdk.plugin.annotation.LineManager;
import com.mapmyindia.sdk.plugin.annotation.LineOptions;
import com.mmi.services.api.Place;
import com.mmi.services.api.PlaceResponse;
import com.mmi.services.api.autosuggest.model.AutoSuggestAtlasResponse;
import com.mmi.services.api.autosuggest.model.ELocation;
import com.mmi.services.api.directions.DirectionsCriteria;
import com.mmi.services.api.directions.MapmyIndiaDirections;
import com.mmi.services.api.directions.models.DirectionsResponse;
import com.mmi.services.api.directions.models.DirectionsRoute;
import com.mmi.services.api.distance.MapmyIndiaDistanceMatrix;
import com.mmi.services.api.distance.models.DistanceResponse;
import com.mmi.services.api.distance.models.DistanceResults;
import com.mmi.services.api.reversegeocode.MapmyIndiaReverseGeoCode;
import com.mmi.services.api.textsearch.MapmyIndiaTextSearch;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomedrawerActivity extends Fragment implements OnMapReadyCallback, LocationEngineListener, TextWatcher, TextView.OnEditorActionListener {
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 100;
    private LocationComponent locationComponent;
    private MapboxMap mapmyIndiaMaps;
    private LocationEngine locationEngine;
    private MapView mapView;
    private ImageView iv1;
    private EditText autoSuggestText,autoSuggestText1;
    private RecyclerView recyclerView,recyclerView1;
    private LineManager lineManager;
    private DirectionPolylinePlugin directionPolylinePlugin;
    private LinearLayoutManager mLayoutManager,linearLayoutManager;
    private TransparentProgressDialog transparentProgressDialog;
    private Handler handler;
    private int tp=0,tb1=0;
    double fare,b=2;
    AppCompatButton appCompatButton;
    private String placename1,placename2,name,vehcile,phno;
    private TextView tvDistance, tvDuration,tvplacename,tvfare;
    private LinearLayout directionDetailsLayout;
    private double lat1,long1, lat2=0 ,long2=0,lat3,long3;
    private int vt,tb=0;
    Marker marker1,marker2;
    APIService apiService;
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    List aList;
    DatabaseReference dataref;
    int t=0;
    String otp;
    LinearLayout linearLayout,linearLayout2,linearLayout3;
    AppCompatButton button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_homedrawer, container, false);
        mapView = view.findViewById(R.id.map_view);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        linearLayout=view.findViewById(R.id.Accepted);
        linearLayout2=view.findViewById(R.id.Searching);
        linearLayout3=view.findViewById(R.id.Not_found);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        initReferences(view);
        initListeners();
        mapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setVisibility(View.INVISIBLE);
            }
        });
        ImageButton currentloc1=view.findViewById(R.id.current_location1);
        currentloc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoSuggestText1.setVisibility(View.VISIBLE);
                reverseGeocode(lat1,long1);
                tp=1;
                autoSuggestText.clearFocus();
                InputMethodManager in = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(autoSuggestText.getWindowToken(), 0);
                addMarker1(lat1,long1,"Current Location");
                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(
                        lat1, long1)).zoom(16).tilt(0).build();
                mapmyIndiaMaps.setCameraPosition(cameraPosition);
            }
        });
        ImageButton currentloc=view.findViewById(R.id.current_location);
        currentloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(
                        lat1, long1)).zoom(16).tilt(0).build();
                mapmyIndiaMaps.setCameraPosition(cameraPosition);
            }
        });
        Button book=view.findViewById(R.id.nav);
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t=0;
                autoSuggestText1.setEnabled(false);
                autoSuggestText.setEnabled(false);
                directionDetailsLayout.setVisibility(View.GONE);
                linearLayout2.setVisibility(View.VISIBLE);
                aList = new ArrayList();
                otp=getRandomNumberString();
                FirebaseDatabase.getInstance().getReference().child("Tokens").child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Accepted").setValue("false");
                FirebaseDatabase.getInstance().getReference().child("Tokens").child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Alloted").setValue("false");
                FirebaseDatabase.getInstance().getReference().child("Tokens").child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("OTP").setValue(otp);
                dataref = FirebaseDatabase.getInstance().getReference().child("Tokens").child("Driver");
                dataref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (t == 0) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                DatabaseReference dataref2 = dataref.child(snapshot.getKey().toString());
                                DatabaseReference dataref3 = dataref.child(snapshot.getKey().toString());
                                dataref2.child("Live").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                        String live = snapshot1.getValue().toString();
                                        if (live.equals("true")) {
                                            dataref3.child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot11) {
                                                    if (!aList.contains(snapshot.getKey().toString())) {
                                                        String usertoken = snapshot11.getValue().toString();
                                                        dataref.child(snapshot.getKey().toString()).child("user id").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                        dataref.child(snapshot.getKey().toString()).child("phone number").setValue(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
                                                        dataref.child(snapshot.getKey().toString()).child("pickup lat").setValue(lat3);
                                                        dataref.child(snapshot.getKey().toString()).child("pickup long").setValue(long3);
                                                        dataref.child(snapshot.getKey().toString()).child("dropup lat").setValue(lat2);
                                                        dataref.child(snapshot.getKey().toString()).child("dropup long").setValue(long2);
                                                        //Toast.makeText(After_direction.this, usertoken, Toast.LENGTH_SHORT).show();
                                                        sendNotifications(usertoken, "hello", "user id");
                                                        aList.add(snapshot.getKey().toString());
                                                    }

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                            t=1;
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        FirebaseDatabase.getInstance().getReference().child("Tokens").child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Alloted").setValue("true");
                        FirebaseDatabase.getInstance().getReference().child("Tokens").child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Accepted").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.getValue().toString().equals("false")){
                                    linearLayout2.setVisibility(View.GONE);
                                    linearLayout3.setVisibility(View.VISIBLE);
                                }
                                else {

                                    linearLayout2.setVisibility(View.GONE);
                                    linearLayout.setVisibility(View.VISIBLE);
                                    Handler handler1 = new Handler();
                                    handler1.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            FirebaseDatabase.getInstance().getReference().child("Tokens").child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Vehcile no").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    vehcile=snapshot.getValue().toString();
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                            FirebaseDatabase.getInstance().getReference().child("Tokens").child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    name=snapshot.getValue().toString();
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                            FirebaseDatabase.getInstance().getReference().child("Tokens").child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("phone no").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    phno=snapshot.getValue().toString();
                                                    Intent intent = new Intent(getActivity(), Direction.class);
                                                    Bundle b = new Bundle();
                                                    b.putDouble("lat2", lat2);
                                                    b.putDouble("long2", long2);
                                                    b.putDouble("lat3",lat1);
                                                    b.putDouble("fare",fare);
                                                    b.putDouble("long3",long1);
                                                    b.putString("otp",otp);
                                                    b.putString("vehcile",vehcile);
                                                    b.putString("name",name);
                                                    b.putString("phone_no",phno);
                                                    b.putString("placename",placename2);
                                                    intent.putExtras(b);
                                                    startActivity(intent);
                                                    getActivity().finish();;
                                                }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });

                                        }
                                    },5000);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                },30000);
            }
        });
        Button confirm=view.findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(marker1==null || marker2== null){
                    Toast.makeText(getContext(),"location Field Empty",Toast.LENGTH_SHORT).show();
                }
                else{
                    tb=1;
                    getDirections();
                    appCompatButton.setVisibility(View.INVISIBLE);
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(
                            lat3, long3)).zoom(12).tilt(0).build();
                    mapmyIndiaMaps.setCameraPosition(cameraPosition);
                }
            }
        });
        autoSuggestText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                autoSuggestText1.setVisibility(View.INVISIBLE);
                appCompatButton.setVisibility(View.VISIBLE);
                directionDetailsLayout.setVisibility(View.GONE);
                recyclerView1.setVisibility(View.GONE);
                tp=0;
                tb1=0;
                return false;
            }
        });
        autoSuggestText1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tp=0;
                tb1=1;
                appCompatButton.setVisibility(View.VISIBLE);
                directionDetailsLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                return false;
            }
        });

       return view;
    }
    private void initListeners() {
        autoSuggestText.addTextChangedListener(this);
        autoSuggestText.setOnEditorActionListener(this);
        autoSuggestText1.addTextChangedListener(this);
        autoSuggestText1.setOnEditorActionListener(this);
    }

    private void initReferences(View view) {
        iv1=(ImageView)view.findViewById(R.id.iv);
        iv1.setVisibility(View.INVISIBLE);
        appCompatButton=view.findViewById(R.id.confirm);
        autoSuggestText = view.findViewById(R.id.auto_suggest);
        recyclerView = view.findViewById(R.id.recyclerview);
        autoSuggestText1 =view.findViewById(R.id.auto_suggest1);
        recyclerView1 = view.findViewById(R.id.recyclerview1);
        mLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setVisibility(View.GONE);
        recyclerView1.setLayoutManager(linearLayoutManager);
        recyclerView1.setVisibility(View.GONE);
        directionDetailsLayout = view.findViewById(R.id.Distance_direction);
        tvfare=view.findViewById(R.id.estimated_fare);
        tvDistance = view.findViewById(R.id.Distance);
        tvDuration =view.findViewById(R.id.duration);
        tvplacename=view.findViewById(R.id.pl_name);
        transparentProgressDialog = new TransparentProgressDialog(getActivity(), R.drawable.circle_loader, "");
        transparentProgressDialog.show();
        handler = new Handler();
    }

    @Override
    public void onMapReady(MapboxMap mapmyIndiaMaps) {
        this.mapmyIndiaMaps = mapmyIndiaMaps;
        lineManager = new LineManager(mapView, mapmyIndiaMaps, null, new GeoJsonOptions().withLineMetrics(true).withBuffer(2));
        enableLocation();
    }

    @Override
    public void onMapError(int i, String s) {

    }

    private void enableLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            return;
        }
        LocationComponentOptions options = LocationComponentOptions.builder(getActivity())
                .trackingGesturesManagement(true)
                .accuracyColor(ContextCompat.getColor(getActivity(), R.color.teal_200))
                .foregroundDrawable(R.drawable.location_pointer)
                .build();
        locationComponent = mapmyIndiaMaps.getLocationComponent();
        locationComponent.activateLocationComponent(getActivity(), options);
        locationComponent.setLocationComponentEnabled(true);
        locationEngine = locationComponent.getLocationEngine();
        locationEngine.addLocationEngineListener(this);
        locationComponent.setCameraMode(CameraMode.TRACKING);
        locationComponent.setRenderMode(RenderMode.COMPASS);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableLocation();

                } else {
                    iv1.setVisibility(View.VISIBLE);
                    mapView.setVisibility(View.INVISIBLE);

                }
                return;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        if (locationEngine != null) {
            locationEngine.removeLocationEngineListener(this);
            locationEngine.addLocationEngineListener(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        if (locationEngine != null)
            locationEngine.removeLocationEngineListener(this);
    }


    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
        if (locationEngine != null) {
            locationEngine.removeLocationEngineListener(this);
            locationEngine.removeLocationUpdates();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (locationEngine != null) {
            locationEngine.deactivate();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onConnected() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    getActivity(), // Activity
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            return;
        }
        locationEngine.requestLocationUpdates();

    }
    private String getFormattedDistance(double distance) {
        fare=(distance/1000)*10;
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

    @Override
    public void onLocationChanged(Location location) {
        if(tb!=1){
            mapmyIndiaMaps.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16));
            transparentProgressDialog.hide();
        }
        lat1=location.getLatitude();
        long1=location.getLongitude();

        vt=1;
    }
    public void selectedPlace(ELocation eLocation) {
        String add = "Latitude: " + eLocation.latitude + " longitude: " + eLocation.longitude;
        tp=1;
        if(tb1!=1){
            autoSuggestText.setText(eLocation.placeName);
            autoSuggestText.clearFocus();
            InputMethodManager in = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(autoSuggestText.getWindowToken(), 0);
            addMarker1(Double.parseDouble(eLocation.latitude), Double.parseDouble(eLocation.longitude) , eLocation.placeName);
            showToast(add);
        }
        else
        {
            autoSuggestText1.setText(eLocation.placeName);
            autoSuggestText1.clearFocus();
            InputMethodManager in = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(autoSuggestText1.getWindowToken(), 0);
            addMarker2(Double.parseDouble(eLocation.latitude), Double.parseDouble(eLocation.longitude) , eLocation.placeName);
            showToast(add);
        }
    }



    private void addMarker1(double latitude, double longitude , String placename) {
        if(marker1!=null){
            mapmyIndiaMaps.removeMarker(marker1);
        }
        if(lineManager != null) {
            lineManager.clearAll();
        }
        lat3=latitude;
        long3=longitude;
        tb=1;
        placename1=placename;
        LatLng latLng=new LatLng(latitude,longitude);
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).icon(IconFactory.getInstance(getActivity()).fromResource(R.drawable.placeholder));
        markerOptions.setTitle(placename);
        marker1 = mapmyIndiaMaps.addMarker(markerOptions);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(
                latitude, longitude)).zoom(16).tilt(0).build();
        mapmyIndiaMaps.setCameraPosition(cameraPosition);
    }
    private void addMarker2(double latitude, double longitude , String placename) {
        if(marker2!=null){
            mapmyIndiaMaps.removeMarker(marker1);
        }
        if(lineManager != null) {
            lineManager.clearAll();
        }
        lat2=latitude;
        long2=longitude;
        tb=1;
        placename2=placename;
        LatLng latLng=new LatLng(latitude,longitude);
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).icon(IconFactory.getInstance(getActivity()).fromResource(R.drawable.placeholder));
        markerOptions.setTitle(placename);
        marker2 = mapmyIndiaMaps.addMarker(markerOptions);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(
                latitude, longitude)).zoom(16).tilt(0).build();
        mapmyIndiaMaps.setCameraPosition(cameraPosition);
    }
    private void callTextSearchApi(String searchString){
        MapmyIndiaTextSearch.builder()
                .query(searchString)
                .build().enqueueCall(new Callback<AutoSuggestAtlasResponse>() {
            @Override
            public void onResponse(Call<AutoSuggestAtlasResponse> call, Response<AutoSuggestAtlasResponse> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        ArrayList<ELocation> suggestedList = response.body().getSuggestedLocations();
                        if (suggestedList.size() > 0) {
                            if(tb1!=1) {
                                recyclerView.setVisibility(View.VISIBLE);
                                AutoSuggestAdapter autoSuggestAdapter = new AutoSuggestAdapter(suggestedList, eLocation -> {
                                    selectedPlace(eLocation);
                                    recyclerView.setVisibility(View.GONE);
                                    autoSuggestText1.setVisibility(View.VISIBLE);
                                });
                                recyclerView.setAdapter(autoSuggestAdapter);
                            }
                            else{
                                recyclerView1.setVisibility(View.VISIBLE);
                                AutoSuggestAdapter autoSuggestAdapter = new AutoSuggestAdapter(suggestedList, eLocation -> {
                                    selectedPlace(eLocation);
                                    recyclerView1.setVisibility(View.GONE);
                                    tb1=0;
                                });
                                recyclerView1.setAdapter(autoSuggestAdapter);
                            }
                        }
                    } else {
                        showToast("Not able to get value, Try again.");
                    }
                }
            }
            @Override
            public void onFailure(Call<AutoSuggestAtlasResponse> call, Throwable t) {
                showToast(t.toString());
            }
        });
    }
    private void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        handler.postDelayed(() -> {
            if(tb1!=1){
                recyclerView.setVisibility(View.GONE);
                if (s.length() < 3)
                    recyclerView.setAdapter(null);

                if (s != null && s.toString().trim().length() < 2) {
                    recyclerView.setAdapter(null);
                    return;
                }
                if (s.length() > 3 && tp==1)
                    recyclerView.setAdapter(null);

                if (s.length() > 2 && tp==0) {
                    if (CheckInternet.isNetworkAvailable(getContext())) {
                        callTextSearchApi(s.toString());
                    } else {
                        showToast(getString(R.string.pleaseCheckInternetConnection));
                    }
                }
            }
            else if(tb1==1){
                recyclerView1.setVisibility(View.GONE);
                if (s.length() < 3)
                    recyclerView1.setAdapter(null);

                if (s != null && s.toString().trim().length() < 2) {
                    recyclerView1.setAdapter(null);
                    return;
                }
                if (s.length() > 3 && tp==1)
                    recyclerView1.setAdapter(null);

                if (s.length() > 2 && tp==0) {
                    if (CheckInternet.isNetworkAvailable(getContext())) {
                        callTextSearchApi(s.toString());
                    } else {
                        showToast(getString(R.string.pleaseCheckInternetConnection));
                    }
                }
            }
        }, 3);

    }

    @Override
    public void afterTextChanged(Editable s) {
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId== EditorInfo.IME_ACTION_SEARCH){
            if(tb1!=1){
                callTextSearchApi(v.getText().toString());
                autoSuggestText.clearFocus();
                InputMethodManager in = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(autoSuggestText.getWindowToken(), 0);
                return true;
            }
            else{
                callTextSearchApi(v.getText().toString());
                autoSuggestText1.clearFocus();
                InputMethodManager in = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(autoSuggestText1.getWindowToken(), 0);
                return true;
            }
        }
        return false;
    }

   /* public void hide1(View view) {
        recyclerView.setVisibility(View.INVISIBLE);
    }


    public void current(View view) {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(
                lat1, long1)).zoom(16).tilt(0).build();
        mapmyIndiaMaps.setCameraPosition(cameraPosition);
    }
    public void current1(View view) {
        autoSuggestText1.setVisibility(View.VISIBLE);
        reverseGeocode(lat1,long1);
        tp=1;
        autoSuggestText.clearFocus();
        InputMethodManager in = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(autoSuggestText.getWindowToken(), 0);
        addMarker1(lat1,long1,"Current Location");
        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(
                lat1, long1)).zoom(16).tilt(0).build();
        mapmyIndiaMaps.setCameraPosition(cameraPosition);
    }

    public void show_path(View view) {
        if(marker1==null || marker2== null){
            Toast.makeText(getContext(),"location Field Empty",Toast.LENGTH_SHORT).show();
        }
        else{
            tb=1;
            getDirections();
            appCompatButton.setVisibility(View.INVISIBLE);
            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(
                    lat3, long3)).zoom(12).tilt(0).build();
            mapmyIndiaMaps.setCameraPosition(cameraPosition);
        }

    }

    */
    private void getDirections() {
        //progressDialogShow();
        MapmyIndiaDirections.builder()
                .origin(Point.fromLngLat(long3, lat3))
                .destination(Point.fromLngLat(long2, lat2))
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .steps(true)
                .alternatives(true)
                .overview(DirectionsCriteria.OVERVIEW_FULL).build().enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(@NonNull Call<DirectionsResponse> call, @NonNull Response<DirectionsResponse> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        DirectionsResponse directionsResponse = response.body();
                        List<DirectionsRoute> results = directionsResponse.routes();

                        if (results.size() > 0) {
                            addMarker2(lat2,long2,placename2);
                            addMarker1(lat3,long3,placename1);
                            DirectionsRoute directionsRoute = results.get(0);
                            if (directionsRoute != null && directionsRoute.geometry() != null) {
                                drawPath(PolylineUtils.decode(directionsRoute.geometry(), Constants.PRECISION_6));
                                updateData(directionsRoute);
                            }
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), response.message() + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {

            }

        });
    }
    private void updateData(@NonNull DirectionsRoute directionsRoute) {
        if (directionsRoute.distance() != null && directionsRoute.distance() != null) {
            directionDetailsLayout.setVisibility(View.VISIBLE);
            tb=1;
            DecimalFormat df = new DecimalFormat("0.00");
            tvDuration.setText(getFormattedDuration(directionsRoute.duration()));
            tvDistance.setText(getFormattedDistance(directionsRoute.distance()));
            tvfare.setText(df.format(fare));
            tvplacename.setText(placename2);
        }
    }
    private void drawPath(@NonNull List<Point> waypoints) {
        ArrayList<LatLng> listOfLatLng = new ArrayList<>();
        for (Point point : waypoints) {
            listOfLatLng.add(new LatLng(point.latitude(), point.longitude()));
        }

        if(directionPolylinePlugin == null) {
            directionPolylinePlugin = new DirectionPolylinePlugin(mapmyIndiaMaps, mapView, DirectionsCriteria.PROFILE_DRIVING);
            directionPolylinePlugin.createPolyline(listOfLatLng);
        } else {
            directionPolylinePlugin.updatePolyline(DirectionsCriteria.PROFILE_DRIVING,listOfLatLng);

        }
        LatLngBounds latLngBounds = new LatLngBounds.Builder().includes(listOfLatLng).build();
    }
    private void reverseGeocode(Double latitude, Double longitude) {
        MapmyIndiaReverseGeoCode.builder()
                .setLocation(latitude, longitude)
                .build().enqueueCall(new Callback<PlaceResponse>() {
            @Override
            public void onResponse(Call<PlaceResponse> call, Response<PlaceResponse> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        List<Place> placesList = response.body().getPlaces();
                        Place place = placesList.get(0);
                        String add = place.getFormattedAddress();
                        autoSuggestText.setText(add);
                    } else {
                        Toast.makeText(getContext(), "Not able to get value, Try again.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<PlaceResponse> call, Throwable t) {
                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void sendNotifications(String usertoken, String title, String message) {
        Data data = new Data(title, message);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(getContext(), "Failed ", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }

  /*  public void Book(View view) {
        t=0;
        autoSuggestText1.setEnabled(false);
        autoSuggestText.setEnabled(false);
        directionDetailsLayout.setVisibility(View.GONE);
        linearLayout2.setVisibility(View.VISIBLE);
        aList = new ArrayList();
        otp=getRandomNumberString();
        FirebaseDatabase.getInstance().getReference().child("Tokens").child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Accepted").setValue("false");
        FirebaseDatabase.getInstance().getReference().child("Tokens").child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Alloted").setValue("false");
        FirebaseDatabase.getInstance().getReference().child("Tokens").child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("OTP").setValue(otp);
        dataref = FirebaseDatabase.getInstance().getReference().child("Tokens").child("Driver");
        dataref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (t == 0) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        DatabaseReference dataref2 = dataref.child(snapshot.getKey().toString());
                        DatabaseReference dataref3 = dataref.child(snapshot.getKey().toString());
                        dataref2.child("Live").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                String live = snapshot1.getValue().toString();
                                if (live.equals("true")) {
                                    dataref3.child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot11) {
                                            if (!aList.contains(snapshot.getKey().toString())) {
                                                String usertoken = snapshot11.getValue().toString();
                                                dataref.child(snapshot.getKey().toString()).child("user id").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                dataref.child(snapshot.getKey().toString()).child("phone number").setValue(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
                                                dataref.child(snapshot.getKey().toString()).child("pickup lat").setValue(lat3);
                                                dataref.child(snapshot.getKey().toString()).child("pickup long").setValue(long3);
                                                dataref.child(snapshot.getKey().toString()).child("dropup lat").setValue(lat2);
                                                dataref.child(snapshot.getKey().toString()).child("dropup long").setValue(long2);
                                                //Toast.makeText(After_direction.this, usertoken, Toast.LENGTH_SHORT).show();
                                                sendNotifications(usertoken, "hello", "user id");
                                                aList.add(snapshot.getKey().toString());
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    t=1;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseDatabase.getInstance().getReference().child("Tokens").child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Alloted").setValue("true");
                FirebaseDatabase.getInstance().getReference().child("Tokens").child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Accepted").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getValue().toString().equals("false")){
                            linearLayout2.setVisibility(View.GONE);
                            linearLayout3.setVisibility(View.VISIBLE);
                        }
                        else {

                            linearLayout2.setVisibility(View.GONE);
                            linearLayout.setVisibility(View.VISIBLE);
                            Handler handler1 = new Handler();
                            handler1.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    FirebaseDatabase.getInstance().getReference().child("Tokens").child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Vehcile no").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            vehcile=snapshot.getValue().toString();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    FirebaseDatabase.getInstance().getReference().child("Tokens").child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            name=snapshot.getValue().toString();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    FirebaseDatabase.getInstance().getReference().child("Tokens").child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("phone no").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            phno=snapshot.getValue().toString();
                                            Intent intent = new Intent(getActivity(), Direction.class);
                                            Bundle b = new Bundle();
                                            b.putDouble("lat2", lat2);
                                            b.putDouble("long2", long2);
                                            b.putDouble("lat3",lat1);
                                            b.putDouble("fare",fare);
                                            b.putDouble("long3",long1);
                                            b.putString("otp",otp);
                                            b.putString("vehcile",vehcile);
                                            b.putString("name",name);
                                            b.putString("phone_no",phno);
                                            b.putString("placename",placename2);
                                            intent.putExtras(b);
                                            startActivity(intent);
                                            getActivity().finish();;
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                }
                            },5000);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        },30000);
    }

   */
    public static String getRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }
}