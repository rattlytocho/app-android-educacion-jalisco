package mx.gob.jalisco.portalsej.portalsej.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.viewpagerindicator.CirclePageIndicator;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mx.gob.jalisco.portalsej.portalsej.Calendar;
import mx.gob.jalisco.portalsej.portalsej.Contact;
import mx.gob.jalisco.portalsej.portalsej.R;
import mx.gob.jalisco.portalsej.portalsej.adapters.ImageSlideAdapter;
import mx.gob.jalisco.portalsej.portalsej.adapters.TweetAdapter;
import mx.gob.jalisco.portalsej.portalsej.objects.Slide;
import mx.gob.jalisco.portalsej.portalsej.objects.Tweet;
import mx.gob.jalisco.portalsej.portalsej.services.WebServices;
import mx.gob.jalisco.portalsej.portalsej.utils.NetworkUtils;


public class Inicio extends Fragment implements View.OnClickListener{

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    public static List<Slide> itemsSlide = new ArrayList<>();
    View V;
    NetworkUtils utils;
    private RecyclerView recycler;
    private TweetAdapter adapter;
    private RecyclerView.LayoutManager lManager;

    List<Tweet> items = new ArrayList<>();

    RelativeLayout launcher_calendar,launcher_cafetaría;

    public Inicio() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_inicio, container, false);
        V = view;

        recycler = (RecyclerView) view.findViewById(R.id.tweetsRecycler);
        recycler.setHasFixedSize(true);

        lManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(lManager);
        recycler.setNestedScrollingEnabled(false);

        utils = new NetworkUtils(getActivity());

        if(utils.isConnectingToInternet()){
            new getJson().execute(WebServices.HOST_SERVICES+"/"+WebServices.SLIDER);
            new getTweet().execute("http://edu.jalisco.gob.mx/e-gobierno/appservices/tweets/tweets_get.php");
        }

        launcher_calendar = (RelativeLayout) view.findViewById(R.id.launcher_calendar);
        launcher_calendar.setOnClickListener(this);
        launcher_cafetaría = (RelativeLayout) view.findViewById(R.id.launcher_cafetaría);
        launcher_cafetaría.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.launcher_calendar){
            Intent intent = new Intent(getContext(), Calendar.class);
            startActivity(intent);
        }else if(id == R.id.launcher_cafetaría){
            Intent intent = new Intent(getContext(), Contact.class);
            startActivity(intent);
        }
    }

    class getJson extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            itemsSlide = new ArrayList<>();
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
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObjectTweet = jsonArray.getJSONObject(i);

                        String SlideTitle = jsonObjectTweet.getString("title");
                        String SlideUrl = jsonObjectTweet.getString("field_url_al_sitio_web");
                        String SlideImage = jsonObjectTweet.getString("field_jssor_image");

                        itemsSlide.add(new Slide(SlideImage, SlideTitle, SlideUrl));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mPager = (ViewPager) V.findViewById(R.id.pager);

            mPager.setAdapter(new ImageSlideAdapter(getActivity(),itemsSlide));

            NUM_PAGES = itemsSlide.size();
            CirclePageIndicator indicator = (CirclePageIndicator)
                    V.findViewById(R.id.indicator);

            assert indicator != null;
            indicator.setViewPager(mPager);

            final float density = getResources().getDisplayMetrics().density;

            indicator.setRadius(5 * density);

            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                public void run() {
                    if (currentPage == NUM_PAGES) {
                        currentPage = 0;
                    }
                    mPager.setCurrentItem(currentPage++, true);
                }
            };
            /*Timer swipeTimer = new Timer();
            swipeTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(Update);
                }
            }, 5000,5000);*/

            // Pager listener over indicator
            indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    currentPage = position;
                }

                @Override
                public void onPageScrolled(int pos, float arg1, int arg2) {
                }

                @Override
                public void onPageScrollStateChanged(int pos) {
                }
            });
        }
    }

    class getTweet extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            items = new ArrayList<>();
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
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray jsonArray = jsonObject.getJSONArray("tweets");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObjectTweet = jsonArray.getJSONObject(i);

                        String tweetText = jsonObjectTweet.getString("text");
                        String tweetDate = jsonObjectTweet.getString("fecha");
                        String image = jsonObjectTweet.getString("imagen_perfil");
                        if(tweetText.indexOf("&quot;") > 0 ){
                            tweetText = tweetText.replaceAll("&quot;", "\"");
                        }

                        SpannableString hashText = new SpannableString(tweetText);
                        Matcher matcher = Pattern.compile("#([A-Za-z0-9_-]+)").matcher(hashText);
                        while (matcher.find()) {
                            hashText.setSpan(new ForegroundColorSpan(Color.rgb(22, 132, 180)), matcher.start(), matcher.end(), 0);
                        }
                        Linkify.addLinks(hashText, Linkify.WEB_URLS);

                        items.add(new Tweet(image, tweetText, tweetDate));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            adapter = new TweetAdapter(items,getActivity());

            recycler.setAdapter(adapter);
        }
    }

}
