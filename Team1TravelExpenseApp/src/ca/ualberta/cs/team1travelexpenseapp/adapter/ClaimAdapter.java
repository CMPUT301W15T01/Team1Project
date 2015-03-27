package ca.ualberta.cs.team1travelexpenseapp.adapter;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import android.util.Log;
import ca.ualberta.cs.team1travelexpenseapp.ClaimListController;
import ca.ualberta.cs.team1travelexpenseapp.Expense;
import ca.ualberta.cs.team1travelexpenseapp.ExpenseList;
import ca.ualberta.cs.team1travelexpenseapp.Listener;
import ca.ualberta.cs.team1travelexpenseapp.Tag;
import ca.ualberta.cs.team1travelexpenseapp.User;
import ca.ualberta.cs.team1travelexpenseapp.claims.ApprovedClaim;
import ca.ualberta.cs.team1travelexpenseapp.claims.BasicClaim;
import ca.ualberta.cs.team1travelexpenseapp.claims.ProgressClaim;
import ca.ualberta.cs.team1travelexpenseapp.claims.ReturnedClaim;
import ca.ualberta.cs.team1travelexpenseapp.claims.SubmittedClaim;

public class ClaimAdapter<T> extends BasicClaim {
	
	T adapteeClaim;
	
	public ClaimAdapter(T claim, Class<?> claimStatusType ) {
		adapteeClaim = claim;
		
		claimantName          = ((BasicClaim) adapteeClaim).getClaimantName();
		startDate             = ((BasicClaim) adapteeClaim).getStartDate();
		endDate               = ((BasicClaim) adapteeClaim).getEndDate();
		destinationReasonList = ((BasicClaim) adapteeClaim).getDestinationReasonList();
		claimTagList          = ((BasicClaim) adapteeClaim).getClaimTagList();
		status                = claimStatusType;
		isComplete            = ((BasicClaim) adapteeClaim).isComplete();
		approverList          = ((BasicClaim) adapteeClaim).getApproverList();
		commentList           = ((BasicClaim) adapteeClaim).getCommentList();
		expenseList           = ((BasicClaim) adapteeClaim).getExpenseList();
	}
	
}
