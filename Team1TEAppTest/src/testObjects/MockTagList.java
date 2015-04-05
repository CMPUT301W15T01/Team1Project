package testObjects;

import ca.ualberta.cs.team1travelexpenseapp.TagList;
/**
 * Taglist with saving/loading functionality removed to simplify testing.
 * @author kenny_789
 *
 */
public class MockTagList extends TagList {
	@Override
	public void saveTags(){
		//do nothing
	}
	
	@Override
	public void loadTags(){
		//do nothing
	}
}
