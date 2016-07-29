package mx.gob.jalisco.portalsej.portalsej.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mx.gob.jalisco.portalsej.portalsej.R;
import mx.gob.jalisco.portalsej.portalsej.adapters.ConvocatoriaAdapter;
import mx.gob.jalisco.portalsej.portalsej.objects.Convocatoria;
import mx.gob.jalisco.portalsej.portalsej.services.WebServices;
import mx.gob.jalisco.portalsej.portalsej.utils.NetworkUtils;

public class Alumnos extends Fragment {
    List<Convocatoria> items;
    ConvocatoriaAdapter adapter;
    RecyclerView recycler;
    RecyclerView.LayoutManager lManager;
    NetworkUtils utils;
    SwipeRefreshLayout refresh;
    ProgressBar loader;
    ImageButton closeRecipe;

    public Alumnos() {
        // Required empty public constructor
    }

    public static Alumnos newInstance(String param1, String param2) {
        Alumnos fragment = new Alumnos();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_alumnos, container, false);

        recycler = (RecyclerView) view.findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);

        lManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(lManager);
        recycler.setNestedScrollingEnabled(false);

        refresh = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        loader = (ProgressBar) view.findViewById(R.id.loader);


        utils = new NetworkUtils(getActivity());
        if(utils.isConnectingToInternet()){
            new DataFetcherTask().execute();
        }

        refresh.setColorSchemeResources(R.color.colorPrimary);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(utils.isConnectingToInternet()){
                    new DataFetcherTask().execute();
                }
            }
        });

        return view;
    }
    class DataFetcherTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            items = new ArrayList<>();
            refresh.setRefreshing(false);
            recycler.setVisibility(View.GONE);
            loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            String serverData = null;// String object to store fetched data from server
            // Http Request Code start
            DefaultHttpClient httpClient = new DefaultHttpClient();
            String URLSERVICES = WebServices.HOST_SERVICES+"/"+WebServices.PERFILES[0]+"/"+WebServices.CONVOTATORIAS;
            HttpGet httpGet = new HttpGet(URLSERVICES);
            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                serverData = EntityUtils.toString(httpEntity);
                Log.d("Response Data", serverData);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                JSONArray jsonArray = new JSONArray(serverData);

                for (int i=0;i<jsonArray.length();i++) {
                    JSONObject jsonObjectC = jsonArray.getJSONObject(i);

                    String title = jsonObjectC.getString("title");
                    String field_fecha_de_publicacion = jsonObjectC.getString("field_fecha_de_publicacion");
                    String field_fecha_de_vencimiento = jsonObjectC.getString("field_fecha_de_vencimiento");
                    String field_nivel = jsonObjectC.getString("field_nivel");
                    String field_perfil = jsonObjectC.getString("field_perfil");
                    String body = jsonObjectC.getString("body");
                    String field_archivo = jsonObjectC.getString("field_archivo");
                    String view_node = jsonObjectC.getString("path");

                    /*
                    REPLACE SCAPE CHARACTERS TO SLASH
                    if(var.indexOf("&quot;") > 0 ){
                        var = var.replaceAll("&quot;", "\"");
                    }*/

                    items.add(new Convocatoria(title, field_fecha_de_publicacion, field_fecha_de_vencimiento, field_nivel, field_perfil, body, field_archivo, view_node));
                }
                adapter = new ConvocatoriaAdapter(items, getContext());


            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new ConvocatoriaAdapter(items, getContext());
            recycler.setAdapter(adapter);
            refresh.setRefreshing(false);
            loader.setVisibility(View.GONE);
            recycler.setVisibility(View.VISIBLE);
        }
    }

}
