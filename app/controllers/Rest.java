package controllers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mindrot.jbcrypt.BCrypt;

import models.ActivityModel;
import models.ActorModel;
import models.FBAccount;
import models.FBImage;
import models.GHAccount;
import models.GitObject;
import models.ImageObject;
import models.JenkinsActor;
import models.JenkinsObject;
import models.LocalAccount;
import models.ObjectModel;
import models.PersonActor;
import models.TargetModel;
import models.TaskID;
import models.User;
import models.UserModel;
import models.GroupWithCreator;
import models.GroupWithUser;
import models.authentication.LoginAttempt;
import models.authentication.Session;
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

import controllers.db.MongoLink;

public class Rest extends Controller {

	public static Result createMessage(String groupID) {
		String activityJson = getValueFromRequest("activity");

		try {
			ActivityModel activity = ActivityModel.activityModelGson.fromJson(
					activityJson, ActivityModel.class);
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
			GroupWithCreator project = gson.fromJson(Json,
					GroupWithCreator.class);

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
			GroupWithUser userGroup = gson.fromJson(Json, GroupWithUser.class);

			MongoLink.MONGO_LINK.addUsersToProject(userGroup.id,
					userGroup.username);
			return ok();
		} catch (JsonSyntaxException e) {
			return status(422);
		}
	}

	public static Result leaveProject() {
		try {
			String Json = getValueFromRequest("activity");
			String userName = getUsernameFromSession();

			Gson gson = new Gson();
			GroupWithUser users = gson.fromJson(Json, GroupWithUser.class);

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
			GroupWithUser users = gson.fromJson(Json, GroupWithUser.class);

			MongoLink.MONGO_LINK.deleteProject(users.id);
			return ok();
		} catch (JsonSyntaxException e) {
			return status(422);
		}
	}

	public static Result getUser() {
		return ok(getUsernameFromSession());
	}

