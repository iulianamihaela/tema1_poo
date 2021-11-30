package main;

import java.util.ArrayList;

public class VideosDB {
	private Database database;
	private ArrayList<Action> actions;

	public VideosDB() {
		this.database = new Database();
		this.actions = new ArrayList<Action>();
	}

	public ArrayList<Action> getActions() {
		return actions;
	}

	public void setActions(ArrayList<Action> actions) {
		this.actions = actions;
	}

	public Database getDatabase() {
		return database;
	}

	public void setDatabase(Database database) {
		this.database = database;
	}

}
