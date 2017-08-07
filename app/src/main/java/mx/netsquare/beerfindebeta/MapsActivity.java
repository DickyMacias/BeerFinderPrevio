package mx.netsquare.beerfindebeta;

import android.Manifest;
import android.app.AutomaticZenRule;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import com.google.android.gms.common.api.BooleanResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    public static String LATITUD = null;
    public static String LONGITUD = null;
    public static String COORDENADAS = null;
    private GoogleMap mMap;


    private boolean localizado = false;
    Double miLatitud = 28.6458775;
    Double miLongitud = -106.1475035;

    private static  final int MY_PERMIT_FINE_LOCATION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.layout_map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        //Delimitar el enfoque de la pantalla
        LatLngBounds Chihuahua = new LatLngBounds(new LatLng(28.6, -106.1), new LatLng(28.7, -106.15));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Chihuahua.getCenter(), 12));



        //Crear Consulta para llamar puntos

        LatLng utch = new LatLng(28.6458775, -106.1475035);
    //    mMap.addMarker(new MarkerOptions().position(utch).title("UTCH"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(utch));


        UiSettings uisettings = mMap.getUiSettings();
        uisettings.setAllGesturesEnabled(true);
        uisettings.setMyLocationButtonEnabled(true);


        //Eventos a crear
        mMap.setOnMapLongClickListener(this);

        getUbicacion();


    }

    private void getUbicacion() {

        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION )
                        != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION },
                    MY_PERMIT_FINE_LOCATION);
            return;
        }

        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Geocoder geocoder;
        String bestProvider;
        List<Address> user = null;

        Criteria criteria = new Criteria();
        bestProvider = lm.getBestProvider(criteria, false);
        Location location = lm.getLastKnownLocation(bestProvider);

        if (location == null){
            Toast.makeText(this,"No pudimos localizarte, utilizaremos UTCH como referencia",
                    Toast.LENGTH_LONG).show();
            localizado = true;
            setUpMap();
        }else{
            geocoder = new Geocoder(this);
            try {
                user = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                miLatitud     = user.get(0).getLatitude();
                miLongitud    = user.get(0).getLongitude();

                localizado = true;
                setUpMap();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void setUpMap() {
        if (localizado) {
            if (mMap != null) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(miLatitud, miLongitud), 13));
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(miLatitud, miLongitud))
                        .title("Aqui estoy")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
        case MY_PERMIT_FINE_LOCATION: {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                getUbicacion();

            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.

                Toast.makeText(this, "La app no tiene permiso de usar tu ubicación se utilizará " +
                                "UTCH como referencia"
                        , Toast.LENGTH_LONG).show();

                localizado = true;
                setUpMap();
            }
            return;
        }
    }
    }


    @Override
    public void onMapLongClick(LatLng point) {


        mMap.addMarker(new MarkerOptions().position(point).title(point.toString()));

        //The code below demonstrate how to convert between LatLng and Location

        //Convert LatLng to Location
        Location location = new Location("Test");
        location.setLatitude(point.latitude);
        location.setLongitude(point.longitude);

        //Convert Location to LatLng
        LatLng newLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions marker = new MarkerOptions()
                .position(newLatLng)
                .title(newLatLng.toString());


        mMap.addMarker(marker);

        COORDENADAS = newLatLng.toString();
        LATITUD = String.valueOf(point.latitude);
        LONGITUD = String.valueOf(point.longitude);

        Intent intent = new Intent(this, Agregando.class);
        intent.putExtra("lat", LATITUD);
        Log.e("Cual es la latitud: ", LATITUD);
        intent.putExtra("lon", LONGITUD);
        Log.e("Cual es la longitud: ", LONGITUD);
        intent.putExtra(COORDENADAS, newLatLng.toString());
        startActivity(intent);
        finish();
    }
}

