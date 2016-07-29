package mx.gob.jalisco.portalsej.portalsej.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 10/06/16.
 */
public class Tweet {

    private String imagenPerfil;
    private String text;
    private String date;

    public static List<Tweet> Tweets = new ArrayList<>();


    public Tweet(String imagenPerfil, String text, String date) {
        this.imagenPerfil = imagenPerfil;
        this.text = text;
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }

    public String getImagenPerfil() {
        return imagenPerfil;
    }
}