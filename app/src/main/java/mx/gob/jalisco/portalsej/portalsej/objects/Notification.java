package mx.gob.jalisco.portalsej.portalsej.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 30/06/16.
 */
public class Notification {

    private String title;
    private String time_ago;
    private String body;
    private String image;
    public static List<Notification> Notification = new ArrayList<>();


    public Notification(String title, String time_ago, String body,String image) {
        this.title = title;
        this.time_ago = time_ago;
        this.body = body;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime_ago() {
        return time_ago;
    }

    public void setTime_ago(String time_ago) {
        this.time_ago = time_ago;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
