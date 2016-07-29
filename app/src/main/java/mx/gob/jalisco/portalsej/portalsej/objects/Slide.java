package mx.gob.jalisco.portalsej.portalsej.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 13/06/16.
 */
public class Slide {

    private String image;
    private String text;
    private String url;

    public static List<Slide> Slides = new ArrayList<>();


    public Slide(String image, String text, String url) {
        this.image = image;
        this.text = text;
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public String getUrl() {
        return url;
    }

    public String getImage() {
        return image;
    }
}
