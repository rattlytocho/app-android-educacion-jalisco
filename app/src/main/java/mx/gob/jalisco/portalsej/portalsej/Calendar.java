package mx.gob.jalisco.portalsej.portalsej;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import mx.gob.jalisco.portalsej.portalsej.adapters.SimpleImageAdapter;
import mx.gob.jalisco.portalsej.portalsej.utils.Utils;

public class Calendar extends AppCompatActivity implements View.OnClickListener {

    boolean isUserFirstTime;

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final int[] IMAGES = {
            R.drawable.agosto,
            R.drawable.septiembre,
            R.drawable.octubre,
            R.drawable.noviembre,
            R.drawable.diciembre,
            R.drawable.enero,
            R.drawable.febrero,
            R.drawable.marzo,
            R.drawable.abril,
            R.drawable.mayo,
            R.drawable.junio,
            R.drawable.julio
    };
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();

    ImageButton next;
    ImageButton prev;

    View bottomSheet;

    LinearLayout number_calendario, number_telsep;
    private BottomSheetBehavior mBottomSheetBehavior;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        isUserFirstTime = Boolean.valueOf(Utils.readSharedSetting(Calendar.this, MainActivity.PREF_USER_FIRST_TIME, "true"));

        if (isUserFirstTime) {
        }

        prev = (ImageButton) findViewById(R.id.prev);
        next = (ImageButton) findViewById(R.id.next);
        prev.setOnClickListener(this);
        next.setOnClickListener(this);

        bottomSheet = findViewById(R.id.phone_numbers);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        number_calendario = (LinearLayout) findViewById(R.id.dudas_calendario);
        number_telsep = (LinearLayout) findViewById(R.id.telsep);

        number_calendario.setOnClickListener(this);
        number_telsep.setOnClickListener(this);

        Button numberButton = (Button) findViewById(R.id.numberButton);
        numberButton.setOnClickListener(this);

        init();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void Callnumber(String number){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(Calendar.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(callIntent);
    }

    private void init() {

        for(int i=0;i<IMAGES.length;i++){
            ImagesArray.add(IMAGES[i]);
        }

        mPager = (ViewPager) findViewById(R.id.pagerCalendar);

        mPager.setAdapter(new SimpleImageAdapter(Calendar.this,ImagesArray));

        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

        indicator.setRadius(5 * density);

        NUM_PAGES = IMAGES.length;

        // Auto start of viewpager
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
        }, 4000);

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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.numberButton){
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }else if(id == R.id.next){
            if(currentPage!=NUM_PAGES) currentPage++; else currentPage=0;
            mPager.setCurrentItem(currentPage, true);
        }else if(id == R.id.prev){
            if (currentPage!=0) currentPage--;else currentPage = NUM_PAGES;
            mPager.setCurrentItem(currentPage, true);
        }else if(id == R.id.dudas_calendario){
            Callnumber("018001120597");
        }else if(id == R.id.telsep){
            Callnumber("018002886688");
        }

    }
}
