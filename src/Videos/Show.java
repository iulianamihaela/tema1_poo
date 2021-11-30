package Videos;

import entertainment.Season;
import fileio.SerialInputData;

import java.util.ArrayList;

public class Show extends Video {
	private int numberOfSeasons;
	private ArrayList<Season> seasons;

	public Show(SerialInputData serialInputData) {
		super(serialInputData.getTitle(), serialInputData.getYear(), serialInputData.getCast(), serialInputData.getGenres());
		this.numberOfSeasons = serialInputData.getNumberSeason();
		this.seasons = serialInputData.getSeasons();
	}

	public int getNumberOfSeasons() {
		return numberOfSeasons;
	}

	public void setNumberOfSeasons(int numberOfSeasons) {
		this.numberOfSeasons = numberOfSeasons;
	}

	public ArrayList<Season> getSeasons() {
		return seasons;
	}

	public void setSeasons(ArrayList<Season> seasons) {
		this.seasons = seasons;
	}

	public void addRating(Integer season, Double rating) {
		this.seasons.get(season - 1).getRatings().add(rating);
	}
}
