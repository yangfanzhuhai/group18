package models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import com.google.gson.Gson;

public class MessageObjectTest {

	private static final String MESSAGE = "test message";

	@Test
	public void testToJson() throws JSONException {
		Gson gson = new Gson();
		MessageObject messageObject = new MessageObject(MESSAGE);
		messageObject.setType();

		String expectedJSON = "{\"message\":\""
				+ MESSAGE + "\",\"objectType\":\"MESSAGE\"}";
		String actualJSON = gson.toJson(messageObject);
		assertEquals(expectedJSON, actualJSON);
		try {
			new JSONObject(actualJSON);
		} catch (JSONException e) {
			fail("MessageObject toJSON function does not return a string as a valid JSON structure");
		}
	}

}
