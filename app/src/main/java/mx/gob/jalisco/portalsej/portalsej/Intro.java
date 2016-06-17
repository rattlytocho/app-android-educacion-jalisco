package mx.gob.jalisco.portalsej.portalsej;

import android.animation.ArgbEvaluator;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import mx.gob.jalisco.portalsej.portalsej.utils.Utils;

public class Intro extends AppCompatActivity {

    SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    ImageButton mNextBtn;
    Button mSkipBtn, mFinishBtn;

    ImageView zero, one, two, three;
    ImageView[] indicators;

    int lastLeftValue = 0;

    CoordinatorLayout mCoordinator;


    static final String TAG = "PagerActivity";

    int page = 0;   //  to track page position

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //overridePendingTransition(R.anim.fadein, R.anim.fadein);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorAccent));
        }

        setContentView(R.layout.activity_intro);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mNextBtn = (ImageButton) findViewById(R.id.intro_btn_next);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
            mNextBtn.setImageDrawable(
                    Utils.tintMyDrawable(ContextCompat.getDrawable(this, R.drawable.ic_action_image_navigate_next_white), Color.WHITE)
            );

        mSkipBtn = (Button) findViewById(R.id.intro_btn_skip);
        mFinishBtn = (Button) findViewById(R.id.intro_btn_finish);

        zero = (ImageView) findViewById(R.id.intro_indicator_0);
        one = (ImageView) findViewById(R.id.intro_indicator_1);
        two = (ImageView) findViewById(R.id.intro_indicator_2);
        three = (ImageView) findViewById(R.id.intro_indicator_3);
        mCoordinator = (CoordinatorLayout) findViewById(R.id.main_content);


        indicators = new ImageView[]{zero, one, two, three};

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setCurrentItem(page);
        updateIndicators(page);

        final int color1 = ContextCompat.getColor(this, R.color.white);
        final int color2 = ContextCompat.getColor(this, R.color.white);
        final int color3 = ContextCompat.getColor(this, R.color.white);
        final int color4 = ContextCompat.getColor(this, R.color.white);

        final int[] colorList = new int[]{color1, color2, color3, color4};

        final ArgbEvaluator evaluator = new ArgbEvaluator();

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                /*
                color update
                 */
                int colorUpdate = (Integer) evaluator.evaluate(positionOffset, colorList[position], colorList[position == 3 ? position : position + 1]);
                mViewPager.setBackgroundColor(colorUpdate);
            }

            @Override
            public void onPageSelected(int position) {

                page = position;

                updateIndicators(page);

                switch (position) {
                    case 0:
                        mViewPager.setBackgroundColor(color1);
                        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                        break;
                    case 1:
                        mViewPager.setBackgroundColor(color2);
                        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                        break;
                    case 2:
                        mViewPager.setBackgroundColor(color3);
                        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                        break;
                    case 3:
                        mViewPager.setBackgroundColor(color3);
                        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                        break;
                }
                mNextBtn.setVisibility(position == 3 ? View.GONE : View.VISIBLE);
                mFinishBtn.setVisibility(position == 3 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page += 1;
                mViewPager.setCurrentItem(page, true);
            }
        });

        mSkipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mFinishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Utils.saveSharedSetting(Intro.this, MainActivity.PREF_USER_FIRST_TIME, "false");
            }
        });

    }

    void updateIndicators(int position) {
        for (int i = 0; i < indicators.length; i++) {
            indicators[i].setBackgroundResource(
                    i == position ? R.drawable.indicator_selected : R.drawable.indicator_unselected
            );
        }
    }
    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        ImageView img;

        int[] bgs = new int[]{R.drawable.indicator_unselected, R.drawable.indicator_unselected, R.drawable.indicator_unselected,R.drawable.indicator_unselected};

        public PlaceholderFragment() {
        }
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_intro, container, false);
            TextView Label = (TextView) rootView.findViewById(R.id.section_label);
            TextView Title = (TextView) rootView.findViewById(R.id.titleSlide);
            img = (ImageView) rootView.findViewById(R.id.section_img);

            Log.i("ITEM",getArguments().getInt(ARG_SECTION_NUMBER)+"");
            //TEXTO PARA MOSTRAR
            String label = "";
            String title = "";

            switch(getArguments().getInt(ARG_SECTION_NUMBER)){
                case 1 :
                    label = "Gracias por descargar la aplicación de la Secretaría de Educación Jalisco, ahora estaras informado de lo que sucede en el ambito educativo.";
                    title = "Educación Jalisco";
                    break;
                case 2:
                    label = "Podras consultar información como avisos, convocatorias para becas, tu calendario escolar y mucho mas.";
                    title = "Si eres Alumno";
                    break;
                case 3:
                    label = "Podras consultar información relacionada a los procesos del servicios profesional docente, noticias de tu interés y actividades relacionadas a tu labor docente o administrativa.";
                    title ="Si trabajas en la Secretaría";
                    break;
                case 4:
                    label = "Enterate de la actividad de la Secretaría, te invitamos a seguirnos en nuestras redes sociales (agregar iconos de las redes face y twitter)";
                    title ="Como cuidadano";
            }

            Label.setText(label);
            Title.setText(title);

            img.setBackgroundResource(bgs[getArguments().getInt(ARG_SECTION_NUMBER) - 1]);


            return rootView;
        }


    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }

    }


}
