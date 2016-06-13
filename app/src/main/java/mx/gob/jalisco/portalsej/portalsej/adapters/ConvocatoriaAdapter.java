package mx.gob.jalisco.portalsej.portalsej.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import mx.gob.jalisco.portalsej.portalsej.MainActivity;
import mx.gob.jalisco.portalsej.portalsej.R;
import mx.gob.jalisco.portalsej.portalsej.objects.Convocatoria;
import mx.gob.jalisco.portalsej.portalsej.utils.Utils;

/**
 * Created by root on 8/06/16.
 */
public class ConvocatoriaAdapter extends RecyclerView.Adapter<ConvocatoriaAdapter.ViewHolder>{

    List<Convocatoria> items = new ArrayList<>();

    private final Context context;

    public ConvocatoriaAdapter(List<Convocatoria> items, Context context) {
        this.items = items;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item

        public TextView title;
        public TextView field_fecha_de_publicacion;
        public TextView field_fecha_de_vencimiento;
        public TextView field_nivel;
        public TextView field_perfil;
        public TextView body;
        public RelativeLayout launcher;

        private ConvocatoriaAdapter padre = null;

        public ViewHolder(View v, ConvocatoriaAdapter padre) {
            super(v);

            this.padre = padre;

            title = (TextView) v.findViewById(R.id.title);
            field_fecha_de_publicacion = (TextView) v.findViewById(R.id.field_fecha_de_publicacion);
            field_fecha_de_vencimiento = (TextView) v.findViewById(R.id.field_fecha_de_vencimiento);
           /* field_nivel = (TextView) v.findViewById(R.id.field_nivel);
            field_perfil = (TextView) v.findViewById(R.id.field_perfil);
            body = (TextView) v.findViewById(R.id.body);

*/
            launcher = (RelativeLayout) v.findViewById(R.id.launcher);

        }
    }



    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = null;

        String type = Utils.readSharedSetting(context, MainActivity.PREF_USER_VIEW_RECIPE, "true");

        if(Objects.equals(type, "list")){
            //v = LayoutInflater.from(viewGroup.getContext())
              //      .inflate(R.layout.list_item_recipe, viewGroup, false);
            return new ViewHolder(v,this);

        }else{
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_card_convocatoria, viewGroup, false);
            return new ViewHolder(v,this);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        viewHolder.title.setText(items.get(i).getTitle());
        viewHolder.field_fecha_de_publicacion.setText("Del: "+items.get(i).getField_fecha_de_publicacion());

        final int item = i;

        viewHolder.launcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(context, DetailConvocatoria.class);
                Bundle bundle =new Bundle();

                bundle.putString("TITLE", items.get(item).getTitle());
                bundle.putString("FIELD_FECHA_DE_PUBLICACION", items.get(item).getField_fecha_de_publicacion());
                bundle.putString("FIELD_FECHA_DE_VENCIMIENTO", items.get(item).getField_fecha_de_vencimiento());
                bundle.putString("FIELD_NIVEL", items.get(item).getField_nivel());
                bundle.putString("FIELD_PERFIL", items.get(item).getField_perfil());
                bundle.putString("BODY", items.get(item).getBody());
                bundle.putString("FIELD_ARCHIVO", items.get(item).getField_archivo());
                bundle.putString("VIEW_NODE", items.get(item).getView_node());

                //ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, viewHolder.img, viewHolder.img.getTransitionName());

                //intent.putExtras(bundle);
                //context.startActivity(intent, options.toBundle());
                //((Activity)  context).overridePendingTransition(R.anim.fadein, R.anim.fadeout);


            }
        });
        setFadeAnimation(viewHolder.itemView);

    }
    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1);
        view.startAnimation(anim);
    }
}