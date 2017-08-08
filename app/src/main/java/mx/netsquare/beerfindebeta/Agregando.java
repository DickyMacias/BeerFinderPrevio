/*Esta clase permite agregar una ubicacion en el mapa.
Su unica funcionalidad es hacer que las coordenadas que fueron seleccionadas en el mapa
sean enviadas a una base de datos donde se almacenaran para ser llamadas con posterioridad.

Desarrrollada por Ricardo Ivan Macias Fusco y Daniel Emir Olivas Castro.
Fecha de Creacion: 16/Mayo/2017
Version 1.0(Version reciente en la clase Android Manifest)
Ultima Actualizacion: 06/Agosto/2017
*/

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

    //Se define la url a la cual se hara el POST para insertar la informacion.
    private static final String  _url = "http://beerfinderbeta.96.lt/webservice/create_place.php";

    // Se definen e inicializan los atributos y variables
    private EditText lugar = null, horario = null, desc = null;
    private TextView  coordenadas = null;
    private TextView gm_lat = null;
    private TextView gm_lng = null;
    private JSONParser jsonParser = null;
    private ProgressDialog progressDialog;

    //Se determinan las variables estaticas y finales, para poder almacenar la informacion
    //que llega desde los arreglos generados en el JSON con el archivo PHP de la url.
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_LUGAR    = "lugar";
    private static final String TAG_HORARIO    = "horario";
    private static final String TAG_DESC    = "descripcion";
    private static final String TAG_COORD = "coordenadas";
    private static final String TAG_LATITUD    = "gm_lat";
    private static final String TAG_LONGITUD = "gm_lng";

    // Se definen variables lat y lng para almacenar directamente al llegar al activity
    //las coordenadas arrastradas desde la actividad anterior con el putExtra
    private String lat;
    private String lng;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregando);

        // Se instancia la clase JSONParser para crear un JSON Object
        jsonParser = new JSONParser();

        // Se crean las variables para los campos de informacion
        lugar  = (EditText)findViewById(R.id.editLugar);
        horario = (EditText)findViewById(R.id.editHorario);
        desc  = (EditText)findViewById(R.id.editDesc);


        // Se extraen coordenadas de la actividad anterior con el metodo getStringExtra.
        Intent intent = getIntent();
        String latlng = intent.getStringExtra(MapsActivity.COORDENADAS);
        lat = intent.getStringExtra("lat");
        lng = intent.getStringExtra("lon");

        Toast.makeText(this, R.string.coordenadas_guardadas, Toast.LENGTH_LONG).show();

        // Se llenan las leyendas con la informacion recabada
        coordenadas = (TextView) findViewById(R.id.txtLatLng);
        coordenadas.setText(latlng);
        gm_lat = (TextView)findViewById(R.id.txtLat);
        gm_lat.setText(lat);
        gm_lng = (TextView)findViewById(R.id.txtLng);
        gm_lng.setText(lng);

    }

    //Se crea metodo para el boton que realizara la accion de enviar la informacion
    //a la base de datos.
    public void agregarLugar(View view) {

        //Se extrae el texto de los campos.
        String lugar  = this.lugar.getText().toString();
        String horario = this.horario.getText().toString();
        String desc  = this.desc.getText().toString();
        String coord = this.coordenadas.getText().toString();


        //Lat y lng pasan directo sin ser llamadas desde el txtView.
        new agregarNuevo().execute(lugar,horario,desc,coord, lat, lng);

        //Se termina la actividad para que no quede en segundo plano.
        finish();

        //Se llama a la actividad Mapa General para que quede desplegada.
        Intent intentRegistro = new Intent(this, MapaGeneral.class);
        startActivity(intentRegistro);
    }


    //Se crea un metodo que depende de un hilo para realizar el insertado en la base de datos.
    //Es necesario que se realice a traves de un hilo por los web services que estan implementados.

    //El metodo se extiende de la clase AsyncTask para generar un hilo que trabaje en segundo plano.
    class agregarNuevo extends AsyncTask<String, String, String> {

        //Se realiza la primera accion en el metodo PreExecute
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //Se crea un dialogo que muestra al usuario un circulo de carga.
            //Depende del sistema. No es un layout.
            progressDialog = new ProgressDialog(Agregando.this);
            progressDialog.setMessage(getString(R.string.dialog_creando_nuevo_lugar));
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        //El metodo doInBackground permite realizar tods las funciones mientras
        //El progressDialog cubre la pantalla para dar tiempo de realizar las actividades.
        @Override
        protected String doInBackground(String... params) {

            //Se genera un ArrayList generico con las variables anteriormente senaladas.
            try{
                ArrayList<NameValuePair> Params = new ArrayList<NameValuePair>();
                Params.add(new BasicNameValuePair(TAG_LUGAR,params[0]));
                Params.add(new BasicNameValuePair(TAG_HORARIO,params[1]));
                Params.add(new BasicNameValuePair(TAG_DESC,params[2]));
                Params.add(new BasicNameValuePair(TAG_COORD,params[3]));
                Params.add(new BasicNameValuePair(TAG_LATITUD,params[4]));
                Params.add(new BasicNameValuePair(TAG_LONGITUD,params[5]));

                //Se genera un JSONObject que deriva de la clase JsonParser para
                //poder crear el arreglo de objetos que se enviara.
                JSONObject json = jsonParser.makeHttpRequest(_url,"POST",Params);

                //Se genera una variable para revisar si el json fue exitoso.
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


        //En PostExecute se termine le progressDialog creado al principio del hilo y se informa
        //del estado de la consulta.
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
