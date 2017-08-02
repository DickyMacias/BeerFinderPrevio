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
import java.util.ArrayList;
import java.util.List;

import static com.loopj.android.http.AsyncHttpClient.LOG_TAG;

public class MapaGeneral extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener {

    //public static List<Marcador> lugares;

    private JSONParser jsonParser = null; //Objeto conexion webservice

    private GoogleMap mMap;

    private static final String TAG_PLACES   = "marcadores";

    private static final String LOG_TAG = "Prueba: ";

    private final String SERVICE_URL = "http://192.168.56.1:9090/webservice/get_all_markers.php";
    private final String SERVICE_URL2 = "http://192.168.56.1:9090/webservice/get_all_negocios.php";


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

        LatLngBounds Chihuahua = new LatLngBounds(new LatLng(28.6, -106.1), new LatLng(28.7, -106.15));
        if (mMap != null) {
            mMap.clear();
            new MarkerTask().execute();
            new NegocioTask().execute();
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Chihuahua.getCenter(), 15));
        }

        mMap.setMaxZoomPreference(15.0f);

        UiSettings uisettings = mMap.getUiSettings();
        uisettings.setAllGesturesEnabled(true);
        uisettings.setMyLocationButtonEnabled(true);

        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerClickListener(this);



        //Agregar sitios en el mapa para primer referencia y posicion

    }

        class MarkerTask extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                marcadores = new JSONArray();

                progressDialog = new ProgressDialog(MapaGeneral.this);
                progressDialog.setMessage("Cargando...");
                progressDialog.setCancelable(false);
                progressDialog.show();

            }
            // Invoked by execute() method of this object
            @Override
            protected String doInBackground(Void... args) {

                HttpURLConnection conn = null;
                final StringBuilder json = new StringBuilder();
                try {
                    // Conectando con el web service
                    URL url = new URL(SERVICE_URL);
                    conn = (HttpURLConnection) url.openConnection();
                    InputStreamReader in = new InputStreamReader(conn.getInputStream());

                    // Leer informacion del Json en el Builder
                    int read;
                    char[] buff = new char[1024];
                    while ((read = in.read(buff)) != -1) {
                        json.append(buff, 0, read);
                    }

                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error connecting to service", e);

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

                    JSONObject ob = new JSONObject(json);
                    JSONArray arr = ob.getJSONArray("marcadores");


                    ArrayList<Marcador> lugares =
                            Marcadores.parseJsonToObject(arr);


                    if (lugares == null)
                        Log.e("Error", "No es nulo");


                    Marcador m = null;
                    //for(Marcador m : lugares){
                    for (int i = 0; i < lugares.size(); i++) {

                        m = lugares.get(i);

                        Log.e("Mapa General ", m.toString());

                        double lat = Double.parseDouble(m.getGm_latitud());
                        double lng = Double.parseDouble(m.getGm_longitud());
                        LatLng latLng = new LatLng(lat, lng);


                        if (i == 0)
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));


                            mMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.defaultMarker())
                                .title(m.getLugar())
                                .snippet(m.getDescripcion())
                                .position(latLng));

                    }


                } catch (Exception e) {
                    Log.e(LOG_TAG, "Error generando marcadores de espacios", e);
                }

                progressDialog.dismiss();

            }



        }


    class NegocioTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            marcadores = new JSONArray();

/*            progressDialog = new ProgressDialog(MapaGeneral.this);
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
                // Conectando con el web service
                URL url = new URL(SERVICE_URL2);
                conn = (HttpURLConnection) url.openConnection();
                InputStreamReader in = new InputStreamReader(conn.getInputStream());

                // Leer informacion del Json en el Builder
                int read;
                char[] buff = new char[1024];
                while ((read = in.read(buff)) != -1) {
                    json.append(buff, 0, read);
                }

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error connecting to service", e);

            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }

            return json.toString();

        }


        @Override
        protected void onPostExecute(String json) {

            Log.e("No funciona",SERVICE_URL2);
            Log.e("Error",json.toString());

            try {

                JSONObject ob = new JSONObject(json);
                JSONArray arr = ob.getJSONArray("marcadores");


                ArrayList<Marcador> lugares =
                        Marcadores.parseJsonToObject(arr);


                if (lugares == null)
                    Log.e("Error", "No es nulo");


                Marcador m = null;
                //for(Marcador m : lugares){
                for (int i = 0; i < lugares.size(); i++) {

                    m = lugares.get(i);

                    Log.e("Mapa General ", m.toString());

                    double lat = Double.parseDouble(m.getGm_latitud());
                    double lng = Double.parseDouble(m.getGm_longitud());
                    LatLng latLng = new LatLng(lat, lng);


                    if (i == 0)
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));


                    mMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                            .title(m.getLugar())
                            .snippet(m.getDescripcion())
                            .position(latLng));

                }


            } catch (Exception e) {
                Log.e(LOG_TAG, "Error generando marcadores de espacios", e);
            }

            //progressDialog.dismiss();

        }



    }


    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        

        return false;
    }

    public void AgregaLugar(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}
