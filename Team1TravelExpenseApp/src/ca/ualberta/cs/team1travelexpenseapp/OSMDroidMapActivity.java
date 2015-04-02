package ca.ualberta.cs.team1travelexpenseapp;

import java.util.Date;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.MapEventsOverlay;
import org.osmdroid.bonuspack.overlays.MapEventsReceiver;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MyLocationOverlay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class OSMDroidMapActivity extends Activity implements MapEventsReceiver {
	public static final String MOCK_PROVIDER = "mockLocationProvider";
	public GeoPoint startPoint = new GeoPoint(53.5488917, -113.4915883, 14);
	private MyLocationOverlay myLocationOverlay;
	private Marker startMarker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_geolocation);

		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Location location = lm
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location != null) {
			Button tv = (Button) findViewById(R.id.gps);
			tv.setText("Lat: " + location.getLatitude() + "\nLong: "
					+ location.getLongitude());
			startPoint = new GeoPoint(location);

		}
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1,
				listener);

		map = (MapView) findViewById(R.id.map);
		map.setTileSource(TileSourceFactory.MAPNIK);
		map.setBuiltInZoomControls(true);
		map.setMultiTouchControls(true);
		map.setClickable(true);
		IMapController mapController = map.getController();
		mapController.setZoom(9);
		mapController.setCenter(startPoint);

		myLocationOverlay = new MyLocationOverlay(getApplicationContext(), map);
		map.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableCompass();
		myLocationOverlay.enableMyLocation();
		myLocationOverlay.enableFollowLocation();

		startMarker = new Marker(map);
		startMarker.setPosition(startPoint);
		startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
		map.getOverlays().add(startMarker);
		
		MapEventsOverlay evOverlay = new MapEventsOverlay(this, this);
		map.getOverlays().add(evOverlay);
		map.invalidate();
	}

	private final LocationListener listener = new LocationListener() {
		public void onLocationChanged(Location location) {
			Button tv = (Button) findViewById(R.id.gps);
			if (location != null) {
				double lat = location.getLatitude();
				double lng = location.getLongitude();
				Date date = new Date(location.getTime());

				tv.setText("The location is: \nLatitude: " + lat
						+ "\nLongitude: " + lng + "\n at time: "
						+ date.toString());
				// startPoint = new GeoPoint(Location);
			} else {
				tv.setText("Cannot get the location");
			}
		}

		public void onProviderDisabled(String provider) {

		}

		public void onProviderEnabled(String provider) {

		}

		public void onStatusChanged(String provider, int status, Bundle extras) {

		}
	};
	private MapView map;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onClickGeo(View v) {
		Intent intent = new Intent();
		intent.putExtra("longitude",startPoint.getLongitude());
		intent.putExtra("latitude",startPoint.getLatitude());
		setResult(RESULT_OK,intent);
		finish();
	}
	

	@Override
	public boolean longPressHelper(GeoPoint arg0) {
		// TODO Auto-generated method stub
		startPoint = arg0;
		startMarker.setPosition(arg0);
		map.invalidate();
		return false;
	}

	@Override
	public boolean singleTapConfirmedHelper(GeoPoint arg0) {
		// TODO Auto-generated method stub
		startPoint = arg0;
		startMarker.setPosition(arg0);
		map.invalidate();
		Toast.makeText(getApplicationContext(), "fdafasdfsdf", 1);
		return false;
	}
}