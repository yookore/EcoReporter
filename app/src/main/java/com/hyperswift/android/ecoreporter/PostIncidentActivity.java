package com.hyperswift.android.ecoreporter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hyperswift.android.ecoreporter.adapters.ImageAdapter;
import com.hyperswift.android.ecoreporter.models.Incident;
import com.mlsdev.rximagepicker.RxImagePicker;
import com.mlsdev.rximagepicker.Sources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.functions.Action1;

public class PostIncidentActivity extends AppCompatActivity {
    private static final String TAG = PostIncidentActivity.class.getSimpleName();
    FirebaseStorage storage;
    DatabaseReference firebaseDatabase;
    FirebaseAuth mAuth;
    ImageView buttonClose;
    EditText editText;
    LinearLayout locationLayout;
    ImageView btn_location;
    ImageView btn_camera;
    ImageView btn_gallery;
    TextView locationText;
    ArrayList<String> downloadUris;

    Context context;
    int uploadCount = 0;

    ArrayList<Uri> imageUris;
    RecyclerView mThumbnailsRecyclerView;
    ImageAdapter imageAdapter;
    GridLayoutManager mGridLayoutManager;

    UploadTask uploadTask;

    ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_incident);
        Toolbar toolbar = (Toolbar) findViewById(R.id.post_incident_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        buttonClose = (ImageView) findViewById(R.id.buttonClose);
        editText = (EditText) findViewById(R.id.edit_text);
        locationText = (TextView) findViewById(R.id.location_text);
        locationLayout = (LinearLayout) findViewById(R.id.llayout_location);
        btn_location = (ImageView) findViewById(R.id.button_location);
        btn_camera = (ImageView) findViewById(R.id.button_camera);
        btn_gallery = (ImageView) findViewById(R.id.button_gallery);
        btn_location.setImageAlpha(125);
        locationLayout.setVisibility(View.GONE);

        imageUris = new ArrayList<>();
        imageAdapter = new ImageAdapter(this, imageUris);
        mThumbnailsRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_thumbnail_view);
        mGridLayoutManager = new GridLayoutManager(this,
                1,
                GridLayoutManager.HORIZONTAL,
                false);
        mThumbnailsRecyclerView.setAdapter(imageAdapter);
        mThumbnailsRecyclerView.setLayoutManager(mGridLayoutManager);

        configureFirebase();
        context = this;
        mAuth = FirebaseAuth.getInstance();
        signInAnonymously();
        downloadUris = new ArrayList<>();

    }

    private void signInAnonymously() {
        showProgressDialog();
        mAuth.signInAnonymously()
                .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.d(TAG, "signInAnonymously:SUCCESS");
                        hideProgressDialog();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e(TAG, "signInAnonymously:FAILURE", exception);
                        hideProgressDialog();
                    }
                });
    }

    private void configureFirebase() {
        storage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_post_incident) {
            saveData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveData() {
        showProgressDialog();
        saveImageData();
    }

    public void closeActivity(View view) {
        Toast.makeText(this, "Finishing this activity", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void getLocation(View view) {
        if (locationLayout.getVisibility() == View.GONE) {
            //The location button is off. Get location
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            try {
                startActivityForResult(builder.build(this), 100);
            } catch (GooglePlayServicesNotAvailableException
                    | GooglePlayServicesRepairableException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            Log.e("ALPHA", String.valueOf(btn_location.getImageAlpha()));
            btn_location.setImageAlpha(255);
        } else {
            locationLayout.setVisibility(View.GONE);
            Log.e("ALPHA", String.valueOf(btn_location.getImageAlpha()));
            btn_location.setImageAlpha(125);
        }
    }

    public void initiateCamera(View view) {
        RxImagePicker.with(this).requestImage(Sources.CAMERA).subscribe(new Action1<Uri>() {
            @Override
            public void call(Uri uri) {
                imageUris.add(uri);
                int currentSize = imageAdapter.getItemCount();
                imageAdapter.notifyItemInserted(currentSize);
            }
        });
    }

    public void initiatePhotos(View view) {
        RxImagePicker.with(this).requestMultipleImages().subscribe(new Action1<List<Uri>>() {
            @Override
            public void call(List<Uri> uris) {
                String message = "";
                int currentSize = imageAdapter.getItemCount();
                for (Uri uri : uris) {
                    imageUris.add(uri);
                    message += uri.toString() + "\n";
                }
                imageAdapter.notifyItemRangeInserted(currentSize, uris.size());
                mThumbnailsRecyclerView.getLayoutManager().scrollToPosition(currentSize);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                StringBuilder builder = new StringBuilder();

                Place place = PlacePicker.getPlace(this, data);
                String address = place.getAddress().toString();
                LatLng latLong = place.getLatLng();
                builder.append(address);
                builder.append(" (");
                String latitude = Location.convert(latLong.latitude, Location.FORMAT_SECONDS);
                String longitude = Location.convert(latLong.longitude, Location.FORMAT_SECONDS);
                builder.append(latitude);
                builder.append(", ");
                builder.append(longitude);
                builder.append(")");
                locationText.setText(builder.toString());
                locationLayout.setVisibility(View.VISIBLE);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    public void saveImageData() {
        StorageReference storageRef = storage.getReferenceFromUrl(Constants.FIREBASE_STORAGE_URL);
        if(imageUris.size() > 0){
            for (Uri uri : imageUris) {
                uploadCount = 0;
                StorageReference imagesRef = storageRef.child("images/" + uri.getLastPathSegment());
                uploadTask = imagesRef.putFile(uri);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        downloadUris.add(taskSnapshot.getDownloadUrl().toString());
                        Log.e(TAG, "Size of data: " + downloadUris.size());
                        uploadCount += 1;
                        if (uploadCount == imageUris.size()) {
                            Toast.makeText(context, "All uploads are complete", Toast.LENGTH_SHORT).show();
                            //Continue with saving the data to realtime database...
                            saveToDB();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Upload failed: " + e.getMessage());
                        //Still increment the upload count in case its
                        //multiple and others went through.
                        uploadCount += 1;
                    }
                });
            }
        }else {
            saveToDB();
        }

    }

    private void saveToDB() {
        Incident incident = new Incident();
        incident.setBody(editText.getText().toString());
        incident.setLocation(locationText.getText().toString());
        incident.setCreationDate(new Date().getTime());
        incident.setImageUris(downloadUris);

        firebaseDatabase.child("incidents").child(String.valueOf(incident.getCreationDate())).setValue(incident,
                new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        Log.d(TAG, databaseReference.getKey());
                    }
                });
        hideProgressDialog();
        Log.d(TAG, incident.toString());
        Toast.makeText(this, incident.toString(), Toast.LENGTH_LONG).show();
        finish();
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
