package dataManagers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
//from http://stackoverflow.com/a/1785300 March 30
public class ConnectionChangeReceiver extends BroadcastReceiver {
	private ClaimListManager claimListManager;
	
	ConnectionChangeReceiver(ClaimantClaimListManager claimListManager){
		super();
		this.claimListManager=claimListManager;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager connectivityManager 
        = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		if(activeNetworkInfo != null){
			if(activeNetworkInfo.isConnected()){
				claimListManager.onConnect();
			}
		}

	}

}
