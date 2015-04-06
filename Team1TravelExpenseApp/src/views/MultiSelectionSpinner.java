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

//taken and modified from http://v4all123.blogspot.in/2013/09/spinner-with-multiple-selection-in.html (March 14 2015), used with permission of author
package views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import ca.ualberta.cs.team1travelexpenseapp.ClaimantClaimsListActivity;
import ca.ualberta.cs.team1travelexpenseapp.R;
import ca.ualberta.cs.team1travelexpenseapp.R.id;
import ca.ualberta.cs.team1travelexpenseapp.claims.Claim;
import ca.ualberta.cs.team1travelexpenseapp.singletons.UserSingleton;
import ca.ualberta.cs.team1travelexpenseapp.users.Claimant;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.nfc.Tag;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

/**
 * Implements a "MultiSelectionSpinner", when clicked it presents a dialog
 * allowing for selection of a subset of items from a list using checkboxes.
 * Selected items will then show as a comma separated list on the face of the
 * spinner. The methods allow setting and getting of the selected items, as well
 * as the entire list of possible items. It can store an arrayList of arbitrary
 * objects in order to recover the selected objects directly.
 *
 */
public class MultiSelectionSpinner<T> extends Spinner implements
		OnMultiChoiceClickListener {
	String[] _items = null;
	boolean[] mSelection = null;
	ArrayList<T> itemsList = null;
	Context context;
	ArrayAdapter<String> simple_adapter;

	/**
	 * This constructor initializes the adapter for the multiselection spinner's
	 * listview
	 *
	 * @param context
	 *            the context of the application
	 * @since 1.0
	 */

	public MultiSelectionSpinner(Context context) {
		super(context);
		this.context = context;
		simple_adapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item);
		super.setAdapter(simple_adapter);
	}

	/**
	 * This constructor initializes the adapter for the multiselection spinner's
	 * listview
	 *
	 * @param context
	 *            the context of the application
	 * @param attrs
	 *            the attribute set of the spinner
	 * @since 1.0
	 */
	public MultiSelectionSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		simple_adapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item);
		super.setAdapter(simple_adapter);
	}

	/**
	 * The onClick method when the user clicks on a list item on the spinner
	 * <p>
	 *
	 *
	 * @param dialog The alertdialog object
	 * @param which position of the item
	 * @param isChecked sets the item's checkbox to the bool
	 * 
	 * @since 1.0
	 */
	public void onClick(DialogInterface dialog, int which, boolean isChecked) {
		if (mSelection != null && which < mSelection.length) {
			mSelection[which] = isChecked;

			simple_adapter.clear();
			simple_adapter.add(buildSelectedItemString());
		} else {
			throw new IllegalArgumentException(
					"Argument 'which' is out of bounds.");
		}
	}

	/**
	 * 
	 * Creates the AlertDialog for the spinner and populates the listview with
	 * tags from the current claim 
	 *
	 * @return <code>true</code> if the operation successfully completed
	 * @since 1.0
	 */
	@Override
	public boolean performClick() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setMultiChoiceItems(_items, mSelection, this);

		builder.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				MultiSelectionSpinner<Tag> tagSpinner = (MultiSelectionSpinner<Tag>) findViewById(R.id.claimFilterSpinner);
				try {
					Claimant user = (Claimant) UserSingleton.getUserSingleton()
							.getUser();
					ClaimantClaimsListActivity activity = (ClaimantClaimsListActivity) getContext();
					ArrayList<Claim> displayList = activity.getDisplayList();
					ArrayAdapter<Claim> claimsAdapter = activity
							.getArrayAdapter();
					List<String> tags = tagSpinner.getSelectedStrings();

					if (tags.size() == 0) {
						displayList.clear();
						Collection<Claim> claims = user.getClaimList()
								.getClaims();
						displayList.addAll(claims);
						claimsAdapter.notifyDataSetChanged();
					} else {
						displayList.clear();
						Collection<Claim> claims = user.getClaimList()
								.getClaims();
						displayList.addAll(claims);
						ArrayList<Claim> list = new ArrayList<Claim>();
						for (Claim claim : displayList) {
							for (String tag : claim.getClaimTagNameList()) {
								if (tags.contains(tag)) {
									list.add(claim);
								}
							}
						}
						displayList.clear();
						displayList.addAll(list);
						claimsAdapter.notifyDataSetChanged();
					}
				} catch (Exception e) {

				}
			}
		});

		builder.show();
		return true;
	}

	/**
	 * Throws runtime exception if adapter is set through this contructor
	 * @throws RuntimeException since setAdapter is not supported by MultiSelectSpinner.
	 * @since 1.0
	 */
	@Override
	public void setAdapter(SpinnerAdapter adapter) {
		throw new RuntimeException(
				"setAdapter is not supported by MultiSelectSpinner.");
	}

	/**
	 * Sets the strings of spinner items
	 * @param items the list of strings in the spinner
	 * @since 1.0
	 */
	public void setItems(String[] items) {
		_items = items;
		mSelection = new boolean[_items.length];
		simple_adapter.clear();
		Arrays.fill(mSelection, false);
	}

	/**
	 * Sets the strings of spinner items
	 * @param items the collection list of strings in the spinner
	 * @since 1.0
	 */
	public void setItems(List<String> items) {
		_items = items.toArray(new String[items.size()]);
		mSelection = new boolean[_items.length];
		simple_adapter.clear();
		Arrays.fill(mSelection, false);
	}

	/**
	 * Sets the strings of spinner items
	 * @param items the arraylist of strings in the spinner
	 * @since 1.0
	 */
	public void setItems(ArrayList<T> items) {
		itemsList = items;
		ArrayList<String> stringList = new ArrayList<String>();
		for (Object i : items) {
			stringList.add(i.toString());
		}
		_items = stringList.toArray(new String[items.size()]);
		mSelection = new boolean[_items.length];
		simple_adapter.clear();
		Arrays.fill(mSelection, false);
	}

	/**
	 * Sets the checkboxes of whether the item has been selected
	 * @param selection The strings of selected items
	 * 
	 * @since 1.0
	 */
	public void setSelection(String[] selection) {
		for (String cell : selection) {
			for (int j = 0; j < _items.length; ++j) {
				if (_items[j].equals(cell)) {
					mSelection[j] = true;
				}
			}
		}
	}

	/**
	 * Sets the checkboxes of whether the item has been selected
	 * @param selection The list collections of strings of selected items
	 * 
	 * @since 1.0
	 */
	public void setSelection(List<String> selection) {
		for (int i = 0; i < mSelection.length; i++) {
			mSelection[i] = false;
		}
		for (String sel : selection) {
			for (int j = 0; j < _items.length; ++j) {
				if (_items[j].equals(sel)) {
					mSelection[j] = true;
				}
			}
		}
		simple_adapter.clear();
		simple_adapter.add(buildSelectedItemString());
	}
	/**
	 * Sets the checkboxes of whether the item has been selected
	 * @param selection The arraylist of the selected items
	 * 
	 * @since 1.0
	 */
	public void setSelection(ArrayList<T> selection) {
		for (int i = 0; i < mSelection.length; i++) {
			mSelection[i] = false;
		}
		for (Object sel : selection) {
			for (int j = 0; j < _items.length; ++j) {
				if (itemsList.get(j).equals(sel)) {
					mSelection[j] = true;
				}
			}
		}
		simple_adapter.clear();
		simple_adapter.add(buildSelectedItemString());
	}

	/**
	 * Sets the position of the spinner's item checkbox to true
	 * @param index position of the item
	 * @since 1.0
	 */
	public void setSelection(int index) {
		for (int i = 0; i < mSelection.length; i++) {
			mSelection[i] = false;
		}
		if (index >= 0 && index < mSelection.length) {
			mSelection[index] = true;
		} else {
			throw new IllegalArgumentException("Index " + index
					+ " is out of bounds.");
		}
		simple_adapter.clear();
		simple_adapter.add(buildSelectedItemString());
	}

	/**
	 * Sets the positions of the spinner's item checkbox to true
	 * @param index list of positions of the item
	 * @since 1.0
	 */
	public void setSelection(int[] selectedIndicies) {
		for (int i = 0; i < mSelection.length; i++) {
			mSelection[i] = false;
		}
		for (int index : selectedIndicies) {
			if (index >= 0 && index < mSelection.length) {
				mSelection[index] = true;
			} else {
				throw new IllegalArgumentException("Index " + index
						+ " is out of bounds.");
			}
		}
		simple_adapter.clear();
		simple_adapter.add(buildSelectedItemString());
	}
	/**
	 * Gets the strings of the spinner's listview
	 * @return the list collection of spinner item strings
	 * @since 1.0
	 */
	public List<String> getSelectedStrings() {
		List<String> selection = new LinkedList<String>();
		for (int i = 0; i < _items.length; ++i) {
			if (mSelection[i]) {
				selection.add(_items[i]);
			}
		}
		return selection;
	}
	/**
	 * Gets the strings of the spinner's listview
	 * @return the arraylist collection of spinner item strings
	 * @since 1.0
	 */
	public ArrayList<T> getSelectedItems() {
		ArrayList<T> selectedItems = new ArrayList<T>();
		for (int i = 0; i < mSelection.length; i++) {
			if (mSelection[i]) {
				selectedItems.add(itemsList.get(i));
			}
		}
		return selectedItems;
	}

	/**
	 * Gets the indices of the spinner's listview
	 * @return the list collection of spinner item indices
	 * @since 1.0
	 */
	public List<Integer> getSelectedIndicies() {
		List<Integer> selection = new LinkedList<Integer>();
		for (int i = 0; i < _items.length; ++i) {
			if (mSelection[i]) {
				selection.add(i);
			}
		}
		return selection;
	}

	/**
	 * Creates a string from the selected item in spinner
	 * @return String representation of the selected item
	 * @since 1.0
	 */
	private String buildSelectedItemString() {
		StringBuilder sb = new StringBuilder();
		boolean foundOne = false;

		for (int i = 0; i < _items.length; ++i) {
			if (mSelection[i]) {
				if (foundOne) {
					sb.append(", ");
				}
				foundOne = true;

				sb.append(_items[i]);
			}
		}
		return sb.toString();
	}


	/**
	 * Creates a string from the selected items in spinner
	 * @return String representation of the selected items
	 * @since 1.0
	 */
	public String getSelectedItemsAsString() {
		StringBuilder sb = new StringBuilder();
		boolean foundOne = false;

		for (int i = 0; i < _items.length; ++i) {
			if (mSelection[i]) {
				if (foundOne) {
					sb.append(", ");
				}
				foundOne = true;
				sb.append(_items[i]);
			}
		}
		return sb.toString();
	}
}