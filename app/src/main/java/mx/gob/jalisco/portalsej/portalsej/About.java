package mx.gob.jalisco.portalsej.portalsej;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import mx.gob.jalisco.portalsej.portalsej.adapters.VersionAdapter;
import mx.gob.jalisco.portalsej.portalsej.objects.Version;

public class About extends AppCompatActivity {

    ListView about_list;

    List<Version> items;
    VersionAdapter adapter;
    RecyclerView recycler;
    RecyclerView.LayoutManager lManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Acerca de");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        about_list = (ListView) findViewById(R.id.about_list);

        String[] values = new String[] {
                "Versión de la Aplicación",
                "Introducción",
                "Desarrolladores",
                "Plolítica de Privacidad",
                "Permisos",
                "Contacto"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);


        about_list.setAdapter(adapter);

        about_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0){
                    Versions(R.layout.dialog_versions);
                }
                else if(position == 1){
                    Intent intent = new Intent(About.this,Intro.class);
                    startActivity(intent);
                }else if(position == 2){

                }else if(position == 3){

                }else if(position == 4){
                    Permissions(R.layout.dialog_versions);
                }else if(position == 5){

                }
            }
        });

    }

    private void Versions(int layout) {

        final Dialog dialog = new Dialog(About.this);

        dialog.setContentView(layout);
        dialog.setTitle(getString(R.string.versions));

        //TextView txt = (TextView) dialog.findViewById(R.id.text);
        //txt.setText(text);

        //ImageView image = (ImageView)dialog.findViewById(R.id.image);
        //image.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_dialog_info));

        //adding button click event

        recycler = (RecyclerView) dialog.findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);

        lManager = new LinearLayoutManager(About.this);
        recycler.setLayoutManager(lManager);
        recycler.setNestedScrollingEnabled(false);
        items = new ArrayList<>();
        items.add(new Version(getString(R.string.latest_version) , Html.fromHtml(getString(R.string.des__1_2_3))));
        items.add(new Version(getString(R.string.version_1_2_2) , Html.fromHtml(getString(R.string.des__1_2_2))));
        items.add(new Version(getString(R.string.version_1_2_1) , Html.fromHtml(getString(R.string.des__1_2_1))));
        items.add(new Version(getString(R.string.version_1_2_0) , Html.fromHtml(getString(R.string.des__1_2_0))));
        items.add(new Version(getString(R.string.version_1_1_9) , Html.fromHtml(getString(R.string.des__1_1_9))));
        items.add(new Version(getString(R.string.version_1_1_8) , Html.fromHtml(getString(R.string.des__1_1_8))));
        items.add(new Version(getString(R.string.version_1_1_7) , Html.fromHtml(getString(R.string.des__1_1_7))));
        items.add(new Version(getString(R.string.version_1_1_6) , Html.fromHtml(getString(R.string.des__1_1_6))));
        items.add(new Version(getString(R.string.version_1_1_5) , Html.fromHtml(getString(R.string.des__1_1_5))));
        items.add(new Version(getString(R.string.version_1_1_4) , Html.fromHtml(getString(R.string.des__1_1_4))));
        items.add(new Version(getString(R.string.version_1_1_3) , Html.fromHtml(getString(R.string.des__1_1_3))));
        items.add(new Version(getString(R.string.version_1_1_2) , Html.fromHtml(getString(R.string.des__1_1_2))));
        items.add(new Version(getString(R.string.version_1_1_1) , Html.fromHtml(getString(R.string.des__1_1_1))));
        adapter = new VersionAdapter(items, About.this);
        adapter = new VersionAdapter(items, About.this);
        recycler.setAdapter(adapter);
        Button dismissButton = (Button) dialog.findViewById(R.id.close);
        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void Permissions(int layout) {

        final Dialog dialog = new Dialog(About.this);

        dialog.setContentView(layout);
        dialog.setTitle("Permisos");

        //TextView txt = (TextView) dialog.findViewById(R.id.text);
        //txt.setText(text);

        //ImageView image = (ImageView)dialog.findViewById(R.id.image);
        //image.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_dialog_info));

        //adding button click event

        recycler = (RecyclerView) dialog.findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);

        lManager = new LinearLayoutManager(About.this);
        recycler.setLayoutManager(lManager);
        recycler.setNestedScrollingEnabled(false);
        items = new ArrayList<>();
        items.add(new Version("Acceso a Internet" , Html.fromHtml("Obtener información al día a travéz de tu conexión a Internet")));
        items.add(new Version("Ejecutar aplicación al encender" , Html.fromHtml("Necesaria para recibir notificaciones")));
        items.add(new Version("Acceso a teléfono" , Html.fromHtml("Acceder a los números de telégono de ayuda(Sección de Calendario)")));
        adapter = new VersionAdapter(items, About.this);
        adapter = new VersionAdapter(items, About.this);
        recycler.setAdapter(adapter);
        Button dismissButton = (Button) dialog.findViewById(R.id.close);
        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
