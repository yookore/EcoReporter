package com.hyperswift.android.ecoreporter.viewholders;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.hyperswift.android.ecoreporter.adapters.ImageAdapter;
import com.hyperswift.android.ecoreporter.R;

import java.util.ArrayList;


public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final String TAG = ImageViewHolder.class.getSimpleName();
    public ImageView imageView;
    public ImageView cancelView;
    private ImageAdapter adapter;
    private ArrayList<Uri> images;
    public ImageViewHolder(View itemView, ImageAdapter imageAdapter, ArrayList<Uri> imageUris) {
        super(itemView);
        adapter = imageAdapter;
        images = imageUris;
        imageView = (ImageView) itemView.findViewById(R.id.thumbnail_view);
        cancelView = (ImageView) itemView.findViewById(R.id.image_button_clicker);
        cancelView.setOnClickListener(this);
        imageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        int adapterPosition = getAdapterPosition();
        if (view.getId() == R.id.image_button_clicker){
            Log.e(TAG, "Image button clicked");
            Toast.makeText(view.getContext(),"Clicked on item number " + adapterPosition, Toast.LENGTH_SHORT).show();
            images.remove(adapterPosition);
            adapter.notifyItemRemoved(adapterPosition);
        }
        if (view.getId() == R.id.thumbnail_view){
            Toast.makeText(view.getContext(),"Clicked on image at item number " + adapterPosition, Toast.LENGTH_SHORT).show();
        }


    }
}
