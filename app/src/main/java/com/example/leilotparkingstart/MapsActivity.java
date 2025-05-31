package com.example.leilotparkingstart;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.leilotparkingstart.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private Marker currentMarker;  // Αποθήκευση του τρέχοντος marker
    private Button startButton;    // Το κουμπί "Έναρξη"

    private Button listButton;

    private Button walletButton;
    private Button statsButton;

    private HashMap<Marker, ParkingSpot> markerSpotMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Αρχικοποίηση του κουμπιού "Έναρξη"
        startButton = findViewById(R.id.start_parking_button);
        startButton.setVisibility(View.GONE);  // Κρύβουμε το κουμπί στην αρχή

        listButton = findViewById(R.id.showListButton);
        currentMarker=null;

        walletButton = findViewById(R.id.wallet_button);
        statsButton = findViewById((R.id.stats_button));

        startButton.setOnClickListener(v -> {
            if (currentMarker != null && markerSpotMap.containsKey(currentMarker)) {
                ParkingSpot selectedSpot = markerSpotMap.get(currentMarker);
                Log.d("Parking", "Ξεκινά στάθμευση για: " + selectedSpot.getName());

                Intent intent = new Intent(MapsActivity.this, FormActivity.class);
                intent.putExtra("selectedSpot", selectedSpot);
                startActivity(intent);
            }
        });

        walletButton.setOnClickListener(v -> {
                Intent intent = new Intent(MapsActivity.this, WalletMain.class);
                startActivity(intent);

        });

        statsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MapsActivity.this, StatsActivity.class);
            startActivity(intent);

        });



    }




    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setTrafficEnabled(true);



        LatLngBounds waikikiBounds = new LatLngBounds(
                new LatLng(21.2710, -157.8400),  // Southwest (κάτω αριστερά)
                new LatLng(21.2950, -157.8000)   // Northeast (πάνω δεξιά)
        );

        LatLng waikikiCenter = new LatLng(21.2810, -157.8350);

        mMap.setLatLngBoundsForCameraTarget(waikikiBounds);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(waikikiCenter, 14.5f));


        Polygon polygon = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(21.288728, -157.834026),
                        new LatLng(21.274078, -157.816902),
                        new LatLng(21.273726, -157.816390),
                        new LatLng(21.273132, -157.816865),
                        new LatLng(21.272093, -157.819408),
                        new LatLng(21.271619, -157.821245),
                        new LatLng(21.271470, -157.822848),
                        new LatLng(21.274836, -157.824654),
                        new LatLng(21.275704, -157.825600),
                        new LatLng(21.277924, -157.833525),
                        new LatLng(21.277924, -157.833525),
                        new LatLng(21.281402, -157.841745),
                        new LatLng(21.287210, -157.841143)
                )
                .strokeColor(Color.parseColor("#E86E58"))
                .fillColor(Color.TRANSPARENT)
                .strokeWidth(3)
        );

        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(this));

        ParkingDatabaseHelper dbHelper = new ParkingDatabaseHelper(this);
        List<ParkingSpot> spots = dbHelper.getAllSpots();

        for (ParkingSpot spot : spots) {
            LatLng location = new LatLng(spot.getLat(), spot.getLng());

            MarkerOptions markerOptions = new MarkerOptions()
                    .position(location)
                    .title(spot.getName())
                    .snippet("Τιμή: $" + spot.getPricePerHour() + " | " + spot.getNotes())
                    .icon(BitmapDescriptorFactory.fromResource(
                            spot.isAvailable() ? R.drawable.available5 : R.drawable.unavailable5
                    ));

           Marker marker = mMap.addMarker(markerOptions);
            markerSpotMap.put(marker, spot);
        }


        // Ορίστε τον OnMapClickListener
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                // Κλείνουμε το info window αν υπάρχει
                if (currentMarker != null) {
                    currentMarker.hideInfoWindow();
                }

                // Κρύβουμε το κουμπί "Έναρξη" αν υπάρχει
                if (startButton != null) {
                    startButton.setVisibility(View.GONE);
                }
            }
        });

        // Ορίστε τον OnMarkerClickListener
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // Εμφάνιση του info window όταν γίνεται κλικ στον marker
                if (!marker.isInfoWindowShown()) {
                    marker.showInfoWindow();
                    currentMarker = marker;  // Αποθήκευση του marker για να το κλείσουμε αργότερα
                } else {
                    marker.hideInfoWindow();
                    currentMarker = null;  // Μηδενίζουμε το currentMarker αν το κλείσουμε
                }

                // Εμφάνιση του κουμπιού "Έναρξη"
                if (startButton != null) {
                    ParkingSpot selectedSpot = markerSpotMap.get(currentMarker);

                    Balance.init(getApplicationContext());
                    Balance.getInstance().loadUserBalance();

                    if (selectedSpot.isAvailable() & (Balance.getInstance().getBalance()>0))
                    startButton.setVisibility(View.VISIBLE);
                    else startButton.setVisibility(View.GONE);
                    if (Balance.getInstance().getBalance()<=0)
                        Toast.makeText(getApplicationContext(), "Insufficient funds", Toast.LENGTH_SHORT).show();
                }

                return true;  // Προλαμβάνουμε την εμφάνιση του default info window
            }
        });

        // Όταν το κουμπί πατηθεί, ανοίγουμε την Activity με τη λίστα
        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, list_activity.class);
                startActivity(intent);
            }
        });






        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
       // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        private final View mWindow;
        private final Context mContext;

        public CustomInfoWindowAdapter(Context context) {
            mContext = context;
            mWindow = LayoutInflater.from(context).inflate(R.layout.info_window, null);
        }

        private void renderWindowText(Marker marker, View view){
            String title = marker.getTitle();
            TextView titleUi = view.findViewById(R.id.title);

            if (!title.equals("")) {
                titleUi.setText(title);
            }

            String snippet = marker.getSnippet();
            TextView priceUi = view.findViewById(R.id.price);
            TextView availabilityUi = view.findViewById(R.id.availability);

            if (snippet != null) {
                // Χωρίζουμε το snippet που βάλαμε στο Marker (τιμή και διαθεσιμότητα)
                String[] parts = snippet.split("\\|");

                if (parts.length > 0) {
                    priceUi.setText(parts[0].trim());
                }
                if (parts.length > 1) {
                    availabilityUi.setText(parts[1].trim());
                }
            }
        }

        @Override
        public View getInfoWindow(Marker marker) {
            renderWindowText(marker, mWindow);
            return mWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {
            renderWindowText(marker, mWindow);
            return mWindow;
        }
    }


}

