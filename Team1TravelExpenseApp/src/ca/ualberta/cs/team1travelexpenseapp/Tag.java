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
/**
 * Implements the Tags which will be added to Claims in order to provide a way of filtering specific types of Claim.
 * Very simple class which contains only a name that can be changed.
 *
 */
public class Tag {
	private String name;
	
	/**
	 * Create a new Tag with a specific name.
	 * @param name A String to be used as the name of the new tag.
	 */
	public Tag(String name){
		this.name=name;
	}
	
	/**
	 * Get the current name of the Tag.
	 * @return The name of the Tag.
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * return a string representation of the Tag (currently just the name).
	 */
	@Override
	public String toString() {
		return this.getName();
	}
	/**
	 * Change the name of the tag to newName.
	 * @param newName A String to be used as the new name for the Tag
	 */
	public void setName(String newName){
		this.name=newName;
	}
}
