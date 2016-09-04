package mx.gob.jalisco.portalsej.portalsej.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Normalizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import mx.gob.jalisco.portalsej.portalsej.Notifications;
import mx.gob.jalisco.portalsej.portalsej.R;
import mx.gob.jalisco.portalsej.portalsej.utils.NetworkUtils;
import mx.gob.jalisco.portalsej.portalsej.utils.Utils;

public class NotifyService extends BroadcastReceiver {

    Context ctx;
    NetworkUtils utils;
    URL url;

    String node_title;
    String cuerpo;
    String imagen;
    String id;
    String publicado;
    String get_last_time;

    public static final String PREF_USER_LAST_NOTIFICACTION = "0";
    String last_time;

    @Override
    public void onReceive(Context context, Intent intent) {

        ctx = context;
        utils = new NetworkUtils(ctx);

        if(utils.isConnectingToInternet()){
            new getDataNotification().execute(WebServices.HOST_SERVICES+"/"+WebServices.LASTEST_NOTIFICACION);
        }
    }

    public static final int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    class getDataNotification extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            try {
                URL u = new URL(params[0]);
                connection = (HttpURLConnection) u.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-length", "0");
                connection.setUseCaches(false);
                connection.setAllowUserInteraction(false);
                connection.setConnectTimeout(100000);
                connection.setReadTimeout(100000);
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

            } catch (MalformedURLException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (connection != null) {
                    try {
                        connection.disconnect();
                    } catch (Exception ex) {
                        Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            return null;
        }

        protected void onPostExecute(String json) {
            try {
                if(json != null){
                    Log.i("SERVER DATA","-->"+json);
                    JSONArray jsonArray = new JSONArray(json);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    node_title = jsonObject.getString("title");
                    cuerpo = jsonObject.getString("body");
                    id = jsonObject.getString("nid");
                    imagen = jsonObject.getString("field_archivo");
                    publicado = jsonObject.getString("created");
                    get_last_time = id;

                    last_time = Utils.readSharedSetting(ctx, PREF_USER_LAST_NOTIFICACTION,"0");

                    //Utils.saveSharedSetting(ctx, NotifyService.PREF_USER_LAST_NOTIFICACTION, "");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.i("NEW NID ",get_last_time+" NIEW NID");
            Log.i("NID IN STORAGE",last_time+" NID IN STORAGE");
            if(get_last_time != null){
                if(Integer.valueOf(last_time) < Integer.valueOf(get_last_time)){
                    if(imagen.equals("")){
                        simpleNotification();
                        Log.i("NOTIFICATION TYPE","SIMPLE");
                    }else{
                        new sendNotification(ctx).execute(node_title, publicado, imagen);
                        Log.i("NOTIFICATION TYPE","W-IMAGE");
                    }
                    Utils.saveSharedSetting(ctx, NotifyService.PREF_USER_LAST_NOTIFICACTION, String.valueOf(get_last_time));
                }
            }
        }
    }

    private class sendNotification extends AsyncTask<String, Void, Bitmap> {
        Context ctx;
        public sendNotification(Context context) {
            super();
            this.ctx = context;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            InputStream in;
            node_title = params[0];
            publicado = params[1];

            try {
                url = new URL( "http://portalsej.jalisco.gob.mx/"+params[2]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {

            super.onPostExecute(result);
            int color = getColor(ctx, R.color.colorPrimary);
            Intent notIintent = new Intent(ctx, Notifications.class);
                android.support.v7.app.NotificationCompat.BigPictureStyle style = new android.support.v7.app.NotificationCompat.BigPictureStyle().bigPicture(result);
                style.setSummaryText(cuerpo);
                notIintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0, notIintent, PendingIntent.FLAG_ONE_SHOT);
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                android.support.v4.app.NotificationCompat.Builder notificationBuilder = new android.support.v4.app.NotificationCompat.Builder(ctx)
                        .setSmallIcon(R.drawable.ic_action_action_book)
                        .setLargeIcon((((BitmapDrawable) ctx.getResources().getDrawable(R.drawable.logo_portal_app)).getBitmap()))
                        .setContentTitle(node_title)
                        .setContentText(cuerpo)
                        .setContentInfo("Hace: "+publicado)
                        .setTicker("")
                        .setAutoCancel(true)
                        .setColor(color)
                        .setSound(defaultSoundUri)
                        .setStyle(style)
                        .setVisibility(android.support.v4.app.NotificationCompat.VISIBILITY_PUBLIC)
                        .setContentIntent(pendingIntent);
                NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0, notificationBuilder.build());
        }

    }

    public void simpleNotification(){
        try {
            int color = getColor(ctx, R.color.colorPrimary);
            ctx.getSystemService(Context.NOTIFICATION_SERVICE);
            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            long[] pattern = {500,500,500};
            NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(ctx)
                    .setSmallIcon(R.drawable.ic_action_action_book)
                    .setLargeIcon((((BitmapDrawable)ctx.getResources().getDrawable(R.drawable.logo_portal_app)).getBitmap()))
                    .setContentTitle(node_title)
                    .setColor(color)
                    .setContentText(cuerpo)
                    .setContentInfo("Hace: "+publicado)
                    .setTicker("")
                    .setAutoCancel(true)
                    .setSound(sound)
                    .setVibrate(pattern);

            Intent notIintent = new Intent(ctx, Notifications.class);
            PendingIntent contIntent = PendingIntent.getActivity(ctx,0,notIintent,PendingIntent.FLAG_CANCEL_CURRENT);
            mBuilder.setContentIntent(contIntent);
            NotificationManager mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(0,mBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String cleanUrl(String input) {
        // Descomposición canónica
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        // Nos quedamos únicamente con los caracteres ASCII
        Pattern pattern = Pattern.compile("\\p{ASCII}+");
        return pattern.matcher(normalized).replaceAll("");
    }
}