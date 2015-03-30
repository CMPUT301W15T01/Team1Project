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

import java.io.File;

import ca.ualberta.cs.team1travelexpenseapp.claims.Claim;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Activity displaying a claim for an approver, approvers get this view instead of EditClaimActivity since they can't edit a claim
 * but may need to view all the info available in the EditClaimActivity.
 */
public class ApproverClaimInfo extends Activity {
	TextView info;
	private Claim currentClaim;
	private Expense expense;
	private ImageView receiptImage;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		currentClaim = SelectedItemsSingleton.getSelectedItemsSingleton().getCurrentClaim();
		expense=SelectedItemsSingleton.getSelectedItemsSingleton().getCurrentExpense();

		setContentView(R.layout.activity_approver_claim_info);
		info = (TextView) findViewById(R.id.ApproverClaimInfoTextView);
		receiptImage = (ImageView) findViewById(R.id.imageViewApproverReceipt);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.approver_claim_info, menu);
		return true;
	}

	public void onStart() {
		super.onStart();
		info.setText(currentClaim.toString() + "\n" + expense.toString());
		
		// Retrieved from http://stackoverflow.com/questions/4181774/show-image-view-from-file-path-in-android (March 29, 2015)
		File imgFile = expense.getReceipt();
		if(imgFile.exists()){

		    Bitmap ReceiptBMP = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
		    receiptImage.setImageBitmap(ReceiptBMP);

		}
	}
}
