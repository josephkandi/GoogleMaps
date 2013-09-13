package za.peruzal.googlemaps;

import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

public class RoutesActivity extends Activity implements LocationListener {
    private static final String TAG = RoutesActivity.class.getSimpleName();
    GoogleMap map;
    LocationManager locationManager;
    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);



        //Find the views
        map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
       //Set the map type
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //Enable My location
        map.setMyLocationEnabled(true);
        //Enable Traffic
        map.setTrafficEnabled(true);


        //Get the Location Manager
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

        //Find last knwown location
        location =  locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Register for location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000,10,this);

        //Add Market ot current position
        Location lastKnownLocation = getBestLastKnowLocation();
        if (lastKnownLocation != null){
            LatLng latLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)).position(latLng).title("Current Location").snippet("You are here"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.routes, menu);
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "New location with coordinates " + location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        Log.i(TAG, "Provider changed to : " + s);
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.i(TAG, "Provider disabled : " + s);
    }

    private Location getBestLastKnowLocation(){
        List<String> matchingProviders = locationManager.getAllProviders();
        for (String provider: matchingProviders) {
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                return location;
            }
        }
        return null;
    }
}
