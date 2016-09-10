package com.hyperswift.android.ecoreporter.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hyperswift.android.ecoreporter.R;
import com.hyperswift.android.ecoreporter.adapters.IncidentAdapter;
import com.hyperswift.android.ecoreporter.models.Incident;

import java.util.ArrayList;

/**
 * Created by jome on 2016/09/07.
 */
public class IncidentViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {
    public TextView incidentBody;
    public TextView incidentPosted;
    public TextView incidentLocation;
    public ImageView firstImage;
    public GoogleMap gMap;
    public MapView mapView;
    private Context context;
    ArrayList<Incident> incidents;

    public IncidentViewHolder(View itemView, Context context, ArrayList<Incident> incidents) {
        super(itemView);
        this.context = context;
        this.incidents = incidents;

        incidentBody = (TextView) itemView.findViewById(R.id.incident_body);
        incidentPosted = (TextView) itemView.findViewById(R.id.incident_posted);
        firstImage = (ImageView) itemView.findViewById(R.id.image_incident_1);
        incidentLocation = (TextView) itemView.findViewById(R.id.incident_location);
        mapView = (MapView) itemView.findViewById(R.id.mapview_layout);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(context);
        gMap = googleMap;

        Incident incident = incidents.get(getAdapterPosition());

        LatLng coords = new LatLng(incident.getLatitude(), incident.getLongitude());
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coords, 15f));
        gMap.addMarker(new MarkerOptions().position(coords).title(incident.getLocation()));
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    public void initializeView(){
        if(mapView != null){
            mapView.onCreate(null);
            mapView.getMapAsync(this);
        }
    }
}
