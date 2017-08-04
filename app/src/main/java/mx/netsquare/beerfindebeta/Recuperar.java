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

        new MailJob(user, passwd).execute(
                new MailJob.Mail("netsquare,mx@gmail.com", destinatario, "subjeto", "contenido")
        );
    }
}
