package mx.netsquare.beerfindebeta;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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


public class MapaVenta extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;

    private static final String LOG_TAG = "Prueba: ";

    private static String BEERFAV = null;

    private final String SERVICE_URL = "http://beerfinderbeta.96.lt/webservice/get_consulta_ventas.php";

    private JSONArray ventas = null;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_venta);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Intent intent = getIntent();
        BEERFAV = intent.getStringExtra("beerfav");

        LatLngBounds Chihuahua = new LatLngBounds(new LatLng(28.6, -106.1), new LatLng(28.7, -106.15));
        if (mMap != null) {
            mMap.clear();
            new VentasTask().execute();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Chihuahua.getCenter(), 14));
        }


        // llamar eventos
        UiSettings uisettings = mMap.getUiSettings();
        uisettings.setAllGesturesEnabled(true);
        uisettings.setMyLocationButtonEnabled(true);
        mMap.setOnMarkerClickListener(this);

    }


    class VentasTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ventas = new JSONArray();

            progressDialog = new ProgressDialog(MapaVenta.this);
            progressDialog.setMessage(getString(R.string.cargando));
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
                JSONArray arr = ob.getJSONArray("ventas");


                ArrayList<Venta> lugares =
                        Ventas.parseJsonToObject(arr);


                if (lugares == null)
                    Log.e("Error", "No es nulo");


                Venta v = null;

                for (int i = 0; i < lugares.size(); i++) {

                    v = lugares.get(i);
                    Log.e("Mapa Ventas ", v.toString());

                    double lat = Double.parseDouble(v.getGm_latitud());
                    double lng = Double.parseDouble(v.getGm_longitud());
                    String match = String.valueOf(v.getBeerID());
                    LatLng latLng = new LatLng(lat, lng);

                    if (BEERFAV.equals(match)) {

                        if (i == 0)
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                        mMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.defaultMarker())
                                .title(v.getLugar())
                                .snippet(v.getDescripcion())
                                .position(latLng));
                    }

                }

            } catch (Exception e) {
                Log.e(LOG_TAG, "Error generando marcadores de espacios", e);
            }

            progressDialog.dismiss();

        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {


        return false;
    }

}
