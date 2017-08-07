package mx.netsquare.beerfindebeta;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class Inicio extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

    // Nuevo Handler para iniciar el Login
    // Cerrar la actividad inicio despues del tiempo designado
    new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
            //Crear intento para lanzar login
            Intent intentLogin = new Intent(Inicio.this, Login.class);
            Inicio.this.startActivity(intentLogin);
            Inicio.this.finish();
        }
    }, SPLASH_DISPLAY_LENGTH);
    }


}
