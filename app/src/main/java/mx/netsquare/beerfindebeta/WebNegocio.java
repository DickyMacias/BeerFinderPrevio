/*En esta clase se crea un ArrayList generico que contiene un objeto de tipo
 venta. En este objeto se deserializa el JSON.

Desarrrollada por Ricardo Ivan Macias Fusco y Daniel Emir Olivas Castro.
Fecha de Creacion: 07/Agosto/2017
Version 1.0(Version reciente en la clase Android Manifest)
Ultima Actualizacion: 08/Agosto/2017
*/

package mx.netsquare.beerfindebeta;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebNegocio extends Activity {

    private WebView mWebview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWebview  = new WebView(this);

        // activar javascript
        mWebview.getSettings().setJavaScriptEnabled(true);

        // declarar actividad
        final Activity activity = this;

        // se configura el cliente con funciones de la clase WebView
        // Selecciona la vista y carga desde donde se llenara
        mWebview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            }
        });

        //Se arrastra la informacion del intent anterior para obtener la url
        Intent intent = getIntent();
        mWebview .loadUrl(intent.getStringExtra("web"));
        setContentView(mWebview);
    }

    // Al seleccionar atras se termina el intent
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
