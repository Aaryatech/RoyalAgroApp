package com.ats.royalagro.activity;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ats.royalagro.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EditText edAddress;
    private ImageView ivSearch;
    Marker m;
    public static String myLatLong;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        edAddress = findViewById(R.id.edMapAddress);
        ivSearch = findViewById(R.id.ivMapSearch);

        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("onClick", "-------------------------");
//                Intent searchAddress = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + edAddress.getText().toString()));
//                startActivity(searchAddress);
                getLocationFromAddress(MapsActivity.this, edAddress.getText().toString());
            }
        });

    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 1);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
            // Log.e("Location : ", "--- " + p1);

            // mMap.addMarker(new MarkerOptions().position(p1));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(p1, 14.0f));


        } catch (IOException ex) {
            ex.printStackTrace();
            Toast.makeText(this, "Unable To Find Address", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Unable To Find Address", Toast.LENGTH_SHORT).show();
        }

        return p1;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng sydney = new LatLng(19.997453, 73.789802);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10.0f));


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (m != null) {
                    m.remove();
                }
                m = mMap.addMarker(new MarkerOptions().position(latLng).draggable(true).visible(true));
                Toast.makeText(MapsActivity.this, "" + latLng, Toast.LENGTH_SHORT).show();

                String lat = "" + latLng.latitude;
                String longi = "" + latLng.longitude;

                myLatLong = lat + "," + longi;


            }
        });

    }

}
