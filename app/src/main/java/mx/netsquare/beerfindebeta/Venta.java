package mx.netsquare.beerfindebeta;

import java.io.Serializable;


public class Venta implements Serializable{

    private int id;
    private int BeerID;
    private int PlaceID;
//    private String BeerName;
    private String lugar;
    private String descripcion;
    private String gm_latitud;
    private String gm_longitud;


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

    public int getBeerID() {
        return BeerID;
    }

    public void setBeerID(int beerID) {
        BeerID = beerID;
    }

    public int getPlaceID() {
        return PlaceID;
    }

    public void setPlaceID(int placeID) {
        PlaceID = placeID;
    }
}

