package com.kgmyshin.awareness.snapshot;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.LocationResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.kgmyshin.awareness.R;

public class LocationActivity extends AppCompatActivity {

    private static final String TAG = "LocationActivity";

    public static Intent createIntent(Context context) {
        return new Intent(context, LocationActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snapshot_location);

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

        GoogleApiClient apiClient = new GoogleApiClient.Builder(this).addApi(Awareness.API).build();
        apiClient.connect();
        Awareness.SnapshotApi.getLocation(apiClient)
                .setResultCallback(new ResultCallback<LocationResult>() {
                    @Override
                    public void onResult(@NonNull LocationResult locationResult) {
                        if (!locationResult.getStatus().isSuccess()) {
                            Log.e(TAG, "Could not get location.");
                            return;
                        }
                        Location location = locationResult.getLocation();
                        Log.i(TAG, "Lat: " + location.getLatitude() + ", Lon: " + location.getLongitude());
                    }
                });
    }

}
