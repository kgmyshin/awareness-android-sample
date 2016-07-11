package com.kgmyshin.awareness.snapshot;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.PlacesResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.kgmyshin.awareness.R;

import java.util.List;

public class NearbyPlaceActivity extends AppCompatActivity {

    private static final String TAG = "NearbyPlaceActivity";
    private GoogleApiClient apiClient;

    public static Intent createIntent(Context context) {
        return new Intent(context, NearbyPlaceActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snapshot_nearby_place);

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    100
            );
            return;
        }

        apiClient = new GoogleApiClient.Builder(this).addApi(Awareness.API).build();

        findViewById(R.id.fire_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Awareness.SnapshotApi.getPlaces(apiClient)
                        .setResultCallback(new ResultCallback<PlacesResult>() {
                            @Override
                            public void onResult(@NonNull PlacesResult placesResult) {
                                if (!placesResult.getStatus().isSuccess()) {
                                    Log.e(TAG, "Could not get places.");
                                    return;
                                }
                                showPlaces(placesResult);
                            }
                        });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        apiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        apiClient.disconnect();
    }

    private void showPlaces(PlacesResult result) {
        List<PlaceLikelihood> placeLikelihoodList = result.getPlaceLikelihoods();
        if (placeLikelihoodList == null || placeLikelihoodList.size() == 0) {
            Toast.makeText(this, "とくになし", Toast.LENGTH_SHORT).show();
            return;
        }
        StringBuilder builder = new StringBuilder("近くにある施設\n");
        for (int i = 0; i < placeLikelihoodList.size(); i++) {
            Place place = placeLikelihoodList.get(0).getPlace();
            builder.append(place.getName());
            builder.append("\n");
        }
        Toast.makeText(this, builder.toString(), Toast.LENGTH_SHORT).show();
    }
}