	public static String getUsernameFromSession() {
		String token = session("token");
		Session session;
		try {
			session = Session.findSessionFromToken(token);
			return session.getUsername();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static Result getGroups() {
		String userName = getUsernameFromSession();
		return ok(MongoLink.MONGO_LINK.getGroups(userName).toString());
	}

	public static Result parseGitHubData() {
		JsonNode json = request().body().asJson();
		System.out.println(json.toString());

		String gravatar_id = getStringValueFromJson(json, "gravatar_id");
		String html_url = getStringValueFromJson(json, "html_url");
		System.out.println(gravatar_id);
		GHAccount ghAccount = new GHAccount(gravatar_id, html_url);
		UserModel userModel = new UserModel(ghAccount);

		MongoLink.MONGO_LINK.registerOrLogin((DBObject) JSON.parse(userModel
				.toJSON()));

		session("connected", "gitaccount");

		return ok();
	}

	public static Result parseFBData() {

		String fbAccountJson = getValueFromRequest("credentials");
		System.out.println(fbAccountJson);
		Gson gson = new Gson();
		FBAccount fbAccount = gson.fromJson(fbAccountJson, FBAccount.class);
		UserModel userModel = new UserModel(fbAccount);

		MongoLink.MONGO_LINK.registerOrLogin((DBObject) JSON.parse(userModel
				.toJSON()));

		session("connected", "fbaccount");

		return ok();
	}

	public static Result addFBImage() {
		Gson gson = new Gson();
		FBImage url = gson.fromJson(getValueFromRequest("activity"),
				FBImage.class);
		MongoLink.MONGO_LINK.addFBImage(session("connected"), url.fb_image);
		return ok();
	}

	public static Result getActivities(String groupID) {
		return ok(MongoLink.MONGO_LINK.getNewsFeed(groupID).toString());
	}

	public static Result getMoreActivities(String groupID, String last_post_id) {
		return ok(MongoLink.MONGO_LINK.getNextNews(groupID, last_post_id)
				.toString());
	}

	public static Result getNewActivities(String groupID, String newest_post_id)
			throws ParseException {
		return ok(MongoLink.MONGO_LINK.getNewNews(groupID, newest_post_id)
				.toString());
	}

	public static Result getTasks(String groupID) {
		return ok(MongoLink.MONGO_LINK.getTasks(groupID).toString());
	}

	public static Result getTaskDetails(String groupID, String ref) {
		try {
			/*
			 * Gson gson = new Gson(); TaskID task =
			 * gson.fromJson(getValueFromRequest("activity"), TaskID.class);
			 */
			return ok(MongoLink.MONGO_LINK.getTaskDetails(groupID, ref)
					.toString());
		} catch (JsonSyntaxException e) {
			return status(422);
		} catch (ParseException e) {
			return status(422);
		}
	}

	public static Result getAllTasks(String groupID) throws ParseException {
		return ok(MongoLink.MONGO_LINK.getAllTasksByName(groupID).toString());
	}
	
	public static Result getNewTasks(String groupID, String newestID) {
		try {
			return ok(MongoLink.MONGO_LINK.getNewTasks(groupID, newestID).toString());
		} catch (ParseException e) {
			return status(422);
		}
	}
	
	public static Result getNewTasksWithStatus(String groupID, String status, String newestID) {
		try {
			return ok(MongoLink.MONGO_LINK.getNewTasksWithStatus(groupID, status, newestID).toString());
		} catch (ParseException e) {
			return status(422);
		}
	}

	public static Result getMoreTasks(String groupID, String last_post_id) {
		return ok(MongoLink.MONGO_LINK.getNextTasks(groupID, last_post_id)
				.toString());
	}
	
	public static Result getMoreTasksWithStatus(String groupID, String status, String oldestID) {
		return ok(MongoLink.MONGO_LINK.getNextTasksWithStatus(groupID, status, oldestID).toString());
	}

	public static Result getTasksWithStatus(String groupID, String status) {
		return ok(MongoLink.MONGO_LINK.getTasksWithStatus(groupID, status)
				.toString());
	}

	public static Result getGits(String groupID) {
		return ok(MongoLink.MONGO_LINK.getGitCommits(groupID).toString());
	}
	
	public static Result getNewGits(String groupID, String newestID) {
		try {
			return ok(MongoLink.MONGO_LINK.getNewGits(groupID, newestID).toString());
		} catch (ParseException e) {
			return status(422);
		}
	}

	public static Result getMoreGits(String groupID, String last_post_id) {
		return ok(MongoLink.MONGO_LINK.getNextGitCommits(groupID, last_post_id)
				.toString());
	}

	public static Result getBuilds(String groupID) {
		return ok(MongoLink.MONGO_LINK.getJenkinsBuilds(groupID).toString());
	}
	
	public static Result getNewBuilds(String groupID, String newestID) {
		try {
			return ok(MongoLink.MONGO_LINK.getNewBuilds(groupID, newestID).toString());
		} catch (ParseException e) {
			return status(422);
		}
	}

	public static Result getMoreBuilds(String groupID, String last_post_id) {
		return ok(MongoLink.MONGO_LINK.getNextJenkinsBuilds(groupID,
				last_post_id).toString());
	}

	public static Result registerUser() {
		Gson gson = new Gson();
		UserModel userModel = gson.fromJson(getValueFromRequest("credentials"),
				UserModel.class);
		LocalAccount localAccount = userModel.getLocalAccount();
		String password = localAccount.getPassword();
		localAccount.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
		// localAccount.setPassword(password);
		String credentialsJson = userModel.toJSON();

		try {
			if (MongoLink.MONGO_LINK.registerOrLogin((DBObject) JSON
					.parse(credentialsJson))) {
				String ipAddress = request().remoteAddress();
				LoginAttempt attempt = new LoginAttempt(
						userModel.getUsername(), password, ipAddress);
				Session currentSession = attempt.getSession();
				session("token", currentSession.getToken());
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
			String ipAddress = request().remoteAddress();

			LoginAttempt attempt = new LoginAttempt(username, password,
					ipAddress);
			Session currentSession = attempt.getSession();

			session("token", currentSession.getToken());
			return ok();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return status(422);
		}
	}

	public static Result getAllUsers() {
		return ok(MongoLink.MONGO_LINK.getUsers().toString());
	}

	public static Result handleUserUpload() {
		JsonNode json = request().body().asJson();
		String event = getStringValueFromJson(json, "event");
		if (event.equals("file-processed")) {
			JsonNode userData = json.findValue("data");
			ActivityModel activity = createActvityModelFromImageUpload(json,
					userData);
			activity.save(getStringValueFromJson(userData, "groupID"));
		}
		return ok();
	}

	private static ActivityModel createActvityModelFromImageUpload(
			JsonNode json, JsonNode userData) {
		String published = createDate();
		ActorModel actor = createPersonActorFromUser(getStringValueFromJson(
				userData, "username"));
		String verb = "uploaded";
		ObjectModel object = createImageObject(json);
		TargetModel target = new TargetModel("", new ArrayList<String>());
		ActivityModel activity = new ActivityModel(published, actor, verb,
				object, target);
		return activity;
	}

	private static ActorModel createPersonActorFromUser(String username) {
		UserModel user = MongoLink.MONGO_LINK.getUserFromUsername(username);
		LocalAccount local_user = user.getLocalAccount();

		ActorModel actor = new PersonActor(local_user.getName(),
				user.getUsername(), local_user.getPhoto_url());
		return actor;
	}

	private static ObjectModel createImageObject(JsonNode json) {
		JsonNode derivatives = json.findValue("derivatives");
		String conversions_root = getStringValueFromJson(derivatives,
				"conversions_root");
		String web_preview = getStringValueFromJson(derivatives, "WEB_PREVIEW");
		String web_preview_url = createS3URL(conversions_root, web_preview);
		String original_root = getStringValueFromJson(json, "original_root");
		String original = getStringValueFromJson(json, "original");
		String original_url = createS3URL(original_root, original);
		ObjectModel object = new ImageObject(original_url, web_preview_url);
		return object;
	}

	private static String createS3URL(String root, String path) {
		return "http://" + root + ".s3.amazonaws.com" + path;
	}

	public static Result parseGitHook(String groupID) {
		JsonNode json = request().body().asJson();
		ActivityModel activity = createActivityModelFromGitHook(json, groupID);
		activity.save(groupID);
		return ok();

	}

	public static Result parseJenkinsNotfication(String groupID) {
		JsonNode json = request().body().asJson();
		JsonNode build = json.findValue("build");
		String phase = getStringValueFromJson(build, "phase");
		if (phase.equals("FINISHED")) {
			ActivityModel activity = createActivityModelFromJenkinsNotification(
					json, build);
			activity.save(groupID);
		}
		return ok();
	}

	public static Result updateStatusAndPriority(String groupID) {
		try {

			MongoLink.MONGO_LINK.updateStatusOrPriority(groupID,
					(DBObject) JSON.parse(getValueFromRequest("activity")));
			return ok();

		} catch (MongoException e) {
			return status(422);
		}
	}

	public static Result deletePost(String groupID) {

		MongoLink.MONGO_LINK.deletePost(groupID,
				(DBObject) JSON.parse(getValueFromRequest("activity")));
		return ok();
	}

	private static ActivityModel createActivityModelFromJenkinsNotification(
			JsonNode json, JsonNode build) {
		String published = createDate();
		ActorModel actor = new JenkinsActor();
		String verb = "reported";
		ObjectModel object = createJenkinsObject(json, build);
		TargetModel target = new TargetModel("", new ArrayList<String>());
		ActivityModel activity = new ActivityModel(published, actor, verb,
				object, target);
		return activity;
	}

	private static JenkinsObject createJenkinsObject(JsonNode json,
			JsonNode build) {
		String name = getStringValueFromJson(json, "name");
		int number = build.findValue("number").asInt();
		String status = getStringValueFromJson(build, "status");
		String url = getStringValueFromJson(build, "full_url");
		return new JenkinsObject(name, number, status, url);
	}

	private static ActivityModel createActivityModelFromGitHook(JsonNode json,
			String groupID) {
		String published = createDate();

		ActorModel actor = new PersonActor(getStringValueFromJson(json,
				"user_name"), "", "");
		String verb = "pushed";
		ObjectModel object = createGitObject(json);
		TargetModel target = new TargetModel("", findReferencedTasksInCommits(
				json, groupID));
		ActivityModel activity = new ActivityModel(published, actor, verb,
				object, target);
		return activity;
	}

	private static List<String> findReferencedTasksInCommits(JsonNode json,
			String groupID) {
		Iterator<JsonNode> iterator = json.findValue("commits").iterator();
		List<String> referencedTasks = new ArrayList<String>();
		while (iterator.hasNext()) {
			JsonNode commitNode = iterator.next();
			String message = getStringValueFromJson(commitNode, "message");
			Pattern p = Pattern.compile("#[^#]*#");
			Matcher m = p.matcher(message);
			if (m.find()) {
				String alias = m.group().substring(1, m.group().length() - 1);
				String taskID = MongoLink.MONGO_LINK.getTaskIDFromAlias(
						groupID, alias);
				if (taskID != null) {
					referencedTasks.add(taskID);
					Pattern p2 = Pattern.compile("@(TO_DO|DOING|DONE)@");
					Matcher m2 = p2.matcher(message);
					if (m2.find()) {
						String status = m2.group().substring(1, m2.group().length() - 1);
						MongoLink.MONGO_LINK.changeTaskStatus(groupID, taskID, status);
					}
					
				}
			}
		}
		return referencedTasks;
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
		String message = replaceSpecialChars(getStringValueFromJson(commitNode,
				"message"));
		String url = getStringValueFromJson(commitNode, "url");
		JsonNode authorNode = commitNode.findValue("author");
		String author = getStringValueFromJson(authorNode, "name");
		Commit commit = new Commit(url, author, message);
		return commit;
	}

	private static String replaceSpecialChars(String msg) {
		return msg.replace("\n", "\\\\n").replace("\t", "\\\\t")
				.replace("\r", "\\\\r");
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
