/*Esta clase permite al usuario llamar la API de google maps, generando un map en el cual se
desplegaran los diferentes marcadores geenerados a partir de las consultas hechas a una base
de datos.

Desarrrollada por Ricardo Ivan Macias Fusco y Daniel Emir Olivas Castro.
Fecha de Creacion: 16/Mayo/2017
Version 1.0(Version reciente en la clase Android Manifest)
Ultima Actualizacion: 5/Agosto/2017
*/

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

//Mapa general es una actividad que se extiende de un fragmento y llama a los metodos de Google Maps API.
public class MapaGeneral extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    //Se crea una variable mapa con sus metodos.
    private GoogleMap mMap;

    private static final String LOG_TAG = "Prueba: ";

    //Se declaran las consultas que se vayan a realizar con PHP a la base de datos. En este caso
    //Se llaman una generadora de negocios y otra de marcadores.
    private final String SERVICE_URL = "http://www.beerfinderbeta.96.lt/webservice/get_all_markers.php";
    private final String SERVICE_URL2 = "http://www.beerfinderbeta.96.lt/webservice/get_all_negocios.php";

    //Se inicializa un JSONArray que contendra la informacion extraida de la base de datos.
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

    // inicializamos la variable mapa.
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Determinamos un limite al area que sera encuadrada en el mapa inicialmente.
        LatLngBounds Chihuahua = new LatLngBounds(new LatLng(28.6, -106.1), new LatLng(28.7, -106.15));
        // Revisamos el estatus del mapa y llamamos los metodos.
        if (mMap != null) {
            mMap.clear();
            new MarkerTask().execute();
            new NegocioTask().execute();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Chihuahua.getCenter(), 14));
        }


        // Se llaman los eventos que se le permitiran al mapa durante su periodo de vida.
        UiSettings uisettings = mMap.getUiSettings();
        uisettings.setAllGesturesEnabled(true);
        uisettings.setMyLocationButtonEnabled(true);
        mMap.setOnMarkerClickListener(this);

    }


    //Se genera un hilo para realizar una consulta.
    class MarkerTask extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                //Se instancia el JSONArray para almacenar la consulta.
                marcadores = new JSONArray();

                //Se crea un progressDialog para dejar trabajndo la app en segunda instancia.
                //No se creara para el otro metodo ya que corren simultaneos.
                progressDialog = new ProgressDialog(MapaGeneral.this);
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

                    // Leer informacion del Json en el Builder con un socket.
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

                //Revisamos status del JSON
                Log.e("No funciona",SERVICE_URL);
                Log.e("Error",json.toString());

                try {

                    //El Json recibido se convierte en un JSONObject y despues se transforma
                    //en un arreglo para poder llamar a la clase Marcador que tiene una lista
                    //de arreglos.
                    JSONObject ob = new JSONObject(json);
                    JSONArray arr = ob.getJSONArray("marcadores");

                    //Se deserializa el Json al pasarlo al ArrayList.
                    //Se utiliza la clase Marcadores para pasar el arreglo.
                    ArrayList<Marcador> lugares =
                        Marcadores.parseJsonToObject(arr);


                    if (lugares == null)
                        Log.e("Error", "No es nulo");

                    //Iniciamos objeto tipo marcador.
                    Marcador m = null;

                    //Recorremos el ArrayList para poder pasar la informacion al objeto Marcador.
                    for (int i = 0; i < lugares.size(); i++) {

                        m = lugares.get(i);
                        Log.e("Mapa General ", m.toString());

                        //Pasamos las latitudes y longitudes del Arreglo y creamos un nuevo Objeto
                        //Que contendra las coordenadas.
                        double lat = Double.parseDouble(m.getGm_latitud());
                        double lng = Double.parseDouble(m.getGm_longitud());
                        LatLng latLng = new LatLng(lat, lng);

                        //Verificamos la posicion en el arreglo y movemos la camara
                        //a las coordenadas indicadas.
                        if (i == 0)
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                        //Se crea un marcador al cual se le asignan los datos del arreglo
                        //Se itera hasta que se haya recorridotodo el arreglo
                        //y generado todos los marcadores.
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


        //Repite la misma funcion de la clase MarkerTask pero con otra consulta diferente.
    class NegocioTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            marcadores = new JSONArray();

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

                for (int i = 0; i < lugares.size(); i++) {

                    m = lugares.get(i);
                    Log.e("Mapa General ", m.toString());


                    double lat = Double.parseDouble(m.getGm_latitud());
                    double lng = Double.parseDouble(m.getGm_longitud());
                    LatLng latLng = new LatLng(lat, lng);


                    if (i == 0)
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));


                    mMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                            .title(m.getLugar())
                            .snippet(m.getDescripcion())
                            .position(latLng));

                }


            } catch (Exception e) {
                Log.e(LOG_TAG, "Error generando marcadores de espacios", e);
            }

        }

    }

    //Sirve para seleccionar un marcador.
    @Override
    public boolean onMarkerClick(Marker marker) {

        return false;
    }

    // Boton que genera un nuevo activity llamado MapsActivity
    public void AgregaLugar(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
        finish();

    }
}
