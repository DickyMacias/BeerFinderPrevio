package mx.netsquare.beerfindebeta;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Login extends AppCompatActivity {

    private ProgressDialog progressDialog;

    private EditText user1 = null;
    private EditText pass1 = null;

    private String user;
    private String pass;

    private JSONParser jsonParser = null; //Objeto conexion webservice
    private static String _url = null;
    ArrayList<HashMap<String,String>> info = null;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_USERS   = "usuarios";
    private static final String TAG_USER   = "Username";
    private static final String TAG_PASS  = "Password";

    String users[]  = new String[100];
    String passwords[] = new String[100];

    private JSONArray usuarios=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        user1 = (EditText) findViewById(R.id.llenarUsuario);
        pass1 = (EditText) findViewById(R.id.llenarContrasena);

    }

    public void login(View view) {

        _url ="http://www.beerfinderbeta.96.lt/webservice/get_usuario.php";
        info = new ArrayList<HashMap<String, String>>(); //Se guardan registros aqui

        user = user1.getText().toString();
        pass = pass1.getText().toString();

        new usersJson().execute();

    }

    class usersJson extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            usuarios = new JSONArray();

            progressDialog = new ProgressDialog(Login.this);
            progressDialog.setMessage(getString(R.string.autenticando));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {

                List<NameValuePair> Params = new ArrayList<NameValuePair>();
                //Almacena la consulta al webservice

                JSONObject json = null;
                jsonParser = new JSONParser();

                try {
                    //Se realiza la conexion al web service
                    json = jsonParser.makeHttpRequest(_url, "GET", Params);
                    Log.e("Error", json.toString());


                    int ready = json.getInt(TAG_SUCCESS);
                    if (ready == 1) {

                        usuarios = json.getJSONArray(TAG_USERS);

                        for(int i = 0; i<usuarios.length(); i++){
                            JSONObject u = usuarios.getJSONObject(i);
                            String username  = u.getString(TAG_USER);
                            String password = u.getString(TAG_PASS);
                            Log.e("pruebas", user + ":" + username + "&" + pass + ":" + password);

                            if (user.equals(username)
                                    && pass.equals(password)) {
                                return true;
                            } else {
                                return false;

                        }

                    }
                }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }

                @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean resultado) {

            if (resultado) {
                Intent intentMap = new Intent(getApplicationContext(), MenuDesplegable.class);
                startActivity(intentMap);




            } else {
                Toast.makeText(Login.this, R.string.user_pass_incorrectos, Toast.LENGTH_SHORT)
                        .show();
            }

            progressDialog.dismiss();
        }

    }

        public void registro(View view) {
            Intent intentRegistro = new Intent(this, Registro.class);
            startActivity(intentRegistro);
        }

        public void recuperar(View view) {
            Intent intentMap = new Intent(getApplicationContext(), Recuperar.class);
            startActivity(intentMap);

        }

    }


