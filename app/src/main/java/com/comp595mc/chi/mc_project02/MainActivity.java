package com.comp595mc.chi.mc_project02;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private SensorManager mSM;
    private Sensor accSensor;
    private boolean hasAcc;
    private boolean changeDisplay = false;
    private String[] text = {
            "It is certain",
            "It is decidedly so",
            "Without a doubt",
            "Yes definitely",
            "You may rely on it",
            "As I see it yes",
            "Most likely",
            "Outlook good",
            "Yes",
            "Signs point to yes",
            "Reply hazy try again",
            "Ask again later",
            "Better not tell you now",
            "Cannot predict now",
            "Concentrate and ask again",
            "Don't count on it",
            "My reply is no",
            "My sources say no",
            "Outlook not so good",
            "Very doubtful"};
    private Random rand = new Random();
    private Toast toast;
    private Button btn;
    private Vibrator vib;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        //Get on screen Button
        btn = (Button) findViewById(R.id.DoMagic);
        btn.setEnabled(false);
        btn.setVisibility(View.INVISIBLE);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                changeDisplay();
//            }
//        });
        SetupSensor();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void changeDisplay() {
        int r = rand.nextInt(text.length);
        Log.i("DisplayText", "Index: " + new Integer(r).toString());
        TextView Display = (TextView) findViewById(R.id.DisplayText);
        Display.setText(text[r]);
    }

    private void SetupSensor() {
        mSM = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> sensorList = mSM.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (sensorList.size() > 0) {
            accSensor = sensorList.get(0);
            hasAcc = true;
//            toast= Toast.makeText(getApplicationContext(), "ACCELEROMETER Sensor FOUND: " + accSensor.getName(), Toast.LENGTH_SHORT);
        } else {
            toast = Toast.makeText(getApplicationContext(), "ACCELEROMETER Sensor not found", Toast.LENGTH_SHORT);
            toast.show();
        }
        vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    private SensorEventListener accListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float z_value = event.values[2];
            if (z_value >= 3) {
                if (!changeDisplay) {
                    Log.i("Sensor", "UP" + "[" + String.valueOf(z_value) + "]");
                    changeDisplay = true;
                    if(vib.hasVibrator()){
                        vib.cancel();
                    }

                }
            } else if (z_value <= -4.5) {
                if (changeDisplay) {
                    Log.i("Sensor", "DOWN" + "[" + String.valueOf(z_value) + "]");
                    changeDisplay = false;
                    changeDisplay();
                    if(vib.hasVibrator()){
                        vib.vibrate(100);
                    }
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (hasAcc) {
            mSM.registerListener(accListener, accSensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        if (hasAcc) {
            mSM.unregisterListener(accListener);
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }
}
