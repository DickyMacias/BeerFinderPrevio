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

    private EditText username = null, email = null, password = null, telefono = null, password2 = null;
    private JSONParser jsonParser = null;
    private ProgressDialog progressDialog;
    private static String _url = null;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_USERNAME  = "username";
    private static final String TAG_EMAIL   = "email";
    private static final String TAG_PASSWORD    = "password";
    private static final String TAG_TELEFONO = "telefono";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        _url = "http://beerfinderbeta.96.lt/webservice/create_usuario.php";
        jsonParser = new JSONParser();

        username  = (EditText)findViewById(R.id.editRegistroUsuario);
        email = (EditText)findViewById(R.id.editRegistroEmail);
        password  = (EditText)findViewById(R.id.editRegistroContrasena);
        password2 = (EditText)findViewById(R.id.editConfirmacionContrasena);
        telefono = (EditText)findViewById(R.id.editRegistroTelefono);


    }

    public void registrando(View view) {

        String username  = this.username.getText().toString();
        String email = this.email.getText().toString();
        String password  = this.password.getText().toString();
        String password2 = this.password2.getText().toString();
        String telefono = this.telefono.getText().toString();

        if (password.equals(password2))
        new registrarNuevo().execute(username,email,password,telefono);

        //Toast.makeText(this, username+email+password+password2+telefono, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, R.string.pass_no_coincide, Toast.LENGTH_SHORT).show();

    }

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
                Toast.makeText(Registro.this, R.string.usuario_registrado, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Registro.this, R.string.usuario_no_registrado, Toast.LENGTH_SHORT).show();
            }
        }

    }
}
