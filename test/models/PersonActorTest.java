package models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import com.google.gson.Gson;

public class PersonActorTest {
	
	private static final String DISPLAY_NAME = "display name";

	@Test
	public void testToJson() throws JSONException {
		Gson gson = new Gson();
		PersonActor personActor = new PersonActor(DISPLAY_NAME);

		String expectedJSON = "{\"displayName\":\""
				+ DISPLAY_NAME + "\",\"objectType\":\"PERSON\"}";
		String actualJSON = gson.toJson(personActor);
		assertEquals(expectedJSON, actualJSON);
		try {
			new JSONObject(actualJSON);
		} catch (JSONException e) {
			fail("PersonActor toJSON function does not return a string as a valid JSON structure");
		}
	}

}
