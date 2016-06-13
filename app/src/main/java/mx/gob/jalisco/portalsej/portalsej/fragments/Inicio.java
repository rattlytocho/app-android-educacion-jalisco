package mx.gob.jalisco.portalsej.portalsej.fragments;

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
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;

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
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mx.gob.jalisco.portalsej.portalsej.R;
import mx.gob.jalisco.portalsej.portalsej.adapters.ImageSlideAdapter;
import mx.gob.jalisco.portalsej.portalsej.adapters.TweetAdapter;
import mx.gob.jalisco.portalsej.portalsej.objects.Slide;
import mx.gob.jalisco.portalsej.portalsej.objects.Tweet;
import mx.gob.jalisco.portalsej.portalsej.services.WebServices;
import mx.gob.jalisco.portalsej.portalsej.utils.NetworkUtils;


@SuppressWarnings("ALL")
public class Inicio extends Fragment {

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

    // ELEMENTOS DEL ÚLTIMO TWEET
    TextView textTweet;
    TextView date;
    SpannableString TweetText;
    String TweetDate;

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

        //textTweet = (TextView) view.findViewById(R.id.textTweet);
        //date  = (TextView) view.findViewById(R.id.date);

        utils = new NetworkUtils(getActivity());

        if(utils.isConnectingToInternet()){
            new DataFetcherTask().execute();
            new DataSlider().execute();
        }
        return view;
    }

    class DataFetcherTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            items.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String serverData = null;// String object to store fetched data from server
            // Http Request Code start
            DefaultHttpClient httpClient = new DefaultHttpClient();
            String URLSERVICES = WebServices.HOST_SERVICES+"/"+WebServices.SLIDER;
            HttpGet httpGet = new HttpGet(URLSERVICES);
            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                serverData = EntityUtils.toString(httpEntity);
                //Log.d("response", serverData);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                JSONArray jsonArray = new JSONArray(serverData);
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObjectTweet = jsonArray.getJSONObject(i);

                    String SlideTitle = jsonObjectTweet.getString("title");
                    String SlideUrl = jsonObjectTweet.getString("field_url_al_sitio_web");
                    String SlideImage = jsonObjectTweet.getString("field_image");

                    itemsSlide.add(new Slide(SlideImage, SlideTitle, SlideUrl));
                }
                //adapter = new TweetAdapter(items,getActivity());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            mPager = (ViewPager) V.findViewById(R.id.pager);


            mPager.setAdapter(new ImageSlideAdapter(getActivity(),itemsSlide));

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
            Timer swipeTimer = new Timer();
            swipeTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(Update);
                }
            }, 1000,1000);

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

    class DataSlider extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            itemsSlide.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String serverData = null;
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("http://estudiaen.jalisco.gob.mx/appservices/tweets/tweets_get.php");
            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                serverData = EntityUtils.toString(httpEntity);
                //Log.d("response", serverData);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = new JSONObject(serverData);
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
                //adapter = new TweetAdapter(items,getActivity());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new TweetAdapter(items,getActivity());

            recycler.setAdapter(adapter);

            /*textTweet.setText(TweetText);
            date.setText(TweetDate);*/

        }
    }

}
