package mx.gob.jalisco.portalsej.portalsej;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
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
import mx.gob.jalisco.portalsej.portalsej.adapters.NotificationAdapter;
import mx.gob.jalisco.portalsej.portalsej.objects.Notification;
import mx.gob.jalisco.portalsej.portalsej.services.NotifyService;
import mx.gob.jalisco.portalsej.portalsej.services.WebServices;
import mx.gob.jalisco.portalsej.portalsej.utils.NetworkUtils;
import mx.gob.jalisco.portalsej.portalsej.utils.Utils;

public class Notifications extends AppCompatActivity {

    public static final String PREF_NOTIFICATIONS_FIRST_TIME = "true";

    List<Notification> items;

    NotificationAdapter adapter;
    RecyclerView recycler;
    RecyclerView.LayoutManager lManager;
    NetworkUtils utils;
    SwipeRefreshLayout refresh;
    ProgressBar loader;

    String node_title;
    String cuerpo;
    String imagen;
    String id;
    String publicado;

    Boolean isUserFirstTime;

    RelativeLayout header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        header = (RelativeLayout) findViewById(R.id.header);
        isUserFirstTime = Boolean.valueOf(Utils.readSharedSetting(this, PREF_NOTIFICATIONS_FIRST_TIME, "true"));
        if(isUserFirstTime){
            Utils.saveSharedSetting(this, PREF_NOTIFICATIONS_FIRST_TIME,"false");
        }else{
            header.setVisibility(View.GONE);
        }

        refresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        loader = (ProgressBar) findViewById(R.id.loader);

        recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);

        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);

        utils = new NetworkUtils(this);
        if(utils.isConnectingToInternet()){
            new getJson().execute(WebServices.HOST_SERVICES+"/"+WebServices.NOTIFICACIONES);
        }

        refresh.setColorSchemeResources(R.color.colorPrimary);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(utils.isConnectingToInternet()){
                    new getJson().execute(WebServices.HOST_SERVICES+"/"+WebServices.NOTIFICACIONES);
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
                        JSONObject jsonObjectNotification = jsonArray.getJSONObject(i);

                        node_title = jsonObjectNotification.getString("title");
                        cuerpo = jsonObjectNotification.getString("body");
                        id = jsonObjectNotification.getString("nid");
                        imagen = jsonObjectNotification.getString("field_archivo");
                        publicado = jsonObjectNotification.getString("created");

                        items.add(new Notification(node_title,publicado,cuerpo,imagen));
                    }
                    adapter = new NotificationAdapter(items, getApplicationContext());
                    recycler.setAdapter(adapter);
                    refresh.setRefreshing(false);
                    loader.setVisibility(View.GONE);
                    recycler.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
