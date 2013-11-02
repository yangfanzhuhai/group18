package controllers;

import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import models.ActivityModel;
import models.ActorModel;
import models.GitObject;
import models.MongoLink;
import models.ObjectModel;
import models.PersonActor;
import models.TargetModel;
import models.User;
import models.git.Branch;
import models.git.Commit;
import models.git.Repository;
import play.mvc.Controller;
import play.mvc.Result;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class Rest extends Controller {

	public static Result createMessage() {
		String activityJson = getValueFromRequest("activity");

		try {
			ActivityModel activity = new ActivityModel(activityJson);
			activity.save();
			return ok();
		} catch (Exception e) {
			return status(400);
		}
	}

	public static Result getUser() {
		return ok(session("connected"));
	}

	public static Result getActivities() throws UnknownHostException {
		return ok(MongoLink.MONGO_LINK.getNewsFeed().toString());
	}

	public static Result getTasks() throws UnknownHostException {
		return ok(MongoLink.MONGO_LINK.getTasks().toString());
	}

	public static Result getAllTasks() throws UnknownHostException,
			ParseException {
		return ok(MongoLink.MONGO_LINK.getAllTasksWithoutReplies().toString());
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

	public static Result parseGitHook() {
		JsonNode json = request().body().asJson();
		String published = createDate();

		ActorModel actor = new PersonActor(getStringValueFromJson(json,
				"user_name"));
		String verb = "pushed";
		ObjectModel object = createGitObject(json);
		TargetModel target = new TargetModel("", new ArrayList<String>());
		ActivityModel activity = new ActivityModel(published, actor, verb,
				object, target);
		activity.save();
		return ok();

	}

	private static String createDate() {
		Calendar rightNow = Calendar.getInstance();
		java.text.SimpleDateFormat df1 = new java.text.SimpleDateFormat(
				"d/MM/yyyy 'at' HH:mm:ss");
		return df1.format(rightNow.getTime());
	}

	private static String getLeadingZero(String number) {
		if (number.length() < 2) {
			return "0" + number;
		} else {
			return number;
		}
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
		String message = getStringValueFromJson(commitNode, "message");
		String url = getStringValueFromJson(commitNode, "url");
		JsonNode authorNode = commitNode.findValue("author");
		String author = getStringValueFromJson(authorNode, "name");
		Commit commit = new Commit(url, author, message);
		return commit;
	}

	public static Result parseJenkinsNotfication() {
		JsonNode json = request().body().asJson();
		System.out.println(json.toString());
		return TODO;
	}

	/**
	 * @return
	 */
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
