package Videos;

import entertainment.Season;
import fileio.SerialInputData;

import java.util.ArrayList;
import java.util.HashMap;

public class Show extends Video {
	private int numberOfSeasons;
	private ArrayList<Season> seasons;

	private ArrayList<HashMap<String, Double>> seasonsRatings;

	public Show(SerialInputData serialInputData) {
		super(serialInputData.getTitle(), serialInputData.getYear(), serialInputData.getCast(), serialInputData.getGenres());
		this.numberOfSeasons = serialInputData.getNumberSeason();
		this.seasons = serialInputData.getSeasons();
		this.seasonsRatings = new ArrayList<>();

		for (int i = 0; i < this.numberOfSeasons; i++) {
			this.seasonsRatings.add(new HashMap<>());
		}
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

	public ArrayList<HashMap<String, Double>> getSeasonsRatings() {
		return seasonsRatings;
	}

	public void addRating(String username, Integer season, Double rating) {
		this.seasons.get(season - 1).getRatings().add(rating);
		this.seasonsRatings.get(season - 1).put(username, rating);
	}

	@Override
	public double getRating() {
		double sumRating = 0;

		for (int i = 0; i < this.numberOfSeasons; i++) {
			HashMap<String, Double> seasonI = this.seasonsRatings.get(i);

			if (seasonI.size() == 0) {
				continue;
			}

			double ratingSeasonI = 0;

			for (Double x : seasonI.values()) {
				ratingSeasonI += x;
			}

			ratingSeasonI /= seasonI.size();

			sumRating += ratingSeasonI;
		}

		return sumRating / (double)this.numberOfSeasons;
	}

	@Override
	public int getDuration() {
		int duration = 0;

		for (Season season : this.seasons) {
			duration += season.getDuration();
		}

		return duration;
	}
}
