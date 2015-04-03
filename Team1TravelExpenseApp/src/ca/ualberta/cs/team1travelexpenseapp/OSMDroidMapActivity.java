package ca.ualberta.cs.team1travelexpenseapp;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.MapEventsOverlay;
import org.osmdroid.bonuspack.overlays.MapEventsReceiver;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MyLocationOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class OSMDroidMapActivity extends Activity implements MapEventsReceiver {
	public static final String MOCK_PROVIDER = "mockLocationProvider";
	public GeoPoint startPoint = new GeoPoint(53.5488917, -113.4915883, 14);
	private MyLocationOverlay myLocationOverlay;
	private Marker startMarker;
	private MapView map;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_geolocation);

		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Location location = lm
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location != null) {

			startPoint = new GeoPoint(location);

		}
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1,
				listener);

		// Retrieved from
		// http://stackoverflow.com/questions/22804650/how-to-handle-long-press-on-a-map-using-osmdroid-osmbonuspack-in-android
		// (April 2, 2015)
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

		//Retrieved from http://android-coding.blogspot.ca/2012/07/osmdroid-mapview-to-follow-user.html (April 3, 2015)
		myLocationOverlay.runOnFirstFix(new Runnable() {
			public void run() {
				map.getController()
						.animateTo(myLocationOverlay.getMyLocation());
			}
		});

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

		}

		public void onProviderDisabled(String provider) {

		}

		public void onProviderEnabled(String provider) {

		}

		public void onStatusChanged(String provider, int status, Bundle extras) {

		}
	};

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		myLocationOverlay.enableMyLocation();
		myLocationOverlay.enableFollowLocation();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		myLocationOverlay.disableMyLocation();
		myLocationOverlay.disableFollowLocation();
	}

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
		intent.putExtra("longitude", startPoint.getLongitude());
		intent.putExtra("latitude", startPoint.getLatitude());
		setResult(RESULT_OK, intent);
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
		return false;
	}
}