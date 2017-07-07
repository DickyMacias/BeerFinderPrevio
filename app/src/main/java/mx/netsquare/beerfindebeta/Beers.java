

package mx.netsquare.beerfindebeta;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static mx.netsquare.beerfindebeta.Preferencias.PREFS_URL;


public class Beers extends ListActivity {

    private ProgressDialog progressDialog=null;
    private JSONParser jsonParser = null; //Objeto conexion webservice
    private static String _url = null;
    ArrayList<HashMap<String,String>> record = null;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_BEERS   = "cervezas";
    private static final String TAG_NOMBRE  = "BeerName";
    private static final String TAG_GRADO   = "BeerGrados";
    private static final String TAG_TIPO    = "BeerTipo";
    private static final String TAG_CASA    = "BeerCasa";
    private static final String TAG_IMAGEN  = "BeerImagen";
    private static final String TAG_DESC    = "BeerDesc";
    String nombres[]  = new String[100];
    String grados[] = new String[100];
    String tipos[]  = new String[100];
    String casas[]  = new String[100];
    String imagenes[]  = new String[100];
    String descs[]  = new String[100];


    private JSONArray cervezas=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.query);

        SharedPreferences preferencias = getSharedPreferences(PREFS_URL,0);
        String laURL = preferencias.getString("siteIP","NohayURL");
        _url ="http://192.168.56.1:9090/webservice/get_beers.php";

        record = new ArrayList<HashMap<String, String>>(); //Se guardan registros aqui

        //Se hace la consulta a la base de datos ejecutando el hilo y rellenando el arraylist
        new consulta().execute();

        ListView lv = getListView();

        /*
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder describe = new AlertDialog.Builder(Beers.this);
                describe.setMessage("descripcion").setCancelable(false)
                        .setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Beers.this.finish();
                            }
                        });
                describe.show();
            }
        });*/



    }


    class consulta extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            cervezas = new JSONArray();

            progressDialog = new ProgressDialog(Beers.this);
            progressDialog.setMessage("Cargando...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            List<NameValuePair> Params = new ArrayList<NameValuePair>();
            //Almacena la consulta al webservice

            JSONObject json = null;
            jsonParser = new JSONParser();

            try{
                //Se realiza la conexion al web service
                json = jsonParser.makeHttpRequest(_url,"GET", Params);
                Log.e("Error",json.toString());


                int ready = json.getInt(TAG_SUCCESS);
                if(ready == 1){

                    cervezas = json.getJSONArray(TAG_BEERS);

                    for(int i = 0; i<cervezas.length(); i++){
                        JSONObject c = cervezas.getJSONObject(i);
                        String nombre   = c.getString(TAG_NOMBRE);
                        String grado = c.getString(TAG_GRADO);
                        String tipo= c.getString(TAG_TIPO);
                        String casa= c.getString(TAG_CASA);
                        String imagen= c.getString(TAG_IMAGEN);
                       // String desc = c.getString(TAG_DESC);

                        nombres[i] = nombre;
                        grados[i]  = grado;
                        tipos[i] = tipo;
                        casas[i]  = casa;
                        imagenes[i] = imagen;
                       // descs[i]    = desc;

                        //Se pasa al hashmap
                        HashMap<String, String> map = new HashMap<>();
                        map.put(TAG_NOMBRE,nombre);
                        map.put(TAG_GRADO, grado);
                        map.put(TAG_TIPO,tipo);
                        map.put(TAG_CASA, casa);
                        map.put(TAG_IMAGEN,imagen);
                       // map.put(TAG_DESC,desc);
                        //agregamos el map a la lista
                        record.add(map);

                    }//SE acaba el for
                    publishProgress();
                    return "ok";
                }

            }catch(JSONException e){
                e.fillInStackTrace();
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //llenamos la lista

//            WebView webview = null;


            ListAdapter adapter = new SimpleAdapter(
                    Beers.this,
                    record,
                    R.layout.beer,
                    new String[]{TAG_NOMBRE,TAG_GRADO,TAG_TIPO,TAG_CASA},
                    new int[]{R.id.beerNombre,R.id.beerGrado,R.id.BeerTipo,R.id.BeerCasa}



            );




            setListAdapter(adapter);
//            webview.loadUrl(TAG_IMAGEN);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
          //  if(s.equals("ok"));
            //Toast.makeText(Beers.this, "Fine", Toast.LENGTH_SHORT).show();
        }
    }

}


