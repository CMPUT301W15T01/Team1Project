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

import java.util.Map;

import ca.ualberta.cs.team1travelexpenseapp.claims.Claim;
import ca.ualberta.cs.team1travelexpenseapp.singletons.SelectedItemsSingleton;
import ca.ualberta.cs.team1travelexpenseapp.singletons.UserSingleton;
import ca.ualberta.cs.team1travelexpenseapp.users.User;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;
/**
 * The view for viewing comments attached to claims as a claimant
 * 
 *
 */
public class ClaimantCommentActivity extends Activity {
	
	Claim claim;
	TextView comments;
	User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_claimant_comment);
		user=UserSingleton.getUserSingleton().getUser();
		claim = SelectedItemsSingleton.getSelectedItemsSingleton().getCurrentClaim();
	}
	
	public void onStart() {
		super.onStart();
		comments = (TextView) findViewById(R.id.claimantCommentString);
		//String commentString = claim.getCommentList().toString();
		String commentString = "";
		Map<String, String> commentList = claim.getCommentList();
		// how to iterate through keys found at http://stackoverflow.com/questions/46898/iterate-over-each-entry-in-a-map (March 15, 2015)
		for(Map.Entry<String, String> comment : commentList.entrySet()) {
			commentString += comment.getKey() + "\n" + comment.getValue();
		}
		comments.setText(commentString);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.claimant_comment, menu);
		return true;
	}

}
