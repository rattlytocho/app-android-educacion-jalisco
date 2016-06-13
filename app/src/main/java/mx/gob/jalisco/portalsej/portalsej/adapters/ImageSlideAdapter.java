package mx.gob.jalisco.portalsej.portalsej.adapters;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import mx.gob.jalisco.portalsej.portalsej.R;
import mx.gob.jalisco.portalsej.portalsej.objects.Slide;

/**
 * Created by root on 6/06/16.
 */
public class ImageSlideAdapter extends PagerAdapter {

    public static List<Slide> items = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;

    public ImageSlideAdapter(Context context, List<Slide> items) {
        this.context = context;
        this.items=items;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = inflater.inflate(R.layout.item_slide_layout, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);

        final TextView textView = (TextView) imageLayout.findViewById(R.id.textSlide);

        Glide.with(context)
                .load(items.get(position).getImage())
                .into(imageView);

        textView.setText(items.get(position).getText());

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}