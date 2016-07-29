package mx.gob.jalisco.portalsej.portalsej;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mx.gob.jalisco.portalsej.portalsej.services.WebServices;

public class DetailConvocatoria extends AppCompatActivity implements View.OnClickListener {

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

    Button download;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_convocatoria);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        field_fecha_de_publicacion = (TextView) findViewById(R.id.field_fecha_de_publicacion);
        field_fecha_de_vencimiento = (TextView) findViewById(R.id.field_fecha_de_vencimiento);
        body = (TextView) findViewById(R.id.body);
        title = (TextView) findViewById(R.id.title);

        Bundle bundle = this.getIntent().getExtras();

        Title = bundle.getString("TITLE");
        Field_fecha_de_publicacion = bundle.getString("FIELD_FECHA_DE_PUBLICACION");
        Field_fecha_de_vencimiento = bundle.getString("FIELD_FECHA_DE_VENCIMIENTO");
        Field_nivel = bundle.getString("FIELD_NIVEL");
        Field_perfil = bundle.getString("FIELD_PERFIL");
        Body = bundle.getString("BODY");
        Field_archivo = bundle.getString("FIELD_ARCHIVO");
        View_node = bundle.getString("VIEW_NODE");

        download = (Button) findViewById(R.id.download_file);
        assert download != null;
        download.setOnClickListener(this);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Detalle de Convocatoria");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        title.setText(Title);
        field_fecha_de_publicacion.setText("De: " + Field_fecha_de_publicacion + " a: ");
        field_fecha_de_vencimiento.setText(Field_fecha_de_vencimiento);

        body.setMovementMethod(LinkMovementMethod.getInstance());
        body.setText(Html.fromHtml(Body));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present
        if(Field_archivo!=""){
            getMenuInflater().inflate(R.menu.menu_detail_convocatoria, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity fade_in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://148.243.161.231"+Field_archivo));
                startActivity(browserIntent);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.download_file) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://148.243.161.231"+Field_archivo));
                startActivity(browserIntent);
        }
    }
}
