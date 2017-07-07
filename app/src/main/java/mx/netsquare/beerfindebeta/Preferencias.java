package mx.netsquare.beerfindebeta;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class Preferencias extends AppCompatActivity {

    private String ip = null;
    static final String PREFS_URL = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ip = "192.168.56.1:9090";
        SharedPreferences preferencias = getSharedPreferences(PREFS_URL,0);
    }



    public void Guardar(View view) {
        SharedPreferences preferencias = getSharedPreferences(PREFS_URL,0);

        SharedPreferences.Editor editor = preferencias.edit();
        editor.putString("siteIP",ip);

        editor.putString("script_productos","create_place.php");


        editor.commit();
        finish();
    }
}
