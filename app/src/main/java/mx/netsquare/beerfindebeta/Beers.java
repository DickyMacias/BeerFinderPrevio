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

    private final String SERVICE_URL = "http://www.beerfinderbeta.96.lt/webservice/get_beer2.php";

    public static String ID_PREF = null;

    private ListView listView;

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

        listView = (ListView)findViewById(R.id.listView);

        cargarImagen();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id){
                AlertDialog.Builder preferencias = new AlertDialog.Builder(Beers.this);
                preferencias.setMessage(BeerName.get(position).toString()).setCancelable(false)
                        .setPositiveButton(R.string.abrir_preferencias, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intento = new Intent(getApplicationContext(), MapaVenta.class);
                                ID_PREF = BeerID.get(position).toString();
                                intento.putExtra("beerfav", ID_PREF);
                                startActivity(intento);

                            }
                        })
                        .setNegativeButton(R.string.cerrar, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {



                            }
                        });

                preferencias.show();

            }
        });


    }

    private void cargarImagen() {

        BeerID.clear();
        BeerName.clear();
        BeerGrados.clear();
        BeerTipo.clear();
        BeerCasa.clear();
        BeerImagen.clear();

        AsyncHttpClient con = new AsyncHttpClient();
        con.get(SERVICE_URL,
                new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200){

                    try {
                        Log.e("El Json: ", responseBody.toString());

                        JSONArray jsonArray = new JSONArray(new String(responseBody));
                        for (int i = 0; i<jsonArray.length(); i++){
                            BeerID.add(jsonArray.getJSONObject(i).getString("BeerID"));
                            BeerName.add(jsonArray.getJSONObject(i).getString("BeerName"));
                            BeerGrados.add(jsonArray.getJSONObject(i).getString("BeerGrados"));
                            BeerTipo.add(jsonArray.getJSONObject(i).getString("BeerTipo"));
                            BeerCasa.add(jsonArray.getJSONObject(i).getString("BeerCasa"));
                            BeerImagen.add(jsonArray.getJSONObject(i).getString("BeerImagen"));
                        }

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



    private class ImagenAdapter extends BaseAdapter {

        Context context;
        LayoutInflater layoutInflater;
        SmartImageView smartImageView;
        TextView txtBeerName, txtBeerGrados, txtBeerTipo, txtBeerCasa;


        public ImagenAdapter(Context applicationContext) {

            this.context = applicationContext;
            layoutInflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return BeerImagen.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewGroup viewGroup = (ViewGroup)layoutInflater.inflate(R.layout.beer, null);

            smartImageView = (SmartImageView) viewGroup.findViewById(R.id.BeerImagen);
            txtBeerName = (TextView) viewGroup.findViewById(R.id.txtBeerName);
            txtBeerGrados = (TextView) viewGroup.findViewById(R.id.txtBeerGrados);
            txtBeerTipo = (TextView) viewGroup.findViewById(R.id.txtBeerTipo);
            txtBeerCasa = (TextView) viewGroup.findViewById(R.id.txtBeerCasa);

            String urlImagen = "http://beerfinderbeta.96.lt/webservice/BeerImagen/"
                    + BeerImagen.get(position).toString();
            Log.e("LaURL: ", urlImagen);

            smartImageView.setImageUrl(urlImagen);

            txtBeerName.setText(BeerName.get(position).toString());
            txtBeerGrados.setText(BeerGrados.get(position).toString());
            txtBeerTipo.setText(BeerTipo.get(position).toString());
            txtBeerCasa.setText(BeerCasa.get(position).toString());

            return viewGroup;

        }
    }

}


