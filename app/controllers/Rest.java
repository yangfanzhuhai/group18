package controllers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import models.ActivityModel;
import models.ActorModel;
import models.GitObject;
import models.JenkinsObject;
import models.MongoLink;
import models.ObjectModel;
import models.PersonActor;
import models.TargetModel;
import models.User;
import models.UserWithGroup;
import models.UsersWithGroup;
import models.git.Branch;
import models.git.Commit;
import models.git.Repository;
import play.mvc.Controller;
import play.mvc.Result;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;

public class Rest extends Controller {

	public static Result createMessage(String groupID) {
		String activityJson = getValueFromRequest("activity");

		try {
			ActivityModel activity = new ActivityModel(activityJson);
			activity.save(groupID);
			return ok();
		} catch (Exception e) {
			return status(400);
		}
	}
	
	public static Result addProject() {
		
		try {
			String Json = getValueFromRequest("activity");
			
			Gson gson = new Gson();
			UserWithGroup project = gson.fromJson(Json, UserWithGroup.class);

			MongoLink.MONGO_LINK.addNewProject(project.name, project.creator);
			return ok();
		} catch (JsonSyntaxException e) {
			return status(422);
		}
	}
	
	public static Result addUsersToProject() {
		
		try {
			String Json = getValueFromRequest("activity");
			
			Gson gson = new Gson();
			UsersWithGroup users = gson.fromJson(Json, UsersWithGroup.class);

			MongoLink.MONGO_LINK.addUsersToProject(users.id, users.users);
			return ok();
		} catch (JsonSyntaxException e) {
			return status(422);
		}
	}
	
	public static Result leaveProject() {
		try {
			String Json = getValueFromRequest("activity");
			String userName = session("connected");
			
			Gson gson = new Gson();
			UsersWithGroup users = gson.fromJson(Json, UsersWithGroup.class);

			MongoLink.MONGO_LINK.removeFromProject(users.id, userName);
			return ok();
		} catch (JsonSyntaxException e) {
			return status(422);
		}
	}
	
	public static Result deleteProject() {
		try {
			String Json = getValueFromRequest("activity");
			
			Gson gson = new Gson();
			UsersWithGroup users = gson.fromJson(Json, UsersWithGroup.class);

			MongoLink.MONGO_LINK.deleteProject(users.id);
			return ok();
		} catch (JsonSyntaxException e) {
			return status(422);
		}
	}

	public static Result getUser() {
		return ok(session("connected"));
	}
	
	public static Result getGroups(){
		String userName = session("connected");
		return ok(MongoLink.MONGO_LINK.getGroups(userName).toString());
	}

	public static Result getActivities(String groupID) {
		return ok(MongoLink.MONGO_LINK.getNewsFeed(groupID).toString());
	}
	
	public static Result getMoreActivities(String groupID, String last_post_id) {
		return ok(MongoLink.MONGO_LINK.getNextNews(groupID, last_post_id).toString());
	}
	
	public static Result getNewActivities(String groupID, String newest_post_id) {
		return ok(MongoLink.MONGO_LINK.getNewNews(groupID, newest_post_id).toString());
	}

	public static Result getTasks(String groupID) {
		return ok(MongoLink.MONGO_LINK.getTasks(groupID).toString());
	}

	public static Result getAllTasks(String groupID) throws ParseException {
		return ok(MongoLink.MONGO_LINK.getAllTasksByName(groupID).toString());
	}

	public static Result getMoreTasks(String groupID, String last_post_id) {
		return ok(MongoLink.MONGO_LINK.getNextTasks(groupID, last_post_id).toString());
	}

	public static Result getTasksWithStatus(String groupID, String status) {
		return ok(MongoLink.MONGO_LINK.getTasksWithStatus(groupID, status).toString());
	}
	
	public static Result getGits(String groupID) {
		return ok(MongoLink.MONGO_LINK.getGitCommits(groupID).toString());
	}
	
	public static Result getBuilds(String groupID) {
		return ok(MongoLink.MONGO_LINK.getJenkinsBuilds(groupID).toString());
	}

	public static Result registerUser() {
		String credentialsJson = getValueFromRequest("credentials");

		try {
			if (MongoLink.MONGO_LINK.registerNewUser((DBObject) JSON
					.parse(credentialsJson))) {
				return ok();
			} else {
				return status(400);
			}
		} catch (Exception e) {
			return status(422);
		}

	}

	public static Result loginUser() {
		String credentialsJson = getValueFromRequest("credentials");

		try {
			Gson gson = new Gson();
			User user = gson.fromJson(credentialsJson, User.class);
			String username = user.username;
			String password = user.password;
			if (MongoLink.MONGO_LINK.checkLogin(username, password)) {
				session("connected", username);
				return ok();
			} else {
				return status(400);
			}
		} catch (Exception e) {
			return status(422);
		}

	}
	
	public static Result getAllUsers() {
		return ok(MongoLink.MONGO_LINK.getUsers().toString());
	}

