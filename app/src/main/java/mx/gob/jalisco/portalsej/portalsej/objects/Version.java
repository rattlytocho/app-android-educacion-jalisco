package mx.gob.jalisco.portalsej.portalsej.objects;

import android.text.Spanned;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 20/06/16.
 */

public class Version {

    private String version;
    private Spanned desc;

    public static List<Version> Version = new ArrayList<>();


    public Version(String version, Spanned desc) {
        this.version = version;
        this.desc = desc;
    }

    public String getVersion() {
        return version;
    }

    public Spanned getDesc() {
        return desc;
    }
}
