package ca.ualberta.cs.team1travelexpenseapp.claims;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import android.util.Log;
import ca.ualberta.cs.team1travelexpenseapp.Destination;
import ca.ualberta.cs.team1travelexpenseapp.Expense;
import ca.ualberta.cs.team1travelexpenseapp.ExpenseList;
import ca.ualberta.cs.team1travelexpenseapp.Listener;
import ca.ualberta.cs.team1travelexpenseapp.Tag;
import ca.ualberta.cs.team1travelexpenseapp.singletons.UserSingleton;
import ca.ualberta.cs.team1travelexpenseapp.users.Claimant;
import ca.ualberta.cs.team1travelexpenseapp.users.User;

public class Claim implements ClaimInfo, Comparable<Claim> {
	

	private ExpenseList expenseList;
	private String claimantName;
	private Date startDate;
	private Date endDate;
	private ArrayList<Destination> destinationList;
	private ArrayList<Tag> claimTagList;
	private boolean isComplete;
	private ArrayList<User> approverList;
	private Map<String, String> commentList;
	private ArrayList<Listener> listeners;
	transient private Class<?> status;
	private UUID uniqueId;
	private boolean synced;
	
	/*//from http://stackoverflow.com/a/22081826 March 29 2015
	private static final RuntimeTypeAdapterFactory<Claim> adapter = 
            RuntimeTypeAdapterFactory.of(Claim.class);

    private static final HashSet<Class<?>> registeredClasses= new HashSet<Class<?>>();

    static {
        GsonUtils.registerType(adapter);
    }
    
    private synchronized void registerClass() {
        if (!this.registeredClasses.contains(this.getClass())) {
            adapter.registerSubtype(this.getClass());
            registeredClasses.add(this.getClass());
        }
    }*/
	
	
	public UUID getUniqueId() {
		return uniqueId;
	}

	public boolean isSynced() {
		return synced;
	}

	public void setSynced(boolean synced) {
		this.synced = synced;
	}

	
	/** Initializes attributes to new instances **/
	public Claim() { 
		//registerClass();
		claimantName          = "";
		startDate             = new Date();
		endDate               = new Date();
		destinationList = new ArrayList<Destination>();
		claimTagList          = new ArrayList<Tag>();
		status                = Claim.class;
		isComplete            = false;
		approverList          = new ArrayList<User>();
		commentList           = new HashMap<String, String>();
		listeners             = new ArrayList<Listener>();
		expenseList           = new ExpenseList();
		synced                = false;
		uniqueId              = UUID.randomUUID();
	}

	/** set claimant name, start and end date, all other attributes are initializes to new instances 
	 * @param cName - a string
	 * @param sDate - a Date
	 * @param eDate - a Date **/
	public Claim(String cName, Date sDate, Date eDate) {
		//registerClass();
		claimantName = cName;
		startDate = sDate;
		endDate = eDate;
		
		destinationList = new ArrayList<Destination>();
		claimTagList          = new ArrayList<Tag>();
		status                = Claim.class;
		isComplete            = false;
		approverList          = new ArrayList<User>();
		commentList           = new HashMap<String, String>();
		listeners             = new ArrayList<Listener>();
		expenseList           = new ExpenseList();
		synced                = false;
		uniqueId              = UUID.randomUUID();
	}
	
	
	/** 	 
	 * returns a exenepseList object that contains 
	 * list of expenses for the claim 
	 * @param 
	 * @return ExpenseList object **/
	public ExpenseList getExpenseList() {
		return expenseList;
	}

	
	/** 
	 * sets the claim's expense list object to a given expenseList
	 * @param expenseList
	 */
	public void setExpenseList(ExpenseList expenseList) {
		this.expenseList = expenseList;
	}
	
