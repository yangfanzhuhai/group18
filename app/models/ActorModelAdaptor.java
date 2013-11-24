package models;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

public class ActorModelAdaptor implements JsonDeserializer<ActorModel>{

	@Override
	public ActorModel deserialize(JsonElement json, Type typeOfT,
	        JsonDeserializationContext context) throws JsonParseException  {
	    JsonObject jsonObject =  json.getAsJsonObject();
	    JsonPrimitive prim = (JsonPrimitive) jsonObject.get("objectType");
	    String objectType = prim.getAsString();

	    Class<?> klass = null;
	    switch (ActorType.valueOf(objectType)) {
		case PERSON:
			klass = PersonActor.class;
	    case JENKINS:
	    	klass = JenkinsActor.class;
		}
	    return context.deserialize(jsonObject, klass);
	}
	
}