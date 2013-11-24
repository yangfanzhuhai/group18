package models;

public class JenkinsActor extends ActorModel {

	
	public JenkinsActor() {
		super("Jenkins");
		objectType = ActorType.JENKINS;
	}

	@Override
	public void setType() {
		objectType = ActorType.JENKINS;
	}

}
