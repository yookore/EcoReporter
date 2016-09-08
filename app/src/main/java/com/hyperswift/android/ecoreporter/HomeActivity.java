package com.hyperswift.android.ecoreporter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.yasevich.endlessrecyclerview.EndlessRecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hyperswift.android.ecoreporter.adapters.IncidentAdapter;
import com.hyperswift.android.ecoreporter.models.Incident;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = HomeActivity.class.getSimpleName();
    ArrayList<Incident> incidents;
    EndlessRecyclerView recyclerView;
    IncidentAdapter incidentAdapter;
    LinearLayoutManager linearLayoutManager;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        incidents = new ArrayList<>();
        incidentAdapter = new IncidentAdapter(this, incidents);
        recyclerView = (EndlessRecyclerView) findViewById(R.id.stream_incidents);
        linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setAdapter(incidentAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent postIncidentIntent = new Intent(context, PostIncidentActivity.class);
                startActivity(postIncidentIntent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        populateList();
    }

    private void populateList() {
        final Query incidentQuery = databaseReference.child("incidents").orderByKey().limitToFirst(25);
//        final Query incidentQuery = databaseReference.child("incidents").orderByChild("creationDate").limitToLast(25);
        incidents.clear(); //This is just to make stuff work. Eventually, we will need to make this unique
        incidentAdapter.notifyDataSetChanged();
        incidentQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "Datasnapshot: " + String.valueOf(dataSnapshot.getChildrenCount()));
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Incident i = d.getValue(Incident.class);
                    Log.d(TAG, "Incident: " + i.toString());
                    incidents.add(0,i);
                }
                incidentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
