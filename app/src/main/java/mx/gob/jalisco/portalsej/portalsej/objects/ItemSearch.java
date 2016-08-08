package mx.gob.jalisco.portalsej.portalsej.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manuel on 2/08/16.
 */
public class ItemSearch {

    private String title;
    private String nid;
    public static List<ItemSearch> ItemSearch = new ArrayList<>();


    public ItemSearch(String title, String nid) {
        this.title = title;
        this.nid = nid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }
}
