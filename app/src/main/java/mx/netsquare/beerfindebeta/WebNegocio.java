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
