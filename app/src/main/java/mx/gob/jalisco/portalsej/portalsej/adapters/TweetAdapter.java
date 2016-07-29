package mx.gob.jalisco.portalsej.portalsej.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mx.gob.jalisco.portalsej.portalsej.R;
import mx.gob.jalisco.portalsej.portalsej.objects.Tweet;

/**
 * Created by root on 10/06/16.
 */
public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder>{

    List<Tweet> items = new ArrayList<>();

    private final Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public ImageView img;
        public TextView date;
        public TextView text;
        public Button see;
        public Button share;
        public RelativeLayout to;

        private TweetAdapter padre = null;

        public ViewHolder(View v, TweetAdapter padre) {
            super(v);

            this.padre = padre;

            img = (ImageView) v.findViewById(R.id.img);
            //date = (TextView) v.findViewById(R.id.date);
            text = (TextView) v.findViewById(R.id.text);
            //to = (RelativeLayout) v.findViewById(R.id.clickTo);

        }

    }

    public TweetAdapter(List<Tweet> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = null;
        int layoutId = -1;
        v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_tweet, viewGroup, false);
        return new ViewHolder(v,this);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        final Integer I = i;

        //viewHolder.date.setText(items.get(i).getDate());
        //viewHolder.text.setText(items.get(i).getVisitas());

        SpannableString hashText = new SpannableString(items.get(i).getText());
        Matcher matcher = Pattern.compile("#([A-Za-z0-9_-]+)").matcher(hashText);
        while (matcher.find()) {
            hashText.setSpan(new ForegroundColorSpan(Color.rgb(22, 132, 180)), matcher.start(), matcher.end(), 0);
        }
        Linkify.addLinks(hashText, Linkify.WEB_URLS);
        viewHolder.text.setText(hashText);

        if (viewHolder.img != null) {
            Glide.with(context)
                    .load(items.get(i).getImagenPerfil())
                    .into(viewHolder.img);
        }

        final int SIZE = getItemCount();
    }
}