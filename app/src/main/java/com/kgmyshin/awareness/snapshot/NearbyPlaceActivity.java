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

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.PlacesResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.kgmyshin.awareness.R;

import java.util.List;

public class NearbyPlaceActivity extends AppCompatActivity {

    private static final String TAG = "NearbyPlaceActivity";

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

        GoogleApiClient apiClient = new GoogleApiClient.Builder(this).addApi(Awareness.API).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Log.wtf(TAG, "fail to connect google api client");
            }
        }).build();
        apiClient.connect();
        Awareness.SnapshotApi.getPlaces(apiClient)
                .setResultCallback(new ResultCallback<PlacesResult>() {
                    @Override
                    public void onResult(@NonNull PlacesResult placesResult) {
                        if (!placesResult.getStatus().isSuccess()) {
                            Log.e(TAG, "Could not get places.");
                            return;
                        }
                        List<PlaceLikelihood> placeLikelihoodList = placesResult.getPlaceLikelihoods();
                        // Show the top 5 possible location results.
                        for (int i = 0; i < 5; i++) {
                            PlaceLikelihood p = placeLikelihoodList.get(i);
                            Log.i(TAG, p.getPlace().getName().toString() + ", likelihood: " + p.getLikelihood());
                        }
                    }
                });
    }
}
