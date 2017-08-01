package mx.netsquare.beerfindebeta;

import java.io.Serializable;

/**
 * Created by dicky on 7/14/2017.
 */

public class Marcador implements Serializable{

    private int id;
    private String lugar;
    private String descripcion;
//    private String coordenadas;
//    private String horario;
    private String gm_latitud;
    private String gm_longitud;


    @Override
    public String toString() {
        return "--> " + lugar + " " + descripcion + " " + gm_latitud + " " + gm_longitud;
     }

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

//    public String getCoordenadas() {
//        return coordenadas;
//    }

//    public void setCoordenadas(String coordenadas) {
//        this.coordenadas = coordenadas;
//    }
//
//    public String getHorario() {
//        return horario;
//    }
//
//    public void setHorario(String horario) {
//        this.horario = horario;
//    }

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
