package Videos;

import entertainment.Genre;
import fileio.MovieInputData;

import java.util.List;

public class Movie extends Video {
	private int duration;
	private int rating;

	public Movie(MovieInputData movieInputData) {
		super(movieInputData.getTitle(), movieInputData.getYear(), movieInputData.getCast(), movieInputData.getGenres());
		this.rating = 0;
		this.duration = movieInputData.getDuration();
	}
}
