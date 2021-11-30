package main;

import Users.User;
import Videos.Movie;
import Videos.Show;
import common.Constants;
import fileio.Writer;
import org.json.simple.JSONArray;
import utils.Utils;

import java.io.IOException;
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

	public JSONArray executeActions(Writer writer) {
		JSONArray results = new JSONArray();

		for (Action action : this.actions) {
			String message = executeAction(action);

			try {
				results.add(writer.writeFile(action.getActionId(), "", message));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return results;
	}

	public String executeAction(Action action) {
		if (action.getActionType().equals(Constants.COMMAND)) {
			if (action.getType().equals(Constants.FAVORITE)) {
				return addFavoriteTitleForUser(action.getUsername(), action.getTitle());
			}
			else if (action.getType().equals(Constants.VIEW)) {
				return markTitleAsViewedForUser(action.getUsername(), action.getTitle());
			}
			else if (action.getType().equals(Constants.RATING)) {
				if (action.getSeasonNumber() == 0) {
					return rateMovie(action.getUsername(), action.getTitle(), action.getGrade());
				} else {
					return rateShow(action.getUsername(), action.getTitle(), action.getSeasonNumber(), action.getGrade());
				}
			}
		}

		return Utils.Error("eroare din check");
	}

	public String addFavoriteTitleForUser(String username, String title) {
		User user = database.getUserByUsername(username);

		if (user == null) {
			return Utils.Error("User doesn't exist.");
		}

		if (user.getFavoriteMovies().contains(title)) {
			return Utils.Error(title + " is already in favourite list");
		}

		if (user.getHistory().containsKey(title)) {
			user.getFavoriteMovies().add(title);
			return Utils.Success(title + " was added as favourite");
		} else {
			return Utils.Error(title + " is not seen");
		}
	}

	public String markTitleAsViewedForUser(String username, String title) {
		User user = database.getUserByUsername(username);

		if (user == null) {
			return Utils.Error("User doesn't exist.");
		}

		if (user.getHistory().containsKey(title)) {
			int noViews = user.getHistory().get(title);

			user.getHistory().replace(title, noViews + 1);

			return Utils.Success(title + " was viewed with total views of " + noViews + 1);
		} else {
			user.getHistory().put(title, 1);

			return Utils.Success(title + " was viewed with total views of 1");
		}
	}

	public String rateMovie(String username, String title, Double rating) {
		User user = database.getUserByUsername(username);
		Movie movie = database.getMovieByTitle(title);

		if (user == null) {
			return Utils.Error("User doesn't exist.");
		}

		if (movie == null) {
			return Utils.Error("Movie doesn't exist.");
		}

		if (!user.getHistory().containsKey(title)) {
			return Utils.Error(title + " is not seen");
		}

		movie.addRating(username, rating);

		return Utils.Success(title + " was rated with " + rating + " by " + username);
	}

	public String rateShow(String username, String title, Integer season, Double rating) {
		User user = database.getUserByUsername(username);
		Show show = database.getShowByTitle(title);

		if (user == null) {
			return Utils.Error("User doesn't exist.");
		}

		if (show == null) {
			return Utils.Error("Show doesn't exist.");
		}

		if (!user.getHistory().containsKey(title)) {
			return Utils.Error(title + " is not seen");
		}

		show.addRating(season, rating);
		return Utils.Success(title + " was rated with " + rating + " by " + username);
	}
}
