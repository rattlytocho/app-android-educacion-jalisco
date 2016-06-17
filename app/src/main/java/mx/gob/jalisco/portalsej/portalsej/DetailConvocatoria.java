package mx.gob.jalisco.portalsej.portalsej;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import mx.gob.jalisco.portalsej.portalsej.services.WebServices;

public class DetailConvocatoria extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    String Title;
    String Field_fecha_de_publicacion;
    String Field_fecha_de_vencimiento;
    String Field_nivel;
    String Field_perfil;
    String Body;
    String Field_archivo;
    String View_node;


    TextView title;
    TextView field_fecha_de_publicacion;
    TextView field_fecha_de_vencimiento;
    TextView body;

    Button view_on_web;
    Button download;

    TextView tv_loading;
    String dest_file_path = "test.pdf";
    int downloadedSize = 0, totalsize;
    String download_file_url = "http://ilabs.uw.edu/sites/default/files/sample_0.pdf";
    float per = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_convocatoria);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        field_fecha_de_publicacion = (TextView) findViewById(R.id.field_fecha_de_publicacion);
        field_fecha_de_vencimiento = (TextView) findViewById(R.id.field_fecha_de_vencimiento);
        body = (TextView) findViewById(R.id.body);
        title = (TextView) findViewById(R.id.title);

        view_on_web = (Button) findViewById(R.id.view_on_web);
        download = (Button) findViewById(R.id.download_file);

        view_on_web.setOnClickListener(this);
        download.setOnClickListener(this);

        Bundle bundle = this.getIntent().getExtras();

        Title = bundle.getString("TITLE");
        Field_fecha_de_publicacion = bundle.getString("FIELD_FECHA_DE_PUBLICACION");
        Field_fecha_de_vencimiento = bundle.getString("FIELD_FECHA_DE_VENCIMIENTO");
        Field_nivel = bundle.getString("FIELD_NIVEL");
        Field_perfil = bundle.getString("FIELD_PERFIL");
        Body = bundle.getString("BODY");
        Field_archivo = bundle.getString("FIELD_ARCHIVO");
        View_node = bundle.getString("VIEW_NODE");


        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Detalle de Convocatoria");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        title.setText(Title);
        field_fecha_de_publicacion.setText("De: " + Field_fecha_de_publicacion + " a: ");
        field_fecha_de_vencimiento.setText(Field_fecha_de_vencimiento);


        body.setText(Html.fromHtml(Body));

        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.actions);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Ver en sitio Web");
        categories.add("Descargar archivo(s)");
        categories.add("Informar un problema");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent share = new Intent();
                share.setAction(Intent.ACTION_SEND);
                share.putExtra(Intent.EXTRA_TEXT, WebServices.ROOT_PATH + View_node);
                share.setType("text/plain");
                startActivity(Intent.createChooser(share, "Compartir por..."));
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        if(position == 1){
        }
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.download_file) {

        }
    }
}
