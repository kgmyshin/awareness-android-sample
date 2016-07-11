package com.kgmyshin.awareness.snapshot;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.DetectedActivityResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.kgmyshin.awareness.R;

public class CurrentActivityActivity extends AppCompatActivity {

    private static final String TAG = "CurrentActivityActivity";
    private GoogleApiClient apiClient;

    public static Intent createIntent(Context context) {
        return new Intent(context, CurrentActivityActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snapshot_current_activity);

        apiClient = new GoogleApiClient.Builder(this).addApi(Awareness.API).build();
        findViewById(R.id.fire_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Awareness.SnapshotApi.getDetectedActivity(apiClient)
                        .setResultCallback(new ResultCallback<DetectedActivityResult>() {
                            @Override
                            public void onResult(@NonNull DetectedActivityResult detectedActivityResult) {
                                if (!detectedActivityResult.getStatus().isSuccess()) {
                                    Log.e(TAG, "Could not get the current activity.");
                                }
                                ActivityRecognitionResult ar = detectedActivityResult.getActivityRecognitionResult();
                                DetectedActivity probableActivity = ar.getMostProbableActivity();
                                showActivity(probableActivity);
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

    private void showActivity(DetectedActivity activity) {
        Toast.makeText(this, getTypeName(activity) + " confidence:" + activity.getConfidence(), Toast.LENGTH_SHORT).show();
    }

    private String getTypeName(DetectedActivity activity) {
        int type = activity.getType();
        switch (type) {
            case DetectedActivity.IN_VEHICLE:
                return "自動車とかに乗ってる";
            case DetectedActivity.ON_BICYCLE:
                return "自転車に乗ってる";
            case DetectedActivity.ON_FOOT:
                return "歩いてるか走ってる";
            case DetectedActivity.RUNNING:
                return "走ってる";
            case DetectedActivity.STILL:
                return "止まってる";
            case DetectedActivity.TILTING:
                return "傾いてる";
            case DetectedActivity.WALKING:
                return "歩いてる";
            case DetectedActivity.UNKNOWN:
            default:
                return "わからない";
        }
    }

}
