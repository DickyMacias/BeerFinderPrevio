/*Esta clase permite al usuario llamar la API de google maps, generando un map en el cual se
podran fijar los nuevos puntos que el usuario desee agregar en el mapa. Esto para que
pueda generar puntos libremente en la aplicacion y sean mandados a la BD.

Desarrrollada por Ricardo Ivan Macias Fusco y Daniel Emir Olivas Castro.
Fecha de Creacion: 16/Mayo/2017
Version 1.0(Version reciente en la clase Android Manifest)
Ultima Actualizacion: 5/Agosto/2017
*/

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

    //Se declaran variables que contendran coordenadas.
    //Se declaran como variables estaticas para que puedan se usadas en otro Activity.
    public static String LATITUD = null;
    public static String LONGITUD = null;
    public static String COORDENADAS = null;
    private GoogleMap mMap;


    //Se determina la lat y lng de un punto de referencia.
    private boolean localizado = false;
    Double miLatitud = 28.6458775;
    Double miLongitud = -106.1475035;

    //Se genera un codigo para verificar en aso de que se requieran permisos.
    //Los permisos ya estan declarados en el manifiesto.
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
        mMap.moveCamera(CameraUpdateFactory.newLatLng(utch));

        //Eventos de la interfaz grafica de usuario
        UiSettings uisettings = mMap.getUiSettings();
        uisettings.setAllGesturesEnabled(true);
        uisettings.setMyLocationButtonEnabled(true);


        //Eventos a crear
        mMap.setOnMapLongClickListener(this);

        //Se llama metodo para obtener la ubicacion actual.
        getUbicacion();

    }


    private void getUbicacion() {

        //Se realiza una revision para verificar si se cuentan con los permisos para acceder al GPS
        //de la aplicacion.
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

        //Se genera un localizador para determinar la posicion actual si los permisos se obtuvieron.
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        //Se verifican las coordenadoas con un Geocoder
        Geocoder geocoder;
        String bestProvider;
        List<Address> user = null;

        //Se revisa un criterio para en caso de que no se localicen las coordenadas.
        Criteria criteria = new Criteria();
        bestProvider = lm.getBestProvider(criteria, false);
        Location location = lm.getLastKnownLocation(bestProvider);

        //Si no se localiza se genera un punto de referencia.
        if (location == null){
            Toast.makeText(this, R.string.no_localizable,
                    Toast.LENGTH_LONG).show();
            localizado = true;
            //Se llama al mapa.
            setUpMap();
        }else{
            //Si se localiza, se generan las coordenadas.
            geocoder = new Geocoder(this);
            try {
                user = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                miLatitud     = user.get(0).getLatitude();
                miLongitud    = user.get(0).getLongitude();

                localizado = true;
                //Se llama al mapa.
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

                Toast.makeText(this, getString(R.string.sin_permisos) +
                                getString(R.string.utch_referencia)
                        , Toast.LENGTH_LONG).show();

                localizado = true;
                setUpMap();
            }
            return;
        }
    }
    }


    //Se genera metodo para que al seleccionar un punto en el mapa se cree un marcador.
    //Se genera como Long para evitar marcadores por erro.
    @Override
    public void onMapLongClick(LatLng point) {


        //Se crea marcador con la posicion seleccionada.
        mMap.addMarker(new MarkerOptions().position(point).title(point.toString()));

        //Convert LatLng to Location
        Location location = new Location("Test");
        location.setLatitude(point.latitude);
        location.setLongitude(point.longitude);

        //Convert Location to LatLng
        LatLng newLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        //Se crea marcador.
        MarkerOptions marker = new MarkerOptions()
                .position(newLatLng)
                .title(newLatLng.toString());

        mMap.addMarker(marker);

        //Se pasan las coordenadas a las variables para enviarlas a una nueva actividad.
        COORDENADAS = newLatLng.toString();
        LATITUD = String.valueOf(point.latitude);
        LONGITUD = String.valueOf(point.longitude);

        //Se envian variables con un PutExtra y se genera una llave para recibirlas en la
        //otra actividad.
        Intent intent = new Intent(this, Agregando.class);
        intent.putExtra("lat", LATITUD);
        Log.e("Cual es la latitud: ", LATITUD);
        intent.putExtra("lon", LONGITUD);
        Log.e("Cual es la longitud: ", LONGITUD);
        intent.putExtra(COORDENADAS, newLatLng.toString());
        startActivity(intent);

        //Se termina el activity una vez que se creo la nueva actividad.
        finish();
    }
}

