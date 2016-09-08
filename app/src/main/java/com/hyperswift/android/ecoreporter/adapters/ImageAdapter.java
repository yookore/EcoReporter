package com.hyperswift.android.ecoreporter.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hyperswift.android.ecoreporter.R;
import com.hyperswift.android.ecoreporter.viewholders.ImageViewHolder;

import java.util.ArrayList;


public class ImageAdapter extends RecyclerView.Adapter<ImageViewHolder> {
   private  ArrayList<Uri> imageUris;
    private Context context;

    public ImageAdapter(Context context, ArrayList<Uri> uriArrayList){
        this.imageUris = uriArrayList;
        this.context = context;
    }
    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View imageView = inflater.inflate(R.layout.img_thumbnail,parent,false);
        return new ImageViewHolder(imageView, this, imageUris);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Uri imageUri = imageUris.get(position);
        ImageView imageView = holder.imageView;
        Glide.with(context).load(imageUri).crossFade().centerCrop().into(imageView);
    }

    @Override
    public int getItemCount() {
        return imageUris.size();
    }
}
