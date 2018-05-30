package robbyturnip333.gmail.com.gps;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private ImageView image;
    private float currentDegree = 0f;
    private SensorManager mSensorManager;
    TextView tvHeading;
    LocationManager locationManager;
    private TextView textView;
    private GpsTool gpsTool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image=findViewById(R.id.g1);
        tvHeading=findViewById(R.id.TextView08);
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        textView = (TextView) this.findViewById(R.id.textView);
        if (gpsTool == null) {
            gpsTool = new GpsTool(this) {
                @Override
                public void onGpsLocationChanged(Location location) {
                    super.onGpsLocationChanged(location);
                    refreshLocation(location);
                }
            };
        }
    }

    private void refreshLocation(Location location) {
        Double longitude = location.getLongitude();
        Double latitude = location.getLatitude();
        Double altitude = location.getAltitude();
        StringBuilder sb = new StringBuilder();
        sb.append("Longitude  : ").append(longitude).append("\n");
        sb.append("Latitude     : ").append(latitude).append("\n");
        sb.append("Altitude      : ").append(altitude);
        textView.setText(sb.toString());
    }

    @Override
    protected void onPause() {
        super.onPause();
        gpsTool.stopGpsUpdate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
        gpsTool.startGpsUpdate();
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        // get the angle around the z-axis rotated
        float degree = Math.round(event.values[0]);

        tvHeading.setText(Float.toString(degree) + " degrees");

        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // how long the animation will take place
        ra.setDuration(210);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        // Start the animation
        image.startAnimation(ra);
        currentDegree = -degree;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

