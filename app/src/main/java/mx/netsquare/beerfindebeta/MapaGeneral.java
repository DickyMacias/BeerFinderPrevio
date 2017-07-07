package mx.netsquare.beerfindebeta;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapaGeneral extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_general);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLngBounds Chihuahua = new LatLngBounds(new LatLng(28.6, -106.1), new LatLng(28.7, -106.15));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Chihuahua.getCenter(), 15));

        mMap.setMaxZoomPreference(10.0f);

        UiSettings uisettings = mMap.getUiSettings();
        uisettings.setAllGesturesEnabled(true);
        uisettings.setMyLocationButtonEnabled(true);

        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerClickListener(this);



        // Agregar sitios en el mapa para primer referencia y posicion
        LatLng utch = new LatLng(28.6458775, -106.1475035);
        mMap.addMarker(new MarkerOptions().position(utch).title("UTCH"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(utch));


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(false);

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        

        return false;
    }


    @Override
    public void onMapLongClick(LatLng latLng) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }
}
