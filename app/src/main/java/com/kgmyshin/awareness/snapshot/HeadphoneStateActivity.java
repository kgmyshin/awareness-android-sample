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
import com.google.android.gms.awareness.snapshot.HeadphoneStateResult;
import com.google.android.gms.awareness.state.HeadphoneState;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.kgmyshin.awareness.R;

public class HeadphoneStateActivity extends AppCompatActivity {

    private static final String TAG = "HeadphoneStateActivity";
    private GoogleApiClient apiClient;

    public static Intent createIntent(Context context) {
        return new Intent(context, HeadphoneStateActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snapshot_headphone_state);

        apiClient = new GoogleApiClient.Builder(this).addApi(Awareness.API).build();

        findViewById(R.id.fire_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Awareness.SnapshotApi.getHeadphoneState(apiClient)
                        .setResultCallback(new ResultCallback<HeadphoneStateResult>() {
                            @Override
                            public void onResult(@NonNull HeadphoneStateResult headphoneStateResult) {
                                if (!headphoneStateResult.getStatus().isSuccess()) {
                                    Log.e(TAG, "Could not get headphone state.");
                                    return;
                                }
                                HeadphoneState headphoneState = headphoneStateResult.getHeadphoneState();
                                showState(headphoneState);
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

    private void showState(HeadphoneState state) {
        Toast.makeText(this, getStateName(state), Toast.LENGTH_SHORT).show();
    }

    private String getStateName(HeadphoneState state) {
        if (state.getState() == HeadphoneState.PLUGGED_IN) {
            return "ささってる";
        } else {
            return "ささってない";
        }
    }

}
