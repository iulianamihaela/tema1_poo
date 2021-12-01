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

	@Override
	public double getRating() {
		if (this.ratings.size() == 0) {
			return 0;
		}

		double sum = 0;

		for (Double x : this.ratings.values()) {
			sum += x;
		}

		return sum / this.ratings.size();
	}

	@Override
	public int getDuration() {
		return this.duration;
	}
}
