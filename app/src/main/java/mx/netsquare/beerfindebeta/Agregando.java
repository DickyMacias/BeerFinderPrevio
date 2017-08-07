package mx.netsquare.beerfindebeta;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;


public class Agregando extends AppCompatActivity {

    private static final String  _url = "http://beerfinderbeta.96.lt/webservice/create_place.php";

    private EditText lugar = null, horario = null, desc = null;
    private TextView  coordenadas = null;
    private TextView gm_lat = null;
    private TextView gm_lng = null;
    private JSONParser jsonParser = null;
    private ProgressDialog progressDialog;


    private static final String TAG_SUCCESS = "success";
    private static final String TAG_LUGAR    = "lugar";
    private static final String TAG_HORARIO    = "horario";
    private static final String TAG_DESC    = "descripcion";
    private static final String TAG_COORD = "coordenadas";
    private static final String TAG_LATITUD    = "gm_lat";
    private static final String TAG_LONGITUD = "gm_lng";

    private String lat;
    private String lng;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregando);


        jsonParser = new JSONParser();

        lugar  = (EditText)findViewById(R.id.editLugar);
        horario = (EditText)findViewById(R.id.editHorario);
        desc  = (EditText)findViewById(R.id.editDesc);


        // Extraer lat. y lng.
        Intent intent = getIntent();
        String latlng = intent.getStringExtra(MapsActivity.COORDENADAS);
        lat = intent.getStringExtra("lat");
        lng = intent.getStringExtra("lon");

        Toast.makeText(this,lat + " " + lng, Toast.LENGTH_LONG).show();

        // llenar campos
        coordenadas = (TextView) findViewById(R.id.txtLatLng);
        coordenadas.setText(latlng);
        gm_lat = (TextView)findViewById(R.id.txtLat);
        gm_lat.setText(lat);
        gm_lng = (TextView)findViewById(R.id.txtLng);
        gm_lng.setText(lng);

    }


    public void agregarLugar(View view) {

        String lugar  = this.lugar.getText().toString();
        String horario = this.horario.getText().toString();
        String desc  = this.desc.getText().toString();
        String coord = this.coordenadas.getText().toString();


        //Lat y lng pasan directo sin ser llamadas desde el txtView
        new agregarNuevo().execute(lugar,horario,desc,coord, lat, lng);

        finish();

        Intent intentRegistro = new Intent(this, MapaGeneral.class);
        startActivity(intentRegistro);
    }



    class agregarNuevo extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(Agregando.this);
            progressDialog.setMessage(getString(R.string.dialog_creando_nuevo_lugar));
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try{
                ArrayList<NameValuePair> Params = new ArrayList<NameValuePair>();
                Params.add(new BasicNameValuePair(TAG_LUGAR,params[0]));
                Params.add(new BasicNameValuePair(TAG_HORARIO,params[1]));
                Params.add(new BasicNameValuePair(TAG_DESC,params[2]));
                Params.add(new BasicNameValuePair(TAG_COORD,params[3]));
                Params.add(new BasicNameValuePair(TAG_LATITUD,params[4]));
                Params.add(new BasicNameValuePair(TAG_LONGITUD,params[5]));

                JSONObject json = jsonParser.makeHttpRequest(_url,"POST",Params);

                int succ = json.getInt(TAG_SUCCESS);

                if(succ == 1){
                    Agregando.this.finish();

                    return getString(R.string.hecho);
                }else{
                    return null;
                }

            }catch(JSONException e){
                e.printStackTrace();

            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if(s.equals(R.string.hecho)){
                Toast.makeText(Agregando.this, R.string.dato_agregado, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Agregando.this, R.string.dato_no_agregado, Toast.LENGTH_SHORT).show();
            }

        }
    }
}
