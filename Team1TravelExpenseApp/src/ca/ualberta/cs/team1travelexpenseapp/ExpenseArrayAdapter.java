package ca.ualberta.cs.team1travelexpenseapp;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import ca.ualberta.cs.team1travelexpenseapp.singletons.UserSingleton;
import ca.ualberta.cs.team1travelexpenseapp.users.User;

public class ExpenseArrayAdapter extends ArrayAdapter<Expense> {

	public ExpenseArrayAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}

	public ExpenseArrayAdapter(Context context, int resource,
			ArrayList<Expense> items) {
		super(context, resource, items);
	}

	//Retrieved from http://stackoverflow.com/questions/340209/generate-colors-between-red-and-green-for-a-power-meter (April 3,2015)
	//Returns Traffic light colors based on distance
	public int getDistanceColor(double value) {
		if (value >= 20000000) {
			value = 20000000;
		}
			value=value / 20000000;
		return android.graphics.Color.HSVToColor(new float[] {
				(float) (1-value) * 120f, 1f, 1f });
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = super.getView(position, convertView, parent);
		// Get the data item for this position
		Expense expense = getItem(position);
		User user=UserSingleton.getUserSingleton().getUser();
		float distance = expense.getLocation().distanceTo(user.getLocation());
		Log.d("distanceTest", expense.getLocation().toString());
		Log.d("distanceTest", user.getLocation().toString());
		int color = getDistanceColor(distance);
		convertView.setBackgroundColor(color);
		return convertView;
	}
}
