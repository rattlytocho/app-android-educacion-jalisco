package mx.gob.jalisco.portalsej.portalsej;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

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
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.gob.jalisco.portalsej.portalsej.adapters.ConvocatoriaAdapter;
import mx.gob.jalisco.portalsej.portalsej.objects.Convocatoria;
import mx.gob.jalisco.portalsej.portalsej.services.WebServices;
import mx.gob.jalisco.portalsej.portalsej.utils.NetworkUtils;

public class MostConsulted extends AppCompatActivity {

    List<Convocatoria> items;
    ConvocatoriaAdapter adapter;
    RecyclerView recycler;
    RecyclerView.LayoutManager lManager;
    NetworkUtils utils;
    SwipeRefreshLayout refresh;
    ProgressBar loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_most_consulted);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        refresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        refresh.setColorSchemeResources(R.color.colorPrimary);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);
        recycler.setNestedScrollingEnabled(false);

        loader = (ProgressBar) findViewById(R.id.loader);

        utils = new NetworkUtils(this);
        if(utils.isConnectingToInternet()){
            new getJson().execute(WebServices.HOST_SERVICES+"/"+WebServices.MOST_CONSULTED);
        }

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(utils.isConnectingToInternet()){
                    new getJson().execute(WebServices.HOST_SERVICES+"/"+WebServices.MOST_CONSULTED);
                }
            }
        });

    }


    class getJson extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            items = new ArrayList<>();
            refresh.setRefreshing(false);
            recycler.setVisibility(View.GONE);
            loader.setVisibility(View.VISIBLE);
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
                connection.setConnectTimeout(100000);
                connection.setReadTimeout(100000);
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
                if(json != null) {
                    JSONArray jsonArray = new JSONArray(json);

                    for (int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObjectC = jsonArray.getJSONObject(i);

                        String title = jsonObjectC.getString("title");
                        String field_fecha_de_publicacion = jsonObjectC.getString("field_fecha_de_publicacion");
                        String field_fecha_de_vencimiento = jsonObjectC.getString("field_fecha_de_vencimiento");
                        String field_nivel = jsonObjectC.getString("field_nivel");
                        String field_perfil = jsonObjectC.getString("field_perfil");
                        String body = jsonObjectC.getString("body");
                        String field_archivo = jsonObjectC.getString("field_archivo");
                        String view_node = jsonObjectC.getString("field_url_al_sitio_web");

                    /*
                    REPLACE SCAPE CHARACTERS TO SLASH
                    if(var.indexOf("&quot;") > 0 ){
                        var = var.replaceAll("&quot;", "\"");
                    }*/

                        items.add(new Convocatoria(title, field_fecha_de_publicacion, field_fecha_de_vencimiento, field_nivel, field_perfil, body, field_archivo, view_node));
                    }
                    adapter = new ConvocatoriaAdapter(items, getApplicationContext());

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            adapter = new ConvocatoriaAdapter(items, getApplicationContext());
            recycler.setAdapter(adapter);
            refresh.setRefreshing(false);
            loader.setVisibility(View.GONE);
            recycler.setVisibility(View.VISIBLE);
        }
    }

}
