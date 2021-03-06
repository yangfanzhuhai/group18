package models;

import java.util.List;

import models.git.Branch;
import models.git.Commit;
import models.git.Repository;

public class GitObject extends ObjectModel {

	private Repository repository;
	private Branch branch;
	private List<Commit> commits;
	private int totalCommits;

	public GitObject(Repository repository, Branch branch,
			List<Commit> commits, int totalCommits) {
		this.repository = repository;
		this.branch = branch;
		this.commits = commits;
		this.totalCommits = totalCommits;
		this.objectType = ObjectType.GIT;
	}

	public Repository getRepository() {
		return repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public List<Commit> getCommits() {
		return commits;
	}

	public void setCommits(List<Commit> commits) {
		this.commits = commits;
	}

	public int getTotalCommits() {
		return totalCommits;
	}

	public void setTotalCommits(int totalCommits) {
		this.totalCommits = totalCommits;
	}

	@Override
	public void setType() {
		this.objectType = ObjectType.GIT;
	}


}
