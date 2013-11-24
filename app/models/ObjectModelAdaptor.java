package models;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

public class ObjectModelAdaptor implements JsonDeserializer<ObjectModel>{

	@Override
	public ObjectModel deserialize(JsonElement json, Type typeOfT,
	        JsonDeserializationContext context) throws JsonParseException  {
	    JsonObject jsonObject =  json.getAsJsonObject();
	    JsonPrimitive prim = (JsonPrimitive) jsonObject.get("objectType");
	    String objectType = prim.getAsString();

	    Class<?> klass = null;
	    switch (ObjectType.valueOf(objectType)) {
	    case MESSAGE:
	    	klass = MessageObject.class;
			break;
		case TASK:
			klass = TaskObject.class;
			break;
		case GIT:
			klass = GitObject.class;
			break;
		case JENKINS:
			klass = JenkinsObject.class;
			break;
		}

	    return context.deserialize(jsonObject, klass);
	}
	
}