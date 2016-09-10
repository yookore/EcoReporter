package com.hyperswift.android.ecoreporter.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hyperswift.android.ecoreporter.R;
import com.hyperswift.android.ecoreporter.models.Incident;
import com.hyperswift.android.ecoreporter.viewholders.IncidentViewHolder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class IncidentAdapter extends RecyclerView.Adapter<IncidentViewHolder> {
    private static final String TAG = IncidentAdapter.class.getSimpleName();
    private ArrayList<Incident> incidentArrayList;
    private Context context;



    public IncidentAdapter(Context context, ArrayList<Incident> incidents) {
        this.context = context;
        this.incidentArrayList = incidents;
    }

    @Override
    public IncidentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View incidentView = inflater.inflate(R.layout.incident_card, parent, false);
        IncidentViewHolder holder = new IncidentViewHolder(incidentView, context, incidentArrayList);
        holder.initializeView();
        return holder;
    }

    @Override
    public void onBindViewHolder(IncidentViewHolder holder, int position) {
        Incident incident = incidentArrayList.get(position);
        TextView body = holder.incidentBody;
        TextView location = holder.incidentLocation;
        ImageView image1 = holder.firstImage;
        TextView posted = holder.incidentPosted;
        holder.mapView.onResume();

        if(holder.gMap != null ){
            Log.e(TAG, incident.toString());
            LatLng coords = new LatLng(incident.getLatitude(), incident.getLongitude());
            holder.gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coords, 15f));
            holder.gMap.addMarker(new MarkerOptions().title(incident.getLocation()).position(coords));
            holder.gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        }else{
            Log.e(TAG, "Holder was not initialized");
        }

        body.setText(incident.getBody());
        if(incident.getImageUris().size() > 0){
            Glide.with(context).load(incident.getImageUris().get(0))
                    .fitCenter()
                    .into(image1);
        }
        String incidentDate = getDateString(incident.getCreationDate());
        posted.setText(String.format("Posted: %s", incidentDate));
        location.setText(incident.getLocation());
    }

    private String getDateString(long creationDate) {
        Date date = new Date(creationDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM, yyyy");
        return dateFormat.format(date);
    }

    @Override
    public int getItemCount() {
        return incidentArrayList.size();
    }
}
