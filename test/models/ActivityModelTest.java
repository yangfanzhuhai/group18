package models;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

public class ActivityModelTest {

  private static final String ACTOR_JSON = "{\"actor\" : \"json\"}";
  private static final String OBJECT_JSON = "{\"object\" : \"json\"}";
  private static final String TARGET_JSON = "{\"target\" : \"json\"}";
  private static final Date PUBLISHED_DATE = new Date();
  private static final String VERB_STRING = "verb";

  @Test
  public void testToJson() {

    ActorModel mockActor = createMock(ActorModel.class);
    expect(mockActor.toJSON()).andReturn(ACTOR_JSON);
    replay(mockActor);
    ObjectModel mockObject = createMock(ObjectModel.class);
    expect(mockObject.toJSON()).andReturn(OBJECT_JSON);
    replay(mockObject);
    TargetModel mockTarget = createMock(TargetModel.class);
    expect(mockTarget.toJSON()).andReturn(TARGET_JSON);
    replay(mockTarget);

    ActivityModel activityModel = new ActivityModel(PUBLISHED_DATE, mockActor,
        VERB_STRING, mockObject, mockTarget);

    String expectedJSON = "{\"published\" : \"" + PUBLISHED_DATE
        + "\", \"actor\" : " + ACTOR_JSON + ", \"verb\" : \"" + VERB_STRING
        + "\", \"object\" : " + OBJECT_JSON + ", \"target\" : " + TARGET_JSON
        + " }";
    String actualJSON = activityModel.toJSON();
    assertEquals(expectedJSON, actualJSON);
    try {
      new JSONObject(actualJSON);
    } catch (JSONException e) {
      fail("ActivityModel toJSON function does not return a string as a valid JSON structure");
    }

  }

}
