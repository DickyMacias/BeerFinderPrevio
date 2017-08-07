package mx.netsquare.beerfindebeta;

import android.app.DownloadManager;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.image.SmartImageView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static mx.netsquare.beerfindebeta.R.id.NegocioDireccion;
import static mx.netsquare.beerfindebeta.R.id.NegocioImagen;
import static mx.netsquare.beerfindebeta.R.id.NegocioNombre;

public class Negocios extends AppCompatActivity {

    private final String SERVICE_URL = "http://www.beerfinderbeta.96.lt/webservice/get_negocios.php";

    public static String URL_WEB = null;

    private GridView gridView;

    ArrayList Lugar = new ArrayList();
    ArrayList Descripcion  = new ArrayList();
    ArrayList Web  = new ArrayList();
    ArrayList NegocioImagen  = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_negocios);

        gridView = (GridView)findViewById(R.id.gridView);

        cargarImagen();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder direccion = new AlertDialog.Builder(Negocios.this);
                direccion.setMessage(Descripcion.get(position).toString()).setCancelable(false)
                        .setPositiveButton("Website", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intento = new Intent(getApplicationContext(), WebNegocio.class);
                                URL_WEB = Web.get(position).toString();
                                intento.putExtra("web", URL_WEB);
                                startActivity(intento);
                                Negocios.this.finish();
                            }
                        })
                        .setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Negocios.this.finish();

                            }
                        });
                direccion.show();

            }
        });
    }



    private void cargarImagen() {

        Lugar.clear();
        Descripcion.clear();
        Web.clear();
        NegocioImagen.clear();


        AsyncHttpClient con = new AsyncHttpClient();
        con.get(SERVICE_URL,
                new AsyncHttpResponseHandler() {


                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        if (statusCode == 200) {

                            try {
                                Log.e("El Json: ", responseBody.toString());
//                        JSONObject ob = new JSONObject(String.valueOf(responseBody));
//                        JSONArray jsonArray = ob.getJSONArray("cervezas");
//                        Log.e("Este es el Json: ", jsonArray.toString());

                                JSONArray jsonArray = new JSONArray(new String(responseBody));
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Lugar.add(jsonArray.getJSONObject(i).getString("lugar"));
                                    Descripcion.add(jsonArray.getJSONObject(i).getString("descripcion"));
                                    Web.add(jsonArray.getJSONObject(i).getString("web"));
                                    NegocioImagen.add(jsonArray.getJSONObject(i).getString("urlimagen"));

                                }

                                gridView.setAdapter(new NegocioAdapter(getApplicationContext()));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {

                    }
                });


    }





    private class NegocioAdapter extends BaseAdapter {

        Context context;
        LayoutInflater layoutInflater;
        SmartImageView smartImageView;
        TextView txtNegocioNombre, txtNegocioDireccion;





        public NegocioAdapter(Context applicationContext) {

            this.context = applicationContext;
            layoutInflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public int getCount() {
            return NegocioImagen.size();

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
            ViewGroup viewGroup = (ViewGroup)layoutInflater.inflate(R.layout.negocio, null);

            smartImageView = (SmartImageView) viewGroup.findViewById(R.id.NegocioImagen);
            txtNegocioNombre = (TextView) viewGroup.findViewById(R.id.NegocioNombre);
            txtNegocioDireccion = (TextView) viewGroup.findViewById(R.id.NegocioDireccion);

            String urlImagen = "http://beerfinderbeta.96.lt/webservice/NegocioImagen/"
                    + NegocioImagen.get(position).toString();
            Log.e("LaURL: ", urlImagen);

            smartImageView.setImageUrl(urlImagen);

            txtNegocioNombre.setText(Lugar.get(position).toString());
            txtNegocioDireccion.setText(Descripcion.get(position).toString());


            return viewGroup;



        }
    }

}



