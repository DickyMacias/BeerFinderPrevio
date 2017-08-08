/*En esta clase se crea un ArrayList generico que contiene un objeto de tipo
 venta. En este objeto se deserializa el JSON.

Desarrrollada por Ricardo Ivan Macias Fusco y Daniel Emir Olivas Castro.
Fecha de Creacion: 07/Agosto/2017
Version 1.0(Version reciente en la clase Android Manifest)
Ultima Actualizacion: 08/Agosto/2017
*/

package mx.netsquare.beerfindebeta;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Ventas {

    public static ArrayList<Venta> parseJsonToObject(JSONArray arrJson){

        //Se genera un ArrayList de tipo Venta.
        ArrayList<Venta> ventas = new ArrayList<Venta>();

        try {
            //Se recorre un JSONArray el cual se transforma en JSONObject.
            for (int i = 0; i < arrJson.length(); i++) {
                JSONObject jsonObj = arrJson.getJSONObject(i);

                Log.e("Info", "--------------- Ini Json ------------------");

                //Se asignan los valores con la informamcion generada desde la consulta PHP.
                //Los strings deben coincidir con los campos en la base de datos.
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

                //Se genera un arreglo ventas que contiene un Arreglo
                ventas.add(venta);
            }
        } catch (JSONException e) {
            Log.e("Ventas", "Error procesando el JSON");
        }

        Log.e("Ventas count", "" + ventas.size());

        return ventas;

    }
}