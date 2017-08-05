package mx.netsquare.beerfindebeta;

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
import android.widget.BaseAdapter;
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

public class Beers extends AppCompatActivity {

    private final String SERVICE_URL = "http://www.beerfinderbeta.96.lt/webservice/get_beer2.php";

    private ListView listView;

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
        
    }

    private void cargarImagen() {

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
//                        JSONObject ob = new JSONObject(String.valueOf(responseBody));
//                        JSONArray jsonArray = ob.getJSONArray("cervezas");
//                        Log.e("Este es el Json: ", jsonArray.toString());

                        JSONArray jsonArray = new JSONArray(new String(responseBody));
                        for (int i = 0; i<jsonArray.length(); i++){
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
            Rect rect = new Rect(smartImageView.getLeft(), smartImageView.getTop(),
                    smartImageView.getRight(), smartImageView.getBottom());

            smartImageView.setImageUrl(urlImagen);

            txtBeerName.setText(BeerName.get(position).toString());
            txtBeerGrados.setText(BeerGrados.get(position).toString());
            txtBeerTipo.setText(BeerTipo.get(position).toString());
            txtBeerCasa.setText(BeerCasa.get(position).toString());

            return viewGroup;

        }
    }

}


