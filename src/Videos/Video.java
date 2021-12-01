package Videos;

import java.util.ArrayList;

public class Video {
	private String title;
	private Integer year;
	private ArrayList<String> cast;
	private ArrayList<String> genres;

	public Video(final String title, final int year,
	             final ArrayList<String> cast, final ArrayList<String> genres) {
		this.title = title;
		this.year = year;
		this.cast = cast;
		this.genres = genres;
	}

	public String getTitle() {
		return title;
	}

	public ArrayList<String> getGenres() {
		return genres;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setGenres(ArrayList<String> genres) {
		this.genres = genres;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public ArrayList<String> getCast() {
		return cast;
	}

	public void setCast(ArrayList<String> cast) {
		this.cast = cast;
	}

	public double getRating() {
		return 0;
	};

	public int getDuration() {
		return 0;
	}
}