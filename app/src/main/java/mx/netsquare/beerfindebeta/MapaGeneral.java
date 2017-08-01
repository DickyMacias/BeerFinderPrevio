package mx.netsquare.beerfindebeta;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static com.loopj.android.http.AsyncHttpClient.LOG_TAG;

public class MapaGeneral extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    public static List<Marcador> lugares;

    private JSONParser jsonParser = null; //Objeto conexion webservice

    private GoogleMap mMap;

    private static final String TAG_PLACES   = "marcadores";

    private static final String LOG_TAG = "Prueba: ";

    private final String SERVICE_URL = "http://192.168.56.1:9090/webservice/get_all_markers.php";

    private JSONArray marcadores = null;
    private ProgressDialog progressDialog;


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

        if (mMap != null) {
            mMap.clear();
            new MarkerTask().execute();
        }

        LatLngBounds Chihuahua = new LatLngBounds(new LatLng(28.6, -106.1), new LatLng(28.7, -106.15));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Chihuahua.getCenter(), 15));

//        mMap.setMaxZoomPreference(10.0f);

        UiSettings uisettings = mMap.getUiSettings();
        uisettings.setAllGesturesEnabled(true);
        uisettings.setMyLocationButtonEnabled(true);

        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMapClickListener(this);
//        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerClickListener(this);


        // Agregar sitios en el mapa para primer referencia y posicion
//        LatLng utch = new LatLng(28.6458775, -106.1475035);
//        mMap.addMarker(new MarkerOptions().position(utch).title("UTCH"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(utch));


//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {

            return;
        }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }
//        mMap.setMyLocationEnabled(true);


//      }




        class MarkerTask extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                marcadores = new JSONArray();

                /*progressDialog = new ProgressDialog(MapaGeneral.this);
                progressDialog.setMessage("Cargando...");
                progressDialog.setCancelable(false);
                progressDialog.show();*/
            }
            // Invoked by execute() method of this object
            @Override
            protected String doInBackground(Void... args) {



                HttpURLConnection conn = null;
                final StringBuilder json = new StringBuilder();
                try {
                    // Connect to the web service
                    URL url = new URL(SERVICE_URL);
                    conn = (HttpURLConnection) url.openConnection();
                    InputStreamReader in = new InputStreamReader(conn.getInputStream());

                    // Read the JSON data into the StringBuilder
                    int read;
                    char[] buff = new char[1024];
                    while ((read = in.read(buff)) != -1) {
                        json.append(buff, 0, read);
                    }

                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error connecting to service", e);
                    //throw new IOException("Error connecting to service", e); //uncaught
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }

                return json.toString();

            }


            @Override
            protected void onPostExecute(String json) {

                Log.e("No funciona",SERVICE_URL);
                Log.e("Error",json.toString());

                try {

        //            JSONObject ob = new JSONObject(json);
        //            JSONArray arr = ob.getJSONArray("marcadores");

                    lugares = Marcadores.parseJsonToObject(json);



                    for (Marcador marcador:lugares) {
                        double lat = Double.parseDouble(marcador.getGm_latitud());
                        double lng = Double.parseDouble(marcador.getGm_longitud());
                        LatLng latLng = new LatLng(lat,lng);
                        mMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.defaultMarker())
                                .title(marcador.getLugar())
                                .snippet(marcador.getDescripcion())
                                .position(latLng));
                        System.out.println(latLng + " " + marcador.getLugar() + " " + marcador.getDescripcion());

                    }

                    LatLng utch = new LatLng(28.6458775, -106.1475035);
                    mMap.addMarker(new MarkerOptions().position(utch).title("UTCH"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(utch));

                } catch (Exception e) {
                    Log.e(LOG_TAG, "Error generando marcadores de espacios", e);
                }

            }


        }



    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        

        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    public void AgregaLugar(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}
