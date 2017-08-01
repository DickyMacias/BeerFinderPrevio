package mx.netsquare.beerfindebeta;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import com.google.android.gms.common.api.BooleanResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;




public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    public final static String LATITUD = null;
    public final static String LONGITUD = null;
    public static String COORDENADAS = null;
    private GoogleMap mMap;

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
        mMap.addMarker(new MarkerOptions().position(utch).title("UTCH"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(utch));


        mMap.getUiSettings().setAllGesturesEnabled(true);


        //Eventos a crear
        mMap.setOnInfoWindowClickListener(this);

        mMap.setOnMapClickListener(this);

        mMap.setOnMarkerClickListener(this);


    }


    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public boolean onMarkerClick (Marker marker){

            Intent intent = new Intent(this, Agregando.class);
            intent.putExtra(LATITUD, marker.getPosition().latitude);
        Toast.makeText(this, LATITUD + "ese no " + LONGITUD , Toast.LENGTH_SHORT).show();
            intent.putExtra(LONGITUD, marker.getPosition().longitude);

            startActivity(intent);

        return false;
    }




    @Override
    public void onMapClick(LatLng point) {

        mMap.addMarker(new MarkerOptions().position(point).title(point.toString()));

        //The code below demonstrate how to convert between LatLng and Location

        //Convert LatLng to Location
        Location location = new Location("Test");
        location.setLatitude(point.latitude);
        location.setLongitude(point.longitude);
        //location.setTime(new Date().getTime()); //Set time as current Date
        //info.setText(location.toString());

        //Convert Location to LatLng
        LatLng newLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions marker = new MarkerOptions()
                .position(newLatLng)
                .title(newLatLng.toString());

        mMap.addMarker(marker);
        COORDENADAS = newLatLng.toString();



/*        MarkerOptions marker = new MarkerOptions().position(

                new LatLng(point.latitude, point.longitude)) //Strings del fragmento se guardan en base de datos y de BD se traen con un select para almacenarlos en variable
                    .title("New Marker")
                    .snippet("Descripcion")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.corona));



            mMap.addMarker(marker);*/

        Intent intent = new Intent(this, Agregando.class);
        intent.putExtra(LATITUD, marker.getPosition().latitude);
        intent.putExtra(LONGITUD, marker.getPosition().longitude);
        intent.putExtra(COORDENADAS, newLatLng.toString());
        startActivity(intent);



        }

    }

