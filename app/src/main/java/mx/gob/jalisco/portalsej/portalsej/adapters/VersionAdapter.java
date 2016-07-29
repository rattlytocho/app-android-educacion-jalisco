package mx.gob.jalisco.portalsej.portalsej.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mx.gob.jalisco.portalsej.portalsej.R;
import mx.gob.jalisco.portalsej.portalsej.objects.Version;

/**
 * Created by root on 20/06/16.
 */
public class VersionAdapter extends RecyclerView.Adapter<VersionAdapter.ViewHolder> {

    List<Version> items = new ArrayList<>();

    private final Context context;

    public VersionAdapter(List<Version> items, Context context) {
        this.items = items;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item

        public TextView version,desc;

        private VersionAdapter padre = null;

        public ViewHolder(View v, VersionAdapter padre) {
            super(v);

            this.padre = padre;

            version = (TextView) v.findViewById(R.id.version);
            desc = (TextView) v.findViewById(R.id.desc);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = null;

        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_version, viewGroup, false);
        return new ViewHolder(v,this);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        viewHolder.version.setText(items.get(i).getVersion());
        viewHolder.desc.setText(items.get(i).getDesc());

    }
}
