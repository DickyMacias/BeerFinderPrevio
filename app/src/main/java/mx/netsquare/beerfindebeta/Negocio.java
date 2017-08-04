package mx.netsquare.beerfindebeta;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * Created by dicky on 8/1/2017.
 */

public class Negocio implements Serializable{


    public String URLImagen;
    public Drawable imagen;
    public String lugar;
    public String descripcion;
    public long id;

    public String toString() {
        return "--> " + lugar + " " + descripcion + " " + URLImagen;
    }
    //PUEDE GENERARSE CONSTRUCTOR

    public String getURLImagen() {
        return URLImagen;
    }

    public void setURLImagen(String URLImagen) {
        this.URLImagen = URLImagen;
    }

    public Drawable getImagen() {
        return imagen;
    }

    public void setImagen(Drawable imagen) {
        this.imagen = imagen;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }



}
