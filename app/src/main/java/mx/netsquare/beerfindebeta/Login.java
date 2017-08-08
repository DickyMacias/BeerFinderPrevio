/*Esta clase es para iniciar sesion con la cuenta de usuario.
Permite al usuario autenticarse debidamente. Igualmente comunica a los
activities para registrarse y en caso de que el usuario perdiera
su contrasena.

Desarrrollada por Ricardo Ivan Macias Fusco y Daniel Emir Olivas Castro.
Fecha de Creacion: 16/Mayo/2017
Version 1.0(Version reciente en la clase Android Manifest)
Ultima Actualizacion: 20/Julio/2017
*/

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

    //Inicializa las variables que seran utilizadas en el login.
    private ProgressDialog progressDialog;

    private EditText user1 = null;
    private EditText pass1 = null;

    private String user;
    private String pass;

    //Genera el objeto que es la conexion al web service.
    private JSONParser jsonParser = null;
    private static String _url = null;
    ArrayList<HashMap<String,String>> info = null;

    // Se crean variables finales que almacenaran los capos de la BD.
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_USERS   = "usuarios";
    private static final String TAG_USER   = "Username";
    private static final String TAG_PASS  = "Password";

    private JSONArray usuarios=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Se crean los campos de texto a llamar.
        user1 = (EditText) findViewById(R.id.llenarUsuario);
        pass1 = (EditText) findViewById(R.id.llenarContrasena);

    }

    public void login(View view) {

        //Se inicializa la url con la consulta PHP que se generara.
        _url ="http://www.beerfinderbeta.96.lt/webservice/get_usuario.php";
        info = new ArrayList<HashMap<String, String>>(); //Se guardan registros aqui

        //Se pasan los datos de los campos a las variables para verificarlas.
        user = user1.getText().toString();
        pass = pass1.getText().toString();

        //Se llama al hilo en el cual se realizara la consulta.
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


                    //Se confirma si la consulta fue exitosa.
                    int ready = json.getInt(TAG_SUCCESS);
                    if (ready == 1) {

                        //Se genera un arreglo con los usuarios;
                        usuarios = json.getJSONArray(TAG_USERS);

                        //Se hace una iteracion para corroborar la informacion del usuario.
                        for(int i = 0; i<usuarios.length(); i++){
                            JSONObject u = usuarios.getJSONObject(i);
                            String username  = u.getString(TAG_USER);
                            String password = u.getString(TAG_PASS);
                            Log.e("pruebas", user + ":" + username + "&" + pass + ":" + password);

                            //Si concide la informacion del usuario con la consulta de la BD,
                            //envia un true
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

        //Entra como parametro el true lanzado en el cotejo de usuario
        @Override
        protected void onPostExecute(Boolean resultado) {

            //Si es true lanza una nueva actividad llamada Menu Desplegable
            if (resultado) {
                Intent intentMap = new Intent(getApplicationContext(), MenuDesplegable.class);
                startActivity(intentMap);

                //Caso contrario, informa que el usuario y la contrasena son incorrectos.

            } else {
                Toast.makeText(Login.this, R.string.user_pass_incorrectos, Toast.LENGTH_SHORT)
                        .show();
            }

            progressDialog.dismiss();
        }

    }

        // Lanzar el activity Registro seleccionando la leyenda registrarse.
        public void registro(View view) {
            Intent intentRegistro = new Intent(this, Registro.class);
            startActivity(intentRegistro);
        }

    // Lanzar el activity Recuperar seleccionando la leyenda recuperar contrasena..
        public void recuperar(View view) {
            Intent intentMap = new Intent(getApplicationContext(), Recuperar.class);
            startActivity(intentMap);

        }

    }


