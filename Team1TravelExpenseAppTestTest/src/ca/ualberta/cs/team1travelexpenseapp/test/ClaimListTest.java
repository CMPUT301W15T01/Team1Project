package ca.ualberta.cs.team1travelexpenseapp.test;

import junit.framework.TestCase;

public class ClaimListTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public void addClaimTest() {
		Claim claim = new Claim();
		claim.setName("name");
		claim.setStartDate(new Date(2000,11,11));
		claim.setEndDate(new Date(2015,12,12));
		assertEquals("name?","name",claim.getName());
		assertEquals("start date?",new Date(2000,11,11),claim.getStartDate());
		assertEquals("end date?",new Date(2015,12,12),claim.getEndDate());
	}
	
	public void enterDestinationTest() {
		Claim claim = new Claim();
		claim.addDestination("dest 1");
		assertEquals("Destination","dest 1",claim.getDestination(1));
		claim.editDestination(1,"dest 2");
		assertEquals("Destination","dest 2",claim.getDestination(1));

	}
}
