/*En esta clase se crea objeto de tipo Marcador para almacenar los datos del JSON cuando se llama desde una
consulta remota. Implementa la clase serializable para poder descomponer el JSON.

Desarrrollada por Ricardo Ivan Macias Fusco y Daniel Emir Olivas Castro.
Fecha de Creacion: 16/Junio/2017
Version 1.0(Version reciente en la clase Android Manifest)
Ultima Actualizacion: 25/Julio/2017
*/

package mx.netsquare.beerfindebeta;

import java.io.Serializable;


public class Marcador implements Serializable{

    //Se crean las variables que se utilizaran como campos tanto en el PHP como en la BD.
    private int id;
    private String lugar;
    private String descripcion;
    private String gm_latitud;
    private String gm_longitud;

    //Se generan lso getters y setters para cada atributo.
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getGm_latitud() {
        return gm_latitud;
    }

    public void setGm_latitud(String gm_latitud) {
        this.gm_latitud = gm_latitud;
    }

    public String getGm_longitud() {
        return gm_longitud;
    }

    public void setGm_longitud(String gm_longitud) {
        this.gm_longitud = gm_longitud;
    }


}
