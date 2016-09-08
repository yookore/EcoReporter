package com.hyperswift.android.ecoreporter.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyperswift.android.ecoreporter.R;
import com.hyperswift.android.ecoreporter.adapters.IncidentAdapter;
import com.hyperswift.android.ecoreporter.models.Incident;

import java.util.ArrayList;

/**
 * Created by jome on 2016/09/07.
 */
public class IncidentViewHolder extends RecyclerView.ViewHolder {
    public TextView incidentBody;
    public TextView incidentPosted;
    public TextView incidentLocation;
    public ImageView firstImage;

    public IncidentViewHolder(View itemView, IncidentAdapter adapter, ArrayList<Incident> incidents) {
        super(itemView);
        incidentBody = (TextView) itemView.findViewById(R.id.incident_body);
        incidentPosted = (TextView) itemView.findViewById(R.id.incident_posted);
        firstImage = (ImageView) itemView.findViewById(R.id.image_incident_1);
        incidentLocation = (TextView) itemView.findViewById(R.id.incident_location);
    }
}
