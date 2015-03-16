package ca.ualberta.cs.team1travelexpenseapp;
/**
 * Listener interface, listeners will be placed in views and passed to model objects to allow the model to call update in the listener,
 * the listener then updates the necessary elements of the views in the update method.
 *
 */
public interface Listener {
	public void update();	
}
