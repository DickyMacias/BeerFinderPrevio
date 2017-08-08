/*Esta clase permite realizar una consulta para recuperar una contrasena y enviar un email
al usuario con su nuevo password.
NO ESTA ACTUALMENTE DESARROLLADA

Desarrollada por Ricardo Ivan Macias Fusco y Daniel Emir Olivas Castro.
Fecha de Creacion: 1/Agosto/2017
Version 1.0(Version reciente en la clase Android Manifest)
Ultima Actualizacion: 08/Agosto/2017
*/

package mx.netsquare.beerfindebeta;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Recuperar extends AppCompatActivity {

    String user;
    String passwd;
    String destinatario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar);

    //Generar consulta para traer destinatario desde TextArea
    }
}
