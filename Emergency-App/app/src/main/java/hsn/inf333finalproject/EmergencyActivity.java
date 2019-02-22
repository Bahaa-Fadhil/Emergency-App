package hsn.inf333finalproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class EmergencyActivity extends Activity implements android.location.LocationListener {
    TextView txtDiagnose, txtLatitude, txtLongitude;
    private Database db;

    private Intent alarm;
    private final int REQUEST_CODE = 123;

    // Variables for storing values at orientation change
    private final String ALARM_ACTIVATED = "ALARM_ACTIVATED";
    private final String LATITUDE = "LATITUDE";
    private final String LONGITUDE = "LONGITUDE";

    private User user;

    private Location location;
    private volatile double lat, lng;
    private boolean alarmActivated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        // If the alarm has been activated, do not activate again at orientation change
        if(savedInstanceState != null) {
            alarmActivated = savedInstanceState.getBoolean(ALARM_ACTIVATED);
            lat = savedInstanceState.getDouble(LATITUDE);
            lng = savedInstanceState.getDouble(LONGITUDE);
        } else {
            alarmActivated = false;
        }

        alarm = new Intent(this, AlarmService.class);

        db = new Database(this);
        user = db.getUser();

        // Get diagnose from database to show on screen
        txtDiagnose = findViewById(R.id.txtDiagnoseHere);
        txtDiagnose.setText(user.getDiagnosis());

        if(!alarmActivated || getIntent().getBooleanExtra("from_widget", false)) {
            activateAlarm();
            alarmActivated = true;
        }

        txtLatitude = findViewById(R.id.txtLatitude);
        txtLongitude = findViewById(R.id.txtLongitude);

        txtLatitude.setText("Latitude: " + lat);
        txtLongitude.setText("Longitude: " + lng);
    }

    // Gets permissions and the users location
    private void activateAlarm() {
        // Start sound alarm
        soundAlarm(true);

        if(!checkPermission()) {
            getPermission();
        } else {
            getLocation();
        }

        if(checkPermission()) {
            sendSms();
        }
    }

    // Check if app has permission to send SMS and get location
    private boolean checkPermission() {
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int hasSendSmsPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);

        if(hasFineLocationPermission != PackageManager.PERMISSION_GRANTED || hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED || hasSendSmsPermission != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        return true;
    }

    // Ask for permission to send SMS and get location
    private void getPermission() {
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.SEND_SMS}, REQUEST_CODE);
    }

    // After a permission request this is run automatically
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int hasSendSmsPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);

        if(hasFineLocationPermission == PackageManager.PERMISSION_GRANTED || hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED || hasSendSmsPermission == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            Toast.makeText(this, "Please give permission for SMS and GPS, the application will not work without it.", Toast.LENGTH_LONG).show();
        }
    }

    // Get the actual location
    @SuppressLint("MissingPermission") // We check for permission elsewhere
    public void getLocation() {
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);

        for(String provider: providers) {
            Location l = locationManager.getLastKnownLocation(provider);
            if(l == null) {
                continue;
            }

            if(location == null || l.getAccuracy() < location.getAccuracy()) {
                location = l;
            }
        }
    }

    // Get the coordinates of the user
    public void getCoords() {
        if(location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
        } else {
            Toast.makeText(this, "GPS not found. Remember to turn it on.", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendSms() {
        getCoords();

        SmsManager sms = SmsManager.getDefault();

        String name = user.getName();

        List<Contact> contacts = db.getContacts();
        for(Contact contact: contacts) {
            sms.sendTextMessage(contact.getPhone(), null, name + " has activated the emergency alarm: https://www.google.no/maps/@" + lat + "," + lng + ",21z?hl=no", null, null);
        }

        Toast.makeText(this, "Message sent to emergency contacts", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(ALARM_ACTIVATED, alarmActivated);
        savedInstanceState.putDouble(LATITUDE, lat);
        savedInstanceState.putDouble(LONGITUDE, lng);

        soundAlarm(true);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        soundAlarm(true);
    }

    private void soundAlarm(boolean start) {
        if(start) {
            startService(alarm);
        } else {
            stopService(alarm);
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        // Go back to MainFragment on restart, this way you do not send multiple messages
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onStop() {
        super.onStop();

        // Stop sound alarm
        soundAlarm(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Stop sound alarm
        soundAlarm(false);
        alarmActivated = false;
    }

    // Mandatory functions for location listener
    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
