package mx.netsquare.beerfindebeta;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by dicky on 8/1/2017.
 */

public class NegociosAdapter extends BaseAdapter {

    protected Activity activity;
    //ARRAYLIST CON TODOS LOS ITEMS
    protected ArrayList<Negocio> items;

    //CONSTRUCTOR
    public NegociosAdapter(Activity activity, ArrayList<Negocio> items) {
        this.activity = activity;
        this.items = items;
    }
    //CUENTA LOS ELEMENTOS
    @Override
    public int getCount() {
        return items.size();
    }
    //DEVUELVE UN OBJETO DE UNA DETERMINADA POSICION
    @Override
    public Object getItem(int arg0) {
        return items.get(arg0);
    }
    //DEVUELVE EL ID DE UN ELEMENTO
    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }
    //METODO PRINCIPAL, AQUI SE LLENAN LOS DATOS
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // SE GENERA UN CONVERTVIEW POR MOTIVOS DE EFICIENCIA DE MEMORIA
        //ES UN NIVEL MAS BAJO DE VISTA, PARA QUE OCUPEN MENOS MEMORIA LAS
//        IMAGENES
        View v = convertView;

        //ASOCIAMOS LA VISTA AL LAYOUT DEL RECURSO XML DONDE ESTA LA BASE DE
//        CADA ITEM
        if(convertView == null){
            LayoutInflater inf = (LayoutInflater) activity.getSystemService

                    (Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.negocio_item, null);
        }

        Negocio dir = items.get(position);
        //RELLENAMOS LA IMAGEN Y EL TEXTO


 //       Picasso.with(this.context)
   //             .load(dir.getURLImagen())
     //           .placeholder(R.drawable.tarro_iphone1)
       //         .error(R.drawable.tarro_iphone3)
         //       .into(R.id.NegocioImagen);

    //    ImageView foto = (ImageView) v.findViewById(R.id.NegocioImagen);
      //  foto.setImageDrawable(dir.getImagen());
        TextView lugar = (TextView) v.findViewById(R.id.NegocioNombre);
        lugar.setText(dir.getLugar());
        TextView desc = (TextView) v.findViewById(R.id.NegocioDireccion);
        desc.setText(dir.getDescripcion());

        // DEVOLVEMOS VISTA
        return v;
    }
}
