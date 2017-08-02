package mx.netsquare.beerfindebeta;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends Activity {
    private MenuItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ListView lista = (ListView) findViewById(R.id.list4);
        ArrayList<elemento> arraydir = new ArrayList< elemento>();
        elemento item;

        // Introduzco los datos
        item = new elemento(getResources().getDrawable(R.drawable.ic_explore_black_48dp), "Vectorial");
        arraydir.add(item);
        item = new elemento(getResources().getDrawable(R.drawable.ic_local_bar_black_48dp), "Bitmap");
        arraydir.add(item);
        item = new elemento(getResources().getDrawable(R.drawable.ic_room_black_48dp), "3D");
        arraydir.add(item);

        //ADAPTER
        MenuAdapter adaptador = new MenuAdapter(this, arraydir);
        lista.setAdapter(adaptador);
    }
}