	/**
	 * adds a destination, with a reason to the claim 
	 * if destination already exist, new reason will write over old reason 
	else new destination will reason will be added to the Map 
	 * @param destination - a string
	 * @param reason - a string 
	 */
	public void addDestination(Destination destination) {
			destinationList.add(destination);
	}
	
	
	/**
	 * Returns a HashMap with destinations as keys and reasons as values.
	 * @return HashMap of destinations (String) mapped to reasons (String).
	 */
	public ArrayList<Destination> getDestinationList(){
		return this.destinationList;
	}
	
	/**
	 * Return the number of destinations in the Claim.
	 * @return int corresponding to number of destinations in Claim.
	 */
	private int getDestinationCount() {
		return destinationList.size();
	}
	
	/**
	 * Set the claimantName for the Claim.
	 * @param name The name of the claimant for the Claim.
	 */
	public void setClaimantName(String name) {
		claimantName = name;
	}

	/**
	 * Get the claimantName for the Claim.
	 * @return name The name of the claimant for the Claim.
	 */
	public String getClaimantName() {
		return claimantName;
	}

	/**
	 * Get the ArrayList of Tags claimTagList for the claim.
	 * @return ArrayList of Tags containing the tags set for the claim.
	 */
	public ArrayList<Tag> getClaimTagList() {
		return claimTagList;
	}
	
	/**
	 * Get a list of the names of all the tags attached to the claim
	 * @return
	 */
	public ArrayList<String> getClaimTagNameList() {
		ArrayList<String> tagNames = new ArrayList<String>();
		ArrayList<Tag> tags = getClaimTagList();
		for(Tag tag : tags) {
			tagNames.add(tag.getName());
		}
		
		return tagNames;
	}
	
	/**
	 * Get the number of tags in the claim.
	 * @return int corresponding the number of tags set for the claim.
	 */
	private int getTagCount() {
		return claimTagList.size();
	}

	/**
	 * Set the TagList for the claim
	 * @param claimTagList The TagList containing the new tags to be set for the claim.
	 */
	public void setClaimTagList(ArrayList<Tag> claimTagList) {
		this.claimTagList = claimTagList;
	}

	/**
	 * Return a boolean indicating whether the claim is "complete".
	 * @return boolean indicating whether claim is complete.
	 */
	public boolean isComplete() {
		return isComplete;
	}

