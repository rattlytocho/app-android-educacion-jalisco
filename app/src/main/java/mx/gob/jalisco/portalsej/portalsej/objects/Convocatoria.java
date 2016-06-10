package mx.gob.jalisco.portalsej.portalsej.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 8/06/16.
 */

public class Convocatoria {

    private String title;
    private String field_fecha_de_publicacion;
    private String field_fecha_de_vencimiento;
    private String field_nivel;
    private String field_perfil;
    private String body;
    private String field_archivo;
    private String view_node;

    public static List<Convocatoria> Convocatoria = new ArrayList<>();


    public Convocatoria(String title, String field_fecha_de_publicacion, String field_fecha_de_vencimiento, String field_nivel, String field_perfil, String body, String field_archivo, String view_node) {
        this.title = title;
        this.field_fecha_de_publicacion = field_fecha_de_publicacion;
        this.field_fecha_de_vencimiento = field_fecha_de_vencimiento;
        this.field_nivel = field_nivel;
        this.field_perfil = field_perfil;
        this.body = body;
        this.field_archivo = field_archivo;
        this.view_node = view_node;
    }

    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getField_fecha_de_publicacion(){
        return field_fecha_de_publicacion;
    }
    public void setField_fecha_de_publicacion(String field_fecha_de_publicacion){
        this.field_fecha_de_publicacion = field_fecha_de_publicacion;
    }
    public String getField_fecha_de_vencimiento(){
        return field_fecha_de_vencimiento;
    }
    public void setField_fecha_de_vencimiento(String field_fecha_de_vencimiento){
        this.field_fecha_de_vencimiento = field_fecha_de_vencimiento;
    }
    public String getField_nivel(){
        return field_nivel;
    }
    public void setField_nivel(String field_nivel){
        this.field_nivel = field_nivel;
    }
    public String getField_perfil(){
        return field_perfil;
    }
    public void setField_perfil(String field_perfil){
        this.field_perfil = field_perfil;
    }
    public String getBody(){
        return body;
    }
    public void setBody(String body){
        this.body = body;
    }
    public String getField_archivo(){
        return field_archivo;
    }
    public void setField_archivo(String field_archivo){
        this.field_archivo = field_archivo;
    }
    public String getView_node(){
        return view_node;
    }
    public void setView_node(String view_node){
        this.view_node = view_node;
    }


}
