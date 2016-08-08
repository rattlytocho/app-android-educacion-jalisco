package mx.gob.jalisco.portalsej.portalsej;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.gob.jalisco.portalsej.portalsej.adapters.SearchAdapter;
import mx.gob.jalisco.portalsej.portalsej.objects.ItemSearch;
import mx.gob.jalisco.portalsej.portalsej.services.WebServices;

public class DetailConvocatoria extends AppCompatActivity implements View.OnClickListener {

    String Title;
    String Field_fecha_de_publicacion;
    String Field_fecha_de_vencimiento;
    String Field_nivel;
    String Field_perfil;
    String Body;
    String Field_archivo;
    String View_node;

    TextView title;
    TextView field_fecha_de_publicacion;
    TextView field_fecha_de_vencimiento;
    TextView body;

    Button download;

    String TYPE;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_convocatoria);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        field_fecha_de_publicacion = (TextView) findViewById(R.id.field_fecha_de_publicacion);
        field_fecha_de_vencimiento = (TextView) findViewById(R.id.field_fecha_de_vencimiento);
        body = (TextView) findViewById(R.id.body);
        title = (TextView) findViewById(R.id.title);

        bundle = this.getIntent().getExtras();

        TYPE = bundle.getString("TYPE");

        if(!Objects.equals(TYPE, "SEARCH")){
            Title = bundle.getString("TITLE");
            Field_fecha_de_publicacion = bundle.getString("FIELD_FECHA_DE_PUBLICACION");
            Field_fecha_de_vencimiento = bundle.getString("FIELD_FECHA_DE_VENCIMIENTO");
            Field_nivel = bundle.getString("FIELD_NIVEL");
            Field_perfil = bundle.getString("FIELD_PERFIL");
            Body = bundle.getString("BODY");
            Field_archivo = bundle.getString("FIELD_ARCHIVO");
            View_node = bundle.getString("VIEW_NODE");

            title.setText(Html.fromHtml(Title));
            field_fecha_de_publicacion.setText("De: " + Field_fecha_de_publicacion + " a: ");
            field_fecha_de_vencimiento.setText(Field_fecha_de_vencimiento);

            body.setMovementMethod(LinkMovementMethod.getInstance());
            body.setText(Html.fromHtml(Body));
        }else{
            new getNode().execute(WebServices.HOST_SERVICES+"/"+WebServices.SEARHC_NODE+"?nid="+bundle.getString("NID"));
        }
        download = (Button) findViewById(R.id.download_file);
        assert download != null;
        download.setOnClickListener(this);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Detalle de Convocatoria");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent share = new Intent();
                share.setAction(Intent.ACTION_SEND);
                share.putExtra(Intent.EXTRA_TEXT, WebServices.ROOT_PATH + View_node);
                share.setType("text/plain");
                startActivity(Intent.createChooser(share, "Compartir por..."));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present
        if(Field_archivo!=""){
            getMenuInflater().inflate(R.menu.menu_detail_convocatoria, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity fade_in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(WebServices.ROOT_PATH +Field_archivo));
                startActivity(browserIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.download_file) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(WebServices.ROOT_PATH + Field_archivo));
                startActivity(browserIntent);
        }
    }

    class getNode extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            try {
                URL u = new URL(params[0]);
                connection = (HttpURLConnection) u.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-length", "0");
                connection.setUseCaches(false);
                connection.setAllowUserInteraction(false);
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                connection.connect();
                int status = connection.getResponseCode();

                switch (status) {
                    case 200:
                    case 201:
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line+"\n");
                        }
                        br.close();
                        return sb.toString();
                }

            } catch (MalformedURLException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (connection != null) {
                    try {
                        connection.disconnect();
                    } catch (Exception ex) {
                        Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            return null;
        }

        protected void onPostExecute(String json) {
            try {
                JSONArray jsonArray = new JSONArray(json);
                for (int i=0;i<jsonArray.length();i++) {
                    JSONObject jsonObjectNode = jsonArray.getJSONObject(i);
                    Field_archivo = jsonObjectNode.getString("field_archivo");
                    View_node = "/node/"+bundle.getString("NID");
                    title.setText(Html.fromHtml(jsonObjectNode.getString("title")));
                    field_fecha_de_publicacion.setText("De: " + jsonObjectNode.getString("field_fecha_de_publicacion") + " a: ");
                    field_fecha_de_vencimiento.setText(jsonObjectNode.getString("field_fecha_de_vencimiento"));
                    body.setMovementMethod(LinkMovementMethod.getInstance());
                    body.setText(Html.fromHtml(jsonObjectNode.getString("body")));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
