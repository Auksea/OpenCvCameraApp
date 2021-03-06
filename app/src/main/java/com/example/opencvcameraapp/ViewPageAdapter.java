package com.example.opencvcameraapp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class ViewPageAdapter extends PagerAdapter {
    Context context;
    ArrayList<String> images = new ArrayList<>();
    LayoutInflater mLayoutInflater;

    public ViewPageAdapter(Context context, ArrayList images) {
        this.context = context;
        this.images = images;

        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    public void removeView(int index)
    {
        images.remove(0);
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.item, container, false);
        ImageView image_page = (ImageView) itemView.findViewById(R.id.image_page);

        Uri imgUri = Uri.parse("file://"+images.get(position));
        image_page.setImageURI(imgUri);
        Objects.requireNonNull(container).addView(itemView);

        return itemView;

    }
}