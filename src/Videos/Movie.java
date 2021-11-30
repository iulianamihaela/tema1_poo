package Videos;

import entertainment.Genre;
import fileio.MovieInputData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Movie extends Video {
	private int duration;
	private Map<String, Double> ratings;

	public Movie(MovieInputData movieInputData) {
		super(movieInputData.getTitle(), movieInputData.getYear(), movieInputData.getCast(), movieInputData.getGenres());

		this.duration = movieInputData.getDuration();
		this.ratings = new HashMap<>();
	}

	public Map<String, Double> getRatings() {
		return this.ratings;
	}

	public void addRating(String username, Double rating) {
		this.ratings.put(username, rating);
	}
}
