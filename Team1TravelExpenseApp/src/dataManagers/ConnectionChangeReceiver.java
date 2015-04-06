package dataManagers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/*
 * from http://stackoverflow.com/a/1785300 March 30
 * 
 * ConnectionChangerReceiver is a network listener that detects the connectivity of the device
 * and saves to the online/offline location using the {@link ClaimListManager}
 * 
 * @since 1.0
 */
public class ConnectionChangeReceiver extends BroadcastReceiver {
	private ClaimListManager claimListManager;

	ConnectionChangeReceiver(ClaimantClaimListManager claimListManager) {
		super();
		this.claimListManager = claimListManager;
	}

	/*
	 * Connects to the web server when connectivity has been established
	 * @param context of the application
	 * @param intent of activity
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		if (activeNetworkInfo != null) {
			if (activeNetworkInfo.isConnected()) {
				claimListManager.onConnect();
			}
		}

	}

}
