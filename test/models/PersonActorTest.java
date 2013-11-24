package models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import com.google.gson.Gson;

public class PersonActorTest {

	private static final String DISPLAY_NAME = "display name";
	private static final String USERNAME = "username";
	private static final String PHOTO_URL = "photo_url";

	@Test
	public void testToJson() throws JSONException {
		Gson gson = new Gson();
		PersonActor personActor = new PersonActor(DISPLAY_NAME, USERNAME,
				PHOTO_URL);

		String expectedJSON = "{\"username\":\"" + USERNAME
				+ "\",\"photo_url\":\"" + PHOTO_URL + "\",\"displayName\":\""
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
