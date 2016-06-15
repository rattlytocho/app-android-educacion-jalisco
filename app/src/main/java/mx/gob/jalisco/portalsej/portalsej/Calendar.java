package mx.gob.jalisco.portalsej.portalsej;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import mx.gob.jalisco.portalsej.portalsej.adapters.ListAdapterIconDialog;
import mx.gob.jalisco.portalsej.portalsej.utils.Utils;

public class Calendar extends AppCompatActivity implements View.OnClickListener {

    boolean isUserFirstTime;

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

        Button numberButton = (Button) findViewById(R.id.numberButton);
        numberButton.setOnClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void ContactModal() {

        final String[] items = {
                "Dudas Calendario",
                "TELSEP"
        };

        final Integer[] icons = new Integer[]{R.drawable.ic_action_communication_call_primary,R.drawable.ic_action_communication_call_primary};
        ListAdapter adapter = new ListAdapterIconDialog(this, items, icons);

        new AlertDialog.Builder(this)
                .setTitle("Selecciona un télefono para llamar")
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        if(item == 0){
                            callIntent.setData(Uri.parse("tel:" + "018001120597"));
                        }else if(item == 1){
                            callIntent.setData(Uri.parse("tel:" + "018002886688"));
                        }
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
                }).show();

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.numberButton){
            ContactModal();
        }
    }
}
