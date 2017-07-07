package mx.netsquare.beerfindebeta;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;


public class Login extends AppCompatActivity {

    private EditText user = null;
    private EditText pass = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        user = (EditText) findViewById(R.id.llenarUsuario);
        pass = (EditText) findViewById(R.id.llenarContrasena);
    }

    public void login(View view) {

        new usersJson().execute();
    }

    class usersJson extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {

// Creamos el Gson y le pasamos la URL

                Gson miGson = new Gson();
                URL url = new URL("http://192.168.56.1:9090/webservice/get_usuario.php");
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(url.openStream(),
                                Charset.forName("UTF-8")));

//Pasamos la info del Json a un objeto para hacer la consulta
                Usuarios data = miGson.fromJson(reader, Usuarios.class);
                List<Usuario> users = data.getUsers();

                for (int i = 0; i < users.size(); i++) {

                    if (user.equals(users.get(i).toString())
                            && pass.equals(users.get(i).toString())) {
                        return true;
                    } else
                        return false;
                }

            } catch (Exception e) {
                Log.i("valores", "Error al generar consulta");
                e.printStackTrace();
            }

            return false;
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean resultado) {

            if (true) {
                Intent intentMap = new Intent(getApplicationContext(), MapaGeneral.class);
                startActivity(intentMap);


            } else {
                Toast.makeText(Login.this, "Usuario o Contrasena Incorrectos", Toast.LENGTH_SHORT).show();
            }
        }

    }

        public void registro(View view) {
            Intent intentRegistro = new Intent(this, Registro.class);
            startActivity(intentRegistro);
        }

        public void recuperar(View view) {
            Intent intentMap = new Intent(getApplicationContext(), Beers.class);
            startActivity(intentMap);

        }

    }


