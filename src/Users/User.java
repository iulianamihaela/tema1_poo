package Users;

import Videos.Video;
import entertainment.Genre;
import fileio.UserInputData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class User {
	private String username;
	private String subscriptionType;
	private Map<String, Integer> history;
	private ArrayList<String> favoriteMovies;

	public User(UserInputData userInputData) {
		this.username = userInputData.getUsername();
		this.subscriptionType = userInputData.getSubscriptionType();
		this.history = userInputData.getHistory();
		this.favoriteMovies = userInputData.getFavoriteMovies();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSubscriptionType() {
		return subscriptionType;
	}

	public void setSubscriptionType(String subscriptionType) {
		this.subscriptionType = subscriptionType;
	}

	public Map<String, Integer> getHistory() {
		return history;
	}

	public void setHistory(Map<String, Integer> history) {
		this.history = history;
	}

	public ArrayList<String> getFavoriteMovies() {
		return favoriteMovies;
	}

	public void setFavoriteMovies(ArrayList<String> favoriteMovies) {
		this.favoriteMovies = favoriteMovies;
	}
}
