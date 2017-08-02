package mx.netsquare.beerfindebeta;

/**
 * Created by dicky on 8/1/2017.
 */

import android.graphics.drawable.Drawable;

/**
 * Created by dicky on 7/31/2017.
 */

public class elemento {

    public Drawable imagen;
    public String texto;
    public long id;

    //CONSTRUCTOR2
    public elemento(Drawable img,String text) {
        super();
        this.texto =text;
        this.imagen=img;
    }

    //CONSTRUCTOR 1
    public elemento(Drawable img,String text,long id) {
        super();
        this.texto =text;
        this.imagen=img;
        this.id = id;

    }
    public Drawable getImagen() {
        return imagen;
    }
    public void setImagen(Drawable imagen) {
        this.imagen = imagen;
    }
    public String getTexto() {
        return texto;
    }
    public void setTexto(String texto) {
        this.texto = texto;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
}

