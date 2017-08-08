/*Esta clase permite llamar desde la base de datos la inforamcion
 de los diferentes tipos de cerveza que hay.
 Los datos se almacenan en arreglos para despues pintar los diferentes layouts.

Desarrrollada por Ricardo Ivan Macias Fusco y Daniel Emir Olivas Castro.
Fecha de Creacion: 16/Mayo/2017
Version 1.0(Version reciente en la clase Android Manifest)
Ultima Actualizacion: 08/Agosto/2017
*/

package mx.netsquare.beerfindebeta;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.image.SmartImageView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class Beers extends AppCompatActivity {

    // Se senala la url desde la cual se llamara la consulta.
    private final String SERVICE_URL = "http://www.beerfinderbeta.96.lt/webservice/get_beer2.php";
    public static String ID_PREF = null;

    private ListView listView;

    //Se crea una lista de arreglos que almacenaran un solo tipo de dato
    //No se hace un ArrayList de objetos, son diferentes ArrayList por cada posicion de un arreglo.
    ArrayList BeerID = new ArrayList();
    ArrayList BeerName = new ArrayList();
    ArrayList BeerGrados  = new ArrayList();
    ArrayList BeerTipo  = new ArrayList();
    ArrayList BeerCasa = new ArrayList();
    ArrayList BeerImagen = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beers);

        //Se inicializa la lista que se llenara con la informacion.
        listView = (ListView)findViewById(R.id.listView);

        //Se corre el metodo que traera la informacion desde la consulta
        //y se pinta el layout con esa informacion.
        cargarImagen();

        //Se crea un listener para los items que se encuentran en la lista.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id){
                //Se crea una ventana de alerta que contiene informacion de la cerveza.
                //Las alertas dependen del sistema operativo. No son layout.
                AlertDialog.Builder preferencias = new AlertDialog.Builder(Beers.this);
                preferencias.setMessage(BeerName.get(position).toString()).setCancelable(false)
                        //Se genera un boton en la ventana de alerta y se le asignan una funcion.
                        .setPositiveButton(R.string.abrir_preferencias, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            //Se inicia una nueva clase y se envia un parametro que contiene el
                            //ID del item seleccionado.
                            Intent intento = new Intent(getApplicationContext(), MapaVenta.class);
                            ID_PREF = BeerID.get(position).toString();
                            intento.putExtra("beerfav", ID_PREF);
                            startActivity(intento);

                            }
                        })
                        //El boton negativo solo termina la funcion.
                        .setNegativeButton(R.string.cerrar, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                //Se llama a la alerta. Sin este metodo no se despliega la alerta.
                preferencias.show();

            }
        });


    }

    //Este metodo carga la informacion de la consulta en el layout que despues se usara
    //para inflar la lista.
    private void cargarImagen() {

        //Se borra la informacion que hay en los ArrayList.
        //Si no se borra puede ser que llame datos y se vuelva un arreglo irregular
        //y las posiciones no coincidan.
        BeerID.clear();
        BeerName.clear();
        BeerGrados.clear();
        BeerTipo.clear();
        BeerCasa.clear();
        BeerImagen.clear();

        //Se genera la conexion a la base de datos.
        AsyncHttpClient con = new AsyncHttpClient();
        con.get(SERVICE_URL,
                new AsyncHttpResponseHandler() {

            //Este metodo verifica la informacion que esta en el PHP y genera un arreglo
            //el arreglo es cargado en bytes.
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200){

                    try {
                        //Se revisa si se cargo el arreglo
                        Log.e("El Json: ", responseBody.toString());

                        //Se manda el arreglo a un JSONArray que se recorrera.
                        JSONArray jsonArray = new JSONArray(new String(responseBody));

                        //Se recorre el arreglo y se llama a las variables del PHP que se
                        //Encuentran como strings. Se anade cada campo al arreglo correspondiente.
                        //Los strings deben tener el mismo dato que el campo en la BD.
                        for (int i = 0; i<jsonArray.length(); i++){
                            BeerID.add(jsonArray.getJSONObject(i).getString("BeerID"));
                            BeerName.add(jsonArray.getJSONObject(i).getString("BeerName"));
                            BeerGrados.add(jsonArray.getJSONObject(i).getString("BeerGrados"));
                            BeerTipo.add(jsonArray.getJSONObject(i).getString("BeerTipo"));
                            BeerCasa.add(jsonArray.getJSONObject(i).getString("BeerCasa"));
                            BeerImagen.add(jsonArray.getJSONObject(i).getString("BeerImagen"));
                        }

                    //Se genera un adapter y se trae la informacion que se encuentra actualmente
                    //en contexto.
                    listView.setAdapter(new ImagenAdapter(getApplicationContext()));

                    }catch (JSONException e) {
                    e.printStackTrace();
                        }
                    }

                }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });


    }


    //Se crea un adaptador presonalizado que se extiende de la clase BaseAdapter.
    //Base adapter permite que se cree un Adaptador que puede contener diferentes
    //tipos de datos y objetos
    private class ImagenAdapter extends BaseAdapter {

        //Se inicializan las variables. Contexto permite llamar desde el adaptador la informacion.
        Context context;
        //Sera el encargado de llenar los campos en la lista.
        LayoutInflater layoutInflater;
        //Es una libreria que permite llamar una imagen desde una consulta.
        SmartImageView smartImageView;
        TextView txtBeerName, txtBeerGrados, txtBeerTipo, txtBeerCasa;

        //Se genera el constructor
        public ImagenAdapter(Context applicationContext) {

            //El Inflater depende de los recursos del sistema.
            this.context = applicationContext;
            layoutInflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        //GetCount nos regresa el tamano del arreglo que querramos como base.
        @Override
        public int getCount() {
            return BeerImagen.size();
        }

        //GetItem nos regresa el objeto tenga.
        @Override
        public Object getItem(int position) {
            return position;
        }

        //GetItemId nos da la posicion para saber que coincidan los objetos de la tabla.
        @Override
        public long getItemId(int position) {
            return position;
        }

        //empieza a llenar los campos del item con la informacion que se trae por posicion
        //para despues inflarlos en la lista.
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewGroup viewGroup = (ViewGroup)layoutInflater.inflate(R.layout.beer, null);

            smartImageView = (SmartImageView) viewGroup.findViewById(R.id.BeerImagen);
            txtBeerName = (TextView) viewGroup.findViewById(R.id.txtBeerName);
            txtBeerGrados = (TextView) viewGroup.findViewById(R.id.txtBeerGrados);
            txtBeerTipo = (TextView) viewGroup.findViewById(R.id.txtBeerTipo);
            txtBeerCasa = (TextView) viewGroup.findViewById(R.id.txtBeerCasa);

            //Se declara la url de la carpeta desde la cual se vayan a llamar las imagenes.
            //El dato en la BD debe tener el mismo nombre que el archivo de la carpeta.
            String urlImagen = "http://beerfinderbeta.96.lt/webservice/BeerImagen/"
                    + BeerImagen.get(position).toString();
            Log.e("LaURL: ", urlImagen);

            //Se llama la imagen desde la url.
            smartImageView.setImageUrl(urlImagen);

            //Se llaman las variables para llenar los campos.
            txtBeerName.setText(BeerName.get(position).toString());
            txtBeerGrados.setText(BeerGrados.get(position).toString());
            txtBeerTipo.setText(BeerTipo.get(position).toString());
            txtBeerCasa.setText(BeerCasa.get(position).toString());

            //Se regresa el item con toda la informacion para empezar a llenar la lista.
            //Esta funcion es como si iterara por posicion.
            return viewGroup;

        }
    }

}


