package mx.gob.jalisco.portalsej.portalsej;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.PermissionRequest;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import mx.gob.jalisco.portalsej.portalsej.fragments.Administrativo;
import mx.gob.jalisco.portalsej.portalsej.fragments.Alumnos;
import mx.gob.jalisco.portalsej.portalsej.fragments.Ciudadania;
import mx.gob.jalisco.portalsej.portalsej.fragments.Inicio;
import mx.gob.jalisco.portalsej.portalsej.services.NotifyService;
import mx.gob.jalisco.portalsej.portalsej.utils.Utils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

    public static final String PREF_USER_FIRST_TIME = "user_first_time";
    public static final String PREF_USER_VIEW_RECIPE = "typw_view";
    private static final int PERMISSION_CALL_PHONE = 0;

    boolean isUserFirstTime;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    PendingIntent pendingIntent;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CALL_PHONE)) {

            } else {

                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_CALL_PHONE );

            }
        }

        isUserFirstTime = Boolean.valueOf(Utils.readSharedSetting(MainActivity.this, PREF_USER_FIRST_TIME, "true"));

        Intent introIntent = new Intent(MainActivity.this, Intro.class);
        introIntent.putExtra(PREF_USER_FIRST_TIME, isUserFirstTime);

        Intent alarmIntent = new Intent(MainActivity.this, NotifyService.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, 0);

        if (isUserFirstTime) {
            Utils.saveSharedSetting(this, Notifications.PREF_NOTIFICATIONS_FIRST_TIME,"true");
            Utils.saveSharedSetting(this, NotifyService.PREF_USER_LAST_NOTIFICACTION,"0");
            Utils.saveSharedSetting(this, PREF_USER_VIEW_RECIPE,"card");
            Utils.saveSharedSetting(this,PREF_USER_FIRST_TIME,"false");
            startActivity(introIntent);
        }

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        assert tabLayout != null;
        tabLayout.setupWithViewPager(viewPager);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        setupTabIcons();
        enableNotifications();
    }

    public void enableNotifications(){
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        long updateInterval = 600000;
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + updateInterval, updateInterval, pendingIntent);

    }

    private void setupTabIcons() {
        View view1 = getLayoutInflater().inflate(R.layout.item_tab, null);
        ImageView image1 = (ImageView)  view1.findViewById(R.id.icon);
        image1.setImageResource(R.drawable.ic_action_action_home_primary);
        tabLayout.getTabAt(0).setCustomView(view1);

        View view2 = getLayoutInflater().inflate(R.layout.item_tab, null);
        ImageView image2 = (ImageView)  view2.findViewById(R.id.icon);
        image2.setImageResource(R.drawable.ic_action_social_school_primary);
        tabLayout.getTabAt(1).setCustomView(view2);

        View view3 = getLayoutInflater().inflate(R.layout.item_tab, null);
        ImageView image3 = (ImageView)  view3.findViewById(R.id.icon);
        image3.setImageResource(R.drawable.ic_action_maestros);
        tabLayout.getTabAt(2).setCustomView(view3);

        View view4 = getLayoutInflater().inflate(R.layout.item_tab, null);
        ImageView image4 = (ImageView)  view4.findViewById(R.id.icon);
        image4.setImageResource(R.drawable.ic_action_ciudadanos);
        tabLayout.getTabAt(3).setCustomView(view4);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Inicio(), "Inicio");
        adapter.addFragment(new Alumnos(), "Alumnos");
        adapter.addFragment(new Administrativo(), "Docentes");
        adapter.addFragment(new Ciudadania(), "Ciudadania");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.i("QUERY",query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;//mFragmentTitleList.get(position);
        }
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity fade_in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent = null;

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = null;

        if (id == R.id.nav_notifications) {
            intent = new Intent(MainActivity.this, Notifications.class);
        } else if (id == R.id.nav_new) {
            intent = new Intent(MainActivity.this, MostConsulted.class);
        }else if (id == R.id.nav_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Hola descarga la nueva app de Educación Jalisco https://play.google.com/store/apps/details?id=mx.gob.jalisco.portalsej.portalsej");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        } else if (id == R.id.nav_info) {
            intent = new Intent(MainActivity.this, About.class);
        }

        if(intent  != null){
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
