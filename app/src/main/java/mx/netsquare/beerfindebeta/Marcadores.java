package mx.netsquare.beerfindebeta;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dicky on 7/14/2017.
 */

public class Marcadores {

    public static List<Marcador> parseJsonToObject(String json){
        ArrayList<Marcador> marcadores = new ArrayList<Marcador>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                Marcador marcador = new Marcador();
                marcador.setLugar(jsonObj.getString("lugar"));
                marcador.setDescripcion(jsonObj.getString("descripcion"));
                marcador.setGm_latitud(jsonObj.getString("gm_lat"));
                marcador.setGm_longitud(jsonObj.getString("gm_lng"));
                marcadores.add(marcador);
            }
        } catch (JSONException e) {
            Log.e("Marcadores", "Error procesando el JSON");
        }
        return marcadores;

    }
}
