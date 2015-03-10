package ca.ualberta.cs.team1travelexpenseapp;


import java.util.Calendar;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;


public class EditExpenseActivity extends Activity {
	private ExpenseList expenseList;
	private Listener listener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_expense);	
		
		expenseList = ExpenseListController.getCurrentExpenseList();
		
		listener=new Listener() {			
			@Override
			public void update() {
				updateValues();
			}
		};
		
		expenseList.addListener(listener);
	}

	private void updateValues(){

		Expense expense = ExpenseListController.getCurrentExpense();
		Spinner categorySpinner = (Spinner) this.findViewById(R.id.categorySelector);	
		for (int i = 0; i < categorySpinner.getAdapter().getCount();++i){
			if (String.valueOf(categorySpinner.getItemAtPosition(i)).equals(expense.getCategory())){
				categorySpinner.setSelection(i);
				break;
			}
		}
		EditText descriptionView = (EditText) this.findViewById(R.id.descriptionBody);
		descriptionView.setText(expense.getDescription());
		

		DatePicker dateView = (DatePicker) this.findViewById( R.id.expenseDate);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(expense.getDate());
		dateView.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));

		EditText amountView = (EditText) this.findViewById(R.id.currencyBody);
		amountView.setText(expense.getAmount().toString());
		
		Spinner currencySpinner = (Spinner) this.findViewById(R.id.currencySelector);
		for (int i = 0; i < currencySpinner.getAdapter().getCount();++i){
			if (String.valueOf(currencySpinner.getItemAtPosition(i)).equals(expense.getCurrency())){
				currencySpinner.setSelection(i);
				break;
			}
		}
	}
	
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
