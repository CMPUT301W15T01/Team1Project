package ca.ualberta.cs.team1travelexpenseapp;

import java.util.ArrayList;

import org.osmdroid.util.GeoPoint;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import ca.ualberta.cs.team1travelexpenseapp.claims.Claim;
import ca.ualberta.cs.team1travelexpenseapp.singletons.UserSingleton;
import ca.ualberta.cs.team1travelexpenseapp.users.User;

public class ClaimArrayAdapter extends ArrayAdapter<Claim> {

	public ClaimArrayAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}

	public ClaimArrayAdapter(Context context, int resource,
			ArrayList<Claim> displayList) {
		super(context, resource, displayList);
	}

	//Retrieved from http://stackoverflow.com/questions/340209/generate-colors-between-red-and-green-for-a-power-meter (April 3,2015)
	//Returns Traffic light colors based on distance
	public int getDistanceColor(double value) {
		if (value >= 10000000) {
			value = 0;
		} else {
			value = 1 - value / 10000000;
		}
		return android.graphics.Color.HSVToColor(new float[] {
				(float) value * 120f, 1f, 1f });
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = super.getView(position, convertView, parent);
		// Get the data item for this position
		Claim claim = getItem(position);
		User user=UserSingleton.getUserSingleton().getUser();
		ArrayList<Destination> dest = claim.getDestinationList();
		if (dest.size() == 0) {
			return convertView;
		}
		GeoPoint start = new GeoPoint(user.getLocation());
		GeoPoint end = new GeoPoint(dest.get(0).getLocation());
		int distance = start.distanceTo(end);
		int color = getDistanceColor(distance);
		convertView.setBackgroundColor(color);
		return convertView;
	}
}
