package mx.netsquare.beerfindebeta;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Ventas {

    public static ArrayList<Venta> parseJsonToObject(JSONArray arrJson){

        ArrayList<Venta> ventas = new ArrayList<Venta>();

        try {
            for (int i = 0; i < arrJson.length(); i++) {
                JSONObject jsonObj = arrJson.getJSONObject(i);

                Log.e("Info", "--------------- Ini Json ------------------");

                Venta venta = new Venta();
                venta.setBeerID(Integer.parseInt(jsonObj.getString("BeerID")));
                venta.setPlaceID(Integer.parseInt(jsonObj.getString("PlaceID")));
                venta.setLugar(jsonObj.getString("lugar"));
                venta.setDescripcion(jsonObj.getString("descripcion"));
                venta.setGm_latitud(jsonObj.getString("gm_lat"));
                venta.setGm_longitud(jsonObj.getString("gm_lng"));
                //venta.setGm_longitud(jsonObj.getString("BeerName"));

                Log.e("Info", venta.toString());

                Log.e("Info", "-------------------------------------------");

                ventas.add(venta);
            }
        } catch (JSONException e) {
            Log.e("Ventas", "Error procesando el JSON");
        }

        Log.e("Ventas count", "" + ventas.size());

        return ventas;

    }
}