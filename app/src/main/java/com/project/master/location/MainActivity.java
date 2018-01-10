package com.project.master.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

  static final int REQUEST_LOCATION =1;
  LocationManager mLocationManager;
  private  String userid;
  FirebaseFirestore fstore;


  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

  mLocationManager =(LocationManager)getSystemService(Context.LOCATION_SERVICE) ;
    fstore = FirebaseFirestore.getInstance();
    userid = "Ibrahim";
    getLocation();
  }

  private void getLocation() {
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
        != PackageManager.PERMISSION_GRANTED){
      ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
    }else{
      Location location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
      if (location != null){
        double latti = location.getLatitude();
        double longi = location.getLongitude();
        double speed = location.getSpeed();
        double time_stamp = location.getTime();
        Map<String, Object> newLocation = new HashMap<>();

        newLocation.put("userid", userid);
        newLocation.put("timestamp", time_stamp );
        newLocation.put("latitude", latti);
        newLocation.put("longtitude", longi);
        newLocation.put("velocity", speed);

        fstore.collection("locations").document().set(newLocation).addOnSuccessListener(new OnSuccessListener<Void>() {
          @Override public void onSuccess(Void aVoid) {
            Toast.makeText(MainActivity.this, "Location Registered", Toast.LENGTH_SHORT).show();
          }
        }).addOnFailureListener(new OnFailureListener() {

          @Override public void onFailure(@NonNull Exception e) {
            Toast.makeText(MainActivity.this, "ERROR" + e.toString(), Toast.LENGTH_SHORT).show();
            Log.d("TAG", e.toString());
          }
        });
        ((TextView)findViewById(R.id.Latitude)).setText("Latitude : "+latti);
        ((TextView)findViewById(R.id.Longitude)).setText("Longitude : "+longi);

      }else{
        ((TextView)findViewById(R.id.Latitude)).setText("Unable to find Latitude");
        ((TextView)findViewById(R.id.Longitude)).setText("Unable to find Latitude");
      }
    }
  }
    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String [] permissions,@NonNull int [] result ){
      super.onRequestPermissionsResult(requestCode,permissions,result);

      switch (requestCode){
          case REQUEST_LOCATION:
            getLocation();
            break;

      }

    }


}
