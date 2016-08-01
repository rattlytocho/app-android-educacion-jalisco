package mx.gob.jalisco.portalsej.portalsej.adapters;

import android.content.Context;
import mx.gob.jalisco.portalsej.portalsej.R;
import mx.gob.jalisco.portalsej.portalsej.objects.Notification;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>{

    List<Notification> items = new ArrayList<>();

    private final Context context;

    public NotificationAdapter(List<Notification> items, Context context) {
        this.items = items;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView title;
        public TextView time_ago;
        public TextView body;

        public ViewHolder(View v, NotificationAdapter padre) {
            super(v);

            title = (TextView) v.findViewById(R.id.title);
            time_ago = (TextView) v.findViewById(R.id.time_ago);
            body = (TextView) v.findViewById(R.id.body);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = null;

        v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.notification_item, viewGroup, false);
        return new ViewHolder(v,this);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        viewHolder.title.setText(items.get(i).getTitle());
        viewHolder.time_ago.setText(items.get(i).getTime_ago());
        viewHolder.body.setText(items.get(i).getBody());

        setFadeAnimation(viewHolder.itemView);
    }
    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1);
        view.startAnimation(anim);
    }
}