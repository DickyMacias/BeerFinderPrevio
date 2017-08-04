package mx.netsquare.beerfindebeta;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Negocios extends ListActivity {

    private MenuItem item;

    private JSONParser jsonParser = null; //Objeto conexion webservice

    private static final String TAG_PLACES = "negocios";

    private static final String LOG_TAG = "Prueba: ";

    private final String SERVICE_URL = "http://beerfinderbeta.96.lt/webservice/get_all_negocios2.php";

    private JSONArray negocios = null;
    private ProgressDialog progressDialog;

    private ImageView imagen;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_negocios);



        new BusinessTask().execute();




    }

    class BusinessTask extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            negocios = new JSONArray();

        }

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

            Log.e("No funciona", SERVICE_URL);
            Log.e("Error", json.toString());

            try {

                JSONObject ob = new JSONObject(json);
                JSONArray arr = ob.getJSONArray("negocios");


                ArrayList<Negocio> lugares =
                        Negocios.parseJsonToObject(arr);


                if (lugares == null)
                    Log.e("Error", "No es nulo");


                Negocio n = null;

                for (int i = 0; i < lugares.size(); i++) {

                    n = lugares.get(i);

                    Log.e("Lista a desplegar ", n.toString());

                    ListView lista = (ListView) findViewById(R.id.list_negocios);
                    ArrayList<Negocio> negocios = new ArrayList<Negocio>();

                    //NegociosAdapter adaptador = new NegociosAdapter(this, negocios);
                    //Log.e("Si pasa a adapter: ", negocios.toString());
                    //lista.setAdapter(adaptador);

                    //new download().execute(n.getURLImagen());

                }


            } catch (Exception e) {
                Log.e(LOG_TAG, "Error generando marcadores de espacios", e);
            }


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);



        }
    }

    public static ArrayList<Negocio> parseJsonToObject(JSONArray arrJson) {

        ArrayList<Negocio> negocios = new ArrayList<Negocio>();

        try {
            //JSONArray jsonArray = new JSONArray(json);


            for (int i = 0; i < arrJson.length(); i++) {
                JSONObject jsonObj = arrJson.getJSONObject(i);

                Log.e("Info", "--------------- Ini Json ------------------");


                Negocio n = new Negocio();
                n.setLugar(jsonObj.getString("lugar"));
                n.setDescripcion(jsonObj.getString("descripcion"));
                n.setURLImagen(jsonObj.getString("urlimagen"));

                Log.e("Info", n.toString());

                Log.e("Info", "-------------------------------------------");

                negocios.add(n);
            }
        } catch (JSONException e) {
            Log.e("Negocios", "Error procesando el JSON");
        }

        Log.e("Negocios count", "" + negocios.size());

        return negocios;

    }



}



