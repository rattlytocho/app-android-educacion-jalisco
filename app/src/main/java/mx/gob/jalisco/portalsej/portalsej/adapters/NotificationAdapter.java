package mx.gob.jalisco.portalsej.portalsej.adapters;

import android.content.Context;
import mx.gob.jalisco.portalsej.portalsej.R;
import mx.gob.jalisco.portalsej.portalsej.objects.Notification;
import mx.gob.jalisco.portalsej.portalsej.services.WebServices;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        public ImageView image;

        public ViewHolder(View v, NotificationAdapter padre) {
            super(v);

            title = (TextView) v.findViewById(R.id.title);
            time_ago = (TextView) v.findViewById(R.id.time_ago);
            body = (TextView) v.findViewById(R.id.body);
            image = (ImageView) v.findViewById(R.id.image);
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
                .inflate(R.layout.item_notification, viewGroup, false);
        return new ViewHolder(v,this);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        viewHolder.title.setText(items.get(i).getTitle());
        viewHolder.time_ago.setText(items.get(i).getTime_ago());

        viewHolder.body.setMovementMethod(LinkMovementMethod.getInstance());
        viewHolder.body.setText(items.get(i).getBody());

//        Log.i("IMAGE->->->",items.get(i).getImage());

        if(!Objects.equals(items.get(i).getImage(), "")){
            Glide.with(context)
                    .load(WebServices.ROOT_PATH+items.get(i).getImage())
                    .into(viewHolder.image);
        }else{
            viewHolder.image.setVisibility(View.GONE);
        }

        setFadeAnimation(viewHolder.itemView);
    }
    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1);
        view.startAnimation(anim);
    }
}