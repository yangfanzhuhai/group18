package models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

public class PersonActorTest {
	
	private static final String DISPLAY_NAME = "display name";

	@Test
	public void testToJson() throws JSONException {
		PersonActor personActor = new PersonActor(DISPLAY_NAME);

		String expectedJSON = "{\"objectType\" : \"PERSON\", \"displayName\" : \""
				+ DISPLAY_NAME + "\"}";
		String actualJSON = personActor.toJSON();
		assertEquals(expectedJSON, actualJSON);
		try {
			new JSONObject(actualJSON);
		} catch (JSONException e) {
			fail("PersonActor toJSON function does not return a string as a valid JSON structure");
		}
	}

}
