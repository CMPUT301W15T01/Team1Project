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
import ca.ualberta.cs.team1travelexpenseapp.claims.ApprovedClaim;
import ca.ualberta.cs.team1travelexpenseapp.claims.Claim;
import ca.ualberta.cs.team1travelexpenseapp.claims.ProgressClaim;
import ca.ualberta.cs.team1travelexpenseapp.claims.ReturnedClaim;
import ca.ualberta.cs.team1travelexpenseapp.claims.SubmittedClaim;
import ca.ualberta.cs.team1travelexpenseapp.users.User;

public class ClaimAdapter<T> extends Claim {
	
	T adapteeClaim;
	
	public ClaimAdapter(T claim, Class<?> claimStatusType ) {
		adapteeClaim = claim;
		
		claimantName          = ((Claim) adapteeClaim).getClaimantName();
		startDate             = ((Claim) adapteeClaim).getStartDate();
		endDate               = ((Claim) adapteeClaim).getEndDate();
		destinationReasonList = ((Claim) adapteeClaim).getDestinationReasonList();
		claimTagList          = ((Claim) adapteeClaim).getClaimTagList();
		status                = claimStatusType;
		isComplete            = ((Claim) adapteeClaim).isComplete();
		approverList          = ((Claim) adapteeClaim).getApproverList();
		commentList           = ((Claim) adapteeClaim).getCommentList();
		expenseList           = ((Claim) adapteeClaim).getExpenseList();
	}
	
}
