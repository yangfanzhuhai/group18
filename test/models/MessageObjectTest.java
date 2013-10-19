package models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

public class MessageObjectTest {

	private static final String MESSAGE = "test message";

	@Test
	public void testToJson() throws JSONException {
		MessageObject messageObject = new MessageObject();
		messageObject.setMessage(MESSAGE);

		String expectedJSON = "{\"objectType\" : \"MESSAGE\", \"message\" : \""
				+ MESSAGE + "\"}";
		String actualJSON = messageObject.toJSON();
		assertEquals(expectedJSON, actualJSON);
		try {
			new JSONObject(actualJSON);
		} catch (JSONException e) {
			fail("MessageObject toJSON function does not return a string as a valid JSON structure");
		}
	}

}
