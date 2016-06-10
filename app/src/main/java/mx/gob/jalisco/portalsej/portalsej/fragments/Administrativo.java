package mx.gob.jalisco.portalsej.portalsej.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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

public class Administrativo extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    List<Convocatoria> items;
    ConvocatoriaAdapter adapter;
    RecyclerView recycler;
    RecyclerView.LayoutManager lManager;
    NetworkUtils utils;
    SwipeRefreshLayout refresh;
    Snackbar snackbar;
    ProgressBar loader;
    ImageButton closeRecipe;

    public Administrativo() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Administrativo.
     */
    // TODO: Rename and change types and number of parameters
    public static Administrativo newInstance(String param1, String param2) {
        Administrativo fragment = new Administrativo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_administrativo, container, false);

        recycler = (RecyclerView) view.findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);

        lManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(lManager);
        recycler.setNestedScrollingEnabled(false);

        refresh = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        loader = (ProgressBar) view.findViewById(R.id.loader);

        new DataFetcherTask().execute();

        utils = new NetworkUtils(getActivity());
        if(utils.isConnectingToInternet()){
        }
        else{
            snackbar.show();
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
            String URLSERVICES = WebServices.HOST_SERVICES+"/"+WebServices.PERFILES[1]+"/"+WebServices.CONVOTATORIAS;
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
                    String view_node = jsonObjectC.getString("view_node");

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
