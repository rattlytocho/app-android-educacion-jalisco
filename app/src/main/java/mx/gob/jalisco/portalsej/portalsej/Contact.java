package mx.gob.jalisco.portalsej.portalsej;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Contact extends AppCompatActivity{

    TextInputLayout name,email,message,tel;
    Button send;
    RelativeLayout form,done_message;
    String Name,Tel,Email,Message,Category,TypeSubject;
    Spinner category,type_subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        send = (Button) findViewById(R.id.submit);
        name = (TextInputLayout) findViewById(R.id.name);
        email = (TextInputLayout) findViewById(R.id.email);
        message = (TextInputLayout) findViewById(R.id.message);
        tel = (TextInputLayout) findViewById(R.id.tel);
        form = (RelativeLayout) findViewById(R.id.form);
        done_message = (RelativeLayout) findViewById(R.id.done_message);

        category = (Spinner) findViewById(R.id.category);
        final ArrayAdapter<CharSequence> adapter_category = ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter_category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adapter_category);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Category = adapterView.getSelectedItem()+"";
                Category =  Category.replaceAll("\\s+","%20");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        type_subject = (Spinner) findViewById(R.id.type_subject);
        ArrayAdapter<CharSequence> adapter_type_subject = ArrayAdapter.createFromResource(this,
                R.array.type_subject, android.R.layout.simple_spinner_item);
        adapter_type_subject.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type_subject.setAdapter(adapter_type_subject);
        type_subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TypeSubject = adapterView.getSelectedItem()+"";
                TypeSubject = TypeSubject.replaceAll("\\s+","%20");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        assert send != null;
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar
                        .make(view, "Enviando...", Snackbar.LENGTH_LONG);
                snackbar.show();

                Tel = tel.getEditText().getText().toString();

                if(name.getEditText().getText().toString().trim().equals("")){
                    name.setErrorEnabled(true);
                    name.setError("El nombre obligatorio");
                }else{
                    Name = name.getEditText().getText().toString().replaceAll("\\s+","%20");
                    name.setError(null);
                }
                if(email.getEditText().getText().toString().trim().equals("")){
                    email.setErrorEnabled(true);
                    email.setError("El correo es obligatorio");
                }else{
                    Email = email.getEditText().getText().toString().replaceAll("\\s+","%20");
                    email.setError(null);
                }
                if(message.getEditText().getText().toString().trim().equals("")){
                    message.setErrorEnabled(true);
                    message.setError("El mensaje es obligatorio");
                }else{
                    Message = message.getEditText().getText().toString().replaceAll("\\s+","%20");
                    message.setError(null);
                }

                if(!name.getEditText().getText().toString().trim().equals("") || !email.getEditText().getText().toString().trim().equals("") || !message.getEditText().getText().toString().trim().equals("")) {
                    new getJson().execute("http://edu.jalisco.gob.mx/mailtest/index.php?name=" + Name + "&email=" + Email + "&tel=" + Tel +  "&message=" + Message +"&category=" + Category + "&type=" + TypeSubject.replaceAll("\\s+","%20"));
                }
            }
        });
    }

    class getJson extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            try {
                URL    url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput( true );
                connection.setInstanceFollowRedirects( false );
                connection.setRequestMethod( "POST" );
                connection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty( "charset", "utf-8");
                connection.setUseCaches( false );

                DataOutputStream data = new DataOutputStream( connection.getOutputStream());
                Log.i("Response",data+"");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hide(form,done_message);
                    }
                });
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

            } catch (IOException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            } finally {
                connection.disconnect();
            }
            return null;
        }

        protected void onPostExecute(String json) {
                if(json != null) {
                        Log.i("response",json);
                }
        }
    }
    public void hide(RelativeLayout hide, RelativeLayout show){
        hide.setVisibility(View.GONE);
        show.setVisibility(View.VISIBLE);
    }
}