/*Esta clase es la bienvenida a la aplicacion.
Su unica funcionalidad es hacer mas visual el acceso y permitir
que se carguen datos si se necesitara llamar algun servicio web.

Desarrrollada por Ricardo Ivan Macias Fusco y Daniel Emir Olivas Castro.
Fecha de Creacion: 16/Mayo/2017
Version 1.0(Version reciente en la clase Android Manifest)
Ultima Actualizacion: 14/Junio/2017
*/

package mx.netsquare.beerfindebeta;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Inicio extends AppCompatActivity {

    //Se declara el tiempo que durara en pasar a la siguiente actividad.
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