	public static Result parseGitHook(String groupID) {
		JsonNode json = request().body().asJson();
		ActivityModel activity = createActivityModelFromGitHook(json);
		activity.save(groupID);
		return ok();

	}


	public static Result parseJenkinsNotfication(String groupID) {
		JsonNode json = request().body().asJson();
		JsonNode build = json.findValue("build");
		String phase = getStringValueFromJson(build, "phase");
		if(phase.equals("FINISHED")){
		ActivityModel activity = createActivityModelFromJenkinsNotification(
				json, build);
		activity.save(groupID);
		}
		return ok();
	}
	
	
	public static Result updateStatusAndPriority(String groupID) {
		try {
			
			MongoLink.MONGO_LINK.updateStatusOrPriority(groupID, (DBObject) JSON.parse(getValueFromRequest("activity")));
			return ok();
			
		} catch (MongoException e) {
			return status(422);
		}
	}
	
	public static Result deletePost(String groupID) {
		
		MongoLink.MONGO_LINK.deletePost(groupID, (DBObject) JSON.parse(getValueFromRequest("activity")));
		return ok();
	}

	
	private static ActivityModel createActivityModelFromJenkinsNotification(
			JsonNode json, JsonNode build) {
		String published = createDate();
		ActorModel actor = new PersonActor("Jenkins");
		String verb = "reported";
		ObjectModel object = createJenkinsObject(json, build);
		TargetModel target = new TargetModel("", new ArrayList<String>());
		ActivityModel activity = new ActivityModel(published, actor, verb,
				object, target);
		return activity;
	}

	private static JenkinsObject createJenkinsObject(JsonNode json, JsonNode build) {
		String name = getStringValueFromJson(json, "name");
		int number = build.findValue("number").asInt();
		String status = getStringValueFromJson(build, "status");
		String url = getStringValueFromJson(build, "full_url");
		return new JenkinsObject(name, number, status, url);
	}
	
	private static ActivityModel createActivityModelFromGitHook(JsonNode json) {
		String published = createDate();

		ActorModel actor = new PersonActor(getStringValueFromJson(json,
				"user_name"));
		String verb = "pushed";
		ObjectModel object = createGitObject(json);
		TargetModel target = new TargetModel("", new ArrayList<String>());
		ActivityModel activity = new ActivityModel(published, actor, verb,
				object, target);
		return activity;
	}

	private static GitObject createGitObject(JsonNode json) {

		Repository repository = createRepository(json);
		Branch branch = createBranch(json, repository.getUrl());
		List<Commit> commits = createCommitsList(json);
		int totalCommits = json.findValue("total_commits_count").asInt();
		return new GitObject(repository, branch, commits, totalCommits);
	}

	private static Repository createRepository(JsonNode json) {
		JsonNode repositoryNode = json.findValue("repository");
		String repositoryName = getStringValueFromJson(repositoryNode, "name");
		String repositoryURL = getStringValueFromJson(repositoryNode,
				"homepage");
		return new Repository(repositoryName, repositoryURL);
	}

	private static Branch createBranch(JsonNode json, String repoURL) {
		String branchName = getBranchName(json);
		String branchURL = repoURL + "/commits/" + branchName;
		return new Branch(branchName, branchURL);
	}

	private static String getBranchName(JsonNode json) {
		String ref = getStringValueFromJson(json, "ref");
		String branchName = getSuffix(ref);
		return branchName;
	}

	private static String getSuffix(String ref) {
		String[] split = ref.split("/");
		String branchName = split[split.length - 1];
		return branchName;
	}

	private static List<Commit> createCommitsList(JsonNode json) {
		Iterator<JsonNode> iterator = json.findValue("commits").iterator();
		List<Commit> commits = new ArrayList<Commit>();
		while (iterator.hasNext()) {
			JsonNode commitNode = iterator.next();
			commits.add(createCommit(commitNode));
		}
		return commits;
	}

	private static Commit createCommit(JsonNode commitNode) {
		String message = replaceSpecialChars(getStringValueFromJson(commitNode, "message"));
		String url = getStringValueFromJson(commitNode, "url");
		JsonNode authorNode = commitNode.findValue("author");
		String author = getStringValueFromJson(authorNode, "name");
		Commit commit = new Commit(url, author, message);
		return commit;
	}
	
	private static String replaceSpecialChars(String msg) {
		return msg.replace("\n", "\\\\n").replace("\t", "\\\\t").replace("\r", "\\\\r");
	}
	
	private static String createDate() {
		Calendar rightNow = Calendar.getInstance();
		java.text.SimpleDateFormat df1 = new java.text.SimpleDateFormat(
				"d/MM/yyyy 'at' HH:mm:ss");
		return df1.format(rightNow.getTime());
	}
	
	private static String getValueFromRequest(String value) {
		final Map<String, String[]> values = request().body()
				.asFormUrlEncoded();
		String stringValue = values.get(value)[0];
		return stringValue;
	}

	private static String getStringValueFromJson(JsonNode json, String fieldName) {
		return json.findValue(fieldName).asText();
	}

}
