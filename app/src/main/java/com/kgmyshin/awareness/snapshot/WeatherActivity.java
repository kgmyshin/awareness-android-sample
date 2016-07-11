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
import com.google.android.gms.awareness.snapshot.WeatherResult;
import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.kgmyshin.awareness.R;

public class WeatherActivity extends AppCompatActivity {

    private static final String TAG = "WeatherActivity";
    private GoogleApiClient apiClient;

    public static Intent createIntent(Context context) {
        return new Intent(context, WeatherActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

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
                Awareness.SnapshotApi.getWeather(apiClient)
                        .setResultCallback(new ResultCallback<WeatherResult>() {
                            @Override
                            public void onResult(@NonNull WeatherResult weatherResult) {
                                if (!weatherResult.getStatus().isSuccess()) {
                                    Log.e(TAG, "Could not get weather.");
                                    return;
                                }
                                Weather weather = weatherResult.getWeather();
                                showWeather(weather);
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

    private void showWeather(Weather weather) {
        StringBuilder builder = new StringBuilder("天気\n");
        int[] conditions = weather.getConditions();
        for (int i = 0; i < conditions.length; i++) {
            builder.append(getWeatherName(conditions[i]));
            builder.append("\n");
        }
        Toast.makeText(this, builder.toString(), Toast.LENGTH_SHORT).show();
    }

    private String getWeatherName(int condition) {
        switch (condition) {
            case Weather.CONDITION_CLEAR:
                return "快晴";
            case Weather.CONDITION_CLOUDY:
                return "曇り";
            case Weather.CONDITION_FOGGY:
                return "霧";
            case Weather.CONDITION_HAZY:
                return "もやのかかった";
            case Weather.CONDITION_ICY:
                return "凍るほど冷たい";
            case Weather.CONDITION_RAINY:
                return "雨";
            case Weather.CONDITION_SNOWY:
                return "雪";
            case Weather.CONDITION_STORMY:
                return "嵐";
            case Weather.CONDITION_WINDY:
                return "風の強い";
            case Weather.CONDITION_UNKNOWN:
            default:
                return "不明";
        }
    }
}
