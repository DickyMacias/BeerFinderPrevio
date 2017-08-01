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




        class MarkerTask extends AsyncTask<Void, Void, JSONObject> {

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

            int responseCode = -1;
            JSONObject resultado = null;

            try{

                URL apiURL =  new URL(SERVICE_URL);

                Log.d(TAG, apiURL.toString());

                HttpURLConnection httpConnection = (HttpURLConnection) apiURL.openConnection();
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();

                responseCode = httpConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK){

                    InputStream inputStream = httpConnection.getInputStream();
                    BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder sBuilder = new StringBuilder();

                    String line = null;
                    while ((line = bReader.readLine()) != null) {
                        sBuilder.append(line + "\n");
                    }

                    inputStream.close();

                    resultado = new JSONObject(sBuilder.toString());

                    Log.d(TAG, sBuilder.toString());

                }else{
                    Log.i(TAG, "Error en el HTTP " + responseCode);
                }
            }
            catch (JSONException e){
                manejoExcepcion(e);
            }
            catch (MalformedURLException e){manejoExcepcion(e);}
            catch (IOException e){manejoExcepcion(e);}
            catch (Exception e){
                manejoExcepcion(e);

            }

            return resultado;

            }


            @Override
            protected void onPostExecute(JSONObject place) {
                // Probar estos dos marcadores primero (poner en un lugar visible del mapa)
                LatLng lat1 = new LatLng(28.55, -106.05);
                mMap.addMarker(new MarkerOptions()
                    .title("Hola")
                    .position(lat1));
                
                LatLng lat2 = new LatLng(28.45, -106.35);
                mMap.addMarker(new MarkerOptions()
                    .title("Mundo")
                    .position(lat2));
                
                Log.e("No funciona",SERVICE_URL);
                Log.e("Error",json.toString());

                try {
                    
                    JSONArray lugares = place.getJSONArray("places");

        //            JSONObject ob = new JSONObject(json);
        //            JSONArray arr = ob.getJSONArray("marcadores");

                    // lugares = Marcadores.parseJsonToObject(json);

                    for (JSONObject lugar:lugares) {
                        // Obtener las coordenadas del lugar
				        String coord = lugar.getString("coordenadas");
                        
                        // separarlas por la coma
                        String lats[] = coord.split(",");
                        
                        Double latitud = Double.parseDouble(lats[0]);
                        Double longitud = Double.parseDouble(lats[1]);
                        
                        /*
                        o en su defecto obtenerlos directo del objeto
                        Double latitud = lugar.getDouble("lat");
                        Double longitud = lugar.getDouble("lon");
                        */
                        
                        // Crear LatLng
                        LatLng latLng = new LatLng(lat,lng);
                        
                        /*
                        double lat = Double.parseDouble(marcador.getGm_latitud());
                        double lng = Double.parseDouble(marcador.getGm_longitud());
                        LatLng latLng = new LatLng(lat,lng);*/
                        mMap.addMarker(new MarkerOptions()
                                //.icon(BitmapDescriptorFactory.defaultMarker())
                                .title("Hola Mundo") // marcador.getLugar())
                                //.snippet(marcador.getDescripcion())
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

    private void manejoExcepcion(Exception e){
        Log.e(TAG, "Exception caught: ", e);
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
