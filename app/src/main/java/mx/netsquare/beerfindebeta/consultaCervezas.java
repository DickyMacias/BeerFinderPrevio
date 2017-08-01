package mx.netsquare.beerfindebeta;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class consultaCervezas extends Activity {

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

        _url ="http://192.168.56.1:9090/webservice/get_beers.php";

        record = new ArrayList<HashMap<String, String>>(); //Se guardan registros aqui

        //Se hace la consulta a la base de datos ejecutando el hilo y rellenando el arraylist
        new consulta().execute();

    }


    class consulta extends AsyncTask<Object, Object, String> {
    ListView listado = (ListView)findViewById(R.id.list2);

        consulta() {
            this.listado=listado;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            cervezas = new JSONArray();

            progressDialog = new ProgressDialog(consultaCervezas.this);
            progressDialog.setMessage("Cargando...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Object... params) {

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
                        String nombre  = c.getString(TAG_NOMBRE);
                        String grado = c.getString(TAG_GRADO);
                        String tipo= c.getString(TAG_TIPO);
                        String casa= c.getString(TAG_CASA);
                        String imagen= c.getString(TAG_IMAGEN);
                       // String desc = c.getString(TAG_DESC);


                        //Enviar a otra activity
                        nombres[i] = nombre;
                        grados[i]  = grado;
                        tipos[i] = tipo;
                        casas[i]  = casa;
                        imagenes[i] = imagen;
                       // descs[i]    = desc;

                        //Se pasa al hashmap
                        Cervezas map = new Cervezas();
                        map.setNombre(TAG_NOMBRE,nombre);
                        map.setGrados(TAG_GRADO, grado);
                        map.setTipo(TAG_TIPO,tipo);
                        map.setCasa(TAG_CASA, casa);
                        map.setURLimagen(TAG_IMAGEN,imagen);
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            MyAdapter myAdapter = new MyAdapter();
            listado.setAdapter(myAdapter);

          //  if(s.equals("ok"));
            //Toast.makeText(Beers.this, "Fine", Toast.LENGTH_SHORT).show();
        }
    }

    class MyAdapter extends BaseAdapter
    {

        protected Activity activity;
        //ARRAYLIST CON TODOS LOS ITEMS
        protected ArrayList<Cervezas> record;

        public MyAdapter() {
            this.activity = activity;
            this.record = record;
        }

        @Override
        public int getCount() {
            return record.size(); // los que tenga mi arreglo
        }

        @Override
        public Object getItem(int position) {
            return record.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;
            //ASOCIAMOS LA VISTA AL LAYOUT DEL RECURSO XML DONDE ESTA LA BASE DE

            if(convertView == null){


                LayoutInflater inf = (LayoutInflater) getSystemService

                        (Context.LAYOUT_INFLATER_SERVICE);
                v = inf.inflate(R.layout.beer, null);
            }

            Cervezas dir = record.get(position);
            ImageView foto = (ImageView) v.findViewById(R.id.BeerImagen);
            if(foto != null) {
                new LoadImage(foto).execute(dir.getURLimagen());
            }

            TextView nombre = (TextView) v.findViewById(R.id.beerNombre);
            nombre.setText(dir.getNombre());
            TextView tipo = (TextView) v.findViewById(R.id.BeerTipo);
            tipo.setText(dir.getNombre());
            TextView casa = (TextView) v.findViewById(R.id.BeerCasa);
            casa.setText(dir.getNombre());
            TextView grados = (TextView) v.findViewById(R.id.beerGrado);
            grados.setText(dir.getNombre());


            // DEVOLVEMOS VISTA
            return v;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        class LoadImage extends AsyncTask<Object, Object, Bitmap> {
        ImageView bmImage;

        LoadImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        @Override
        protected Bitmap doInBackground(Object... urls) {
            Object urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                mIcon11 = BitmapFactory.decodeStream((InputStream)new URL((String) urldisplay).getContent());

            } catch (Exception e) {
                //Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
    }

}



