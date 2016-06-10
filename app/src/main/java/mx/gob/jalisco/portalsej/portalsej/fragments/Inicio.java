package mx.gob.jalisco.portalsej.portalsej.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import mx.gob.jalisco.portalsej.portalsej.R;
import mx.gob.jalisco.portalsej.portalsej.adapters.ImageSlideAdapter;


public class Inicio extends Fragment {

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final String[] URLIMAGES = {"http://se.jalisco.gob.mx/sites/se.jalisco.gob.mx/files/styles/slideshow_full1200x/public/dsc_0008_0.jpg?itok=NlFyAYv_", "http://se.jalisco.gob.mx/sites/se.jalisco.gob.mx/files/styles/slideshow_full1200x/public/21-04-16_da-a_de_la_educadora_3_0.jpg?itok=lpPZcnBa", "http://se.jalisco.gob.mx/sites/se.jalisco.gob.mx/files/styles/slideshow_full1200x/public/18-04-16_cambios_de_adscripcion_2.jpg?itok=syB0oj1U","http://se.jalisco.gob.mx/sites/se.jalisco.gob.mx/files/styles/slideshow_full1200x/public/16_03_16_r.p._resultados_de_la_evaluacion_al_desempeno_3_2.jpg?itok=plCogZIg","http://se.jalisco.gob.mx/sites/se.jalisco.gob.mx/files/styles/slideshow_full1200x/public/foto_boletin_281_1.jpg?itok=cCOGBbFt"};
    private ArrayList<String> ImagesUrlArray = new ArrayList<String>();

    public Inicio() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);

        /*  ADD STATIC RESOURCE DRAWABLE
        for(int i=0;i<IMAGES.length;i++){
            ImagesArray.add(IMAGES[i]);
        }*/

        ImagesUrlArray.clear();

        for(int i=0;i<URLIMAGES.length;i++){
            ImagesUrlArray.add(URLIMAGES[i]);
        }

        mPager = (ViewPager) view.findViewById(R.id.pager);

        mPager.setAdapter(new ImageSlideAdapter(getActivity(),ImagesUrlArray));

        CirclePageIndicator indicator = (CirclePageIndicator)
                view.findViewById(R.id.indicator);

        assert indicator != null;
        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

        indicator.setRadius(5 * density);

        NUM_PAGES = URLIMAGES.length;

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
        }, 10000, 10000);

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

        return view;
    }

}
