package com.kgmyshin.awareness;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kgmyshin.awareness.snapshot.CurrentActivityActivity;
import com.kgmyshin.awareness.snapshot.HeadphoneStateActivity;
import com.kgmyshin.awareness.snapshot.LocationActivity;
import com.kgmyshin.awareness.snapshot.NearbyPlaceActivity;
import com.kgmyshin.awareness.snapshot.NearybyBeaconsActivity;
import com.kgmyshin.awareness.snapshot.WeatherActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView) findViewById(R.id.page_list_view);
        final ArrayAdapter<Page> pageAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Page.values());
        listView.setAdapter(pageAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Page page = pageAdapter.getItem(position);
                page.move(MainActivity.this);
            }
        });
    }

    enum Page {
        SNAPSHOT_CURRENT_ACTIVITY {
            @Override
            void move(Activity activity) {
                activity.startActivity(CurrentActivityActivity.createIntent(activity));
            }
        },
        SNAPSHOT_HEADPHONE_STATE {
            @Override
            void move(Activity activity) {
                activity.startActivity(HeadphoneStateActivity.createIntent(activity));
            }
        },
        SNAPSHOT_LOCATION {
            @Override
            void move(Activity activity) {
                activity.startActivity(LocationActivity.createIntent(activity));
            }
        },
        SNAPSHOT_NEARBY_PLACES {
            @Override
            void move(Activity activity) {
                activity.startActivity(NearbyPlaceActivity.createIntent(activity));
            }
        },
        SNAPSHOT_NEARBY_BEACONS {
            @Override
            void move(Activity activity) {
                activity.startActivity(NearybyBeaconsActivity.createIntent(activity));
            }
        },
        SNAPSHOT_WEATHER {
            @Override
            void move(Activity activity) {
                activity.startActivity(WeatherActivity.createIntent(activity));
            }
        };

        abstract void move(Activity activity);
    }

}