	/**
	 * Set the completeness of the claim.
	 * @param isComplete true or false, is the claim complete?
	 */
	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}

	/**
	 * Get the list of approvers for the current Claim
	 * @return ArrayList of Users corresponding to the approvers who have returned or approved the claim
	 */
	public ArrayList<User> getApproverList() {
		return approverList;
	}

	/**
	 * Set the list of approvers for the current Claim
	 * @param approverList ArrayList of Users corresponding to the approvers who have returned or approved the claim
	 */
	public void setApproverList(ArrayList<User> approverList) {
		this.approverList = approverList;
	}

	/**
	 * Get a Map mapping approvers of the claim to any comments they may have left.
	 * @return Map from approver name (String) to that approver's comments (String).
	 */
	public Map<String, String> getCommentList() {
		return commentList;
	}
	
	/**
	 * Add a comment to the claim from the current User.
	 * @param comment String to be added as comment.
	 */
	public void addComment(String comment) {
		commentList.put(UserSingleton.getUserSingleton().getUser().getName(), comment);
	}

	/**
	 * Return the startDate for the Claim.
	 * @return startDate (Date) of the Claim.
	 */
	public Date getStartDate() {
		return startDate;
	}
	
	/**
	 * Set the startDate of the Claim.
	 * @param date startDate to be set for the current Claim.
	 */
	public void setStartDate(Date date) {
		startDate = date;
	}
	
	/**
	 * Return the endDate for the Claim.
	 * @return endDate (Date) of the Claim.
	 */
	public Date getEndDate() {
		return endDate;
	}
	
	/**
	 * Set the endDate of the Claim.
	 * @param date endDate to be set for the current Claim.
	 */
	public void setEndDate(Date date) {
		endDate = date;
	}
	
	/**
	 * Return a map from currency types (String) to amounts (BigDecimal) of the currency spent in the expenses of the Claim.
	 * @return Map from currency types (String) to amount of that currency spent (BigDecimal).
	 */
	public Map<String, BigDecimal> getCurrencyTotals() {
		HashMap<String, BigDecimal> counts = new HashMap<String, BigDecimal>();	
		for (Expense expense : this.getExpenseList().getExpenses()){
			if(counts.containsKey(expense.getCurrency())){
				Log.d("String test", expense.getAmount().toString());
				counts.put(expense.getCurrency(), expense.getAmount().add(counts.get(expense.getCurrency())));
			}
			else {
				counts.put(expense.getCurrency(), expense.getAmount());
			}
		}
		return counts;
	}
	
	public String getCurrencyTotal(String currency) {
		return getCurrencyTotals().get(currency).toString();
	}
	
	/**
	 * Return a String representation of the information contained in the claim to be printed to a list item or similar.
	 */
	public String toString(){
		
		String str = claimantName + "\n";
		
		//date format, has year month day 
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		str += "Starting Date of travel: " + dateformat.format(getStartDate()) + "\n";
		str += "End Date: " + dateformat.format(getEndDate()) + "\n";
		Iterator<Destination> destinations = getDestinationList().iterator();
		str += "Destinations:";
		while (destinations.hasNext()) {
			Destination tempDest = destinations.next();
			//if has next iterator or only has one destination
			if (destinations.hasNext() || (getDestinationCount() == 1) ) {
				str += " " + tempDest.getName();
				if (getDestinationCount() != 1) {
					str += ",";
				}
			} else {
				str += " and " + tempDest.getName();
			}
		}
		
		//get status
		str += "\nStatus: " + getStatusString();
		
		//get tag list 
		str += "\nTags:";
		Iterator<Tag> tags = getClaimTagList().iterator();
		while (tags.hasNext()) {
			String tempTag = tags.next().toString();
			//if has next iterator or only has one tag
			if (tags.hasNext() || (getTagCount() == 1) ) {
				str += " " + tempTag;
				if (getTagCount() != 1) {
					str += ",";
				}
			} else {
				str += " and " + tempTag;
			}
		}
		str+="\nTotals: ";
		//get total currency amounts
		Map<String,BigDecimal> totals = getCurrencyTotals();
	    for(Map.Entry<String, BigDecimal> currency: totals.entrySet()) {
	         // add each currency to string
	    	//Log.d("String test", currency.getValue().toString());
	    	if(currency.getValue().floatValue()==0 || currency.getKey().equals("") ) {
	    		continue;
	    	}
	    	str += currency.getValue() + "-" + currency.getKey() + " ";
	    }
		if (getStatus() == SubmittedClaim.class) { str+= "\nApprovers: " + approverList.toString(); }
		
		return str;
		
	}

	public String getStatusString() {
		return "";
	}

	//sorting
	//https://docs.oracle.com/javase/tutorial/collections/interfaces/order.html 03/24/15 
	/**
	 * override comapreTo method so that claims will be sorted by largest to smallest date 
	 * for a Claimant user and oldest to newest for an Approver user. Note only two types of users exists.
	 * use Collections.sort(ArrayList<Claim> Object); to sort Object. 
	 */
	@Override
	public int compareTo( Claim claim ) {
		if (UserSingleton.getUserSingleton().getUserType().equals("Claimant")) {
			return claim.getStartDate().compareTo(this.startDate);
		}
		return this.startDate.compareTo(claim.getStartDate());
	}

	/**
	 * Get the status (inProgress, submitted, approved, returned) for the claim.
	 * @return Status for the claim.
	 */
	public Class<?> getStatus() {
		return status;
	}

	@Override
	public Claim changeStatus(Class<?> claimStatusType) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isSubmittable() {
		// TODO Auto-generated method stub
		return status != SubmittedClaim.class && 
				status != ApprovedClaim.class;
	}
	
}
