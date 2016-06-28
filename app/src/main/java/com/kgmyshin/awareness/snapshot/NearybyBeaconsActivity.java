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
import com.google.android.gms.awareness.snapshot.BeaconStateResult;
import com.google.android.gms.awareness.state.BeaconState;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.kgmyshin.awareness.R;

import java.util.Arrays;
import java.util.List;

public class NearybyBeaconsActivity extends AppCompatActivity {

    private static final String TAG = "NearybyBeaconsActivity";
    private static final List<BeaconState.TypeFilter> BEACON_TYPE_FILTERS = Arrays.asList(
            BeaconState.TypeFilter.with(
                    "my.beacon.namespace",
                    "my-attachment-type"),
            BeaconState.TypeFilter.with(
                    "my.other.namespace",
                    "my-attachment-type"));

    public static Intent createIntent(Context context) {
        return new Intent(context, NearybyBeaconsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snapshot_nearyby_beacons);

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
        Awareness.SnapshotApi.getBeaconState(apiClient, BEACON_TYPE_FILTERS)
                .setResultCallback(new ResultCallback<BeaconStateResult>() {
                    @Override
                    public void onResult(@NonNull BeaconStateResult beaconStateResult) {
                        if (!beaconStateResult.getStatus().isSuccess()) {
                            Log.e(TAG, "Could not get beacon state.");
                            return;
                        }
                        BeaconState beaconState = beaconStateResult.getBeaconState();
                        // Get info from the BeaconState.
                        if (beaconState.getBeaconInfo().size() > 0) {
                            Log.i(TAG, "there are beacons.");
                        } else {
                            Log.i(TAG, "no beacons");
                        }
                    }
                });
    }

}
