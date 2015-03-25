
/*
Copyright 2015 Jeffrey Oduro, Cody Ingram, Boyan Peychoff, Kenny Young, Dennis Truong, Victor Olivares 

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package ca.ualberta.cs.team1travelexpenseapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

/**
 * 
 * Activity providing login screen where user can enter a user name for them self and then choose to enter either the claimant or
 * approver mode of the application.
 *
 */
public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
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
	
	/**
	 * Start the ApproverClaimsListActivity if the user chooses to log in as an approver.
	 * @param v the button clicked by the user.
	 */
	public void approverLogin(View v) {
		Intent intent = new Intent(LoginActivity.this,ApproverClaimsListActivity.class);
		EditText userNameField = (EditText) findViewById(R.id.userNameField);
		String currentUserName=userNameField.getText().toString();
		if(currentUserName.equals("")) currentUserName="Guest";
		Approver currentUser=new Approver(currentUserName);
		UserSingleton.getUserSingleton().setUser(currentUser);
		ClaimList claimList=currentUser.getClaimList();
		claimList.getManager().setContext(getApplicationContext());
		claimList.loadClaims();
		startActivity(intent);
	}
	
	/**
	 * Start the ClaimantClaimsListActivity if the user chooses to log in as an claimant.
	 * @param v the button clicked by the user.
	 */
	public void claimantLogin(View v){
		Intent intent = new Intent(LoginActivity.this,ClaimantClaimsListActivity.class);
		EditText userNameField = (EditText) findViewById(R.id.userNameField);
		String currentUserName=userNameField.getText().toString();
		if(currentUserName.equals("")) currentUserName="Guest";
		Claimant currentUser=new Claimant(currentUserName);
		UserSingleton.getUserSingleton().setUser(currentUser);
		currentUser.getTagList().getManager().setContext(getApplicationContext());
		currentUser.getTagList().loadTags();
		currentUser.getClaimList().getManager().setContext(getApplicationContext());
		currentUser.getClaimList().loadClaims();
		startActivity(intent);
	}
}
