/*En esta clase se crea un ArrayList generico que contiene un objeto de tipo
 marcador. En este objeto se deserializa el JSON.

Desarrrollada por Ricardo Ivan Macias Fusco y Daniel Emir Olivas Castro.
Fecha de Creacion: 16/Junio/2017
Version 1.0(Version reciente en la clase Android Manifest)
Ultima Actualizacion: 25/Julio/2017
*/

package mx.netsquare.beerfindebeta;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Marcadores {

    public static ArrayList<Marcador> parseJsonToObject(JSONArray arrJson){

        //Se genera un ArrayList de tipo Marcador.
        ArrayList<Marcador> marcadores = new ArrayList<Marcador>();


        try {
            //Se recorre un JSONArray el cual se transforma en JSONObject.
            for (int i = 0; i < arrJson.length(); i++) {
                JSONObject jsonObj = arrJson.getJSONObject(i);

                Log.e("Info", "--------------- Ini Json ------------------");

                //Se asignan los valores con la informamcion generada desde la consulta PHP.
                //Los strings deben coincidir con los campos en la base de datos.
                Marcador marcador = new Marcador();
                marcador.setLugar(jsonObj.getString("lugar"));
                marcador.setDescripcion(jsonObj.getString("descripcion"));
                marcador.setGm_latitud(jsonObj.getString("gm_lat"));
                marcador.setGm_longitud(jsonObj.getString("gm_lng"));

                Log.e("Info", marcador.toString());

                Log.e("Info", "-------------------------------------------");

                //Se genera un arreglo marcadores que contiene un Arreglo
                marcadores.add(marcador);
            }
        } catch (JSONException e) {
            Log.e("Marcadores", "Error procesando el JSON");
        }

        Log.e("Marcadores count", "" + marcadores.size());

        return marcadores;

    }
}
