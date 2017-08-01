package mx.netsquare.beerfindebeta;

import android.graphics.Bitmap;

import java.util.HashMap;

/**
 * Created by dicky on 7/11/2017.
 */

public class Cervezas extends HashMap<String, String> {

    private String Nombre;
    private String Grados;
    private String Tipo;
    private String Casa;
    private String Desc;
    private String URLimagen;
    public Bitmap imagen;

         public Cervezas() {
             super();
         }

        public String getNombre() {
            return Nombre;
        }

        public void setNombre(String tagNombre, String nombre) {
            Nombre = nombre;
        }

        public String getGrados() {
            return Grados;
        }

        public void setGrados(String tagGrado, String grados) {
            Grados = grados;
        }

        public String getTipo() {
            return Tipo;
        }

        public void setTipo(String tagTipo, String tipo) {
            Tipo = tipo;
        }

        public String getCasa() {
            return Casa;
        }

        public void setCasa(String tagCasa, String casa) {
            Casa = casa;
        }

        public String getDesc() {
            return Desc;
        }

        public void setDesc(String desc) {
            Desc = desc;
        }

        public String getURLimagen() {
            return URLimagen;
        }

        public void setURLimagen(String tagImagen, String uRLimagen) {
            URLimagen = uRLimagen;
        }
        public Bitmap getImagen() {
            // TODO Auto-generated method stub
            return imagen;
        }
    }

