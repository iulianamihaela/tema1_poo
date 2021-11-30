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

	public Integer getRelease_year() {
		return year;
	}

	public ArrayList<String> getGenres() {
		return genres;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setRelease_year(Integer release_year) {
		this.year = release_year;
	}

	public void setGenres(ArrayList<String> genres) {
		this.genres = genres;
	}
}