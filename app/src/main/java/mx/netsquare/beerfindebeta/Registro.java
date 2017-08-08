/*Esta clase permite al usuario egistrarse en la aplicacion para poder acceder a la misma.
La informacion que el usuario accesa es enviada a una base de datos.

Desarrrollada por Ricardo Ivan Macias Fusco y Daniel Emir Olivas Castro.
Fecha de Creacion: 16/Mayo/2017
Version 1.0(Version reciente en la clase Android Manifest)
Ultima Actualizacion: 16/Junio/2017
*/

package mx.netsquare.beerfindebeta;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Registro extends AppCompatActivity {

    //Se declaran los campos a llenar como nulos.
    private EditText username = null, email = null, password = null, telefono = null, password2 = null;
    private JSONParser jsonParser = null;
    private ProgressDialog progressDialog;
    private static String _url = null;

    //Se determinan como finales las variables que seran llenadas con la informacion
    //recopilada desde la base de datos.
    //String deben coincidir con los capos en la base de datos.
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_USERNAME  = "username";
    private static final String TAG_EMAIL   = "email";
    private static final String TAG_PASSWORD    = "password";
    private static final String TAG_TELEFONO = "telefono";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //Desde esta url se realizara el POST.
        _url = "http://beerfinderbeta.96.lt/webservice/create_usuario.php";
        jsonParser = new JSONParser();

        //Se relacionan los campos con las variables
        username  = (EditText)findViewById(R.id.editRegistroUsuario);
        email = (EditText)findViewById(R.id.editRegistroEmail);
        password  = (EditText)findViewById(R.id.editRegistroContrasena);
        password2 = (EditText)findViewById(R.id.editConfirmacionContrasena);
        telefono = (EditText)findViewById(R.id.editRegistroTelefono);


    }

    //Se crea un metodo para enviar la informacion de los campos a las variables.
    public void registrando(View view) {

        String username  = this.username.getText().toString();
        String email = this.email.getText().toString();
        String password  = this.password.getText().toString();
        String password2 = this.password2.getText().toString();
        String telefono = this.telefono.getText().toString();

        //Se verifica que la contrasena sea valida
        // para evitar perdida de contrasenas en el registro
        if (password.equals(password2))

        //Se genera el nuevo registro.
        new registrarNuevo().execute(username,email,password,telefono);

        //Toast.makeText(this, username+email+password+password2+telefono, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, R.string.pass_no_coincide, Toast.LENGTH_SHORT).show();

    }

    //Se crea un hilo para enviar la informacion a la BD.
    //El metodo es el mismo que el usado en la clase Agregando
    class registrarNuevo extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(Registro.this);
            progressDialog.setMessage(getString(R.string.creando_usuario));
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try{
                ArrayList<NameValuePair> Params = new ArrayList<NameValuePair>();
                Params.add(new BasicNameValuePair(TAG_USERNAME,params[0]));
                Params.add(new BasicNameValuePair(TAG_EMAIL,params[1]));
                Params.add(new BasicNameValuePair(TAG_PASSWORD,params[2]));
                Params.add(new BasicNameValuePair(TAG_TELEFONO,params[3]));


                JSONObject json = jsonParser.makeHttpRequest(_url,"POST",Params);

                int succ = json.getInt(TAG_SUCCESS);

                if(succ == 1){
                    Registro.this.finish();
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
            if(s.equals("ok")){
                Toast.makeText(Registro.this, R.string.usuario_no_registrado, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Registro.this, R.string.usuario_registrado, Toast.LENGTH_SHORT).show();
            }
        }

    }
}
