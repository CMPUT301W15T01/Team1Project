package ca.ualberta.cs.team1travelexpenseapp;

import java.util.ArrayList;
import java.util.Collection;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class EditExpenseActivity extends Activity {
	private ExpenseList expenseList;
	private Spinner categorySpinner;
	private EditText descriptionText;
	private DatePicker datePicker;
	private EditText amountText;
	private Spinner currencySpinner;
	private ImageButton imageButton;
	private Listener listener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_expense);	
		
		expenseList = ExpenseListController.getExpenseList();
		
		listener=new Listener() {			
			@Override
			public void update() {
				updateValues();
			}
		};
		
		expenseList.addListener(listener);
	}

	private void updateValues(){
		//TODO fill in current Values
	}
	
	//TODO 
	//on start method that loads all of the CurrentClaim values into the editTexts
	protected void onStart(){
		super.onStart();

		if((ExpenseListController.getCurrentExpense() != null)){
			updateValues();
		}
	}
	
	public void onExpenseSaveClick(View v) {

		//editing model happens in controller 
		ExpenseListController.onExpenseSaveClick(this);
	}
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_expense, menu);
		return true;
	}
		
	public void onDestroy(){
		super.onDestroy();
		expenseList.removeListener(listener);
	}
}
