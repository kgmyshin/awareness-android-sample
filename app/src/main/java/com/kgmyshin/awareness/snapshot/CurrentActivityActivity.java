package com.kgmyshin.awareness.snapshot;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.DetectedActivityResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.kgmyshin.awareness.R;

public class CurrentActivityActivity extends AppCompatActivity {

    private static final String TAG = "CurrentActivityActivity";

    public static Intent createIntent(Context context) {
        return new Intent(context, CurrentActivityActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snapshot_current_activity);

        GoogleApiClient apiClient = new GoogleApiClient.Builder(this).addApi(Awareness.API).build();
        apiClient.connect();
        Awareness.SnapshotApi.getDetectedActivity(apiClient)
                .setResultCallback(new ResultCallback<DetectedActivityResult>() {
                    @Override
                    public void onResult(@NonNull DetectedActivityResult detectedActivityResult) {
                        if (!detectedActivityResult.getStatus().isSuccess()) {
                            Log.e(TAG, "Could not get the current activity.");
                        }
                        ActivityRecognitionResult ar = detectedActivityResult.getActivityRecognitionResult();
                        DetectedActivity probableActivity = ar.getMostProbableActivity();
                        Log.i(TAG, probableActivity.toString());
                    }
                });
    }
}
