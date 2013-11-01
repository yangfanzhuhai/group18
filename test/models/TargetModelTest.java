package models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

public class TargetModelTest {
	
	private static final String MESSAGE_ID = "message id";
	private static final String TASK_ID = "task id";
	private static final List<String> TASK_IDS = new ArrayList<String>(
		    Arrays.asList(TASK_ID)); 
	
	@Test
	public void testToJson() throws JSONException {
		TargetModel targetModel = new TargetModel(MESSAGE_ID, TASK_IDS);

		String expectedJSON = "{\"messageID\" : \"" + MESSAGE_ID + "\", \"taskIDs\" : ["
		        + "\"" + TASK_ID + "\"]}";
		String actualJSON = targetModel.toJSON();
		assertEquals(expectedJSON, actualJSON);
		try {
			new JSONObject(actualJSON);
		} catch (JSONException e) {
			fail("TargetModel toJSON function does not return a string as a valid JSON structure");
		}
	}
		

}
