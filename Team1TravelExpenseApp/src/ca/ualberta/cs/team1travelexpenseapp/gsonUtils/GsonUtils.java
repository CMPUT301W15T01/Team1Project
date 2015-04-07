// from http://stackoverflow.com/a/22081826 (April 2, 2015)

package ca.ualberta.cs.team1travelexpenseapp.gsonUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
/**
 * Provides a Gson instance set up to deal with RunTime polymorphism of Claims, subtypes are registered in ClaimListManager class
 *
 */
public class GsonUtils {

	private static final GsonBuilder gsonBuilder = new GsonBuilder()
			.setPrettyPrinting();

	public static void registerType(RuntimeTypeAdapterFactory<?> adapter) {
		gsonBuilder.registerTypeAdapterFactory(adapter);
	}

	public static Gson getGson() {
		return gsonBuilder.create();
	}
}